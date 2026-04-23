package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.highspeed_long_range_torpedo;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedTorpedoProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.HIGHSPEED_LONG_RANGE_TORPEDO;

public class HighspeedLongRangeTorpedoBlockItem extends FuzedTorpedoProjectileBlockItem {

	public HighspeedLongRangeTorpedoBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		TorpedoProperties properties = CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE.getPropertiesOf(HIGHSPEED_LONG_RANGE_TORPEDO.get());
		CBCMSTooltip.appendTorpedoInfo(stack, context, tooltip, flag, properties);

	}

}
