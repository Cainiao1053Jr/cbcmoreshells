package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_ap_shot;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.InertDualCannonProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ExtendedAPShotBlock extends InertDualCannonProjectileBlock<ExtendedAPShotProjectile> {

	private static final MapCodec<ExtendedAPShotBlock> CODEC = simpleCodec(ExtendedAPShotBlock::new);

	public ExtendedAPShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends ExtendedAPShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.EXTENDED_AP_SHOT.get();
	}

}
