package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * One half of a cannon as seen from a clicked block: everything found walking along the cannon axis
 * in a single direction.
 *
 * @param endPos   position of the last cannon block in that direction
 * @param endState state at {@link #endPos}
 * @param length   number of blocks walked, not counting the block the scan started from
 * @param closed   whether this half contains a breech or a solid end, including the start block
 */
public record CannonRun(BlockPos endPos, BlockState endState, int length, boolean closed) {

	public static CannonRun scan(Level level, BlockPos from, BlockState fromState, Direction dir,
								 DualCannonMaterial material, int maxScan) {
		BlockPos pos = from;
		BlockState state = fromState;
		int length = 0;
		boolean closed = state.getBlock() instanceof DualCannonBlock startBlock && startBlock.closesCannonEnd();

		while (length < maxScan) {
			if (!(state.getBlock() instanceof DualCannonBlock block) || !block.canConnectToSide(state, dir)) break;

			BlockPos nextPos = pos.relative(dir);
			BlockState nextState = level.getBlockState(nextPos);
			// Adjacency is deliberately checked against the blocks themselves rather than
			// BigCannonBehavior#isConnectedTo: materials with connectsInSurvival disabled are
			// unconnected until welded, and the assist should still work on them.
			if (!(nextState.getBlock() instanceof DualCannonBlock nextBlock)
				|| nextBlock.getCannonMaterialInLevel(level, nextState, nextPos) != material
				|| nextBlock.getFacing(nextState).getAxis() != dir.getAxis()
				|| !nextBlock.canConnectToSide(nextState, dir.getOpposite()))
				break;

			pos = nextPos;
			state = nextState;
			length++;
			closed |= nextBlock.closesCannonEnd();
		}
		return new CannonRun(pos, state, length, closed);
	}

	/**
	 * Whether another cannon block may be appended past {@link #endPos}. A breech or solid end
	 * terminating a real cannon has the barrel on its inner side, so its outer side is the cannon's
	 * tail and cannot be extended. A lone block is exempt: it is still ambiguous which side of a
	 * freshly placed breech the barrel will go on.
	 */
	public boolean canExtendOutward(Direction dir, int cannonLength) {
		if (!(this.endState.getBlock() instanceof DualCannonBlock endBlock)) return false;
		if (cannonLength > 1 && endBlock.closesCannonEnd()) return false;
		return endBlock.canConnectToSide(this.endState, dir);
	}

}
