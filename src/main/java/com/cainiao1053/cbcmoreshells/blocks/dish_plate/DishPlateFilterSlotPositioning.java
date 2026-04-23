//package com.cainiao1053.cbcmoreshells.blocks.dish_plate;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import com.mojang.math.Axis;
//import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
//import com.simibubi.create.foundation.utility.AngleHelper;
//import com.simibubi.create.foundation.utility.VecHelper;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.world.phys.Vec3;
//
//public class DishPlateFilterSlotPositioning extends ValueBoxTransform.Sided{
//
//
//	@Override
//	protected boolean isSideActive(BlockState state, Direction direction) {
//		return direction == Direction.UP;
//	}
//
//	@Override
//	public Vec3 getLocalOffset(BlockState state) {
//		//float horizontalAngle = 90;
//		//Vec3 southLocation = VecHelper.voxelSpace(8, 1, 15.5f);
//		Vec3 vec = VecHelper.voxelSpace(2f, 0f, 3f);
//		return VecHelper.rotateCentered(vec, AngleHelper.horizontalAngle(state.getValue(BlockStateProperties.HORIZONTAL_FACING)), Direction.Axis.Y);
//	}
//
//
//	@Override
//	public void rotate(BlockState state, PoseStack ms) {
//		Direction facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
//		float yRot = AngleHelper.horizontalAngle(facing);
//		//ms.rotateAround(Axis.YP.rotationDegrees(yRot),8,2,8);
//		ms.mulPose(Axis.YP.rotationDegrees(yRot));
//		super.rotate(state, ms);
//	}
//
//	@Override
//	protected Vec3 getSouthLocation() {
//		//return VecHelper.voxelSpace(14, 3, 1.75);
//		return Vec3.ZERO;
//	}
//
//}
