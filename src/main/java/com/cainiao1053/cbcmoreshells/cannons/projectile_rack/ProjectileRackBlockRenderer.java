package com.cainiao1053.cbcmoreshells.cannons.projectile_rack;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.createmod.catnip.render.CachedBuffers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;


public class ProjectileRackBlockRenderer implements BlockEntityRenderer<ProjectileRackBlockEntity> {
    public ProjectileRackBlockRenderer(BlockEntityRendererProvider.Context context) {
    }

    public void render(ProjectileRackBlockEntity blockEntity, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
        BigCannonBehavior behavior = blockEntity.cannonBehavior();
        if (behavior == null) return;

        BlockState loadedBlockState = behavior.block().state();
        if (loadedBlockState == null || loadedBlockState.isAir()) return;

        BlockState blockState = blockEntity.getBlockState();
        Direction facing = blockState.getValue(BlockStateProperties.FACING);
        VertexConsumer vcons = buffer.getBuffer(RenderType.solid());
        CachedBuffers.block(loadedBlockState)
                .translate(facing.step().mul(0.6f))
                .light(light)
                .renderInto(ms, vcons);
    }
}
