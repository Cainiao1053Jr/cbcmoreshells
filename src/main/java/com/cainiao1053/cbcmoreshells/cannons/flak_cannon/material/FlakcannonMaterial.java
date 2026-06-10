package com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public record FlakcannonMaterial(ResourceLocation name, FlakcannonMaterialProperties defaultProperties) {

	public FlakcannonMaterialProperties properties() {
		FlakcannonMaterialProperties handlerProperties = FlakcannonMaterialPropertiesHandler.getMaterial(this);
		return handlerProperties == null ? this.defaultProperties : handlerProperties;
	}

    private static final Map<ResourceLocation, FlakcannonMaterial> CANNON_MATERIALS = new HashMap<>();

    public static FlakcannonMaterial register(ResourceLocation loc, FlakcannonMaterialProperties defaultProperties) {
        FlakcannonMaterial material = new FlakcannonMaterial(loc, defaultProperties);
        CANNON_MATERIALS.put(material.name(), material);
        return material;
    }

    public static FlakcannonMaterial fromName(ResourceLocation loc) {
		if (!CANNON_MATERIALS.containsKey(loc)) throw new IllegalArgumentException("No autocannon material '" + loc + "' registered");
        return CANNON_MATERIALS.get(loc);
    }

	public static FlakcannonMaterial fromNameOrNull(ResourceLocation loc) { return CANNON_MATERIALS.get(loc); }

}
