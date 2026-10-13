package com.cainiao1053.cbcmoreshells.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class CBCMSMassiveBlockUtil {

    private CBCMSMassiveBlockUtil() {
    }

    @FunctionalInterface
    public interface BlockVisitor {
        void visit(BlockPos.MutableBlockPos pos, BlockState state);
    }

    @FunctionalInterface
    public interface BlockSearcher {
        boolean visit(BlockPos.MutableBlockPos pos, BlockState state);
    }

    @FunctionalInterface
    public interface ChunkVisitor {
        void visit(LevelChunk chunk, int minX, int minY, int minZ, int maxX, int maxY, int maxZ);
    }

    public static BoundingBox box(BlockPos a, BlockPos b) {
        return BoundingBox.fromCorners(a, b);
    }

    public static BoundingBox box(AABB aabb) {
        int minX = Mth.floor(aabb.minX);
        int minY = Mth.floor(aabb.minY);
        int minZ = Mth.floor(aabb.minZ);
        int maxX = Math.max(minX, Mth.ceil(aabb.maxX) - 1);
        int maxY = Math.max(minY, Mth.ceil(aabb.maxY) - 1);
        int maxZ = Math.max(minZ, Mth.ceil(aabb.maxZ) - 1);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static BoundingBox cube(BlockPos center, int radius) {
        return new BoundingBox(center).inflatedBy(Math.max(radius, 0));
    }

    @Nullable
    public static LevelChunk getChunk(Level level, int chunkX, int chunkZ, boolean loadChunks) {
        return loadChunks ? level.getChunk(chunkX, chunkZ) : level.getChunkSource().getChunkNow(chunkX, chunkZ);
    }

    public static void forEachChunk(Level level, BoundingBox box, boolean loadChunks, ChunkVisitor visitor) {
        for (int cx = box.minX() >> 4; cx <= box.maxX() >> 4; cx++) {
            for (int cz = box.minZ() >> 4; cz <= box.maxZ() >> 4; cz++) {
                LevelChunk chunk = getChunk(level, cx, cz, loadChunks);
                if (chunk == null) continue;
                visitor.visit(chunk,
                        Math.max(box.minX(), cx << 4), box.minY(), Math.max(box.minZ(), cz << 4),
                        Math.min(box.maxX(), (cx << 4) + 15), box.maxY(), Math.min(box.maxZ(), (cz << 4) + 15));
            }
        }
    }

    public static void forEachBlock(Level level, BoundingBox box, boolean loadChunks, BlockVisitor visitor) {
        scan(level, box, loadChunks, false, null, toSearcher(visitor));
    }

    public static void forEachBlock(Level level, AABB aabb, boolean loadChunks, BlockVisitor visitor) {
        forEachBlock(level, box(aabb), loadChunks, visitor);
    }

    public static void forEachBlock(Level level, BlockPos a, BlockPos b, boolean loadChunks, BlockVisitor visitor) {
        forEachBlock(level, box(a, b), loadChunks, visitor);
    }

    public static void forEachNonAir(Level level, BoundingBox box, boolean loadChunks, BlockVisitor visitor) {
        scan(level, box, loadChunks, true, null, toSearcher(visitor));
    }

    public static void forEachNonAir(Level level, AABB aabb, boolean loadChunks, BlockVisitor visitor) {
        forEachNonAir(level, box(aabb), loadChunks, visitor);
    }

    public static void forEachNonAir(Level level, BlockPos a, BlockPos b, boolean loadChunks, BlockVisitor visitor) {
        forEachNonAir(level, box(a, b), loadChunks, visitor);
    }

    public static void forEachMatching(Level level, BoundingBox box, boolean loadChunks,
                                       Predicate<BlockState> filter, BlockVisitor visitor) {
        scan(level, box, loadChunks, false, filter, toSearcher(visitor));
    }

    public static void forEachMatching(Level level, AABB aabb, boolean loadChunks,
                                       Predicate<BlockState> filter, BlockVisitor visitor) {
        forEachMatching(level, box(aabb), loadChunks, filter, visitor);
    }

    public static void forEachMatching(Level level, BlockPos a, BlockPos b, boolean loadChunks,
                                       Predicate<BlockState> filter, BlockVisitor visitor) {
        forEachMatching(level, box(a, b), loadChunks, filter, visitor);
    }

    public static boolean search(Level level, BoundingBox box, boolean loadChunks, BlockSearcher searcher) {
        return scan(level, box, loadChunks, false, null, searcher);
    }

    public static boolean search(Level level, AABB aabb, boolean loadChunks, BlockSearcher searcher) {
        return search(level, box(aabb), loadChunks, searcher);
    }

    public static boolean search(Level level, BlockPos a, BlockPos b, boolean loadChunks, BlockSearcher searcher) {
        return search(level, box(a, b), loadChunks, searcher);
    }

    public static boolean anyMatch(Level level, BoundingBox box, boolean loadChunks, Predicate<BlockState> filter) {
        return !scan(level, box, loadChunks, false, filter, (pos, state) -> false);
    }

    public static int count(Level level, BoundingBox box, boolean loadChunks, Predicate<BlockState> filter) {
        int[] count = {0};
        forEachMatching(level, box, loadChunks, filter, (pos, state) -> count[0]++);
        return count[0];
    }

    public static List<BlockPos> collect(Level level, BoundingBox box, boolean loadChunks, Predicate<BlockState> filter) {
        List<BlockPos> result = new ArrayList<>();
        forEachMatching(level, box, loadChunks, filter, (pos, state) -> result.add(pos.immutable()));
        return result;
    }

    public static int replace(Level level, BoundingBox box, boolean loadChunks, Predicate<BlockState> filter,
                              Function<BlockState, BlockState> mapper, int flags) {
        List<BlockPos> positions = new ArrayList<>();
        List<BlockState> states = new ArrayList<>();
        forEachMatching(level, box, loadChunks, filter, (pos, state) -> {
            BlockState replacement = mapper.apply(state);
            if (replacement != state) {
                positions.add(pos.immutable());
                states.add(replacement);
            }
        });
        int changed = 0;
        for (int i = 0; i < positions.size(); i++) {
            if (level.setBlock(positions.get(i), states.get(i), flags)) changed++;
        }
        return changed;
    }

    public static <T extends BlockEntity> void forEachBlockEntity(Level level, BoundingBox box, boolean loadChunks,
                                                                  Class<T> type, Consumer<T> consumer) {
        forEachChunk(level, box, loadChunks, (chunk, minX, minY, minZ, maxX, maxY, maxZ) -> {
            if (chunk.getBlockEntities().isEmpty()) return;
            for (BlockEntity be : List.copyOf(chunk.getBlockEntities().values())) {
                if (type.isInstance(be) && box.isInside(be.getBlockPos())) consumer.accept(type.cast(be));
            }
        });
    }

    public static void forEachFluid(Level level, BoundingBox box, boolean loadChunks,
                                    BiConsumer<BlockPos.MutableBlockPos, FluidState> consumer) {
        scan(level, box, loadChunks, true, state -> !state.getFluidState().isEmpty(), (pos, state) -> {
            consumer.accept(pos, state.getFluidState());
            return true;
        });
    }

    // ---- core ----

    private static BlockSearcher toSearcher(BlockVisitor visitor) {
        return (pos, state) -> {
            visitor.visit(pos, state);
            return true;
        };
    }

    private static boolean scan(Level level, BoundingBox box, boolean loadChunks, boolean skipAir,
                                @Nullable Predicate<BlockState> filter, BlockSearcher searcher) {
        int minY = Math.max(box.minY(), level.getMinBuildHeight());
        int maxY = Math.min(box.maxY(), level.getMaxBuildHeight() - 1);
        if (minY > maxY) return true;

        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        for (int cx = box.minX() >> 4; cx <= box.maxX() >> 4; cx++) {
            int x0 = Math.max(box.minX(), cx << 4);
            int x1 = Math.min(box.maxX(), (cx << 4) + 15);
            for (int cz = box.minZ() >> 4; cz <= box.maxZ() >> 4; cz++) {
                LevelChunk chunk = getChunk(level, cx, cz, loadChunks);
                if (chunk == null) continue;
                int z0 = Math.max(box.minZ(), cz << 4);
                int z1 = Math.min(box.maxZ(), (cz << 4) + 15);
                LevelChunkSection[] sections = chunk.getSections();

                for (int sy = minY >> 4; sy <= maxY >> 4; sy++) {
                    LevelChunkSection section = sections[chunk.getSectionIndexFromSectionY(sy)];
                    if (skipAir && section.hasOnlyAir()) continue;
                    if (filter != null && !section.maybeHas(filter)) continue;
                    int y0 = Math.max(minY, sy << 4);
                    int y1 = Math.min(maxY, (sy << 4) + 15);

                    // y -> z -> x matches the section storage order
                    for (int y = y0; y <= y1; y++) {
                        for (int z = z0; z <= z1; z++) {
                            for (int x = x0; x <= x1; x++) {
                                BlockState state = section.getBlockState(x & 15, y & 15, z & 15);
                                if (skipAir && state.isAir()) continue;
                                if (filter != null && !filter.test(state)) continue;
                                if (!searcher.visit(pos.set(x, y, z), state)) return false;
                            }
                        }
                    }
                }
            }
        }
        return true;
    }
}
