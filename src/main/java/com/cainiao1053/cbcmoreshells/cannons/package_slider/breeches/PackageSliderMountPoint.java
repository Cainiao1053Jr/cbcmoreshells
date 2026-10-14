package com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedPackageSliderContraption;
import com.simibubi.create.content.logistics.box.PackageItem;
import net.minecraft.world.item.ItemStack;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

public final class PackageSliderMountPoint {

	private PackageSliderMountPoint() {
	}

	public static ItemStack packageSliderInsert(ItemStack stack, boolean simulate, MountedPackageSliderContraption cannon,
												PitchOrientedContraptionEntity poce) {
		if (!PackageItem.isPackage(stack) || !cannon.canLoadPackage()) return stack;
		if (!simulate) cannon.tryLoadPackage(stack, poce);
		ItemStack copy = stack.copy();
		copy.shrink(1);
		return copy;
	}

}
