package com.cainiao1053.cbcmoreshells.client.gui;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.BallisticColumnMode;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.BallisticPoint;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.BallisticSkeleton;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonLoadout;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonMaterialFilter;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonShellContext;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonTable;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.DualCannonTableSource;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.MaterialRow;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.StatFormat;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table.StatSpec;
import com.cainiao1053.cbcmoreshells.utils.Paginator;

import java.util.ArrayList;
import java.util.List;
import net.createmod.catnip.gui.AbstractSimiScreen;
import net.createmod.catnip.gui.ScreenOpener;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.block.Block;

/**
 * One shell, every barrel material side by side.
 *
 * <p>Three stacked blocks: the shell's own stats, the ballistic skeleton (shared by all materials),
 * and the per-material rows. The skeleton sits above the rows rather than inside them because
 * elevation, flight time and impact speed do not vary with the barrel — repeating them on every row
 * would be the same numbers twenty times over.
 */
public class DualCannonCompareScreen extends AbstractSimiScreen {

	private static final int PADDING = 8;
	private static final int NAME_COLUMN = 92;
	private static final int STAT_COLUMN = 48;
	private static final int BALLISTIC_COLUMN = 38;
	private static final int BLOCK_GAP = 8;
	private static final int ROW_HEIGHT = 11;
	private static final int MAX_ROWS_PER_PAGE = 12;

	// Ink on aged paper: the journal background is light (~#C0B7AA), so everything is dark.
	private static final int COLOUR_TITLE = 0xFF2B2118;
	private static final int COLOUR_LABEL = 0xFF6B5B4A;
	private static final int COLOUR_VALUE = 0xFF33291E;
	private static final int COLOUR_SKELETON = 0xFF685015;
	private static final int COLOUR_GAP_MATERIAL = 0xFF8A5A12;
	private static final int COLOUR_UNREACHABLE = 0xFF9A9086;
	private static final int COLOUR_RULE = 0x40000000;

	private final DualCannonTableSource source;
	private final Block shell;
	private final Block previousSelection;
	private final Paginator<DualCannonMaterial> paginator = new Paginator<>(MAX_ROWS_PER_PAGE);

	private DualCannonTable table;
	private BallisticColumnMode mode;
	private PageControls pageControls;
	private List<MaterialRow> visibleRows = List.of();

	private int statsBlockTop;
	private int skeletonBlockTop;
	private int rowsBlockTop;

	public DualCannonCompareScreen(DualCannonTableSource source, Block shell) {
		this.source = source;
		this.shell = shell;
		this.previousSelection = shell;
	}

	@Override
	protected void init() {
		this.table = this.source.table(this.shell);
		if (this.table == null) {
			// Nothing to show; bounce straight back rather than rendering an empty frame.
			this.setWindowSize(200, 60);
			super.init();
			return;
		}
		if (this.mode == null || !this.table.modes().contains(this.mode)) this.mode = this.table.defaultMode();

		this.paginator.setSource(new ArrayList<>(this.table.materials()));
		this.setWindowSize(this.computeWidth(), this.computeHeight());
		super.init();

		int contentLeft = this.guiLeft + PADDING;
		int contentRight = this.guiLeft + this.windowWidth - PADDING;

		this.addRenderableWidget(Button.builder(Component.translatable("gui.back"), button -> this.goBack())
			.bounds(contentLeft, this.guiTop + PADDING, 40, 16).build());

		if (this.table.modes().size() > 1) {
			this.addRenderableWidget(Button.builder(this.modeLabel(), button -> this.cycleMode())
				.bounds(contentRight - 110, this.guiTop + PADDING, 110, 16).build());
		}

		this.pageControls = new PageControls(this.paginator, this::refreshRows);
		this.pageControls.setPosition(contentRight - PageControls.width(),
			this.guiTop + this.windowHeight - PADDING - PageControls.height());
		for (Button button : this.pageControls.buttons()) this.addRenderableWidget(button);

		this.refreshRows();
	}

	private void goBack() {
		ScreenOpener.open(new DualCannonCodexScreen(this.source, this.previousSelection));
	}

	private Component modeLabel() {
		return Component.translatable("cbcmoreshells.firing_table.showing", I18n.get(this.mode.key()));
	}

