package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_super_heavy_shell;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonProperties;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.List;

import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.NORMAL_SAP_SHELL;
import static com.cainiao1053.cbcmoreshells.CBCMSEntityTypes.NORMAL_SAP_SUPER_HEAVY_SHELL;


public class NormalSAPSuperHeavyShellBlockItem extends FuzedDualCannonProjectileBlockItem {

	public NormalSAPSuperHeavyShellBlockItem(Block block, Properties properties) {
		super(block, properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, level, tooltip, flag);
		DualCannonProperties properties = CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES.getPropertiesOf(NORMAL_SAP_SUPER_HEAVY_SHELL.get());
		CBCMSTooltip.appendExplosiveDualCannonProjectileInfo(stack, level, tooltip, flag, properties);
	}

}
