package com.cainiao1053.cbcmoreshells.munitions.big_cannon.shellless_ap_shot;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessInertProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.ShellessInertBigCannonProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.SHELLESS_AP_SHOT;

public class ShellessAPProjectileBlockItem extends ShellessInertProjectileBlockItem {

	public ShellessAPProjectileBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);

		ShellessInertBigCannonProperties properties = CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE.getPropertiesOf(SHELLESS_AP_SHOT.get());
		CBCMSTooltip.appendBallisticInfo(stack, context, tooltip, flag, properties.ballistics().durabilityMass(), properties.ballistics().penetration(), properties.ballistics().deflection());

	}

}
