package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.ultraspeed_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessShellBlock;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class UltraspeedTorpedoBlock extends GeneralCannonTorpedoBlock<UltraspeedTorpedoProjectile> {

	public UltraspeedTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends UltraspeedTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.ULTRASPEED_TORPEDO.get();
	}

}
