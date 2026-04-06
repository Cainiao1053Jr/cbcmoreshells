package com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedTorpedoProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.SharpnelTorpedoProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.SHARPNEL_TORPEDO;

public class SharpnelTorpedoBlockItem extends FuzedTorpedoProjectileBlockItem {

	public SharpnelTorpedoBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		SharpnelTorpedoProperties properties = CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE.getPropertiesOf(SHARPNEL_TORPEDO.get());
		//CBCMSTooltip.appendTorpedoInfo(stack, level, tooltip, flag, properties.torpedoProperties().torpedoSpeed(),properties.torpedoProperties().buoyancyFactor(),properties.lifetime());

	}

}
