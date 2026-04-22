package com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.torpedo_end;

import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.SolidTorpedoTubeBlock;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterial;
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

public class TorpedoTubeEndBlock extends SolidTorpedoTubeBlock<TorpedoTubeEndBlockEntity> {

	private final MapCodec<? extends DirectionalBlock> codec;

	public TorpedoTubeEndBlock(Properties properties, TorpedoTubeMaterial cannonMaterial) {
		super(properties, cannonMaterial);
		this.codec = simpleCodec(this::fromSelf);
	}

	private TorpedoTubeEndBlock fromSelf(Properties properties) {
		return new TorpedoTubeEndBlock(properties, this.getCannonMaterial());
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
	public Class<TorpedoTubeEndBlockEntity> getBlockEntityClass() {
		return TorpedoTubeEndBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends TorpedoTubeEndBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.TORPEDO_TUBE_END.get();
	}

}
