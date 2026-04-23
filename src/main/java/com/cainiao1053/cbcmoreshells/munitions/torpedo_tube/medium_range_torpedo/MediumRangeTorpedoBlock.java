package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.medium_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class MediumRangeTorpedoBlock extends GeneralCannonTorpedoBlock<MediumRangeTorpedoProjectile> {

	private static final MapCodec<MediumRangeTorpedoBlock> CODEC = simpleCodec(MediumRangeTorpedoBlock::new);

	public MediumRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends MediumRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.MEDIUM_RANGE_TORPEDO.get();
	}

}
