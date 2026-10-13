package com.cainiao1053.cbcmoreshells.cannons.package_slider.placement;

import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBlock;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import net.createmod.catnip.placement.IPlacementHelper;
import net.createmod.catnip.placement.PlacementHelpers;
import net.createmod.catnip.placement.PlacementOffset;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;
import java.util.function.Predicate;

/**
 * Placement assist for package sliders, same rules as the dual cannon one: hovering a package slider block while
 * holding another one of the same material shows a ghost preview at the far end of the cannon along its axis,
 * a breech is only offered while the cannon has none, and once it has one only the open end can be extended.
 */
public class PackageSliderPlacementHelper implements IPlacementHelper {

	public static final PackageSliderPlacementHelper INSTANCE = new PackageSliderPlacementHelper();
	public static final int ID = PlacementHelpers.register(INSTANCE);

	/** Cap on how far a single assist action will walk down a cannon. */
	private static final int MAX_SCAN = 32;

	/** Triggers class initialisation, and with it the registration above. */
	public static void init() {}

	@Override
	public Predicate<ItemStack> getItemPredicate() {
		return stack -> stack.getItem() instanceof BlockItem blockItem
			&& blockItem.getBlock() instanceof PackageSliderBlock cannonBlock
			&& cannonBlock.supportsPlacementAssist();
	}

	@Override
	public Predicate<BlockState> getStatePredicate() {
		return state -> state.getBlock() instanceof PackageSliderBlock;
	}

	@Override
	public PlacementOffset getOffset(Player player, Level level, BlockState state, BlockPos pos, BlockHitResult ray) {
		if (player == null) return PlacementOffset.fail();
		ItemStack stack = player.getMainHandItem();
		if (!this.matchesItem(stack)) stack = player.getOffhandItem();
		return this.getOffset(player, level, state, pos, ray, stack);
	}

	@Override
	public PlacementOffset getOffset(Player player, Level level, BlockState state, BlockPos pos, BlockHitResult ray,
									 ItemStack heldItem) {
		PlacementOffset offset = this.getCannonOffset(level, state, pos, ray, heldItem);
		// The inherited overload attaches the ghost state for us; overriding it means doing so here.
		if (offset.isSuccessful() && heldItem.getItem() instanceof BlockItem blockItem)
			offset = offset.withGhostState(blockItem.getBlock().defaultBlockState());
		return offset;
	}

	private PlacementOffset getCannonOffset(Level level, BlockState anchorState, BlockPos anchorPos, BlockHitResult ray,
											ItemStack heldItem) {
		if (!(heldItem.getItem() instanceof BlockItem blockItem)
			|| !(blockItem.getBlock() instanceof PackageSliderBlock heldBlock)
			|| !heldBlock.supportsPlacementAssist())
			return PlacementOffset.fail();
		if (!(anchorState.getBlock() instanceof PackageSliderBlock anchorBlock)) return PlacementOffset.fail();

		PackageSliderMaterial material = anchorBlock.getCannonMaterial();
		if (heldBlock.getCannonMaterial() != material) return PlacementOffset.fail();

		Direction.Axis axis = anchorBlock.getFacing(anchorState).getAxis();
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, axis);

		PackageSliderCannonRun runPositive = PackageSliderCannonRun.scan(level, anchorPos, anchorState, positive, material, MAX_SCAN);
		PackageSliderCannonRun runNegative = PackageSliderCannonRun.scan(level, anchorPos, anchorState, positive.getOpposite(), material, MAX_SCAN);
		int cannonLength = runPositive.length() + runNegative.length() + 1;

		// A cannon only ever has one breech; a second one would close both ends.
		if (heldBlock.isPlacementAssistBreech() && (runPositive.closed() || runNegative.closed()))
			return PlacementOffset.fail();

		List<Direction> directions = IPlacementHelper.orderedByDistanceOnlyAxis(anchorPos, ray.getLocation(), axis);
		for (Direction dir : directions) {
			PackageSliderCannonRun run = dir == positive ? runPositive : runNegative;
			if (!run.canExtendOutward(dir, cannonLength)) continue;

			BlockPos target = run.endPos().relative(dir);
			if (!level.isInWorldBounds(target) || !level.getBlockState(target).canBeReplaced()) continue;

			// Match the block the new one will sit against, so a cannon keeps one uniform facing.
			Direction neighbourFacing = ((PackageSliderBlock) run.endState().getBlock()).getFacing(run.endState());
			return PlacementOffset.success(target,
				state -> heldBlock.getAssistedPlacementState(state, dir, neighbourFacing));
		}
		return PlacementOffset.fail();
	}

	/**
	 * Runs the assist for a right click on a package slider block. Returns
	 * {@link ItemInteractionResult#PASS_TO_DEFAULT_BLOCK_INTERACTION} when the assist does not apply.
	 */
	public static ItemInteractionResult tryAssistedPlacement(ItemStack stack, BlockState state, Level level,
															 BlockPos pos, Player player, InteractionHand hand,
															 BlockHitResult ray) {
		if (player == null || player.isShiftKeyDown() || !player.mayBuild())
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!INSTANCE.matchesItem(stack) || !INSTANCE.matchesState(state))
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		PlacementOffset offset = INSTANCE.getOffset(player, level, state, pos, ray, stack);
		if (!offset.isSuccessful()) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;

		BlockPos target = offset.getBlockPos();
		ItemInteractionResult result = offset.placeInWorld(level, (BlockItem) stack.getItem(), player, hand, ray);

		// placeInWorld bypasses BlockItem#place, so the connection pass the block items normally run happens here.
		// Package slider materials always connect in survival.
		if (result == ItemInteractionResult.SUCCESS && !level.isClientSide
			&& level.getBlockState(target).getBlock() instanceof PackageSliderBlock)
			PackageSliderBlock.onPlace(level, target);

		return result;
	}

}
