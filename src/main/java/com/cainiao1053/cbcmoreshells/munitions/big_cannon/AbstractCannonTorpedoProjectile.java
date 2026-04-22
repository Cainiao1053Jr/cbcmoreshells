package com.cainiao1053.cbcmoreshells.munitions.big_cannon;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedTorpedoTubeContraption;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProjectilePropertiesComponent;
import com.mojang.math.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.ImpactExplosion;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import javax.annotation.Nonnull;

public abstract class AbstractCannonTorpedoProjectile extends AbstractCannonProjectile {

	private static final EntityDataAccessor<ItemStack> TRACER = SynchedEntityData.defineId(AbstractCannonTorpedoProjectile.class, EntityDataSerializers.ITEM_STACK);



	protected AbstractCannonTorpedoProjectile(EntityType<? extends AbstractCannonTorpedoProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void defineSynchedData() {
		super.defineSynchedData();
		this.entityData.define(TRACER, ItemStack.EMPTY);
	}

	@Override
	public void tick() {
		ChunkPos cpos = new ChunkPos(this.blockPosition());
		if (this.level().isClientSide || this.level().hasChunk(cpos.x, cpos.z)) {
			Vec3 oldPos = this.position();
			super.tick();
		}
	}


	public boolean hasTracer() {
		return (!this.getTracer().isEmpty() || CBCConfigs.server().munitions.allBigCannonProjectilesAreTracers.get()) && !this.isInGround();
	}

	public void setTracer(ItemStack stack) {
		this.entityData.set(TRACER, stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack);
	}

	public ItemStack getTracer() { return this.entityData.get(TRACER); }

	@Override
	protected Vec3 getForces(Vec3 position, Vec3 velocity) {
		return velocity.normalize().scale(-this.getDragForce()).add((double)0.0F, this.getGravity(), (double)0.0F);
	}

	@Override
	protected double getDefaultGravity() {
		double vel = this.getDeltaMovement().y;
		double damp = getBallisticProperties().drag();
		double waterDamp = 0;
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
		double gForce = this.getBallisticProperties().gravity();
		if (!fluidState.isEmpty()){
			gForce *= -getBigCannonProjectileProperties().buoyancyFactor();
			waterDamp = Math.max(damp,0) * vel;
		}
		gForce = gForce - waterDamp;
		return gForce;
	}

	@Override
	protected boolean onImpactFluid(ProjectileContext projectileContext, BlockState blockState, FluidState fluidState,
									Vec3 impactPos, BlockHitResult fluidHitResult){
		return false;
    }

	@Override
	protected double getDragForce() {
		BallisticPropertiesComponent properties = this.getBallisticProperties();
		double vel = this.getDeltaMovement().length();
		double ssVel = getBigCannonProjectileProperties().torpedoSpeed();
		double formDrag = properties.drag()/10;
		double drag = formDrag * vel;
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
		if (!fluidState.isEmpty()) {
			drag = 0.07 * (vel - ssVel);
		}


		return drag;
	}



	//Redo End

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		if (!this.getTracer().isEmpty())
			tag.put("Tracer", this.getTracer().save(new CompoundTag()));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.setTracer(tag.contains("Tracer", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("Tracer")) : ItemStack.EMPTY);
	}

	@Override
	protected void onTickRotate() {
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();

		if (!this.isInGround()) {
			Vec3 vel = this.getDeltaMovement();
			if (vel.lengthSqr() > 0.005d) {
				this.setYRot((float) (Mth.atan2(vel.x, vel.z) * (double) Constants.RAD_TO_DEG));
				this.setXRot((float) (Mth.atan2(vel.y, vel.horizontalDistance()) * (double) Constants.RAD_TO_DEG));
			}

			this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
			this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
		}
	}

	public abstract BlockState getRenderedBlockState();

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {
		BlockPos pos = blockHitResult.getBlockPos();
		Vec3 hitLoc = blockHitResult.getLocation();

		BallisticPropertiesComponent ballistics = this.getBallisticProperties();
		BlockArmorPropertiesProvider blockArmor = BlockArmorPropertiesHandler.getProperties(state);
		boolean unbreakable = projectileContext.griefState() == CBCCfgMunitions.GriefState.NO_DAMAGE || state.getDestroySpeed(this.level(), pos) == -1;

		Vec3 accel = this.getForces(this.position(), this.getDeltaMovement());
		Vec3 curVel = this.getDeltaMovement().add(accel);

		Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level(), blockHitResult);
		double incidence = Math.max(0, curVel.normalize().dot(normal.reverse()));
		double velMag = curVel.length();
		double mass = this.getProjectileMass();
		double bonusMomentum = 1 + Math.max(0, (velMag - CBCConfigs.server().munitions.minVelocityForPenetrationBonus.getF())
			* CBCConfigs.server().munitions.penetrationBonusScale.getF());
		double incidentVel = velMag * incidence;
		double momentum = mass * incidentVel * bonusMomentum;

		double toughness = blockArmor.toughness(this.level(), state, pos, true);
		double toughnessPenalty = toughness - momentum;
		double hardnessPenalty = blockArmor.hardness(this.level(), state, pos, true) - ballistics.penetration();
		double bounceBonus = Math.max(1 - hardnessPenalty, 0);

