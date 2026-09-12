package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.utils.CBCMSBallisticUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * The distance columns of a firing table, computed once per shell and shared by every material row.
 *
 * <p>Nothing in here depends on the barrel, so switching material never recomputes it. Only the
 * equivalent momentum and the reachability flags are per material, and those are cheap.
 *
 * @param maxRange         furthest the shell can land on the muzzle's own level, ignoring lifetime
 * @param optimalElevation elevation that reaches {@link #maxRange}, in radians
 * @param points           evenly spaced up to {@link #maxRange}, nearest first
 */
public record BallisticSkeleton(DualCannonShellContext shell, boolean highArc, double maxRange,
								double optimalElevation, List<BallisticPoint> points) {

	public BallisticSkeleton {
		points = List.copyOf(points);
	}

	/**
	 * Samples distances up to the shell's maximum range, preceded by a muzzle column.
	 *
	 * @param columns how many downrange samples to take; the returned skeleton has one more column
	 *                than this, because distance zero is always prepended
	 * @return null when the shell's ballistics fall outside what the closed form covers, e.g.
	 *         quadratic drag or non-negative gravity
	 */
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
		// Muzzle column. Needs no solving: at zero distance the shell has flown for no time and
		// still carries its full muzzle velocity, whatever the elevation. It anchors the table's
		// left edge so the fall-off across the row reads against a known starting point.
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

	/**
	 * The value a distance column shows for a material-independent mode. Momentum is not one of
	 * these — ask the {@link MaterialRow} for that.
	 */
	public double value(BallisticColumnMode mode, int column) {
		BallisticPoint point = this.point(column);
		return switch (mode) {
			case FLIGHT_TIME -> point.flightTicks();
			case IMPACT_SPEED -> point.impactSpeed();
			case MOMENTUM -> Double.NaN;
		};
	}

}
