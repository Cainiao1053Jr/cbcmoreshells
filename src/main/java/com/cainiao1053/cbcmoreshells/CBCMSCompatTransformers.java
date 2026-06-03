package com.cainiao1053.cbcmoreshells;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;

public class CBCMSCompatTransformers {

    private static final List<shootOnSublevelHandler> SHOOT_ON_SUBLEVEL = new ReferenceArrayList<>();

    public static void addShootOnSublevelHandler(shootOnSublevelHandler handler) {
        SHOOT_ON_SUBLEVEL.add(handler);
    }

    public static boolean shootOnSublevel(Level level, Vec3 pos) {
        return shootOnSublevel(level, pos, 4,4,4);
    }

    public static boolean shootOnSublevel(Level level, Vec3 pos, int x, int y, int z) {
        for (shootOnSublevelHandler t : SHOOT_ON_SUBLEVEL) {
            return  t.shootOnSublevel(level, pos, x, y, z);
        }
        return false;
    }

    public interface shootOnSublevelHandler {
        boolean shootOnSublevel(Level level, Vec3 pos, int x, int y, int z);
    }

    private static final List<hasShipsAroundHandler> HAS_SHIPS_AROUND = new ReferenceArrayList<>();

    public static void addhasShipsAroundHandler(hasShipsAroundHandler handler) {
        HAS_SHIPS_AROUND.add(handler);
    }

    public static boolean hasShipsAround(Level level, AABB box) {
        for (hasShipsAroundHandler t : HAS_SHIPS_AROUND) {
            return  t.hasShipsAround(level, box);
        }
        return false;
    }

    public interface hasShipsAroundHandler {
        boolean hasShipsAround(Level level, AABB box);
    }

    private static final List<isOnSublevelHandler> IS_ON_SUBLEVEL = new ReferenceArrayList<>();

    public static void addIsOnSublevelHandler(isOnSublevelHandler handler) {
        IS_ON_SUBLEVEL.add(handler);
    }

    public static boolean isOnSublevel(Level level, Vec3 pos) {
        for (isOnSublevelHandler t : IS_ON_SUBLEVEL) {
            return  t.isOnSublevel(level, pos);
        }
        return false;
    }

    public interface isOnSublevelHandler {
        boolean isOnSublevel(Level level, Vec3 pos);
    }

    public record SubLevelTarget(Object sublevel, Vec3 position, Vec3 velocity) {}

    public interface SubLevelSearchHandler {
        @Nullable SubLevelTarget findTarget(Level level, Vec3 pos, Vec3 dir,
                                            int searchLength, int searchWidth, float scanAngle);
    }

    public interface SubLevelTrackHandler {
        @Nullable SubLevelTarget trackTarget(Level level, Object sublevel);
    }

    private static SubLevelSearchHandler sublevelSearchHandler = null;
    private static SubLevelTrackHandler sublevelTrackHandler = null;

    public static void setSublevelSearchHandler(SubLevelSearchHandler handler) {
        sublevelSearchHandler = handler;
    }

    public static void setSublevelTrackHandler(SubLevelTrackHandler handler) {
        sublevelTrackHandler = handler;
    }

    @Nullable
    public static SubLevelTarget findSublevelTarget(Level level, Vec3 pos, Vec3 dir,
                                                    int searchLength, int searchWidth, float scanAngle) {
        return sublevelSearchHandler != null
                ? sublevelSearchHandler.findTarget(level, pos, dir, searchLength, searchWidth, scanAngle)
                : null;
    }

    @Nullable
    public static SubLevelTarget trackSublevelTarget(Level level, Object sublevel) {
        return sublevelTrackHandler != null
                ? sublevelTrackHandler.trackTarget(level, sublevel)
                : null;
    }

    private static final List<spawnFireOnSublevelHandler> SPAWNFIRE_ON_SUBLEVEL = new ReferenceArrayList<>();

    public static void addSpawnFireOnSublevelHandler(spawnFireOnSublevelHandler handler) {
        SPAWNFIRE_ON_SUBLEVEL.add(handler);
    }

    public static void spawnFireOnSublevel(Level level, AABB box, int radius, Vec3 pos) {
        for (spawnFireOnSublevelHandler t : SPAWNFIRE_ON_SUBLEVEL) {
            t.spawnFireOnSublevel(level,box, radius, pos);
            return;
        }
    }

    public interface spawnFireOnSublevelHandler {
        void spawnFireOnSublevel(Level level, AABB box, int radius, Vec3 pos);
    }

    private static final List<extinguishFireOnSublevelHandler> EXTINGUISH_FIRE_ON_SUBLEVEL = new ReferenceArrayList<>();

    public static void addExtinguishFireOnSublevelHandler(extinguishFireOnSublevelHandler handler) {
        EXTINGUISH_FIRE_ON_SUBLEVEL.add(handler);
    }

    public static void extinguishFireOnSublevel(Level level, AABB box, int radius, Vec3 pos) {
        for (extinguishFireOnSublevelHandler t : EXTINGUISH_FIRE_ON_SUBLEVEL) {
            t.extinguishFireOnSublevel(level,box, radius, pos);
            return;
        }
    }

    public interface extinguishFireOnSublevelHandler {
        void extinguishFireOnSublevel(Level level, AABB box, int radius, Vec3 pos);
    }

    private static final List<getShipAABBHandler> GET_SHIP_AABB = new ReferenceArrayList<>();

    public static void addGetShipAABBHandler(getShipAABBHandler handler) {
        GET_SHIP_AABB.add(handler);
    }

    public static AABB getShipAABB(Level level, Vec3 pos) {
        for (getShipAABBHandler t : GET_SHIP_AABB) {
            return t.getShipAABB(level, pos);
        }
        return AABB.ofSize(pos, 1, 1, 1);
    }

    public interface getShipAABBHandler {
        AABB getShipAABB(Level level,  Vec3 pos);
    }



}
