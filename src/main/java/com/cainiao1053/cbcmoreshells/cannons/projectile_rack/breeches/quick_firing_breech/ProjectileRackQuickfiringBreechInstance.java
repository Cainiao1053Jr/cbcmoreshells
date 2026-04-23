//package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech;
//
//import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlock;
//import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
//import com.mojang.math.Axis;
//import dev.engine_room.flywheel.api.visual.DynamicVisual;
//import dev.engine_room.flywheel.api.visualization.VisualizationContext;
//import dev.engine_room.flywheel.lib.instance.OrientedInstance;
//import dev.engine_room.flywheel.lib.model.baked.PartialModel;
//import dev.engine_room.flywheel.lib.visual.AbstractBlockEntityVisual;
//import dev.engine_room.flywheel.lib.visual.SimpleDynamicVisual;
//import net.createmod.catnip.animation.AnimationTickHolder;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import org.joml.Vector3f;
//import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntity;
//
//import static com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBaseBlock.CEILING;
//
//
//public class ProjectileRackQuickfiringBreechVisual extends AbstractBlockEntityVisual<ProjectileRackQuickfiringBreechBlockEntity> implements SimpleDynamicVisual {
//
//	private OrientedInstance breechblock;
//	//private OrientedData shaft;
//	//private OrientedData lever;
//	private Direction direction;
//	private Direction blockRotation;
//	private Direction blockTranslation;
//	private Vector3f positionI;
//	private Vector3f offsetI = new Vector3f(0, 0, 0);
//
//	public ProjectileRackQuickfiringBreechVisual(VisualizationContext ctx, ProjectileRackQuickfiringBreechBlockEntity blockEntity, float partialTick) {
//		super(ctx, blockEntity, partialTick);
//	}
//
//	@Override
//	public void init() {
//		super.init();
//
//		Direction.Axis axis = getRotationAxis(this.blockState);
//		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
//		this.blockRotation = facing.getCounterClockWise(axis);
//		this.blockTranslation = facing.getOpposite();
//		if (this.blockRotation == Direction.DOWN) this.blockRotation = Direction.UP;
//
//		this.breechblock = this.materialManager.defaultSolid()
//			.material(Materials.ORIENTED)
//			.getModel(getPartialModelForState(this.blockState), this.blockState, this.blockRotation)
//			.createInstance();
//
//
//		this.direction = facing.getCounterClockWise(this.blockRotation.getAxis());
//
//		if(facing == Direction.UP) {
//			this.breechblock.setRotation(Axis.YN.rotationDegrees(90f));
//			//this.breechblock.setRotation(Axis.XN.rotationDegrees(180f));
//		}else if(facing == Direction.DOWN) {
//			this.breechblock.setRotation(Axis.YN.rotationDegrees(-90f));
//		}else if(facing == Direction.NORTH) {
//			this.breechblock.setRotation(Axis.of(this.blockRotation.step()).rotationDegrees(180f));
//		}else if(facing == Direction.WEST){
//			this.breechblock.setRotation(Axis.of(this.blockRotation.step()).rotationDegrees(270f));
//		}else if(facing == Direction.EAST){
//			this.breechblock.setRotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
//		}
//
//		if(!this.blockState.getValue(CEILING) && facing != Direction.UP && facing != Direction.DOWN) {
//			offsetI.add(0,-0.875f,0);
//		}
//
//		this.transformModels();
//	}
//
//	@Override
//	public void beginFrame(DynamicVisual.Context ctx) {
//		this.transformModels(ctx);
//	}
//
//	private void transformModels(DynamicVisual.Context ctx) {
//		float progress = this.blockEntity.getOpenProgress(AnimationTickHolder.getPartialTicks());
//		BlockPos instancePos = this.getVisualPosition();
//		positionI = new Vector3f(instancePos.getX(), instancePos.getY(), instancePos.getZ());
////		Vector3f offsetI = installLock(this.blockState);
////		positionI = positionI.add(offsetI);
//
//		float renderedBreechblockOffset = progress / 16.0f * 7.0f;
////		Vector3f normal = this.blockRotation.step();
//		Vector3f normal = this.blockTranslation.step();
//		normal.mul(renderedBreechblockOffset);
//		this.breechblock.setPosition(positionI.add(offsetI)).nudge(normal.x(), normal.y(), normal.z());
//
////		float angle = -progress * 90;
////		Quaternionf qrot = Axis.of(this.direction.step()).rotationDegrees(angle);
////		//this.shaft.setPosition(instancePos).setRotation(qrot);
////		this.lever.setPosition(instancePos.relative(this.direction)).setRotation(qrot);
////		this.lever.setPosition(positionI).setRotation(qrot);
//	}
//
//	@Override
//	public void updateLight() {
//		super.updateLight();
//		this.relight(this.pos, this.breechblock);
//		//this.relight(this.pos, this.shaft);
//		//this.relight(this.pos, this.lever);
//	}
//
//	@Override
//	public void remove() {
//		this.breechblock.delete();
//		//this.shaft.delete();
//		//this.lever.delete();
//	}
//
//	private static PartialModel getPartialModelForState(BlockState state) {
//		return state.getBlock() instanceof ProjectileRackBlock cBlock ? CBCMSBlockPartials.projectileLockBlockFor(cBlock.getCannonMaterial())
//			: CBCMSBlockPartials.STEEL_PROJECTILE_RACK_SLIDING_BREECHBLOCK;
//	}
//
//	private Vector3f installLock(BlockState state) {
//		Vector3f lockIns = new Vector3f(0,-0.5f,0);
//		Direction facing = state.getValue(BlockStateProperties.FACING);
//		Boolean ceiling = state.getValue(CEILING);
//		switch (facing) {
//			case DOWN:
//			case UP:
//			case NORTH:
//				lockIns.add(0,0,0);
//				if(ceiling){
//					lockIns.add(0,1,0);
//				}
//			case SOUTH:
//				lockIns.add(0f,0,0f);
//				if(ceiling){
//					lockIns.add(0,1,0);
//				}
//			case WEST:
//				lockIns.add(-1,0,1);
//				if(ceiling){
//					lockIns.add(0,1,0);
//				}
//			case EAST:
//				lockIns.add(-0.5f,0,-0.5f);
//				if(ceiling){
//					lockIns.add(0,1,0);
//				}
//		}
//		return lockIns;
//	}
//
////	private Direction getTranslation(BlockState state) {
////		switch (state.getValue(BlockStateProperties.FACING)) {
////			case DOWN:
////			case UP:
////			case NORTH:
////			case SOUTH:
////			case WEST:
////			case EAST:
////		}
////	}
//
//	private static Direction.Axis getRotationAxis(BlockState state) {
//		boolean flag = state.getValue(ProjectileRackQuickfiringBreechBlock.AXIS);
//		return switch (state.getValue(ProjectileRackQuickfiringBreechBlock.FACING).getAxis()) {
//			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
//			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
//			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
//		};
//	}
//
//}
