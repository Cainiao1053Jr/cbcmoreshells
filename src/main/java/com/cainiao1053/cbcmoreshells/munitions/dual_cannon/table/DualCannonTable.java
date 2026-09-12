package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;

import javax.annotation.Nullable;
import java.util.List;

public record DualCannonTable(DualCannonShellContext shell,
							  List<StatSpec<DualCannonShellContext>> shellStats,
							  List<StatSpec<DualCannonLoadout>> materialStats,
							  @Nullable BallisticSkeleton skeleton,
							  List<BallisticColumnMode> modes,
							  List<DualCannonMaterial> materials) {

	public DualCannonTable {
		shellStats = List.copyOf(shellStats);
		materialStats = List.copyOf(materialStats);
		modes = List.copyOf(modes);
		materials = List.copyOf(materials);
	}

	/** False when the shell's ballistics fall outside the closed form; show the stats only. */
	public boolean hasBallistics() {
		return this.skeleton != null;
	}

	public int ballisticColumns() {
		return this.skeleton == null ? 0 : this.skeleton.columns();
	}

	/** Material name column, plus one per material stat, plus one per distance. */
	public int totalColumns() {
		return 1 + this.materialStats.size() + this.ballisticColumns();
	}

	public BallisticColumnMode defaultMode() {
		return this.modes.isEmpty() ? BallisticColumnMode.FLIGHT_TIME : this.modes.get(0);
	}

	/** Next mode the toggle button should switch to. */
	public BallisticColumnMode nextMode(BallisticColumnMode current) {
		int index = this.modes.indexOf(current);
		return this.modes.isEmpty() ? current : this.modes.get((index + 1) % this.modes.size());
	}

	/** Only shell stats that this shell actually reports a number for. */
	public List<StatSpec<DualCannonShellContext>> applicableShellStats() {
		return this.shellStats.stream().filter(spec -> spec.applies(this.shell)).toList();
	}

}
