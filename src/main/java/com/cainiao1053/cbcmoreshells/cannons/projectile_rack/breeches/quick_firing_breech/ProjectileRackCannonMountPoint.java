package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech;

import java.util.HashSet;
import java.util.Set;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedProjectileRackContraption;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.IProjectileRackBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlock;
//import com.cainiao1053.cbcmoreshells.munitions.big_cannon.TorpedoProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.RackedProjectileBlock;
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
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.ExtendsCannonMount;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
//import rbasamoyai.createbigcannons.cannon_control.contraption.MountedAutocannonContraption;
//import rbasamoyai.createbigcannons.cannon_control.contraption.MountedBigCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
//import rbasamoyai.createbigcannons.cannons.autocannon.breech.AbstractAutocannonBreechBlockEntity;
//import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
//import rbasamoyai.createbigcannons.cannons.big_cannons.IBigCannonBlockEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
//import rbasamoyai.createbigcannons.munitions.autocannon.ammo_container.AutocannonAmmoContainerItem;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
//import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCartridgeBlockItem;

public class ProjectileRackCannonMountPoint extends AllArmInteractionPointTypes.DepositOnlyArmInteractionPoint {

	public ProjectileRackCannonMountPoint(ArmInteractionPointType type, Level level, BlockPos pos, BlockState state) {
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
		if (cannon instanceof MountedProjectileRackContraption bigCannon) {
			return projectileRackInsert(stack, simulate, bigCannon, poce);
		}
		return stack;
	}


	public static ItemStack projectileRackInsert(ItemStack stack, boolean simulate, MountedProjectileRackContraption bigCannon,
											  PitchOrientedContraptionEntity poce) {
		if (!(stack.getItem() instanceof BlockItem blockItem) || !(blockItem.getBlock() instanceof BigCannonMunitionBlock munition))
			return stack;

		Direction pushDirection = bigCannon.initialOrientation();
		Direction extractDirection = pushDirection.getOpposite();
		BlockPos startPos = bigCannon.getStartPos();
		BlockPos breechPos = startPos.relative(extractDirection);
		if (!(bigCannon.presentBlockEntities.get(breechPos) instanceof ProjectileRackQuickfiringBreechBlockEntity breech) || !breech.canBeAutomaticallyLoaded())
			return stack;

		BlockPos nextPos = startPos.relative(pushDirection);
		int barrelLength = 0;
		while (bigCannon.presentBlockEntities.get(nextPos) instanceof IProjectileRackBlockEntity cbe2) {
			StructureBlockInfo info = cbe2.cannonBehavior().block();
			if (!info.state().isAir()) return stack;
			nextPos = nextPos.relative(pushDirection);
			++barrelLength;
		}

		BlockEntity be1 = bigCannon.presentBlockEntities.get(startPos);
		if (!(be1 instanceof IProjectileRackBlockEntity cbe1)) return stack;
		StructureBlockInfo firstInfo = cbe1.cannonBehavior().block();

		if (munition instanceof RackedProjectileBlock<?> rpb) {
			if (barrelLength == 0) return stack;
			if (firstInfo.state().getBlock() instanceof BigCartridgeBlock cartridge) {
				if (!simulate) {
					loadProjectile(stack, munition, poce, bigCannon);
					breech.setLoadingCooldown(rpb.getProjectile(poce.level(), stack).getReloadTime()); //getLoadingCooldown()
				}
				if (BigCartridgeBlock.getPowerFromData(firstInfo) == 0) {
					if (simulate) stack.setCount(1);
					return cartridge.getExtractedItem(firstInfo, poce.level().registryAccess());
				} else {
					return stack;
				}
			}
			if (!firstInfo.state().isAir()) return stack;
			if (!simulate) {
				loadProjectile(stack, munition, poce, bigCannon);
				breech.setLoadingCooldown(rpb.getProjectile(poce.level(), stack).getReloadTime()); //getLoadingCooldown()
			}
			ItemStack copy = stack.copy();
			copy.shrink(1);
			return copy;
		}
//		if (munition instanceof BigCartridgeBlock) {
//			if (BigCartridgeBlockItem.getPower(stack) == 0 || !(firstInfo.state().getBlock() instanceof RackedProjectileBlock))
//				return stack;
//			if (!simulate) {
//				loadCartridge(stack, munition, poce, bigCannon);
//				breech.setLoadingCooldown(getLoadingCooldown());
//			}
//			ItemStack copy = stack.copy();
//			copy.shrink(1);
//			return copy;
//		}
		return stack;
	}

	public static void loadProjectile(ItemStack stack, BigCannonMunitionBlock munition, AbstractContraptionEntity entity,
									  MountedProjectileRackContraption bigCannon) {
		BlockPos startPos = bigCannon.getStartPos();
		Direction dir = bigCannon.initialOrientation();
		BlockEntity be = bigCannon.presentBlockEntities.get(startPos);
		IProjectileRackBlockEntity cbe = (IProjectileRackBlockEntity) be;
		cbe.cannonBehavior().removeBlock();
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir, entity.level().registryAccess()));
		ProjectileRackBlock.writeAndSyncSingleBlockData(be, bigCannon.getBlocks().get(startPos), entity, bigCannon);
	}

	public static void loadCartridge(ItemStack stack, BigCannonMunitionBlock munition, AbstractContraptionEntity entity,
									 MountedProjectileRackContraption cannon) {
		Direction dir = cannon.initialOrientation();
		BlockPos startPos = cannon.getStartPos();

		Set<BlockPos> changes = new HashSet<>(2);
		changes.add(startPos);

		BlockEntity be = cannon.presentBlockEntities.get(startPos);
		IProjectileRackBlockEntity cbe = (IProjectileRackBlockEntity) be;

		StructureBlockInfo oldInfo = cbe.cannonBehavior().block();
		if (!oldInfo.state().isAir()) {
			BlockPos nextPos = startPos.relative(dir);
			BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
			IProjectileRackBlockEntity cbe1 = (IProjectileRackBlockEntity) be1;
			cbe1.cannonBehavior().loadBlock(oldInfo);
			cbe.cannonBehavior().removeBlock();
			changes.add(nextPos);
		}
		cbe.cannonBehavior().tryLoadingBlock(munition.getHandloadingInfo(stack, startPos, dir, entity.level().registryAccess()));

		ProjectileRackBlock.writeAndSyncMultipleBlockData(changes, entity, cannon);
	}



	private static int getLoadingCooldown() {
		return CBCConfigs.server().cannons.quickfiringBreechLoadingCooldown.get()*2+100;
	}

}
