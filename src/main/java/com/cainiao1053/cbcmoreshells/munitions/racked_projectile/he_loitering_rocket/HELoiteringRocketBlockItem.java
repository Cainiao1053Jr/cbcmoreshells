package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_loitering_rocket;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.AbstractRackedRocketBlockItem;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class HELoiteringRocketBlockItem extends AbstractRackedRocketBlockItem<HELoiteringRocketProjectile> {

	public HELoiteringRocketBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		//super.appendHoverText(stack, level, tooltip, flag);
		//RackedLoiteringRocketProjectileProperties properties = CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET.getPropertiesOf(HE_LOITERING_ROCKET.get());
		//CBCMSTooltip.appendRackedRocketInfo(stack, level, tooltip, flag, properties.ballistics().durabilityMass(), properties.ballistics().penetration(), properties.ballistics().deflection(), properties.explosion().explosivePower(),properties.lifetime(), properties.steadyStateVel(), properties.thrustTime());
	}

	@Override
	public EntityType<? extends HELoiteringRocketProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.HE_LOITERING_ROCKET.get();
	}

}
