package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.simibubi.create.content.logistics.funnel.FunnelBlock;
import com.simibubi.create.content.logistics.funnel.FunnelFilterSlotPositioning;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public class SwitchFunnelSlotPositioning extends FunnelFilterSlotPositioning {

	private static final double SPACING = 5 / 16.0;
	private static final double HIT_RADIUS = 2.4 / 16.0;
	private static final double OUTWARD = 1 / 16.0;

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
		Vec3 offset = base.add(Vec3.atLowerCornerOf(right.getNormal()).scale(column * SPACING));
		// Horizontal funnels: nudge the slots out of the model so the funnel frame does not cover them
		// (not for the extended belt funnel, whose slot sits on the top face)
		if (funnelFacing.getAxis().isHorizontal() && getSide().getAxis().isHorizontal())
			offset = offset.add(Vec3.atLowerCornerOf(face.getNormal()).scale(OUTWARD));
		return offset;
	}

	@Override
	public boolean testHit(LevelAccessor level, BlockPos pos, BlockState state, Vec3 localHit) {
		if (!isSideActive(state, getSide()))
			return false;
		Vec3 offset = getLocalOffset(level, pos, state);
		return offset != null && localHit.distanceTo(offset) < HIT_RADIUS;
	}

}
