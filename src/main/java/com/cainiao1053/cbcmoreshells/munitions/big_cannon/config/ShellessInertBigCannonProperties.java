package com.cainiao1053.cbcmoreshells.munitions.big_cannon.config;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public record ShellessInertBigCannonProperties(BallisticPropertiesComponent ballistics, EntityDamagePropertiesComponent damage,
											 BigCannonProjectilePropertiesComponent bigCannonProperties, float maxCharges, int lifetime) {
}
