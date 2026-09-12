package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonModifiers;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyComponents;

import java.util.Objects;

public record DualCannonLoadout(DualCannonShellContext shell, DualCannonMaterial material,
								DualCannonMaterialProperties barrel,
								float commandModifier, float equipmentModifier) {

	public DualCannonLoadout {
		Objects.requireNonNull(shell, "shell");
		Objects.requireNonNull(material, "material");
		Objects.requireNonNull(barrel, "barrel");
	}

	public static DualCannonLoadout of(DualCannonShellContext shell, DualCannonMaterial material) {
		return new DualCannonLoadout(shell, material, material.properties(), 1.0F, 1.0F);
	}

	public double durabilityModifier() {
		return (double) this.barrel.durabilityMassModifier() * this.commandModifier * this.equipmentModifier;
	}

	public double effectiveMass() {
		return this.shell.baseMass() * this.durabilityModifier();
	}

	public int lifetimeTicks() {
		return this.shell.entry().launchProfile().resolveLifetimeTicks(this.shell.baseLifetimeTicks(),
			this.barrel.addedLifetime(), this.commandModifier, this.equipmentModifier);
	}

	/** Burst power after the barrel's modifier. Zero for solid shot. */
	public double explosionPower() {
		return this.shell.baseExplosionPower()
			* DualCannonModifiers.explosionPower(this.shell.kind(), this.durabilityModifier());
	}

	/** Chance to start a fire, or NaN when this shell carries no incendiary payload. */
	public double fireChance() {
		MunitionPropertyComponents.IncendiaryProperties incendiary = this.shell.incendiary();
		return incendiary == null
			? Double.NaN
			: incendiary.fireChance() * DualCannonModifiers.incendiaryFire(this.durabilityModifier());
	}

	/** Fire spread radius, or NaN when this shell carries no incendiary payload. */
	public double fireRange() {
		MunitionPropertyComponents.IncendiaryProperties incendiary = this.shell.incendiary();
		return incendiary == null
			? Double.NaN
			: incendiary.fireRange() * DualCannonModifiers.incendiaryRange(this.durabilityModifier());
	}

	/** Reload time multiplier for this pairing; lower is faster. */
	public double reloadCoefficient() {
		return this.shell.shell().dualCannon().reloadTimeCoefficient() * this.barrel.reloadTimeModifier() * 2.5;
	}

	public double recoil(){
		return this.shell.shell().dualCannon().baseRecoil() * this.barrel.recoilMultiplier();
	}

}
