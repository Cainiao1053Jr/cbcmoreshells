package com.cainiao1053.cbcmoreshells;

import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CBCMSCompatTransformers {

    private static final List<shootOnSublevelHandler> SHOOT_ON_SUBLEVEL = new ReferenceArrayList<>();

    public static void addShootOnSublevelHandler(shootOnSublevelHandler handler) {
        SHOOT_ON_SUBLEVEL.add(handler);
    }

    public static boolean shootOnSublevel(Level level, Vec3 pos) {
        for (shootOnSublevelHandler t : SHOOT_ON_SUBLEVEL) {
            return  t.shootOnSublevel(level, pos);
        }
        return false;
    }

    public interface shootOnSublevelHandler {
        boolean shootOnSublevel(Level level, Vec3 pos);
    }

}
