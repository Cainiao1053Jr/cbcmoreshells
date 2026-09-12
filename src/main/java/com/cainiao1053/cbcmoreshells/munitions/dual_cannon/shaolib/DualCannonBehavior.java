package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

import com.cainiao1053.cbcmoreshells.network.CBCMSNetworkImpl;
import com.cainiao1053.cbcmoreshells.network.ClientboundCBCMSSplashPacket;
import com.cainiao1053.cbcmoreshells.network.ClientboundCBCMSTrailPacket;
import com.verr1.shaolib.api.projectile.ProjectileInstance;
import com.verr1.shaolib.api.projectile.ProjectileServerContext;
import com.verr1.shaolib.api.projectile.chunkload.ProjectileChunkLoadPolicy;
import com.verr1.shaolib.api.projectile.entity.ProjectileEntityHitOptions;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyComponents;
import com.verr1.shaolib.munitions.config.properties.MunitionPropertyResolver;
import com.verr1.shaolib.munitions.fuze.FuzeResult;
import com.verr1.shaolib.munitions.fuze.MunitionFuzes;
import com.verr1.shaolib.munitions.projectile.effects.CbcMunitionEffectPipeline;
import com.verr1.shaolib.munitions.projectile.impact.CbcLikeEntityImpact;
import com.verr1.shaolib.munitions.projectile.impact.MunitionImpactKinematics;
import com.verr1.shaolib.munitions.projectile.impact.MunitionImpactOutcome;
import com.verr1.shaolib.munitions.projectile.impact.MunitionImpactSweep;
import com.verr1.shaolib.munitions.projectile.shell.AbstractFuzedShellBehavior;
import com.verr1.shaolib.munitions.projectile.shell.FuzedShellData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

