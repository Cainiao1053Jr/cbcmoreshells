package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_antiair_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;


public class NormalAntiairHEShellBlock extends GeneralDualCannonProjectileBlock<NormalAntiairHEShellProjectile> {

	private static final MapCodec<NormalAntiairHEShellBlock> CODEC = simpleCodec(NormalAntiairHEShellBlock::new);

	public NormalAntiairHEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends NormalAntiairHEShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.NORMAL_ANTIAIR_HE_SHELL.get();
	}

}
