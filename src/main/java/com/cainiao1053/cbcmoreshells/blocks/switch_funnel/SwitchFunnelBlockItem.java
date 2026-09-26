package com.cainiao1053.cbcmoreshells.blocks.switch_funnel;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

@EventBusSubscriber(modid = Cbcmoreshells.MODID)
public class SwitchFunnelBlockItem extends BlockItem {

	public SwitchFunnelBlockItem(SwitchFunnelBlock block, Properties properties) {
		super(block, properties);
	}

	/** Same as Create's FunnelItem: place onto containers instead of opening their screen. */
	@SubscribeEvent
	public static void switchFunnelAlwaysPlacesWhenUsed(PlayerInteractEvent.RightClickBlock event) {
		if (event.getItemStack().getItem() instanceof SwitchFunnelBlockItem)
			event.setUseBlock(TriState.FALSE);
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
