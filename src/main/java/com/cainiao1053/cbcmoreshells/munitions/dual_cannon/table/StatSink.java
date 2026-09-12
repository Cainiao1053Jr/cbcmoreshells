package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class StatSink<C> {

	private final List<StatSpec<C>> specs = new ArrayList<>();

	public void add(StatSpec<C> spec) {
		this.specs.add(spec);
	}

	public void addIf(boolean condition, StatSpec<C> spec) {
		if (condition) this.add(spec);
	}

	/** Everything declared, in declaration order. */
	public List<StatSpec<C>> specs() {
		return Collections.unmodifiableList(this.specs);
	}

	public int size() {
		return this.specs.size();
	}

	public List<StatSpec<C>> limited(int max) {
		if (max >= this.specs.size()) return this.specs();
		if (max <= 0) return List.of();

		List<StatSpec<C>> ranked = new ArrayList<>(this.specs);
		// Stable sort, so equal priorities keep declaration order and the tail gets dropped first.
		ranked.sort(Comparator.comparingInt(StatSpec<C>::priority).reversed());
		List<StatSpec<C>> keep = new ArrayList<>(ranked.subList(0, max));

		List<StatSpec<C>> ordered = new ArrayList<>(max);
		for (StatSpec<C> spec : this.specs) {
			if (keep.contains(spec)) ordered.add(spec);
		}
		return ordered;
	}

	/** Drops stats that report nothing for this context, e.g. fire chance on a solid shot. */
	public List<StatSpec<C>> applicable(C context) {
		List<StatSpec<C>> applicable = new ArrayList<>();
		for (StatSpec<C> spec : this.specs) {
			if (spec.applies(context)) applicable.add(spec);
		}
		return applicable;
	}

}
