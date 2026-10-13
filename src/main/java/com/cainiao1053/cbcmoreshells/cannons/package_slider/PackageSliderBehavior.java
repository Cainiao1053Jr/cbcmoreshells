package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.cannons.CannonBehavior;

/**
 * Cannon behaviour holding at most one loaded package item.
 *
 * <p>The full package (with its contents) is only written to server NBT. Client packets only carry the
 * package item, which is all the renderer needs; {@link #stripForClient} does the same for tags the
 * contraption builds itself.
 */
public class PackageSliderBehavior extends CannonBehavior {

	public static final BehaviourType<PackageSliderBehavior> TYPE = new BehaviourType<>();

	private static final String PACKAGE_KEY = "Package";
	private static final String DISPLAY_KEY = "PackageDisplay";

	private ItemStack loaded = ItemStack.EMPTY;

	public PackageSliderBehavior(SmartBlockEntity be) {
		super(be);
	}

	public ItemStack getLoaded() {
		return this.loaded;
	}

	public boolean isLoaded() {
		return !this.loaded.isEmpty();
	}

	public void setLoaded(ItemStack stack) {
		this.loaded = stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1);
		this.blockEntity.setChanged();
	}

	public ItemStack removeLoaded() {
		ItemStack removed = this.loaded;
		this.setLoaded(ItemStack.EMPTY);
		return removed;
	}

	@Override
	public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.write(nbt, registries, clientPacket);
		if (this.loaded.isEmpty()) return;
		nbt.putString(DISPLAY_KEY, BuiltInRegistries.ITEM.getKey(this.loaded.getItem()).toString());
		if (!clientPacket)
			nbt.put(PACKAGE_KEY, this.loaded.save(registries));
	}

	@Override
	public void writeSafe(CompoundTag nbt, HolderLookup.Provider registries) {
		this.write(nbt, registries, true);
	}

	@Override
	public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(nbt, registries, clientPacket);
		if (nbt.contains(PACKAGE_KEY)) {
			this.loaded = ItemStack.parseOptional(registries, nbt.getCompound(PACKAGE_KEY));
		} else if (nbt.contains(DISPLAY_KEY)) {
			// Client side: only the package item is known, which is enough to render it.
			this.loaded = new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.parse(nbt.getString(DISPLAY_KEY))));
		} else {
			this.loaded = ItemStack.EMPTY;
		}
	}

	/** Removes the package contents from a full block entity tag before it is sent to clients. */
	public static void stripForClient(CompoundTag tag) {
		tag.remove(PACKAGE_KEY);
	}

	@Override
	public BehaviourType<?> getType() {
		return TYPE;
	}

}
