package com.cainiao1053.cbcmoreshells.munitions.big_cannon.shellless_ap_shot;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ShellessAPShotBlock extends ShellessInertProjectileBlock<ShellessAPShotProjectile> {

	private static final MapCodec<ShellessAPShotBlock> CODEC = simpleCodec(ShellessAPShotBlock::new);

	public ShellessAPShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends ShellessAPShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.SHELLESS_AP_SHOT.get();
	}

}
