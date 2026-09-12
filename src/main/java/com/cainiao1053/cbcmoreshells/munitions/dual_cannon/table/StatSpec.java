package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import java.util.Objects;
import java.util.function.ToDoubleFunction;

public record StatSpec<C>(String key, ToDoubleFunction<C> value, StatFormat format, int priority) {

	/** Columns the table must never drop. */
	public static final int PRIORITY_REQUIRED = 100;
	public static final int PRIORITY_HIGH = 90;
	public static final int PRIORITY_NORMAL = 80;
	public static final int PRIORITY_LOW = 60;

	public StatSpec {
		Objects.requireNonNull(key, "key");
		Objects.requireNonNull(value, "value");
		Objects.requireNonNull(format, "format");
	}

	public static <C> StatSpec<C> of(String key, ToDoubleFunction<C> value, StatFormat format) {
		return new StatSpec<>(key, value, format, PRIORITY_NORMAL);
	}

	public static <C> StatSpec<C> of(String key, ToDoubleFunction<C> value, StatFormat format, int priority) {
		return new StatSpec<>(key, value, format, priority);
	}

	public double valueOf(C context) {
		return this.value.applyAsDouble(context);
	}

	public String formatted(C context) {
		return this.format.format(this.valueOf(context));
	}

	/** True when this stat has nothing to say about the given context. */
	public boolean applies(C context) {
		return Double.isFinite(this.valueOf(context));
	}

}
