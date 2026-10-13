package com.cainiao1053.cbcmoreshells.munitions.package_slider;

import com.cainiao1053.cbcmoreshells.config.CBCMSConfigs;
import com.cainiao1053.cbcmoreshells.utils.CBCMSMassiveBlockUtil;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.content.logistics.box.PackageEntity;
import com.simibubi.create.content.logistics.itemHatch.ItemHatchBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ProjectileContext;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public abstract class AbstractPackageShellProjectile extends AbstractCannonProjectile {

	private static final String PACKAGE_KEY = "Package";
	private static final StreamCodec<RegistryFriendlyByteBuf, Item> ITEM_CODEC = ByteBufCodecs.registry(Registries.ITEM);

	private ItemStack contents = ItemStack.EMPTY;
	private Item displayItem = Items.AIR;

	protected AbstractPackageShellProjectile(EntityType<? extends AbstractPackageShellProjectile> type, Level level) {
		super(type, level);
	}

	public void setPackage(ItemStack stack) {
		this.contents = stack.copyWithCount(1);
		this.displayItem = stack.getItem();
	}

	public Item getDisplayItem() {
		return this.displayItem;
	}

	public boolean hitShip(BlockPos pos) {
		if (Math.abs(pos.getX()) > 10000000 || Math.abs(pos.getZ()) > 10000000) {
			return true;
		}
		return false;
	}

	@Override
	protected boolean onHitEntity(Entity entity, ProjectileContext projectileContext) {
		// No damage or knockback: the package just drops where the shell is.
		if (!this.level().isClientSide)
			this.spawnPackage(this.position());
		return true;
	}

	@Override
	protected ImpactResult calculateBlockPenetration(ProjectileContext projectileContext, BlockState state,
													 BlockHitResult blockHitResult) {
		// onImpact is never called, so no block damage and no impulse on Sable sub-levels either.
		if (!this.level().isClientSide)
			this.deliverToBlock(blockHitResult);
		return new ImpactResult(ImpactResult.KinematicOutcome.STOP, true);
	}

	protected void deliverToBlock(BlockHitResult blockHitResult) {
		if (this.contents.isEmpty()) return;
		// On a Sable sub-level the hit position is in its plot coordinates, so the hatch search runs there too.
		BlockPos hitPos = blockHitResult.getBlockPos();
		boolean onShip = this.hitShip(hitPos);
		int radius = onShip
			? CBCMSConfigs.server().packageHatchSearchRadiusShip.get()
			: CBCMSConfigs.server().packageHatchSearchRadius.get();
		if (this.insertIntoNearestHatch(hitPos, blockHitResult.getLocation(), radius)) return;

		// Spawn in the block the shell is in, i.e. in front of the face it hit.
		Vec3 spawnPos = Vec3.atCenterOf(hitPos.relative(blockHitResult.getDirection()));
		if (onShip)
			spawnPos = CBCCompatTransformers.transformVec3(this.level(), spawnPos);
		this.spawnPackage(spawnPos);
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
	}

	@Override
	public void readAdditionalSaveData(CompoundTag tag) {
		super.readAdditionalSaveData(tag);
		this.contents = ItemStack.parseOptional(this.level().registryAccess(), tag.getCompound(PACKAGE_KEY));
		this.displayItem = this.contents.getItem();
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
