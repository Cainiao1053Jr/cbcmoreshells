package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonMunitionRegistry;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonBehavior;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonIncendiaryProjectileProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonMunitionProperties;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyComponents;

import javax.annotation.Nullable;
import java.util.Objects;
import net.minecraft.world.level.block.Block;

/**
 * A shell on its own, with no barrel attached. Everything reachable from here is fixed by the
 * datapack: muzzle velocity, drag and gravity carry no barrel modifier, and per-shot overrides only
 * touch durability mass, so these are the values the round really flies with.
 *
 * <p>Stats that read this context therefore cannot vary between barrel materials, which is the
 * point — they belong in the table's header block, not in the per-material rows.
 */
public record DualCannonShellContext(FuzedDualCannonProjectileBlockItem item,
									 CBCMSDualCannonMunitionRegistry.Entry entry,
									 DualCannonMunitionProperties shell) {

	public DualCannonShellContext {
		Objects.requireNonNull(item, "item");
		Objects.requireNonNull(entry, "entry");
		Objects.requireNonNull(shell, "shell");
	}

	/**
	 * Resolves a projectile block into a context, or null when the block is not a registered dual
	 * cannon round or its item does not carry the firing table hooks.
	 */
	@Nullable
	public static DualCannonShellContext of(Block block) {
		if (!(block.asItem() instanceof FuzedDualCannonProjectileBlockItem item)) return null;
		CBCMSDualCannonMunitionRegistry.Entry entry = item.getMunitionEntry();
		DualCannonMunitionProperties properties = item.getProjectileProperties();
		return entry == null || properties == null ? null : new DualCannonShellContext(item, entry, properties);
	}

	public DualCannonBehavior.Kind kind() {
		return this.entry.kind();
	}

	/** Blocks per tick. Tooltips show this multiplied by 20. */
	public double muzzleVelocity() {
		return this.shell.dualCannon().initialVelocity();
	}

	public double runtimeMaxRange(){
		return this.shell.runtime().maxDistance();
	}

	/** Fraction of speed lost per tick. */
	public double drag() {
		return this.shell.ballistics().drag();
	}

	/** Blocks per tick squared, negative. */
	public double gravity() {
		return this.shell.ballistics().gravity();
	}

	/** Durability mass before the barrel's modifier. */
	public double baseMass() {
		return this.shell.ballistics().durabilityMass();
	}

	/** Cosine of the steepest incidence that can still ricochet. */
	public double deflection() {
		return this.shell.ballistics().deflection();
	}

	public double minDeflection(){
		return this.shell.dualImpact().minDeflection();
	}

	public double maxMomentum() {
		return this.shell.dualImpact().maximumMomentum();
	}

	public double smashToughness() {
		return this.shell.dualImpact().smashToughness();
	}

	/** Ticks before the barrel's bonus. Not the profile's field, which never reaches a live shell. */
	public int baseLifetimeTicks() {
		return this.shell.dualCannon().baseLifetime();
	}

	/** Burst power before the barrel's durability modifier. Zero for solid shot. */
	public double baseExplosionPower() {
		return this.shell.effects().explosion().power();
	}

	@Nullable
	public MunitionPropertyComponents.IncendiaryProperties incendiary() {
		return this.shell instanceof DualCannonIncendiaryProjectileProperties incendiary
			? incendiary.incendiary()
			: null;
	}

	/**
	 * Whether {@code CBCMSBallisticUtils} can describe this shell's flight. Its closed form assumes
	 * linear drag pulling a shell back down, which every dual cannon round currently satisfies.
	 */
	public boolean ballisticsSupported() {
		return !this.shell.ballistics().quadraticDrag()
			&& this.muzzleVelocity() > 0.0
			&& this.gravity() < 0.0
			&& this.drag() >= 0.0;
	}

}
