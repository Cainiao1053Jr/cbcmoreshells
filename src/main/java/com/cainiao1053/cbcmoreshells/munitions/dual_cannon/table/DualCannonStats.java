package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonModifiers;
import com.cainiao1053.cbcmoreshells.utils.CBCMSBallisticUtils;

public final class DualCannonStats {

	private static final String PREFIX = "cbcmoreshells.firing_table.stat.";

	private DualCannonStats() {}

	public static final StatSpec<DualCannonShellContext> MUZZLE_VELOCITY =
		shellStat("muzzle_velocity", DualCannonShellContext::muzzleVelocity, StatFormat.M_PER_SEC);

	public static final StatSpec<DualCannonShellContext> DRAG =
		shellStat("drag", DualCannonShellContext::drag, StatFormat.PLAIN3);

	public static final StatSpec<DualCannonShellContext> GRAVITY =
		shellStat("gravity", DualCannonShellContext::gravity, StatFormat.PLAIN3);

	public static final StatSpec<DualCannonShellContext> BASE_MASS =
		shellStat("base_mass", DualCannonShellContext::baseMass, StatFormat.PLAIN2);

	public static final StatSpec<DualCannonShellContext> BASE_LIFETIME =
		shellStat("base_lifetime", DualCannonShellContext::baseLifetimeTicks, StatFormat.SECONDS);

	/** Steepest incidence that can still bounce, as an angle off the surface normal. */
	public static final StatSpec<DualCannonShellContext> DEFLECTION_ANGLE =
		shellStat("deflection_angle", shell -> {
			double cosine = shell.deflection();
			return cosine <= 0.0 || cosine > 1.0 ? Double.NaN : Math.acos(cosine);
		}, StatFormat.DEGREES);

	public static final StatSpec<DualCannonShellContext> BOUNCE_ANGLE =
			shellStat("bounce_angle", shell -> {
				double cosine = shell.minDeflection();
				return cosine <= 0.0 || cosine > 1.0 ? Double.NaN : Math.acos(cosine);
			}, StatFormat.DEGREES);

	public static final StatSpec<DualCannonShellContext> MAX_MOMENTUM =
		shellStat("max_momentum", DualCannonShellContext::maxMomentum, StatFormat.INTEGER);

	public static final StatSpec<DualCannonShellContext> SMASH_TOUGHNESS =
		shellStat("smash_toughness", DualCannonShellContext::smashToughness, StatFormat.PLAIN2);

	/** Furthest the shell can land on the muzzle's own level, ignoring lifetime. */
	public static final StatSpec<DualCannonShellContext> MAX_RANGE =
		shellStat("max_range", shell -> {
			CBCMSBallisticUtils.DualCannonShot shot = maxRange(shell);
			return shot == null ? Double.NaN : shot.range();
		}, StatFormat.PLAIN1);

	/** Elevation that reaches {@link #MAX_RANGE}. Drag pulls it well below 45 degrees. */
	public static final StatSpec<DualCannonShellContext> OPTIMAL_ELEVATION =
		shellStat("optimal_elevation", shell -> {
			CBCMSBallisticUtils.DualCannonShot shot = maxRange(shell);
			return shot == null ? Double.NaN : shot.angle();
		}, StatFormat.DEGREES);

	/** Ceiling on the AA altitude bonus. The multiplier itself does not depend on the barrel. */
	public static final StatSpec<DualCannonShellContext> AA_ALTITUDE_BONUS =
		shellStat("aa_altitude_bonus", shell -> DualCannonModifiers.antiairAltitude(Double.MAX_VALUE),
			StatFormat.PLAIN1);

	public static final StatSpec<DualCannonLoadout> LIFETIME =
		materialStat("lifetime", DualCannonLoadout::lifetimeTicks, StatFormat.SECONDS,
			StatSpec.PRIORITY_REQUIRED);

	public static final StatSpec<DualCannonLoadout> RELOAD =
		materialStat("reload", DualCannonLoadout::reloadCoefficient, StatFormat.PLAIN2,
			StatSpec.PRIORITY_NORMAL);

	public static final StatSpec<DualCannonLoadout> RECOIL =
			materialStat("recoil", DualCannonLoadout::recoil, StatFormat.PLAIN2,
					StatSpec.PRIORITY_NORMAL);

	public static final StatSpec<DualCannonLoadout> EXPLOSION_POWER =
		materialStat("explosion_power", DualCannonLoadout::explosionPower, StatFormat.PLAIN2,
			StatSpec.PRIORITY_HIGH);

	public static final StatSpec<DualCannonLoadout> FIRE_CHANCE =
		materialStat("fire_chance", DualCannonLoadout::fireChance, StatFormat.PERCENT, 85);

	public static final StatSpec<DualCannonLoadout> FIRE_RANGE =
		materialStat("fire_range", DualCannonLoadout::fireRange, StatFormat.PLAIN1, 85);

	/** Not shown by default; handy when comparing penetration by hand. */
	public static final StatSpec<DualCannonLoadout> EFFECTIVE_MASS =
		materialStat("effective_mass", DualCannonLoadout::effectiveMass, StatFormat.PLAIN2,
			StatSpec.PRIORITY_LOW);

	private static StatSpec<DualCannonShellContext> shellStat(String name,
															  java.util.function.ToDoubleFunction<DualCannonShellContext> value,
															  StatFormat format) {
		return StatSpec.of(PREFIX + name, value, format);
	}

	private static StatSpec<DualCannonLoadout> materialStat(String name,
															java.util.function.ToDoubleFunction<DualCannonLoadout> value,
															StatFormat format, int priority) {
		return StatSpec.of(PREFIX + name, value, format, priority);
	}

	private static CBCMSBallisticUtils.DualCannonShot maxRange(DualCannonShellContext shell) {
		if (!shell.ballisticsSupported()) return null;
		return CBCMSBallisticUtils.dualCannonMaxRangeWithLimit(shell.muzzleVelocity(), shell.drag(), shell.gravity(), shell.runtimeMaxRange());
	}

}
