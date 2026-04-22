package com.cainiao1053.cbcmoreshells.client.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;

import java.util.*;

@EventBusSubscriber(modid = Cbcmoreshells.MODID, value = Dist.CLIENT)
public class RenderHandler {

	private static final Map<EntityType<?>, List<ExtraEntityLayer>> extraLayers = new HashMap<>();

	public static void registerExtraLayer(EntityType<?> type, ExtraEntityLayer layer) {
		extraLayers.computeIfAbsent(type, t -> new ArrayList<>()).add(layer);
	}

	@SubscribeEvent
	public static void onRenderStage(RenderLevelStageEvent event) {
		if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

		Minecraft mc = Minecraft.getInstance();
		Camera camera = event.getCamera();
		PoseStack poseStack = event.getPoseStack();
		MultiBufferSource buffer = mc.renderBuffers().bufferSource();

		//for (Entity entity : mc.level.entitiesForRendering()) {
//		WorldBorder border = mc.level.getWorldBorder();
//		AABB entireWorld = new AABB(border.getMinX(), -64, border.getMinZ(), border.getMaxX(), 320, border.getMaxZ());
//		List<Entity> allEntities = mc.level.getEntities(null, entireWorld);

		for (Entity entity : mc.level.entitiesForRendering()) {
			List<ExtraEntityLayer> layers = extraLayers.get(entity.getType());
			if (layers == null) continue;

			poseStack.pushPose();

			double dx = entity.getX() - camera.getPosition().x;
			double dy = entity.getY() - camera.getPosition().y;
			double dz = entity.getZ() - camera.getPosition().z;
			poseStack.translate(dx, dy, dz);

			for (ExtraEntityLayer layer : layers) {
				layer.render(poseStack, buffer, entity, event.getPartialTick().getGameTimeDeltaTicks());
			}

			poseStack.popPose();
		}
	}
}