package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_ap_shot;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.InertDualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonProjectiles;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonState;
import com.mojang.serialization.MapCodec;
import com.verr1.shaolib.api.projectile.ProjectileType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ExtendedAPShotBlock extends InertDualCannonProjectileBlock {

	private static final MapCodec<ExtendedAPShotBlock> CODEC = simpleCodec(ExtendedAPShotBlock::new);

	public ExtendedAPShotBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public ProjectileType<DualCannonState> getAssociatedProjectile() {
		return CBCMSDualCannonProjectiles.EXTENDED_AP_SHOT;
	}

}
