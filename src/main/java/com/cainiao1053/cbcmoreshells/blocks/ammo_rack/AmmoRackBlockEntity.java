package com.cainiao1053.cbcmoreshells.blocks.ammo_rack;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.inventory.VersionedInventoryTrackerBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.items.IItemHandler;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
/*

 */
public class AmmoRackBlockEntity extends SmartBlockEntity { // , IAirCurrentSource {

	private FilteringBehaviour filter;
	private int index = 0;
	public final SmartInventory inventory = new SmartInventory(getMaxSlots(), this){
		@Override
		public boolean isItemValid(int slot, ItemStack stack) {
			return filter == null || filter.test(stack);
		}
	}
	.withMaxStackSize(64);

	ItemStack item = ItemStack.EMPTY;
	LazyOptional<IItemHandler> lazyHandler;
	AmmoRackItemHandler itemHandler;
	VersionedInventoryTrackerBehaviour invVersionTracker;

	public AmmoRackBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
		itemHandler = new AmmoRackItemHandler(this);
		lazyHandler = LazyOptional.of(() -> inventory);
	}
	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		behaviours.add(filter =
				new FilteringBehaviour(this, new AmmoRackFilterSlotPositioning())
						.withCallback($ -> invVersionTracker.reset()));
		behaviours.add(invVersionTracker = new VersionedInventoryTrackerBehaviour(this));
	}

	@Override
	public void initialize() {
		super.initialize();
		onAdded();
	}

	@Override
	protected AABB createRenderBoundingBox() {
		return new AABB(worldPosition).expandTowards(0, -3, 0);
	}


	@Override
	public void tick() {
		super.tick();
	}

	protected boolean canAcceptItem(ItemStack stack) {
		return item.isEmpty();
	}

	public void setItem(ItemStack stack, int slot) {
		inventory.setStackInSlot(slot, stack);
		invVersionTracker.reset();
		if (!level.isClientSide) {
			notifyUpdate();
		}
	}

	@Override
	public void invalidateCaps() {
		super.invalidateCaps();
		if(lazyHandler != null) {
			lazyHandler.invalidate();
		}
	}

	@Override
	public void reviveCaps() {
		super.reviveCaps();
		lazyHandler = LazyOptional.of(() -> inventory);
	}

	@Override
	public void write(CompoundTag compound, boolean clientPacket) {
		compound.put("Inventory", inventory.serializeNBT());
		compound.putInt("Index", index);
		super.write(compound, clientPacket);
	}

	@Override
	protected void read(CompoundTag compound, boolean clientPacket) {
		inventory.deserializeNBT(compound.getCompound("Inventory"));
		index = compound.getInt("Index");
		super.read(compound, clientPacket);
	}

	@Override
	public void destroy() {
		super.destroy();
	}

	public void onAdded() {
		refreshBlockState();
	}

	@Override
	public <T> LazyOptional<T> getCapability(Capability<T> cap, @Nullable Direction side) {
		if (cap == ForgeCapabilities.ITEM_HANDLER)
			return lazyHandler.cast();
		return super.getCapability(cap, side);
	}

	public ItemStack insertStack(ItemStack stack, int slot) {
//		for (int i = 0; i < inventory.getSlots(); i++) {
//			stack = inventory.insertItem(i, stack, false);
//			if (stack.isEmpty())
//				break;
//		}
		stack = inventory.insertItem(slot, stack, false);
		if (stack.getCount() < stack.getMaxStackSize())
			setChangedAndSync();
		return stack;
	}

	private void setChangedAndSync() {
		this.setChanged();
		if (level instanceof ServerLevel sl)
			sl.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
	}

	public ItemStack extractStack(int slot) {
		ItemStack stack = ItemStack.EMPTY;
//		for (int i = inventory.getSlots()-1; i > -1; i--) {
//			stack = inventory.extractItem(i, 64, false);
//			if(!stack.isEmpty()){
//				break;
//			}
//		}
		stack = inventory.extractItem(slot, 64, false);
		return stack;
	}

	public void switchFilter() {
		SmartInventory inv = inventory;
		if(!inv.isEmpty()){
			Map<ItemStack, Integer> map = mergeStacksIgnoreNBT(this.inventory);
			int size = map.size();
			//LOGGER.info("size: " + size);
			if(size == 0){
				return;
			}
			if(index < size-1){
				index++;
			}else if(index >= size-1){
				index = 0;
			}
			List<Map.Entry<ItemStack,Integer>> list =
					new ArrayList<>(map.entrySet());
			Map.Entry<ItemStack,Integer> e = list.get(index);
			ItemStack stack = e.getKey();
//			LOGGER.info("map: " + map);
//			LOGGER.info("list: " + list);
//			LOGGER.info("stack: " + stack);
			this.filter.setFilter(stack);
		}

	}

	public ItemStack getItem() {
		return item;
	}

	public SmartInventory getInventory() {
		return inventory;
	}

	public FilteringBehaviour getFilter() {
		return filter;
	}

	public int getMaxSlots(){
		return 6;
	}

	protected int getShellCounts() {
		int count = 0;
		for(int i = 0; i < inventory.getSlots(); i++) {
			ItemStack stack = inventory.getStackInSlot(i);
			if(!stack.isEmpty() &&( (stack.getItem() instanceof ProjectileBlockItem) || (stack.getItem() instanceof BigCartridgeBlockItem) )){
				count += stack.getCount();
			}
		}
		return count;
	}

	public static Map<ItemStack, Integer> mergeStacks(SmartInventory inv) {
		Map<ItemStack, Integer> merged = new LinkedHashMap<>();

		for (int slot = 0; slot < inv.getSlots(); slot++) {
			ItemStack stack = inv.getStackInSlot(slot);
			if (stack.isEmpty())
				continue;

			Optional<Map.Entry<ItemStack, Integer>> same =
					merged.entrySet()
							.stream()
							.filter(e -> ItemStack.isSameItemSameTags(stack, e.getKey()))
							.findFirst();

			if (same.isPresent()) {
				same.get().setValue(same.get().getValue() + stack.getCount());
			} else {
				merged.put(stack.copyWithCount(1), 1);
			}
		}
		return merged;
	}

	public static Map<ItemStack, Integer> mergeStacksIgnoreNBT(SmartInventory inv) {
		Map<ItemStack, Integer> merged = new LinkedHashMap<>();
		for (int i = 0; i < inv.getSlots(); i++) {
			ItemStack s = inv.getStackInSlot(i);
			if (s.isEmpty())
				continue;
			if(!(s.getItem() instanceof ProjectileBlockItem)){
				continue;
			}

			Optional<Map.Entry<ItemStack, Integer>> same =
					merged.entrySet()
							.stream()
							.filter(e -> ItemStack.isSameItem(s, e.getKey()))
							.findFirst();

			if (same.isPresent())
				same.get().setValue(same.get().getValue() + s.getCount());
			else
				merged.put(s.copyWithCount(1), s.getCount());
		}
		return merged;
	}
}
