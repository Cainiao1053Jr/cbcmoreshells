package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_antiair_he_shell;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.FuzedDualCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonPropertiesComponent;
import net.minecraft.core.Position;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class ExtendedAntiairHEShellProjectile extends FuzedDualCannonProjectile {
	protected double yInit = 68;

	public ExtendedAntiairHEShellProjectile(EntityType<? extends ExtendedAntiairHEShellProjectile> type, Level level) {
		super(type, level);
	}

	@Override
	protected void detonate(Position position) {
		float deltaY = (float) (this.getY() - yInit);
		float explosionMultiplier = Math.min(Math.max(0,deltaY)/80 + 1,3);
		float explosivePower = this.getAllProperties().explosion().explosivePower()*((this.getDurabilityModifier()-1)/1.5f+1) * explosionMultiplier;
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.NORMAL_ANTIAIR_HE_SHELL_BLOCK.getDefaultState();
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Override
	public void setChargePower(float power) {
		yInit = this.getY();
	}


	@Nonnull
	@Override
	protected DualCannonPropertiesComponent getDualCannonProjectileProperties() {
		return this.getAllProperties().dualCannonProperties();
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

	protected DualCannonProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES.getPropertiesOf(this);
	}

}
