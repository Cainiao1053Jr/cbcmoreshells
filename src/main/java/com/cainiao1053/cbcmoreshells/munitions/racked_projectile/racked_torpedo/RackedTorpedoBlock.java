package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.racked_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.GeneralRackedProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;


public class RackedTorpedoBlock extends GeneralRackedProjectileBlock<RackedTorpedoProjectile> {

	private static final MapCodec<RackedTorpedoBlock> CODEC = simpleCodec(RackedTorpedoBlock::new);

	public RackedTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.RACKED_TORPEDO.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public int getLifetimeFromBlock() {
		return CBCMSMunitionPropertiesHandlers.RACKED_TORPEDO.getPropertiesOf(this.getAssociatedEntityType()).lifetime();
	}

	@Override
	public EntityType<? extends RackedTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.RACKED_TORPEDO.get();
	}

}
