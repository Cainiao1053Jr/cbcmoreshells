package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.client;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonData;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonMunitionRegistry;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonMunitionProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonState;
import com.verr1.shaolib.api.projectile.ProjectileType;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyResolver;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyType;
import com.verr1.shaolib.projectile.client.ClientProjectileBehaviors;
import net.minecraft.resources.ResourceLocation;

public final class CBCMSDualCannonProjectileClient {

	private CBCMSDualCannonProjectileClient() {}

	public static void register() {
		for (CBCMSDualCannonMunitionRegistry.Entry entry : CBCMSDualCannonMunitionRegistry.all()) {
			register(entry.projectileType(), entry.propertyType(), entry.renderedBlock());
		}
	}

	private static <P extends DualCannonMunitionProperties> void register(ProjectileType<DualCannonState> type,
																		  MunitionPropertyType<P> propertyType,
																		  ResourceLocation renderedBlock) {
		MunitionPropertyResolver<P> resolver =
			MunitionPropertyResolver.encodedJson(propertyType, CBCMSDualCannonData.DYNAMIC_PROPERTIES);
		ClientProjectileBehaviors.register(type, new DualCannonProjectileClientBehavior<>(resolver, renderedBlock));
	}

}
