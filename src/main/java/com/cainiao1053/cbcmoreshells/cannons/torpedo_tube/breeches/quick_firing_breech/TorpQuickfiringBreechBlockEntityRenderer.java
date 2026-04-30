package com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech;

import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.TorpedoTubeBlock;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.blockEntity.renderer.SafeBlockEntityRenderer;
import dev.engine_room.flywheel.api.visualization.VisualizationManager;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TorpQuickfiringBreechBlockEntityRenderer extends SafeBlockEntityRenderer<TorpQuickfiringBreechBlockEntity> {

	public TorpQuickfiringBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
	}

	@Override
	public boolean shouldRenderOffScreen(TorpQuickfiringBreechBlockEntity blockEntity) {
		return true;
	}

	@Override
	protected void renderSafe(TorpQuickfiringBreechBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockState = te.getBlockState();

		if (VisualizationManager.supportsVisualization(te.getLevel())) return;

		Direction facing = blockState.getValue(BlockStateProperties.FACING);
		Direction.Axis axis = getRotationAxis(blockState);
		Direction blockRotation = facing.getCounterClockWise(axis);
		if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;
		boolean alongFirst = blockState.getValue(TorpQuickfiringBreechBlock.AXIS);

		Quaternionf qrot;
		if (facing.getAxis().isHorizontal() && !alongFirst) {
			Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
			qrot = Axis.of(rotDir.step()).rotationDegrees(90f);
		} else if (facing.getAxis() == Direction.Axis.X && alongFirst) {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(90f);
		} else {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(0f);
		}

		VertexConsumer vcons = buffer.getBuffer(RenderType.solid());

		// Breechblock
		ms.pushPose();
		float progress = te.getOpenProgress(partialTicks);
		float renderedBreechblockOffset = progress / 16.0f * 13.0f;
		Vector3f normal = blockRotation.step();
		normal.mul(renderedBreechblockOffset);

		CachedBuffers.partialFacing(getPartialModelForState(blockState), blockState, blockRotation)
				.translate(normal.x(), normal.y(), normal.z())
				.rotateCentered(qrot)
				.light(light)
				.renderInto(ms, vcons);
		ms.popPose();

		// Lever
		ms.pushPose();
		float angle = progress * 90f;
		Direction dir = facing.getCounterClockWise(blockRotation.getAxis());
		Vector3f normal1 = dir.step();
		Axis axis1 = Axis.of(normal1);

		CachedBuffers.partialFacing(CBCMSBlockPartials.QUICKFIRING_BREECH_LEVER, blockState, dir)
				.rotateCentered(axis1.rotationDegrees(angle))
				.translate(normal1)
				.light(light)
				.renderInto(ms, vcons);
		ms.popPose();
	}

	private static PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof TorpedoTubeBlock cBlock
				? CBCMSBlockPartials.breechblockFor(cBlock.getCannonMaterial())
				: CBCMSBlockPartials.STEEL_TORPEDO_SLIDING_BREECHBLOCK;
	}

	private static Direction.Axis getRotationAxis(BlockState state) {
		boolean flag = state.getValue(TorpQuickfiringBreechBlock.AXIS);
		return switch (state.getValue(TorpQuickfiringBreechBlock.FACING).getAxis()) {
			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
		};
	}

}
