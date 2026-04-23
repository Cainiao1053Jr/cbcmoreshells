package com.cainiao1053.cbcmoreshells.munitions.big_cannon.ap_super_heavy;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class APSuperHeavyShotBlock extends ShellessInertProjectileBlock<APSuperHeavyShotProjectile> {

	private static final MapCodec<APSuperHeavyShotBlock> CODEC = simpleCodec(APSuperHeavyShotBlock::new);

	public APSuperHeavyShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends APSuperHeavyShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.AP_SUPER_HEAVY_SHOT.get();
	}

}
