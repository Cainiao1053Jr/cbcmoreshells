package com.cainiao1053.cbcmoreshells.client.gui;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public final class JournalBackground {

	public static final ResourceLocation TEXTURE =
		ResourceLocation.fromNamespaceAndPath("cbcmoreshells", "textures/gui/journal_background.png");

	private static final int TEXTURE_WIDTH = 450;
	private static final int TEXTURE_HEIGHT = 300;

	public static final int MARGIN = 6;

	private JournalBackground() {}

	public static void render(GuiGraphics graphics, int x, int y, int width, int height) {
		graphics.blit(TEXTURE, x - MARGIN, y - MARGIN, width + MARGIN * 2, height + MARGIN * 2,
			0.0F, 0.0F, TEXTURE_WIDTH, TEXTURE_HEIGHT, TEXTURE_WIDTH, TEXTURE_HEIGHT);
	}

}
