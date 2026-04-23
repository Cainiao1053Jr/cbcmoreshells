package com.cainiao1053.cbcmoreshells.munitions.big_cannon.sap_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;


public class SAPShellBlock extends ShellessShellBlock<SAPShellProjectile> {

	private static final MapCodec<SAPShellBlock> CODEC = simpleCodec(SAPShellBlock::new);

	public SAPShellBlock(Properties properties) {
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
	public EntityType<? extends SAPShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.SAP_SHELL.get();
	}




}
