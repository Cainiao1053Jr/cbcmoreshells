package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_highspeed_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;



public class ReductiveHighspeedTorpedoBlock extends GeneralCannonTorpedoBlock<ReductiveHighspeedTorpedoProjectile> {

	private static final MapCodec<ReductiveHighspeedTorpedoBlock> CODEC = simpleCodec(ReductiveHighspeedTorpedoBlock::new);

	public ReductiveHighspeedTorpedoBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
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
