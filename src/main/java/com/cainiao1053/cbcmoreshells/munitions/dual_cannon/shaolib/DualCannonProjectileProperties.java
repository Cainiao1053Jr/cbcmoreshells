package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.google.gson.JsonObject;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyComponents;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyType;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyTypes;
import java.util.Objects;
import java.util.function.Supplier;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record DualCannonProjectileProperties(
	MunitionPropertyComponents.BallisticsProperties ballistics,
	MunitionPropertyComponents.RuntimeProperties runtime,
	MunitionPropertyComponents.ImpactProperties impact,
	MunitionPropertyComponents.EffectProperties effects,
	MunitionPropertyComponents.EntityDamageProperties damage,
	DualCannonLaunchProperties dualCannon,
	DualCannonImpactProperties dualImpact
) implements DualCannonMunitionProperties {

	public DualCannonProjectileProperties {
		Objects.requireNonNull(ballistics, "ballistics");
		Objects.requireNonNull(runtime, "runtime");
		Objects.requireNonNull(impact, "impact");
		Objects.requireNonNull(effects, "effects");
		Objects.requireNonNull(damage, "damage");
		Objects.requireNonNull(dualCannon, "dualCannon");
		Objects.requireNonNull(dualImpact, "dualImpact");
	}

	public static MunitionPropertyType<DualCannonProjectileProperties> createType(ResourceLocation id,
																				  Supplier<DualCannonProjectileProperties> fallback) {
		return MunitionPropertyTypes.register(new MunitionPropertyType<>(id, DualCannonProjectileProperties.class,
			fallback, DualCannonProjectileProperties::fromJson, DualCannonProjectileProperties::write,
			DualCannonProjectileProperties::read));
	}

	public static DualCannonProjectileProperties normalApShotFallback() {
		return base("normal_ap_shot", 6.0, 0.78, 0.0F, 52.0F);
	}

	public static DualCannonProjectileProperties normalHeShellFallback() {
		return base("normal_he_shell", 2.5, 0.7, 5.5F, 32.0F);
	}

	public static DualCannonProjectileProperties normalAntiairHeShellFallback() {
		return base("normal_antiair_he_shell", 2.2, 0.7, 4.5F, 28.0F);
	}

	public static DualCannonProjectileProperties normalApShellFallback() {
		return base("normal_ap_shell", 4.8, 0.72, 4.0F, 44.0F);
	}

	public static DualCannonProjectileProperties normalApbcShellFallback() {
		return base("normal_apbc_shell", 4.6, 0.84, 3.8F, 42.0F);
	}

	public static DualCannonProjectileProperties normalSapShellFallback() {
		return base("normal_sap_shell", 3.8, 0.75, 4.8F, 38.0F);
	}

	public static DualCannonProjectileProperties normalSapSuperHeavyShellFallback() {
		return base("normal_sap_super_heavy_shell", 3.8, 0.75, 4.8F, 38.0F);
	}

	public static DualCannonProjectileProperties extendedSapShellFallback() {
		return base("extended_sap_shell", 3.8, 0.75, 4.8F, 38.0F);
	}

	public static DualCannonProjectileProperties extendedApShotFallback() {
		return withReach(normalApShotFallback(), 1.5);
	}

	public static DualCannonProjectileProperties extendedAntiairHeShellFallback() {
		return withReach(normalAntiairHeShellFallback(), 1.5);
	}

	public static DualCannonProjectileProperties ballFallback() {
		return base("normal_ap_shot", 6.0, 0.78, 0.0F, 52.0F);
	}

	private static DualCannonProjectileProperties base(String projectileBlock, double durabilityMass, double deflection,
													   float explosionPower, float entityDamage) {
		MunitionPropertyComponents.ExplosionKind kind = explosionPower <= 0.0F
			? MunitionPropertyComponents.ExplosionKind.NONE
			: MunitionPropertyComponents.ExplosionKind.CBC_SHELL;
		return new DualCannonProjectileProperties(
			new MunitionPropertyComponents.BallisticsProperties(-0.05, 0.01, false, durabilityMass, 2.0, 0.5, deflection),
			new MunitionPropertyComponents.RuntimeProperties(180.0, 80, 12),
			new MunitionPropertyComponents.ImpactProperties(0.25, 32, 1, true, true, 0.05, 2.0, 0.15, 0.62, 0.92, 0.25,
				0.08, 0.42),
			new MunitionPropertyComponents.EffectProperties(true, true, true, Cbcmoreshells.resource(projectileBlock),
				new MunitionPropertyComponents.ExplosionProperties(kind, explosionPower, false, false)),
			new MunitionPropertyComponents.EntityDamageProperties(entityDamage, true, false, false, 2.0F),
			DualCannonLaunchProperties.DEFAULT,
			DualCannonImpactProperties.DEFAULT);
	}

	private static DualCannonProjectileProperties withReach(DualCannonProjectileProperties base, double reachScale) {
		MunitionPropertyComponents.RuntimeProperties runtime = base.runtime();
		return new DualCannonProjectileProperties(base.ballistics(),
			new MunitionPropertyComponents.RuntimeProperties(runtime.maxDistance() * reachScale,
				runtime.terminalTtlTicks(), runtime.detonationTtlTicks()),
			base.impact(), base.effects(), base.damage(), base.dualCannon(), base.dualImpact());
	}

	// ---------------------------------------------------------------------------------------------
	// Codecs
	// ---------------------------------------------------------------------------------------------

	private static DualCannonProjectileProperties fromJson(JsonObject json, DualCannonProjectileProperties fallback) {
		return new DualCannonProjectileProperties(
			MunitionPropertyComponents.BallisticsProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "ballistics"), fallback.ballistics()),
			MunitionPropertyComponents.RuntimeProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "runtime"), fallback.runtime()),
			MunitionPropertyComponents.ImpactProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "impact"), fallback.impact()),
			MunitionPropertyComponents.EffectProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "effects"), fallback.effects()),
			MunitionPropertyComponents.EntityDamageProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "damage"), fallback.damage()),
			DualCannonLaunchProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "dual_cannon"), fallback.dualCannon()),
			DualCannonImpactProperties.fromJson(
				MunitionPropertyComponents.optionalObject(json, "dual_impact"), fallback.dualImpact()));
	}

	private static void write(RegistryFriendlyByteBuf buffer, DualCannonProjectileProperties properties) {
		MunitionPropertyComponents.writeBallistics(buffer, properties.ballistics());
		MunitionPropertyComponents.writeRuntime(buffer, properties.runtime());
		MunitionPropertyComponents.writeImpact(buffer, properties.impact());
		MunitionPropertyComponents.writeEffects(buffer, properties.effects());
		MunitionPropertyComponents.writeEntityDamage(buffer, properties.damage());
		DualCannonLaunchProperties.write(buffer, properties.dualCannon());
		DualCannonImpactProperties.write(buffer, properties.dualImpact());
	}

	private static DualCannonProjectileProperties read(RegistryFriendlyByteBuf buffer) {
		return new DualCannonProjectileProperties(
			MunitionPropertyComponents.readBallistics(buffer),
			MunitionPropertyComponents.readRuntime(buffer),
			MunitionPropertyComponents.readImpact(buffer),
			MunitionPropertyComponents.readEffects(buffer),
			MunitionPropertyComponents.readEntityDamage(buffer),
			DualCannonLaunchProperties.read(buffer),
			DualCannonImpactProperties.read(buffer));
	}

}
