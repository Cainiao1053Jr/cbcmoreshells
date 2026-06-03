package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_rocket;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.AbstractRackedRocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedProjectilePropertiesComponent;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.RackedRocketProjectileProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class HERocketProjectile extends AbstractRackedRocketProjectile {

	protected int lifetime = this.getAllProperties().lifetime();
	protected float explosivePower = 0;
	protected int explisionCooldown = this.getAllProperties().explosionCooldown();

	public HERocketProjectile(EntityType<? extends HERocketProjectile> type, Level level) {
		super(type, level);
	}
	

	@Override
	protected void detonate(Position position) {
		if(this.getAge() > explisionCooldown) {
			this.explosivePower = this.getAllProperties().explosion().blockDamagePower();
		}
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.HE_ROCKET.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
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
	protected RackedRocketProjectileProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.RACKED_ROCKET.getPropertiesOf(this);
	}
}
