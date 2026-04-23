package com.cainiao1053.cbcmoreshells.blocks.ammo_rack;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class AmmoRackInteractionPoint extends ArmInteractionPoint {

	public AmmoRackInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
		this.mode = Mode.TAKE;
	}

	Logger LOGGER = Cbcmoreshells.LOGGER;

	@Override
	public ItemStack extract(ArmBlockEntity armBlockEntity, int slot, int amount, boolean simulate) {
		AmmoRackBlockEntity be = (AmmoRackBlockEntity) level.getBlockEntity(pos);
		IItemHandler handler = this.getHandler(armBlockEntity);
		if (handler == null) {
			return ItemStack.EMPTY;
		}
		ItemStack stack = handler.extractItem(slot, 1, simulate);
		if (stack.getItem() instanceof BigCartridgeBlockItem) {
			return stack;
		}
		if (!(stack.getItem() instanceof ProjectileBlockItem)) {
			return ItemStack.EMPTY;
		}
		if (be != null && !be.getInventory().isItemValid(slot, stack)) {
			return ItemStack.EMPTY;
		}
		return stack;
	}

	@Override
	public void cycleMode() {
	}

	@Override
	public ItemStack insert(ArmBlockEntity armBlockEntity, ItemStack stack, boolean simulate) {
		return ItemStack.EMPTY;
	}

}
