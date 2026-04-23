package com.cainiao1053.cbcmoreshells.munitions.big_cannon.deepwater_shrapnel_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class DeepwaterShrapnelTorpedoBlock extends GeneralCannonTorpedoBlock<DeepwaterShrapnelTorpedoProjectile> {

	private static final MapCodec<DeepwaterShrapnelTorpedoBlock> CODEC = simpleCodec(DeepwaterShrapnelTorpedoBlock::new);

	public DeepwaterShrapnelTorpedoBlock(Properties properties) {
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
	public EntityType<? extends DeepwaterShrapnelTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.DEEPWATER_SHRAPNEL_TORPEDO.get();
	}

}
