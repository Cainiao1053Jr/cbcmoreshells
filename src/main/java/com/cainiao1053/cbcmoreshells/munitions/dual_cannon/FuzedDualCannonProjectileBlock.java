package com.cainiao1053.cbcmoreshells.munitions.dual_cannon;

import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.BlockHitResult;
import rbasamoyai.createbigcannons.index.CBCDataComponents;
import rbasamoyai.createbigcannons.index.CBCItems;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBlockEntity;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import java.util.List;

public abstract class FuzedDualCannonProjectileBlock<BLOCK_ENTITY extends FuzedBlockEntity, ENTITY extends FuzedDualCannonProjectile>
	extends DualCannonProjectileBlock<ENTITY> implements IBE<BLOCK_ENTITY> {

	protected FuzedDualCannonProjectileBlock(Properties properties) {
		super(properties);
	}

//	public static ItemStack getFuzeFromItemStack(ItemStack stack) {
//		return ItemStack.of(stack.getOrCreateTag().getCompound("BlockEntityTag").getCompound("Fuze"));
//	}

	public static ItemStack getFuzeFromItemStack(ItemStack stack) {
		ItemContainerContents items = stack.getOrDefault(CBCDataComponents.FUZE, ItemContainerContents.EMPTY);
		return items.copyOne();
	}

	@Override
	public AbstractDualCannonProjectile getProjectile(Level level, List<StructureBlockInfo> projectileBlocks) {
		FuzedDualCannonProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromBlocks(projectileBlocks, level.registryAccess()));
		projectile.setFuze(getFuzeFromBlocks(projectileBlocks, level.registryAccess()));
		return projectile;
	}

	@Override
	public AbstractDualCannonProjectile getProjectile(Level level, ItemStack itemStack) {
		FuzedDualCannonProjectile projectile = this.getAssociatedEntityType().create(level);
		projectile.setTracer(getTracerFromItemStack(itemStack));
		projectile.setFuze(getFuzeFromItemStack(itemStack));
		return projectile;
	}

//	protected static ItemStack getFuzeFromBlocks(List<StructureBlockInfo> blocks) {
//		if (blocks.isEmpty()) return ItemStack.EMPTY;
//		StructureBlockInfo info = blocks.get(0);
//		if (info.nbt() == null) return ItemStack.EMPTY;
//		BlockEntity load = BlockEntity.loadStatic(info.pos(), info.state(), info.nbt());
//		return load instanceof FuzedBlockEntity fuzed ? fuzed.getItem(1) : ItemStack.EMPTY;
//	}
//
//	public ItemStack getFuzeFromBlock(Level level, BlockPos pos) {
//		BlockEntity be = level.getBlockEntity(pos);
//		if (be instanceof FuzedBlockEntity fuzed) {
//			return fuzed.getItem(1);
//		}
//		return ItemStack.EMPTY;
//	}

	protected static ItemStack getFuzeFromBlocks(List<StructureBlockInfo> blocks, HolderLookup.Provider registries) {
		if (blocks.isEmpty()) return ItemStack.EMPTY;
		StructureBlockInfo info = blocks.get(0);
		if (info.nbt() == null) return ItemStack.EMPTY;
		Tag tag = info.nbt().getCompound("components").get("createbigcannons:fuze");
		ItemContainerContents contents = ItemContainerContents.CODEC
				.parse(registries.createSerializationContext(NbtOps.INSTANCE), tag)
				.resultOrPartial()
				.orElse(ItemContainerContents.EMPTY);
		return contents.getSlots() > 0 ? contents.getStackInSlot(0) : ItemStack.EMPTY;
	}

	public static ItemStack getFuzeFromBlock(Level level, BlockPos pos, BlockState state) {
		return level.getBlockEntity(pos) instanceof FuzedBlockEntity projectile ? projectile.getFuze() : ItemStack.EMPTY;
	}

