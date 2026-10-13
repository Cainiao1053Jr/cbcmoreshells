package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSIncendiaryEffectService;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonIncendiaryProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonPropertiesComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
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
		float explosivePower = this.getAllProperties().explosion().blockDamagePower()*((this.getDurabilityModifier()-1)/1.2f+1);
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		if(Math.random() > fireChance) {
			return;
		}
		CBCMSIncendiaryEffectService.spawnFire(BlockPos.containing(position), this.level(), fireRange);
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
