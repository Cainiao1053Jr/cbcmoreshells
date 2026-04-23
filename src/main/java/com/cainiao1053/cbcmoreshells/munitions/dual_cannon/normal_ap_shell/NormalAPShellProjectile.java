package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shell;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.DualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonPropertiesComponent;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ImpactExplosion;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fuzes.TimedFuzeItem;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import javax.annotation.Nonnull;

public class NormalAPShellProjectile extends FuzedDualCannonProjectile {

	public NormalAPShellProjectile(EntityType<? extends NormalAPShellProjectile> type, Level level) {
		super(type, level);
	}


	@Override
	protected void detonate(Position position) {
		float explosivePower = this.getAllProperties().explosion().explosivePower() * ((this.getDurabilityModifier()) * 0.47f + 0.4f);
		//float explosivePower = ((this.getDurabilityModifier()) * 0.5f + 0.4f) * initialPower;
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.NORMAL_AP_SHELL_BLOCK.getDefaultState();
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Override
	public void setChargePower(float power) {
	}

	@Override
	public boolean additionalDetonateCondition(Item fuze) {
        return !(fuze instanceof TimedFuzeItem);
    }

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
		double momentum = rawMomentum * incidence;

		double toughness = blockArmor.toughness(this.level(), state, pos, true);

		double projectileDeflection = ballistics.deflection();
		double baseChance = CBCConfigs.server().munitions.baseProjectileBounceChance.getF();
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
		boolean shatter = false;
		if (outcome == ImpactResult.KinematicOutcome.PENETRATE) {
			if(momentum>toughness*3){
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty*1.3f, 0));
			}else if (momentum>toughness*2){
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty*1.08f, 0));
			} else{
				this.setProjectileMass(0.1f);
			}
			this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), DualCannonProjectileBlock.UPDATE_ALL_IMMEDIATE);
			shatter |= this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		} else {
			if (outcome == ImpactResult.KinematicOutcome.STOP) {
				this.setProjectileMass(0);
			} else {
				this.setProjectileMass(incidentVel < 1e-4d ? 0 : Math.max(this.getProjectileMass() - durabilityPenalty/3f, 0));
			}
			Vec3 spallLoc = hitLoc.add(curVel.normalize().scale(2));
			if (!this.level().isClientSide) {
				ImpactExplosion explosion = new ImpactExplosion(this.level(), this, this.indirectArtilleryFire(false), spallLoc.x, spallLoc.y, spallLoc.z, 1, Explosion.BlockInteraction.KEEP);
				CreateBigCannons.handleCustomExplosion(this.level(), explosion);
			}
			SoundType sound = state.getSoundType();
			if (!this.level().isClientSide)
				this.level().playSound(null, spallLoc.x, spallLoc.y, spallLoc.z, sound.getBreakSound(), SoundSource.BLOCKS,
						sound.getVolume(), sound.getPitch());
		}
		//shatter |= this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		return new ImpactResult(outcome, shatter);
	}


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

	protected DualCannonProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES.getPropertiesOf(this);
	}

}
