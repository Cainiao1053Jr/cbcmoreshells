package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;


import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedTorpedoProjectileProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;



public abstract class AbstractRackedTorpedoProjectile extends FuzedRackedProjectile {

	public AbstractRackedTorpedoProjectile(EntityType<? extends AbstractRackedTorpedoProjectile> type, Level level) {
		super(type, level);
	}
	public int tickInWater = 0;

	@Override
	public void waterProof() {
		if(tickInWater < getAllProperties().explosionCooldown()+5) {
			FluidState fluidState = this.level().getFluidState(this.blockPosition());
			if(!fluidState.isEmpty()) {
				tickInWater++;
			}
		}
	}

	@Override
	protected boolean onImpactFluid(ProjectileContext projectileContext, BlockState blockState, FluidState fluidState, Vec3 impactPos, BlockHitResult fluidHitResult) {
		return false;
	}


	@Override
	protected double getDragForce() {
		BallisticPropertiesComponent properties = this.getBallisticProperties();
		double vel = this.getDeltaMovement().length();
		double ssVel = getAllProperties().steadyStateVel();
		double formDrag = properties.drag();
		double drag = formDrag * vel;
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
		if (!fluidState.isEmpty()) {
			drag = 0.07 * (vel - ssVel);
		}


		return drag;
	}

	@Override
	protected double getDefaultGravity() {
		double vel = this.getDeltaMovement().y;
		double damp = getAllProperties().waterDamp();
		double waterDamp = 0;
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
		double gForce = this.getBallisticProperties().gravity();
		if (!fluidState.isEmpty()){
			gForce *= -getAllProperties().buoyancyFactor();
			waterDamp = Math.max(damp,0) * vel;
		}
		gForce = gForce - waterDamp;
		return gForce;
	}

	@Override
	protected Vec3 getForces(Vec3 position, Vec3 velocity) {
		return velocity.normalize().scale(-this.getDragForce()).add((double)0.0F, this.getGravity(), (double)0.0F);
	}

	public int getTickInWater() {return this.tickInWater;}

	@Override
	public void playFlybySound(Vec3 originPos, Vec3 displacement, double radius) {}

	protected abstract RackedTorpedoProjectileProperties getAllProperties();
}
