package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public enum DualCannonMaterialFilter {
	SINGLE_ONLY,
	SINGLE_PLUS_GAPS,
	ALL;

	private static final String SINGLE = "single_";
	private static final String[] BORE_PREFIXES = {"wide_", "large_"};

	/** Materials to show, lightest durability mass modifier first. */
	public List<DualCannonMaterial> select() {
		List<DualCannonMaterial> selected = new ArrayList<>();
		for (DualCannonMaterial material : DualCannonMaterial.all()) {
			if (this == ALL || material.properties().isSingleBarrel()) selected.add(material);
		}
		if (this == SINGLE_PLUS_GAPS) selected.addAll(missingSingleVariants());
		selected.sort(Comparator.comparingDouble(m -> m.properties().durabilityMassModifier()));
		return selected;
	}

	public static List<DualCannonMaterial> missingSingleVariants() {
		Set<String> singlePaths = new HashSet<>();
		for (DualCannonMaterial material : DualCannonMaterial.all()) {
			if (material.properties().isSingleBarrel()) singlePaths.add(material.name().getPath());
		}

		List<DualCannonMaterial> missing = new ArrayList<>();
		for (DualCannonMaterial material : DualCannonMaterial.all()) {
			if (material.properties().isSingleBarrel()) continue;
			if (!singlePaths.contains(singleVariantPath(material.name().getPath()))) missing.add(material);
		}
		return missing;
	}

	/** {@code steel -> single_steel}, {@code wide_steel -> wide_single_steel}. */
	private static String singleVariantPath(String dualPath) {
		for (String prefix : BORE_PREFIXES) {
			if (dualPath.startsWith(prefix)) return prefix + SINGLE + dualPath.substring(prefix.length());
		}
		return SINGLE + dualPath;
	}

	/** Translation key for a material's display name, matching the existing cannon block keys. */
	public static String translationKey(DualCannonMaterial material) {
		return "block." + material.name().getNamespace() + ".material." + material.name().getPath();
	}

}
