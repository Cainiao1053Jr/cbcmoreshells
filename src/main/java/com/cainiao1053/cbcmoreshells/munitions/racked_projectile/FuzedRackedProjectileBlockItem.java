package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class FuzedRackedProjectileBlockItem extends FuzedProjectileBlockItem {

	public FuzedRackedProjectileBlockItem(Block block, Properties properties) {
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
//		String key1 = "block."+Cbcmoreshells.MODID+".fuzedrackedprojectile.tooltip.title";
//		TooltipHelper.Palette palette = getPalette(level, stack);
//		tooltip.add(Components.translatable(key1).withStyle(ChatFormatting.GRAY));
//		String key2 = "block."+Cbcmoreshells.MODID+".fuzedrackedprojectile.tooltip.desc";
//		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2), palette.primary(), palette.highlight(), 1));

	}

}
