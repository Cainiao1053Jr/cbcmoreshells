package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;


public class NormalIncendiaryHEShellBlock extends GeneralDualCannonProjectileBlock<NormalIncendiaryHEShellProjectile> {

	private static final MapCodec<NormalIncendiaryHEShellBlock> CODEC = simpleCodec(NormalIncendiaryHEShellBlock::new);

	public NormalIncendiaryHEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.DUAL_CANNON_INCENDIARY_PROPERTIES.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

//	@Override
//	public int getLifetimeFromBlock() {
//		return CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).lifetime();
//	}

	@Override
	public EntityType<? extends NormalIncendiaryHEShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.NORMAL_INCENDIARY_HE_SHELL.get();
	}

}
