//package com.cainiao1053.cbcmoreshells.blocks.dish_plate;
//
//import java.util.*;
//
//import javax.annotation.Nullable;
//import javax.annotation.ParametersAreNonnullByDefault;
//
//import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
//import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
//import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
//import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
//import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
//import com.simibubi.create.foundation.item.SmartInventory;
//import net.minecraft.MethodsReturnNonnullByDefault;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.world.phys.AABB;
//
//import net.minecraftforge.common.capabilities.Capability;
//import net.minecraftforge.common.capabilities.ForgeCapabilities;
//import net.minecraftforge.common.util.LazyOptional;
//import net.minecraftforge.items.IItemHandler;
//import org.slf4j.Logger;
//import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;
//import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;
//
//@ParametersAreNonnullByDefault
//@MethodsReturnNonnullByDefault
///*
//
// */
//public class DishPlateBlockEntity extends SmartBlockEntity { // , IAirCurrentSource {
//
//	private FilteringBehaviour filter;
//	//private int index = 0;
//	public final SmartInventory inventory = new SmartInventory(getMaxSlots(), this){
//		@Override
//		public boolean isItemValid(int slot, ItemStack stack) {
//			return (filter != null && filter.test(stack) && filter.getFilter()!= ItemStack.EMPTY);
//		}
//	}
//	.withMaxStackSize(1);
//
//	ItemStack item = ItemStack.EMPTY;
//	LazyOptional<IItemHandler> lazyHandler;
//	DishPlateItemHandler itemHandler;
//	VersionedInventoryTrackerBehaviour invVersionTracker;
//
//	Logger LOGGER = Cbcmoreshells.LOGGER;
//
//	public DishPlateBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//		super(type, pos, state);
//		itemHandler = new DishPlateItemHandler(this);
//		lazyHandler = LazyOptional.of(() -> inventory);
//	}
//	@Override
//	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
//		behaviours.add(filter =
//				new FilteringBehaviour(this, new DishPlateFilterSlotPositioning())
//						.withCallback($ -> invVersionTracker.reset()));
//		behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
//	}
//
//	@Override
//	public void initialize() {
//		super.initialize();
//		onAdded();
//	}
//
//	@Override
//	protected AABB createRenderBoundingBox() {
//		return new AABB(worldPosition).expandTowards(0, -3, 0);
//	}
//
//
//	@Override
//	public void tick() {
//		super.tick();
//	}
//
//	protected boolean canAcceptItem(ItemStack stack) {
//		return item.isEmpty();
//	}
//
//	public void setItem(ItemStack stack, int slot) {
//		inventory.setStackInSlot(slot, stack);
//		invVersionTracker.reset();
//		if (!level.isClientSide) {
//			notifyUpdate();
//		}
//	}
//
//	@Override
//	public void invalidateCaps() {
//		super.invalidateCaps();
//		if(lazyHandler != null) {
//			lazyHandler.invalidate();
//		}
//	}
//
//	@Override
//	public void reviveCaps() {
//		super.reviveCaps();
//		lazyHandler = LazyOptional.of(() -> inventory);
//	}
//
//	@Override
//	public void write(CompoundTag compound, boolean clientPacket) {
//		compound.put("Inventory", inventory.serializeNBT());
//		//compound.putInt("Index", index);
//		super.write(compound, clientPacket);
//	}
//
//	@Override
//	protected void read(CompoundTag compound, boolean clientPacket) {
//		inventory.deserializeNBT(compound.getCompound("Inventory"));
//		//index = compound.getInt("Index");
//		super.read(compound, clientPacket);
//	}
//
//	@Override
//	public void destroy() {
//		super.destroy();
//	}
//
//	public void onAdded() {
//		refreshBlockState();
//	}
//
//	@Override
//	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
//		if (cap == ForgeCapabilities.ITEM_HANDLER)
//			return lazyHandler.cast();
//		return super.getCapability(cap, side);
//	}
//
//	public ItemStack insertStack(ItemStack stack, int slot, boolean simulate) {
//		stack = inventory.insertItem(slot, stack, simulate);
//		if (stack.getCount() < stack.getMaxStackSize())
//			setChangedAndSync();
//		if(!simulate){this.unpower();}
//		return stack;
//	}
//
//	private void setChangedAndSync() {
//		this.setChanged();
//		if (level instanceof ServerLevel sl)
//			sl.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
//	}
//
//	public ItemStack extractStack(int slot) {
//		ItemStack stack = ItemStack.EMPTY;
//		stack = inventory.extractItem(slot, 1, false);
//		return stack;
//	}
//
//	public void unpower(){
//		level.setBlock(this.getBlockPos(), this.getBlockState().setValue(BlockStateProperties.POWERED, false), 3);
//	}
//
//
//	public ItemStack getItem() {
//		return item;
//	}
//
//	public SmartInventory getInventory() {
//		return inventory;
//	}
//
//	public FilteringBehaviour getFilter() {
//		return filter;
//	}
//
//	public int getMaxSlots(){
//		return 1;
//	}
//
//	protected int getShellCounts() {
//		int count = 0;
//		for(int i = 0; i < inventory.getSlots(); i++) {
//			ItemStack stack = inventory.getStackInSlot(i);
//			if(!stack.isEmpty() &&( (stack.getItem() instanceof ProjectileBlockItem) || (stack.getItem() instanceof BigCartridgeBlockItem) )){
//				count += stack.getCount();
//			}
//		}
//		return count;
//	}
//}
