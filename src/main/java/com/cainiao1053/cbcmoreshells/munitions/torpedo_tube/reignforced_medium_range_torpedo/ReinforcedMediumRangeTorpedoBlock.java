package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_medium_range_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class ReinforcedMediumRangeTorpedoBlock extends GeneralCannonTorpedoBlock<ReinforcedMediumRangeTorpedoProjectile> {

	public ReinforcedMediumRangeTorpedoBlock(Properties properties) {
		super(properties);
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
