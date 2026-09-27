package com.cainiao1053.cbcmoreshells.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

public final class Paginator<T> {

	private final List<T> source = new ArrayList<>();
	private List<T> view = List.of();
	private boolean viewDirty = true;

	private Predicate<? super T> filter;
	private Comparator<? super T> comparator;
	private int pageSize;
	private int pageIndex;

	public Paginator(int pageSize) {
		this.pageSize = Math.max(1, pageSize);
	}

	// ---------------------------------------------------------------------------------------------
	// Inputs
	// ---------------------------------------------------------------------------------------------

	/** Replaces the backing list and returns to the first page. */
	public void setSource(List<T> items) {
		this.source.clear();
		this.source.addAll(items);
		this.viewDirty = true;
		this.pageIndex = 0;
	}

	/** Null clears the filter. Keeps the current first row visible where it still exists. */
	public void setFilter(Predicate<? super T> filter) {
		this.filter = filter;
		this.repaginate();
	}

	/** Null clears the sort, falling back to source order. */
	public void setComparator(Comparator<? super T> comparator) {
		this.comparator = comparator;
		this.repaginate();
	}

	/**
	 * Resizes the window, keeping the row that was at the top of the current page on screen. Without
	 * that the content jumps around whenever the window is resized.
	 */
	public void setPageSize(int pageSize) {
		int normalized = Math.max(1, pageSize);
		if (normalized == this.pageSize) return;
		int anchor = this.pageIndex * this.pageSize;
		this.pageSize = normalized;
		this.pageIndex = anchor / normalized;
		this.clampPage();
	}

	// ---------------------------------------------------------------------------------------------
	// Reads
	// ---------------------------------------------------------------------------------------------

	/** The current page's elements, filtered and sorted. Shorter than the page size on the last page. */
	public List<T> page() {
		List<T> view = this.view();
		if (view.isEmpty()) return List.of();
		int from = this.pageIndex * this.pageSize;
		int to = Math.min(from + this.pageSize, view.size());
		return from >= to ? List.of() : Collections.unmodifiableList(view.subList(from, to));
	}

	/** Zero based. */
	public int pageIndex() {
		return this.pageIndex;
	}

	/** At least 1, so an empty list still has a page zero and callers need no special case. */
	public int pageCount() {
		int total = this.view().size();
		return total == 0 ? 1 : (total + this.pageSize - 1) / this.pageSize;
	}

	/** Elements that survive the filter, across all pages. */
	public int totalCount() {
		return this.view().size();
	}

	public int pageSize() {
		return this.pageSize;
	}

	public boolean isEmpty() {
		return this.view().isEmpty();
	}

	/** Index within the filtered, sorted view of a row on the current page. */
	public int absoluteIndex(int rowInPage) {
		return this.pageIndex * this.pageSize + rowInPage;
	}

	// ---------------------------------------------------------------------------------------------
	// Navigation
	// ---------------------------------------------------------------------------------------------

	/** @return whether the page actually changed */
	public boolean next() {
		return this.goTo(this.pageIndex + 1);
	}

	/** @return whether the page actually changed */
	public boolean prev() {
		return this.goTo(this.pageIndex - 1);
	}

	public boolean first() {
		return this.goTo(0);
	}

	public boolean last() {
		return this.goTo(this.pageCount() - 1);
	}

	/** Clamps out of range indices rather than rejecting them. @return whether the page changed */
	public boolean goTo(int pageIndex) {
		int clamped = Math.max(0, Math.min(pageIndex, this.pageCount() - 1));
		if (clamped == this.pageIndex) return false;
		this.pageIndex = clamped;
		return true;
	}

	/**
	 * Jumps to the page holding {@code item}, for following a selection made elsewhere.
	 *
	 * @return false if the item is not in the current view, leaving the page untouched
	 */
	public boolean revealItem(T item) {
		int index = this.view().indexOf(item);
		if (index < 0) return false;
		this.goTo(index / this.pageSize);
		return true;
	}

	// ---------------------------------------------------------------------------------------------
	// Internals
	// ---------------------------------------------------------------------------------------------

	private List<T> view() {
		if (this.viewDirty) {
			List<T> built = new ArrayList<>(this.source.size());
			for (T item : this.source) {
				if (this.filter == null || this.filter.test(item)) built.add(item);
			}
			// Stable, so equal elements keep source order.
			if (this.comparator != null) built.sort(this.comparator);
			this.view = built;
			this.viewDirty = false;
		}
		return this.view;
	}

	/** Rebuilds the view and pulls the page index back into range. */
	private void repaginate() {
		this.viewDirty = true;
		this.clampPage();
	}

	private void clampPage() {
		this.pageIndex = Math.max(0, Math.min(this.pageIndex, this.pageCount() - 1));
	}

}
