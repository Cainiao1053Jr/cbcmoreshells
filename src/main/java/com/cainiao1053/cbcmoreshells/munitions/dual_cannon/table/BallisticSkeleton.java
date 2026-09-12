package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.utils.CBCMSBallisticUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public record BallisticSkeleton(DualCannonShellContext shell, boolean highArc, double maxRange,
								double optimalElevation, List<BallisticPoint> points) {

	public BallisticSkeleton {
		points = List.copyOf(points);
	}

	@Nullable
	public static BallisticSkeleton compute(DualCannonShellContext shell, int columns, boolean highArc) {
		if (columns < 1 || !shell.ballisticsSupported()) return null;

		double v0 = shell.muzzleVelocity();
		double drag = shell.drag();
		double gravity = shell.gravity();
		double maxRange = shell.runtimeMaxRange();

		CBCMSBallisticUtils.DualCannonShot furthest =
			CBCMSBallisticUtils.dualCannonMaxRangeWithLimit(v0, drag, gravity, maxRange);
		if (furthest == null || !(furthest.range() > 0.0)) return null;

		List<CBCMSBallisticUtils.DualCannonShot> shots =
			CBCMSBallisticUtils.dualCannonRangeTable(v0, drag, gravity, columns, highArc, maxRange);
		if (shots.isEmpty()) return null;

		List<BallisticPoint> points = new ArrayList<>(shots.size() + 1);
		points.add(new BallisticPoint(0.0, 0.0, 0.0, v0));
		for (CBCMSBallisticUtils.DualCannonShot shot : shots) {
			points.add(new BallisticPoint(shot.range(), shot.angle(), shot.flightTicks(), shot.impactSpeed()));
		}
		return new BallisticSkeleton(shell, highArc, furthest.range(), furthest.angle(), points);
	}

	public int columns() {
		return this.points.size();
	}

	public BallisticPoint point(int column) {
		return this.points.get(column);
	}

	public double value(BallisticColumnMode mode, int column) {
		BallisticPoint point = this.point(column);
		return switch (mode) {
			case FLIGHT_TIME -> point.flightTicks();
			case IMPACT_SPEED -> point.impactSpeed();
			case MOMENTUM -> Double.NaN;
		};
	}

}
