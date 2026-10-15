package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterialProperties;

public class CBCMSPackageSliderMaterials {

	public static final PackageSliderMaterial STEEL = PackageSliderMaterial.register(Cbcmoreshells.resource("steel"),
		new PackageSliderMaterialProperties(1.5f, 16, 5, 0.2f));

	public static final PackageSliderMaterial WOODEN = PackageSliderMaterial.register(Cbcmoreshells.resource("wooden"),
			new PackageSliderMaterialProperties(1.0f, 6, 5, 1.5f));

	public static final PackageSliderMaterial NETHER_STEEL = PackageSliderMaterial.register(Cbcmoreshells.resource("nether_steel"),
			new PackageSliderMaterialProperties(3.0f, 32, 5, 0));

}
