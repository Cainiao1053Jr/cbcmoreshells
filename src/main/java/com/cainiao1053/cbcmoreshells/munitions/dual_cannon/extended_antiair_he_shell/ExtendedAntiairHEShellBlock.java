package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_antiair_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;


public class ExtendedAntiairHEShellBlock extends GeneralDualCannonProjectileBlock<ExtendedAntiairHEShellProjectile> {

	private static final MapCodec<ExtendedAntiairHEShellBlock> CODEC = simpleCodec(ExtendedAntiairHEShellBlock::new);

	public ExtendedAntiairHEShellBlock(Properties properties) {
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
	public EntityType<? extends ExtendedAntiairHEShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.EXTENDED_ANTIAIR_HE_SHELL.get();
	}

}
