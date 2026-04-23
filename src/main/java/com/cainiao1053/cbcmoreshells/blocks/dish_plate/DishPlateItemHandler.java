//package com.cainiao1053.cbcmoreshells.blocks.dish_plate;
//
//import com.simibubi.create.foundation.item.ItemHelper;
//
//import net.minecraft.world.item.ItemStack;
//import net.minecraftforge.items.IItemHandler;
//
//public class DishPlateItemHandler implements IItemHandler {
//
//	private DishPlateBlockEntity blockEntity;
//
//	public DishPlateItemHandler(DishPlateBlockEntity be) {
//		this.blockEntity = be;
//	}
//
//	@Override
//	public int getSlots() {
//		return 1;
//	}
//
//	@Override
//	public ItemStack getStackInSlot(int slot) {
//		return blockEntity.inventory.getStackInSlot(slot);
//	}
//
//	@Override
//	public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
//		if (!blockEntity.canAcceptItem(stack))
//			return stack;
//		ItemStack remainder = ItemHelper.limitCountToMaxStackSize(stack, simulate);
////		if (!simulate)
////			blockEntity.setItem(stack);
//		return remainder;
//	}
//
//	@Override
//	public ItemStack extractItem(int slot, int amount, boolean simulate) {
////		ItemStack remainder = blockEntity.item.copy();
////		ItemStack split = remainder.split(amount);
//		ItemStack item = blockEntity.inventory.getStackInSlot(0);
//		if (!simulate)
//			blockEntity.setItem(item,slot);
//		return item;
//	}
//
//	@Override
//	public int getSlotLimit(int slot) {
//		return Math.min(64, getStackInSlot(slot).getMaxStackSize());
//	}
//
//	@Override
//	public boolean isItemValid(int slot, ItemStack stack) {
//		return true;
//	}
//
//}
