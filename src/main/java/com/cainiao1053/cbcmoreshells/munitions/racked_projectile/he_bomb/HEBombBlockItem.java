package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bomb;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.FuzedRackedProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedProjectileProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.HE_BOMB;

public class HEBombBlockItem extends FuzedRackedProjectileBlockItem {

	public HEBombBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		RackedProjectileProperties properties = CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE.getPropertiesOf(HE_BOMB.get());
		CBCMSTooltip.appendBombInfo(stack, context, tooltip, flag, properties.ballistics().durabilityMass(), properties.ballistics().penetration(), properties.ballistics().deflection(), properties.explosion().explosivePower(),properties.lifetime());

	}

}
