package com.cainiao1053.cbcmoreshells.cannons.flak_cannon.breech;

import com.jozufozu.flywheel.backend.instancing.InstancedRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.items.IItemHandler;
import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;

public class FlakcannonBreechBlockEntity extends AbstractFlakcannonBreechBlockEntity {

	private IItemHandler inventory;


	public FlakcannonBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public IItemHandler createItemHandler() {
		return this.inventory == null ? this.inventory = new FlakcannonBreechInterface(this) : this.inventory;
	}

	@Override
	public void requestModelDataUpdate() {
		super.requestModelDataUpdate();
		if (!this.remove) DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> InstancedRenderDispatcher.enqueueUpdate(this));
	}

	@Override
	protected void read(CompoundTag tag, boolean clientPacket) {
		super.read(tag, clientPacket);
		if (clientPacket && !this.isVirtual()) this.requestModelDataUpdate();
	}

}
