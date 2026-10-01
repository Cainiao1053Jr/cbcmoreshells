package com.cainiao1053.cbcmoreshells.items.cannon_combo;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModel;
import com.simibubi.create.foundation.item.render.CustomRenderedItemModelRenderer;
import com.simibubi.create.foundation.item.render.PartialItemModelRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Renders the quickfiring breech of the combo's material as the base, with the combo icon layered on top.
 * The combo's own item model has no display transforms, so each layer is posed by its own model.
 */
public class CannonComboItemRenderer extends CustomRenderedItemModelRenderer {

	private final Map<String, ItemStack> breechCache = new HashMap<>();

	@Override
	protected void render(ItemStack stack, CustomRenderedItemModel model, PartialItemModelRenderer renderer,
						  ItemDisplayContext context, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
		BakedModel icon = CBCMSBlockPartials.CANNON_COMBO_OVERLAY.get();
		boolean leftHand = context == ItemDisplayContext.FIRST_PERSON_LEFT_HAND
				|| context == ItemDisplayContext.THIRD_PERSON_LEFT_HAND;
		boolean gui = context == ItemDisplayContext.GUI;
		ItemStack breech = getBreech(stack);

		ms.pushPose();
		// ItemRenderer already centered the model, undo that as the nested renders center it again
		ms.translate(0.5f, 0.5f, 0.5f);

		if (!breech.isEmpty()) {
			BakedModel breechModel = itemRenderer.getModel(breech, null, null, 0);
			itemRenderer.render(breech, context, leftHand, ms, buffer, light, overlay, breechModel);
		}

		if (breech.isEmpty()) {
			// No valid material, the icon is the whole item
			renderIcon(itemRenderer, stack, icon, context, leftHand, gui, ms, buffer, light, overlay);
		} else if (gui) {
			ms.translate(0.25f, -0.25f, 0.5f);
			ms.scale(0.5f, 0.5f, 0.5f);
			renderIcon(itemRenderer, stack, icon, context, leftHand, true, ms, buffer, light, overlay);
		} else if (context == ItemDisplayContext.FIXED) {
			// Item frames are viewed from -Z
			ms.translate(-0.25f, -0.25f, -0.3f);
			ms.scale(0.5f, 0.5f, 0.5f);
			renderIcon(itemRenderer, stack, icon, context, leftHand, false, ms, buffer, light, overlay);
		}
		ms.popPose();
	}

	private static void renderIcon(ItemRenderer itemRenderer, ItemStack stack, BakedModel icon, ItemDisplayContext context,
								   boolean leftHand, boolean gui, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		// The combo model is lit like a block for the breech, the flat icon needs flat lighting in GUIs
		boolean swapLighting = gui && buffer instanceof MultiBufferSource.BufferSource;
		if (swapLighting) {
			((MultiBufferSource.BufferSource) buffer).endBatch();
			Lighting.setupForFlatItems();
		}
		// The icon model is a plain baked model, so this does not come back to this renderer
		itemRenderer.render(stack, context, leftHand, ms, buffer, light, overlay, icon);
		if (swapLighting) {
			((MultiBufferSource.BufferSource) buffer).endBatch();
			Lighting.setupFor3DItems();
		}
	}

	private ItemStack getBreech(ItemStack stack) {
		if (!(stack.getItem() instanceof CannonComboItem combo)) return ItemStack.EMPTY;
		String material = CannonComboItem.getMaterial(stack);
		if (material.isEmpty()) return ItemStack.EMPTY;
		return this.breechCache.computeIfAbsent(combo.getCanonType() + material, k -> combo.getBreechStack(stack));
	}

}
