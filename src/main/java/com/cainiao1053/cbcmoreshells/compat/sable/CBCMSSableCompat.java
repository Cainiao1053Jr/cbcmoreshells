package com.cainiao1053.cbcmoreshells.compat.sable;

import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.companion.math.BoundingBox3d;
import dev.ryanhcode.sable.sublevel.SubLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3dc;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;

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
        AABB searchBox = AABB.ofSize(pos, 1, 1, 1);
        List<SubLevel> sublevels = getSublevelAround(level, searchBox);
        if(!sublevels.isEmpty()){
            SubLevel sublevel = sublevels.get(0);
            return sublevel.boundingBox().toMojang();
        }
        return searchBox;
    }

    public static boolean hasShipsAround(Level level, AABB box) {
        return !getSublevelAround(level, box).isEmpty();
    }

    public static void spawnFireOnShip(Level level, AABB box, int radius, Vec3 pos) {
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            Vec3 localPos = sublevel.logicalPose().transformPositionInverse(pos);
            spawnFire(new BlockPos((int) localPos.x(),(int) localPos.y(), (int) localPos.z()), level, radius);
        }
        spawnFire(new BlockPos((int) pos.x(),(int) pos.y(),(int) pos.z()), level, radius);
    }

    public static void spawnFire(BlockPos root, Level level, int radius) {
        float chance = FluidBlobBurst.getBlockAffectChance();
        if (chance == 0)
            return;
        AABB bounds = new AABB(root).inflate(radius);
        BlockPos pos1 = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos pos2 = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
        for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
            if (level.getRandom().nextFloat() > chance)
                continue;
            BlockState state = level.getBlockState(pos);
            if (level.isEmptyBlock(pos)) {
                level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
            } else if (CandleBlock.canLight(state) || CampfireBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
                level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
            }
        }
    }

    public static void extinguishFireOnShip(Level level, AABB box, int radius, Vec3 pos) {
        Iterable<SubLevel> sublevels = Sable.HELPER.getAllIntersecting(level, new BoundingBox3d(box));
        for (SubLevel sublevel : sublevels) {
            Vec3 localPos = sublevel.logicalPose().transformPositionInverse(pos);
            extinguishFire(new BlockPos((int) localPos.x(),(int) localPos.y(), (int) localPos.z()), level, radius);
        }
        extinguishFire(new BlockPos((int) pos.x(),(int) pos.y(),(int) pos.z()), level, radius);
    }

    public static void extinguishFire(BlockPos root, Level level, int radius) {
        AABB bounds = new AABB(root).inflate(radius);
        BlockPos pos1 = BlockPos.containing(bounds.minX, bounds.minY, bounds.minZ);
        BlockPos pos2 = BlockPos.containing(bounds.maxX, bounds.maxY, bounds.maxZ);
        for (BlockPos pos : BlockPos.betweenClosed(pos1, pos2)) {
            BlockState state = level.getBlockState(pos);
            if(state.getBlock() instanceof BaseFireBlock){
                level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
            }else if(CandleBlock.canLight(state) || CampfireBlock.canLight(state) || CandleCakeBlock.canLight(state)){
                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 11);
                level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
            }
            //level.playSound(null, pos, SoundEvents., SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
//            if (level.isEmptyBlock(pos)) {
//                level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
//            } else if (CandleBlock.canLight(state) || CampfireBlock.canLight(state) || CandleCakeBlock.canLight(state)) {
//                level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F, level.getRandom().nextFloat() * 0.4F + 0.8F);
//                level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
//                level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
//            }
        }
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
        CBCMSCompatTransformers.addSpawnFireOnSublevelHandler(CBCMSSableCompat::spawnFireOnShip);
        CBCMSCompatTransformers.addhasShipsAroundHandler(CBCMSSableCompat::hasShipsAround);
        CBCMSCompatTransformers.addExtinguishFireOnSublevelHandler(CBCMSSableCompat::extinguishFireOnShip);
        CBCMSCompatTransformers.addGetShipAABBHandler(CBCMSSableCompat::getShipAABB);
//        CBCCompatTransformers.addBlockPosTransformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addVec3Transformer(SableCompat::transformFromShip);
//        CBCCompatTransformers.addNormalTransformer(new SableCannonProjectileCompat.NormalTransformer());
//        CBCCompatTransformers.addProjectileFallHandler(new SableCannonProjectileCompat.ProjectileFallHandler());
//        CBCCompatTransformers.addProjectileGroundingHandler(SableCompat::groundProjectile);
    }

}
