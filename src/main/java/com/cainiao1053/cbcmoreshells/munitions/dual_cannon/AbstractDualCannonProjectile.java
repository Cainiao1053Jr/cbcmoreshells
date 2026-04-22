package com.cainiao1053.cbcmoreshells.munitions.dual_cannon;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonPropertiesComponent;
import com.cainiao1053.cbcmoreshells.network.CBCMSNetworkImpl;
import com.cainiao1053.cbcmoreshells.network.ClientboundCBCMSSplashPacket;
import com.cainiao1053.cbcmoreshells.network.ClientboundCBCMSTrailPacket;
import com.mojang.math.Constants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.CBCClientCommon;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.ImpactExplosion;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class AbstractDualCannonProjectile extends AbstractCannonProjectile {

	private static final EntityDataAccessor<ItemStack> TRACER = SynchedEntityData.defineId(AbstractDualCannonProjectile.class, EntityDataSerializers.ITEM_STACK);

	protected float durabilityMassModifier = 1f;
	protected AbstractDualCannonProjectile(EntityType<? extends AbstractDualCannonProjectile> type, Level level) {
		super(type, level);
		//super.setProjectileMass(durabilityMassModifier*this.getBallisticProperties().durabilityMass());
	}

	protected int age = 0;
	protected int maxAge;
	protected double elasticity = 1.7;

	private int sendTrail = 20;
	private double xOldT;
	private double yOldT;
	private double zOldT;
	private int traced = 0;

	private MountedDualCannonContraption contraption;
	private boolean hitTarget = false;

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
			if (!this.isInGround()) {
				TrailType trailType = TrailType.SHORT;
				if (trailType != TrailType.NONE) {
					int lifetime = trailType == TrailType.SHORT ? 100 : 280 + this.level().random.nextInt(50);
					ParticleOptions options = new TrailSmokeParticleData(lifetime);
					for (int i = 0; i < 10; ++i) {
						double partial = i * 0.1f;
						double dx = Mth.lerp(partial, this.xOld, this.getX());
						double dy = Mth.lerp(partial, this.yOld, this.getY());
						double dz = Mth.lerp(partial, this.zOld, this.getZ());
						double sx = this.level().random.nextDouble() * 0.004d - 0.002d;
						double sy = this.level().random.nextDouble() * 0.004d - 0.002d;
						double sz = this.level().random.nextDouble() * 0.004d - 0.002d;
						this.level().addAlwaysVisibleParticle(options, true, dx, dy, dz, sx, sy, sz);
					}
				}
				Vec3 newPos = this.position();
				if (this.level().isClientSide && this.localSoundCooldown == 0) {
					Vec3 displacement = newPos.subtract(oldPos);
					double dispLen = displacement.length();
					Vec3 originPos = newPos.subtract(displacement.scale(0.5));
					double radius = Math.min(200, dispLen * 30);
					EnvExecute.executeOnClient(() -> () -> CBCClientCommon.playShellFlyingSoundOnClient(this,
						CBCSoundEvents.SHELL_FLYING.getMainEvent(), player -> {
							if (!CBCConfigs.CLIENT.enableBigCannonFlybySounds.get())
								return false;
							if (player.distanceToSqr(originPos) > radius * radius)
								return false;
							Vec3 diff = player.position().subtract(originPos);
							return displacement.normalize().dot(diff.normalize()) >= 0;
						}, radius));
				}
			}
		}
		if (!this.level().isClientSide) {
			if (sendTrail<0) { //send client date for trail, copied from mixin for ABCP
				if(traced == 1){
					if (!this.isInGround() && !this.isInWater()) {
						if (!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel) {
							for (ServerPlayer players : serverLevel.players()) {
								sendTrailToClient(this.getX(), this.getY(), this.getZ(), xOldT, yOldT, zOldT, players);
							}
							sendTrail = 5;
							xOldT = this.getX();
							yOldT = this.getY();
							zOldT = this.getZ();
						}
					}else{
						if(!this.level().isClientSide && this.level() instanceof ServerLevel serverLevel){
							double xLerp = Mth.lerp(0.75,this.getX(),xOldT);
							double yLerp = Mth.lerp(0.75,this.getY(),yOldT);
							double zLerp = Mth.lerp(0.75,this.getZ(),zOldT);
							if(this.isInWater()){
								for (ServerPlayer players : serverLevel.players()) {
									sendSplashToClient(xLerp, yLerp, zLerp, xOldT, yOldT, zOldT, players);
								}
							}else{
								for (ServerPlayer players : serverLevel.players()) {
									sendTrailToClient(xLerp, yLerp, zLerp, xOldT, yOldT, zOldT, players);
								}
							}
							traced++;
							sendTrail = 100;
						}
					}
				}else if(traced == 0){
					xOldT = this.getX();
					yOldT = this.getY();
					zOldT = this.getZ();
					traced++;
					sendTrail = 5;
				}else{sendTrail = 200;}
			}else{sendTrail--;}
			this.age++;
			if (this.age > maxAge)
				this.discard();
		}
	}


	public boolean hasTracer() {
		return (!this.getTracer().isEmpty() || CBCConfigs.SERVER.munitions.allBigCannonProjectilesAreTracers.get()) && !this.isInGround();
	}

	public void setTracer(ItemStack stack) {
		this.entityData.set(TRACER, stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack);
	}

	public ItemStack getTracer() { return this.entityData.get(TRACER); }

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

	public void sendTrailToClient(double x, double y , double z, double xOld, double yOld , double zOld, ServerPlayer player) {
		CBCMSNetworkImpl.sendToClientPlayer(new ClientboundCBCMSTrailPacket(x,y,z,xOld,yOld,zOld), player);
	}

	public void sendSplashToClient(double x, double y ,double z,double xOld, double yOld ,double zOld,ServerPlayer player) {
		CBCMSNetworkImpl.sendToClientPlayer(new ClientboundCBCMSSplashPacket(x,y,z,xOld,yOld,zOld), player);
	}

	public abstract BlockState getRenderedBlockState();

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {
		BlockPos pos = blockHitResult.getBlockPos();
		Vec3 hitLoc = blockHitResult.getLocation();

		reduceCooldownOnHit(pos);

		BallisticPropertiesComponent ballistics = this.getBallisticProperties();
		BlockArmorPropertiesProvider blockArmor = BlockArmorPropertiesHandler.getProperties(state);
		//boolean unbreakable = projectileContext.griefState() == CBCCfgMunitions.GriefState.NO_DAMAGE || state.getDestroySpeed(this.level(), pos) == -1;

		Vec3 accel = this.getForces(this.position(), this.getDeltaMovement());
		Vec3 curVel = this.getDeltaMovement().add(accel);

		Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level(), blockHitResult);
		double incidence = Math.max(0, curVel.normalize().dot(normal.reverse()));
		double velMag = curVel.length();
		double mass = this.getProjectileMass();
		double bonusMomentum = 1 + Math.max(0, (velMag - 2f)
			* 0.15f);
		double incidentVel = velMag * incidence;
		double rawMomentum = mass * bonusMomentum * velMag;
		double excessMomentum = 0;
		double maximumMomentum = getMaximumMomentum();
		if(rawMomentum > maximumMomentum) {
			rawMomentum = maximumMomentum;
			excessMomentum = rawMomentum - maximumMomentum;
		}
		//double momentum = mass * incidentVel * bonusMomentum;
		double momentum = rawMomentum * incidence;

		double toughness = blockArmor.toughness(this.level(), state, pos, true);

		double projectileDeflection = ballistics.deflection();
		double baseChance = CBCConfigs.SERVER.munitions.baseProjectileBounceChance.getF();
		double bounceChance = projectileDeflection < 1e-2d || incidence > projectileDeflection ? 0 : Math.max(0, 1 - incidence / projectileDeflection + baseChance);

		boolean penetrate = false;
		if(momentum>toughness*2){
			penetrate = true;
		}else if(momentum > toughness*0.5){
			double penetrateChance = (momentum/toughness-0.15)/2;
			if(this.level().getRandom().nextDouble()<penetrateChance){
				penetrate = true;
			}
		}

		boolean surfaceImpact = this.canHitSurface();
		ImpactResult.KinematicOutcome outcome;
        if (!this.level().isClientSide && (penetrate || this.getSmashToughness()>toughness)) {
			outcome = ImpactResult.KinematicOutcome.PENETRATE;
		} else if (surfaceImpact && incidence<=projectileDeflection && this.level().getRandom().nextDouble() < bounceChance) {
			outcome = ImpactResult.KinematicOutcome.BOUNCE;
		} else {
			outcome = ImpactResult.KinematicOutcome.STOP;
		}
		float durabilityPenalty = (float) toughness / (float) incidentVel + (float)excessMomentum*1.5f;

		state.onProjectileHit(this.level(), state, blockHitResult, this);
		if (!this.level().isClientSide) {
			boolean bounced = outcome == ImpactResult.KinematicOutcome.BOUNCE;
			Vec3 effectNormal;
			if (bounced) {
				double elasticity = 1.7f;
				effectNormal = curVel.subtract(normal.scale(normal.dot(curVel) * elasticity));
			} else {
				effectNormal = curVel.reverse();
			}
			for (BlockState state1 : blockArmor.containedBlockStates(this.level(), state, pos.immutable(), true)) {
				projectileContext.addPlayedEffect(new ClientboundPlayBlockHitEffectPacket(state1, this.getType(), bounced, true,
					hitLoc.x, hitLoc.y, hitLoc.z, (float) effectNormal.x, (float) effectNormal.y, (float) effectNormal.z));
			}
		}
		if (outcome == ImpactResult.KinematicOutcome.PENETRATE) {
			if(momentum>toughness*3){
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty*1.3f, 0));
			}else if (momentum>toughness*2){
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty*1.08f, 0));
			} else{
				this.setProjectileMass(0.1f);
			}
			this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), DualCannonProjectileBlock.UPDATE_ALL_IMMEDIATE);
		} else {
			if (outcome == ImpactResult.KinematicOutcome.STOP) {
				this.setProjectileMass(0);
			} else {
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty/3f, 0));
			}
			Vec3 spallLoc = hitLoc.add(curVel.normalize().scale(2));
			if (!this.level().isClientSide) {
				ImpactExplosion explosion = new ImpactExplosion(this.level(), this, this.indirectArtilleryFire(false), spallLoc.x, spallLoc.y, spallLoc.z, 1, Level.ExplosionInteraction.NONE);
				CreateBigCannons.handleCustomExplosion(this.level(), explosion);
			}
			SoundType sound = state.getSoundType();
			if (!this.level().isClientSide)
				this.level().playSound(null, spallLoc.x, spallLoc.y, spallLoc.z, sound.getBreakSound(), SoundSource.BLOCKS,
					sound.getVolume(), sound.getPitch());
		}
		boolean shatter = false;
		shatter |= this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		return new ImpactResult(outcome, shatter);
	}

	public static double getHitNearbyAverageToughness(Level level, BlockPos pos, Direction face) {
		double accumulatedArmor = 0;
		for (int depth = 0; depth < 3; depth++) {
			for (int a = -1; a <= 1; a++) {
				for (int b = -1; b <= 1; b++) {
					BlockPos targetPos;

					switch (face) {
						case UP -> targetPos = pos.offset(a, -depth, b);
						case DOWN -> targetPos = pos.offset(a, depth, b);
						case EAST -> targetPos = pos.offset(-depth, a, b);
						case WEST -> targetPos = pos.offset(depth, a, b);
						case SOUTH -> targetPos = pos.offset(a, b, -depth);
						case NORTH -> targetPos = pos.offset(a, b, depth);

						default -> targetPos = pos;
					}
					BlockState state = level.getBlockState(targetPos);
					BlockArmorPropertiesProvider blockArmor = BlockArmorPropertiesHandler.getProperties(state);
					accumulatedArmor += blockArmor.toughness(level, state, targetPos, true);
				}
			}
		}
		accumulatedArmor /= 27;

		return accumulatedArmor;
	}

	public boolean alternativePenetration(){
		return false;
	}

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.BIG_CANNON_PROJECTILE), this.getDamageProperties().ignoresEntityArmor());
	}

	@Override
	protected Vec3 getForces(Vec3 position, Vec3 velocity) {
		return super.getForces(position, velocity);
	}

	@Override
	protected double getDefaultGravity() {
		return super.getGravity();
	}

	@Override
	protected double getDragForce() {
		return super.getDragForce();
	}

