package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_antiair_he_shell;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonProjectiles;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonState;
import com.mojang.serialization.MapCodec;
import com.verr1.shaolib.api.projectile.ProjectileType;
import net.minecraft.world.level.block.DirectionalBlock;


public class ExtendedAntiairHEShellBlock extends GeneralDualCannonProjectileBlock {

	private static final MapCodec<ExtendedAntiairHEShellBlock> CODEC = simpleCodec(ExtendedAntiairHEShellBlock::new);

	public ExtendedAntiairHEShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public ProjectileType<DualCannonState> getAssociatedProjectile() {
		return CBCMSDualCannonProjectiles.EXTENDED_ANTIAIR_HE_SHELL;
	}

}
