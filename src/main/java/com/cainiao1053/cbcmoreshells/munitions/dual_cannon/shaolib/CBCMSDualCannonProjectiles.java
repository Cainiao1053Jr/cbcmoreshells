package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.verr1.shaolib.api.projectile.ProjectileOrientationSync;
import com.verr1.shaolib.api.projectile.ProjectileType;
import com.verr1.shaolib.api.projectile.ShaolibProjectiles;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyResolver;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyType;
import com.verr1.shaolib.munitions.projectile.motion.MunitionMotionModels;

public final class CBCMSDualCannonProjectiles {
	private static final int MAX_LIFETIME_TICKS = 600;
	private static final int SYNC_INTERVAL_TICKS = 10;

	public static final ProjectileType<DualCannonState> NORMAL_AP_SHOT =
		register("normal_ap_shot", CBCMSDualCannonPropertyTypes.NORMAL_AP_SHOT, DualCannonBehavior.Kind.AP_SHOT);

	public static final ProjectileType<DualCannonState> EXTENDED_AP_SHOT =
		register("extended_ap_shot", CBCMSDualCannonPropertyTypes.EXTENDED_AP_SHOT, DualCannonBehavior.Kind.AP_SHOT);

	public static final ProjectileType<DualCannonState> NORMAL_HE_SHELL =
		register("normal_he_shell", CBCMSDualCannonPropertyTypes.NORMAL_HE_SHELL, DualCannonBehavior.Kind.HE);

	public static final ProjectileType<DualCannonState> NORMAL_ANTIAIR_HE_SHELL =
		register("normal_antiair_he_shell", CBCMSDualCannonPropertyTypes.NORMAL_ANTIAIR_HE_SHELL,
			DualCannonBehavior.Kind.ANTIAIR_HE);

	public static final ProjectileType<DualCannonState> EXTENDED_ANTIAIR_HE_SHELL =
		register("extended_antiair_he_shell", CBCMSDualCannonPropertyTypes.EXTENDED_ANTIAIR_HE_SHELL,
			DualCannonBehavior.Kind.ANTIAIR_HE);

	public static final ProjectileType<DualCannonState> NORMAL_AP_SHELL =
		register("normal_ap_shell", CBCMSDualCannonPropertyTypes.NORMAL_AP_SHELL, DualCannonBehavior.Kind.APHE);

	public static final ProjectileType<DualCannonState> NORMAL_APBC_SHELL =
		register("normal_apbc_shell", CBCMSDualCannonPropertyTypes.NORMAL_APBC_SHELL, DualCannonBehavior.Kind.APBC);

	public static final ProjectileType<DualCannonState> NORMAL_SAP_SHELL =
		register("normal_sap_shell", CBCMSDualCannonPropertyTypes.NORMAL_SAP_SHELL, DualCannonBehavior.Kind.SAP);

	public static final ProjectileType<DualCannonState> NORMAL_SAP_SUPER_HEAVY_SHELL =
			register("normal_sap_super_heavy_shell", CBCMSDualCannonPropertyTypes.NORMAL_SAP_SUPER_HEAVY_SHELL, DualCannonBehavior.Kind.HSAP);

	public static final ProjectileType<DualCannonState> EXTENDED_SAP_SHELL =
			register("extended_sap_shell", CBCMSDualCannonPropertyTypes.EXTENDED_SAP_SHELL, DualCannonBehavior.Kind.SAP);

	public static final ProjectileType<DualCannonState> BALL =
			register("ball", CBCMSDualCannonPropertyTypes.BALL, DualCannonBehavior.Kind.AP_SHOT);

	public static final ProjectileType<DualCannonState> NORMAL_INCENDIARY_HE_SHELL =
		register("normal_incendiary_he_shell", CBCMSDualCannonPropertyTypes.NORMAL_INCENDIARY_HE_SHELL,
			DualCannonBehavior.Kind.INCENDIARY);

	private CBCMSDualCannonProjectiles() {}

	public static void register() {}

	private static <P extends DualCannonMunitionProperties> ProjectileType<DualCannonState> register(
		String path, MunitionPropertyType<P> propertyType, DualCannonBehavior.Kind kind) {

		// encodedJson rather than configured: each shot can carry overrides for the barrel it was
		// fired from, falling back to the datapack config when it carries none.
		MunitionPropertyResolver<P> resolver =
			MunitionPropertyResolver.encodedJson(propertyType, CBCMSDualCannonData.DYNAMIC_PROPERTIES);

		return ShaolibProjectiles.register(
			ProjectileType.<DualCannonState>builder(Cbcmoreshells.resource(path),
					(projectile, level) -> new DualCannonState())
				.schema(CBCMSDualCannonData.SCHEMA)
				.motionModel(MunitionMotionModels.configured(resolver))
				.orientationSync(ProjectileOrientationSync.NONE)
				.behavior(new DualCannonBehavior<>(resolver, kind))
				.serializer(new DualCannonSerializer<DualCannonState>())
				.syncIntervalTicks(SYNC_INTERVAL_TICKS)
				.maxLifetimeTicks(MAX_LIFETIME_TICKS)
				.build());
	}

}
