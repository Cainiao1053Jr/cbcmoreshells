package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

import com.verr1.shaolib.api.block.BlockBodyResolverServices;
import com.verr1.shaolib.api.explosion.TraceableExplosion;
import com.verr1.shaolib.api.projectile.ProjectileInstance;
import com.verr1.shaolib.api.projectile.ProjectileServerContext;
import com.verr1.shaolib.api.projectile.trace.ProjectileTraceSnapshot;
import com.verr1.shaolib.api.raycast.ShaolibBlockHitResult;
import com.verr1.shaolib.api.raycast.ShaolibHitResultType;
import com.verr1.shaolib.munitions.api.penetration.BodyPenetrationResistanceModifierContext;
import com.verr1.shaolib.munitions.api.penetration.BodyPenetrationResistanceModifierServices;
import com.verr1.shaolib.munitions.config.properties.CbcLikeMunitionProperties;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyComponents;
import com.verr1.shaolib.munitions.projectile.effects.CbcMunitionEffectPipeline;
import com.verr1.shaolib.munitions.projectile.impact.BlockDestructionResult;
import com.verr1.shaolib.munitions.projectile.impact.BlockImpactSupport;
import com.verr1.shaolib.munitions.projectile.impact.MunitionImpactOutcome;
import com.verr1.shaolib.munitions.projectile.impact.MunitionImpactUtil;
import java.util.Optional;
import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesProvider;
import rbasamoyai.createbigcannons.munitions.ImpactExplosion;

public final class DualCannonPenetrationModel {

	private static final double ELASTICITY = 1.7;
	private static final double EPSILON = 1.0E-6;

	/** How much of the accumulation coefficient each further block into the armour stack costs. */
	private static final double TOUGHNESS_REDUCTION_RATE = 0.25;
	/** Toughness at or below which a block counts as a gap, ending the accumulation. */
	private static final double AIR_TOUGHNESS_THRESHOLD = 1.0;
	/** Hard bound on the accumulation walk so a mis-set reduction rate cannot spin forever. */
	private static final int MAX_TOUGHNESS_ACCUMULATION_STEPS = 16;

	private DualCannonPenetrationModel() {}

	public static <S extends DualCannonState> MunitionImpactOutcome resolve(ProjectileServerContext<S> context,
																			ShaolibBlockHitResult hit,
																			CbcLikeMunitionProperties properties,
																			DualCannonBehavior.Kind kind) {
		if (!(properties instanceof DualCannonMunitionProperties dualProperties)) {
			throw new IllegalArgumentException("Dual cannon projectile requires DualCannonMunitionProperties");
		}
		ProjectileInstance projectile = context.projectile();
		Vec3 velocity = projectile.velocity();
		DualCannonState state = context.state();
		MunitionPropertyComponents.BallisticsProperties ballistics = dualProperties.ballistics();
		state.initializeDurabilityMass(ballistics.durabilityMass() * state.durabilityModifier());
		double mass = state.durabilityMass();

		if (hit.shaolibType() == ShaolibHitResultType.UNCERTAIN) {
			return MunitionImpactOutcome.uncertain(hit, velocity, mass);
		}
		Optional<BlockState> liveState = BlockImpactSupport.liveBlockState(context, hit);
		if (liveState.isEmpty() || liveState.orElseThrow().isAir()) {
			return MunitionImpactOutcome.uncertain(hit, velocity, mass);
		}

		BlockState state1 = liveState.orElseThrow();
		MunitionPropertyComponents.ImpactProperties impact = dualProperties.impact();
		double speed = velocity.length();
		if (speed < impact.stopSpeed() || mass <= EPSILON) {
			return stopOnBlock(context, dualProperties, hit, state1, velocity, mass, false);
		}

		Vec3 direction = velocity.normalize();
		Vec3 normal = MunitionImpactUtil.safeNormal(hit.worldNormal());
		double incidence = Math.max(0.0, -direction.dot(normal));
		double incidentVelocity = speed * incidence;
		BlockPos blockPos = BlockImpactSupport.impactedBlockPos(hit);
		BlockArmorPropertiesProvider armor = BlockArmorPropertiesHandler.getProperties(state1);

		DualCannonImpactProperties dualImpact = dualProperties.dualImpact();
		double cappedMomentum = getCappedMomentum(dualProperties, state.durabilityModifier(), speed, mass);
		// HSAP defeats armour by stacking the plates behind the entry face instead of reading the
		// entry face alone, and it does so head-on: obliquity plays no part in its momentum.
		boolean stacked = kind == DualCannonBehavior.Kind.HSAP;
		double toughness = stacked
			? accumulatedToughness(context, state1, blockPos, hit.localDirection().getOpposite())
			: Math.max(0.0, armor.toughness(context.level(), state1, blockPos, true));
		toughness *= bodyResistanceMultiplier(context, hit, blockPos, state1, toughness);
		boolean unbreakable = !impact.breakBlocks() || state1.getDestroySpeed(context.level(), blockPos) < 0.0F;
		double momentum = stacked ? cappedMomentum : cappedMomentum * incidence;
		double durabilityPenalty = incidentVelocity <= EPSILON ? mass : toughness / incidentVelocity;

		boolean penetrate = momentum > toughness * 1.5;
		if (!penetrate && momentum > toughness * 0.5 && toughness > EPSILON) {
			double penetrationChance = Math.max(0.0, ((momentum / toughness) - 0.5));
			penetrate = context.level().random.nextDouble() < penetrationChance;
		}

		boolean canPenetrate = state.penetrations() < impact.maxPenetrations();
		boolean forceSmash = dualImpact.smashToughness() > toughness;
		if (!canPenetrate || unbreakable || (!penetrate && !forceSmash)) {
			return tryBounce(context, dualProperties, hit, state1, armor, blockPos, velocity, normal, incidence, mass,
				durabilityPenalty)
				.orElseGet(() -> stopOnBlock(context, dualProperties, hit, state1, velocity, mass, false));
		}
		return penetrateBlock(context, dualProperties, hit, state1, armor, blockPos, velocity, direction, mass, momentum,
			toughness, durabilityPenalty);
	}

