//package com.cainiao1053.cbcmoreshells.utils;
//
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.world.phys.AABB;
//import org.jetbrains.annotations.NotNull;
//import org.valkyrienskies.core.api.ships.ServerShip;
//import org.valkyrienskies.mod.common.VSGameUtilsKt;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class VSShipUtils {
//
//    /**
//     * 返回在给定世界空间 AABB 内所有相交的 ServerShip。
//     * 与 ValkyrienSkies.getShipsIntersecting 不同，本方法：
//     *  1. 接受 ServerLevel（避免泛型 Level 在 VS2 内部 cast 失败）
//     *  2. 直接返回 ServerShip，可访问变换矩阵、施力等服务端 API
//     */
////    @NotNull
////    public static List<ServerShip> getServerShipsInAABB(@NotNull ServerLevel level, @NotNull AABB box) {
////        Iterable<? extends ServerShip> ships = VSGameUtilsKt.getShipsIntersecting(level, box);
////        List<ServerShip> result = new ArrayList<>();
////        for (ServerShip ship : ships) {
////            result.add(ship);
////        }
////        return result;
////    }
//
//    /** 返回 AABB 内是否存在任意 ServerShip */
//    public static boolean hasShipInAABB(@NotNull ServerLevel level, @NotNull AABB box) {
//        return VSGameUtilsKt.getShipsIntersecting(level, box).iterator().hasNext();
//    }
//}
