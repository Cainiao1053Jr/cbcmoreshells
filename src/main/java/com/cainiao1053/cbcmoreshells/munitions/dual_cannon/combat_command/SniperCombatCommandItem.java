package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SniperCombatCommandItem extends CombatCommandBaseItem {

	public SniperCombatCommandItem(Properties properties) {
		super(properties);
	}


	@Override
	public float getCommandSpreadModifier() {
		return 0.2f;
	}

	@Override
	public float getCommandReloadTimeModifier() {
		return 2.0f;
	}

	@Override
	public float getCommandLifetimeModifier() {
		return 2f;
	}

	@Override
	public float getCommandDurabilityModifier() {
		return 1.2f;
	}

	@Override
	public int getMaximumUseAtOnce() {
		return 8;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CBCMSTooltip.appendCombatCommandDamageInfo(stack,context,tooltip,flag,getCommandDurabilityModifier());
		CBCMSTooltip.appendCombatCommandSpreadInfo(stack,context,tooltip,flag,getCommandSpreadModifier());
		CBCMSTooltip.appendCombatCommandReloadInfo(stack,context,tooltip,flag,getCommandReloadTimeModifier());
		CBCMSTooltip.appendCombatCommandRangeInfo(stack,context,tooltip,flag,getCommandLifetimeModifier());
	}
}
