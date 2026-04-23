package com.cainiao1053.cbcmoreshells.blocks.ammo_rack;

import com.simibubi.create.foundation.item.ItemHelper;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;

public class AmmoRackItemHandler implements IItemHandler {

	private AmmoRackBlockEntity blockEntity;

	public AmmoRackItemHandler(AmmoRackBlockEntity be) {
		this.blockEntity = be;
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Override
	public ItemStack getStackInSlot(int slot) {
		return blockEntity.inventory.getStackInSlot(slot);
	}

	@Override
	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
		if (!blockEntity.canAcceptItem(stack))
			return stack;
		ItemStack remainder = ItemHelper.limitCountToMaxStackSize(stack, simulate);
		return remainder;
	}

	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		ItemStack item = blockEntity.inventory.getStackInSlot(0);
		if (!simulate)
			blockEntity.setItem(item, slot);
		return item;
	}

	@Override
	public int getSlotLimit(int slot) {
		return Math.min(64, getStackInSlot(slot).getMaxStackSize());
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		return true;
	}

}
