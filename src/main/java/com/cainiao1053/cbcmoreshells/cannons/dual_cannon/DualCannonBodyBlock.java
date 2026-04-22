package com.cainiao1053.cbcmoreshells.cannons.dual_cannon;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEnd;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
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

public class DualCannonBodyBlock extends DualCannonBaseBlock implements IBE<DualCannonBlockEntity> {

	private final VoxelShaper visualShapes;
	private final VoxelShaper collisionShapes;
	private final Supplier<CannonCastShape> cannonShape;
	private final MapCodec<? extends DirectionalBlock> codec;

	public DualCannonBodyBlock(Properties properties, DualCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape base) {
		this(properties, material, cannonShape, base, base);
	}

	public DualCannonBodyBlock(Properties properties, DualCannonMaterial material, Supplier<CannonCastShape> cannonShape, VoxelShape visualShape, VoxelShape collisionShape) {
		super(properties, material);
		this.visualShapes = new AllShapes.Builder(visualShape).forDirectional();
		this.collisionShapes = new AllShapes.Builder(collisionShape).forDirectional();
		this.cannonShape = cannonShape;
		this.codec = simpleCodec(this::fromSelf);
	}

	public static DualCannonBodyBlock verySmall(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.VERY_SMALL, Block.box(0, 0, 5, 16, 16, 11));
	}

	public static DualCannonBodyBlock singleSmall(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.SMALL, Block.box(5, 0, 5, 11, 16, 11));
	}

	public static DualCannonBodyBlock singleMedium(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.SMALL, Block.box(4, 0, 4, 12, 16, 12));
	}

	public static DualCannonBodyBlock singleLarge(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.SMALL, Block.box(3, 0, 3, 13, 16, 13));
	}

	public static DualCannonBodyBlock medium(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.MEDIUM, Block.box(0, 0, 4, 16, 16, 12));
	}

	public static DualCannonBodyBlock wide(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.MEDIUM, Block.box(-4, 0, 4, 20, 16, 12));
	}

	public static DualCannonBodyBlock large(Properties properties, DualCannonMaterial material) {
		return new DualCannonBodyBlock(properties, material, () -> CannonCastShape.VERY_LARGE, Block.box(-6, 0, 2, 22, 16, 14), Shapes.block());
	}

	private DualCannonBodyBlock fromSelf(Properties properties) {
		return new DualCannonBodyBlock(properties, this.getCannonMaterial(), this.cannonShape, this.visualShapes.get(Direction.UP), this.collisionShapes.get(Direction.UP));
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

	@Override public DualCannonEnd getDefaultOpeningType() { return DualCannonEnd.OPEN; }

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override
	public Class<DualCannonBlockEntity> getBlockEntityClass() {
		return DualCannonBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends DualCannonBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.DUAL_CANNON_BODY.get();
	}

}
