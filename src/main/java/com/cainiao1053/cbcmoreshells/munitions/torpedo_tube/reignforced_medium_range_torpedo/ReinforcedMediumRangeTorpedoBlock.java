package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_medium_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ReinforcedMediumRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReinforcedMediumRangeTorpedoProjectile> {

	private static final MapCodec<ReinforcedMediumRangeTorpedoBlock> CODEC = simpleCodec(ReinforcedMediumRangeTorpedoBlock::new);

	public ReinforcedMediumRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends ReinforcedMediumRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REINFORCED_MEDIUM_RANGE_TORPEDO.get();
	}

}
