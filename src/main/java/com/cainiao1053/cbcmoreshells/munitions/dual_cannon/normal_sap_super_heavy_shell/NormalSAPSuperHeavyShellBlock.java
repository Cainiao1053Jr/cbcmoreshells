package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_super_heavy_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import net.minecraft.world.entity.EntityType;


public class NormalSAPSuperHeavyShellBlock extends GeneralDualCannonProjectileBlock<NormalSAPSuperHeavyShellProjectile> {

	public NormalSAPSuperHeavyShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

//	@Override
//	public int getLifetimeFromBlock() {
//		return CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).lifetime();
//	}

	@Override
	public EntityType<? extends NormalSAPSuperHeavyShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.NORMAL_SAP_SUPER_HEAVY_SHELL.get();
	}

}
