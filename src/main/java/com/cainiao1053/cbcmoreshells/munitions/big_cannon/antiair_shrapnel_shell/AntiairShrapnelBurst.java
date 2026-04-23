package com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell;

//import com.cainiao1053.cbcmoreshells.api.vs.ValkyrienSkies;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.index.CBCDamageTypes;
import rbasamoyai.createbigcannons.munitions.CannonDamageSource;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

import javax.annotation.Nullable;

public class AntiairShrapnelBurst extends CBCProjectileBurst {

	public AntiairShrapnelBurst(EntityType<? extends AntiairShrapnelBurst> entityType, Level level) { super(entityType, level); }

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide) {
			ParticleOptions trailParticle = this.getTrailParticle();
			if (trailParticle != null) {
				for (SubProjectile subProjectile : this.subProjectiles) {
					this.level().addParticle(trailParticle, this.getX() + subProjectile.displacement()[0],
						this.getY() + subProjectile.displacement()[1], this.getZ() + subProjectile.displacement()[2], 0, 0, 0);
				}
			}
		}
	}

	@Override
	protected void onSubProjectileHitEntity(EntityHitResult result, SubProjectile subProjectile) {
		EntityDamagePropertiesComponent properties = this.getProperties().damage();
		Entity entity = result.getEntity();
		if (properties == null || properties.ignoresInvulnerability())
			entity.invulnerableTime = 0;
		float damage = properties == null ? 0 : properties.entityDamage();
		entity.hurt(this.getDamageSource(), damage);
		if (properties == null || !properties.rendersInvulnerable())
			entity.invulnerableTime = 0;
	}

	@Override
	protected void onSubProjectileHitBlock(BlockHitResult result, SubProjectile subProjectile) {
		super.onSubProjectileHitBlock(result, subProjectile);

		BlockPos pos = result.getBlockPos();
		BlockState state = this.level().getChunk(pos).getBlockState(pos);
		if (!this.level().isClientSide && state.getDestroySpeed(this.level(), pos) != -1 && this.canDestroyBlock(state)) {
			double shipVel = 0;
			Vector3dc shipPos = new Vector3d(0,0,0);
//			Ship ship = getShipOn(level(),pos);
//			if(ship != null) {
//				shipVel = ship.getVelocity().length();
//				shipPos = ship.getTransform().getPositionInWorld();
//			}
			double penetration = 0.6 * (shipVel) * Math.max(Math.min(shipPos.y()-80,240),40)/160;

			Vec3 curVel = new Vec3(subProjectile.velocity()[0], subProjectile.velocity()[1], subProjectile.velocity()[2]);
			double curPom = this.getProperties().ballistics().durabilityMass() * curVel.length();
			double toughness = BlockArmorPropertiesHandler.getProperties(state).toughness(this.level(), state, pos, true);
			double penetrateRate = 0.6 * Math.min(penetration, toughness)/toughness;
			if(penetrateRate > Math.random()){
				this.level().setBlock(pos, Blocks.AIR.defaultBlockState(), ProjectileBlock.UPDATE_ALL_IMMEDIATE);
			}
//			BlockPos pos1 = pos.immutable();
//			CreateBigCannons.BLOCK_DAMAGE.damageBlock(pos1, (int) Math.min(curPom, toughness), state, this.level(), PartialBlockDamageManager::voidBlock);
		}
		if (this.level() instanceof ServerLevel slevel) {
			ParticleOptions options = new BlockParticleOption(ParticleTypes.BLOCK, state);
			for (ServerPlayer player : slevel.players()) {
				if (player.distanceToSqr(pos.getX(), pos.getY(), pos.getZ()) < 1024d)
					slevel.sendParticles(player, options, true, pos.getX(), pos.getY(), pos.getZ(), 20, 0.4, 2, 0.4, 1);
			}
		}
//		SoundType type = state.getSoundType();
//		this.level().playLocalSound(pos.getX(), pos.getY(), pos.getZ(), type.getBreakSound(), SoundSource.NEUTRAL,
//			type.getVolume() * 2, type.getPitch(), false);
	}

	protected boolean canDestroyBlock(BlockState state) { return true; }

	@Nullable public ParticleOptions getTrailParticle() { return ParticleTypes.SMOKE; }

	@Override public double getSubProjectileWidth() { return 0.8; }
	@Override public double getSubProjectileHeight() { return 0.8; }

	protected DamageSource getDamageSource() {
		return new CannonDamageSource(CannonDamageSource.getDamageRegistry(this.level()).getHolderOrThrow(CBCDamageTypes.SHRAPNEL),
			this.getProperties().damage().ignoresEntityArmor());
	}

//	protected @Nullable Ship getShipOn(Level level, BlockPos pos) {
//		return ValkyrienSkies.getShipManagingBlock(level, pos);
//	}

}
