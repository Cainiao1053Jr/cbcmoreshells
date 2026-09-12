package com.cainiao1053.cbcmoreshells.munitions.dual_cannon;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonMunitionRegistry;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonMunitionProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.BallisticColumnMode;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonLoadout;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonMomentumModel;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonShellContext;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonStats;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.StatSink;
import com.simibubi.create.foundation.item.TooltipHelper;
import net.createmod.catnip.lang.FontHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.munitions.FuzedProjectileBlockItem;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.base.CBCMSTooltip.addHoldShift;
import static rbasamoyai.createbigcannons.base.CBCTooltip.getPalette;

public class FuzedDualCannonProjectileBlockItem extends FuzedProjectileBlockItem {

	public FuzedDualCannonProjectileBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		boolean desc = Screen.hasShiftDown();
		if (!desc) {
			addHoldShift(desc, tooltip);
			return;
		}
		String key1 = "block."+Cbcmoreshells.MODID+".dual_cannon_projectile.tooltip.title";
		FontHelper.Palette palette = getPalette();
		tooltip.add(Component.translatable(key1).withStyle(ChatFormatting.GRAY));
		String key2 = "block."+Cbcmoreshells.MODID+".dual_cannon_projectile.tooltip.desc";
		tooltip.addAll(TooltipHelper.cutStringTextComponent(I18n.get(key2), palette.primary(), palette.highlight(), 1));

	}

	@Nullable
	public CBCMSDualCannonMunitionRegistry.Entry getMunitionEntry() {
		return CBCMSDualCannonMunitionRegistry.of(this.getBlock());
	}

	@Nullable
	public DualCannonMunitionProperties getProjectileProperties() {
		CBCMSDualCannonMunitionRegistry.Entry entry = this.getMunitionEntry();
		return entry == null ? null : CBCMSDualCannonMunitionRegistry.properties(entry);
	}


	public void collectShellStats(StatSink<DualCannonShellContext> sink) {
		sink.add(DualCannonStats.MUZZLE_VELOCITY);
		sink.add(DualCannonStats.DEFLECTION_ANGLE);
		sink.add(DualCannonStats.BOUNCE_ANGLE);
	}

	public void collectMaterialStats(StatSink<DualCannonLoadout> sink) {
		sink.add(DualCannonStats.RELOAD);
		sink.add(DualCannonStats.RECOIL);
	}

	public List<BallisticColumnMode> ballisticModes() {
		return List.of(BallisticColumnMode.MOMENTUM, BallisticColumnMode.FLIGHT_TIME);
	}

	public DualCannonMomentumModel momentumModel() {
		return DualCannonMomentumModel.CAPPED;
	}

}
