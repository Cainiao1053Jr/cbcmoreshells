package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.short_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reinforced_reductive_medium_range_torpedo.ReinforcedReductiveMediumRangeTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class ShortRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ShortRangeTorpedoProjectile> {

	private static final MapCodec<ShortRangeTorpedoBlock> CODEC = simpleCodec(ShortRangeTorpedoBlock::new);

	public ShortRangeTorpedoBlock(Properties properties) {
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
	public EntityType<? extends ShortRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.SHORT_RANGE_TORPEDO.get();
	}

}
