package com.cainiao1053.cbcmoreshells.munitions.package_slider;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.config.CBCMSConfigs;
import com.cainiao1053.cbcmoreshells.utils.CBCMSMassiveBlockUtil;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.itemHatch.ItemHatchBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemHandlerHelper;
import net.neoforged.neoforge.items.ItemStackHandler;
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractPackageShellProjectile extends AbstractCannonProjectile {

	private static final String PACKAGE_KEY = "Package";
	private static final String AGE_KEY = "Age";
	private static final StreamCodec<RegistryFriendlyByteBuf, Item> ITEM_CODEC = ByteBufCodecs.registry(Registries.ITEM);

	private static final double BOUNCE_SPEED = 3.0;
	private static final double BOUNCE_ELASTICITY = 1.5;

	private ItemStack contents = ItemStack.EMPTY;
	private Item displayItem = AllItems.CARDBOARD.asItem();
	private int age;
	@Nullable
	private Vec3 bounceVelocity;

	protected AbstractPackageShellProjectile(EntityType<? extends AbstractPackageShellProjectile> type, Level level) {
		super(type, level);
	}

	protected abstract int getLifetime();

	public void setPackage(ItemStack stack) {
		this.contents = stack.copyWithCount(1);
		this.displayItem = stack.getItem();
	}

	public Item getDisplayItem() {
		return this.displayItem;
	}

	/** The block drawn by PackageShellProjectileRenderer. */
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.PACKAGE_SHELL_BLOCK.getDefaultState();
	}

	public boolean hitShip(BlockPos pos) {
		if (Math.abs(pos.getX()) > 10000000 || Math.abs(pos.getZ()) > 10000000) {
			return true;
		}
		return false;
	}

	// ---- flight ----

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide || this.isRemoved()) return;
		int lifetime = this.getLifetime();
		if (lifetime > 0 && ++this.age >= lifetime) {
			this.spawnPackage(this.position());
			this.discard();
		}
	}

	@Override
	protected void onTickRotate() {
		this.yRotO = this.getYRot();
		this.xRotO = this.getXRot();
		if (!this.isInGround()) {
			Vec3 vel = this.getDeltaMovement();
			if (vel.lengthSqr() > 0.005) {
				this.setYRot((float) (Mth.atan2(vel.x, vel.z) * 180.0F / (float) Math.PI));
				this.setXRot((float) (Mth.atan2(vel.y, vel.horizontalDistance()) * 180.0F / (float) Math.PI));
			}
			this.setYRot(lerpRotation(this.yRotO, this.getYRot()));
			this.setXRot(lerpRotation(this.xRotO, this.getXRot()));
		}
	}

	@Override
	protected void clipAndDamage() {
		super.clipAndDamage();
		if (this.bounceVelocity != null) {
			this.nextVelocity = this.bounceVelocity;
			this.bounceVelocity = null;
		}
	}

	protected Vec3 currentTrajectory() {
		Vec3 vel = this.getDeltaMovement();
		return vel.add(this.getForces(this.position(), vel));
	}

	protected boolean shouldBreak(double speed) {
		return speed * 20.0 > CBCMSConfigs.server().packageBreakSpeed.getF();
	}

	@Override
	protected boolean onHitEntity(Entity entity, ProjectileContext projectileContext) {
		// No damage or knockback: the package just drops (or breaks) where the shell is.
		if (!this.level().isClientSide) {
			if (this.shouldBreak(this.currentTrajectory().length())) {
				this.breakApart(this.position());
			} else {
				this.spawnPackage(this.position());
				AllSoundEvents.CARDBOARD_SWORD.play(this.level(), null,
						this.position(), 1.0f, 1.0f);
			}
		}
		return true;
	}

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state,
													 BlockHitResult blockHitResult) {
		// onImpact is never called, so no block damage and no impulse on Sable sub-levels either.
		Vec3 trajectory = this.currentTrajectory();
		double speed = trajectory.length();
		Vec3 normal = CBCUtils.getSurfaceNormalVector(this.level(), blockHitResult);
		// Same convention as CBC's fluid bounce: incidence is the cosine to the normal, deflection its threshold.
		double incidence = Math.max(0.0, trajectory.normalize().dot(normal.reverse()));
		double deflection = this.getBallisticProperties().deflection();

		if (speed > BOUNCE_SPEED && deflection > 0.01 && incidence <= deflection) {
			this.bounceVelocity = trajectory.subtract(normal.scale(normal.dot(trajectory) * BOUNCE_ELASTICITY));
			if (!this.level().isClientSide) {
				AllSoundEvents.CARDBOARD_SWORD.play(this.level(), null,
					CBCCompatTransformers.transformVec3(this.level(), blockHitResult.getLocation()), 1.0f, 1.0f);
			}
			return new ImpactResult(ImpactResult.KinematicOutcome.BOUNCE, false);
		}

		if (!this.level().isClientSide) {
			if (this.shouldBreak(speed)) {
				this.breakApart(this.blockSpawnPos(blockHitResult));
			} else {
				this.deliverToBlock(blockHitResult);
			}
		}
		return new ImpactResult(ImpactResult.KinematicOutcome.STOP, true);
	}

	protected void deliverToBlock(BlockHitResult blockHitResult) {
		if (this.contents.isEmpty()) return;
		// On a Sable sub-level the hit position is in its plot coordinates, so the hatch search runs there too.
		BlockPos hitPos = blockHitResult.getBlockPos();
		int radius = this.hitShip(hitPos)
			? CBCMSConfigs.server().packageHatchSearchRadiusShip.get()
			: CBCMSConfigs.server().packageHatchSearchRadius.get();
		if (this.insertIntoNearestHatch(hitPos, blockHitResult.getLocation(), radius)) return;
		this.spawnPackage(this.blockSpawnPos(blockHitResult));
		AllSoundEvents.CARDBOARD_SWORD.play(this.level(), null,
				this.position(), 1.0f, 1.0f);
	}

	protected Vec3 blockSpawnPos(BlockHitResult blockHitResult) {
		BlockPos hitPos = blockHitResult.getBlockPos();
		Vec3 pos = Vec3.atCenterOf(hitPos.relative(blockHitResult.getDirection()));
		return this.hitShip(hitPos) ? CBCCompatTransformers.transformVec3(this.level(), pos) : pos;
	}

	protected boolean insertIntoNearestHatch(BlockPos center, Vec3 hitLocation, int radius) {
		List<BlockPos> hatches = new ArrayList<>();
		CBCMSMassiveBlockUtil.forEachMatching(this.level(), CBCMSMassiveBlockUtil.cube(center, radius), false,
			state -> state.getBlock() instanceof ItemHatchBlock,
			(pos, state) -> hatches.add(pos.immutable()));
		if (hatches.isEmpty()) return false;

		hatches.sort(Comparator.comparingDouble(pos -> Vec3.atCenterOf(pos).distanceToSqr(hitLocation)));
		for (BlockPos hatchPos : hatches) {
			if (this.insertIntoHatch(hatchPos)) return true;
		}
		return false;
	}

	protected boolean insertIntoHatch(BlockPos hatchPos) {
		Level level = this.level();
		BlockState state = level.getBlockState(hatchPos);
		if (!(state.getBlock() instanceof ItemHatchBlock)) return false;

		BlockPos targetPos = hatchPos.relative(state.getValue(ItemHatchBlock.FACING));
		IItemHandler targetInv = level.getCapability(Capabilities.ItemHandler.BLOCK, targetPos, null);
		if (targetInv == null) return false;
		FilteringBehaviour filter = BlockEntityBehaviour.get(level, hatchPos, FilteringBehaviour.TYPE);
		if (filter == null || !(filter.getFilter().isEmpty() || filter.test(this.contents))) return false;
		if (!ItemHandlerHelper.insertItemStacked(targetInv, this.contents, true).isEmpty()) return false;

		// Anything not inserted stays in contents, so the caller falls through to the next hatch or the ground.
		this.contents = ItemHandlerHelper.insertItemStacked(targetInv, this.contents, false);
		if (!this.contents.isEmpty()) return false;

		AllSoundEvents.ITEM_HATCH.playOnServer(level, hatchPos);
		level.setBlockAndUpdate(hatchPos, state.setValue(ItemHatchBlock.OPEN, true));
		level.scheduleTick(hatchPos, state.getBlock(), 10);
		return true;
	}

	protected void spawnPackage(Vec3 worldPos) {
		if (this.contents.isEmpty()) return;
		PackageEntity box = PackageEntity.fromItemStack(this.level(), worldPos, this.contents);
		box.setDeltaMovement(Vec3.ZERO);
		this.contents = ItemStack.EMPTY;
		this.level().addFreshEntity(box);
	}

	protected void breakApart(Vec3 worldPos) {
		if (this.contents.isEmpty()) return;
		ItemStackHandler inventory = PackageItem.getContents(this.contents);
		this.contents = ItemStack.EMPTY;
		for (int slot = 0; slot < inventory.getSlots(); slot++) {
			ItemStack stack = inventory.getStackInSlot(slot);
			if (stack.isEmpty()) continue;
			this.level().addFreshEntity(new ItemEntity(this.level(), worldPos.x, worldPos.y, worldPos.z, stack.copy(),
				0.0, 0.0, 0.0));
		}
		CBCSoundEvents.WOOD_SPLINTERS.play(this.level(), null, worldPos, 1.0f, 1.8f);
	}

	@Override
	public void remove(RemovalReason reason) {
		// Killed or discarded without delivering (e.g. after bouncing to rest): drop the package where the shell is.
		// Unloading or changing dimension keeps it, since the package is saved with the entity.
		if (!this.level().isClientSide && reason.shouldDestroy() && !this.contents.isEmpty()
			&& this.getY() >= this.level().getMinBuildHeight()) {
			this.spawnPackage(this.position());
		}
		super.remove(reason);
	}

	@Override
	public void addAdditionalSaveData(CompoundTag tag) {
		super.addAdditionalSaveData(tag);
		tag.put(PACKAGE_KEY, this.contents.saveOptional(this.level().registryAccess()));
		tag.putInt(AGE_KEY, this.age);
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.contents = ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound(PACKAGE_KEY));
		this.displayItem = this.contents.getItem();
		this.age = tag.getInt(AGE_KEY);
	}

	@Override
	public void writeSpawnData(RegistryFriendlyByteBuf buffer) {
		super.writeSpawnData(buffer);
		ITEM_CODEC.encode(buffer, this.displayItem);
	}

	@Override
	public void readSpawnData(RegistryFriendlyByteBuf additionalData) {
		super.readSpawnData(additionalData);
		this.displayItem = ITEM_CODEC.decode(additionalData);
	}

}
