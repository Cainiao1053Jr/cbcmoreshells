package com.cainiao1053.cbcmoreshells.munitions.big_cannon.config;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public record TorpedoProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
											 TorpedoProjectilePropertiesComponent torpedoProperties,
											 BigCannonFuzePropertiesComponent fuze, ExplosionPropertiesComponent explosion, float maxCharges, int lifetime) {
}
