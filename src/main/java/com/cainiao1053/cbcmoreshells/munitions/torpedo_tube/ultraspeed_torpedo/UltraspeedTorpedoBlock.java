package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.ultraspeed_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.slow_long_range_torpedo.SlowLongRangeTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class UltraspeedTorpedoBlock extends GeneralCannonTorpedoBlock<UltraspeedTorpedoProjectile> {

	private static final MapCodec<UltraspeedTorpedoBlock> CODEC = simpleCodec(UltraspeedTorpedoBlock::new);

	public UltraspeedTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends UltraspeedTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.ULTRASPEED_TORPEDO.get();
	}

}
