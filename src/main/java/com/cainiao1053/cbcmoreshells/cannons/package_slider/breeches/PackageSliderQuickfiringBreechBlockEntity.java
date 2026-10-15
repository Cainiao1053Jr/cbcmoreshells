package com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches;

import com.cainiao1053.cbcmoreshells.cannons.IReloadCooldown;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.IPackageSliderBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBehavior;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class PackageSliderQuickfiringBreechBlockEntity extends SmartBlockEntity implements IPackageSliderBlockEntity, IReloadCooldown {

	private PackageSliderBehavior cannonBehavior;
	private int reloadCooldown;
	private int reloadDuration;
	private float sliderStroke;

	public PackageSliderQuickfiringBreechBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
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

	public void tickCooldown() {
		if (this.reloadCooldown > 0) --this.reloadCooldown;
	}

	public void setReloadCooldown(int ticks) {
		this.reloadCooldown = Math.max(0, ticks);
	}

	public void startReload(int ticks, float sliderStroke) {
		this.setReloadCooldown(ticks);
		this.reloadDuration = this.reloadCooldown;
		this.sliderStroke = Math.max(0, sliderStroke);
	}

	public float getSliderOffset(float partialTicks) {
		if (this.reloadDuration <= 0 || this.reloadCooldown <= 0) return 0;
		float remaining = Math.max(0, this.reloadCooldown - partialTicks);
		return this.sliderStroke * remaining / this.reloadDuration;
	}

	public boolean isReloading() {
		return this.reloadCooldown > 0;
	}

	@Override
	public int getBreechCooldown() {
		return this.reloadCooldown;
	}

	@Override
	protected void write(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(tag, registries, clientPacket);
		tag.putInt("ReloadCooldown", this.reloadCooldown);
		tag.putInt("ReloadDuration", this.reloadDuration);
		tag.putFloat("SliderStroke", this.sliderStroke);
	}

	@Override
	protected void read(CompoundTag tag, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(tag, registries, clientPacket);
		this.reloadCooldown = Math.max(0, tag.getInt("ReloadCooldown"));
		this.reloadDuration = Math.max(0, tag.getInt("ReloadDuration"));
		this.sliderStroke = Math.max(0, tag.getFloat("SliderStroke"));
	}

}
