package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.box.PackageItem;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;

/** Renders the loaded package inside the barrel, using Create's model for its package style. */
public class PackageSliderBlockRenderer implements BlockEntityRenderer<PackageSliderBlockEntity> {

	public PackageSliderBlockRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public void render(PackageSliderBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer,
					   int light, int overlay) {
		PackageSliderBehavior behavior = blockEntity.cannonBehavior();
		if (behavior == null || !behavior.isLoaded()) return;

		ItemStack loaded = behavior.getLoaded();
		PartialModel model = AllPartialModels.PACKAGES.get(BuiltInRegistries.ITEM.getKey(loaded.getItem()));
		if (model == null) return;

		// Package models sit on their bottom face and span the block horizontally; centre them vertically.
		ms.pushPose();
		ms.translate(0, 0.5 - PackageItem.getHeight(loaded) / 2.0, 0);
		CachedBuffers.partial(model, blockEntity.getBlockState())
			.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));
		ms.popPose();
	}

}
