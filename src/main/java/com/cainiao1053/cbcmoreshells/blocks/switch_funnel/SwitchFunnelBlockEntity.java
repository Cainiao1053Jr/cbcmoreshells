package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.cainiao1053.cbcmoreshells.mixin.FilteringBehaviourAccessor;
import com.simibubi.create.content.logistics.funnel.FunnelBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class SwitchFunnelBlockEntity extends FunnelBlockEntity {

	public SwitchFunnelBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	@Override
	public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
		super.addBehaviours(behaviours);
		// Called from the SmartBlockEntity constructor: the behaviour map is not filled yet, so scan the list
		for (BlockEntityBehaviour behaviour : behaviours) {
			if (behaviour.getType() == FilteringBehaviour.TYPE) {
				FilteringBehaviour main = (FilteringBehaviour) behaviour;
				// FunnelBlockEntity only enables filtering for Create's brass funnels
				main.onlyActiveWhen(() -> true);
				((FilteringBehaviourAccessor) main).cbcms$setSlotPositioning(new SwitchFunnelSlotPositioning(0));
				break;
			}
		}
		// Layout: [reserve 1 | main | reserve 2]
		behaviours.add(new ReserveFilteringBehaviour(this, new SwitchFunnelSlotPositioning(-1), 1));
		behaviours.add(new ReserveFilteringBehaviour(this, new SwitchFunnelSlotPositioning(1), 2));
	}

	public void cycleFilters() {
		FilteringBehaviour main = getBehaviour(FilteringBehaviour.TYPE);
		FilteringBehaviour reserve1 = getBehaviour(ReserveFilteringBehaviour.TYPE_1);
		FilteringBehaviour reserve2 = getBehaviour(ReserveFilteringBehaviour.TYPE_2);
		if (main == null || reserve1 == null || reserve2 == null)
			return;

		FilteringBehaviour[] slots = {main, reserve1, reserve2};
		ItemStack[] filters = new ItemStack[slots.length];
		for (int i = 0; i < slots.length; i++)
			filters[i] = slots[i].getFilter().copy();

		// Smallest rotation that puts a non-empty filter into the main slot
		int shift = 0;
		for (int k = 1; k < slots.length; k++) {
			if (!filters[k].isEmpty()) {
				shift = k;
				break;
			}
		}
		// Reserves all empty: nothing to switch to, keep the current main filter
		if (shift == 0)
			return;

		for (int i = 0; i < slots.length; i++)
			slots[i].setFilter(filters[(i + shift) % slots.length]);

		if (level != null)
			level.playSound(null, worldPosition, SoundEvents.ITEM_FRAME_ROTATE_ITEM, SoundSource.BLOCKS, 0.5f, 1.2f);
	}

	@Override
	public void clearContent() {
		super.clearContent();
		FilteringBehaviour reserve1 = getBehaviour(ReserveFilteringBehaviour.TYPE_1);
		FilteringBehaviour reserve2 = getBehaviour(ReserveFilteringBehaviour.TYPE_2);
		if (reserve1 != null)
			reserve1.setFilter(ItemStack.EMPTY);
		if (reserve2 != null)
			reserve2.setFilter(ItemStack.EMPTY);
	}

}