	/**
	 * Walks one block at a time from the entry block along {@code inward} — the inward normal of the
	 * face that was hit — and sums each block's toughness weighted by a coefficient that starts at 1
	 * and drops by {@link #TOUGHNESS_REDUCTION_RATE} per step, so deeper plates back the armour up by
	 * less and less. The walk ends once the coefficient goes negative, or as soon as a block is thin
	 * enough to count as a gap (toughness below {@link #AIR_TOUGHNESS_THRESHOLD}); the gap itself
	 * contributes nothing.
	 */
	private static double accumulatedToughness(ProjectileServerContext<?> context, BlockState entryState,
											   BlockPos entryPos, Direction inward) {
		ServerLevel level = context.level();
		double accumulated = 0.0;
		double accCoef = 1.0;
		BlockPos cursor = entryPos;
		BlockState blockState = entryState;
		for (int step = 0; accCoef >= 0.0 && step < MAX_TOUGHNESS_ACCUMULATION_STEPS; ++step) {
			double blockToughness = Math.max(0.0,
				BlockArmorPropertiesHandler.getProperties(blockState).toughness(level, blockState, cursor, true));
			// Toughness stands in for the block lookup here: anything this soft is a void in the stack.
			if (step > 0 && blockToughness < AIR_TOUGHNESS_THRESHOLD) break;
			accumulated += blockToughness * accCoef;
			accCoef -= TOUGHNESS_REDUCTION_RATE;

			cursor = cursor.relative(inward);
			if (!level.isLoaded(cursor)) break;
			blockState = level.getBlockState(cursor);
		}
		return accumulated;
	}

	public static double getCappedMomentum(DualCannonMunitionProperties dualProperties, double dmm, double speed, double mass){
		DualCannonImpactProperties dualImpact = dualProperties.dualImpact();
		MunitionPropertyComponents.ImpactProperties impact = dualProperties.impact();
		double bonusMomentum =
				1.0 + Math.max(0.0, (speed - impact.minVelocityForPenetrationBonus()) * impact.penetrationBonusScale());
		double rawMomentum = mass * bonusMomentum * speed;
		double cappedMomentum =
				dualImpact.maximumMomentum() <= EPSILON ? rawMomentum : Math.min(rawMomentum, dualImpact.maximumMomentum() * dmm);
		return cappedMomentum;
	}

	private static <S extends DualCannonState> MunitionImpactOutcome penetrateBlock(ProjectileServerContext<S> context,
																					DualCannonMunitionProperties properties,
																					ShaolibBlockHitResult hit,
																					BlockState blockState,
																					BlockArmorPropertiesProvider armor,
																					BlockPos blockPos, Vec3 velocity,
																					Vec3 direction, double mass,
																					double momentum, double toughness,
																					double durabilityPenalty) {
		BlockDestructionResult destruction = BlockImpactSupport.tryDestroyImpactedBlock(context, hit);
		if (!destruction.destroyed()) {
			// Something vetoed the block break (protection, grief rules); treat it as solid.
			return stopOnBlock(context, properties, hit, blockState, velocity, mass, false);
		}
		CbcMunitionEffectPipeline.playPenetration(context, properties.effects(), hit, blockState, armor, blockPos, velocity);

		DualCannonState state = context.state();
		double scale;
		if (momentum > toughness * 1.5) {
			scale = properties.dualImpact().penetrationMassPenaltyScale();
		} else {
			// Barely got through: the shell is spent, leave only a token mass so it stops on the next
			// thing it meets.
			state.setDurabilityMass(Math.min(mass, 0.1));
			scale = 0.0;
		}
		if (scale > 0.0) {
			state.setDurabilityMass(Math.max(0.0, mass - durabilityPenalty * scale));
		}

		double nextMass = state.durabilityMass();
		double retained = MunitionImpactUtil.retainedAfterPenetration(mass, nextMass, properties.impact());
		Vec3 nextVelocity = velocity.scale(retained);
		Vec3 nextPosition = MunitionImpactUtil.penetrationReclipPosition(hit.worldLocation(), direction);
		return MunitionImpactOutcome.penetrate(hit, nextPosition, nextVelocity, true, false, velocity, mass,
			destruction.destroyedBlock());
	}

