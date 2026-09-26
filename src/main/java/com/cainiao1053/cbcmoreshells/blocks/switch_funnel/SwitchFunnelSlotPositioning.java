package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelFilterSlotPositioning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

/**
 * Funnel filter slot shifted sideways by {@code column} slots, so three slots fit in a row on one face.
 * Column -1 / 0 / +1 = left / middle / right, as seen by a player looking at that face.
 */
public class SwitchFunnelSlotPositioning extends FunnelFilterSlotPositioning {

	private static final double SPACING = 5 / 16.0;
	private static final double HIT_RADIUS = 2.4 / 16.0;

	private final int column;

	public SwitchFunnelSlotPositioning(int column) {
		this.column = column;
	}

	@Override
	public float getScale() {
		return 0.35f;
	}

	@Override
	public Vec3 getLocalOffset(LevelAccessor level, BlockPos pos, BlockState state) {
		Vec3 base = super.getLocalOffset(level, pos, state);
		Direction funnelFacing = FunnelBlock.getFunnelFacing(state);
		if (base == null || funnelFacing == null)
			return base;
		Direction face = funnelFacing.getAxis().isHorizontal() ? funnelFacing : getSide();
		// Vertical funnels only have slots on horizontal sides; an up/down side (also the Sided default)
		// has no left/right, and getCounterClockWise() throws for it
		if (face.getAxis().isVertical())
			return base;
		Direction right = face.getCounterClockWise();
		return base.add(Vec3.atLowerCornerOf(right.getNormal()).scale(column * SPACING));
	}

	@Override
	public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
		if (!isSideActive(state, getSide()))
			return false;
		Vec3 offset = getLocalOffset(level, pos, state);
		return offset != null && localHit.distanceTo(offset) < HIT_RADIUS;
	}

}
