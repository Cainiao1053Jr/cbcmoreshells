package com.cainiao1053.cbcmoreshells.cannons.flak_cannon.recoil_spring;

import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.FlakcannonBaseBlock;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.MovesWithFlakcannonRecoilSpring;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material.FlakcannonMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullFunction;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

public class FlakcannonRecoilSpringBlock extends FlakcannonBaseBlock implements IBE<FlakcannonRecoilSpringBlockEntity>, MovesWithFlakcannonRecoilSpring {

	private final NonNullFunction<Direction, BlockState> movingBlockFunction;

	public FlakcannonRecoilSpringBlock(Properties properties, FlakcannonMaterial material, NonNullFunction<Direction, BlockState> movingBlockFunction) {
		super(properties, material);
		this.movingBlockFunction = movingBlockFunction;
	}

	@Override
	public Class<FlakcannonRecoilSpringBlockEntity> getBlockEntityClass() {
		return FlakcannonRecoilSpringBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends FlakcannonRecoilSpringBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.FLAKCANNON_RECOIL_SPRING.get();
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.AUTOCANNON_RECOIL_SPRING;
	}

	@Override
	public boolean isBreechMechanism(BlockState state) {
		return false;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return AllShapes.SIX_VOXEL_POLE.get(this.getFacing(state).getAxis());
	}

	@Override
	public BlockState getMovingState(BlockState original) {
		return this.movingBlockFunction.apply(this.getFacing(original));
	}

	@Override public BlockState getStationaryState(BlockState original) { return original; }

}
