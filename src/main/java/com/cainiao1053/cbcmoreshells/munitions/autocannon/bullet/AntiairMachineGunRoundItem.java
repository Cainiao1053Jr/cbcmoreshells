package com.cainiao1053.cbcmoreshells.munitions.autocannon.bullet;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.TooltipContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoItem;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;
import rbasamoyai.createbigcannons.munitions.autocannon.config.AutocannonProjectilePropertiesComponent;

import javax.annotation.Nullable;
import java.util.List;

public class AntiairMachineGunRoundItem extends Item implements AutocannonAmmoItem {


	public AntiairMachineGunRoundItem(Properties properties) {
		super(properties);
	}

	@Override
	@Nullable
	public AbstractAutocannonProjectile getAutocannonProjectile(ItemStack stack, Level level) {
		return CBCMSEntityTypes.ANTIAIR_MACHINE_GUN_BULLET.create(level);
	}

	@Nullable
	@Override
	public EntityType<?> getEntityType(ItemStack stack) {
		return CBCMSEntityTypes.ANTIAIR_MACHINE_GUN_BULLET.get();
	}

	@Override public boolean isTracer(ItemStack stack) { return stack.getComponents().getOrDefault(CBCDataComponents.AUTOCANNON_TRACER, false); }

	@Override
	public void setTracer(ItemStack stack, boolean value) {
		if (!stack.isEmpty()) stack.set(CBCDataComponents.AUTOCANNON_TRACER, value);
	}

	@Override public ItemStack getSpentItem(ItemStack stack) { return CBCItems.EMPTY_MACHINE_GUN_ROUND.asStack(); }

	@Override public AutocannonAmmoType getType() { return AutocannonAmmoType.MACHINE_GUN; }

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, context, tooltipComponents, isAdvanced);
//		if (stack.getOrCreateTag().getBoolean("Tracer")) {
//			Lang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
//		}
	}

	@Override
	public AutocannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack) {
		return CBCMSMunitionPropertiesHandlers.ANTIAIR_AUTOCANNON.getPropertiesOf(this.getEntityType(itemStack)).autocannonProperties();
	}

}
