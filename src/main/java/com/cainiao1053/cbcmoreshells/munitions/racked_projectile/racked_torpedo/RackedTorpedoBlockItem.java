package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.racked_torpedo;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.FuzedRackedProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedTorpedoProjectileProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.RACKED_TORPEDO;


public class RackedTorpedoBlockItem extends FuzedRackedProjectileBlockItem {

	public RackedTorpedoBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		RackedTorpedoProjectileProperties properties = CBCMSMunitionPropertiesHandlers.RACKED_TORPEDO.getPropertiesOf(RACKED_TORPEDO.get());
		CBCMSTooltip.appendRackedTorpedoInfo(stack, context, tooltip, flag, properties.explosion().blockDamagePower(),properties.lifetime(), properties.buoyancyFactor(), properties.steadyStateVel(), properties.waterDamp());
	}

}
