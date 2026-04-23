package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MyopiaCombatCommandItem extends CombatCommandBaseItem {

	public MyopiaCombatCommandItem(Properties properties) {
		super(properties);
	}


	@Override
	public float getCommandSpreadModifier() {
		return 1.85f;
	}

	@Override
	public float getCommandReloadTimeModifier() {
		return 0.25f;
	}

	@Override
	public float getCommandLifetimeModifier() {
		return 0.75f;
	}

	@Override
	public int getMaximumUseAtOnce() {
		return 8;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CBCMSTooltip.appendCombatCommandSpreadInfo(stack,context,tooltip,flag,getCommandSpreadModifier());
		CBCMSTooltip.appendCombatCommandReloadInfo(stack,context,tooltip,flag,getCommandReloadTimeModifier());
		CBCMSTooltip.appendCombatCommandRangeInfo(stack,context,tooltip,flag,getCommandLifetimeModifier());
	}
}
