package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib;

import com.cainiao1053.cbcmoreshells.CBCMSCompatTransformers;
import com.cainiao1053.cbcmoreshells.utils.CBCMSMassiveBlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCandleBlock;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.CandleBlock;
import net.minecraft.world.level.block.CandleCakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.big_cannon.fluid_shell.FluidBlobBurst;

import java.util.ArrayList;
import java.util.List;

public final class CBCMSIncendiaryEffectService implements DualCannonIncendiaryService {

	/** Radius around the detonation searched for sub-level (ship) bodies to also set alight. */
	private static final double SUBLEVEL_SEARCH_INFLATION = 3.0D;

	private final DualCannonIncendiaryService delegate;

	private CBCMSIncendiaryEffectService(DualCannonIncendiaryService delegate) {
		this.delegate = delegate;
	}

	public static void install() {
		DualCannonIncendiaryService previous = DualCannonIncendiaryService.Holder.get();
		DualCannonIncendiaryService.Holder.set(new CBCMSIncendiaryEffectService(previous));
	}

	@Override
	public void ignite(ServerLevel level, Vec3 position, float fireChance, int fireRange) {
		if (this.delegate != null && this.delegate != DualCannonIncendiaryService.NOOP) {
			this.delegate.ignite(level, position, fireChance, fireRange);
		}
		if (Math.random() > fireChance) return;

		spawnFire(BlockPos.containing(position), level, fireRange);
		AABB searchRegion = new AABB(position, position).inflate(SUBLEVEL_SEARCH_INFLATION);
		CBCMSCompatTransformers.spawnFireOnSublevel(level, searchRegion, fireRange, position);
	}

	public static void spawnFire(BlockPos root, Level level, int radius) {
		float chance = FluidBlobBurst.getBlockAffectChance();
		if (chance == 0) return;
		List<BlockPos> toIgnite = new ArrayList<>();
		List<BlockPos> toLight = new ArrayList<>();
		// The projectile sits in a ticking chunk, so this small radius never needs to load chunks.
		CBCMSMassiveBlockUtil.forEachBlock(level, CBCMSMassiveBlockUtil.cube(root, radius), false, (pos, state) -> {
			if (level.getRandom().nextFloat() > chance)
				return;
			if (state.isAir()) {
				toIgnite.add(pos.immutable());
			} else if (canLight(state)) {
				toLight.add(pos.immutable());
			}
		});
		// Re-check before writing: earlier writes may have changed these blocks through neighbour updates.
		for (BlockPos pos : toIgnite) {
			if (level.isEmptyBlock(pos))
				level.setBlockAndUpdate(pos, BaseFireBlock.getState(level, pos));
		}
		for (BlockPos pos : toLight) {
			BlockState state = level.getBlockState(pos);
			if (!canLight(state))
				continue;
			level.playSound(null, pos, SoundEvents.FIRECHARGE_USE, SoundSource.BLOCKS, 1.0F,
				level.getRandom().nextFloat() * 0.4F + 0.8F);
			level.setBlock(pos, state.setValue(BlockStateProperties.LIT, true), 11);
			level.gameEvent(null, GameEvent.BLOCK_PLACE, pos);
		}
	}

	public static void extinguishFire(BlockPos root, Level level, int radius) {
		List<BlockPos> toClear = new ArrayList<>();
		List<BlockPos> toDouse = new ArrayList<>();
		CBCMSMassiveBlockUtil.forEachMatching(level, CBCMSMassiveBlockUtil.cube(root, radius), false,
			state -> state.getBlock() instanceof BaseFireBlock || isLit(state),
			(pos, state) -> (state.getBlock() instanceof BaseFireBlock ? toClear : toDouse).add(pos.immutable()));
		for (BlockPos pos : toClear) {
			if (level.getBlockState(pos).getBlock() instanceof BaseFireBlock)
				level.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
		}
		for (BlockPos pos : toDouse) {
			BlockState state = level.getBlockState(pos);
			if (!isLit(state))
				continue;
			level.setBlock(pos, state.setValue(BlockStateProperties.LIT, false), 11);
			level.gameEvent(null, GameEvent.BLOCK_CHANGE, pos);
		}
	}

	private static boolean canLight(BlockState state) {
		return CandleBlock.canLight(state) || CampfireBlock.canLight(state) || CandleCakeBlock.canLight(state);
	}

	/** Lit candles, candle cakes and campfires. */
	private static boolean isLit(BlockState state) {
		return (state.getBlock() instanceof AbstractCandleBlock || state.getBlock() instanceof CampfireBlock)
			&& state.getValue(BlockStateProperties.LIT);
	}

}
