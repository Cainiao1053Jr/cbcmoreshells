package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import java.util.List;

public record MaterialRow(DualCannonLoadout loadout, boolean missingSingleVariant,
						  double[] statValues, double[] momentum, boolean[] reachable) {

	/** Computes a row against an already-built skeleton; only the momentum column is new work. */
	public static MaterialRow compute(DualCannonLoadout loadout, BallisticSkeleton skeleton,
									  List<StatSpec<DualCannonLoadout>> statSpecs, boolean missingSingleVariant) {
		double[] statValues = new double[statSpecs.size()];
		for (int i = 0; i < statSpecs.size(); i++) statValues[i] = statSpecs.get(i).valueOf(loadout);

		DualCannonMomentumModel model = loadout.shell().item().momentumModel();
		int lifetime = loadout.lifetimeTicks();
		int columns = skeleton.columns();
		double[] momentum = new double[columns];
		boolean[] reachable = new boolean[columns];
		for (int i = 0; i < columns; i++) {
			BallisticPoint point = skeleton.point(i);
			momentum[i] = model.momentum(loadout, point.impactSpeed());
			reachable[i] = point.reachableWithin(lifetime);
		}
		return new MaterialRow(loadout, missingSingleVariant, statValues, momentum, reachable);
	}

	/** Value for a distance column, whichever mode the table is showing. */
	public double ballisticValue(BallisticColumnMode mode, BallisticSkeleton skeleton, int column) {
		return mode == BallisticColumnMode.MOMENTUM ? this.momentum[column] : skeleton.value(mode, column);
	}

	/** Formatted for display, or a dash once the shell can no longer get this far. */
	public String formatBallistic(BallisticColumnMode mode, BallisticSkeleton skeleton, int column) {
		if (!this.reachable[column]) return StatFormat.NOT_APPLICABLE;
		return mode.format().format(this.ballisticValue(mode, skeleton, column));
	}

	public String formatStat(List<StatSpec<DualCannonLoadout>> statSpecs, int index) {
		return statSpecs.get(index).format().format(this.statValues[index]);
	}

	/** Furthest column this material can still reach, or -1 if it cannot reach the first one. */
	public int lastReachableColumn() {
		for (int i = this.reachable.length - 1; i >= 0; i--) {
			if (this.reachable[i]) return i;
		}
		return -1;
	}

}
