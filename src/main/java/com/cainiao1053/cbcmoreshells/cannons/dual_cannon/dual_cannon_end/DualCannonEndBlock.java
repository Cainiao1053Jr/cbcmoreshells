package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.SolidDualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
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

public class DualCannonEndBlock extends SolidDualCannonBlock<DualCannonEndBlockEntity> {

	private final MapCodec<? extends DirectionalBlock> codec;

	public DualCannonEndBlock(Properties properties, DualCannonMaterial cannonMaterial) {
		super(properties, cannonMaterial);
		this.codec = simpleCodec(this::fromSelf);
	}

	private DualCannonEndBlock fromSelf(Properties properties) {
		return new DualCannonEndBlock(properties, this.getCannonMaterial());
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
	public Class<DualCannonEndBlockEntity> getBlockEntityClass() {
		return DualCannonEndBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends DualCannonEndBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.DUAL_CANNON_END.get();
	}

}
