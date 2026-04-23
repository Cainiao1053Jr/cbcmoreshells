package com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech;

import java.util.function.Consumer;

import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.TorpedoTubeBlock;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import com.mojang.math.Axis;
import com.simibubi.create.AllPartialModels;

import dev.engine_room.flywheel.api.instance.Instance;
import dev.engine_room.flywheel.api.visual.DynamicVisual;
import dev.engine_room.flywheel.api.visualization.VisualizationContext;
import dev.engine_room.flywheel.lib.instance.InstanceTypes;
import dev.engine_room.flywheel.lib.instance.OrientedInstance;
import dev.engine_room.flywheel.lib.model.Models;
import dev.engine_room.flywheel.lib.model.baked.PartialModel;
import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class TorpQuickfiringBreechVisual extends AbstractBlockEntityVisual<TorpQuickfiringBreechBlockEntity> implements SimpleDynamicVisual {

	private final OrientedInstance breechblock;
	private final OrientedInstance shaft;
	private final OrientedInstance lever;
	private final Direction direction;
	private final Direction blockRotation;

	public TorpQuickfiringBreechVisual(VisualizationContext ctx, TorpQuickfiringBreechBlockEntity blockEntity, float partialTick) {
		super(ctx, blockEntity, partialTick);

		Direction.Axis axis = getRotationAxis(this.blockState);
		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
		Direction blockRotation = facing.getCounterClockWise(axis);
		if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;
		this.blockRotation = blockRotation;
		this.direction = facing.getCounterClockWise(this.blockRotation.getAxis());

		this.breechblock = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPartialModelForState(this.blockState)))
			.createInstance();

		this.shaft = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(AllPartialModels.SHAFT))
			.createInstance();

		this.lever = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(CBCMSBlockPartials.QUICKFIRING_BREECH_LEVER))
			.createInstance();

		boolean alongFirst = this.blockState.getValue(TorpQuickfiringBreechBlock.AXIS);
		if (facing.getAxis().isHorizontal() && !alongFirst) {
			Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
			Quaternionf q = Axis.of(rotDir.step()).rotationDegrees(90f);
			this.breechblock.rotation(q);
		}
		if (facing.getAxis() == Direction.Axis.X && alongFirst) {
			this.breechblock.rotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
		}
		rotateBreechblock(breechblock, facing);

//		breechblock.rotateXDegrees(90);
//		breechblock.rotateYDegrees(180);
//		breechblock.rotateZDegrees(180);
		this.transformModels(partialTick);
	}

	private static void rotateBreechblock(OrientedInstance breechblock, Direction facing) {
		switch (facing) {
			case EAST ->{
				breechblock.rotateXDegrees(90);
				breechblock.rotateZDegrees(180);
				breechblock.rotateYDegrees(180);
			}
			case WEST ->{
				breechblock.rotateXDegrees(90);
				breechblock.rotateYDegrees(180);
			}
			case NORTH ->{
				breechblock.rotateXDegrees(90);
				breechblock.rotateYDegrees(180);
			}
			case SOUTH ->{
				breechblock.rotateXDegrees(90);
				breechblock.rotateYDegrees(180);
				breechblock.rotateZDegrees(180);
			}
			default ->{}
		}
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels(ctx.partialTick());
	}

	private void transformModels(float partialTick) {
		float progress = this.blockEntity.getOpenProgress(partialTick);
		BlockPos visualPos = this.getVisualPosition();

		float renderedBreechblockOffset = progress / 16.0f * 13.0f;
		Vector3f normal = this.blockRotation.step();
		normal.mul(renderedBreechblockOffset);
		this.breechblock.position(visualPos)
			.translatePosition(normal.x(), normal.y(), normal.z())
			.setChanged();

		float angle = progress * 90;
		Quaternionf qrot = Axis.of(this.direction.step()).rotationDegrees(angle);
		this.shaft.position(visualPos).rotation(qrot).setChanged();
		this.lever.position(visualPos.relative(this.direction)).rotation(qrot).setChanged();
		rotateLever(this.lever, this.direction);
	}

	private static void rotateLever(OrientedInstance lever, Direction direction){
		switch(direction){
			case NORTH ->{
				lever.rotateYDegrees(180);
			}
			case EAST ->{
				lever.rotateYDegrees(90);
			}
			case WEST -> {
				lever.rotateYDegrees(-90);
			}
			case SOUTH -> {
				//lever.rotateYDegrees(90);
			}
			default -> {}
		}
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.pos, this.breechblock);
		this.relight(this.pos, this.shaft);
		this.relight(this.pos, this.lever);
	}

	@Override
	public void _delete() {
		this.breechblock.delete();
		this.shaft.delete();
		this.lever.delete();
	}

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		consumer.accept(this.breechblock);
		consumer.accept(this.shaft);
		consumer.accept(this.lever);
	}

	private static PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof TorpedoTubeBlock cBlock ? CBCMSBlockPartials.breechblockFor(cBlock.getCannonMaterial())
			: CBCMSBlockPartials.STEEL_TORPEDO_SLIDING_BREECHBLOCK;
	}

	private static Direction.Axis getRotationAxis(BlockState state) {
		boolean flag = state.getValue(TorpQuickfiringBreechBlock.AXIS);
		return switch (state.getValue(TorpQuickfiringBreechBlock.FACING).getAxis()) {
			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
		};
	}

}
