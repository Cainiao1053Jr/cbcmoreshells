package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;
import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.getPalette;

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
		String key1 = "block."+ Cbcmoreshells.MODID+".fuzedrackedprojectile.tooltip.title";
		FontHelper.Palette palette = getPalette(context, stack);
		tooltip.add(Component.translatable(key1).withStyle(ChatFormatting.GRAY));
		String key2 = "block."+Cbcmoreshells.MODID+".fuzedrackedprojectile.tooltip.desc";
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2), palette.primary(), palette.highlight(), 1));

	}

}
