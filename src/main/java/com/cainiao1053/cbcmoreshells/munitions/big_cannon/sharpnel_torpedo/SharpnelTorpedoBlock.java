package com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class SharpnelTorpedoBlock extends GeneralCannonTorpedoBlock<SharpnelTorpedoProjectile> {

	private static final MapCodec<SharpnelTorpedoBlock> CODEC = simpleCodec(SharpnelTorpedoBlock::new);

	public SharpnelTorpedoBlock(Properties properties) {
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
	public EntityType<? extends SharpnelTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.SHARPNEL_TORPEDO.get();
	}

}
