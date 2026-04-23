package com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_torpedo_typeb;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;



public class MediumRangeTorpedoTypeBBlock extends GeneralCannonTorpedoBlock<MediumRangeTorpedoTypeBProjectile> {

	private static final MapCodec<MediumRangeTorpedoTypeBBlock> CODEC = simpleCodec(MediumRangeTorpedoTypeBBlock::new);

	public MediumRangeTorpedoTypeBBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public EntityType<? extends MediumRangeTorpedoTypeBProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.MEDIUM_RANGE_TORPEDO_TYPEB.get();
	}

}
