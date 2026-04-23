//package com.cainiao1053.cbcmoreshells.blocks.dish_plate;
//
//import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
//import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
//import com.simibubi.create.content.kinetics.mechanicalArm.ArmBlockEntity;
//import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
//import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;
//import net.minecraft.core.BlockPos;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import org.slf4j.Logger;
//
//public class DishPlateInteractionPoint extends ArmInteractionPoint {
//
//	public DishPlateInteractionPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
//		super(type, level, pos, state);
//		this.mode = Mode.DEPOSIT;
//	}
//
//	Logger LOGGER = Cbcmoreshells.LOGGER;
//
//
//	@Override
//	public ItemStack extract(ArmBlockEntity armBlockEntity,int slot, boolean simulate) {
//		return ItemStack.EMPTY;
//	}
//
//	@Override
//	public void cycleMode() {
//	}
//
//	@Override
//	public ItemStack insert(ArmBlockEntity armBlockEntity, ItemStack stack, boolean simulate) {
////		if(!simulate) {
////			return stack;
////		}
//		//LOGGER.info("simulate: " + simulate);
//		DishPlateBlockEntity be = (DishPlateBlockEntity) level.getBlockEntity(pos);
//		if(!be.getBlockState().getValue(BlockStateProperties.POWERED)) {
//			return stack;
//		}
//		if(!be.getInventory().isItemValid(0,stack)){
//			return stack;
//		}
//		ItemStack insert = be.insertStack(stack,0, simulate);
//		if(!simulate){
//			BlockPos pos = be.getBlockPos();
//			level.playSound(
//					null,
//					pos.getX(),
//					pos.getY(),
//					pos.getZ(),
//					CBCMSSoundEvents.SERVICEBELL.getMainEvent(),
//					SoundSource.BLOCKS,
//					2.0f,
//					1f
//			);
//		}
////		ItemStack stack = be.getInventory().extractItem(slot,1,simulate);
////		if(stack.getItem() instanceof BigCartridgeBlockItem) {
////			return stack;
////		}
////		if(!(stack.getItem() instanceof ProjectileBlockItem)) {
////			return ItemStack.EMPTY;
////		}
////		if(!be.getInventory().isItemValid(slot, stack)) {
////			return ItemStack.EMPTY;
////		}
//		//be.unpower();
//		return insert;
//	}
//
////	@Override
////	public int getSlotCount() {
////		return 0;
////	}
//
//
//
//}
