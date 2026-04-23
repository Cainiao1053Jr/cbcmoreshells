package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.highspeed_long_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class HighspeedLongRangeTorpedoBlock extends GeneralCannonTorpedoBlock<HighspeedLongRangeTorpedoProjectile> {

	private static final MapCodec<HighspeedLongRangeTorpedoBlock> CODEC = simpleCodec(HighspeedLongRangeTorpedoBlock::new);

	public HighspeedLongRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends HighspeedLongRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.HIGHSPEED_LONG_RANGE_TORPEDO.get();
	}

}
