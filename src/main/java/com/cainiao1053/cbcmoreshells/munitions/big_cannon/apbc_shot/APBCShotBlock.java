package com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shot;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class APBCShotBlock extends ShellessInertProjectileBlock<APBCShotProjectile> {

	private static final MapCodec<APBCShotBlock> CODEC = simpleCodec(APBCShotBlock::new);

	public APBCShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends APBCShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.APBC_SHOT.get();
	}

}
