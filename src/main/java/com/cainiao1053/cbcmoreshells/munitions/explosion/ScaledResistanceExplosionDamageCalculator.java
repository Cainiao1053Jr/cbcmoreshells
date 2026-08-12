package com.cainiao1053.cbcmoreshells.munitions.explosion;

import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.EntityBasedExplosionDamageCalculator;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;

/**
 * 在原版爆炸抗性上直接乘一个系数，用于在不改动爆炸半径（范围）的前提下缩放爆炸对方块的威力。
 *
 * <p>原版 {@link Explosion#explode()} 的每一步射线扣除两项能量：固定的 0.225（距离项，决定范围）
 * 与 (抗性 + 0.3) * 0.3（抗性项，决定穿透力）。本类只放大/缩小抗性项，因此空中射程不受影响。
 * 系数 &gt; 1 表示方块更耐炸（威力削弱），&lt; 1 表示方块更脆（威力增强）。</p>
 *
 * <p>内部委托给原版会用的计算器：有来源实体时用 {@link EntityBasedExplosionDamageCalculator}，
 * 否则用 {@link ExplosionDamageCalculator}，以保留 Forge / 其它 mod 对方块抗性的自定义逻辑。</p>
 */
public class ScaledResistanceExplosionDamageCalculator extends ExplosionDamageCalculator {

	private final ExplosionDamageCalculator delegate;
	private final float resistanceFactor;

	public ScaledResistanceExplosionDamageCalculator(@Nullable Entity source, float resistanceFactor) {
		this.delegate = source == null ? new ExplosionDamageCalculator() : new EntityBasedExplosionDamageCalculator(source);
		this.resistanceFactor = Math.max(0.0f, resistanceFactor);
	}

	public float getResistanceFactor() {
		return this.resistanceFactor;
	}

	@Override
	public Optional<Float> getBlockExplosionResistance(Explosion explosion, BlockGetter reader, BlockPos pos,
		BlockState state, FluidState fluid) {
		// 空气会返回 Optional.empty()，此处必须保持为空，否则空中射程会被额外削减
		return this.delegate.getBlockExplosionResistance(explosion, reader, pos, state, fluid)
			.map(resistance -> resistance * this.resistanceFactor);
	}

	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state,
		float power) {
		return this.delegate.shouldBlockExplode(explosion, reader, pos, state, power);
	}

}
