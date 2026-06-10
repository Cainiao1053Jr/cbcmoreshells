package com.cainiao1053.cbcmoreshells.munitions.flak_cannon.config;

import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record InertFlakcannonProjectileProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
												  FlakcannonProjectilePropertiesComponent autocannonProperties) {
}
