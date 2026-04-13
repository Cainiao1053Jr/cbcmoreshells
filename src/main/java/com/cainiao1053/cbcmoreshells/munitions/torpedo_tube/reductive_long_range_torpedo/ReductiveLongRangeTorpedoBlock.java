package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_long_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import net.minecraft.world.entity.EntityType;



public class ReductiveLongRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReductiveLongRangeTorpedoProjectile> {

	public ReductiveLongRangeTorpedoBlock(Properties properties) {
		super(properties);
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
