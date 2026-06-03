package com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessFuzedBigCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.ShrapnelShellessShellProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.shrapnel.ShrapnelExplosion;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

import javax.annotation.Nonnull;

public class AntiairShrapnelShellProjectile extends ShellessFuzedBigCannonProjectile {

	public AntiairShrapnelShellProjectile(EntityType<? extends AntiairShrapnelShellProjectile> type, Level level) {
		super(type, level);
	}

	protected int lifetime = this.getAllProperties().lifetime();

	@Override
	protected void detonate(Position position) {
		Vec3 oldDelta = this.getDeltaMovement();
		ShrapnelShellessShellProperties properties = this.getAllProperties();
		ShrapnelExplosion explosion = new ShrapnelExplosion(this.level(), null, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), properties.explosion().blockDamagePower(),properties.explosion().blockDamagePower(),
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		CBCProjectileBurst.spawnConeBurst(this.level(), CBCMSEntityTypes.ANTIAIR_SHRAPNEL_BURST.get(), new Vec3(position.x(), position.y(), position.z()),
			oldDelta, properties.burst().burstProjectileCount(), properties.burst().burstSpread());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.ANTIAIR_HE_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Override
	public void setChargePower(float power) {
		float maxCharges = this.getAllProperties().maxCharges();
		this.tooManyCharges = maxCharges >= 0 && power > maxCharges;
		setLifetime(lifetime);
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
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

	protected ShrapnelShellessShellProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.SHRAPNEL_SHELLESS_BIG_CANNON_PROJECTILE.getPropertiesOf(this);
	}

}
