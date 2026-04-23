package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_short_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ReinforcedShortRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReinforcedShortRangeTorpedoProjectile> {

	private static final MapCodec<ReinforcedShortRangeTorpedoBlock> CODEC = simpleCodec(ReinforcedShortRangeTorpedoBlock::new);

	public ReinforcedShortRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends ReinforcedShortRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REINFORCED_SHORT_RANGE_TORPEDO.get();
	}

}
