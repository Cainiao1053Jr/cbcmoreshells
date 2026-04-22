package com.cainiao1053.cbcmoreshells.cannons.projectile_rack;

import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.material.ProjectileRackMaterial;
import com.google.common.collect.ImmutableMap;
import com.ibm.icu.impl.duration.impl.DataRecord;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannons.big_cannons.material.BigCannonMaterial;

import java.util.function.Function;

public abstract class ProjectileRackBaseBlock extends DirectionalBlock implements ProjectileRackBlock {

	private final ProjectileRackMaterial material;

	public static final BooleanProperty CEILING = BooleanProperty.create("ceiling");

	protected ProjectileRackBaseBlock(Properties properties, ProjectileRackMaterial material) {
		super(properties.pushReaction(PushReaction.BLOCK));
		this.material = material;
	}

	@Override
	protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
		builder.add(FACING);
		builder.add(CEILING);
		super.createBlockStateDefinition(builder);
	}

	@Override public ProjectileRackMaterial getCannonMaterial() { return this.material; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }
	public Boolean getCeiling(BlockState state) { return state.getValue(CEILING); }

	@SuppressWarnings("deprecation")
//	@Override
//	public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
//		if (!level.isClientSide) this.onRemoveCannon(state, level, pos, newState, isMoving);
//		super.onRemove(state, level, pos, newState, isMoving);
//	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) this.playerWillDestroyBigCannon(level, pos, state, player);
		super.playerWillDestroy(level, pos, state, player);
		return state;
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState()
				.setValue(FACING, context.getHorizontalDirection())
				.setValue(CEILING, context.getClickedFace() == Direction.DOWN);
	}


	@Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
	@Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }

}
