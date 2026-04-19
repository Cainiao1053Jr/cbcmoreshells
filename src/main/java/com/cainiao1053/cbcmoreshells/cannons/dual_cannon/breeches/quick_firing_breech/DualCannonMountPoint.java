package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech;

import java.util.HashSet;
import java.util.Set;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.IDualCannonBlockEntity;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.DualCannonProjectileBlock;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.kinetics.mechanicalArm.AllArmInteractionPointTypes;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class DualCannonMountPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

	public DualCannonMountPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
		super(type, level, pos, state);
	}

	@Override
	protected Vec3 getInteractionPositionVector() {
		BlockEntity be = this.getLevel().getBlockEntity(this.pos);
		if (be instanceof ExtendsCannonMount extendsCannonMount) {
			CannonMountBlockEntity mount = extendsCannonMount.getCannonMount();
			if (mount != null)
				mount.getInteractionLocation();
		}
		if (be instanceof FixedCannonMountBlockEntity mount)
			return mount.getInteractionLocation();
		return super.getInteractionPositionVector();
	}

	public ItemStack getInsertedResultAndDoSomething(ItemStack stack, boolean simulate, AbstractMountedCannonContraption cannon,
													 PitchOrientedContraptionEntity poce) {
		if (cannon instanceof MountedDualCannonContraption bigCannon) {
			return dualCannonInsert(stack, simulate, bigCannon, poce);
		}
		return stack;
	}


