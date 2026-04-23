package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.early_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class EarlyTorpedoBlock extends GeneralCannonTorpedoBlock<EarlyTorpedoProjectile> {

	private static final MapCodec<EarlyTorpedoBlock> CODEC = simpleCodec(EarlyTorpedoBlock::new);

	public EarlyTorpedoBlock(Properties properties) {
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
	public EntityType<? extends EarlyTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.EARLY_TORPEDO.get();
	}

}
