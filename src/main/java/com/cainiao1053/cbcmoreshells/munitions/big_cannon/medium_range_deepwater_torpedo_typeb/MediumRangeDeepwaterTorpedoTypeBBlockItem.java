package com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo_typeb;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedTorpedoProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.MEDIUM_RANGE_DEEPWATER_TORPEDO_TYPEB;

public class MediumRangeDeepwaterTorpedoTypeBBlockItem extends FuzedTorpedoProjectileBlockItem {

	public MediumRangeDeepwaterTorpedoTypeBBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		TorpedoProperties properties = CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE.getPropertiesOf(MEDIUM_RANGE_DEEPWATER_TORPEDO_TYPEB.get());
		CBCMSTooltip.appendTorpedoInfo(stack, context, tooltip, flag, properties);

	}

}
