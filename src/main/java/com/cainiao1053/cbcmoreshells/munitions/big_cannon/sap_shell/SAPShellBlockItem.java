package com.cainiao1053.cbcmoreshells.munitions.big_cannon.sap_shell;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.BigCannonShellessShellProperties;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.SAP_SHELL;
import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class SAPShellBlockItem extends FuzedProjectileBlockItem {

	public SAPShellBlockItem(Block block, Properties properties) {
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

		BigCannonShellessShellProperties properties = CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(SAP_SHELL.get());
		CBCMSTooltip.appendExplosiveInfo(stack, context, tooltip, flag, properties.ballistics().durabilityMass(), properties.ballistics().penetration(), properties.ballistics().deflection(),properties.explosion().explosivePower());

	}

}
