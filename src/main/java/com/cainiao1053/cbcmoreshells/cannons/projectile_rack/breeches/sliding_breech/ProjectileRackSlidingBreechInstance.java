//package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.sliding_breech;
//
////import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpQuickfiringBreechBlock;
//import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackQuickfiringBreechBlock;
//import com.jozufozu.flywheel.api.MaterialManager;
//import com.jozufozu.flywheel.api.instance.DynamicInstance;
//import com.jozufozu.flywheel.core.Materials;
//import com.jozufozu.flywheel.core.materials.oriented.OrientedData;
//import com.mojang.math.Axis;
//
//import org.joml.Quaternionf;
//import org.joml.Vector3f;
//import com.simibubi.create.content.kinetics.base.ShaftInstance;
//import com.simibubi.create.foundation.utility.AnimationTickHolder;
//
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import rbasamoyai.createbigcannons.CBCClientCommon;
////import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlock;
//
//public class ProjectileRackSlidingBreechInstance extends ShaftInstance<ProjectileRackSlidingBreechBlockEntity> implements DynamicInstance {
//
//	private final ProjectileRackSlidingBreechBlockEntity breech;
//	private OrientedData breechblock;
//	private Direction blockRotation;
//
//	public ProjectileRackSlidingBreechInstance(MaterialManager dispatcher, ProjectileRackSlidingBreechBlockEntity tile) {
//		super(dispatcher, tile);
//		this.breech = tile;
//	}
//
//	@Override
//	public void init() {
//		super.init();
//
//		Direction.Axis axis = CBCClientCommon.getRotationAxis(this.blockState);
//		Direction facing = this.blockState.getValue(BlockStateProperties.FACING);
//		this.blockRotation = facing.getCounterClockWise(axis);
//		if (this.blockRotation == Direction.DOWN) this.blockRotation = Direction.UP;
//
//		this.breechblock = this.materialManager.defaultSolid()
//			.material(Materials.ORIENTED)
//			.getModel(CBCClientCommon.getBreechblockForState(this.blockState), this.blockState, this.blockRotation)
//			.createInstance();
//
//		boolean alongFirst = this.blockState.getValue(ProjectileRackQuickfiringBreechBlock.AXIS);
//		if (facing.getAxis().isHorizontal() && !alongFirst) {
//			Direction rotDir = facing.getAxis() == Direction.Axis.X ? Direction.UP : Direction.EAST;
//			Quaternionf q = Axis.of(rotDir.step()).rotationDegrees(90f);
//			this.breechblock.setRotation(q);
//		}
//		if (facing.getAxis() == Direction.Axis.X && alongFirst) {
//			this.breechblock.setRotation(Axis.of(this.blockRotation.step()).rotationDegrees(90f));
//		}
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
//		float renderedBreechblockOffset = this.breech.getRenderedBlockOffset(AnimationTickHolder.getPartialTicks());
//		renderedBreechblockOffset = renderedBreechblockOffset / 16.0f * 13.0f;
//		Vector3f normal = this.blockRotation.step();
//		normal.mul(renderedBreechblockOffset);
//		this.breechblock.setPosition(this.getInstancePosition()).nudge(normal.x(), normal.y(), normal.z());
//	}
//
//	@Override
//	public void updateLight() {
//		super.updateLight();
//		this.relight(this.pos, this.breechblock);
//	}
//
//	@Override
//	public void remove() {
//		super.remove();
//		this.breechblock.delete();
//	}
//
//}
