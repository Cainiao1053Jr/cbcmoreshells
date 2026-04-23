package com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shot;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.ShellessInertBigCannonProperties;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlockItem;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.APBC_SHOT;
import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;

public class APBCBlockItem extends ProjectileBlockItem {

	public APBCBlockItem(Block block, Properties properties) {
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

		ShellessInertBigCannonProperties properties = CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE.getPropertiesOf(APBC_SHOT.get());
		CBCMSTooltip.appendBallisticInfo(stack, context, tooltip, flag, properties.ballistics().durabilityMass(), properties.ballistics().penetration(), properties.ballistics().deflection());


	}

}
