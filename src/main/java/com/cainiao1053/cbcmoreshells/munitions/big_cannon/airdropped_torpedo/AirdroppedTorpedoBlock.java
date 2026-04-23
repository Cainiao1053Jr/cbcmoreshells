package com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_shrapnel_torpedo.AirdroppedShrapnelTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class AirdroppedTorpedoBlock extends GeneralCannonTorpedoBlock<AirdroppedTorpedoProjectile> {

	private static final MapCodec<AirdroppedTorpedoBlock> CODEC = simpleCodec(AirdroppedTorpedoBlock::new);

	public AirdroppedTorpedoBlock(Properties properties) {
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
	public EntityType<? extends AirdroppedTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.AIRDROPPED_TORPEDO.get();
	}

}
