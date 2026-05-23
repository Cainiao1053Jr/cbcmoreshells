package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonIncendiaryProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonPropertiesComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class NormalIncendiaryHEShellProjectile extends FuzedDualCannonProjectile {

	public NormalIncendiaryHEShellProjectile(EntityType<? extends NormalIncendiaryHEShellProjectile> type, Level level) {
		super(type, level);
	}

	//BlockPos hitBlockPos;

	@Override
	protected void detonate(Position position) {
		float fireChance = getAllProperties().incendiary().fireChance() * ((this.getDurabilityModifier()-1)/0.9f+1);
		int fireRange =(int) (getAllProperties().incendiary().fireRange() * ((this.getDurabilityModifier()-1)/1.3f+1));
		float explosivePower = this.getAllProperties().explosion().explosivePower()*((this.getDurabilityModifier()-1)/1.2f+1);
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		if(Math.random() > fireChance) {
			return;
		}
		spawnFire(new BlockPos((int)position.x(), (int)position.y(), (int)position.z()), this.level(), fireRange);
		AABB currentMovementRegion = this.getBoundingBox()
				.inflate(3);
		CBCMSCompatTransformers.spawnFireOnSublevel(level(), currentMovementRegion, fireRange, new Vec3(position.x(),
				position.y(), position.z()));
//		var shipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) this.level());
//		for(var ship :shipWorldCore.getLoadedShips()){
//			AABBdc shipABdc = ship.getWorldAABB();
//			AABB shipAABB = toAABB(shipABdc);
//			if(currentMovementRegion.intersects(shipAABB)){
//				//return true;
//				Vector3d incPos = ship.getWorldToShip().transformPosition(new Vector3d(position.x(), position.y(), position.z()));
//				BlockPos incBp = new BlockPos((int)incPos.x, (int)incPos.y, (int)incPos.z);
//				spawnFire(incBp, this.level(), fireRange);
//			}
//		}
		//if(hitBlockPos != null) {}
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.NORMAL_INCENDIARY_HE_SHELL_BLOCK.getDefaultState();
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Override
	public void setChargePower(float power) {
	}

//	@Override
//	protected void onHitBlock(BlockHitResult result) {
//		hitBlockPos = result.getBlockPos();
//		super.onHitBlock(result);
//	}

	public static void spawnFire(BlockPos root, Level level, int radius) {
		float chance = FluidBlobBurst.getBlockAffectChance();
		if (chance == 0)
			return;
		AABB bounds = new AABB(root).inflate(radius);
		BlockPos pos1 = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
		BlockPos pos2 = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
		for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
			if (level.getRandom().nextFloat() > chance)
				continue;
			BlockState state = level.getBlockState(pos);
			if (level.isEmptyBlock(pos)) {
				level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
			} else if (CandleBlock.canLight(state) || CampfireBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
				level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
				level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
				level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
			}
		}
	}

//	public static AABB toAABB(AABBdc i) {
//		return new AABB(
//				i.minX(), i.minY(), i.minZ(),
//				i.maxX(), i.maxY(), i.maxZ()
//		);
//	}


	@Nonnull
	@Override
	protected DualCannonPropertiesComponent getDualCannonProjectileProperties() {
		return this.getAllProperties().dualCannonProperties();
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	protected DualCannonIncendiaryProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.DUAL_CANNON_INCENDIARY_PROPERTIES.getPropertiesOf(this);
	}

}
