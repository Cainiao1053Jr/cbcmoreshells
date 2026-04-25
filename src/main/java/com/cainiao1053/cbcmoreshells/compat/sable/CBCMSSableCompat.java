package com.cainiao1053.cbcmoreshells.compat.sable;

import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;

import javax.annotation.Nullable;

public class CBCMSSableCompat {

//    public static BlockPos transformFromShip(Level level, BlockPos pos, BlockPos root) {
//        // Adapted from ActiveSableCompanion; this implementation is mainly leveraged for cannon particle handling as
//        // the cannon particle spawn points can be far away from the cannon mount. --ritchie
//        SubLevel sublevel = Sable.HELPER.getContaining(level, root);
//        if (sublevel == null)
//            return pos;
//        Pose3dc pose = level instanceof LevelPoseProviderExtension extension ? extension.sable$getPose(sublevel) : sublevel.logicalPose();
//        return BlockPos.containing(pose.transformPosition(Vec3.atCenterOf(pos)));
//    }
//
//    public static Vec3 transformFromShip(Level level, Vec3 pos, Vec3 root) {
//        // See the BlockPos version for commentary. --ritchie
//        SubLevel sublevel = Sable.HELPER.getContaining(level, root);
//        if (sublevel == null)
//            return pos;
//        Pose3dc pose = level instanceof LevelPoseProviderExtension extension ? extension.sable$getPose(sublevel) : sublevel.logicalPose();
//        return pose.transformPosition(pos);
//    }
//
//    public static boolean groundProjectile(Level level, AbstractCannonProjectile projectile, BlockPos impactPos) {
//        SubLevel sublevel = Sable.HELPER.getContaining(level, impactPos);
//        if (sublevel == null)
//            return false;
//        Vec3 projPos = projectile.position();
//        Pose3d pose = sublevel.logicalPose();
//        Vec3 shipPos = pose.transformPositionInverse(projPos);
//        Vec3 nudge = Vec3.atCenterOf(impactPos).subtract(shipPos).scale(0.05);
//        projectile.setPos(shipPos.add(nudge));
//        Vec3 orientation = projectile.getOrientation();
//        projectile.setOrientation(pose.transformNormalInverse(pose.transformNormalInverse(orientation)));
//        return true;
//    }

    public static boolean hitAroundSublevel(Level level, Vec3 pos){
        if(Sable.HELPER.getContaining(level, pos) != null){
            return true;
        }
        AABB box = AABB.ofSize(pos, 4, 4, 4);
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            return true;
        }
        return false;
    }

    @Nullable
    public static CBCMSCompatTransformers.SubLevelTarget findTarget(Level level, Vec3 pos, Vec3 dir,
                                                                     int searchLength, int searchWidth, float scanAngle) {
        Vec3 normalizedDir = dir.normalize();
        AABB searchBox = new AABB(pos, pos)
                .inflate(searchWidth)
                .expandTowards(normalizedDir.scale(searchLength))
                .move(normalizedDir.scale(searchWidth));

        double leastScore = Double.MAX_VALUE;
        SubLevel best = null;
        Vec3 bestPos = null;
        Vec3 bestVel = null;

        for (SubLevel sublevel : Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(searchBox))) {
            Vector3dc posdc = sublevel.logicalPose().position();
            Vec3 sublevelPos = new Vec3(posdc.x(), posdc.y(), posdc.z());
            Vec3 dirToTarget = sublevelPos.subtract(pos);
            double dist = dirToTarget.length();
            if (dist < 1e-6) continue;
            Vec3 dirToTargetNorm = dirToTarget.scale(1.0 / dist);
            double dot = dirToTargetNorm.dot(normalizedDir);
            if (dot > scanAngle) {
                double score = dist * Math.max(1.0 - dot, 0.01);
                if (score < leastScore) {
                    leastScore = score;
                    best = sublevel;
                    bestPos = sublevelPos;
                    bestVel = Sable.HELPER.getVelocity(level, sublevel, sublevelPos);
                }
            }
        }

        if (best == null) return null;
        return new CBCMSCompatTransformers.SubLevelTarget(best, bestPos, bestVel);
    }

    @Nullable
    public static CBCMSCompatTransformers.SubLevelTarget trackTarget(Level level, Object sublevelObj) {
        SubLevel sublevel = (SubLevel) sublevelObj;
        if (sublevel.isRemoved()) return null;
        Vector3dc posdc = sublevel.logicalPose().position();
        Vec3 pos = new Vec3(posdc.x(), posdc.y(), posdc.z());
        Vec3 vel = Sable.HELPER.getVelocity(level, sublevel, pos);
        return new CBCMSCompatTransformers.SubLevelTarget(sublevel, pos, vel);
    }

    public static void init() {
        CBCMSCompatTransformers.addShootOnSublevelHandler(CBCMSSableCompat::hitAroundSublevel);
        CBCMSCompatTransformers.setSublevelSearchHandler(CBCMSSableCompat::findTarget);
        CBCMSCompatTransformers.setSublevelTrackHandler(CBCMSSableCompat::trackTarget);
//        CBCCompatTransformers.addBlockPosTransformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addVec3Transformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addNormalTransformer(new SableCannonProjectileCompat.NormalTransformer());
//        CBCCompatTransformers.addProjectileFallHandler(new SableCannonProjectileCompat.ProjectileFallHandler());
//        CBCCompatTransformers.addProjectileGroundingHandler(SableCompat::groundProjectile);
    }

}
