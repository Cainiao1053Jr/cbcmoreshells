package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_he_rocket;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.FuzedRackedProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedRocketProjectileProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.DUAL_HE_ROCKET;

public class DualHERocketBlockItem extends FuzedRackedProjectileBlockItem {

	public DualHERocketBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		RackedRocketProjectileProperties properties = CBCMSMunitionPropertiesHandlers.RACKED_ROCKET.getPropertiesOf(DUAL_HE_ROCKET.get());
		CBCMSTooltip.appendRackedRocketInfo(stack, context, tooltip, flag, properties.ballistics().durabilityMass(), properties.ballistics().penetration(), properties.ballistics().deflection(), properties.explosion().blockDamagePower(),properties.lifetime(), properties.steadyStateVel(), properties.thrustTime());

	}

}