	private void cycleMode() {
		this.mode = this.table.nextMode(this.mode);
		this.rebuildWidgets();
	}

	private void refreshRows() {
		this.visibleRows = this.source.rows(this.shell, this.paginator.page());
		if (this.pageControls != null) this.pageControls.refresh();
	}

	// -------------------------------------------------------------------------------------------
	// Layout
	// -------------------------------------------------------------------------------------------

	private int computeWidth() {
		int table = NAME_COLUMN + this.table.materialStats().size() * STAT_COLUMN
			+ BLOCK_GAP + this.table.ballisticColumns() * BALLISTIC_COLUMN;
		return Math.max(260, table + PADDING * 2);
	}

	private int computeHeight() {
		int statLines = (this.table.applicableShellStats().size() + 1) / 2;
		int skeletonLines = this.table.hasBallistics() ? 4 : 0;
		int rows = Math.min(this.table.materials().size(), MAX_ROWS_PER_PAGE);
		return PADDING * 2 + 16 + BLOCK_GAP            // header row
			+ statLines * ROW_HEIGHT + BLOCK_GAP
			+ skeletonLines * ROW_HEIGHT + BLOCK_GAP
			+ (rows + 1) * ROW_HEIGHT + BLOCK_GAP      // header row plus material rows
			+ PageControls.height();
	}

	private int ballisticColumnX(int column) {
		int tableLeft = this.guiLeft + PADDING;
		return tableLeft + NAME_COLUMN + this.table.materialStats().size() * STAT_COLUMN + BLOCK_GAP
			+ column * BALLISTIC_COLUMN;
	}

	// -------------------------------------------------------------------------------------------
	// Rendering
	// -------------------------------------------------------------------------------------------

	@Override
	protected void renderWindow(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
		JournalBackground.render(graphics, this.guiLeft, this.guiTop, this.windowWidth, this.windowHeight);

		if (this.table == null) {
			graphics.drawString(this.font, I18n.get("cbcmoreshells.firing_table.unavailable"),
				this.guiLeft + PADDING, this.guiTop + PADDING, COLOUR_LABEL, false);
			return;
		}

		int left = this.guiLeft + PADDING;
		graphics.drawString(this.font, this.shell.getName(), left + 46, this.guiTop + PADDING + 4,
			COLOUR_TITLE, false);

		int y = this.guiTop + PADDING + 16 + BLOCK_GAP;
		y = this.renderShellStats(graphics, left, y);
		y += BLOCK_GAP;
		y = this.renderSkeleton(graphics, left, y);
		y += BLOCK_GAP;
		this.renderMaterialRows(graphics, left, y);

		this.pageControls.render(graphics, this.font, COLOUR_VALUE);
	}

	/** Block A: the shell's fixed stats, two per line. */
	private int renderShellStats(GuiGraphics graphics, int left, int top) {
		this.statsBlockTop = top;
		List<StatSpec<DualCannonShellContext>> specs = this.table.applicableShellStats();
		DualCannonShellContext context = this.table.shell();
		int halfWidth = (this.windowWidth - PADDING * 2) / 2;
		int y = top;

		for (int i = 0; i < specs.size(); i += 1) {
			this.renderStatPair(graphics, left, y, specs.get(i), context);
//			if (i + 1 < specs.size()) {
//				this.renderStatPair(graphics, left + halfWidth, y, specs.get(i + 1), context);
//			}
			y += ROW_HEIGHT;
		}
		return y;
	}

	private void renderStatPair(GuiGraphics graphics, int x, int y, StatSpec<DualCannonShellContext> spec,
								DualCannonShellContext context) {
		String label = I18n.get(spec.key());
		graphics.drawString(this.font, label, x, y, COLOUR_LABEL, false);
		graphics.drawString(this.font, spec.formatted(context), x + this.font.width(label) + 6, y,
			COLOUR_VALUE, false);
	}

