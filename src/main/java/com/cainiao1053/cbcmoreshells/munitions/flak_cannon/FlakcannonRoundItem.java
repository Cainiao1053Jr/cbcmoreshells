package com.cainiao1053.cbcmoreshells.munitions.flak_cannon;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.cainiao1053.cbcmoreshells.munitions.flak_cannon.config.FlakcannonProjectilePropertiesComponent;
import com.simibubi.create.foundation.utility.Lang;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.CreateBigCannons;

public abstract class FlakcannonRoundItem extends Item {

    protected FlakcannonRoundItem(Properties properties) {
        super(properties);
    }

    public abstract AbstractFlakcannonProjectile getAutocannonProjectile(ItemStack stack, Level level);
	public abstract EntityType<?> getEntityType(ItemStack stack);

	@Nonnull public abstract FlakcannonProjectilePropertiesComponent getAutocannonProperties(ItemStack itemStack);

	public boolean isTracer(){
		return true;
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltipComponents, TooltipFlag isAdvanced) {
		super.appendHoverText(stack, level, tooltipComponents, isAdvanced);
		if (stack.getOrCreateTag().getBoolean("Tracer")) {
			Lang.builder("tooltip").translate(CreateBigCannons.MOD_ID + ".tracer").addTo(tooltipComponents);
		}
	}

}
