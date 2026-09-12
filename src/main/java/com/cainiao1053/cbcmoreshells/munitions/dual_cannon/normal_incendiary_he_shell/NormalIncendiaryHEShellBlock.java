package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonProjectiles;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonState;
import com.mojang.serialization.MapCodec;
import com.verr1.shaolib.api.projectile.ProjectileType;
import net.minecraft.world.level.block.DirectionalBlock;


public class NormalIncendiaryHEShellBlock extends GeneralDualCannonProjectileBlock {

	private static final MapCodec<NormalIncendiaryHEShellBlock> CODEC = simpleCodec(NormalIncendiaryHEShellBlock::new);

	public NormalIncendiaryHEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public ProjectileType<DualCannonState> getAssociatedProjectile() {
		return CBCMSDualCannonProjectiles.NORMAL_INCENDIARY_HE_SHELL;
	}

}
