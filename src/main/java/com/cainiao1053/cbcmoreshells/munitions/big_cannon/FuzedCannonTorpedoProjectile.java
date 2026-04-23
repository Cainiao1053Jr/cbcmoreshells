package com.cainiao1053.cbcmoreshells.munitions.big_cannon;
//import com.cainiao1053.cbcmoreshells.munitions.fuzes.FuzeItem;

import net.minecraft.core.Position;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import javax.annotation.Nonnull;
import java.util.function.Predicate;


public abstract class FuzedCannonTorpedoProjectile extends AbstractCannonTorpedoProjectile {

	private ItemStack fuze = ItemStack.EMPTY;
	public boolean tooManyCharges = false;
	//protected int lifetime;
	protected int ageRemaining;
	protected int tickInWater = 0;

	protected FuzedCannonTorpedoProjectile(EntityType<? extends FuzedCannonTorpedoProjectile> type, Level level) {
		super(type, level);
	}

	public void setFuze(ItemStack stack) { this.fuze = stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack; }

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
		if (this.canDetonate(fz -> fz.onProjectileTick(this.fuze, this))) {
			this.detonate(this.position());
			this.removeNextTick = true;
		}

		if (!this.level().isClientSide) {
			this.ageRemaining--;
			if (this.ageRemaining <= 0)
				this.expireProjectile();
		}
		FluidState fluidState = this.level().getFluidState(this.blockPosition());
		if(!fluidState.isEmpty()) {
			tickInWater += 1;
		}
	}

	@Override
	protected boolean onClip(ProjectileContext ctx, Vec3 start, Vec3 end) {
		if (super.onClip(ctx, start, end)) return true;
		boolean baseFuze = this.getFuzeProperties().baseFuze();
		if (this.canDetonate(fz -> fz.onProjectileClip(this.fuze, this, start, end, ctx, baseFuze))) {
			this.detonate(start);
			return true;
		}
		return false;
	}

	@Override
	protected boolean onImpact(HitResult hitResult, ImpactResult impactResult, ProjectileContext projectileContext) {
		super.onImpact(hitResult, impactResult, projectileContext);
		boolean baseFuze = this.getFuzeProperties().baseFuze();
		if (this.canDetonate(fz -> fz.onProjectileImpact(this.fuze, this, hitResult, impactResult, baseFuze))) {
			this.detonate(hitResult.getLocation());
			return true;
		} else {
			return false;
		}
	}


	protected void expireProjectile() {
		this.discard();
	}

	public void setLifetime(int lifetime) { this.ageRemaining = lifetime; }

	@Nonnull protected abstract BigCannonFuzePropertiesComponent getFuzeProperties();

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		//tag.put("Fuze", this.fuze.save(new CompoundTag()));
		tag.put("Fuze", this.fuze.saveOptional(this.level().registryAccess()));
		tag.putBoolean("TooManyCharges", this.tooManyCharges);
		tag.putInt("Age", this.ageRemaining);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		//this.fuze = ItemStack.of(tag.getCompound("Fuze"));
		this.fuze = ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound("Fuze"));
		this.tooManyCharges = tag.getBoolean("TooManyCharges");
		this.ageRemaining = tag.getInt("Age");
	}


	protected final boolean canDetonate(Predicate<FuzeItem> cons) {
		return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && !this.isRemoved()
			&& this.fuze.getItem() instanceof FuzeItem FuzeItem && cons.test(FuzeItem);
	}


	@Override
	public boolean ignoreExplosion(Explosion explosion) {
		return true;
	}

	/**
	 * Use {@link #detonate(Position)}
	 */
	@Deprecated
	protected void detonate() { this.detonate(this.position()); }

	protected abstract void detonate(Position position);

	public void triggerDetonation(){this.detonate(this.position());}

	public int getTickInWater() {
		return this.tickInWater;
	}

	public boolean normalDetonate() {
		return (getTickInWater() > this.getExplosionCooldown());
	}

	@Override
	public boolean canLingerInGround() {
		return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && this.fuze.getItem() instanceof FuzeItem FuzeItem && FuzeItem.canLingerInGround(this.fuze, this);
	}

}
