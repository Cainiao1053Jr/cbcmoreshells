package com.cainiao1053.cbcmoreshells.cannon_control.contraption;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.cannon_control.cannon_types.CBCMSCannonContraptionTypes;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.IPackageSliderBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBehavior;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBlock;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderEnd;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches.PackageSliderQuickfiringBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches.PackageSliderQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSContraptionTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSPackageSliderMaterials;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import com.cainiao1053.cbcmoreshells.munitions.package_slider.PackageShellProjectile;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.logistics.box.PackageItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundSoundPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import rbasamoyai.createbigcannons.CBCCompatTransformers;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundUpdateContraptionPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MountedPackageSliderContraption extends AbstractMountedCannonContraption {

	/** Entities in front of the breech leave at this fraction of the muzzle velocity. */
	private static final double ENTITY_LAUNCH_FRACTION = 1;
	private static final float LAUNCH_SOUND_VOLUME = 1.25f;
	private static final double LAUNCH_SOUND_RANGE = 10000;

	private PackageSliderMaterial cannonMaterial;

	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = this.createBoundsFromExtensionLengths();
		return !this.blocks.isEmpty();
	}

	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		BlockState startState = level.getBlockState(pos);
		if (!(startState.getBlock() instanceof PackageSliderBlock startCannon)) return false;

		PackageSliderMaterial material = startCannon.getCannonMaterial();
		int maxLength = material.properties().maxLength();
		Direction.Axis axis = startCannon.getFacing(startState).getAxis();
		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, axis);
		Direction negative = positive.getOpposite();

		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));
		PackageSliderEnd startEnd = startCannon.getOpeningType(startState);

		// Walk both ways along the axis (the start block may itself be the breech), stopping after a closed end
		// (the breech) or before a non-cannon block.
		BlockPos positiveEndPos = pos;
		PackageSliderEnd positiveEnd = startEnd;
		while (true) {
			BlockPos next = positiveEndPos.relative(positive);
			BlockState nextState = level.getBlockState(next);
			if (!this.isValidCannonBlock(level, nextState, next, material, axis)) break;
			positiveEndPos = next;
			cannonBlocks.add(new StructureBlockInfo(next, nextState, this.getBlockEntityNBT(level, next)));
			++this.frontExtensionLength;
			if (cannonBlocks.size() > maxLength) throw new AssemblyException(Component.translatable("exception.createbigcannons.cannon_mount.cannonTooLarge", material.properties().maxLength()));
			positiveEnd = ((PackageSliderBlock) nextState.getBlock()).getOpeningType(nextState);
			if (positiveEnd != PackageSliderEnd.OPEN) break;
		}

		BlockPos negativeEndPos = pos;
		PackageSliderEnd negativeEnd = startEnd;
		while (true) {
			BlockPos next = negativeEndPos.relative(negative);
			BlockState nextState = level.getBlockState(next);
			if (!this.isValidCannonBlock(level, nextState, next, material, axis)) break;
			negativeEndPos = next;
			cannonBlocks.add(new StructureBlockInfo(next, nextState, this.getBlockEntityNBT(level, next)));
			++this.backExtensionLength;
			if (cannonBlocks.size() > maxLength) throw cannonTooLarge();
			negativeEnd = ((PackageSliderBlock) nextState.getBlock()).getOpeningType(nextState);
			if (negativeEnd != PackageSliderEnd.OPEN) break;
		}

		// Exactly one end must be the breech.
		if (positiveEnd == negativeEnd) throw invalidCannon();
		boolean firesPositive = positiveEnd == PackageSliderEnd.OPEN;
		BlockPos breechPos = firesPositive ? negativeEndPos : positiveEndPos;
		if (!(level.getBlockState(breechPos).getBlock() instanceof PackageSliderQuickfiringBreechBlock))
			throw invalidCannon();

		this.initialOrientation = firesPositive ? positive : negative;
		this.anchor = pos;
		// The loading barrel: the block right in front of the breech.
		this.startPos = breechPos.relative(this.initialOrientation).subtract(pos);

		HolderLookup.Provider registries = level.registryAccess();
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos().subtract(pos);
			BlockState alignedState = blockInfo.state().setValue(BlockStateProperties.FACING, this.initialOrientation);
			this.getBlocks().put(localPos, new StructureBlockInfo(localPos, alignedState, blockInfo.nbt()));
			if (blockInfo.nbt() == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, alignedState, blockInfo.nbt(), registries);
			if (be != null) this.presentBlockEntities.put(localPos, be);
		}
		this.cannonMaterial = material;
		return true;
	}

	private boolean isValidCannonBlock(Level level, BlockState state, BlockPos pos, PackageSliderMaterial material,
									   Direction.Axis axis) {
		return state.getBlock() instanceof PackageSliderBlock cBlock
			&& cBlock.getCannonMaterial() == material
			&& cBlock.getFacing(state).getAxis() == axis
			&& level.getBlockEntity(pos) instanceof IPackageSliderBlockEntity;
	}

	/** Number of cannon blocks, breech included. */
	public int getCannonLength() {
		return this.blocks.size();
	}

	public BlockPos getBreechPos() {
		return this.startPos.relative(this.initialOrientation.getOpposite());
	}

	public float getMuzzleVelocity() {
		return (this.getCannonLength() - 1) * 0.5f + 1.0f;
	}

	@Override
	public float getWeightForStress() {
		float weight = this.cannonMaterial == null ? 1 : this.cannonMaterial.properties().weight();
		return this.blocks.size() * weight;
	}

	@Override
	public void tick(Level level, PitchOrientedContraptionEntity entity) {
		super.tick(level, entity);
		BlockPos breechPos = this.getBreechPos();
		if (!level.isClientSide && this.presentBlockEntities.get(breechPos) instanceof PackageSliderQuickfiringBreechBlockEntity breech)
			breech.tickCooldown();
		// The rendered copy of the breech is a separate block entity; tick it too so the slider animates.
		if (level.isClientSide && this.getBlockEntityClientSide(breechPos) instanceof PackageSliderQuickfiringBreechBlockEntity clientBreech)
			clientBreech.tickCooldown();
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower,
								 ControlPitchContraption controller) {
		if (!togglePower || firePower <= 0) return;
		this.fireShot(level, entity);
	}

	// ---- loading ----

	/** Whether a package can go in now: the breech has finished reloading and the loading barrel is empty. */
	public boolean canLoadPackage() {
		return this.presentBlockEntities.get(this.getBreechPos()) instanceof PackageSliderQuickfiringBreechBlockEntity breech
			&& !breech.isReloading()
			&& this.presentBlockEntities.get(this.startPos) instanceof IPackageSliderBlockEntity barrel
			&& !barrel.cannonBehavior().isLoaded();
	}

	/** Loads one package into the barrel in front of the breech. Server side only. */
	public boolean tryLoadPackage(ItemStack stack, PitchOrientedContraptionEntity entity) {
		if (!PackageItem.isPackage(stack) || !this.canLoadPackage()) return false;
		((IPackageSliderBlockEntity) this.presentBlockEntities.get(this.startPos)).cannonBehavior().setLoaded(stack);
		this.syncBlocks(List.of(this.startPos), entity);
		return true;
	}

	/** Takes the loaded package back out of the barrel. Server side only. */
	public ItemStack unloadPackage(PitchOrientedContraptionEntity entity) {
		if (!(this.presentBlockEntities.get(this.startPos) instanceof IPackageSliderBlockEntity barrel)
			|| !barrel.cannonBehavior().isLoaded()) return ItemStack.EMPTY;
		ItemStack removed = barrel.cannonBehavior().removeLoaded();
		this.syncBlocks(List.of(this.startPos), entity);
		return removed;
	}

	// ---- firing ----

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		BlockPos breechPos = this.getBreechPos();
		if (!(this.presentBlockEntities.get(breechPos) instanceof PackageSliderQuickfiringBreechBlockEntity breech)
			|| breech.isReloading()
			|| !(this.presentBlockEntities.get(this.startPos) instanceof IPackageSliderBlockEntity barrel)
			|| !barrel.cannonBehavior().isLoaded()) return;

		PackageShellProjectile projectile = CBCMSEntityTypes.PACKAGE_SHELL.create(level);
		if (projectile == null) return;
		projectile.setPackage(barrel.cannonBehavior().removeLoaded());

		int length = this.getCannonLength();
		Vec3 dir = entity.applyRotation(Vec3.atLowerCornerOf(this.initialOrientation.getNormal()), 0).normalize();
		// Leave from the last barrel block.
		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.initialOrientation, length - 2)), 0);

		projectile.setPos(spawnPos);
		projectile.shoot(dir.x, dir.y, dir.z, this.getMuzzleVelocity(), this.cannonMaterial.properties().spread());
		projectile.xRotO = projectile.getXRot();
		projectile.yRotO = projectile.getYRot();
		projectile.addUntouchableEntity(entity, 6);
		level.addFreshEntity(projectile);
		// No recoil: the controller is never told about this shot.
		this.launchEntitiesInBarrel(level, entity, dir);

		StructureBlockInfo breechInfo = this.blocks.get(breechPos);
		int baseReload = breechInfo != null && breechInfo.state().getBlock() instanceof PackageSliderQuickfiringBreechBlock qfb
			? qfb.getBaseReloadTicks()
			: PackageSliderQuickfiringBreechBlock.DEFAULT_BASE_RELOAD_TICKS;
		// The slider block rests centred 0.25 blocks behind the breech centre and is pushed to the centre of the
		// frontmost barrel, length - 1 blocks ahead of the breech.
		breech.startReload(baseReload + length * this.cannonMaterial.properties().reloadPerSection(), length - 0.75f);
		this.syncBlocks(List.of(this.startPos, breechPos), entity);

		this.playLaunchSound(level, CBCCompatTransformers.transformVec3(level, spawnPos, entity.position()));

		if (CBCConfigs.server().munitions.projectilesCanChunkload.get()) {
			ChunkPos cpos = new ChunkPos(BlockPos.containing(projectile.position()));
			RitchiesProjectileLib.queueForceLoad(level, cpos.x, cpos.z);
		}
	}

	private void playLaunchSound(ServerLevel level, Vec3 soundPos) {
		Holder<SoundEvent> sound = BuiltInRegistries.SOUND_EVENT.wrapAsHolder(CBCMSSoundEvents.PACKAGE_SLIDER_LAUNCH.getMainEvent());
		long seed = level.getRandom().nextLong();
		for (ServerPlayer player : level.players()) {
			Vec3 ear = player.getEyePosition();
			double dist = ear.distanceToSqr(soundPos);
			if (dist >= LAUNCH_SOUND_RANGE) continue;
			float volume = (float) (LAUNCH_SOUND_VOLUME * (1 - dist / LAUNCH_SOUND_RANGE));
			// The client would attenuate again over 16 * volume blocks, so the sound is played just in front of the
			// player, towards the muzzle, which keeps its direction and leaves the falloff to the volume alone.
			Vec3 pos = dist < 1 ? soundPos : ear.add(soundPos.subtract(ear).scale(1 / dist));
			player.connection.send(new ClientboundSoundPacket(sound, SoundSource.BLOCKS, pos.x, pos.y, pos.z, volume, 1f, seed));
		}
	}

	private void launchEntitiesInBarrel(ServerLevel level, PitchOrientedContraptionEntity entity, Vec3 dir) {
		Vec3 centre = entity.toGlobalVector(Vec3.atCenterOf(this.startPos), 0);
		AABB box = AABB.ofSize(centre, 1, 1, 1);
		Vec3 velocity = dir.scale(this.getMuzzleVelocity() * ENTITY_LAUNCH_FRACTION);
		for (Entity target : level.getEntities(entity, box, EntitySelector.NO_SPECTATORS)) {
			if (target instanceof PackageShellProjectile || target.isPassenger() || target.noPhysics) continue;
			target.setDeltaMovement(velocity);
			target.hasImpulse = true;
			// Players move client-side; this sends them the new motion.
			target.hurtMarked = true;
			target.resetFallDistance();
		}
	}

	private void syncBlocks(List<BlockPos> positions, PitchOrientedContraptionEntity entity) {
		HolderLookup.Provider registries = entity.level().registryAccess();
		Map<BlockPos, StructureBlockInfo> changes = new HashMap<>(positions.size());
		for (BlockPos pos : positions) {
			StructureBlockInfo info = this.blocks.get(pos);
			BlockEntity be = this.presentBlockEntities.get(pos);
			if (info == null || be == null) continue;
			CompoundTag full = fullTag(be, registries);
			this.blocks.put(pos, new StructureBlockInfo(info.pos(), info.state(), full));
			CompoundTag client = clientTag(full);
			this.updateTags.put(pos, client);
			changes.put(pos, new StructureBlockInfo(info.pos(), info.state(), client));
		}
		if (!changes.isEmpty())
			NetworkPlatform.sendToClientTracking(ClientboundUpdateContraptionPacket.entity(entity, changes), entity);
	}

	private static CompoundTag fullTag(BlockEntity be, HolderLookup.Provider registries) {
		CompoundTag tag = be.saveWithFullMetadata(registries);
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");
		return tag;
	}

	private static CompoundTag clientTag(CompoundTag full) {
		CompoundTag tag = full.copy();
		PackageSliderBehavior.stripForClient(tag);
		return tag;
	}

	@Override
	public CompoundTag writeNBT(HolderLookup.Provider registries, boolean spawnPacket) {
		// Spawn packets send update tags instead of the full block data when present.
		if (spawnPacket) {
			for (Map.Entry<BlockPos, BlockEntity> entry : this.presentBlockEntities.entrySet())
				this.updateTags.put(entry.getKey(), clientTag(fullTag(entry.getValue(), registries)));
		}
		CompoundTag tag = super.writeNBT(registries, spawnPacket);
		tag.putString("CannonMaterial", this.cannonMaterial == null
			? CBCMSPackageSliderMaterials.STEEL.name().toString()
			: this.cannonMaterial.name().toString());
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = PackageSliderMaterial.fromNameOrNull(CBCUtils.location(tag.getString("CannonMaterial")));
		if (this.cannonMaterial == null) this.cannonMaterial = CBCMSPackageSliderMaterials.STEEL;
	}

	@Override
	public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
		return poce.toGlobalVector(Vec3.atCenterOf(this.getBreechPos()), 1);
	}

	@Override
	public ICannonContraptionType getCannonType() {
		return CBCMSCannonContraptionTypes.PACKAGE_SLIDER;
	}

	@Override
	public ContraptionType getType() {
		return CBCMSContraptionTypes.PACKAGE_SLIDER.value();
	}

}
