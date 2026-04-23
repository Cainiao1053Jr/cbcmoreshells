package com.cainiao1053.cbcmoreshells;


import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.createmod.catnip.animation.AnimationTickHolder;
import net.createmod.catnip.render.SuperRenderTypeBuffer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;


//@Mod.EventBusSubscriber(Dist.CLIENT)
//@EventBusSubscriber(modid = Cbcmoreshells.MODID, value = Dist.CLIENT)
public class CBCMSClientEvents {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Pre event) {
        //CbcmoreshellsClient.CLIENT_LERPED_OUTLINER.tickOutlines();
    }

//    @SubscribeEvent
//    public static void onTick(TickEvent.ClientTickEvent event){
//
//        if (event.phase == TickEvent.Phase.START){
//            CbcmoreshellsClient.CLIENT_LERPED_OUTLINER.tickOutlines();
//        }
//
//        if (event.phase == TickEvent.Phase.END) { // Only call code once as the tick event is called twice every tick
//        }
//
//    }

//    @SubscribeEvent
//    public static void onRenderWorld(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
//            return;
//        PoseStack ms = event.getPoseStack();
//        ms.pushPose();
//        SuperRenderTypeBuffer buffer = SuperRenderTypeBuffer.getInstance();
//        float partialTicks = AnimationTickHolder.getPartialTicks();
//        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera()
//                .getPosition();
//        RenderSystem.disableDepthTest();
//
//        CbcmoreshellsClient.CLIENT_LERPED_OUTLINER.renderOutlines(ms, buffer, camera, partialTicks);
//
//        RenderSystem.enableDepthTest();
//        buffer.draw();
//        //RenderSystem.enableCull();
//        RenderSystem.disableCull();
//        ms.popPose();
//
//    }

//    @SubscribeEvent
//    public static void onRenderWorld(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_PARTICLES)
//            return;
//
//        PoseStack ms = event.getPoseStack();
//        ms.pushPose();
//
//        MultiBufferSource.BufferSource buffer = Minecraft.getInstance()
//                .renderBuffers()
//                .bufferSource();
//
//        float partialTicks = AnimationTickHolder.getPartialTicks();
//        Vec3 camera = Minecraft.getInstance().gameRenderer.getMainCamera().getPosition();
//
//        RenderSystem.disableDepthTest();
//
//        CbcmoreshellsClient.CLIENT_LERPED_OUTLINER.renderOutlines(ms, buffer, camera, partialTicks);
//
//        RenderSystem.enableDepthTest();
//        buffer.endBatch();
//        RenderSystem.disableCull();
//        ms.popPose();
//    }

}
