package com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Vector3f;

/**
 * Fallback for {@link PackageSliderQuickfiringBreechVisual} when Flywheel cannot visualize, e.g. with shaders.
 * Draws the slider block the same way; does nothing while the visual is active.
 */
public class PackageSliderQuickfiringBreechBlockEntityRenderer extends SafeBlockEntityRenderer<PackageSliderQuickfiringBreechBlockEntity> {

	public PackageSliderQuickfiringBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public boolean shouldRenderOffScreen(PackageSliderQuickfiringBreechBlockEntity blockEntity) {
		// The slider can travel down the whole barrel, well outside the breech block.
		return true;
	}

	@Override
	protected void renderSafe(PackageSliderQuickfiringBreechBlockEntity be, float partialTicks, PoseStack ms,
							  MultiBufferSource buffer, int light, int overlay) {
		if (VisualizationManager.supportsVisualization(be.getLevel())) return;

		BlockState state = be.getBlockState();
		Direction facing = state.getValue(BlockStateProperties.FACING);
		Vector3f offset = facing.step().mul(PackageSliderQuickfiringBreechVisual.REST_OFFSET + be.getSliderOffset(partialTicks));

		CachedBuffers.partial(CBCMSBlockPartials.PACKAGE_SLIDER_SLIDER_BLOCK, state)
			.translate(offset.x(), offset.y() + PackageSliderQuickfiringBreechVisual.VERTICAL_OFFSET, offset.z())
			// Direction#getRotation maps up onto the given direction, about the block centre, as in the visual.
			.rotateCentered(facing.getRotation())
			.light(light)
			.renderInto(ms, buffer.getBuffer(RenderType.solid()));
	}

}
