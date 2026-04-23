package com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;


public class APBCShellBlock extends ShellessShellBlock<APBCShellProjectile> {

	private static final MapCodec<APBCShellBlock> CODEC = simpleCodec(APBCShellBlock::new);

	public APBCShellBlock(Properties properties) {
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
	public EntityType<? extends APBCShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.APBC_SHELL.get();
	}

}
