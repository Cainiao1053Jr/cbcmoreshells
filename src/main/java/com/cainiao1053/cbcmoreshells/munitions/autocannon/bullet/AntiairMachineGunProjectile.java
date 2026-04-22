package com.cainiao1053.cbcmoreshells.munitions.autocannon.bullet;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.autocannon.config.AntiairAutocannonProjectileProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.config.CBCCfgMunitions;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonAmmoType;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.network.ClientboundPlayBlockHitEffectPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import javax.annotation.Nonnull;

public class AntiairMachineGunProjectile extends AbstractAutocannonProjectile {

	public AntiairMachineGunProjectile(EntityType<? extends AntiairMachineGunProjectile> type, Level level) {
		super(type, level);
	}

	Logger LOGGER = Cbcmoreshells.LOGGER;

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

	protected AntiairAutocannonProjectileProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.ANTIAIR_AUTOCANNON.getPropertiesOf(this);
	}

	@Override public AutocannonAmmoType getAutocannonRoundType() { return AutocannonAmmoType.MACHINE_GUN; }

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		ResourceKey<DamageType> type = entity.isInWater() ? CBCDamageTypes.MACHINE_GUN_FIRE_IN_WATER : CBCDamageTypes.MACHINE_GUN_FIRE;
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(type), this.getDamageProperties().ignoresEntityArmor());
	}

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {

		BlockPos pos = blockHitResult.getBlockPos();
		Vec3 hitLoc = blockHitResult.getLocation();

		double shipVel = 0;
		Vector3dc shipPos = new Vector3d(0,0,0);
//		Ship ship = getShipOn(level(),pos);
//		if(ship != null) {
//			shipVel = ship.getVelocity().length() + 4;
//			shipPos = ship.getTransform().getPositionInWorld();
//		}


		double penetration = 2.4 * (shipVel + 4) * Math.max(Math.min(shipPos.y()-80,240),40)/160;

		BallisticPropertiesComponent ballistics = this.getBallisticProperties();
		BlockArmorPropertiesProvider blockArmor = BlockArmorPropertiesHandler.getProperties(state);
		boolean unbreakable = projectileContext.griefState() == CBCCfgMunitions.GriefState.NO_DAMAGE || state.getDestroySpeed(this.level(), pos) == -1;

		Vec3 accel = this.getForces(this.position(), this.getDeltaMovement());
		Vec3 curVel = this.getDeltaMovement().add(accel);

		Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level(), blockHitResult);
		double incidence = Math.max(0, curVel.normalize().dot(normal.reverse()));
		double penetrativeEffect = penetration * incidence;
		boolean destroy = penetrativeEffect>blockArmor.toughness(this.level(),state,pos,true);


		double hardnessPenalty = Math.max(blockArmor.hardness(this.level(), state, pos, true) - ballistics.penetration(), 0);

		double projectileDeflection = ballistics.deflection();
		double baseChance = CBCConfigs.server().munitions.baseProjectileBounceChance.getF();
		double bounceChance = projectileDeflection < 1e-2d || incidence > projectileDeflection ? 0 : Math.max(baseChance, 1 - incidence / projectileDeflection);

		boolean surfaceImpact = this.lastPenetratedBlock.isAir();
		boolean canBounce = CBCConfigs.server().munitions.projectilesCanBounce.get();
		ImpactResult.KinematicOutcome outcome;
		if (surfaceImpact && canBounce && this.level().getRandom().nextDouble() < bounceChance) {
			outcome = ImpactResult.KinematicOutcome.BOUNCE;
		} else {
			outcome = ImpactResult.KinematicOutcome.STOP;
		}
		boolean shatter = surfaceImpact && outcome != ImpactResult.KinematicOutcome.BOUNCE && hardnessPenalty > ballistics.toughness();

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
//			if (hardnessPenalty > 1e-2d) {
//				if (ballistics.toughness() < 1e-2d){
//					momentum = 0;
//				} else{
//					momentum *= Math.max(0.25, 1 - hardnessPenalty / ballistics.toughness());
//				}
//			}
			if (!unbreakable && destroy){
				this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), ProjectileBlock.UPDATE_ALL_IMMEDIATE);
				this.discard();
			}
				//CreateBigCannons.BLOCK_DAMAGE.damageBlock(pos.immutable(), Math.max(Mth.ceil(momentum), 0), state, this.level());
		}
		this.onImpact(blockHitResult, new ImpactResult(outcome, shatter), projectileContext);
		return new ImpactResult(outcome, !this.level().isClientSide && (shatter || outcome != ImpactResult.KinematicOutcome.BOUNCE));
	}

//	protected @Nullable Ship getShipOn(Level level, BlockPos pos) {
//		return ValkyrienSkies.getShipManagingBlock(level, pos);
//	}
}
