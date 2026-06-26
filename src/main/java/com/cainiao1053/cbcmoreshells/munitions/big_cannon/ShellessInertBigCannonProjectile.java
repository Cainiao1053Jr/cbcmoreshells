package com.cainiao1053.cbcmoreshells.munitions.big_cannon;

import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.block_armor_properties.BlockArmorPropertiesHandler;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;
import org.valkyrienskies.mod.common.util.IEntityDraggingInformationProvider;
import rbasamoyai.createbigcannons.munitions.fragment_burst.CBCProjectileBurst;

public abstract class ShellessInertBigCannonProjectile extends AbstractBigCannonProjectile {

	public boolean tooManyCharges = false;
	protected int ageRemaining;
	protected Long vs$embeddedShipId = null;

	protected ShellessInertBigCannonProjectile(EntityType<? extends ShellessInertBigCannonProjectile> type, Level level) {
		super(type, level);
	}


	@Override
	public void tick() {
		if (this.tooManyCharges) {
			if (this.level() instanceof ServerLevel slevel) {
				slevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, this.getRenderedBlockState()), this.getX(), this.getY(), this.getZ(), 40, 0.1f, 0.1f, 0.1f, 0.01d);
			}
			SoundType soundType = this.getRenderedBlockState().getSoundType();
			this.playSound(soundType.getBreakSound(), soundType.getVolume() * 0.5f, soundType.getPitch() * 0.75f);
			this.discard();
			return;
		}
		super.tick();

		if (!this.level().isClientSide) {
			this.ageRemaining--;
			if (this.ageRemaining <= 0)
				this.expireProjectile();
			if (vs$embeddedShipId != null && this.isInGround()) {
				((IEntityDraggingInformationProvider) this)
						.getDraggingInformation()
						.setLastShipStoodOn(vs$embeddedShipId);
			}
		}
	}

	protected ImpactResult vsCheckHead() {
		if (vs$embeddedShipId != null)
			return new ImpactResult(ImpactResult.KinematicOutcome.STOP, false);
		return null;
	}

	protected void vsCheckReturn(ImpactResult result, BlockHitResult bhr) {
		if (result.kinematics() == ImpactResult.KinematicOutcome.STOP && !this.level().isClientSide) {
			LoadedShip ship = VSGameUtilsKt.getShipObjectManagingPos(this.level(), bhr.getBlockPos());
			if (ship != null)
				vs$embeddedShipId = ship.getId();
		}
	}

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {
		ImpactResult vsEarly = vsCheckHead();
		if (vsEarly != null) return vsEarly;
		ImpactResult result = super.calculateBlockPenetration(projectileContext, state, blockHitResult);
		if(result.kinematics() == ImpactResult.KinematicOutcome.PENETRATE) {
			double toughness = BlockArmorPropertiesHandler.getProperties(state).toughness(this.level(),state,blockHitResult.getBlockPos(),true);
			if(toughness > 11) {
				Vec3 oldDelta = this.getDeltaMovement();
				//Vec3 location = blockHitResult.getLocation();
				Vec3 location = new Vec3(this.getX(), this.getY(), this.getZ());
				int shrapnelCount = (int) (toughness / 5) + 2;
				CBCProjectileBurst.spawnConeBurst(this.level(), CBCEntityTypes.SHRAPNEL_BURST.get(), location,
						oldDelta, shrapnelCount, 0.7);
			}
		}
		vsCheckReturn(result, blockHitResult);
		return result;
	}

	protected void expireProjectile() {
		this.discard();
	}

	public void setLifetime(int lifetime) { this.ageRemaining = lifetime; }


	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.putBoolean("TooManyCharges", this.tooManyCharges);
		tag.putInt("Age", this.ageRemaining);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.tooManyCharges = tag.getBoolean("TooManyCharges");
		this.ageRemaining = tag.getInt("Age");
	}





}
