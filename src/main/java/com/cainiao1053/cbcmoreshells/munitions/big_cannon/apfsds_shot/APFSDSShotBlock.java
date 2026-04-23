package com.cainiao1053.cbcmoreshells.munitions.big_cannon.apfsds_shot;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class APFSDSShotBlock extends ShellessInertProjectileBlock<APFSDSShotProjectile> {

	private static final MapCodec<APFSDSShotBlock> CODEC = simpleCodec(APFSDSShotBlock::new);

	public APFSDSShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends APFSDSShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.APFSDS_SHOT.get();
	}

}
