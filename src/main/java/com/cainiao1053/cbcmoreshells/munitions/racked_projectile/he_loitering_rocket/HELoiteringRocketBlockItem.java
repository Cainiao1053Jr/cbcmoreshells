package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_loitering_rocket;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.AbstractRackedRocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedLoiteringRocketProjectileProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.HE_LOITERING_ROCKET;

public class HELoiteringRocketBlockItem extends AbstractRackedRocketBlockItem<HELoiteringRocketProjectile> {

	public HELoiteringRocketBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		RackedLoiteringRocketProjectileProperties properties = CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET.getPropertiesOf(HE_LOITERING_ROCKET.get());
		CBCMSTooltip.appendLoiteringRackedRocketInfo(stack, context, tooltip, flag, properties);
	}

	@Override
	public EntityType<? extends HELoiteringRocketProjectile> getAssociatedEntityType() {
		return HE_LOITERING_ROCKET.get();
	}

}
