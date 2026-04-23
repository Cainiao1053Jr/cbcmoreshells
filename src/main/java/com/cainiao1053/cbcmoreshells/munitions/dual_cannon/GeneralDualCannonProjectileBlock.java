package com.cainiao1053.cbcmoreshells.munitions.dual_cannon;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import net.minecraft.world.level.block.entity.BlockEntityType;
import rbasamoyai.createbigcannons.index.CBCBlockEntities;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;

public abstract class GeneralDualCannonProjectileBlock<ENTITY_TYPE extends FuzedDualCannonProjectile>
	extends FuzedDualCannonProjectileBlock<FuzedBlockEntity, ENTITY_TYPE> {

	protected GeneralDualCannonProjectileBlock(Properties properties) {
		super(properties);
	}

	@Override
	public Class<FuzedBlockEntity> getBlockEntityClass() {
		return FuzedBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends FuzedBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.FUZED_BLOCK.get();
	}

}
