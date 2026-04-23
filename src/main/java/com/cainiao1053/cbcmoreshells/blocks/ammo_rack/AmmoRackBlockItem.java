package com.cainiao1053.cbcmoreshells.blocks.ammo_rack;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class AmmoRackBlockItem<T extends AmmoRackBlock> extends BlockItem {

	public AmmoRackBlockItem(T block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		boolean desc = Screen.hasShiftDown();
		if (!desc) {
			addHoldShift(desc, tooltip);
			return;
		}
		CBCMSTooltip.appendAmmoRackInfo(stack, context, tooltip, flag);
	}

}
