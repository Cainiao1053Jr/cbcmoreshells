package com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.GeneralCannonTorpedoBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;




public class MediumRangeDeepwaterTorpedoBlock extends GeneralCannonTorpedoBlock<MediumRangeDeepwaterTorpedoProjectile> {

	private static final MapCodec<MediumRangeDeepwaterTorpedoBlock> CODEC = simpleCodec(MediumRangeDeepwaterTorpedoBlock::new);

	public MediumRangeDeepwaterTorpedoBlock(Properties properties) {
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
	public EntityType<? extends MediumRangeDeepwaterTorpedoProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.MEDIUM_RANGE_DEEPWATER_TORPEDO.get();
	}

}
