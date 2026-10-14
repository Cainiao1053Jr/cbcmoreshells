package com.cainiao1053.cbcmoreshells.cannons.package_slider;

import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.placement.PackageSliderPlacementHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.material.PushReaction;

public abstract class PackageSliderBaseBlock extends DirectionalBlock implements PackageSliderBlock {

	private final PackageSliderMaterial material;

	protected PackageSliderBaseBlock(Properties properties, PackageSliderMaterial material) {
		super(properties.pushReaction(PushReaction.BLOCK));
		this.material = material;
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(FACING);
		super.createBlockStateDefinition(builder);
	}

	@Override public PackageSliderMaterial getCannonMaterial() { return this.material; }
	@Override public Direction getFacing(BlockState state) { return state.getValue(FACING); }

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos,
											  Player player, InteractionHand hand, BlockHitResult hitResult) {
		ItemInteractionResult result = PackageSliderPlacementHelper.tryAssistedPlacement(stack, state, level, pos, player,
			hand, hitResult);
		if (result != ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION) return result;
		return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
	}

	@Override
	public BlockState playerWillDestroy(Level level, BlockPos pos, BlockState state, Player player) {
		if (!level.isClientSide) this.playerWillDestroyPackageSlider(level, pos, state);
		return super.playerWillDestroy(level, pos, state, player);
	}

	@Override public BlockState rotate(BlockState state, Rotation rotation) { return state.setValue(FACING, rotation.rotate(state.getValue(FACING))); }
	@Override public BlockState mirror(BlockState state, Mirror mirror) { return state.setValue(FACING, mirror.mirror(state.getValue(FACING))); }

}
