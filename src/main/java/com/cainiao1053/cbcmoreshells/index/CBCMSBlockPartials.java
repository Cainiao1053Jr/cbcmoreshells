package com.cainiao1053.cbcmoreshells.index;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material.FlakcannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.material.ProjectileRackMaterial;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterial;
import com.jozufozu.flywheel.core.PartialModel;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.DyeColor;

public class CBCMSBlockPartials {

	private static final Map<TorpedoTubeMaterial, PartialModel> BREECHBLOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<TorpedoTubeMaterial, PartialModel> SCREW_LOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<ProjectileRackMaterial, PartialModel> PROJECTILE_LOCK_BY_MATERIAL = new HashMap<>();
	private static final Map<DualCannonMaterial, PartialModel> DUAL_CANNON_BREECHBLOCK_BY_MATERIAL = new HashMap<>();

	private static final Map<FlakcannonMaterial, PartialModel> FLAKCANNON_SPRING_BY_MATERIAL = new HashMap<>();
	private static final Map<FlakcannonMaterial, PartialModel> FLAKCANNON_EJECTOR_BY_MATERIAL = new HashMap<>();
	private static final Map<DyeColor, PartialModel> FLAKCANNON_SEAT_BY_COLOR = new EnumMap<>(DyeColor.class);



	private static final Collection<Runnable> DEFERRED_MODEL_CALLBACKS = new ArrayList<>();

	public static final PartialModel
		CAST_IRON_SLIDING_BREECHBLOCK = breechblockPartial(CBCMSTorpedoTubeMaterials.CAST_IRON, "cast_iron_sliding_breechblock"),
		BRONZE_SLIDING_BREECHBLOCK = breechblockPartial(CBCMSTorpedoTubeMaterials.BRONZE, "bronze_sliding_breechblock"),
		STEEL_TORPEDO_SLIDING_BREECHBLOCK = breechblockPartial(CBCMSTorpedoTubeMaterials.STEEL, "steel_torpedo_sliding_breechblock"),

		QUICKFIRING_BREECH_LEVER = block("quickfiring_breech_lever"),

	    STEEL_PROJECTILE_RACK_SLIDING_BREECHBLOCK = projectileLockBlockPartial(CBCMSProjectileRackMaterials.STEEL, "steel_projectile_rack_sliding_breechblock"),

		STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.STEEL, "steel_dual_cannon_sliding_breechblock"),
		WIDE_STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_STEEL, "wide_steel_dual_cannon_sliding_breechblock"),
		WIDE_TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_TOUGH_STEEL, "wide_steel_dual_cannon_sliding_breechblock"),
		WIDE_BRASS_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_BRASS, "wide_steel_dual_cannon_sliding_breechblock"),
		WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_ROSEQUARTZ_BRASS, "wide_steel_dual_cannon_sliding_breechblock"),
		WIDE_NETHER_STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_NETHER_STEEL, "wide_steel_dual_cannon_sliding_breechblock"),
		STEEL_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.SINGLE_STEEL, "steel_single_cannon_sliding_breechblock"),
		WIDE_CAST_IRON_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_CAST_IRON, "wide_steel_single_cannon_sliding_breechblock"),
		WIDE_BRONZE_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_BRONZE, "wide_steel_single_cannon_sliding_breechblock"),
		WIDE_STEEL_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_STEEL, "wide_steel_single_cannon_sliding_breechblock"),
		WIDE_TOUGH_STEEL_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL, "wide_steel_single_cannon_sliding_breechblock"),
		WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY, "wide_steel_dual_cannon_sliding_breechblock"),
		WIDE_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY, "wide_steel_dual_cannon_sliding_breechblock"),
		BRASS_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.SINGLE_BRASS, "steel_single_cannon_sliding_breechblock"),
		WIDE_BRASS_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_BRASS, "wide_steel_single_cannon_sliding_breechblock"),
		SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.SINGLE_SLATE_ALLOY, "steel_single_cannon_sliding_breechblock"),
		MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.SINGLE_MILITARY_SLATE_ALLOY, "steel_single_cannon_sliding_breechblock"),
		WIDE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_SLATE_ALLOY, "wide_steel_single_cannon_sliding_breechblock"),
		WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.WIDE_SINGLE_MILITARY_SLATE_ALLOY, "wide_steel_single_cannon_sliding_breechblock"),
		LARGE_BRASS_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_BRASS, "large_steel_dual_cannon_sliding_breechblock"),
		LARGE_BRASS_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_SINGLE_BRASS, "large_steel_single_cannon_sliding_breechblock"),
		LARGE_STEEL_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_SINGLE_STEEL, "large_steel_single_cannon_sliding_breechblock"),
		LARGE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_SINGLE_SLATE_ALLOY, "large_steel_single_cannon_sliding_breechblock"),
		LARGE_STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_STEEL, "large_steel_dual_cannon_sliding_breechblock"),
		LARGE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_SINGLE_MILITARY_SLATE_ALLOY, "large_steel_single_cannon_sliding_breechblock"),
		LARGE_NETHER_STEEL_SINGLE_CANNON_SLIDING_BREECHBLOCK = dualCannonBreechblockPartial(CBCMSDualCannonMaterials.LARGE_SINGLE_NETHER_STEEL, "large_steel_single_cannon_sliding_breechblock"),



	STEEL_FLAKCANNON_SPRING = flakcannonSpringPartial(CBCMSFlakcannonMaterials.STEEL, "flakcannon/steel_flakcannon_spring"),
	STEEL_FLAKCANNON_EJECTOR = flakcannonEjectorPartial(CBCMSFlakcannonMaterials.STEEL, "steel"),

	FLAKCANNON_SEAT_WHITE = flakcannonSeatPartial(DyeColor.WHITE, "flakcannon/seat_white")
	;


	private static PartialModel block(String path) {
		return new PartialModel(Cbcmoreshells.resource("block/" + path));
	}
	private static PartialModel entity(String path) { return new PartialModel(Cbcmoreshells.resource("entity/" + path)); }

	private static PartialModel breechblockPartial(TorpedoTubeMaterial material, String path) {
		return breechblockPartial(material, Cbcmoreshells.resource("item/" + path));
	}

	public static PartialModel breechblockPartial(TorpedoTubeMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		BREECHBLOCK_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel breechblockFor(TorpedoTubeMaterial material) {
		return BREECHBLOCK_BY_MATERIAL.getOrDefault(material, STEEL_TORPEDO_SLIDING_BREECHBLOCK);
	}

	private static PartialModel projectileLockBlockPartial(ProjectileRackMaterial material, String path) {
		return projectileLockBlockPartial(material, Cbcmoreshells.resource("item/" + path));
	}

	public static PartialModel projectileLockBlockPartial(ProjectileRackMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		PROJECTILE_LOCK_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel projectileLockBlockFor(ProjectileRackMaterial material) {
		return PROJECTILE_LOCK_BY_MATERIAL.getOrDefault(material, STEEL_PROJECTILE_RACK_SLIDING_BREECHBLOCK);
	}

	private static PartialModel dualCannonBreechblockPartial(DualCannonMaterial material, String path) {
		return dualCannonBreechblockPartial(material, Cbcmoreshells.resource("item/" + path));
	}

	public static PartialModel dualCannonBreechblockPartial(DualCannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		DUAL_CANNON_BREECHBLOCK_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel dualCannonBreechblockFor(DualCannonMaterial material) {
		return DUAL_CANNON_BREECHBLOCK_BY_MATERIAL.getOrDefault(material, STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK);
	}

	private static PartialModel flakcannonSpringPartial(FlakcannonMaterial material, String path) {
		return flakcannonSpringPartial(material, Cbcmoreshells.resource("block/" + path));
	}

	public static PartialModel flakcannonSpringPartial(FlakcannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		FLAKCANNON_SPRING_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel flakcannonSpringFor(FlakcannonMaterial material) {
		return FLAKCANNON_SPRING_BY_MATERIAL.getOrDefault(material, STEEL_FLAKCANNON_SPRING);
	}

	private static PartialModel flakcannonEjectorPartial(FlakcannonMaterial material, String path) {
		return flakcannonEjectorPartial(material, Cbcmoreshells.resource("item/" + path + "_autocannon_breech_extractor"));
	}

	public static PartialModel flakcannonEjectorPartial(FlakcannonMaterial material, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		FLAKCANNON_EJECTOR_BY_MATERIAL.put(material, model);
		return model;
	}

	public static PartialModel flakcannonEjectorFor(FlakcannonMaterial material) {
		return FLAKCANNON_EJECTOR_BY_MATERIAL.getOrDefault(material, STEEL_FLAKCANNON_EJECTOR);
	}

	private static PartialModel flakcannonSeatPartial(DyeColor color, String path) {
		return flakcannonSeatPartial(color, Cbcmoreshells.resource("block/" + path));
	}

	public static PartialModel flakcannonSeatPartial(DyeColor color, ResourceLocation loc) {
		PartialModel model = new PartialModel(loc);
		FLAKCANNON_SEAT_BY_COLOR.put(color, model);
		return model;
	}

	public static PartialModel flakcannonSeatFor(DyeColor color) {
		return FLAKCANNON_SEAT_BY_COLOR.getOrDefault(color, FLAKCANNON_SEAT_WHITE);
	}




	public static void init() {}

	public static void resolveDeferredModels() {
		for (Runnable run : DEFERRED_MODEL_CALLBACKS) run.run();
		DEFERRED_MODEL_CALLBACKS.clear();
	}

}
