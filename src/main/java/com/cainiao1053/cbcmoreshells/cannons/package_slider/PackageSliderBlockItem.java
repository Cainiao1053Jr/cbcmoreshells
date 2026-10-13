package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;

public class PackageSliderBlockItem<T extends Block & PackageSliderBlock> extends BlockItem {

	public PackageSliderBlockItem(T block, Properties properties) {
		super(block, properties);
	}

	@Override
	public InteractionResult place(BlockPlaceContext context) {
		InteractionResult result = super.place(context);
		// Package slider materials always connect in survival.
		if (result.consumesAction())
			PackageSliderBlock.onPlace(context.getLevel(), context.getClickedPos());
		return result;
	}

}
