package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.light_apbc_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import net.minecraft.world.entity.EntityType;


public class LightAPBCShellBlock extends GeneralDualCannonProjectileBlock<LightAPBCShellProjectile> {

	public LightAPBCShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends LightAPBCShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.LIGHT_APBC_SHELL.get();
	}

}
