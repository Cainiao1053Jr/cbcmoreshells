package com.cainiao1053.cbcmoreshells.blocks.speed_limiter;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class SpeedLimiterBlock extends RotatedPillarKineticBlock implements IBE<SpeedLimiterBlockEntity>, IWrenchable {

	public SpeedLimiterBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		if (context.getPlayer() != null && context.getPlayer().isShiftKeyDown())
			return super.getStateForPlacement(context);
		Axis preferredAxis = getPreferredAxis(context);
		return defaultBlockState().setValue(AXIS,
			preferredAxis == null ? context.getNearestLookingDirection().getAxis() : preferredAxis);
	}

	@Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		return face.getAxis() == state.getValue(AXIS);
	}

	@Override
	public Axis getRotationAxis(BlockState state) {
		return state.getValue(AXIS);
	}

	@Override
	public InteractionResult onWrenched(BlockState state, UseOnContext context) {
		Level level = context.getLevel();
		BlockPos pos = context.getClickedPos();
		SpeedLimiterBlockEntity be = getBlockEntity(level, pos);
		if (be == null || be.getSpeedRatio() >= 1)
			return super.onWrenched(state, context);
		if (!level.isClientSide) {
			be.resetSpeedRatio();
			IWrenchable.playRotateSound(level, pos);
		}
		return InteractionResult.SUCCESS;
	}

	@Override
	public Class<SpeedLimiterBlockEntity> getBlockEntityClass() {
		return SpeedLimiterBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends SpeedLimiterBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.SPEED_LIMITER.get();
	}

}
