package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.racked_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.AbstractRackedTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedProjectilePropertiesComponent;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedTorpedoProjectileProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class RackedTorpedoProjectile extends AbstractRackedTorpedoProjectile {

	protected int lifetime = this.getAllProperties().lifetime();
	protected float explosivePower = 0;
	protected int explisionCooldown = this.getAllProperties().explosionCooldown();

	public RackedTorpedoProjectile(EntityType<? extends RackedTorpedoProjectile> type, Level level) {
		super(type, level);
	}
	

	@Override
	protected void detonate(Position position) {
		if(this.getTickInWater() > explisionCooldown) {
			this.explosivePower = this.getAllProperties().explosion().explosivePower();
		}
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.RACKED_TORPEDO.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
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

	@Override
	public void playFlybySound(Vec3 originPos, Vec3 displacement, double radius) {
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
	protected RackedTorpedoProjectileProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.RACKED_TORPEDO.getPropertiesOf(this);
	}
}
