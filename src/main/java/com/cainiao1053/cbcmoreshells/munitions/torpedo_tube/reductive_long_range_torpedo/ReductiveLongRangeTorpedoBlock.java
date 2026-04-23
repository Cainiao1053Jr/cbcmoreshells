package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_long_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ReductiveLongRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReductiveLongRangeTorpedoProjectile> {

	private static final MapCodec<ReductiveLongRangeTorpedoBlock> CODEC = simpleCodec(ReductiveLongRangeTorpedoBlock::new);

	public ReductiveLongRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends ReductiveLongRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REDUCTIVE_LONG_RANGE_TORPEDO.get();
	}

}
