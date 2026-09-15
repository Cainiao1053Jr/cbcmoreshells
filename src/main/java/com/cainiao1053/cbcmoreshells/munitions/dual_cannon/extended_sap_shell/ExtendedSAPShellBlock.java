package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_sap_shell;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.GeneralDualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonProjectiles;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonState;
import com.mojang.serialization.MapCodec;
import com.verr1.shaolib.api.projectile.ProjectileType;
import net.minecraft.world.level.block.DirectionalBlock;


public class ExtendedSAPShellBlock extends GeneralDualCannonProjectileBlock {

	private static final MapCodec<ExtendedSAPShellBlock> CODEC = simpleCodec(ExtendedSAPShellBlock::new);

	public ExtendedSAPShellBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public ProjectileType<DualCannonState> getAssociatedProjectile() {
		return CBCMSDualCannonProjectiles.EXTENDED_SAP_SHELL;
	}

}
