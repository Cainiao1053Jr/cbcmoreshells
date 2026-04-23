package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;


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
    //protected Ship targetShip;
    //protected Vec3 control = new Vec3(0.0D, 0.0D, 0.0D);

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
//        if(getAge() > guidanceStart && this.targetShip == null) {
//            //start control
//            List<Ship> ships = new ArrayList<>();
//            Vec3 dir = this.getOrientation().normalize();
//            Vec3 offset = dir.scale(searchWidth);
//            dir = dir.scale(searchLength);
//
//            AABB currentMovementRegion = this.getBoundingBox()
//                    .inflate(searchWidth)
//                    .expandTowards(dir)
//                    .move(offset);
//            var shipWorldCore = VSGameUtilsKt.getShipObjectWorld((ServerLevel) this.level());
//
//            double leastScore = Double.MAX_VALUE;
//            for(var ship :shipWorldCore.getLoadedShips()){
//                AABBdc shipABdc = ship.getWorldAABB();
//                AABB shipAABB = toAABB(shipABdc);
//                if(currentMovementRegion.intersects(shipAABB)) {
//                    //set target
//                    Vector3dc shipPosdc = ship.getTransform().getPositionInWorld();
//                    Vec3 shipPos = new Vec3(shipPosdc.x(), shipPosdc.y(), shipPosdc.z());
//                    Vec3 dirToTarget = shipPos.subtract(this.position()).normalize();
//                    Vec3 heading = this.getDeltaMovement().normalize();
//                    double dot = dirToTarget.dot(heading);
//                    if(dot > scanAngle){
//                        ships.add(ship);
//                    }
//                }
//            }
//            if(!ships.isEmpty()) {
//                for(Ship ship : ships) {
//                    Vector3dc shipPosdc = ship.getTransform().getPositionInWorld();
//                    Vec3 shipPos = new Vec3(shipPosdc.x(), shipPosdc.y(), shipPosdc.z());
//                    double distSqr = this.position().distanceTo(shipPos);
//                    Vec3 dirToTarget = shipPos.subtract(this.position()).normalize();
//                    Vec3 heading = this.getDeltaMovement().normalize();
//                    double dot = dirToTarget.dot(heading);
//                    double score = distSqr*Math.max((1-dot), 0.01);
//                    if(score < leastScore) {
//                        leastScore = score;
//                        this.targetShip = ship;
//                    }
//                }
//            }
//        }
    }

//    public static AABB toAABB(AABBdc i) {
//        return new AABB(
//                i.minX(), i.minY(), i.minZ(),
//                i.maxX(), i.maxY(), i.maxZ()
//        );
//    }

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
        //add control
        Vec3 control = new Vec3(0,0,0);
//        if(this.targetShip != null) {
//            //control here
//            Vector3dc shipVeldc = targetShip.getVelocity();
//            Vec3 shipVel = new Vec3(shipVeldc.x(), shipVeldc.y(), shipVeldc.z());
//            Vector3dc shipPosdc = targetShip.getTransform().getPositionInWorld();
//            Vec3 shipPos = new Vec3(shipPosdc.x(), shipPosdc.y(), shipPosdc.z()).add(shipVel.scale(0.15));
//            Vec3 dirToTarget = position.subtract(shipPos).normalize();
//            Vec3 heading = velocity.normalize(); //used to normalize
//            double dot = dirToTarget.dot(heading);
//            Vec3 lateral = dirToTarget.subtract(heading.scale(dot));
//            double lateralLength = lateral.length();
//            double vel = velocity.length();
//            double actual_force = lateralLength * vel * controlGain;
//            if(actual_force > maxG){
//                control = lateral.normalize().scale(-maxG).subtract(0,this.getGravity(),0);
//            }else{
//                control = lateral.scale(-vel*controlGain).subtract(0,this.getGravity(),0);
//            }
//        }
        return naturalForce.add(control);
    }

    protected abstract RackedLoiteringRocketProjectileProperties getAllProperties();
}
