package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedPackageSliderContraption;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.level.BlockEvent;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.cannons.InteractableCannonBlock;

import javax.annotation.Nonnull;

public interface PackageSliderBlock extends CannonContraptionProviderBlock, InteractableCannonBlock, IWrenchable {

	PackageSliderMaterial getCannonMaterial();

	@Override
	Direction getFacing(BlockState state);

	PackageSliderEnd getDefaultOpeningType();

	default PackageSliderEnd getOpeningType(BlockState state) {
		return this.getDefaultOpeningType();
	}

	@Override
	default boolean isComplete(BlockState state) {
		return true;
	}

	/** Whether this block closes its end of the cannon, i.e. is the breech. */
	default boolean closesCannonEnd() {
		return this.getDefaultOpeningType() != PackageSliderEnd.OPEN;
	}

	/** Whether this block may be placed by the placement assist while held. */
	default boolean supportsPlacementAssist() {
		return true;
	}

	/** Whether this block counts as the cannon's single breech for the placement assist. */
	default boolean isPlacementAssistBreech() {
		return this.closesCannonEnd();
	}

	/**
	 * Orientation of a block placed by the placement assist.
	 *
	 * @param state        the default state of the block being placed
	 * @param placementDir the direction the cannon is being extended in
	 * @param cannonFacing {@link #getFacing} of the block the new one will sit against
	 */
	default BlockState getAssistedPlacementState(BlockState state, Direction placementDir, Direction cannonFacing) {
		return state.setValue(DirectionalBlock.FACING, cannonFacing);
	}

	/** Drops the loaded package and disconnects the neighbours. Only for player breaking, not contraption assembly. */
	default void playerWillDestroyPackageSlider(Level level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof IPackageSliderBlockEntity cbe) {
			ItemStack loaded = cbe.cannonBehavior().removeLoaded();
			if (!loaded.isEmpty())
				Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, loaded);
		}
		this.disconnectPackageSlider(level, pos, state);
	}

	/** Disconnects this block from both of its neighbours along the cannon axis. */
	default void disconnectPackageSlider(Level level, BlockPos pos, BlockState state) {
		IPackageSliderBlockEntity self = level.getBlockEntity(pos) instanceof IPackageSliderBlockEntity cbe ? cbe : null;
		Direction facing = this.getFacing(state);
		for (Direction dir : new Direction[] { facing, facing.getOpposite() }) {
			if (self != null) self.cannonBehavior().setConnectedFace(dir, false);
			if (level.getBlockEntity(pos.relative(dir)) instanceof IPackageSliderBlockEntity neighbour) {
				neighbour.cannonBehavior().setConnectedFace(dir.getOpposite(), false);
				neighbour.cannonBehavior().blockEntity.notifyUpdate();
			}
		}
		if (self != null) self.cannonBehavior().blockEntity.notifyUpdate();
	}

	/** Connects to same-material package slider blocks on both ends of the cannon axis. */
	static void onPlace(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		if (!(state.getBlock() instanceof PackageSliderBlock cBlock)
			|| !(level.getBlockEntity(pos) instanceof IPackageSliderBlockEntity cbe)) return;

		Direction facing = cBlock.getFacing(state);
		for (Direction dir : new Direction[] { facing, facing.getOpposite() }) {
			BlockPos otherPos = pos.relative(dir);
			BlockState otherState = level.getBlockState(otherPos);
			if (!cBlock.canConnectToSide(state, dir)
				|| !(otherState.getBlock() instanceof PackageSliderBlock otherBlock)
				|| otherBlock.getCannonMaterial() != cBlock.getCannonMaterial()
				|| !otherBlock.canConnectToSide(otherState, dir.getOpposite())
				|| !(level.getBlockEntity(otherPos) instanceof IPackageSliderBlockEntity otherBe)) continue;

			cbe.cannonBehavior().setConnectedFace(dir, true);
			otherBe.cannonBehavior().setConnectedFace(dir.getOpposite(), true);
			otherBe.cannonBehavior().blockEntity.notifyUpdate();
			if (level instanceof ServerLevel slevel) {
				Vec3 particlePos = Vec3.atCenterOf(pos).add(Vec3.atLowerCornerOf(dir.getNormal()).scale(0.5));
				slevel.sendParticles(ParticleTypes.CRIT, particlePos.x, particlePos.y, particlePos.z, 10, 0.5d, 0.5d, 0.5d, 0.1d);
			}
		}
		cbe.cannonBehavior().blockEntity.notifyUpdate();
	}

	/** Package sliders only lie horizontally; the wrench turns them clockwise about the vertical axis. */
	@Override
	default BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
		Direction facing = originalState.getValue(DirectionalBlock.FACING);
		return originalState.setValue(DirectionalBlock.FACING, facing.getAxis().isHorizontal() ? facing.getClockWise() : Direction.NORTH);
	}

	@Override
	default InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		BlockState rotated = this.getRotatedBlockState(state, context.getClickedFace());
		if (rotated == state) return InteractionResult.PASS;
		if (!level.isClientSide) {
			// Break the old connections first; setting the same block keeps the block entity and its package.
			this.disconnectPackageSlider(level, pos, state);
			level.setBlock(pos, rotated, Block.UPDATE_ALL);
			onPlace(level, pos);
			IWrenchable.playRotateSound(level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		Player player = context.getPlayer();
		if (!(level instanceof ServerLevel slevel)) return InteractionResult.SUCCESS;

		BlockEvent.BreakEvent event = new BlockEvent.BreakEvent(level, pos, state, player);
		NeoForge.EVENT_BUS.post(event);
		if (event.isCanceled()) return InteractionResult.SUCCESS;

		// Same as IWrenchable, but also drops the loaded package and disconnects the neighbours.
		this.playerWillDestroyPackageSlider(level, pos, state);
		if (player != null && !player.isCreative()) {
			Block.getDrops(state, slevel, pos, level.getBlockEntity(pos), player, context.getItemInHand())
				.forEach(stack -> player.getInventory().placeItemBackInInventory(stack));
		}
		state.spawnAfterBreak(slevel, pos, ItemStack.EMPTY, true);
		level.destroyBlock(pos, false);
		IWrenchable.playRemoveSound(level, pos);
		return InteractionResult.SUCCESS;
	}

	@Override
	default boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand,
											 Level level, Contraption contraption, BlockEntity be,
											 StructureBlockInfo info, PitchOrientedContraptionEntity entity) {
		return false;
	}

	@Nonnull
	@Override
	default AbstractMountedCannonContraption getCannonContraption() {
		return new MountedPackageSliderContraption();
	}

}
