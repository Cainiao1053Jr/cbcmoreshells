package com.cainiao1053.cbcmoreshells.munitions.fuzes;

import java.util.List;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.createmod.catnip.lang.Lang;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.base.CBCTooltip;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

public class SensitiveImpactFuzeItem extends FuzeItem {

	public SensitiveImpactFuzeItem(Properties properties) {
		super(properties);
	}

	@Override
	public boolean onProjectileImpact(ItemStack stack, AbstractCannonProjectile projectile, HitResult hitResult, AbstractCannonProjectile.ImpactResult impactResult, boolean baseFuze) {
		if (baseFuze) return false;
		CompoundTag tag = stack.getOrCreateTag();
		int damage = tag.contains("Damage") ? tag.getInt("Damage") : this.getFuzeDurability();
		if (damage > 0) {
			--damage;
			tag.putInt("Damage", damage);
		}
		if (damage == 0) return false;
		float f = this.getDetonateChance();
		return f > 0 && projectile.level().getRandom().nextFloat() < f;
	}

	@Override
	public boolean onProjectileTick(ItemStack stack, AbstractCannonProjectile projectile) {
		return !projectile.level().getFluidState(projectile.blockPosition()).isEmpty();
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		//CBCTooltip.appendImpactFuzeText(stack, level, tooltip, flag, this.getDetonateChance(), this.getFuzeDurability());
		CBCMSTooltip.appendSensitiveFuzeInfo(stack,context.level(),tooltip,flag);
	}

	protected float getDetonateChance() {
		return CBCConfigs.server().munitions.impactFuzeDetonationChance.getF();
	}

	protected int getFuzeDurability() {
		return CBCConfigs.server().munitions.impactFuzeDurability.get();
	}

	@Override
	public void addExtraInfo(List<Component> tooltip, boolean isSneaking, ItemStack stack) {
		super.addExtraInfo(tooltip, isSneaking, stack);
		MutableComponent info = Lang.builder("item")
			.translate(CreateBigCannons.MOD_ID + ".impact_fuze.tooltip.shell_info.chance", (int) (this.getDetonateChance() * 100.0f))
			.component();
		tooltip.addAll(TooltipHelper.cutTextComponent(info, Style.EMPTY, Style.EMPTY, 6));
	}

}
