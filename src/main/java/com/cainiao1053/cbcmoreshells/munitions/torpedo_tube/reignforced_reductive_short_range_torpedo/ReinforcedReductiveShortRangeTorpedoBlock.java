package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_reductive_short_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ReinforcedReductiveShortRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReinforcedReductiveShortRangeTorpedoProjectile> {

	private static final MapCodec<ReinforcedReductiveShortRangeTorpedoBlock> CODEC = simpleCodec(ReinforcedReductiveShortRangeTorpedoBlock::new);

	public ReinforcedReductiveShortRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends ReinforcedReductiveShortRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REINFORCED_REDUCTIVE_SHORT_RANGE_TORPEDO.get();
	}

}
