package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_medium_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class ReductiveMediumRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReductiveMediumRangeTorpedoProjectile> {

	public ReductiveMediumRangeTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends ReductiveMediumRangeTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REDUCTIVE_MEDIUM_RANGE_TORPEDO.get();
	}

}
