package com.cainiao1053.cbcmoreshells.munitions.big_cannon.aphe_super_heavy;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;


public class APHESuperHeavyShellBlock extends ShellessShellBlock<APHESuperHeavyShellProjectile> {

	public APHESuperHeavyShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends APHESuperHeavyShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.APHE_SUPER_HEAVY_SHELL.get();
	}

}
