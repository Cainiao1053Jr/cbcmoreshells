package com.cainiao1053.cbcmoreshells.munitions.big_cannon.inferior_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.munitions.big_cannon.SimpleShellBlock;


public class InferiorHEShellBlock extends SimpleShellBlock<InferiorHEShellProjectile> {

	private static final MapCodec<InferiorHEShellBlock> CODEC = simpleCodec(InferiorHEShellBlock::new);

	public InferiorHEShellBlock(Properties properties) {
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
	public EntityType<? extends InferiorHEShellProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.Inferior_HE_SHELL.get();
	}




}
