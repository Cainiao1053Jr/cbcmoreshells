package com.cainiao1053.cbcmoreshells.client.gui;

import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonMaterialFilter;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonTableSource;

import java.util.List;
import javax.annotation.Nullable;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.ScreenOpener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

/**
 * Shell picker. Shows the dual cannon rounds as a grid of icons and opens a comparison table for
 * whichever one is chosen; the table's shape (material filter, column budget) is chosen here so it
 * applies to every shell.
 *
 * <p>Names appear only on hover, so the grid stays compact no matter how long the shell names get.
 *
 * <p>Listing shells resolves no properties — that only happens once a shell is picked.
 */
public class DualCannonCodexScreen extends AbstractSimiScreen {

	private static final int PADDING = 8;
	private static final int ICON_SIZE = 16;

	/** Icons per row. Change this to reshape the grid. */
	private static final int GRID_COLUMNS = 5;
	/** Space each icon gets, icon plus its share of the gutter. */
	private static final int CELL_SIZE = 24;
	/** Height of the title plus the settings buttons above the grid. */
	private static final int HEADER_HEIGHT = 34;

	// Ink on aged paper: the journal background is light (~#C0B7AA), so everything is dark.
	private static final int COLOUR_TITLE = 0xFF2B2118;
	private static final int COLOUR_LABEL = 0xFF6B5B4A;
	private static final int COLOUR_HOVER = 0x30000000;
	private static final int COLOUR_SELECTED = 0x60000000;

	private static final int[] COLUMN_CHOICES = {11, 13, 15};

	private DualCannonTableSource source;
	private List<Block> shells = List.of();
	private DualCannonMaterialFilter filter;
	private int gridTop;
	private int gridLeft;

	@Nullable
	private final Block lastSelection;

	public DualCannonCodexScreen() {
		this(null, null);
	}

	/** Reopened from a comparison table: keeps the settings and remembers what was being viewed. */
	public DualCannonCodexScreen(@Nullable DualCannonTableSource source, @Nullable Block lastSelection) {
		this.source = source;
		this.lastSelection = lastSelection;
		this.filter = DualCannonMaterialFilter.SINGLE_PLUS_GAPS;
	}

	@Override
	protected void init() {
		if (this.source == null) this.source = this.buildSource();
		this.shells = this.source.shells();

		int gridWidth = GRID_COLUMNS * CELL_SIZE;
		int gridHeight = Math.max(1, this.rowCount()) * CELL_SIZE;
		// Wide enough for the grid, but never narrower than the two settings buttons need.
		int width = Math.max(gridWidth, 248) + PADDING * 2;
		this.setWindowSize(width, PADDING * 2 + HEADER_HEIGHT + gridHeight);
		super.init();

		int left = this.guiLeft + PADDING;
		int top = this.guiTop + PADDING;

//		this.addRenderableWidget(Button.builder(this.filterLabel(), button -> this.cycleFilter())
//			.bounds(left, top + 12, 150, 16).build());
//		this.addRenderableWidget(Button.builder(this.columnLabel(), button -> this.cycleColumns())
//			.bounds(left + 154, top + 12, 90, 16).build());

		this.gridTop = top + HEADER_HEIGHT;
		this.gridLeft = this.guiLeft + (this.windowWidth - gridWidth) / 2;
	}

	private int rowCount() {
		return (this.shells.size() + GRID_COLUMNS - 1) / GRID_COLUMNS;
	}

	private DualCannonTableSource buildSource() {
		return new DualCannonTableSource(12, this.filter, false);
	}

	private Component filterLabel() {
		return Component.translatable("cbcmoreshells.firing_table.filter",
			I18n.get("cbcmoreshells.firing_table.filter." + this.filter.name().toLowerCase()));
	}

//	private Component columnLabel() {
//		return Component.translatable("cbcmoreshells.firing_table.columns", COLUMN_CHOICES[this.columnChoice]);
//	}

	/** Changing either setting throws the cached tables away, since both change their shape. */
	private void cycleFilter() {
		DualCannonMaterialFilter[] values = DualCannonMaterialFilter.values();
		this.filter = values[(this.filter.ordinal() + 1) % values.length];
		this.source = this.buildSource();
		this.rebuildWidgets();
	}

//	private void cycleColumns() {
//		this.columnChoice = (this.columnChoice + 1) % COLUMN_CHOICES.length;
//		this.source = this.buildSource();
//		this.rebuildWidgets();
//	}

	@Override
	protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		JournalBackground.render(graphics, this.guiLeft, this.guiTop, this.windowWidth, this.windowHeight);

		graphics.drawString(this.font, I18n.get("cbcmoreshells.firing_table.title"),
			this.guiLeft + PADDING, this.guiTop + PADDING, COLOUR_TITLE, false);

		if (this.shells.isEmpty()) {
			graphics.drawString(this.font, I18n.get("cbcmoreshells.firing_table.no_shells"),
				this.guiLeft + PADDING, this.gridTop, COLOUR_LABEL, false);
			return;
		}

		int hovered = this.cellAt(mouseX, mouseY);
		for (int i = 0; i < this.shells.size(); i++) {
			Block shell = this.shells.get(i);
			int x = this.cellX(i);
			int y = this.cellY(i);

			if (i == hovered) {
				graphics.fill(x, y, x + CELL_SIZE, y + CELL_SIZE, COLOUR_HOVER);
			} else if (shell == this.lastSelection) {
				graphics.fill(x, y, x + CELL_SIZE, y + CELL_SIZE, COLOUR_SELECTED);
			}

			int inset = (CELL_SIZE - ICON_SIZE) / 2;
			graphics.renderItem(new ItemStack(shell), x + inset, y + inset);
		}
	}

	/**
	 * Names live here rather than in {@link #renderWindow} so the tooltip lands on top of the
	 * buttons and icons instead of underneath them.
	 */
	@Override
	protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		super.renderWindowForeground(graphics, mouseX, mouseY, partialTicks);

		int hovered = this.cellAt(mouseX, mouseY);
		if (hovered < 0) return;
		graphics.renderTooltip(this.font, this.shells.get(hovered).getName(), mouseX, mouseY);
	}

	private int cellX(int index) {
		return this.gridLeft + (index % GRID_COLUMNS) * CELL_SIZE;
	}

	private int cellY(int index) {
		return this.gridTop + (index / GRID_COLUMNS) * CELL_SIZE;
	}

	/** Index of the shell under the cursor, or -1. */
	private int cellAt(double mouseX, double mouseY) {
		int column = (int) Math.floor((mouseX - this.gridLeft) / CELL_SIZE);
		int row = (int) Math.floor((mouseY - this.gridTop) / CELL_SIZE);
		if (mouseX < this.gridLeft || column < 0 || column >= GRID_COLUMNS) return -1;
		if (mouseY < this.gridTop || row < 0 || row >= this.rowCount()) return -1;

		int index = row * GRID_COLUMNS + column;
		return index < this.shells.size() ? index : -1;
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		if (super.mouseClicked(mouseX, mouseY, button)) return true;
		if (button != 0) return false;

		int index = this.cellAt(mouseX, mouseY);
		if (index < 0) return false;
		ScreenOpener.open(new DualCannonCompareScreen(this.source, this.shells.get(index)));
		return true;
	}

	/** So a table reopened from the picker starts on the shell that was being viewed. */
	@Nullable
	public Block lastSelection() {
		return this.lastSelection;
	}

}