		double projectileDeflection = ballistics.deflection();
		double baseChance = CBCConfigs.server().munitions.baseProjectileBounceChance.getF();
		double bounceChance = projectileDeflection < 1e-2d || incidence > projectileDeflection ? 0 : Math.max(baseChance, 1 - incidence / projectileDeflection) * bounceBonus;

		boolean surfaceImpact = this.canHitSurface();
		boolean canBounce = CBCConfigs.server().munitions.projectilesCanBounce.get();
		boolean blockBroken = toughnessPenalty < 1e-2d && !unbreakable;
		ImpactResult.KinematicOutcome outcome;
		if (surfaceImpact && canBounce && this.level().getRandom().nextDouble() < bounceChance) {
			outcome = ImpactResult.KinematicOutcome.STOP;
		} else if (blockBroken && !this.level().isClientSide) {
			outcome = ImpactResult.KinematicOutcome.PENETRATE;
		} else {
			outcome = ImpactResult.KinematicOutcome.STOP;
		}
		boolean shatter = surfaceImpact && outcome != ImpactResult.KinematicOutcome.BOUNCE && hardnessPenalty > ballistics.toughness();
		float durabilityPenalty = ((float) Math.max(0, hardnessPenalty) + 1) * (float) toughness / (float) incidentVel;

		state.onProjectileHit(this.level(), state, blockHitResult, this);
		if (!this.level().isClientSide) {
			boolean bounced = false;
			Vec3 effectNormal;
//			if (bounced) {
//				double elasticity = 1.7f;
//				effectNormal = curVel.subtract(normal.scale(normal.dot(curVel) * elasticity));
//			} else {
				effectNormal = curVel.reverse();
//			}
			for (BlockState state1 : blockArmor.containedBlockStates(this.level(), state, pos.immutable(), true)) {
				projectileContext.addPlayedEffect(new ClientboundPlayBlockHitEffectPacket(state1, this.getType(), bounced, true,
					hitLoc.x, hitLoc.y, hitLoc.z, (float) effectNormal.x, (float) effectNormal.y, (float) effectNormal.z));
			}
		}
		if (blockBroken) {
			this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty, 0));
			this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), ProjectileBlock.UPDATE_ALL_IMMEDIATE);
			if (surfaceImpact) {
				float f = (float) toughness / (float) momentum;
				float overPenetrationPower = f < 0.15f ? 2 - 2 * f : 0;
				if (overPenetrationPower > 0 && outcome == ImpactResult.KinematicOutcome.PENETRATE)
					projectileContext.queueExplosion(pos, overPenetrationPower);
			}
		} else {
			if (outcome == ImpactResult.KinematicOutcome.STOP) {
				this.setProjectileMass(0);
			} else {
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty / 2f, 0));
			}
			Vec3 spallLoc = hitLoc.add(curVel.normalize().scale(2));
			if (!this.level().isClientSide) {
				ImpactExplosion explosion = new ImpactExplosion(this.level(), this, this.indirectArtilleryFire(false), spallLoc.x, spallLoc.y, spallLoc.z, 2, Level.ExplosionInteraction.NONE);
				CreateBigCannons.handleCustomExplosion(this.level(), explosion);
			}
			SoundType sound = state.getSoundType();
			if (!this.level().isClientSide)
				this.level().playSound(null, spallLoc.x, spallLoc.y, spallLoc.z, sound.getBreakSound(), SoundSource.BLOCKS,
					sound.getVolume(), sound.getPitch());
		}
		shatter |= this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		return new ImpactResult(outcome, shatter);
	}

	public void onShoot(MountedTorpedoTubeContraption contraption, ServerLevel level){}

	public BlockPos getBreechPos(MountedTorpedoTubeContraption contraption){
		Direction pushDirection = contraption.initialOrientation();
		Direction extractDirection = pushDirection.getOpposite();
		BlockPos startPos = contraption.getStartPos();
		BlockPos breechPos = startPos.relative(extractDirection);
		return breechPos;
	}

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.BIG_CANNON_PROJECTILE), this.getDamageProperties().ignoresEntityArmor());
	}

	public float addedChargePower() { return this.getBigCannonProjectileProperties().addedChargePower(); }
	public float minimumChargePower() { return this.getBigCannonProjectileProperties().minimumChargePower(); }
	public boolean canSquib() { return this.getBigCannonProjectileProperties().canSquib(); }
	public float addedRecoil() { return this.getBigCannonProjectileProperties().addedRecoil(); }
	public int getReloadTime(){
		return this.getBigCannonProjectileProperties().reloadTime();
	}
	public int getExplosionCooldown(){
		return this.getBigCannonProjectileProperties().explosionCooldown();
	}

	public float getBuoyancyFactor(){return this.getBigCannonProjectileProperties().buoyancyFactor();}
	public float getTorpedoSpeed(){return this.getBigCannonProjectileProperties().torpedoSpeed();}
	public float getTorpedoSpread(){return this.getBigCannonProjectileProperties().torpedoSpread();}



	@Nonnull protected abstract TorpedoProjectilePropertiesComponent getBigCannonProjectileProperties();

	public enum TrailType {
		NONE,
		LONG,
		SHORT
	}

}
