package com.cainiao1053.cbcmoreshells.munitions.big_cannon.baguette_shot;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class BaguetteShotBlock extends ShellessInertProjectileBlock<BaguetteShotProjectile> {

	private static final MapCodec<BaguetteShotBlock> CODEC = simpleCodec(BaguetteShotBlock::new);

	public BaguetteShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends BaguetteShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.BAGUETTE_SHOT.get();
	}

}
