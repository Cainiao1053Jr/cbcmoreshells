package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.light_high_speed_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class LightHighSpeedTorpedoBlock extends GeneralCannonTorpedoBlock<LightHighSpeedTorpedoProjectile> {

	private static final MapCodec<LightHighSpeedTorpedoBlock> CODEC = simpleCodec(LightHighSpeedTorpedoBlock::new);

	public LightHighSpeedTorpedoBlock(Properties properties) {
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
	public EntityType<? extends LightHighSpeedTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.LIGHT_HIGH_SPEED_TORPEDO.get();
	}

}
