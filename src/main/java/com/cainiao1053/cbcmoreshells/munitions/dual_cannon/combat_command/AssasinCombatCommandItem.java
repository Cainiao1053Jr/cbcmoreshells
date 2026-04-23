package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class AssasinCombatCommandItem extends CombatCommandBaseItem {

	public AssasinCombatCommandItem(Properties properties) {
		super(properties);
	}


	@Override
	public float getCommandDurabilityModifier() {
		return 1.55f;
	}

	@Override
	public float getCommandSpreadModifier() {
		return 1.5f;
	}

	@Override
	public float getCommandReloadTimeModifier() {
		return 2f;
	}

	@Override
	public int getMaximumUseAtOnce() {
		return 6;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CBCMSTooltip.appendCombatCommandDamageInfo(stack,context,tooltip,flag,getCommandDurabilityModifier());
		CBCMSTooltip.appendCombatCommandSpreadInfo(stack,context,tooltip,flag,getCommandSpreadModifier());
		CBCMSTooltip.appendCombatCommandReloadInfo(stack,context,tooltip,flag,getCommandReloadTimeModifier());
	}
}
