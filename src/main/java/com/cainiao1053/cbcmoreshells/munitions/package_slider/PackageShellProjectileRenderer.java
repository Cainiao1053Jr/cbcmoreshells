package com.cainiao1053.cbcmoreshells.munitions.package_slider;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.box.PackageRenderer;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;

/** Renders the shell as the Create package model of the package it carries, pointed along its flight. */
public class PackageShellProjectileRenderer<T extends AbstractPackageShellProjectile> extends EntityRenderer<T> {

	public PackageShellProjectileRenderer(EntityRendererProvider.Context context) {
		super(context);
		this.shadowRadius = 0.5f;
	}

	@Override
	public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffers,
					   int packedLight) {
		ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(entity.getDisplayItem());
		PartialModel model = AllPartialModels.PACKAGES.get(itemId);
		if (model != null) {
			float yaw = Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
			float pitch = Mth.lerp(partialTicks, entity.xRotO, entity.getXRot());
			float height = PackageItem.getHeight(new ItemStack(entity.getDisplayItem()));

			poseStack.pushPose();
			poseStack.mulPose(Axis.YP.rotationDegrees(yaw));
			poseStack.mulPose(Axis.XP.rotationDegrees(-pitch));
			// The entity position is the centre of its box, the package model's origin is its bottom centre.
			poseStack.translate(0, -height / 2.0f, 0);
			// A yaw of -90 cancels the rotation renderBox applies, since the pose stack is already rotated.
			PackageRenderer.renderBox(entity, -90.0f, poseStack, buffers, packedLight, model);
			poseStack.popPose();
		}
		super.render(entity, entityYaw, partialTicks, poseStack, buffers, packedLight);
	}

	@Override
	public ResourceLocation getTextureLocation(T entity) {
		return null;
	}

}
