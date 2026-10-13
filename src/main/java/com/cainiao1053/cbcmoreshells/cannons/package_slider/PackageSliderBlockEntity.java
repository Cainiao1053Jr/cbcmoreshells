package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PackageSliderBlockEntity extends SmartBlockEntity implements IPackageSliderBlockEntity {

	private PackageSliderBehavior cannonBehavior;

	public PackageSliderBlockEntity(BlockEntityType<? extends PackageSliderBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(this.cannonBehavior = new PackageSliderBehavior(this));
	}

	@Override
	public PackageSliderBehavior cannonBehavior() {
		return this.cannonBehavior;
	}

}
