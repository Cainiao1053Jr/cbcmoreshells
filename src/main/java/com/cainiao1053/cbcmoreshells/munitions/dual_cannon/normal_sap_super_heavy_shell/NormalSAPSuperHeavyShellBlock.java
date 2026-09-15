package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_super_heavy_shell;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonProjectiles;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonState;
import com.mojang.serialization.MapCodec;
import com.verr1.shaolib.api.projectile.ProjectileType;
import net.minecraft.world.level.block.DirectionalBlock;


public class NormalSAPSuperHeavyShellBlock extends GeneralDualCannonProjectileBlock {

	private static final MapCodec<NormalSAPSuperHeavyShellBlock> CODEC = simpleCodec(NormalSAPSuperHeavyShellBlock::new);

	public NormalSAPSuperHeavyShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public ProjectileType<DualCannonState> getAssociatedProjectile() {
		return CBCMSDualCannonProjectiles.NORMAL_SAP_SUPER_HEAVY_SHELL;
	}

}
