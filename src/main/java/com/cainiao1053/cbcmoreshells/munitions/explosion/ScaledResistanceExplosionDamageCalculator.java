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
		return this.delegate.getBlockExplosionResistance(explosion, reader, pos, state, fluid)
			.map(resistance -> resistance * this.resistanceFactor);
	}

	@Override
	public boolean shouldBlockExplode(Explosion explosion, BlockGetter reader, BlockPos pos, BlockState state,
		float power) {
		return this.delegate.shouldBlockExplode(explosion, reader, pos, state, power);
	}

}
