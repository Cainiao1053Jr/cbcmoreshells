package com.cainiao1053.cbcmoreshells.cannons.projectile_rack;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.createmod.catnip.render.SuperByteBuffer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.slf4j.Logger;


public class ProjectileRackBlockRenderer implements BlockEntityRenderer<ProjectileRackBlockEntity> {
    public ProjectileRackBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(ProjectileRackBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        Block loadedBlock = blockEntity.getBlockState().getBlock();

//        if (loadedBlock instanceof ProjectileRackBlock) {
//            BlockState loadedBlockState = blockEntity.cannonBehavior().block().state();
//            BlockState blockState = blockEntity.getBlockState();
//            Direction facing = (Direction) blockState.getValue(BlockStateProperties.FACING);
//            VertexConsumer vcons = buffer.getBuffer(RenderType.solid());
//            SuperByteBuffer c = CachedBuffers.block(loadedBlockState);
//            //float rotationAngle = facing.getAxisDirection().getStep() == 1 ? 0.0F : 180.0F;
//            c.rotateCentered(Axis.YN.rotationDegrees(0))
//                    .translate(facing.step().mul(0.6f))
//                    .light(light)
//                    .forEntityRender()
//                    .renderInto(ms, vcons);
//            ms.pushPose();
//            ms.popPose();
//        }

    }
}
