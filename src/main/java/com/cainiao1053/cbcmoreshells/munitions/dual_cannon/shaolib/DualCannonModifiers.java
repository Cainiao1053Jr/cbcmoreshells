package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

/**
 * How a barrel's durability mass modifier scales a shell's non-ballistic payload.
 *
 * <p>These used to live on {@link DualCannonBehavior} as instance methods reading the projectile
 * context, which made them unreachable to anything without a live shell — firing tables and
 * tooltips had to copy the formulas and would drift out of sync. They are static here so the
 * behavior and any UI share one definition.
 */
public final class DualCannonModifiers {

	/** Metres of climb that buy one extra multiple of AA burst power. */
	private static final double ANTIAIR_ALTITUDE_SCALE = 80.0;
	private static final double ANTIAIR_MAX_MULTIPLIER = 3.0;

	private DualCannonModifiers() {}

	/** Multiplier applied to {@code effects().explosion().power()} for this shell class. */
	public static double explosionPower(DualCannonBehavior.Kind kind, double durabilityModifier) {
		return switch (kind) {
			case AP_SHOT -> 0.0;
			case HE, INCENDIARY -> (durabilityModifier - 1.0) / 1.2 + 1.0;
			case ANTIAIR_HE -> (durabilityModifier - 1.0) / 1.5 + 1.0;
			case APHE -> durabilityModifier * 0.47 + 0.4;
			case APBC -> durabilityModifier * 0.45 + 0.4;
			case SAP, HSAP -> durabilityModifier * 0.82 + 0.18;
		};
	}

	/**
	 * AA shells burst harder the higher they climb, up to {@link #ANTIAIR_MAX_MULTIPLIER}. Stacks
	 * on top of {@link #explosionPower}; a firing table with no altitude to report should use 1.0.
	 */
	public static double antiairAltitude(double climbBlocks) {
		return Math.min(Math.max(0.0, climbBlocks) / ANTIAIR_ALTITUDE_SCALE + 1.0, ANTIAIR_MAX_MULTIPLIER);
	}

	/** Multiplier applied to {@code incendiary().fireChance()}. */
	public static double incendiaryFire(double durabilityModifier) {
		return (durabilityModifier - 1.0) / 0.9 + 1.0;
	}

	/** Multiplier applied to {@code incendiary().fireRange()}. */
	public static double incendiaryRange(double durabilityModifier) {
		return (durabilityModifier - 1.0) / 1.3 + 1.0;
	}

}
