//package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech;
//
//import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
//import com.cainiao1053.cbcmoreshells.index.CBCMSBlockPartials;
//import com.mojang.math.Axis;
//
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//import com.simibubi.create.AllBlocks;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//
//public class DualCannonQuickfiringBreechInstance extends BlockEntityInstance<DualCannonQuickfiringBreechBlockEntity> implements DynamicInstance {
//
//	private OrientedData breechblock;
//	private OrientedData shaft;
//	private OrientedData lever;
//	private Direction direction;
//	private Direction blockRotation;
//
//	public DualCannonQuickfiringBreechInstance(MaterialManager materialManager, DualCannonQuickfiringBreechBlockEntity blockEntity) {
//		super(materialManager, blockEntity);
//	}
//
//	@Override
//	public void init() {
//		super.init();
//
//		Direction.Axis axis = getRotationAxis(this.blockState);
//		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
//		if(facing.getAxis().isHorizontal()) {
//			this.blockRotation = facing.getCounterClockWise(axis);
//		}else {
//			this.blockRotation = Direction.SOUTH;
//		}
//		//this.blockRotation = facing.getCounterClockWise(axis);
//		if (this.blockRotation == Direction.DOWN) this.blockRotation = Direction.UP;
//
////		this.breechblock = this.materialManager.defaultSolid()
////			.material(Materials.ORIENTED)
////			.getModel(getPartialModelForState(this.blockState), this.blockState, this.blockRotation)
////			.createInstance();
//
//		this.breechblock = this.materialManager.defaultSolid()
//				.material(Materials.ORIENTED)
//				.getModel(getPartialModelForState(this.blockState), this.blockState, facing.getOpposite())
//				.createInstance();
//
////		this.shaft = this.materialManager.defaultSolid()
////			.material(Materials.ORIENTED)
////			.getModel(AllBlocks.SHAFT.getDefaultState().setValue(BlockStateProperties.AXIS, axis))
////			.createInstance();
//
//		this.direction = facing.getCounterClockWise(this.blockRotation.getAxis());
//
////		this.lever = this.materialManager.defaultSolid()
////			.material(Materials.ORIENTED)
////			.getModel(CBCMSBlockPartials.QUICKFIRING_BREECH_LEVER, this.blockState, this.direction)
////			.createInstance();
//
////		boolean alongFirst = this.blockState.getValue(DualCannonQuickfiringBreechBlock.AXIS);
////		if (facing.getAxis().isHorizontal() && !alongFirst) {
////			Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
////			Quaternionf q = Axis.of(rotDir.step()).rotationDegrees(90f);
////			this.breechblock.setRotation(q);
////		}
////		if (facing.getAxis() == Direction.Axis.X && alongFirst) {
////			this.breechblock.setRotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
////		}
//		//this.breechblock.setRotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
//
//		this.transformModels();
//	}
//
//	@Override
//	public void beginFrame() {
//		this.transformModels();
//	}
//
//	private void transformModels() {
//		float progress = this.blockEntity.getOpenProgress(AnimationTickHolder.getPartialTicks());
//		BlockPos instancePos = this.getInstancePosition();
//
//		float renderedBreechblockOffset = progress / 16.0f * 7.0f;
//		Vector3f normal = this.blockRotation.step();
//		normal.mul(renderedBreechblockOffset);
//		this.breechblock.setPosition(instancePos).nudge(normal.x(), normal.y(), normal.z());
//
//		//float angle = progress * 90;
//		//Quaternionf qrot = Axis.of(this.direction.step()).rotationDegrees(angle);
//		//this.shaft.setPosition(instancePos).setRotation(qrot);
//		//this.lever.setPosition(instancePos.relative(this.direction)).setRotation(qrot);
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
//		return state.getBlock() instanceof DualCannonBlock cBlock ? CBCMSBlockPartials.dualCannonBreechblockFor(cBlock.getCannonMaterial())
//			: CBCMSBlockPartials.STEEL_DUAL_CANNON_SLIDING_BREECHBLOCK;
//	}
//
//	private static Direction.Axis getRotationAxis(BlockState state) {
//		boolean flag = state.getValue(DualCannonQuickfiringBreechBlock.AXIS);
//		return switch (state.getValue(DualCannonQuickfiringBreechBlock.FACING).getAxis()) {
//			case X -> flag ? Direction.Axis.Y : Direction.Axis.Z;
//			case Y -> flag ? Direction.Axis.X : Direction.Axis.Z;
//			case Z -> flag ? Direction.Axis.X : Direction.Axis.Y;
//		};
//	}
//
//}
