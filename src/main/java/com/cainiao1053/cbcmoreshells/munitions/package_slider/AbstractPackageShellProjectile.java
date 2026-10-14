package com.cainiao1053.cbcmoreshells.munitions.package_slider;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.config.CBCMSConfigs;
import com.cainiao1053.cbcmoreshells.utils.CBCMSMassiveBlockUtil;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.content.logistics.chute.AbstractChuteBlock;
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

	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.PACKAGE_SHELL_BLOCK.getDefaultState();
	}

	public boolean hitShip(BlockPos pos) {
		if (Math.abs(pos.getX()) > 10000000 || Math.abs(pos.getZ()) > 10000000) {
			return true;
		}
		return false;
	}

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
						this.position(), 2.5f, 1.2f);
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
					CBCCompatTransformers.transformVec3(this.level(), blockHitResult.getLocation()), 2.5f, 1.2f);
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
		// On a Sable sub-level the hit position is in its plot coordinates, so the chute search runs there too.
		BlockPos hitPos = blockHitResult.getBlockPos();
		int radius = this.hitShip(hitPos)
			? CBCMSConfigs.server().packageChuteSearchRadiusShip.get()
			: CBCMSConfigs.server().packageChuteSearchRadius.get();
		AllSoundEvents.CARDBOARD_SWORD.play(this.level(), null,
				this.position(), 2.5f, 1.2f);
		if (this.insertIntoNearestChute(hitPos, blockHitResult.getLocation(), radius)) return;
		this.spawnPackage(this.blockSpawnPos(blockHitResult));
	}

	protected Vec3 blockSpawnPos(BlockHitResult blockHitResult) {
		BlockPos hitPos = blockHitResult.getBlockPos();
		Vec3 pos = Vec3.atCenterOf(hitPos.relative(blockHitResult.getDirection()));
		return this.hitShip(hitPos) ? CBCCompatTransformers.transformVec3(this.level(), pos) : pos;
	}

	protected boolean insertIntoNearestChute(BlockPos center, Vec3 hitLocation, int radius) {
		List<BlockPos> chutes = new ArrayList<>();
		CBCMSMassiveBlockUtil.forEachMatching(this.level(), CBCMSMassiveBlockUtil.cube(center, radius), false,
			state -> state.getBlock() instanceof AbstractChuteBlock,
			(pos, state) -> chutes.add(pos.immutable()));
		if (chutes.isEmpty()) return false;

		chutes.sort(Comparator.comparingDouble(pos -> Vec3.atCenterOf(pos).distanceToSqr(hitLocation)));
		for (BlockPos chutePos : chutes) {
			if (this.insertIntoChute(chutePos)) return true;
		}
		return false;
	}

	protected boolean insertIntoChute(BlockPos chutePos) {
		Level level = this.level();
		if (!(level.getBlockState(chutePos).getBlock() instanceof AbstractChuteBlock)) return false;
		IItemHandler chuteInv = level.getCapability(Capabilities.ItemHandler.BLOCK, chutePos, null);
		if (chuteInv == null) return false;
		if (!ItemHandlerHelper.insertItem(chuteInv, this.contents, true).isEmpty()) return false;

		// Anything not inserted stays in contents, so the caller falls through to the next chute or the ground.
		this.contents = ItemHandlerHelper.insertItem(chuteInv, this.contents, false);
		if (!this.contents.isEmpty()) return false;

		Vec3 soundPos = Vec3.atCenterOf(chutePos);
		if (this.hitShip(chutePos)) soundPos = CBCCompatTransformers.transformVec3(level, soundPos);
		AllSoundEvents.CARDBOARD_SWORD.play(level, null, soundPos, 2.5f, 1.2f);
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
		CBCSoundEvents.WOOD_SPLINTERS.play(this.level(), null, worldPos, 3.0f, 1.8f);
	}

	@Override
	public void remove(RemovalReason reason) {
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
