package com.cainiao1053.cbcmoreshells.munitions.explosion;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.Nullable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.block_hit_effects.BlockImpactTransformationHandler;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.explosions.ShellBlastWaveEffectParticleData;
import rbasamoyai.createbigcannons.effects.particles.explosions.ShellExplosionCloudParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundCBCExplodePacket;
import rbasamoyai.createbigcannons.remix.CustomExplosion;

/**
 * 与 {@link rbasamoyai.createbigcannons.munitions.ShellExplosion} 行为一致的爆炸，唯一区别是
 * 额外接收一个抗性系数 {@code resistanceFactor}，用来在范围（size）不变的前提下缩放对方块的威力。
 *
 * <p>系数会通过 {@link ScaledResistanceExplosionDamageCalculator} 直接乘到每个方块的爆炸抗性上：
 * 系数 &gt; 1 = 方块更耐炸（威力削弱），系数 &lt; 1 = 方块更脆（威力增强），系数 = 1 时与
 * 原版 ShellExplosion 完全等价。</p>
 *
 * <p>穿透深度的近似公式：{@code 穿透格数 ≈ size / (1.05 + resistanceFactor * 方块爆炸抗性)}，
 * 空中最大射程恒为 {@code ≈ 1.333 * size}，不随系数变化。</p>
 *
 * <p>客户端表现沿用 CBC 自带的 SHELL 爆炸：威力计算只发生在服务端，被摧毁的方块列表随
 * {@link ClientboundCBCExplodePacket} 下发，因此无需任何客户端改动。</p>
 */
public class ScaledShellExplosion extends CustomExplosion.Impl {

	private final Set<BlockPos> changedBlocks = new HashSet<>();
	private final boolean isPlume;
	private final boolean noEffects;
	private final float resistanceFactor;

	public ScaledShellExplosion(Level level, @Nullable Entity source, DamageSource damageSource, double x, double y,
		double z, float size, float resistanceFactor, boolean fire, Level.ExplosionInteraction interaction,
		boolean noEffects) {
		super(level, source, damageSource, new ScaledResistanceExplosionDamageCalculator(source, resistanceFactor), x, y,
			z, size, fire, interaction);
		BlockPos pos = BlockPos.containing(this.x, this.y, this.z);
		this.isPlume = this.level.getBlockState(pos.above()).isAir() && !this.level.getBlockState(pos.below()).isAir();
		this.noEffects = noEffects;
		this.resistanceFactor = Math.max(0.0f, resistanceFactor);
	}

	public ScaledShellExplosion(Level level, @Nullable Entity source, DamageSource damageSource, double x, double y,
		double z, float size, float resistanceFactor, boolean fire, Level.ExplosionInteraction interaction) {
		this(level, source, damageSource, x, y, z, size, resistanceFactor, fire, interaction, false);
	}

	public float getResistanceFactor() {
		return this.resistanceFactor;
	}

	@Override
	protected void spawnParticles() {
		if (this.noEffects)
			return;
		Holder<SoundEvent> sound = BuiltInRegistries.SOUND_EVENT
			.wrapAsHolder(CBCSoundEvents.SHELL_EXPLOSION.getMainEvent());
		ShellBlastWaveEffectParticleData blastWave = new ShellBlastWaveEffectParticleData(this.size * 12.0f, sound,
			SoundSource.BLOCKS, Math.max(this.size * 2, 16.0f), 0.8f + this.level.random.nextFloat() * 0.4f, 2,
			this.size);
		ShellExplosionCloudParticleData cloud = new ShellExplosionCloudParticleData(this.size, this.isPlume);
		this.level.addAlwaysVisibleParticle(blastWave, true, this.x, this.y, this.z, 0, 0, 0);
		this.level.addAlwaysVisibleParticle(cloud, true, this.x, this.y, this.z, 0, 0, 0);
	}

	@Override
	public void editBlock(Level level, BlockPos pos, BlockState state, FluidState fluid, float power) {
		if (this.noEffects || !CBCConfigs.SERVER.munitions.projectilesChangeSurroundings.get()
			|| this.changedBlocks.contains(pos))
			return;
		level.setBlock(pos, BlockImpactTransformationHandler.transformBlock(state), 11);
		this.changedBlocks.add(pos);
	}

	@Override
	public void sendExplosionToClient(ServerPlayer player) {
		if (player.distanceToSqr(this.x, this.y, this.z) >= 263000.0d)
			return;
		Vec3 knockback = this.getHitPlayers().getOrDefault(player, Vec3.ZERO);
		ClientboundCBCExplodePacket.ExplosionType type = this.noEffects
			? ClientboundCBCExplodePacket.ExplosionType.SHELL_NO_EFFECTS
			: ClientboundCBCExplodePacket.ExplosionType.SHELL;
		NetworkPlatform.sendToClientPlayer(new ClientboundCBCExplodePacket(this.x, this.y, this.z, this.size,
			this.getToBlow(), (float) knockback.x, (float) knockback.y, (float) knockback.z, type), player);
	}

}
