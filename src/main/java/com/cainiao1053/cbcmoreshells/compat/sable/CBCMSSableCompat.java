package com.cainiao1053.cbcmoreshells.compat.sable;

import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSIncendiaryEffectService;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

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

    public static List<SubLevel> getSublevelAround(Level level, AABB box) {
        //AABB box = AABB.ofSize(pos, x, y, z);
        List<SubLevel> all_sublevels = new ArrayList<>();
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            all_sublevels.add(sublevel);
        }
        return all_sublevels;
    }

    public static AABB getShipAABB(Level level, Vec3 pos) {
        //AABB searchBox = AABB.ofSize(pos, 1, 1, 1);
        //List<SubLevel> sublevels = getSublevelAround(level, searchBox);
        SubLevel sublevel = Sable.HELPER.getContaining(level, pos);
//        if(!sublevels.isEmpty()){
//            SubLevel sublevel = sublevels.get(0);
//            //BoundingBox3d box = sublevel.boundingBox().transform(sublevel.logicalPose(), new BoundingBox3d());
//            //return box.toMojang();
//            return sublevel.boundingBox().toMojang();
//        }
        if(sublevel != null){
            return sublevel.boundingBox().toMojang();
        }
        return AABB.ofSize(pos, 1, 1, 1);
    }

    public static boolean hasShipsAround(Level level, AABB box) {
        return !getSublevelAround(level, box).isEmpty();
    }

    public static void spawnFireOnShip(Level level, AABB box, int radius, Vec3 pos) {
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            Vec3 localPos = sublevel.logicalPose().transformPositionInverse(pos);
            CBCMSIncendiaryEffectService.spawnFire(BlockPos.containing(localPos), level, radius);
        }
        CBCMSIncendiaryEffectService.spawnFire(BlockPos.containing(pos), level, radius);
    }

    public static void extinguishFireOnShip(Level level, AABB box, int radius, Vec3 pos) {
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            Vec3 localPos = sublevel.logicalPose().transformPositionInverse(pos);
            CBCMSIncendiaryEffectService.extinguishFire(BlockPos.containing(localPos), level, radius);
        }
        CBCMSIncendiaryEffectService.extinguishFire(BlockPos.containing(pos), level, radius);
    }

    public static boolean isOnSublevel(Level level, Vec3 pos){
        if(Sable.HELPER.getContaining(level, pos) != null){
            return true;
        }
        return false;
    }

    public static Vec3 rotateOnSublevel(Level level, Vec3 pos, Vec3 vec) {
        SubLevel sublevel = Sable.HELPER.getContaining(level, pos);
        if (sublevel == null) return vec;
        Vector3d rotated = sublevel.logicalPose().orientation().transform(new Vector3d(vec.x, vec.y, vec.z));
        return new Vec3(rotated.x, rotated.y, rotated.z);
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
        CBCMSCompatTransformers.addSpawnFireOnSublevelHandler(CBCMSSableCompat::spawnFireOnShip);
        CBCMSCompatTransformers.addhasShipsAroundHandler(CBCMSSableCompat::hasShipsAround);
        CBCMSCompatTransformers.addExtinguishFireOnSublevelHandler(CBCMSSableCompat::extinguishFireOnShip);
        CBCMSCompatTransformers.addGetShipAABBHandler(CBCMSSableCompat::getShipAABB);
        CBCMSCompatTransformers.addRotateOnSublevelHandler(CBCMSSableCompat::rotateOnSublevel);
//        CBCCompatTransformers.addBlockPosTransformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addVec3Transformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addNormalTransformer(new SableCannonProjectileCompat.NormalTransformer());
//        CBCCompatTransformers.addProjectileFallHandler(new SableCannonProjectileCompat.ProjectileFallHandler());
//        CBCCompatTransformers.addProjectileGroundingHandler(SableCompat::groundProjectile);
    }

}
