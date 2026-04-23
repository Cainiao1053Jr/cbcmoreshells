package com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;


public class AntiairShrapnelShellBlock extends ShellessShellBlock<AntiairShrapnelShellProjectile> {

	private static final MapCodec<AntiairShrapnelShellBlock> CODEC = simpleCodec(AntiairShrapnelShellBlock::new);

	public AntiairShrapnelShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.SHRAPNEL_SHELLESS_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends AntiairShrapnelShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.ANTIAIR_SHRAPNEL_SHELL.get();
	}

}
