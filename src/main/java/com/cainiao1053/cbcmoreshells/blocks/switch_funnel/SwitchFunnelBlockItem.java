package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.simibubi.create.content.logistics.funnel.FunnelItem;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

/**
 * Extends Create's FunnelItem to share its behaviour: places onto containers instead of opening them,
 * and places the belt funnel variant when used above a belt or depot.
 */
public class SwitchFunnelBlockItem extends FunnelItem {

	public SwitchFunnelBlockItem(SwitchFunnelBlock block, Properties properties) {
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
		CBCMSTooltip.appendSwitchFunnelInfo(stack, context, tooltip, flag);
	}

}
