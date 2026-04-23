//package com.cainiao1053.cbcmoreshells.blocks.landing_indicator;
//
//import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
//import net.minecraft.client.gui.screens.Screen;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.item.BlockItem;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.TooltipFlag;
//import net.minecraft.world.level.Level;
//
//import javax.annotation.Nullable;
//import java.util.List;
//
//import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;
//
//public class LandingIndicatorBlockItem<T extends LandingIndicatorBlock> extends BlockItem {
//
//	public LandingIndicatorBlockItem(T block, Properties properties) {
//		super(block, properties);
//	}
//
//	@Override
//	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
//		super.appendHoverText(stack, level, tooltip, flag);
//		boolean desc = Screen.hasShiftDown();
//		if (!desc) {
//			addHoldShift(desc, tooltip);
//			return;
//		}
//		CBCMSTooltip.appendLandingIndicatorInfo(stack,level,tooltip,flag);
//	}
//
//
//}
