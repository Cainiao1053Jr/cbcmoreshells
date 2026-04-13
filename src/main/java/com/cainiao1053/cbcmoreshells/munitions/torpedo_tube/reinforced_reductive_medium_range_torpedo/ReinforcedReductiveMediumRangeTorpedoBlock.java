package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reinforced_reductive_medium_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import net.minecraft.world.entity.EntityType;



public class ReinforcedReductiveMediumRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReinforcedReductiveMediumRangeTorpedoProjectile> {

	public ReinforcedReductiveMediumRangeTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends ReinforcedReductiveMediumRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REINFORCED_REDUCTIVE_MEDIUM_RANGE_TORPEDO.get();
	}

}
