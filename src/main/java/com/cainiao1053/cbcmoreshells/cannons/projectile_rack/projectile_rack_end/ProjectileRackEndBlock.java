package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.projectile_rack_end;

import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.SolidProjectileRackBlock;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.material.ProjectileRackMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.index.CBCShapes;

public class ProjectileRackEndBlock extends SolidProjectileRackBlock<ProjectileRackEndBlockEntity> {

	private final MapCodec<? extends DirectionalBlock> codec;

	public ProjectileRackEndBlock(Properties properties, ProjectileRackMaterial cannonMaterial) {
		super(properties, cannonMaterial);
		this.codec = simpleCodec(this::fromSelf);
	}

	private ProjectileRackEndBlock fromSelf(Properties properties) {
		return new ProjectileRackEndBlock(properties, this.getCannonMaterial());
	}

	@Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

	@Override public boolean canConnectToSide(BlockState state, Direction dir) { return this.getFacing(state) == dir; }

	@Override
	public Direction getFacing(BlockState state) {
		return super.getFacing(state).getOpposite();
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getNearestLookingDirection().getOpposite());
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return CBCShapes.CANNON_END.get(state.getValue(FACING));
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.CANNON_END;
	}

	@Override
	public Class<ProjectileRackEndBlockEntity> getBlockEntityClass() {
		return ProjectileRackEndBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends ProjectileRackEndBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.PROJECTILE_RACK_END.get();
	}

}