//	@Override
//	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
//		if (hand == InteractionHand.OFF_HAND)
//			return InteractionResult.PASS;
//		FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
//		if (fuzedBlock == null)
//			return InteractionResult.PASS;
//		ItemStack stack = player.getItemInHand(hand);
//		Direction fuzeFace = state.getValue(FACING);
//		if (this.isBaseFuze())
//			fuzeFace = fuzeFace.getOpposite();
//		if (stack.isEmpty()) {
//			int slot;
//			if (!fuzedBlock.getItem(0).isEmpty()) {
//				slot = 0;
//			} else if (result.getDirection() == fuzeFace && !fuzedBlock.getItem(1).isEmpty()) {
//				slot = 1;
//			} else {
//				return InteractionResult.PASS;
//			}
//			if (!level.isClientSide) {
//				ItemStack resultStack = fuzedBlock.removeItem(slot, 1);
//				if (!player.addItem(resultStack) && !player.isCreative()) {
//					ItemEntity item = player.drop(resultStack, false);
//					if (item != null) {
//						item.setNoPickUpDelay();
//						item.setTarget(player.getUUID());
//					}
//				}
//				fuzedBlock.notifyUpdate();
//			}
//			level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
//			return InteractionResult.sidedSuccess(level.isClientSide);
//		} else {
//			int slot;
//			if (CBCItems.TRACER_TIP.isIn(stack)) {
//				slot = 0;
//			} else if (stack.getItem() instanceof FuzeItem && result.getDirection() == fuzeFace) {
//				slot = 1;
//			} else {
//				return InteractionResult.PASS;
//			}
//			if (!fuzedBlock.getItem(slot).isEmpty())
//				return InteractionResult.PASS;
//			if (!level.isClientSide) {
//				ItemStack copy = player.getAbilities().instabuild ? stack.copy() : stack.split(1);
//				copy.setCount(1);
//				fuzedBlock.setItem(slot, copy);
//				fuzedBlock.notifyUpdate();
//			}
//			level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
//			return InteractionResult.sidedSuccess(level.isClientSide);
//		}
//	}

	@Override
	public ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (hand == InteractionHand.OFF_HAND)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
		if (fuzedBlock == null)
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		Direction fuzeFace = state.getValue(FACING);
		if (this.isBaseFuze())
			fuzeFace = fuzeFace.getOpposite();
		int slot;
		if (CBCItems.TRACER_TIP.isIn(stack)) {
			slot = 0;
		} else if (stack.getItem() instanceof FuzeItem && hitResult.getDirection() == fuzeFace) {
			slot = 1;
		} else {
			return stack.isEmpty() ? ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION : ItemInteractionResult.FAIL;
		}
		if (!fuzedBlock.getItem(slot).isEmpty())
			return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
		if (!level.isClientSide) {
			ItemStack copy = player.getAbilities().instabuild ? stack.copy() : stack.split(1);
			copy.setCount(1);
			fuzedBlock.setItem(slot, copy);
			fuzedBlock.notifyUpdate();
			if (!level.getBlockTicks().willTickThisTick(pos, this)) {
				level.scheduleTick(pos, this, 0);
			}
		}
		level.playSound(null, pos, SoundEvents.ITEM_FRAME_ADD_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
		return ItemInteractionResult.sidedSuccess(level.isClientSide);
	}

	@Override
	public InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
		FuzedBlockEntity fuzedBlock = this.getBlockEntity(level, pos);
		if (fuzedBlock == null)
			return InteractionResult.PASS;
		Direction fuzeFace = state.getValue(FACING);
		if (this.isBaseFuze())
			fuzeFace = fuzeFace.getOpposite();
		int slot;
		if (!fuzedBlock.getItem(0).isEmpty()) {
			slot = 0;
		} else if (hitResult.getDirection() == fuzeFace && !fuzedBlock.getItem(1).isEmpty()) {
			slot = 1;
		} else {
			return InteractionResult.PASS;
		}
		if (!level.isClientSide) {
			ItemStack resultStack = fuzedBlock.removeItem(slot, 1);
			if (!player.addItem(resultStack) && !player.isCreative()) {
				ItemEntity item = player.drop(resultStack, false);
				if (item != null) {
					item.setNoPickUpDelay();
					item.setTarget(player.getUUID());
				}
			}
			fuzedBlock.notifyUpdate();
			if (!level.getBlockTicks().willTickThisTick(pos, this)) {
				level.scheduleTick(pos, this, 0);
			}
		}
		level.playSound(player, pos, SoundEvents.ITEM_FRAME_REMOVE_ITEM, SoundSource.NEUTRAL, 1.0f, 1.0f);
		return InteractionResult.sidedSuccess(level.isClientSide);
	}

//	@Override
//	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbour, boolean p_60514_) {
//		super.neighborChanged(state, level, pos, block, neighbour, p_60514_);
//		if (!level.isClientSide()) {
//			if(level.hasNeighborSignal(pos) && directDrop()){
//				FuzedDualCannonProjectile projectile = (FuzedDualCannonProjectile) this.getProjectile(level,pos,state);
//				projectile.setFuze(getFuzeFromBlock(level,pos));
//				projectile.setLifetime(getLifetimeFromBlock()-15);
//				Vec3 initVel = getInitialVelDir(state);
//				projectile.setPos(pos.getX() + 1.5*initVel.x,pos.getY(),pos.getZ() + 1.5*initVel.z);
//				projectile.shoot(initVel.x, initVel.y - 0.1 + getVerticalInitVelRefine(), initVel.z, 0.2f + extraInitVel(), 1.5f);
//				level.addFreshEntity(projectile);
//				playSoundForLaunching(level, pos);
//				level.setBlock(pos, Blocks.AIR.defaultBlockState(),2);
//			}
//		}
//	}

//	public Vec3 getInitialVelDir(BlockState state){
//		switch (state.getValue(FACING)) {
//			case NORTH:
//				return new Vec3(0, 0, -1);
//			case SOUTH:
//				return new Vec3(0, 0, 1);
//			case EAST:
//				return new Vec3(1, 0, 0);
//			case WEST:
//				return new Vec3(-1, 0, 0);
//			case UP:
//				return new Vec3(0, 1, 0);
//			case DOWN:
//				return new Vec3(0, -1, 0);
//		}
//		return new Vec3(0, 0, 1);
//	}

//	public int getLifetimeFromBlock(){
//		return 30;
//	}




	public boolean directDrop(){
		return true;
	}

	public abstract boolean isBaseFuze();

}
