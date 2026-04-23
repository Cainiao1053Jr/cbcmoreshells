package com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_shrapnel_torpedo;

import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedTorpedoProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.SharpnelTorpedoProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.LONG_RANGE_SHRAPNEL_TORPEDO;

public class LongRangeShrapnelTorpedoBlockItem extends FuzedTorpedoProjectileBlockItem {

	public LongRangeShrapnelTorpedoBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		SharpnelTorpedoProperties properties = CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE.getPropertiesOf(LONG_RANGE_SHRAPNEL_TORPEDO.get());
		//CBCMSTooltip.appendTorpedoInfo(stack, level, tooltip, flag, properties.torpedoProperties().torpedoSpeed(),properties.torpedoProperties().buoyancyFactor(),properties.lifetime());

	}

}
