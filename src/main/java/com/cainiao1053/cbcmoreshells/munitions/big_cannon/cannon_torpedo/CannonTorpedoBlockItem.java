package com.cainiao1053.cbcmoreshells.munitions.big_cannon.cannon_torpedo;

import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedTorpedoProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.CANNON_TORPEDO;

public class CannonTorpedoBlockItem extends FuzedTorpedoProjectileBlockItem {

	public CannonTorpedoBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		TorpedoProperties properties = CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE.getPropertiesOf(CANNON_TORPEDO.get());
		//CBCMSTooltip.appendTorpedoInfo(stack, level, tooltip, flag, properties.torpedoProperties().torpedoSpeed(),properties.torpedoProperties().buoyancyFactor(),properties.lifetime());

	}

}
