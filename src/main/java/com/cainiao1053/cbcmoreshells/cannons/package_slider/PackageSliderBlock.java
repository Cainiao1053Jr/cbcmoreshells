package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedPackageSliderContraption;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.CannonContraptionProviderBlock;
import rbasamoyai.createbigcannons.cannons.InteractableCannonBlock;

import javax.annotation.Nonnull;

public interface PackageSliderBlock extends CannonContraptionProviderBlock, InteractableCannonBlock {

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

	/** Drops the loaded package and disconnects the neighbours. Only for player breaking, not contraption assembly. */
	default void playerWillDestroyPackageSlider(Level level, BlockPos pos, BlockState state) {
		if (level.getBlockEntity(pos) instanceof IPackageSliderBlockEntity cbe) {
			ItemStack loaded = cbe.cannonBehavior().removeLoaded();
			if (!loaded.isEmpty())
				Containers.dropItemStack(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, loaded);
		}
		Direction facing = this.getFacing(state);
		for (Direction dir : new Direction[] { facing, facing.getOpposite() }) {
			if (level.getBlockEntity(pos.relative(dir)) instanceof IPackageSliderBlockEntity neighbour) {
				neighbour.cannonBehavior().setConnectedFace(dir.getOpposite(), false);
				neighbour.cannonBehavior().blockEntity.notifyUpdate();
			}
		}
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
