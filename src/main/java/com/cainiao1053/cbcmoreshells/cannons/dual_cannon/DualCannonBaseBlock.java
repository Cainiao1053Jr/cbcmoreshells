package com.cainiao1053.cbcmoreshells.cannons.dual_cannon;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement.DualCannonBarrelPlacementHelper;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement.DualCannonBreechPlacementHelper;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementHelpers;
import com.simibubi.create.foundation.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public abstract class DualCannonBaseBlock extends DirectionalBlock implements DualCannonBlock, IWrenchable {

	/** Breech first: it only ever matches when the cannon has no breech yet. */
	private static final int[] PLACEMENT_HELPER_IDS = {
		PlacementHelpers.register(new DualCannonBreechPlacementHelper()),
		PlacementHelpers.register(new DualCannonBarrelPlacementHelper())
	};

	private final DualCannonMaterial material;

	protected DualCannonBaseBlock(Properties properties, DualCannonMaterial material) {
		super(properties.pushReaction(PushReaction.BLOCK));
		this.material = material;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.createBlockStateDefinition(builder);
	}

	@Override public DualCannonMaterial getCannonMaterial() { return this.material; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }

	@SuppressWarnings("deprecation")
//	@Override
//	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
//		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
//		super.onRemove(state, level, pos, newState, isMoving);
//	}

	@Override
	public void playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) this.playerWillDestroyBigCannon(level, pos, state, player);
		super.playerWillDestroy(level, pos, state, player);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection());
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
		ItemStack heldItem = player.getItemInHand(hand);
		for (int id : PLACEMENT_HELPER_IDS) {
			IPlacementHelper helper = PlacementHelpers.get(id);
			if (!helper.matchesItem(heldItem)) continue;
			PlacementOffset offset = helper.getOffset(player, level, state, pos, ray, heldItem);
			if (!offset.isSuccessful()) continue;

			InteractionResult result = offset.placeInWorld(level, (BlockItem) heldItem.getItem(), player, hand, ray);
			// PlacementOffset sets the block directly, so the usual BlockItem#place hookup is skipped.
			if (result.consumesAction() && !level.isClientSide
				&& (this.material.properties().connectsInSurvival() || player.isCreative()))
				DualCannonBlock.onPlace(level, offset.getBlockPos());
			return result;
		}
		return InteractionResult.PASS;
	}

	@Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
	@Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }

}
