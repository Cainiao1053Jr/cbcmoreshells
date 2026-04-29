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

    public static boolean hitAroundSublevel(Level level, Vec3 pos, int x, int y, int z) {
        if(Sable.HELPER.getContaining(level, pos) != null){
            return true;
        }
        AABB box = AABB.ofSize(pos, x, y, z);
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            return true;
        }
        return false;
    }

    public static boolean isOnSublevel(Level level, Vec3 pos){
        if(Sable.HELPER.getContaining(level, pos) != null){
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
        Vec3 vel = Sable.HELPER.getVelocity(level, sublevel, pos).multiply(0.00001, -0.00001, 0.00001);
        return new CBCMSCompatTransformers.SubLevelTarget(sublevel, pos, vel);
    }

    public static void init() {
        CBCMSCompatTransformers.addShootOnSublevelHandler(CBCMSSableCompat::hitAroundSublevel);
        CBCMSCompatTransformers.setSublevelSearchHandler(CBCMSSableCompat::findTarget);
        CBCMSCompatTransformers.setSublevelTrackHandler(CBCMSSableCompat::trackTarget);
        CBCMSCompatTransformers.addIsOnSublevelHandler(CBCMSSableCompat::isOnSublevel);
//        CBCCompatTransformers.addBlockPosTransformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addVec3Transformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addNormalTransformer(new SableCannonProjectileCompat.NormalTransformer());
//        CBCCompatTransformers.addProjectileFallHandler(new SableCannonProjectileCompat.ProjectileFallHandler());
//        CBCCompatTransformers.addProjectileGroundingHandler(SableCompat::groundProjectile);
    }

}