//	@Override
//	public boolean ignoreExplosion() {
//		return true;
//	}

	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	@Override
	protected boolean onImpactFluid(ProjectileContext projectileContext, BlockState blockState, FluidState fluidState, Vec3 impactPos, BlockHitResult fluidHitResult) {
		return super.onImpactFluid(projectileContext, blockState, fluidState, impactPos, fluidHitResult);
	}

	public void onShoot(MountedDualCannonContraption contraption, ServerLevel level) {
		setShootContraption(contraption);
	}

	public void setShootContraption(MountedDualCannonContraption contraption) {
		this.contraption = contraption;
	}

	public void reduceCooldownOnHit(BlockPos hitPos){
		if(hitTarget){
			return;
		}
		if(!hitShip(hitPos)){
			return;
		}
		if(this.contraption == null){
			return;
		}
		contraption.reduceCooldown(getCooldownReductionRate());
		hitTarget = true;
	}

	public float addedChargePower() { return this.getDualCannonProjectileProperties().addedChargePower(); }
	public float minimumChargePower() { return this.getDualCannonProjectileProperties().minimumChargePower(); }
	public boolean canSquib() { return this.getDualCannonProjectileProperties().canSquib(); }
	public float addedRecoil() { return this.getDualCannonProjectileProperties().addedRecoil(); }
	public int getLifetime() { return this.getDualCannonProjectileProperties().lifetime(); }
	public float getInitVel(){return this.getDualCannonProjectileProperties().initialVel();}
	public float getProjectileSpread() {return this.getDualCannonProjectileProperties().projectileSpread();}
	public float getProjectileMinimumSpread() {return this.getDualCannonProjectileProperties().minimumSpread();}
	public float getSmashToughness(){return this.getDualCannonProjectileProperties().smashToughness();}
	public float getMaximumMomentum(){return this.getDualCannonProjectileProperties().maximumMomentum();}
	public float getMaximumMass(){return this.getBallisticProperties().durabilityMass();}
	public float getReloadTimeCoef(){return this.getDualCannonProjectileProperties().reloadTimeCoef();}
	public float getCooldownReductionRate(){
		return this.getDualCannonProjectileProperties().cooldownReductionRate();
	}

	public void setLifetime(int lifetime){
		this.maxAge = lifetime;
	};
	public int getAge(){return this.age;}

	public void setDurabilityModifier(float modifier){
		this.durabilityMassModifier = modifier;
	}

	public float getDurabilityModifier(){return this.durabilityMassModifier;}

	public boolean hitShip(BlockPos pos){
		if(Math.abs(pos.getX()) > 10000000 || Math.abs(pos.getZ()) > 10000000){//guess what, I save 100000 iterations
			return true;
		}
		return false;
	}

	@Nonnull protected abstract DualCannonPropertiesComponent getDualCannonProjectileProperties();

	public enum TrailType {
		NONE,
		LONG,
		SHORT
	}

}
