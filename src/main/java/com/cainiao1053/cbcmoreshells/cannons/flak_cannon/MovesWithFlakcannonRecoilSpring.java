package com.cainiao1053.cbcmoreshells.cannons.flak_cannon;

import net.minecraft.world.level.block.state.BlockState;

public interface MovesWithFlakcannonRecoilSpring {

	BlockState getMovingState(BlockState original);
	BlockState getStationaryState(BlockState original);

}
