package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement;

import java.util.List;
import java.util.function.Predicate;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;

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

/**
 * Placement assist for dual cannons, in the spirit of Create's CopycatPanel/PoleHelper: hovering a
 * cannon block while holding another one of the same material shows a ghost preview, and right
 * clicking places it there.
 * <p>
 * Cannon rules differ from copycats in three ways:
 * <ul>
 * <li>blocks are only ever placed along the cannon axis, at the far end of the run rather than
 * against the clicked block;</li>
 * <li>a cannon has exactly one closed end, so a breech can only be placed while the cannon has
 * none, and it goes on the side the player is looking at;</li>
 * <li>once a breech is present the only extendable end is the opposite, open one.</li>
 * </ul>
 */
public class DualCannonPlacementHelper implements IPlacementHelper {

	public static final DualCannonPlacementHelper INSTANCE = new DualCannonPlacementHelper();
	public static final int ID = PlacementHelpers.register(INSTANCE);

	/** Cap on how far a single assist action will walk down a cannon. */
	private static final int MAX_SCAN = 32;

	/** Triggers class initialisation, and with it the registration above. */
	public static void init() {}

	@Override
	public Predicate<ItemStack> getItemPredicate() {
		return stack -> stack.getItem() instanceof BlockItem blockItem
			&& blockItem.getBlock() instanceof DualCannonBlock cannonBlock
			&& cannonBlock.supportsPlacementAssist();
	}

	@Override
	public Predicate<BlockState> getStatePredicate() {
		return state -> state.getBlock() instanceof DualCannonBlock;
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
			|| !(blockItem.getBlock() instanceof DualCannonBlock heldBlock)
			|| !heldBlock.supportsPlacementAssist())
			return PlacementOffset.fail();
		if (!(anchorState.getBlock() instanceof DualCannonBlock anchorBlock)) return PlacementOffset.fail();

		DualCannonMaterial material = anchorBlock.getCannonMaterialInLevel(level, anchorState, anchorPos);
		if (heldBlock.getCannonMaterial() != material) return PlacementOffset.fail();

		Direction.Axis axis = anchorBlock.getFacing(anchorState).getAxis();
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, axis);

		CannonRun runPositive = CannonRun.scan(level, anchorPos, anchorState, positive, material, MAX_SCAN);
		CannonRun runNegative = CannonRun.scan(level, anchorPos, anchorState, positive.getOpposite(), material, MAX_SCAN);
		int cannonLength = runPositive.length() + runNegative.length() + 1;

		// A cannon only ever has one closed end, so a breech is refused outright once the run
		// contains a breech or a solid end - adding a second one would make both ends closed.
		if (heldBlock.isPlacementAssistBreech() && (runPositive.closed() || runNegative.closed()))
			return PlacementOffset.fail();

		List<Direction> directions = IPlacementHelper.orderedByDistanceOnlyAxis(anchorPos, ray.getLocation(), axis);
		for (Direction dir : directions) {
			CannonRun run = dir == positive ? runPositive : runNegative;
			if (!run.canExtendOutward(dir, cannonLength)) continue;

			BlockPos target = run.endPos().relative(dir);
			if (!level.isInWorldBounds(target) || !level.getBlockState(target).canBeReplaced()) continue;

			// Match the block the new one will sit against, so a cannon keeps one uniform facing.
			Direction neighbourFacing = ((DualCannonBlock) run.endState().getBlock()).getFacing(run.endState());
			return PlacementOffset.success(target,
				state -> heldBlock.getAssistedPlacementState(state, dir, neighbourFacing));
		}
		return PlacementOffset.fail();
	}

	/**
	 * Runs the assist for a right click on a cannon block. Returns
	 * {@link ItemInteractionResult#PASS_TO_DEFAULT_BLOCK_INTERACTION} when the assist does not
	 * apply, so callers can fall through to their normal interaction.
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

		// placeInWorld bypasses BlockItem#place, so the connection pass the cannon block items
		// normally run has to be kicked off here.
		if (result == ItemInteractionResult.SUCCESS && !level.isClientSide
			&& level.getBlockState(target).getBlock() instanceof DualCannonBlock placed
			&& (placed.getCannonMaterial().properties().connectsInSurvival() || player.isCreative()))
			DualCannonBlock.onPlace(level, target);

		return result;
	}

}
