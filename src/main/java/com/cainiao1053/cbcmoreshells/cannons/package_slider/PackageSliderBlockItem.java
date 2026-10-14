package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.cainiao1053.cbcmoreshells.base.CBCMSTooltip;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class PackageSliderBlockItem<T extends Block & PackageSliderBlock> extends BlockItem {

	private final T cannonBlock;

	public PackageSliderBlockItem(T block, Properties properties) {
		super(block, properties);
		this.cannonBlock = block;
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		// Package slider materials always connect in survival.
		if (result.consumesAction())
			PackageSliderBlock.onPlace(context.getLevel(), context.getClickedPos());
		return result;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext ctx, List<Component> tooltip, TooltipFlag flag) {
		super.appendHoverText(stack, ctx, tooltip, flag);
		CBCMSTooltip.appendPackageSliderBlockText(stack, ctx, tooltip, flag, cannonBlock);
	}
}
