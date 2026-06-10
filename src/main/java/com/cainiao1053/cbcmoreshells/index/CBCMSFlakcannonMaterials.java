package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material.FlakcannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material.FlakcannonMaterialProperties;

public class CBCMSFlakcannonMaterials {

	public static final FlakcannonMaterial

		STEEL = FlakcannonMaterial.register(Cbcmoreshells.resource("steel"),
			FlakcannonMaterialProperties.builder()
				.maxBarrelLength(7)
				.weight(2.5f)
				.baseSpread(3.0f)
				.spreadReductionPerBarrel(1.5f)
				.baseSpeed(3f)
				.speedIncreasePerBarrel(1.5f)
				.maxSpeedIncreases(4)
				.projectileLifetime(60)
				.baseRecoil(3f)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(2)
				.weldStressPenalty(2)
				.build());

}
