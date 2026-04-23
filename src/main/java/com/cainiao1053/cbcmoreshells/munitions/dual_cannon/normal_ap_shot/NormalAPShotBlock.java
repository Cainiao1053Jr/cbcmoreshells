package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shot;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.InertDualCannonProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class NormalAPShotBlock extends InertDualCannonProjectileBlock<NormalAPShotProjectile> {

	private static final MapCodec<NormalAPShotBlock> CODEC = simpleCodec(NormalAPShotBlock::new);

	public NormalAPShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public EntityType<? extends NormalAPShotProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.NORMAL_AP_SHOT.get();
	}

}