//	public static ItemStack dualCannonInsert(ItemStack stack, boolean simulate, MountedDualCannonContraption bigCannon,
//											  PitchOrientedContraptionEntity poce) {
//		if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof BigCannonMunitionBlock munition))
//			return stack;
//
//		Direction pushDirection = bigCannon.initialOrientation();
//		Direction extractDirection = pushDirection.getOpposite();
//		BlockPos startPos = bigCannon.getStartPos();
//		BlockPos breechPos = startPos.relative(extractDirection);
//		if (!(bigCannon.presentBlockEntities.get(breechPos) instanceof DualCannonQuickfiringBreechBlockEntity breech) || !breech.canBeAutomaticallyLoaded())
//			return stack;
//
//		//Magazine behavior
//		if(bigCannon.hasMagazine()){
//			ItemStackHandler magazine = bigCannon.getCachedMunition();
//			int maxSlots = magazine.getSlots();
//			for(int i = 0; i < maxSlots; i++){
//				ItemStack projectile = magazine.getStackInSlot(i);
//				if(!projectile.isEmpty()){
//					continue;
//				}
//				magazine.setStackInSlot(i, stack);
//				ItemStack copy = stack.copy();
//				copy.shrink(1);
//				float reloadTimeModifier = bigCannon.getReloadTimeModifier();
//				float reloadTimeCoef = ((DualCannonProjectileBlock<?>) munition).getProjectile(poce.level(), stack).getReloadTimeCoef();
//				breech.setLoadingCooldown(getLoadingCooldown(reloadTimeModifier*reloadTimeCoef));
//				return copy;
//			}
//			float reloadTimeModifier = bigCannon.getReloadTimeModifier();
//			float reloadTimeCoef = ((DualCannonProjectileBlock<?>) munition).getProjectile(poce.level(), stack).getReloadTimeCoef();
//			breech.setLoadingCooldown(getLoadingCooldown(reloadTimeModifier*reloadTimeCoef));
//			return stack;
//		}
//
//		BlockPos nextPos = startPos.relative(pushDirection);
//		int barrelLength = 0;
//		while (bigCannon.presentBlockEntities.get(nextPos) instanceof IDualCannonBlockEntity cbe2) {
//			StructureBlockInfo info = cbe2.cannonBehavior().block();
//			if (!info.state().isAir()) return stack;
//			nextPos = nextPos.relative(pushDirection);
//			++barrelLength;
//		}
//
//		BlockEntity be1 = bigCannon.presentBlockEntities.get(startPos);
//		if (!(be1 instanceof IDualCannonBlockEntity cbe1)) return stack;
//		StructureBlockInfo firstInfo = cbe1.cannonBehavior().block();
//
//		if (munition instanceof DualCannonProjectileBlock) {
//			if (barrelLength == 0) return stack;
//			if (!firstInfo.state().isAir() && bigCannon.getCannonMaterial().properties().isSingleBarrel()) { //add cannon material property here to separate single and dual
//				return stack;
//			}
//			if (!simulate) {
//				loadCartridge(stack, munition, poce, bigCannon);
//				float reloadTimeModifier = bigCannon.getReloadTimeModifier();
//				float reloadTimeCoef = ((DualCannonProjectileBlock<?>) munition).getProjectile(poce.level(), stack).getReloadTimeCoef();
//				breech.setLoadingCooldown(getLoadingCooldown(reloadTimeModifier*reloadTimeCoef));
//			}
//			ItemStack copy = stack.copy();
//			copy.shrink(1);
//			return copy;
//		}
//		return stack;
//	}

	public static ItemStack dualCannonInsert(ItemStack stack, boolean simulate, MountedDualCannonContraption bigCannon,
											 PitchOrientedContraptionEntity poce) {
		if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof BigCannonMunitionBlock munition))
			return stack;

		Direction pushDirection = bigCannon.initialOrientation();
		Direction extractDirection = pushDirection.getOpposite();
		BlockPos startPos = bigCannon.getStartPos();
		BlockPos breechPos = startPos.relative(extractDirection);
		if (!(bigCannon.presentBlockEntities.get(breechPos) instanceof DualCannonQuickfiringBreechBlockEntity breech) || !breech.canBeAutomaticallyLoaded())
			return stack;

		//Magazine behavior
		if (bigCannon.hasMagazine()) {
			ItemStackHandler magazine = bigCannon.getCachedMunition();
			int maxSlots = magazine.getSlots();

			for(int i = 0; i < maxSlots; ++i) {
				ItemStack projectile = magazine.getStackInSlot(i);
				if (projectile.isEmpty()) {
					if (!simulate) {
						ItemStack toStore = stack.copy();
						toStore.setCount(1);
						magazine.setStackInSlot(i, toStore);
						float reloadTimeModifier = bigCannon.getReloadTimeModifier();
						float reloadTimeCoef = ((DualCannonProjectileBlock<?>)munition).getProjectile(poce.level(), stack).getReloadTimeCoef();
						breech.setLoadingCooldown(getLoadingCooldown(reloadTimeModifier * reloadTimeCoef));
					}

					ItemStack copy = stack.copy();
					copy.shrink(1);
					return copy;
				}
			}

			return stack;
		}

		BlockPos nextPos = startPos.relative(pushDirection);
		int barrelLength = 0;
		while (bigCannon.presentBlockEntities.get(nextPos) instanceof IDualCannonBlockEntity cbe2) {
			StructureBlockInfo info = cbe2.cannonBehavior().block();
			if (!info.state().isAir()) return stack;
			nextPos = nextPos.relative(pushDirection);
			++barrelLength;
		}

		BlockEntity be1 = bigCannon.presentBlockEntities.get(startPos);
		if (!(be1 instanceof IDualCannonBlockEntity cbe1)) return stack;
		StructureBlockInfo firstInfo = cbe1.cannonBehavior().block();

		if (munition instanceof DualCannonProjectileBlock) {
			if (barrelLength == 0) return stack;
			if (!firstInfo.state().isAir() && bigCannon.getCannonMaterial().properties().isSingleBarrel()) { //add cannon material property here to separate single and dual
				return stack;
			}
			if (!simulate) {
				loadCartridge(stack, munition, poce, bigCannon);
				float reloadTimeModifier = bigCannon.getReloadTimeModifier();
				float reloadTimeCoef = ((DualCannonProjectileBlock<?>) munition).getProjectile(poce.level(), stack).getReloadTimeCoef();
				breech.setLoadingCooldown(getLoadingCooldown(reloadTimeModifier*reloadTimeCoef));
			}
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
		return stack;
	}

	public static ItemStack dualCannonInsertCustomized(ItemStack stack, boolean simulate, MountedDualCannonContraption bigCannon,
											 PitchOrientedContraptionEntity poce) {
		if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof BigCannonMunitionBlock munition))
			return stack;

		Direction pushDirection = bigCannon.initialOrientation();
		Direction extractDirection = pushDirection.getOpposite();
		BlockPos startPos = bigCannon.getStartPos();
		BlockPos breechPos = startPos.relative(extractDirection);
		float reloadTimeModifier = bigCannon.getReloadTimeModifier();
		if (!(bigCannon.presentBlockEntities.get(breechPos) instanceof DualCannonQuickfiringBreechBlockEntity breech) || !breech.canBeAutomaticallyLoaded())
			return stack;

		BlockPos nextPos = startPos.relative(pushDirection);
		int barrelLength = 0;
		while (bigCannon.presentBlockEntities.get(nextPos) instanceof IDualCannonBlockEntity cbe2) {
			StructureBlockInfo info = cbe2.cannonBehavior().block();
			if (!info.state().isAir()) return stack;
			nextPos = nextPos.relative(pushDirection);
			++barrelLength;
		}

		BlockEntity be1 = bigCannon.presentBlockEntities.get(startPos);
		if (!(be1 instanceof IDualCannonBlockEntity cbe1)) return stack;
		StructureBlockInfo firstInfo = cbe1.cannonBehavior().block();

		if (munition instanceof DualCannonProjectileBlock) {
			if (barrelLength == 0) return stack;
			if (!firstInfo.state().isAir() && bigCannon.getCannonMaterial().properties().isSingleBarrel()) { //add cannon material property here to separate single and dual
				return stack;
			}
			if (!simulate) {
				loadCartridge(stack, munition, poce, bigCannon);
				//breech.setLoadingCooldown(getLoadingCooldown(reloadTimeModifier));
			}
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
		return stack;
	}

	public static void loadProjectile(ItemStack stack, BigCannonMunitionBlock munition, AbstractContraptionEntity entity,
									  MountedDualCannonContraption bigCannon) {
		BlockPos startPos = bigCannon.getStartPos();
		Direction dir = bigCannon.initialOrientation();
		BlockEntity be = bigCannon.presentBlockEntities.get(startPos);
		IDualCannonBlockEntity cbe = (IDualCannonBlockEntity) be;
		cbe.cannonBehavior().removeBlock();
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir));
		DualCannonBlock.writeAndSyncSingleBlockData(be, bigCannon.getBlocks().get(startPos), entity, bigCannon);
	}

	public static void loadCartridge(ItemStack stack, BigCannonMunitionBlock munition, AbstractContraptionEntity entity,
									 MountedDualCannonContraption cannon) {
		Direction dir = cannon.initialOrientation();
		BlockPos startPos = cannon.getStartPos();

		Set<BlockPos> changes = new HashSet<>(2);
		changes.add(startPos);
		BlockEntity be = cannon.presentBlockEntities.get(startPos);
		IDualCannonBlockEntity cbe = (IDualCannonBlockEntity) be;

		StructureBlockInfo oldInfo = cbe.cannonBehavior().block();
		if (!oldInfo.state().isAir()) {
			BlockPos nextPos = startPos.relative(dir);
			BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
			IDualCannonBlockEntity cbe1 = (IDualCannonBlockEntity) be1;
			cbe1.cannonBehavior().loadBlock(oldInfo);
			cbe.cannonBehavior().removeBlock();
			changes.add(nextPos);
		}
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir));

		DualCannonBlock.writeAndSyncMultipleBlockData(changes, entity, cannon);
	}



	private static int getLoadingCooldown(float modifier) {
		return (int) (50*modifier);
	}

}
