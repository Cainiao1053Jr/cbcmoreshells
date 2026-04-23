package com.cainiao1053.cbcmoreshells.munitions.big_cannon.he_cannon_rocket;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;


public class HECannonRocketBlock extends ShellessShellBlock<HECannonRocketProjectile> {

	private static final MapCodec<HECannonRocketBlock> CODEC = simpleCodec(HECannonRocketBlock::new);

	public HECannonRocketBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends HECannonRocketProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.HE_CANNON_ROCKET.get();
	}

}