	/** Block B: distances and the two values that do not vary with the barrel. */
	private int renderSkeleton(GuiGraphics graphics, int left, int top) {
		this.skeletonBlockTop = top;
		if (!this.table.hasBallistics()) {
			graphics.drawString(this.font, I18n.get("cbcmoreshells.firing_table.no_ballistics"), left, top,
				COLOUR_LABEL, false);
			return top + ROW_HEIGHT;
		}

		BallisticSkeleton skeleton = this.table.skeleton();
		int y = top;
		this.renderRule(graphics, y - 2);

		graphics.drawString(this.font, I18n.get("cbcmoreshells.firing_table.distance"), left, y, COLOUR_LABEL, false);
		for (int i = 0; i < skeleton.columns(); i++) {
			this.drawCell(graphics, this.ballisticColumnX(i), y, StatFormat.PLAIN1.format(skeleton.point(i).range()),
				COLOUR_SKELETON);
		}
		y += ROW_HEIGHT;

		graphics.drawString(this.font, I18n.get(BallisticColumnMode.FLIGHT_TIME.key()), left, y, COLOUR_LABEL, false);
		for (int i = 0; i < skeleton.columns(); i++) {
			BallisticPoint point = skeleton.point(i);
			this.drawCell(graphics, this.ballisticColumnX(i), y,
				BallisticColumnMode.FLIGHT_TIME.format().format(point.flightTicks()), COLOUR_SKELETON);
		}
		y += ROW_HEIGHT;

		graphics.drawString(this.font, I18n.get(BallisticColumnMode.IMPACT_SPEED.key()), left, y, COLOUR_LABEL, false);
		for (int i = 0; i < skeleton.columns(); i++) {
			BallisticPoint point = skeleton.point(i);
			this.drawCell(graphics, this.ballisticColumnX(i), y,
				BallisticColumnMode.IMPACT_SPEED.format().format(point.impactSpeed()), COLOUR_SKELETON);
		}
		return y + ROW_HEIGHT;
	}

	/** Block C: one row per barrel material. */
	private void renderMaterialRows(GuiGraphics graphics, int left, int top) {
		this.rowsBlockTop = top;
		this.renderRule(graphics, top - 2);

		List<StatSpec<DualCannonLoadout>> statSpecs = this.table.materialStats();
		int y = top;

		graphics.drawString(this.font, I18n.get("cbcmoreshells.firing_table.material"), left, y, COLOUR_LABEL, false);
		for (int i = 0; i < statSpecs.size(); i++) {
			this.drawCell(graphics, left + NAME_COLUMN + i * STAT_COLUMN, y,
				I18n.get(statSpecs.get(i).key()), COLOUR_LABEL);
		}
		if (this.table.hasBallistics()) {
			int headerX = this.ballisticColumnX(0);
			graphics.drawString(this.font, I18n.get(this.mode.key()), headerX, y, COLOUR_LABEL, false);
		}
		y += ROW_HEIGHT;

		for (MaterialRow row : this.visibleRows) {
			String name = I18n.get(DualCannonMaterialFilter.translationKey(row.loadout().material()));
			if (row.missingSingleVariant()){}
			graphics.drawString(this.font, this.font.plainSubstrByWidth(name, NAME_COLUMN - 4), left, y,
				COLOUR_VALUE, false);

			for (int i = 0; i < statSpecs.size(); i++) {
				this.drawCell(graphics, left + NAME_COLUMN + i * STAT_COLUMN, y,
					row.formatStat(statSpecs, i), COLOUR_VALUE);
			}
			if (this.table.hasBallistics()) {
				BallisticSkeleton skeleton = this.table.skeleton();
				for (int i = 0; i < skeleton.columns(); i++) {
					String text = row.formatBallistic(this.mode, skeleton, i);
					boolean reachable = row.reachable()[i];
					this.drawCell(graphics, this.ballisticColumnX(i), y, text,
						reachable ? COLOUR_VALUE : COLOUR_UNREACHABLE);
				}
			}
			y += ROW_HEIGHT;
		}
	}

	/** Numbers read better right aligned inside their column. */
	private void drawCell(GuiGraphics graphics, int columnLeft, int y, String text, int colour) {
		int width = this.font.width(text);
		graphics.drawString(this.font, text, columnLeft + BALLISTIC_COLUMN - 4 - width, y, colour, false);
	}

	private void renderRule(GuiGraphics graphics, int y) {
		graphics.fill(this.guiLeft + PADDING, y, this.guiLeft + this.windowWidth - PADDING, y + 1, COLOUR_RULE);
	}

}
