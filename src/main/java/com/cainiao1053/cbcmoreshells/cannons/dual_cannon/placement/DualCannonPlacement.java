package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEnd;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * Shared scanning helpers for the assisted placement of dual cannon blocks.
 *
 * <p>A cannon is a straight run of blocks sharing one material and one axis, capped by at most one
 * closed end - the breech - as read back by {@link com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption}
 * during assembly.
 */
public class DualCannonPlacement {

	/** Cannons longer than this cannot be assembled anyway, so stop walking there. */
	private static final int MAX_SCAN_LENGTH = 32;

	private DualCannonPlacement() {}

	public static boolean belongsToCannon(Level level, BlockPos pos, BlockState state, DualCannonMaterial material, Direction.Axis axis) {
		return state.getBlock() instanceof DualCannonBlock cannon
			&& cannon.getCannonMaterialInLevel(level, state, pos) == material
			&& cannon.getFacing(state).getAxis() == axis;
	}

	/** A closed block caps the cannon - the breech, or a solid cannon end. */
	public static boolean isClosedEnd(Level level, BlockPos pos, BlockState state) {
		return state.getBlock() instanceof DualCannonBlock cannon
			&& cannon.getOpeningType(level, state, pos) != DualCannonEnd.OPEN;
	}

	/**
	 * Walks the cannon from {@code pos} towards {@code dir} and returns the position of the last
	 * block of the run. A closed end is included in the run, but nothing past it is.
	 */
	public static BlockPos endOfCannon(Level level, BlockPos pos, Direction dir, DualCannonMaterial material, Direction.Axis axis) {
		BlockPos current = pos;
		for (int i = 0; i < MAX_SCAN_LENGTH; ++i) {
			BlockPos next = current.relative(dir);
			BlockState nextState = level.getBlockState(next);
			if (!belongsToCannon(level, next, nextState, material, axis)) return current;
			current = next;
			if (isClosedEnd(level, next, nextState)) return current;
		}
		return current;
	}

	/** Returns true if either end of the cannon containing {@code pos} is already closed off. */
	public static boolean hasClosedEnd(Level level, BlockPos pos, BlockState state, DualCannonMaterial material, Direction.Axis axis) {
		if (isClosedEnd(level, pos, state)) return true;
		for (Direction dir : Direction.values()) {
			if (dir.getAxis() != axis) continue;
			BlockPos end = endOfCannon(level, pos, dir, material, axis);
			if (isClosedEnd(level, end, level.getBlockState(end))) return true;
		}
		return false;
	}

}
