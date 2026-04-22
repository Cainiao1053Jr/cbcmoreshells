package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

public abstract class AbstractRackedRocketBlockItem<ENTITY extends AbstractRackedProjectile> extends FuzedRackedProjectileBlockItem {

	public AbstractRackedRocketBlockItem(Block block, Properties properties) {
		super(block, properties);
	}



	public abstract EntityType<? extends ENTITY> getAssociatedEntityType();

}
