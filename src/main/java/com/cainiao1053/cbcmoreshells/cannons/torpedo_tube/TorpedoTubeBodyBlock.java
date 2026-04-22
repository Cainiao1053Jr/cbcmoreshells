package com.cainiao1053.cbcmoreshells.cannons.torpedo_tube;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBodyBlock;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterial;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.torpedo_end.TorpedoTubeEnd;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

import java.util.function.Supplier;

public class TorpedoTubeBodyBlock extends TorpedoTubeBaseBlock implements IBE<TorpedoTubeBlockEntity> {

	private final VoxelShaper visualShapes;
	private final VoxelShaper collisionShapes;
	private final Supplier<CannonCastShape> cannonShape;
	private final MapCodec<? extends DirectionalBlock> codec;

	public TorpedoTubeBodyBlock(Properties properties, TorpedoTubeMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape base) {
		this(properties, material, cannonShape, base, base);
	}

	public TorpedoTubeBodyBlock(Properties properties, TorpedoTubeMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape visualShape, VoxelShape collisionShape) {
		super(properties, material);
		this.visualShapes = new AllShapes.Builder(visualShape).forDirectional();
		this.collisionShapes = new AllShapes.Builder(collisionShape).forDirectional();
		this.cannonShape = cannonShape;
		this.codec = simpleCodec(this::fromSelf);
	}

	public static TorpedoTubeBodyBlock verySmall(Properties properties, TorpedoTubeMaterial material) {
		return new TorpedoTubeBodyBlock(properties, material, () -> CannonCastShape.VERY_SMALL, Block.box(2, 0, 2, 14, 16, 14));
	}

	public static TorpedoTubeBodyBlock small(Properties properties, TorpedoTubeMaterial material) {
		return new TorpedoTubeBodyBlock(properties, material, () -> CannonCastShape.SMALL, Block.box(1, 0, 1, 15, 16, 15));
	}

	public static TorpedoTubeBodyBlock medium(Properties properties, TorpedoTubeMaterial material) {
		return new TorpedoTubeBodyBlock(properties, material, () -> CannonCastShape.MEDIUM, Shapes.block());
	}

	public static TorpedoTubeBodyBlock large(Properties properties, TorpedoTubeMaterial material) {
		return new TorpedoTubeBodyBlock(properties, material, () -> CannonCastShape.LARGE, Block.box(-1, 0, -1, 17, 16, 17), Shapes.block());
	}

	public static TorpedoTubeBodyBlock veryLarge(Properties properties, TorpedoTubeMaterial material) {
		return new TorpedoTubeBodyBlock(properties, material, () -> CannonCastShape.VERY_LARGE, Block.box(-2, 0, -2, 18, 16, 18), Shapes.block());
	}

	private TorpedoTubeBodyBlock fromSelf(Properties properties) {
		return new TorpedoTubeBodyBlock(properties, this.getCannonMaterial(), this.cannonShape, this.visualShapes.get(Direction.UP), this.collisionShapes.get(Direction.UP));
	}

	@Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

	@Override
	public CannonCastShape getCannonShape() {
		return this.cannonShape.get();
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.collisionShapes.get(this.getFacing(state));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.visualShapes.get(this.getFacing(state));
	}

	@Override public TorpedoTubeEnd getDefaultOpeningType() { return TorpedoTubeEnd.OPEN; }

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public Class<TorpedoTubeBlockEntity> getBlockEntityClass() {
		return TorpedoTubeBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends TorpedoTubeBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.TORPEDO_TUBE_BODY.get();
	}

}
