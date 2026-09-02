package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.depth_charge;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.explosion.ScaledShellExplosion;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.AbstractDepthChargeProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedProjectilePropertiesComponent;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedShrapnelProjectileProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

import javax.annotation.Nonnull;

public class DepthChargeProjectile extends AbstractDepthChargeProjectile {

	protected int lifetime = this.getAllProperties().lifetime();
	protected float explosivePower = 0;
	protected int explisionCooldown = this.getAllProperties().explosionCooldown();

	public DepthChargeProjectile(EntityType<? extends DepthChargeProjectile> type, Level level) {
		super(type, level);
	}
	

	@Override
	protected void detonate(Position position) {
		int burstCount = 2;
		RackedShrapnelProjectileProperties properties = this.getAllProperties();
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
		if(this.getAge() > explisionCooldown && !fluidState.isEmpty()){
			this.explosivePower = this.getAllProperties().explosion().explosivePower();
			if(!level().isClientSide()){
				destroyTorpedo(position, (ServerLevel) level());
				burstCount =properties.burst().burstProjectileCount();
			}
		}
		Vec3 oldDelta = this.getDeltaMovement().normalize().scale(5);
		ScaledShellExplosion explosion = new ScaledShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, 0.45f, false,
			CBCConfigs.SERVER.munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		CBCProjectileBurst.spawnConeBurst(this.level(), CBCMSEntityTypes.TORPEDO_BURST.get(), new Vec3(position.x(), position.y(), position.z()),
				oldDelta, burstCount, properties.burst().burstSpread());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.DEPTH_CHARGE.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Override
	public void setChargePower(float power) {
		setLifetime(lifetime);
	}


	@Nonnull
	@Override
	protected RackedProjectilePropertiesComponent getRackedProjectileProperties() {
		return this.getAllProperties().rackedProjectileProperties();
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	@Override
	protected RackedShrapnelProjectileProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.RACKED_SHRAPNEL_PROJECTILE.getPropertiesOf(this);
	}

}
