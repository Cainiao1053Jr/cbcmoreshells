package com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_reductive_short_range_torpedo;

import javax.annotation.Nonnull;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.AbstractCannonTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.AbstractReductiveTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.FuzedCannonTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ShellessFuzedBigCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.BigCannonShellessShellProperties;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.ReductiveTorpedoProperties;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProjectilePropertiesComponent;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.TorpedoProperties;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
//import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class ReinforcedReductiveShortRangeTorpedoProjectile extends AbstractReductiveTorpedoProjectile {

	protected int lifetime = this.getAllProperties().lifetime();

	public ReinforcedReductiveShortRangeTorpedoProjectile(EntityType<? extends ReinforcedReductiveShortRangeTorpedoProjectile> type, Level level) {
		super(type, level);
	}



	@Override
	protected void detonate(Position position) {
		float explosivePower = this.getAllProperties().explosion().explosivePower();
		if (normalDetonate()){
			explosivePower *=4;
			if(explodeOnShip(new Vec3(position.x(), position.y(), position.z()), this.level())){
				reduceReloadTime();
				playSoundOnHit(this.level());
			}
		}

		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), explosivePower, false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
	}

	@Override
	public float getReductionPercentage() {
		return getAllProperties().reductiveProjectileProperties().percentReduction();
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.REINFORCED_REDUCTIVE_SHORT_RANGE_TORPEDO.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
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

	protected ReductiveTorpedoProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE.getPropertiesOf(this);
	}

}
