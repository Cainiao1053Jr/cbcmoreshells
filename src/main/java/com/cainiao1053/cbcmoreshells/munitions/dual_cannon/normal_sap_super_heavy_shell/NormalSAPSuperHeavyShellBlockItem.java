package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_super_heavy_shell;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonLoadout;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonStats;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.StatSink;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;



public class NormalSAPSuperHeavyShellBlockItem extends FuzedDualCannonProjectileBlockItem {

	public NormalSAPSuperHeavyShellBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, context, tooltip, flag);
		CBCMSTooltip.appendExplosiveDualCannonProjectileInfo(stack, context, tooltip, flag,
				getMunitionEntry());
	}

	@Override
	public void collectMaterialStats(StatSink<DualCannonLoadout> sink) {
		super.collectMaterialStats(sink);
		sink.add(DualCannonStats.EXPLOSION_POWER);
	}

}
