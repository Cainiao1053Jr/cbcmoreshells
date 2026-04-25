package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;


import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedLoiteringRocketProjectileProperties;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.effects.particles.smoke.TrailSmokeParticleData;
import rbasamoyai.createbigcannons.munitions.config.FluidDragHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;


public abstract class AbstractRackedLoiteringRocketProjectile extends FuzedRackedProjectile {

    public AbstractRackedLoiteringRocketProjectile(EntityType<? extends AbstractRackedLoiteringRocketProjectile> type, Level level) {
        super(type, level);
    }

    protected int thrustTime = getAllProperties().thrustTime();
    protected int guidanceStart = getAllProperties().control().guidanceStart();
    protected int searchLength = getAllProperties().control().searchLength();
    protected int searchWidth = getAllProperties().control().searchWidth();
    protected float controlGain = getAllProperties().control().guidanceControl();
    protected float maxG = getAllProperties().control().maxG();
    protected float scanAngle = getAllProperties().control().scanAngle();
    /** Locked sublevel target; stored as Object to avoid a direct Sable dependency here. */
    protected Object targetSublevel = null;
    protected Vec3 cachedTargetPos = null;
    protected Vec3 cachedTargetVel = null;

    @Override
    public void tick() {
        super.tick();
        if (!this.isInGround()) {
            int lifetime = 20;
            ParticleOptions options = new TrailSmokeParticleData(lifetime);
            for (int i = 0; i < 5; ++i) {
                double partial = i * 0.2f;
                double dx = Mth.lerp(partial, this.xOld, this.getX());
                double dy = Mth.lerp(partial, this.yOld, this.getY());
                double dz = Mth.lerp(partial, this.zOld, this.getZ());
                double sx = this.level().random.nextDouble() * 0.004d - 0.002d;
                double sy = this.level().random.nextDouble() * 0.004d - 0.002d;
                double sz = this.level().random.nextDouble() * 0.004d - 0.002d;
                this.level().addAlwaysVisibleParticle(options, true, dx, dy, dz, sx, sy, sz);
            }
        }
        if (!this.level().isClientSide && getAge() > guidanceStart) {
            if (this.targetSublevel == null) {
                Vec3 heading = this.getDeltaMovement();
                if (heading.lengthSqr() > 1e-12) {
                    CBCMSCompatTransformers.SubLevelTarget found = CBCMSCompatTransformers.findSublevelTarget(
                            this.level(), this.position(), heading,
                            searchLength, searchWidth, scanAngle);
                    if (found != null) {
                        this.targetSublevel = found.sublevel();
                        this.cachedTargetPos = found.position();
                        this.cachedTargetVel = found.velocity();
                    }
                }
            } else {
                CBCMSCompatTransformers.SubLevelTarget tracked = CBCMSCompatTransformers.trackSublevelTarget(
                        this.level(), this.targetSublevel);
                if (tracked != null) {
                    this.cachedTargetPos = tracked.position();
                    this.cachedTargetVel = tracked.velocity();
                } else {
                    this.targetSublevel = null;
                    this.cachedTargetPos = null;
                    this.cachedTargetVel = null;
                }
            }
        }
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
            drag += FluidDragHandler.getFluidDrag(fluidState) * formDrag * vel;
        }
        if (this.getAge() < thrustTime) {
            drag += 0.12 * (vel - ssVel);
        }
        return drag;
    }

    @Override
    protected Vec3 getForces(Vec3 position, Vec3 velocity) {
        Vec3 naturalForce = velocity.normalize().scale(-this.getDragForce()).add((double) 0.0F, this.getGravity(), (double) 0.0F);
        Vec3 control = new Vec3(0, 0, 0);
        if (this.targetSublevel != null && this.cachedTargetPos != null) {
            Vec3 targetVel = this.cachedTargetVel != null ? this.cachedTargetVel : new Vec3(0, 0, 0);
            Vec3 shipPos = this.cachedTargetPos.add(targetVel.scale(0.15));
            Vec3 dirToTarget = position.subtract(shipPos).normalize();
            Vec3 heading = velocity.normalize();
            double dot = dirToTarget.dot(heading);
            Vec3 lateral = dirToTarget.subtract(heading.scale(dot));
            double lateralLength = lateral.length();
            double vel = velocity.length();
            double actualForce = lateralLength * vel * controlGain;
            if (actualForce > maxG) {
                control = lateral.normalize().scale(-maxG).subtract(0, this.getGravity(), 0);
            } else {
                control = lateral.scale(-vel * controlGain).subtract(0, this.getGravity(), 0);
            }
        }
        return naturalForce.add(control);
    }

    protected abstract RackedLoiteringRocketProjectileProperties getAllProperties();
}
