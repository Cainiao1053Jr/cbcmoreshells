package com.cainiao1053.cbcmoreshells.blocks.command_displayer;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class CommandDisplayerBlockItem<T extends CommandDisplayerBlock> extends BlockItem {

	public CommandDisplayerBlockItem(T block, Properties properties) {
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
		CBCMSTooltip.genericItemTooltipInfo(stack,context,tooltip,flag);
	}


}
