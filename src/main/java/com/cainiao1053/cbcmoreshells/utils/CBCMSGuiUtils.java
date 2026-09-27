package com.cainiao1053.cbcmoreshells.utils;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

/**
 * GUI helpers. Every {@code scale} argument is a text scale factor (1.0 = vanilla size); all positions, widths and
 * heights are in regular screen (GUI) pixels regardless of the scale.
 */
@OnlyIn(Dist.CLIENT)
public final class CBCMSGuiUtils {

	private CBCMSGuiUtils() {
	}

	// ---------------------------------------------------------------------------------------------
	// Single line
	// ---------------------------------------------------------------------------------------------

	/** Draws one scaled line with its top-left corner at (x, y). Returns the drawn width in screen pixels. */
	public static int drawString(GuiGraphics graphics, Font font, FormattedText text, int x, int y,
								 int colour, boolean shadow, float scale) {
		FormattedCharSequence line = Language.getInstance().getVisualOrder(text);
		PoseStack pose = graphics.pose();
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale(scale, scale, 1);
		graphics.drawString(font, line, 0, 0, colour, shadow);
		pose.popPose();
		return Mth.ceil(font.width(line) * scale);
	}

	public static int drawString(GuiGraphics graphics, Font font, String text, int x, int y,
								 int colour, boolean shadow, float scale) {
		return drawString(graphics, font, FormattedText.of(text), x, y, colour, shadow, scale);
	}

	/** Draws one scaled line horizontally centred on {@code centreX}. Returns the drawn width in screen pixels. */
	public static int drawCenteredString(GuiGraphics graphics, Font font, FormattedText text, int centreX, int y,
										 int colour, boolean shadow, float scale) {
		int width = Mth.ceil(font.width(text) * scale);
		return drawString(graphics, font, text, centreX - width / 2, y, colour, shadow, scale);
	}

	public static int drawCenteredString(GuiGraphics graphics, Font font, String text, int centreX, int y,
										 int colour, boolean shadow, float scale) {
		return drawCenteredString(graphics, font, FormattedText.of(text), centreX, y, colour, shadow, scale);
	}

	// ---------------------------------------------------------------------------------------------
	// Wrapped within a box
	// ---------------------------------------------------------------------------------------------

	/**
	 * Draws scaled text starting at (x, y), wrapped to {@code maxWidth}. Returns the drawn height in screen pixels.
	 */
	public static int drawString(GuiGraphics graphics, Font font, FormattedText text, int x, int y, int maxWidth,
								 int colour, boolean shadow, float scale) {
		return drawWrapped(graphics, font, text, x, y, maxWidth, Integer.MAX_VALUE, colour, shadow, scale, false);
	}

	public static int drawString(GuiGraphics graphics, Font font, String text, int x, int y, int maxWidth,
								 int colour, boolean shadow, float scale) {
		return drawString(graphics, font, FormattedText.of(text), x, y, maxWidth, colour, shadow, scale);
	}

	/**
	 * Draws scaled text inside the box (x, y, maxWidth, maxHeight), wrapped to the box width. Lines that would not
	 * fully fit in the box height are dropped. Returns the drawn height in screen pixels.
	 */
	public static int drawString(GuiGraphics graphics, Font font, FormattedText text, int x, int y, int maxWidth,
								 int maxHeight, int colour, boolean shadow, float scale) {
		return drawWrapped(graphics, font, text, x, y, maxWidth, maxHeight, colour, shadow, scale, false);
	}

	public static int drawString(GuiGraphics graphics, Font font, String text, int x, int y, int maxWidth,
								 int maxHeight, int colour, boolean shadow, float scale) {
		return drawString(graphics, font, FormattedText.of(text), x, y, maxWidth, maxHeight, colour, shadow, scale);
	}

	/** Same as the wrapped {@code drawString}, but each line is centred horizontally within {@code maxWidth}. */
	public static int drawCenteredString(GuiGraphics graphics, Font font, FormattedText text, int x, int y,
										 int maxWidth, int colour, boolean shadow, float scale) {
		return drawWrapped(graphics, font, text, x, y, maxWidth, Integer.MAX_VALUE, colour, shadow, scale, true);
	}

	public static int drawCenteredString(GuiGraphics graphics, Font font, String text, int x, int y,
										 int maxWidth, int colour, boolean shadow, float scale) {
		return drawCenteredString(graphics, font, FormattedText.of(text), x, y, maxWidth, colour, shadow, scale);
	}

	/** Same as the boxed {@code drawString}, but each line is centred horizontally within {@code maxWidth}. */
	public static int drawCenteredString(GuiGraphics graphics, Font font, FormattedText text, int x, int y,
										 int maxWidth, int maxHeight, int colour, boolean shadow, float scale) {
		return drawWrapped(graphics, font, text, x, y, maxWidth, maxHeight, colour, shadow, scale, true);
	}

	public static int drawCenteredString(GuiGraphics graphics, Font font, String text, int x, int y,
										 int maxWidth, int maxHeight, int colour, boolean shadow, float scale) {
		return drawCenteredString(graphics, font, FormattedText.of(text), x, y, maxWidth, maxHeight, colour, shadow,
			scale);
	}

	// ---------------------------------------------------------------------------------------------
	// Measuring
	// ---------------------------------------------------------------------------------------------

	/** Splits text into the lines the wrapped draw methods would produce for {@code maxWidth} at {@code scale}. */
	public static List<FormattedCharSequence> wrapLines(Font font, FormattedText text, int maxWidth, float scale) {
		return font.split(text, Math.max(1, (int) (maxWidth / scale)));
	}

	/** Height in screen pixels the wrapped draw methods would use, ignoring any {@code maxHeight}. */
	public static int wrappedHeight(Font font, FormattedText text, int maxWidth, float scale) {
		return Mth.ceil(wrapLines(font, text, maxWidth, scale).size() * font.lineHeight * scale);
	}

	// ---------------------------------------------------------------------------------------------
	// Internals
	// ---------------------------------------------------------------------------------------------

	private static int drawWrapped(GuiGraphics graphics, Font font, FormattedText text, int x, int y, int maxWidth,
								   int maxHeight, int colour, boolean shadow, float scale, boolean centred) {
		int wrapWidth = Math.max(1, (int) (maxWidth / scale));
		List<FormattedCharSequence> lines = font.split(text, wrapWidth);
		float lineHeight = font.lineHeight * scale;
		int lineCount = Math.min(lines.size(), (int) (maxHeight / lineHeight));
		if (lineCount <= 0) return 0;

		PoseStack pose = graphics.pose();
		pose.pushPose();
		pose.translate(x, y, 0);
		pose.scale(scale, scale, 1);
		for (int i = 0; i < lineCount; i++) {
			FormattedCharSequence line = lines.get(i);
			int lineX = centred ? (wrapWidth - font.width(line)) / 2 : 0;
			graphics.drawString(font, line, lineX, i * font.lineHeight, colour, shadow);
		}
		pose.popPose();
		return Mth.ceil(lineCount * lineHeight);
	}

}
