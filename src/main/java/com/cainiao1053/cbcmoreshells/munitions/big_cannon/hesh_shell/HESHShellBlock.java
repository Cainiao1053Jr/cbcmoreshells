package com.cainiao1053.cbcmoreshells.munitions.big_cannon.hesh_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;


public class HESHShellBlock extends ShellessShellBlock<HESHShellProjectile> {

	private static final MapCodec<HESHShellBlock> CODEC = simpleCodec(HESHShellBlock::new);

	public HESHShellBlock(Properties properties) {
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
	public EntityType<? extends HESHShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.HESH_SHELL.get();
	}




}
