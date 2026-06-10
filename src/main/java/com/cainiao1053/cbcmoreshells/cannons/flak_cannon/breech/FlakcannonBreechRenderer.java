package com.cainiao1053.cbcmoreshells.cannons.flak_cannon.breech;

import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.FlakcannonBlock;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import com.jozufozu.flywheel.backend.Backend;
import com.jozufozu.flywheel.core.PartialModel;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;

import org.joml.Quaternionf;
import org.joml.Vector3f;
import com.simibubi.create.foundation.blockEntity.renderer.SmartBlockEntityRenderer;
import com.simibubi.create.foundation.render.CachedBufferer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerBlock;
import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;

public class FlakcannonBreechRenderer extends SmartBlockEntityRenderer<AbstractFlakcannonBreechBlockEntity> {

	public FlakcannonBreechRenderer(BlockEntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	protected void renderSafe(AbstractFlakcannonBreechBlockEntity breech, float partialTicks, PoseStack ms, MultiBufferSource buffer, int light, int overlay) {
		super.renderSafe(breech, partialTicks, ms, buffer, light, overlay);
		if (Backend.canUseInstancing(breech.getLevel())) return;

		BlockState state = breech.getBlockState();
		Direction facing = state.getValue(FlakcannonBreechBlock.FACING);

		ms.pushPose();

		if (state.getValue(FlakcannonBreechBlock.HANDLE)) {
			if (breech.getSeatColor() != null) {
				CachedBufferer.partialFacing(CBCMSBlockPartials.flakcannonSeatFor(breech.getSeatColor()), state, facing)
					.rotateCentered(Axis.YP.rotationDegrees(facing.getAxis().isVertical() ? 180 : 0))
					.light(light)
					.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
			}
		} else {
			Vector3f normal = facing.step();
			normal.mul(breech.getAnimateOffset(partialTicks) * -0.5f);
			CachedBufferer.partialFacing(getPartialModelForState(breech), state, facing)
				.translate(normal)
				.rotateCentered(Axis.YP.rotationDegrees(facing.getAxis().isVertical() ? 180 : 0))
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
		}

		ItemStack container = breech.getMagazine();
		if (container.getItem() instanceof AutocannonAmmoContainerItem) {
			boolean flag = facing.getAxis().isVertical();
			Quaternionf q1;
			if (flag) {
				float f = facing == Direction.UP ? 90 : -90;
				q1 = Axis.ZP.rotationDegrees(f);
				q1.mul(Axis.XP.rotationDegrees(f));
			} else {
				q1 = Axis.YP.rotationDegrees(-90 - facing.toYRot());
			}
			Direction offset = flag
				? facing.getCounterClockWise(Direction.Axis.Z)
				: facing.getClockWise(Direction.Axis.Y);
			Vector3f normal = facing == Direction.UP ? offset.getOpposite().step() : offset.step();
			normal.mul(10 / 16f);

			CachedBufferer.block(getAmmoContainerModel(container))
				.translate(normal)
				.rotateCentered(q1)
				.light(light)
				.renderInto(ms, buffer.getBuffer(RenderType.cutoutMipped()));
		}

		ms.popPose();
	}

	private static PartialModel getPartialModelForState(AbstractFlakcannonBreechBlockEntity breech) {
		return breech.getBlockState().getBlock() instanceof FlakcannonBlock cBlock
			? CBCMSBlockPartials.flakcannonEjectorFor(cBlock.getAutocannonMaterial())
			: CBCMSBlockPartials.STEEL_FLAKCANNON_EJECTOR;
	}

	private static BlockState getAmmoContainerModel(ItemStack stack) {
		BlockState state = ((BlockItem) stack.getItem()).getBlock().defaultBlockState();
		if (state.hasProperty(AutocannonAmmoContainerBlock.CONTAINER_STATE)) {
			state = state.setValue(AutocannonAmmoContainerBlock.CONTAINER_STATE,
				AutocannonAmmoContainerBlock.State.getFromFilled(AutocannonAmmoContainerItem.getTotalAmmoCount(stack) > 0));
		}
		return state;
	}

}
