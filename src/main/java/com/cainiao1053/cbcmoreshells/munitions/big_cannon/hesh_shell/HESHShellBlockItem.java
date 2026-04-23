package com.cainiao1053.cbcmoreshells.munitions.big_cannon.hesh_shell;

import com.simibubi.create.foundation.item.TooltipHelper;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;
import static rbasamoyai.createbigcannons.base.CBCTooltip.getPalette;

public class HESHShellBlockItem extends FuzedProjectileBlockItem {

	public HESHShellBlockItem(Block block, Properties properties) {
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
		String key1 = stack.getDescriptionId()+".tooltip.title";
		TooltipHelper.Palette palette = getPalette();
		tooltip.add(Component.translatable(key1).withStyle(ChatFormatting.GRAY));
		String key2 = stack.getDescriptionId()+".tooltip.desc";
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2), palette.primary(), palette.highlight(), 1));

	}

}
