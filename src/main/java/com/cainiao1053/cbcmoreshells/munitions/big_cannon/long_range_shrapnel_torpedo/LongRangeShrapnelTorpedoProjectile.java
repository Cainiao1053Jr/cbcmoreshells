package com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_shrapnel_torpedo;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedCannonTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.SharpnelTorpedoProperties;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProjectilePropertiesComponent;
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
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

import javax.annotation.Nonnull;

public class LongRangeShrapnelTorpedoProjectile extends FuzedCannonTorpedoProjectile {

	protected int lifetime = this.getAllProperties().lifetime();

	public LongRangeShrapnelTorpedoProjectile(EntityType<? extends LongRangeShrapnelTorpedoProjectile> type, Level level) {
		super(type, level);
	}



	@Override
	protected void detonate(Position position) {
		Vec3 oldDelta = this.getDeltaMovement().normalize().scale(4);
		SharpnelTorpedoProperties properties = this.getAllProperties();
		int burstCount = 0;
		if (normalDetonate()){burstCount =properties.torpedoBurst().burstProjectileCount();}

		ShellExplosion explosion = new ShellExplosion(this.level(), null, this.indirectArtilleryFire(false), position.x(),
				position.y(), position.z(), properties.explosion().explosivePower(), false,
				CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());

		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
		CBCProjectileBurst.spawnConeBurst(this.level(), CBCMSEntityTypes.TORPEDO_BURST.get(), new Vec3(position.x(), position.y(), position.z()),
				oldDelta, burstCount, properties.torpedoBurst().burstSpread());
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.LONG_RANGE_SHRAPNEL_TORPEDO.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
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
	protected TorpedoProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().torpedoProperties();
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

	protected SharpnelTorpedoProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE.getPropertiesOf(this);
	}

}
