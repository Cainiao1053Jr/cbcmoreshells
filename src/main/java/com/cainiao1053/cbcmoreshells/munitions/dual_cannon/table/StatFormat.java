package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import java.util.Locale;

public enum StatFormat {

	/** As-is, no decimals. */
	INTEGER("%.0f", 1.0),
	/** As-is, one decimal. */
	PLAIN1("%.1f", 1.0),
	/** As-is, two decimals. */
	PLAIN2("%.2f", 1.0),
	/** As-is, three decimals — drag and gravity are small numbers. */
	PLAIN3("%.3f", 1.0),
	/** Blocks per tick in, metres per second out. */
	M_PER_SEC("%.0f", 20.0),
	/** Ticks in, seconds out. */
	SECONDS("%.2f", 1.0 / 20.0),
	/** Ticks in, ticks out. */
	TICKS("%.0f", 1.0),
	/** A 0..1 fraction in, a percentage out. */
	PERCENT("%.0f%%", 100.0),
	/** Radians in, degrees out. */
	DEGREES("%.1f", 180.0 / Math.PI);

	/** Shown instead of a number when a stat does not apply to this shell. */
	public static final String NOT_APPLICABLE = "-";

	private final String pattern;
	private final double scale;

	StatFormat(String pattern, double scale) {
		this.pattern = pattern;
		this.scale = scale;
	}

	/** Value in display units, before formatting. Useful for sorting a column. */
	public double scaled(double raw) {
		return raw * this.scale;
	}

	public String format(double raw) {
		if (!Double.isFinite(raw)) return NOT_APPLICABLE;
		return String.format(Locale.ROOT, this.pattern, this.scaled(raw));
	}

}
