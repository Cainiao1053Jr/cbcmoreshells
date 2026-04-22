package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.sliding_breech;

import javax.annotation.Nullable;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEnd;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullSupplier;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.PushReaction;
//import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
//import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
//import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;
// rbasamoyai.createbigcannons.cannons.big_cannons.cannon_end.BigCannonEnd;
//import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;

public class DualCannonSlidingBreechBlock extends DirectionalAxisKineticBlock implements IBE<DualCannonSlidingBreechBlockEntity>, DualCannonBlock {

	private final DualCannonMaterial cannonMaterial;
	private final NonNullSupplier<? extends Block> quickfiringConversion;

	public DualCannonSlidingBreechBlock(Properties properties, DualCannonMaterial cannonMaterial, NonNullSupplier<? extends Block> quickfiringConversion) {
		super(properties.pushReaction(PushReaction.BLOCK));
		this.cannonMaterial = cannonMaterial;
		this.quickfiringConversion = quickfiringConversion;
	}

	@Override
	public DualCannonMaterial getCannonMaterial() {
		return this.cannonMaterial;
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.SLIDING_BREECH;
	}

	@Override
	public Direction getFacing(BlockState state) {
		return state.getValue(FACING);
	}

	@Override
	public DualCannonEnd getOpeningType(@Nullable Level level, BlockState state, BlockPos pos) {
		return level != null && level.getBlockEntity(pos) instanceof DualCannonSlidingBreechBlockEntity breech ? breech.getOpeningType() : DualCannonEnd.OPEN;
	}

	@Override
	public DualCannonEnd getOpeningType(MountedDualCannonContraption contraption, BlockState state, BlockPos pos) {
		return contraption.presentBlockEntities.get(pos) instanceof DualCannonSlidingBreechBlockEntity breech ? breech.getOpeningType() : DualCannonEnd.OPEN;
	}

	@Override public DualCannonEnd getDefaultOpeningType() { return DualCannonEnd.CLOSED; }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		Direction facing = context.getNearestLookingDirection().getOpposite();
		Direction horizontal = context.getHorizontalDirection();
		return this.defaultBlockState()
			.setValue(FACING, facing)
			.setValue(AXIS_ALONG_FIRST_COORDINATE, horizontal.getAxis() == Direction.Axis.Z);
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

	@Override
	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
		super.onRemove(state, level, pos, newState, isMoving);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) this.playerWillDestroyBigCannon(level, pos, state, player);
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public Class<DualCannonSlidingBreechBlockEntity> getBlockEntityClass() {
		return DualCannonSlidingBreechBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends DualCannonSlidingBreechBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.DUAL_CANNON_SLIDING_BREECH.get();
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	public BlockState getConversion(BlockState old) {
		return this.quickfiringConversion.get().defaultBlockState()
			.setValue(FACING, old.getValue(FACING))
			.setValue(AXIS_ALONG_FIRST_COORDINATE, old.getValue(AXIS_ALONG_FIRST_COORDINATE));
	}

}
