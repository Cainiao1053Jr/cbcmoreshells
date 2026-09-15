package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement;

import java.util.function.Predicate;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonQuickfiringBreechBlock;
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
 * Caps a cannon with a quick-firing breech. A cannon only ever takes one breech, so this does
 * nothing once either end of the run is already closed off.
 *
 * <p>{@code MountedDualCannonContraption} reads the breech back at
 * {@code startPos.relative(initialOrientation.getOpposite())}, with {@code initialOrientation}
 * pointing at the muzzle: the breech sits behind the barrel and faces along it.
 */
public class DualCannonBreechPlacementHelper implements IPlacementHelper {

	@Override
	public Predicate<ItemStack> getItemPredicate() {
		return stack -> stack.getItem() instanceof BlockItem item && item.getBlock() instanceof DualCannonQuickfiringBreechBlock;
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
		if (!(stack.getItem() instanceof BlockItem item) || !(item.getBlock() instanceof DualCannonQuickfiringBreechBlock held))
			return PlacementOffset.fail();
		if (!(state.getBlock() instanceof DualCannonBlock hovered)) return PlacementOffset.fail();

		DualCannonMaterial material = hovered.getCannonMaterialInLevel(level, state, pos);
		if (held.getCannonMaterial() != material) return PlacementOffset.fail();

		Direction.Axis axis = hovered.getFacing(state).getAxis();
		// One breech per cannon: a closed end anywhere on the run means there is nothing to add.
		if (DualCannonPlacement.hasClosedEnd(level, pos, state, material, axis)) return PlacementOffset.fail();

		for (Direction dir : IPlacementHelper.orderedByDistanceOnlyAxis(pos, ray.getLocation(), axis)) {
			BlockPos placePos = DualCannonPlacement.endOfCannon(level, pos, dir, material, axis).relative(dir);
			if (!level.getBlockState(placePos).canBeReplaced()) continue;

			// Face the barrel, matching the orientation the contraption assembles the breech at.
			Direction facing = dir.getOpposite();
			// Reproduces getStateForPlacement for a player sighting along the cannon.
			boolean axisAlongFirst = axis == Direction.Axis.Z;
			return PlacementOffset
				.success(placePos, s -> s.setValue(DirectionalBlock.FACING, facing)
					.setValue(DualCannonQuickfiringBreechBlock.AXIS, axisAlongFirst))
				.withGhostState(held.defaultBlockState());
		}
		return PlacementOffset.fail();
	}

}
