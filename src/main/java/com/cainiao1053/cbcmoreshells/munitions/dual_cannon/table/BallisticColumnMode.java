package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

public enum BallisticColumnMode {

	MOMENTUM("cbcmoreshells.firing_table.column.momentum", StatFormat.PLAIN1, true),
	FLIGHT_TIME("cbcmoreshells.firing_table.column.flight_time", StatFormat.SECONDS, false),
	IMPACT_SPEED("cbcmoreshells.firing_table.column.impact_speed", StatFormat.M_PER_SEC, false);

	private final String key;
	private final StatFormat format;
	private final boolean materialSensitive;

	BallisticColumnMode(String key, StatFormat format, boolean materialSensitive) {
		this.key = key;
		this.format = format;
		this.materialSensitive = materialSensitive;
	}

	public String key() {
		return this.key;
	}

	public StatFormat format() {
		return this.format;
	}

	/** False when every material row prints identical values. */
	public boolean materialSensitive() {
		return this.materialSensitive;
	}

}
