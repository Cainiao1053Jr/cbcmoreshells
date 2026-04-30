package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech;

import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlock;
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

import static com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBaseBlock.CEILING;

public class ProjectileRackQuickfiringBreechBlockEntityRenderer extends SafeBlockEntityRenderer<ProjectileRackQuickfiringBreechBlockEntity> {

	public ProjectileRackQuickfiringBreechBlockEntityRenderer(BlockEntityRendererProvider.Context context) {}

	@Override
	public boolean shouldRenderOffScreen(ProjectileRackQuickfiringBreechBlockEntity blockEntity) {
		return true;
	}

	@Override
	protected void renderSafe(ProjectileRackQuickfiringBreechBlockEntity te, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		BlockState blockState = te.getBlockState();

		if (VisualizationManager.supportsVisualization(te.getLevel())) return;

		Direction facing = blockState.getValue(BlockStateProperties.FACING);
		Direction.Axis axis = getRotationAxis(blockState);
		Direction blockRotation = facing.getCounterClockWise(axis);
		if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;
		Direction blockTranslation = facing.getOpposite();

		// Initial rotation — mirrors the Visual's rotation(q) call per facing.
		// partialFacing absorbs the rotateXDegrees(-90) correction, so only the
		// explicit rotation(q) set in the Visual needs to be replicated here.
		Quaternionf qrot;
		if (facing == Direction.UP) {
			qrot = Axis.YN.rotationDegrees(90f);
		} else if (facing == Direction.DOWN) {
			qrot = Axis.YN.rotationDegrees(-90f);
		} else if (facing == Direction.NORTH) {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(180f);
		} else if (facing == Direction.WEST) {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(270f);
		} else if (facing == Direction.EAST) {
			qrot = Axis.of(blockRotation.step()).rotationDegrees(90f);
		} else { // SOUTH
			qrot = Axis.of(blockRotation.step()).rotationDegrees(0f);
		}

		// Static Y offset for non-ceiling horizontal mounts (matches Visual's offsetI)
		float offsetY = (!blockState.getValue(CEILING) && facing != Direction.UP && facing != Direction.DOWN) ? -0.875f : 0f;

		VertexConsumer vcons = buffer.getBuffer(RenderType.solid());
		ms.pushPose();

		float progress = te.getOpenProgress(partialTicks);
		float renderedBreechblockOffset = progress / 16.0f * 7.0f;
		Vector3f normal = blockTranslation.step();
		normal.mul(renderedBreechblockOffset);

		CachedBuffers.partialFacing(getPartialModelForState(blockState), blockState, blockRotation)
				.translate(normal.x(), normal.y() + offsetY, normal.z())
				.rotateCentered(qrot)
				.light(light)
				.renderInto(ms, vcons);

		ms.popPose();
	}

	private static PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof ProjectileRackBlock cBlock
				? CBCMSBlockPartials.projectileLockBlockFor(cBlock.getCannonMaterial())
				: CBCMSBlockPartials.STEEL_PROJECTILE_RACK_SLIDING_BREECHBLOCK;
	}

	private static Direction.Axis getRotationAxis(BlockState state) {
		boolean flag = state.getValue(ProjectileRackQuickfiringBreechBlock.AXIS);
		return switch (state.getValue(ProjectileRackQuickfiringBreechBlock.FACING).getAxis()) {
			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
		};
	}

}
