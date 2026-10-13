package com.cainiao1053.cbcmoreshells.cannons.package_slider.material;

import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public record PackageSliderMaterial(ResourceLocation name, PackageSliderMaterialProperties defaultProperties) {

	private static final Map<ResourceLocation, PackageSliderMaterial> CANNON_MATERIALS = new HashMap<>();

	public PackageSliderMaterialProperties properties() {
		return this.defaultProperties;
	}

	public static PackageSliderMaterial register(ResourceLocation loc, PackageSliderMaterialProperties defaultProperties) {
		PackageSliderMaterial material = new PackageSliderMaterial(loc, defaultProperties);
		CANNON_MATERIALS.put(material.name(), material);
		return material;
	}

	@Nullable
	public static PackageSliderMaterial fromNameOrNull(ResourceLocation loc) {
		return CANNON_MATERIALS.get(loc);
	}

}
