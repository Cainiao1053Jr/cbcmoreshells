package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_highspeed_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class ReductiveHighspeedTorpedoBlock extends GeneralCannonTorpedoBlock<ReductiveHighspeedTorpedoProjectile> {

	public ReductiveHighspeedTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends ReductiveHighspeedTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.REDUCTIVE_HIGHSPEED_TORPEDO.get();
	}

}
