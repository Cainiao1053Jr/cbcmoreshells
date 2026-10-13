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

/**
 * Batch block access over a box region. Chunks are fetched once per chunk and blocks are
 * read straight from their 16x16x16 sections, so whole sections can be skipped when they are
 * all air or their palette cannot contain the wanted states.
 *
 * <p>Reads only: writes must still go through {@link Level#setBlock} so neighbour updates,
 * lighting, client sync and block entities are handled.
 *
 * <p>Must be called on the thread that owns the level (server thread / client render thread).
 *
 * <p>{@code loadChunks}: {@code false} skips chunks that are not loaded (never triggers loading);
 * {@code true} loads (and if needed generates) them synchronously.
 *
 * <p>Visitors receive a shared {@link BlockPos.MutableBlockPos}; call {@code pos.immutable()}
 * before storing it.
 */
public final class CBCMSMassiveBlockUtil {

    private CBCMSMassiveBlockUtil() {
    }

    @FunctionalInterface
    public interface BlockVisitor {
        void visit(BlockPos.MutableBlockPos pos, BlockState state);
    }

    /** Return {@code false} to stop the scan. */
    @FunctionalInterface
    public interface BlockSearcher {
        boolean visit(BlockPos.MutableBlockPos pos, BlockState state);
    }

    /** Bounds are inclusive and already clipped to the chunk and the box (Y is not clipped to build height). */
    @FunctionalInterface
    public interface ChunkVisitor {
        void visit(LevelChunk chunk, int minX, int minY, int minZ, int maxX, int maxY, int maxZ);
    }

    // ---- regions ----

    /** Inclusive box between two corners, in any order. */
    public static BoundingBox box(BlockPos a, BlockPos b) {
        return BoundingBox.fromCorners(a, b);
    }

    /** All blocks whose cells intersect the AABB. A zero-size AABB yields the block containing it. */
    public static BoundingBox box(AABB aabb) {
        int minX = Mth.floor(aabb.minX);
        int minY = Mth.floor(aabb.minY);
        int minZ = Mth.floor(aabb.minZ);
        int maxX = Math.max(minX, Mth.ceil(aabb.maxX) - 1);
        int maxY = Math.max(minY, Mth.ceil(aabb.maxY) - 1);
        int maxZ = Math.max(minZ, Mth.ceil(aabb.maxZ) - 1);
        return new BoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    /** Symmetric cube [center - radius, center + radius], i.e. (2r+1)^3 blocks. */
    public static BoundingBox cube(BlockPos center, int radius) {
        return new BoundingBox(center).inflatedBy(Math.max(radius, 0));
    }

    // ---- chunks ----

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

    // ---- block iteration ----

    public static void forEachBlock(Level level, BoundingBox box, boolean loadChunks, BlockVisitor visitor) {
        scan(level, box, loadChunks, false, null, toSearcher(visitor));
    }

    public static void forEachBlock(Level level, AABB aabb, boolean loadChunks, BlockVisitor visitor) {
        forEachBlock(level, box(aabb), loadChunks, visitor);
    }

    public static void forEachBlock(Level level, BlockPos a, BlockPos b, boolean loadChunks, BlockVisitor visitor) {
        forEachBlock(level, box(a, b), loadChunks, visitor);
    }

    /** Like {@link #forEachBlock} but skips air, including whole all-air sections. */
    public static void forEachNonAir(Level level, BoundingBox box, boolean loadChunks, BlockVisitor visitor) {
        scan(level, box, loadChunks, true, null, toSearcher(visitor));
    }

    public static void forEachNonAir(Level level, AABB aabb, boolean loadChunks, BlockVisitor visitor) {
        forEachNonAir(level, box(aabb), loadChunks, visitor);
    }

    public static void forEachNonAir(Level level, BlockPos a, BlockPos b, boolean loadChunks, BlockVisitor visitor) {
        forEachNonAir(level, box(a, b), loadChunks, visitor);
    }

    /**
     * Visits only blocks matching {@code filter}. Sections whose palette cannot contain a matching
     * state are skipped without reading any block, so the filter must depend on the state alone.
     */
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

    /** Visits every block until the searcher returns {@code false}. Returns {@code true} if it ran to the end. */
    public static boolean search(Level level, BoundingBox box, boolean loadChunks, BlockSearcher searcher) {
        return scan(level, box, loadChunks, false, null, searcher);
    }

    public static boolean search(Level level, AABB aabb, boolean loadChunks, BlockSearcher searcher) {
        return search(level, box(aabb), loadChunks, searcher);
    }

    public static boolean search(Level level, BlockPos a, BlockPos b, boolean loadChunks, BlockSearcher searcher) {
        return search(level, box(a, b), loadChunks, searcher);
    }

    // ---- convenience ----

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

    /**
     * Scans first, then writes every changed state through {@link Level#setBlock}, so updates caused
     * by the writes cannot affect what the scan sees. Returns the number of blocks actually changed.
     */
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

    // ---- block entities / fluids ----

    public static <T extends BlockEntity> void forEachBlockEntity(Level level, BoundingBox box, boolean loadChunks,
                                                                  Class<T> type, Consumer<T> consumer) {
        forEachChunk(level, box, loadChunks, (chunk, minX, minY, minZ, maxX, maxY, maxZ) -> {
            if (chunk.getBlockEntities().isEmpty()) return;
            for (BlockEntity be : List.copyOf(chunk.getBlockEntities().values())) {
                if (type.isInstance(be) && box.isInside(be.getBlockPos())) consumer.accept(type.cast(be));
            }
        });
    }

    /** Visits non-empty fluid states only (source/flowing fluids and waterlogged blocks). */
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

    /** Returns {@code false} if the searcher stopped the scan early. */
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
