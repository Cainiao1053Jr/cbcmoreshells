package com.cainiao1053.cbcmoreshells.cannons.package_slider.placement;

import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBlock;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

/**
 * One half of a package slider as seen from a clicked block: everything found walking along the cannon axis
 * in a single direction.
 *
 * @param endPos   position of the last cannon block in that direction
 * @param endState state at {@link #endPos}
 * @param length   number of blocks walked, not counting the block the scan started from
 * @param closed   whether this half contains a breech, including the start block
 */
public record PackageSliderCannonRun(BlockPos endPos, BlockState endState, int length, boolean closed) {

	public static PackageSliderCannonRun scan(Level level, BlockPos from, BlockState fromState, Direction dir,
											  PackageSliderMaterial material, int maxScan) {
		BlockPos pos = from;
		BlockState state = fromState;
		int length = 0;
		boolean closed = state.getBlock() instanceof PackageSliderBlock startBlock && startBlock.closesCannonEnd();

		while (length < maxScan) {
			if (!(state.getBlock() instanceof PackageSliderBlock block) || !block.canConnectToSide(state, dir)) break;

			BlockPos nextPos = pos.relative(dir);
			BlockState nextState = level.getBlockState(nextPos);
			if (!(nextState.getBlock() instanceof PackageSliderBlock nextBlock)
				|| nextBlock.getCannonMaterial() != material
				|| nextBlock.getFacing(nextState).getAxis() != dir.getAxis()
				|| !nextBlock.canConnectToSide(nextState, dir.getOpposite()))
				break;

			pos = nextPos;
			state = nextState;
			length++;
			closed |= nextBlock.closesCannonEnd();
		}
		return new PackageSliderCannonRun(pos, state, length, closed);
	}

	/**
	 * Whether another cannon block may be appended past {@link #endPos}. A breech terminating a real cannon has
	 * the barrel on its inner side, so its outer side is the cannon's tail. A lone block is exempt: it is still
	 * ambiguous which side of a freshly placed breech the barrel will go on.
	 */
	public boolean canExtendOutward(Direction dir, int cannonLength) {
		if (!(this.endState.getBlock() instanceof PackageSliderBlock endBlock)) return false;
		if (cannonLength > 1 && endBlock.closesCannonEnd()) return false;
		return endBlock.canConnectToSide(this.endState, dir);
	}

}
