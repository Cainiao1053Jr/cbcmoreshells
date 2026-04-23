package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.gambler_medium_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class GamblerMediumRangeTorpedoBlock extends GeneralCannonTorpedoBlock<GamblerMediumRangeTorpedoProjectile> {

	private static final MapCodec<GamblerMediumRangeTorpedoBlock> CODEC = simpleCodec(GamblerMediumRangeTorpedoBlock::new);

	public GamblerMediumRangeTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends GamblerMediumRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.GAMBLER_MEDIUM_RANGE_TORPEDO.get();
	}

}
