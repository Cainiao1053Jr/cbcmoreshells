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
