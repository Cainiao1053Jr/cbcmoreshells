package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech;

import java.util.function.Consumer;

import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlock;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
import com.mojang.math.Axis;

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
import org.joml.Vector3f;

import static com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBaseBlock.CEILING;

public class ProjectileRackQuickfiringBreechVisual extends AbstractBlockEntityVisual<ProjectileRackQuickfiringBreechBlockEntity> implements SimpleDynamicVisual {

	private final OrientedInstance breechblock;
	//private final OrientedInstance shaft;
	//private final OrientedInstance lever;
	private final Direction direction;
	private final Direction blockRotation;
	private final Direction blockTranslation;
	private final Vector3f offsetI;

	public ProjectileRackQuickfiringBreechVisual(VisualizationContext ctx, ProjectileRackQuickfiringBreechBlockEntity blockEntity, float partialTick) {
		super(ctx, blockEntity, partialTick);

		Direction.Axis axis = getRotationAxis(this.blockState);
		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
		Direction blockRotation = facing.getCounterClockWise(axis);
		this.blockTranslation = facing.getOpposite();
		if (blockRotation == Direction.DOWN) blockRotation = Direction.UP;
		this.blockRotation = blockRotation;
		this.direction = facing.getCounterClockWise(this.blockRotation.getAxis());

		this.breechblock = instancerProvider().instancer(InstanceTypes.ORIENTED, Models.partial(getPartialModelForState(this.blockState)))
			.createInstance();

		if (facing == Direction.UP) {
			this.breechblock.rotation(Axis.YN.rotationDegrees(90f));
		} else if (facing == Direction.DOWN) {
			this.breechblock.rotation(Axis.YN.rotationDegrees(-90f));
		} else if (facing == Direction.NORTH) {
			this.breechblock.rotation(Axis.of(this.blockRotation.step()).rotationDegrees(180f));
		} else if (facing == Direction.WEST) {
			this.breechblock.rotation(Axis.of(this.blockRotation.step()).rotationDegrees(270f));
		} else if (facing == Direction.EAST) {
			this.breechblock.rotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
		}

		this.offsetI = new Vector3f(0, 0, 0);
		if (!this.blockState.getValue(CEILING) && facing != Direction.UP && facing != Direction.DOWN) {
			this.offsetI.add(0, -0.875f, 0);
		}

		this.transformModels(partialTick);
	}

	@Override
	public void beginFrame(DynamicVisual.Context ctx) {
		this.transformModels(ctx.partialTick());
	}

	private void transformModels(float partialTick) {
		float progress = this.blockEntity.getOpenProgress(partialTick);
		BlockPos visualPos = this.getVisualPosition();

		float renderedBreechblockOffset = progress / 16.0f * 7.0f;
		Vector3f normal = this.blockTranslation.step();
		normal.mul(renderedBreechblockOffset);
		this.breechblock.position(visualPos)
			.translatePosition(this.offsetI.x + normal.x(), this.offsetI.y + normal.y(), this.offsetI.z + normal.z())
			.setChanged();

//		float angle = -progress * 90;
//		Quaternionf qrot = Axis.of(this.direction.step()).rotationDegrees(angle);
//		this.shaft.position(visualPos).rotation(qrot).setChanged();
//		this.lever.position(visualPos.relative(this.direction)).rotation(qrot).setChanged();
	}

	@Override
	public void updateLight(float partialTick) {
		this.relight(this.pos, this.breechblock);
//		this.relight(this.pos, this.shaft);
//		this.relight(this.pos, this.lever);
	}

	@Override
	public void _delete() {
		this.breechblock.delete();
//		this.shaft.delete();
//		this.lever.delete();
	}

	@Override
	public void collectCrumblingInstances(Consumer<Instance> consumer) {
		consumer.accept(this.breechblock);
//		consumer.accept(this.shaft);
//		consumer.accept(this.lever);
	}

	private static PartialModel getPartialModelForState(BlockState state) {
		return state.getBlock() instanceof ProjectileRackBlock cBlock
			? CBCMSBlockPartials.projectileLockBlockFor(cBlock.getCannonMaterial())
			: CBCMSBlockPartials.STEEL_PROJECTILE_RACK_SLIDING_BREECHBLOCK;
	}

	private static Direction.Axis getRotationAxis(BlockState state) {
		boolean flag = state.getValue(ProjectileRackQuickfiringBreechBlock.AXIS);
		return switch (state.getValue(ProjectileRackQuickfiringBreechBlock.FACING).getAxis()) {
			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
		};
	}

}
