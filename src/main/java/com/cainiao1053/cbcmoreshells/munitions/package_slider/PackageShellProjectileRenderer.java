package com.cainiao1053.cbcmoreshells.munitions.package_slider;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.utils.CBCUtils;

/** Same as CBC's BigCannonProjectileRenderer: the rendered block state, pointed along the shell's orientation. */
public class PackageShellProjectileRenderer<T extends AbstractPackageShellProjectile> extends EntityRenderer<T> {

	public PackageShellProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5f;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers,
					   int packedLight) {
		BlockState blockState = entity.getRenderedBlockState();
		if (blockState.getRenderShape() == RenderShape.MODEL) {
			Vec3 vel = entity.getOrientation();
			if (vel.lengthSqr() < 1e-4d)
				vel = new Vec3(0, -1, 0);

			poseStack.pushPose();
			if (vel.horizontalDistanceSqr() > 1e-4d && Math.abs(vel.y) > 1e-2d) {
				Vec3 horizontal = new Vec3(vel.x, 0, vel.z).normalize();
				poseStack.mulPose(CBCUtils.mat4x4fFacing(vel.normalize().reverse(), horizontal));
				poseStack.mulPose(CBCUtils.mat4x4fFacing(horizontal));
			} else {
				poseStack.mulPose(CBCUtils.mat4x4fFacing(vel.normalize()));
			}
			poseStack.translate(-0.5, -0.5, -0.5);
			Minecraft.getInstance().getBlockRenderer().renderSingleBlock(blockState, poseStack, buffers, packedLight,
				OverlayTexture.NO_OVERLAY);
			poseStack.popPose();
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return null;
	}

}
