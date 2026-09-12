package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.DualCannonPenetrationModel;

@FunctionalInterface
public interface DualCannonMomentumModel {
	DualCannonMomentumModel CAPPED = (loadout, speed) ->
		DualCannonPenetrationModel.getCappedMomentum(loadout.shell().shell(), loadout.durabilityModifier(), speed, loadout.effectiveMass());

	double momentum(DualCannonLoadout loadout, double speed);

}