	private static <S extends DualCannonState> Optional<MunitionImpactOutcome> tryBounce(
		ProjectileServerContext<S> context, DualCannonMunitionProperties properties, ShaolibBlockHitResult hit,
		BlockState blockState, BlockArmorPropertiesProvider armor, BlockPos blockPos, Vec3 velocity, Vec3 normal,
		double incidence, double mass, double durabilityPenalty) {

		MunitionPropertyComponents.ImpactProperties impact = properties.impact();
		MunitionPropertyComponents.BallisticsProperties ballistics = properties.ballistics();
		DualCannonState state = context.state();

		boolean eligible = impact.canBounce()
			&& state.ricochets() < impact.maxRicochets()
			&& ballistics.deflection() > EPSILON
			&& incidence <= ballistics.deflection();
		if (!eligible) return Optional.empty();

		// Shallower hits bounce more readily; at grazing incidence this approaches certainty.
		double bounceChance = Math.max(impact.baseBounceChance(), 1.0 - incidence / ballistics.deflection());
		if(incidence < properties.dualImpact().minDeflection()){
			bounceChance = 1;
		}
		if (context.level().random.nextDouble() >= bounceChance) return Optional.empty();

		Vec3 reflected = velocity.subtract(normal.scale(ELASTICITY * velocity.dot(normal)))
			.scale(impact.bounceVelocityRetained());
		if (reflected.length() < impact.stopSpeed()) return Optional.empty();

		CbcMunitionEffectPipeline.playBounce(context, properties.effects(), hit, blockState, armor, blockPos, velocity,
			normal);
		state.setDurabilityMass(
			Math.max(0.0, mass - durabilityPenalty * properties.dualImpact().bounceMassPenaltyScale()));
		Vec3 nextPosition = hit.worldLocation().add(reflected.normalize().scale(impact.bounceExitDistance()));
		return Optional.of(MunitionImpactOutcome.bounce(hit, nextPosition, reflected, velocity, mass));
	}

	private static <S extends DualCannonState> MunitionImpactOutcome stopOnBlock(ProjectileServerContext<S> context,
																				 DualCannonMunitionProperties properties,
																				 ShaolibBlockHitResult hit,
																				 BlockState blockState, Vec3 velocity,
																				 double mass, boolean shatter) {
		BlockPos blockPos = BlockImpactSupport.impactedBlockPos(hit);
		BlockArmorPropertiesProvider armor = BlockArmorPropertiesHandler.getProperties(blockState);
		CbcMunitionEffectPipeline.playStop(context, properties.effects(), hit, blockState, armor, blockPos, velocity);
		context.state().setDurabilityMass(0.0);
		playSpallExplosion(context, hit.worldLocation().add(velocity.normalize().scale(2.0)), properties);
		return MunitionImpactOutcome.stop(hit, shatter, velocity, mass);
	}

	private static void playSpallExplosion(ProjectileServerContext<?> context, Vec3 position,
										   DualCannonMunitionProperties properties) {
		double power = properties.dualImpact().spallExplosionPower();
		if (power <= EPSILON) return;
		ImpactExplosion explosion = new ImpactExplosion(context.level(), null, null, position.x, position.y, position.z,
			(float) power, (float) power, Explosion.BlockInteraction.KEEP);
		if (explosion instanceof TraceableExplosion traceable) {
			traceable.shaolib$setProjectileTrace(ProjectileTraceSnapshot.from(context.projectile()));
		}
		CreateBigCannons.handleCustomExplosion(context.level(), explosion);
	}

	private static double bodyResistanceMultiplier(ProjectileServerContext<?> context, ShaolibBlockHitResult hit,
												   BlockPos blockPos, BlockState blockState, double toughness) {
		Optional<UUID> bodyId = hit.bodyId().or(() -> BlockBodyResolverServices.bodyId(context.level(), blockPos));
		if (bodyId.isEmpty()) return 1.0;
		return BodyPenetrationResistanceModifierServices.multiplier(new BodyPenetrationResistanceModifierContext(
			context.level(), context.projectile(), bodyId.orElseThrow(), hit, blockPos, blockState, toughness, 0.0));
	}

}
