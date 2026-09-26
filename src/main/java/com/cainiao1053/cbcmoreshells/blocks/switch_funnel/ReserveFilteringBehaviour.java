package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BehaviourType;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;

/**
 * Storage-only filter slot of the switch funnel. It never filters anything itself: funnel logic only
 * looks up {@link FilteringBehaviour#TYPE}, which this behaviour does not use.
 */
public class ReserveFilteringBehaviour extends FilteringBehaviour {

	public static final BehaviourType<ReserveFilteringBehaviour> TYPE_1 = new BehaviourType<>("switch_funnel_reserve_1");
	public static final BehaviourType<ReserveFilteringBehaviour> TYPE_2 = new BehaviourType<>("switch_funnel_reserve_2");

	private final int index;

	public ReserveFilteringBehaviour(SmartBlockEntity be, ValueBoxTransform slot, int index) {
		super(be, slot);
		this.index = index;
		setLabel(Component.translatable("cbcmoreshells.switch_funnel.reserve_" + index));
	}

	private String nbtKey() {
		return "SwitchReserve" + index;
	}

	@Override
	public BehaviourType<?> getType() {
		return index == 1 ? TYPE_1 : TYPE_2;
	}

	@Override
	public int netId() {
		return 1 + index;
	}

	@Override
	public String getClipboardKey() {
		return nbtKey();
	}

	@Override
	public void write(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		CompoundTag sub = new CompoundTag();
		super.write(sub, registries, clientPacket);
		nbt.put(nbtKey(), sub);
	}

	@Override
	public void read(CompoundTag nbt, HolderLookup.Provider registries, boolean clientPacket) {
		super.read(nbt.getCompound(nbtKey()), registries, clientPacket);
	}

}
