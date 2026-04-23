package com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;


public class ShellessHEShellBlock extends ShellessShellBlock<ShellessHEShellProjectile> {

	private static final MapCodec<ShellessHEShellBlock> CODEC = simpleCodec(ShellessHEShellBlock::new);

	public ShellessHEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends ShellessHEShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.SHELLESS_HE_SHELL.get();
	}

}
