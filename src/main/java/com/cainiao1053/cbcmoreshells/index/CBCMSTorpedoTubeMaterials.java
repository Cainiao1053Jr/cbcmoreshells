package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterial;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterialProperties;


public class CBCMSTorpedoTubeMaterials {

	public static final TorpedoTubeMaterial


		CAST_IRON = TorpedoTubeMaterial.register(Cbcmoreshells.resource("cast_iron"),
				TorpedoTubeMaterialProperties.builder()
				.minimumVelocityPerBarrel(2d)
				.weight(3.0f)
				.maxSafePropellantStress(2)
				.failureMode(TorpedoTubeMaterialProperties.FailureMode.RUPTURE)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(1)
				.weldStressPenalty(1)
				.minimumSpread(0.05f)
				.spreadReductionPerBarrel(2f)
				.build()),

		BRONZE = TorpedoTubeMaterial.register(Cbcmoreshells.resource("bronze"),
				TorpedoTubeMaterialProperties.builder()
				.minimumVelocityPerBarrel(4d / 3d)
				.weight(2.0f)
				.maxSafePropellantStress(4)
				.failureMode(TorpedoTubeMaterialProperties.FailureMode.RUPTURE)
				.connectsInSurvival(false)
				.isWeldable(true)
				.weldDamage(1)
				.weldStressPenalty(0)
				.minimumSpread(0.03f)
				.spreadReductionPerBarrel(1.4f)
				.build()),

		STEEL = TorpedoTubeMaterial.register(Cbcmoreshells.resource("steel"),
				TorpedoTubeMaterialProperties.builder()
				.minimumVelocityPerBarrel(1d)
				.weight(3.0f)
				.maxSafePropellantStress(10)
				.failureMode(TorpedoTubeMaterialProperties.FailureMode.RUPTURE)
				.connectsInSurvival(true)
				.isWeldable(true)
				.weldDamage(0)
				.weldStressPenalty(0)
				.minimumSpread(1f)
				.spreadReductionPerBarrel(0f)
				.build());



}
