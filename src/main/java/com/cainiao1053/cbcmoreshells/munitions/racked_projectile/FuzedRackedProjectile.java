package com.cainiao1053.cbcmoreshells.munitions.racked_projectile;

import net.minecraft.core.Position;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.fuzes.FuzeItem;

import javax.annotation.Nonnull;
import java.util.function.Predicate;

public abstract class FuzedRackedProjectile extends AbstractRackedProjectile {

	private ItemStack fuze = ItemStack.EMPTY;
	//public boolean tooManyCharges = false;
	//protected int lifetime;
	protected int ageRemaining;

	protected FuzedRackedProjectile(EntityType<? extends FuzedRackedProjectile> type, Level level) {
		super(type, level);
	}

	public void setFuze(ItemStack stack) {
		this.fuze = stack == null || stack.isEmpty() ? ItemStack.EMPTY : stack;
	}

	@Override
	public void tick() {
		super.tick();
		if (this.canDetonate(fz -> fz.onProjectileTick(this.fuze, this))) {
			this.detonate(this.position());
			this.removeNextTick = true;
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

	//public void setLifetime(int lifetime) { this.ageRemaining = lifetime; }

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state, BlockHitResult blockHitResult) {
		return super.calculateBlockPenetration(projectileContext, state, blockHitResult);
	}

	@Override
	protected DamageSource getEntityDamage(Entity entity) {
		return super.getEntityDamage(entity);
	}

	@Override
	protected double getDragForce() {
		return super.getDragForce();
	}

	@Override
	protected Vec3 getForces(Vec3 position, Vec3 velocity) {
		return super.getForces(position, velocity);
	}

	@Override
	protected double getDefaultGravity() {
		return super.getDefaultGravity();
	}

	@Override
	protected boolean onImpactFluid(ProjectileContext projectileContext, BlockState blockState, FluidState fluidState, Vec3 impactPos, BlockHitResult fluidHitResult) {
		return super.onImpactFluid(projectileContext, blockState, fluidState, impactPos, fluidHitResult);
	}

	@Nonnull protected abstract BigCannonFuzePropertiesComponent getFuzeProperties();

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put("Fuze", this.fuze.save(new CompoundTag()));
		//tag.putBoolean("TooManyCharges", this.tooManyCharges);
		//tag.putInt("Age", this.ageRemaining);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.fuze = tag.contains("Fuze", Tag.TAG_COMPOUND)
			? ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound("Fuze"))
			: ItemStack.EMPTY;
		//this.tooManyCharges = tag.getBoolean("TooManyCharges");
		//this.ageRemaining = tag.getInt("Age");
	}

//	public ItemStack getFuze(){
//		return this.fuze;
//	}


	protected final boolean canDetonate(Predicate<FuzeItem> cons) {
		return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && !this.isRemoved()
			&& this.fuze.getItem() instanceof FuzeItem fuzeItem && cons.test(fuzeItem);
	}

	/**
	 * Use {@link #detonate(Position)}
	 */
	@Deprecated
	protected void detonate() { this.detonate(this.position()); }

	protected abstract void detonate(Position position);

	@Override
	public boolean canLingerInGround() {
		return !this.level().isClientSide && this.level().hasChunkAt(this.blockPosition()) && this.fuze.getItem() instanceof FuzeItem fuzeItem && fuzeItem.canLingerInGround(this.fuze, this);
	}

}

