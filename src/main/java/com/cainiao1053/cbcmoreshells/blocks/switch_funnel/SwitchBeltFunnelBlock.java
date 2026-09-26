package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.logistics.funnel.BeltFunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

import static com.cainiao1053.cbcmoreshells.blocks.switch_funnel.SwitchFunnelBlock.TRIGGERED;

/**
 * Belt/depot form of the switch funnel, the counterpart of Create's brass belt funnel.
 * The switch funnel turns into this block when placed horizontally above a belt or depot, and back again
 * when that support is gone; both share {@link SwitchFunnelBlockEntity}.
 */
public class SwitchBeltFunnelBlock extends BeltFunnelBlock {

	public SwitchBeltFunnelBlock(BlockEntry<SwitchFunnelBlock> parent, Properties properties) {
		super(parent, properties);
		registerDefaultState(defaultBlockState().setValue(TRIGGERED, false));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder.add(TRIGGERED));
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext ctx) {
		BlockState state = super.getStateForPlacement(ctx);
		return state.setValue(POWERED, false)
			.setValue(TRIGGERED, ctx.getLevel().hasNeighborSignal(ctx.getClickedPos()));
	}

	@Override
	public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		SwitchFunnelBlock.tickRedstone(state, level, pos);
	}

	@Override
	public BlockState updateShape(BlockState state, Direction direction, BlockState neighbour, LevelAccessor world,
								  BlockPos pos, BlockPos neighbourPos) {
		return SwitchFunnelBlock.copyTriggered(state, super.updateShape(state, direction, neighbour, world, pos, neighbourPos));
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