public class DualCannonBehavior<P extends DualCannonMunitionProperties>
	extends AbstractFuzedShellBehavior<DualCannonState, P> {
	private static final int TRAIL_INTERVAL = 5;
	private static final int TRAIL_IDLE = 200;
	private static final int TRAIL_INITIAL_DELAY = 20;
	private static final double TRAIL_ENDPOINT_PULLBACK = 0.75;

	private final Kind kind;

	public DualCannonBehavior(MunitionPropertyResolver<P> propertyResolver, Kind kind) {
		super(propertyResolver);
		this.kind = kind;
	}

	public Kind kind() {
		return this.kind;
	}

	@Override
	public void onSpawn(ProjectileServerContext<DualCannonState> context) {
		super.onSpawn(context);
		DualCannonState state = context.state();
		Vec3 position = context.projectile().position();
		state.setLaunchY(position.y);
		state.setTrailAnchor(position);
		state.setTrailCooldown(TRAIL_INITIAL_DELAY);
		state.setTrailStage(DualCannonState.TRAIL_STAGE_TRACING);
	}

	@Override
	public void tickServer(ProjectileServerContext<DualCannonState> context) {
		if (this.expireOnLifetime(context)) return;
		this.tickTrail(context);

		ProjectileInstance projectile = context.projectile();
		int projectileState = projectile.get(FuzedShellData.STATE);
		if (projectileState == FuzedShellData.STATE_EMBEDDED || projectile.isEmbedded()) {
			this.tickEmbedded(context);
			return;
		}
		if (projectileState == FuzedShellData.STATE_ARMED) {
			this.tickArmed(context);
			return;
		}
		if (FuzedShellData.isTerminalState(projectileState)) {
			this.tickTerminal(context, this.properties(context).runtime());
			return;
		}

		P properties = this.properties(context);
		DualCannonState state = context.state();
		state.setDetonationVelocity(projectile.velocity());
		if (this.applyFuzeResult(context, MunitionFuzes.serverTick(context), projectile.position())) return;

		// Integrate a full tick, then rewind and sweep the segment we just covered, so impacts are
		// resolved against the path travelled rather than the endpoint alone.
		Vec3 start = projectile.position();
		projectile.set(FuzedShellData.TRAIL_VELOCITY, projectile.velocity());
		context.steps().integrateMotion(1.0);
		Vec3 end = projectile.position();
		Vec3 sweepVelocity = projectile.velocity();
		state.addTravelled(start.distanceTo(end));
		projectile.setPosition(start);
		projectile.setVelocity(sweepVelocity);

		MunitionImpactSweep.Result sweep = MunitionImpactSweep.sweep(context, start, end, sweepVelocity, properties,
			loadPolicy(projectile), this.entityHitOptions(context),
			(sweepContext, clipStart, clipEnd) ->
				this.applyFuzeResult(sweepContext, MunitionFuzes.onClip(sweepContext, clipStart, clipEnd), clipStart),
			this::applyImpactOutcome,
			this::applyEntityImpact,
				(prjContext, hit, result) -> {
			return DualCannonPenetrationModel.resolve(prjContext, hit, result);
				}
			//DualCannonPenetrationModel::resolve
		);

		if (!sweep.stopped() && state.travelled() >= properties.runtime().maxDistance()) {
			context.runtime().discard("max_distance");
		}
	}
	protected boolean expireOnLifetime(ProjectileServerContext<DualCannonState> context) {
		ProjectileInstance projectile = context.projectile();
		if (FuzedShellData.isTerminalState(projectile.get(FuzedShellData.STATE))) return false;
		int lifetime = context.state().lifetimeTicks();
		if (lifetime <= 0 || projectile.ageTicks() < lifetime) return false;
		context.runtime().discard("cbcms_lifetime");
		return true;
	}

	protected void tickTrail(ProjectileServerContext<DualCannonState> context) {
		DualCannonState state = context.state();
		if (state.trailStage() != DualCannonState.TRAIL_STAGE_TRACING) return;

		int cooldown = state.trailCooldown();
		if (cooldown > 0) {
			state.setTrailCooldown(cooldown - 1);
			return;
		}

		ProjectileInstance projectile = context.projectile();
		ServerLevel level = context.level();
		Vec3 anchor = state.trailAnchor();
		Vec3 position = projectile.position();
		boolean grounded =
			projectile.isEmbedded() || !FuzedShellData.isFlyingState(projectile.get(FuzedShellData.STATE));
		boolean inWater = level.getFluidState(projectile.blockPosition()).is(FluidTags.WATER);

		if (!grounded && !inWater) {
			this.broadcastTrail(level, position, anchor, false);
			state.setTrailAnchor(position);
			state.setTrailCooldown(TRAIL_INTERVAL);
			return;
		}

		// Pull the endpoint back toward the anchor so the trail does not visibly poke through the
		// surface the shell just hit.
		Vec3 endpoint = new Vec3(
			Mth.lerp(TRAIL_ENDPOINT_PULLBACK, position.x, anchor.x),
			Mth.lerp(TRAIL_ENDPOINT_PULLBACK, position.y, anchor.y),
			Mth.lerp(TRAIL_ENDPOINT_PULLBACK, position.z, anchor.z));
		this.broadcastTrail(level, endpoint, anchor, inWater);
		state.setTrailStage(DualCannonState.TRAIL_STAGE_DONE);
		state.setTrailCooldown(TRAIL_IDLE);
	}

	private void broadcastTrail(ServerLevel level, Vec3 to, Vec3 from, boolean splash) {
		for (ServerPlayer player : level.players()) {
			if (splash) {
				CBCMSNetworkImpl.sendToClientPlayer(
					new ClientboundCBCMSSplashPacket(to.x, to.y, to.z, from.x, from.y, from.z), player);
			} else {
				CBCMSNetworkImpl.sendToClientPlayer(
					new ClientboundCBCMSTrailPacket(to.x, to.y, to.z, from.x, from.y, from.z), player);
			}
		}
	}


	@Override
	protected boolean applyImpactOutcome(ProjectileServerContext<DualCannonState> context,
										 MunitionImpactOutcome outcome) {
		context.state().hitCallback().onHit(context, outcome);

		if (this.kind == Kind.AP_SHOT) {
			return this.applyApShotImpactOutcome(context, outcome);
		}
		if (this.kind.isSAP() && outcome.kinematics() == MunitionImpactKinematics.PENETRATE) {
			context.state().setDurabilityMass(0.0);
			this.detonate(context, this.blockImpactDetonationPosition(outcome.hit()));
			return false;
		}
		if (this.kind.isApheLike() && outcome.kinematics() == MunitionImpactKinematics.PENETRATE) {
			FuzeResult fuzeResult = MunitionFuzes.onImpact(context, outcome.hit(), outcome);
			if (this.applyFuzeResult(context, fuzeResult, this.blockImpactDetonationPosition(outcome.hit()))) {
				return false;
			}
		}
		return super.applyImpactOutcome(context, outcome);
	}


	private boolean applyApShotImpactOutcome(ProjectileServerContext<DualCannonState> context,
											 MunitionImpactOutcome outcome) {
		DualCannonState state = context.state();
		switch (outcome.kinematics()) {
			case UNCERTAIN -> {
				this.skipUncertainImpact(context, outcome);
				return true;
			}
			case BOUNCE -> {
				this.applyFlightOutcome(context, outcome, FuzedShellData.STATE_RICOCHET, FuzedShellData.COLOR_RICOCHET);
				state.incrementRicochets();
				return false;
			}
			case PENETRATE -> {
				this.applyFlightOutcome(context, outcome, FuzedShellData.STATE_PENETRATED,
					FuzedShellData.COLOR_PENETRATED);
				state.incrementPenetrations();
				return true;
			}
			default -> {
				this.hitAndLinger(context, outcome.hit(), FuzedShellData.STATE_HIT, FuzedShellData.COLOR_HIT);
				state.setTerminalAge(context.projectile().ageTicks());
				return false;
			}
		}
	}

	@Override
	protected ProjectileEntityHitOptions entityHitOptions(ProjectileServerContext<DualCannonState> context) {
		return CbcLikeEntityImpact.shellHitOptions();
	}

	// -------------------------------------------------------------------------------------------
	// Detonation
	// -------------------------------------------------------------------------------------------

	@Override
	protected boolean detonatePayload(ProjectileServerContext<DualCannonState> context, P properties, Vec3 position,
									  Vec3 detonationVelocity) {
		if (this.kind == Kind.AP_SHOT) return false;

		CbcMunitionEffectPipeline.detonateShell(context, this.scaledEffects(context, properties, position), position);

		if (this.kind == Kind.INCENDIARY && properties instanceof DualCannonIncendiaryProjectileProperties incendiary) {
			float fireChance = (float) (incendiary.incendiary().fireChance() * this.incendiaryFireMultiplier(context));
			int fireRange = (int) Math.round(incendiary.incendiary().fireRange() * this.incendiaryRangeMultiplier(context));
			DualCannonIncendiaryService.Holder.get().ignite(context.level(), position, fireChance, fireRange);
		}
		return true;
	}

	/** Rebuilds the effect set with the explosion power scaled for this shell's kind and modifier. */
	protected MunitionPropertyComponents.EffectProperties scaledEffects(ProjectileServerContext<DualCannonState> context,
																		P properties, Vec3 position) {
		MunitionPropertyComponents.EffectProperties effects = properties.effects();
		MunitionPropertyComponents.ExplosionProperties explosion = effects.explosion();
		float power = (float) (explosion.power() * this.explosionPowerMultiplier(context));
		return new MunitionPropertyComponents.EffectProperties(effects.blockHitEffects(), effects.trailSmoke(),
			effects.flybySound(), effects.cbcProjectileEffectEntity(),
			new MunitionPropertyComponents.ExplosionProperties(explosion.kind(), power, explosion.fire(),
				explosion.noEffects()));
	}

	protected double explosionPowerMultiplier(ProjectileServerContext<DualCannonState> context) {
		double modifier = context.state().durabilityModifier();
		double base = DualCannonModifiers.explosionPower(this.kind, modifier);
		return this.kind == Kind.ANTIAIR_HE ? base * this.antiairAltitudeMultiplier(context) : base;
	}

	private double antiairAltitudeMultiplier(ProjectileServerContext<DualCannonState> context) {
		DualCannonState state = context.state();
		double currentY = context.projectile().position().y;
		double launchY = state.hasLaunchY() ? state.launchY() : currentY;
		return DualCannonModifiers.antiairAltitude(currentY - launchY);
	}

	private double incendiaryFireMultiplier(ProjectileServerContext<DualCannonState> context) {
		return DualCannonModifiers.incendiaryFire(context.state().durabilityModifier());
	}

	private double incendiaryRangeMultiplier(ProjectileServerContext<DualCannonState> context) {
		return DualCannonModifiers.incendiaryRange(context.state().durabilityModifier());
	}

	private static ProjectileChunkLoadPolicy loadPolicy(ProjectileInstance projectile) {
		int ordinal = projectile.get(FuzedShellData.LOAD_POLICY);
		ProjectileChunkLoadPolicy[] values = ProjectileChunkLoadPolicy.values();
		return ordinal >= 0 && ordinal < values.length ? values[ordinal] : ProjectileChunkLoadPolicy.IF_ALREADY_LOADED;
	}

	/**
	 * Selects the impact and detonation rules for a shell class. One behavior class covers all of
	 * them so the flight pipeline stays in a single place.
	 */
	public enum Kind {
		/** Solid shot: no burst charge, penetrates or ricochets and then lies where it lands. */
		AP_SHOT,
		/** High explosive: thin walled, large burst. */
		HE,
		/** Anti-air high explosive: burst scales with altitude gained. */
		ANTIAIR_HE,
		/** Armour-piercing high explosive: base fuze, bursts after punching through. */
		APHE,
		/** Armour-piercing ballistic capped: as APHE with a better deflection envelope. */
		APBC,
		/** Semi armour-piercing: bursts at the entry face instead of carrying on through. */
		SAP,
		/** High explosive incendiary: HE burst plus a fire-starting payload. */
		INCENDIARY,
		HSAP;

		/** True for shells whose fuze is only consulted after the armour is defeated. */
		public boolean isApheLike() {
			return this == APHE || this == APBC;
		}

		public boolean isSAP(){
			return this == SAP || this == HSAP;
		}
	}

}
