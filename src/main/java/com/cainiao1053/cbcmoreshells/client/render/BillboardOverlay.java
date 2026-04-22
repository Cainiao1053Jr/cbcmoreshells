package com.cainiao1053.cbcmoreshells.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import org.joml.Matrix3f;
import org.joml.Matrix4f;


public class BillboardOverlay implements ExtraEntityLayer {

	private final ResourceLocation texture;
	private final float scale;

	public BillboardOverlay(ResourceLocation texture, float scale) {
		this.texture = texture;
		this.scale = scale;
	}

//	@Override
//	public void render(PoseStack poseStack, MultiBufferSource buffer, Entity entity, float partialTicks) {
//		poseStack.pushPose();
//
//		poseStack.translate(0, entity.getBbHeight() + 0.5, 0);
//
//		Quaternionf cameraRotation = Minecraft.getInstance()
//				.gameRenderer.getMainCamera().rotation();
//		poseStack.mulPose(cameraRotation);
//
//		poseStack.scale(scale, scale, scale);
//
////		Matrix4f matrix = poseStack.last().pose();
////		VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(texture));
////		float s = 0.5f;
////
////		builder.vertex(matrix, -s, -s, 0).uv(0, 1).color(255, 255, 255, 255).endVertex();
////		builder.vertex(matrix,  s, -s, 0).uv(1, 1).color(255, 255, 255, 255).endVertex();
////		builder.vertex(matrix,  s,  s, 0).uv(1, 0).color(255, 255, 255, 255).endVertex();
////		builder.vertex(matrix, -s,  s, 0).uv(0, 0).color(255, 255, 255, 255).endVertex();
//		Matrix4f matrix = poseStack.last().pose();
//		VertexConsumer builder = buffer.getBuffer(RenderType.entityTranslucent(texture));
//		float s = 0.5f;
//
//		int overlay = OverlayTexture.NO_OVERLAY;
//		int light = LightTexture.FULL_BRIGHT;
//
//		builder.vertex(matrix, -s, -s, 0).uv(0, 1).color(255,255,255,255).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), 0, 0, 1).endVertex();
//		builder.vertex(matrix,  s, -s, 0).uv(1, 1).color(255,255,255,255).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), 0, 0, 1).endVertex();
//		builder.vertex(matrix,  s,  s, 0).uv(1, 0).color(255,255,255,255).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), 0, 0, 1).endVertex();
//		builder.vertex(matrix, -s,  s, 0).uv(0, 0).color(255,255,255,255).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), 0, 0, 1).endVertex();
//
//		poseStack.popPose();
//	}
@Override
public void render(PoseStack poseStack, MultiBufferSource buffer, Entity entity, float partialTicks) {
	RenderType renderType = RenderType.entityCutoutNoCull(texture);



	poseStack.pushPose();
	//poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
	poseStack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(90));
	poseStack.scale(1.5f, 1.5f, 1.5f);

	PoseStack.Pose lastPose = poseStack.last();
	Matrix4f pose = lastPose.pose();
	Matrix3f normal = lastPose.normal();
	VertexConsumer builder = buffer.getBuffer(renderType);

	vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, -0.5f, -0.5f, 0, 1);
	vertex(builder, pose, normal, LightTexture.FULL_BRIGHT, -0.5f,  0.5f, 0, 0);
	vertex(builder, pose, normal, LightTexture.FULL_BRIGHT,  0.5f,  0.5f, 1, 0);
	vertex(builder, pose, normal, LightTexture.FULL_BRIGHT,  0.5f, -0.5f, 1, 1);

	poseStack.popPose();
}

	private static void vertex(VertexConsumer builder, Matrix4f pose, Matrix3f normal, int packedLight, float x, float y, int u, int v) {
		builder.addVertex(pose, x, y, 0.0f)
				.setColor(255, 255, 255, 255)
				.setUv((float) u, (float) v)
				.setOverlay(OverlayTexture.NO_OVERLAY)
				.setUv2(packedLight, 1)
				.setNormal(0.0f, 1.0f, 0.0f);
				//.endVertex();
	}
}