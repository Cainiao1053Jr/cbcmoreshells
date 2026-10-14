package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
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
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;

/** A barrel section; the one right in front of the breech holds the loaded package. */
public class PackageSliderBodyBlock extends PackageSliderBaseBlock implements IBE<PackageSliderBlockEntity> {

	private final VoxelShaper shapes;
	private final MapCodec<? extends DirectionalBlock> codec;

	public PackageSliderBodyBlock(Properties properties, PackageSliderMaterial material, VoxelShape shape) {
		super(properties, material);
		this.shapes = new AllShapes.Builder(shape).forDirectional();
		this.codec = simpleCodec(this::fromSelf);
	}

	public static PackageSliderBodyBlock barrel(Properties properties, PackageSliderMaterial material) {
		return new PackageSliderBodyBlock(properties, material, Block.box(1, 0, 10, 15, 16, 16));
	}

	private PackageSliderBodyBlock fromSelf(Properties properties) {
		return new PackageSliderBodyBlock(properties, this.getCannonMaterial(), this.shapes.get(Direction.UP));
	}

	@Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.shapes.get(this.getFacing(state));
	}

	@Override public PackageSliderEnd getDefaultOpeningType() { return PackageSliderEnd.OPEN; }

	@Override public CannonCastShape getCannonShape() { return CannonCastShape.MEDIUM; }

	@Override
	public Class<PackageSliderBlockEntity> getBlockEntityClass() {
		return PackageSliderBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends PackageSliderBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.PACKAGE_SLIDER_BODY.get();
	}

}
