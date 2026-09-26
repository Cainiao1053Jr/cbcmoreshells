package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class SwitchFunnelBlock extends FunnelBlock {

	public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

	public SwitchFunnelBlock(Properties properties) {
		super(properties);
		registerDefaultState(defaultBlockState().setValue(TRIGGERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(TRIGGERED));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		BlockState state = super.getStateForPlacement(context);
		return state.setValue(POWERED, false)
			.setValue(TRIGGERED, context.getLevel().hasNeighborSignal(context.getClickedPos()));
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		tickRedstone(state, level, pos);
	}

	/**
	 * Shared by the funnel and its belt variant. POWERED stays false so the block entity never pauses;
	 * the signal level lives in TRIGGERED and each rising edge cycles the filters.
	 */
	static void tickRedstone(BlockState state, ServerLevel level, BlockPos pos) {
		boolean powered = level.hasNeighborSignal(pos);
		if (powered == state.getValue(TRIGGERED))
			return;
		level.setBlock(pos, state.setValue(TRIGGERED, powered), 2);
		if (powered && level.getBlockEntity(pos) instanceof SwitchFunnelBlockEntity be)
			be.cycleFilters();
	}

	/** Keeps the remembered signal level when switching between funnel and belt funnel. */
	static BlockState copyTriggered(BlockState from, BlockState to) {
		if (from == to || !from.hasProperty(TRIGGERED) || !to.hasProperty(TRIGGERED))
			return to;
		return to.setValue(TRIGGERED, from.getValue(TRIGGERED));
	}

	@Override
	public BlockState getEquivalentBeltFunnel(BlockGetter world, BlockPos pos, BlockState state) {
		Direction facing = getFacing(state);
		return CBCMSBlocks.SWITCH_BELT_FUNNEL.getDefaultState()
			.setValue(BeltFunnelBlock.HORIZONTAL_FACING, facing)
			.setValue(POWERED, false)
			.setValue(TRIGGERED, state.getValue(TRIGGERED));
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighbourState, LevelAccessor world,
								  BlockPos pos, BlockPos neighbourPos) {
		return copyTriggered(state, super.updateShape(state, direction, neighbourState, world, pos, neighbourPos));
	}

	@Override
	@SuppressWarnings({"unchecked", "rawtypes"})
	public Class<FunnelBlockEntity> getBlockEntityClass() {
		return (Class) SwitchFunnelBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends FunnelBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.SWITCH_FUNNEL.get();
	}

}
