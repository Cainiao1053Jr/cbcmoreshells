package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement;

import java.util.function.Predicate;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEnd;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.simibubi.create.foundation.placement.IPlacementHelper;
import com.simibubi.create.foundation.placement.PlacementOffset;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

/**
 * Extends a cannon by one barrel block when a barrel of the same material is used on it, the way
 * {@code CopycatPanelBlock} chains panels.
 *
 * <p>Blocks are only appended to the open end of the cannon: once a breech is present the barrel
 * always grows away from it.
 */
public class DualCannonBarrelPlacementHelper implements IPlacementHelper {

	@Override
	public Predicate<ItemStack> getItemPredicate() {
		return stack -> stack.getItem() instanceof BlockItem item
			&& item.getBlock() instanceof DualCannonBlock cannon
			&& cannon.getDefaultOpeningType() == DualCannonEnd.OPEN;
	}

	@Override
	public Predicate<BlockState> getStatePredicate() {
		return state -> state.getBlock() instanceof DualCannonBlock && state.hasProperty(DirectionalBlock.FACING);
	}

	@Override
	public PlacementOffset getOffset(Player player, Level level, BlockState state, BlockPos pos, BlockHitResult ray) {
		return this.getOffset(player, level, state, pos, ray, player.getMainHandItem());
	}

	@Override
	public PlacementOffset getOffset(Player player, Level level, BlockState state, BlockPos pos, BlockHitResult ray, ItemStack stack) {
		if (!(stack.getItem() instanceof BlockItem item) || !(item.getBlock() instanceof DualCannonBlock held)) return PlacementOffset.fail();
		if (!(state.getBlock() instanceof DualCannonBlock hovered)) return PlacementOffset.fail();

		DualCannonMaterial material = hovered.getCannonMaterialInLevel(level, state, pos);
		if (held.getCannonMaterial() != material) return PlacementOffset.fail();

		Direction.Axis axis = hovered.getFacing(state).getAxis();

		for (Direction dir : IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axis)) {
			BlockPos end = DualCannonPlacement.endOfCannon(level, pos, dir, material, axis);
			BlockState endState = level.getBlockState(end);
			if (DualCannonPlacement.isClosedEnd(level, end, endState)) {
				// A breech seals the cannon behind it, so grow from the other end instead. Only a
				// breech with nothing behind it yet may be built on, along the way it faces.
				BlockPos behind = end.relative(dir.getOpposite());
				if (DualCannonPlacement.belongsToCannon(level, behind, level.getBlockState(behind), material, axis)) continue;
				if (((DualCannonBlock) endState.getBlock()).getFacing(endState) != dir) continue;
			}

			BlockPos placePos = end.relative(dir);
			if (!level.getBlockState(placePos).canBeReplaced()) continue;

			// Match the neighbour's facing so the connected textures line up along the barrel.
			Direction facing = endState.hasProperty(DirectionalBlock.FACING) ? endState.getValue(DirectionalBlock.FACING) : dir;
			return PlacementOffset.success(placePos, s -> s.setValue(DirectionalBlock.FACING, facing))
				.withGhostState(item.getBlock().defaultBlockState());
		}
		return PlacementOffset.fail();
	}

}
