package com.cainiao1053.cbcmoreshells.cannon_control.contraption;

import static rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock.writeAndSyncSingleBlockData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.FlakcannonBarrelBlock;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.FlakcannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.IFlakcannonBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.MovesWithFlakcannonRecoilSpring;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.breech.AbstractFlakcannonBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.breech.FlakcannonBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material.FlakcannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material.FlakcannonMaterialProperties;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.recoil_spring.FlakcannonRecoilSpringBlock;
import com.cainiao1053.cbcmoreshells.cannons.flak_cannon.recoil_spring.FlakcannonRecoilSpringBlockEntity;
import com.cainiao1053.cbcmoreshells.index.CBCMSFlakcannonMaterials;
import com.cainiao1053.cbcmoreshells.munitions.flak_cannon.AbstractFlakcannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.flak_cannon.FlakcannonRoundItem;
import com.cainiao1053.cbcmoreshells.munitions.flak_cannon.config.FlakcannonProjectilePropertiesComponent;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.foundation.utility.Components;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CBCCannonContraptionTypes;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.ItemCannon;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannon_control.fixed_cannon_mount.FixedCannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannons.ItemCannonBehavior;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.effects.particles.plumes.AutocannonPlumeParticleData;
import rbasamoyai.createbigcannons.index.CBCContraptionTypes;
import rbasamoyai.createbigcannons.index.CBCEntityTypes;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.ClientboundAnimateCannonContraptionPacket;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

public class MountedFlakcannonContraption extends AbstractMountedCannonContraption implements ItemCannon {

	private FlakcannonMaterial cannonMaterial;
	private final Set<BlockPos> recoilSpringPositions = new LinkedHashSet<>();
	private boolean isHandle = false;
	private boolean isFiring = false;
//	private ItemStackHandler magazine = new ItemStackHandler(1);
	private int bulletCount = 0;
	private int short_reload = 0;

	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = this.createBoundsFromExtensionLengths();
		return !this.blocks.isEmpty();
	}

	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		BlockState startState = level.getBlockState(pos);

		if (!(startState.getBlock() instanceof FlakcannonBlock startCannon)) {
			return false;
		}
		if (!startCannon.isComplete(startState)) {
			throw hasIncompleteCannonBlocks(pos);
		}

		FlakcannonMaterial material = startCannon.getAutocannonMaterial();
		boolean isStartBreech = startCannon.isBreechMechanism(startState);

		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));

		int cannonLength = 1;

		Direction cannonFacing = startCannon.getFacing(startState);

		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
		Direction negative = positive.getOpposite();

		BlockPos start = pos;
		BlockState nextState = level.getBlockState(pos.relative(positive));
		boolean positiveBreech = false;

		while (nextState.getBlock() instanceof FlakcannonBlock cBlock && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
			start = start.relative(positive);
			if (!cBlock.isComplete(nextState)) throw hasIncompleteCannonBlocks(start);
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			this.frontExtensionLength++;
			cannonLength++;
			positiveBreech = cBlock.isBreechMechanism(nextState);
			if (positiveBreech && isStartBreech)
				throw invalidCannon();
			if (positiveBreech && cBlock.getFacing(nextState) != negative)
				throw incorrectBreechDirection(start);
			nextState = level.getBlockState(start.relative(positive));
			if (cannonLength > getMaxCannonLength()) throw cannonTooLarge();
			if (positiveBreech) break;
		}
		BlockPos positiveEndPos = positiveBreech ? start : start.relative(negative);

		start = pos;
		nextState = level.getBlockState(pos.relative(negative));

		boolean negativeBreech = false;
		while (nextState.getBlock() instanceof FlakcannonBlock cBlock && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);
			if (!cBlock.isComplete(nextState)) throw hasIncompleteCannonBlocks(start);
			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			this.backExtensionLength++;
			cannonLength++;
			negativeBreech = cBlock.isBreechMechanism(nextState);
			if (negativeBreech && isStartBreech)
				throw invalidCannon();
			if (negativeBreech && cBlock.getFacing(nextState) != positive)
				throw incorrectBreechDirection(start);
			nextState = level.getBlockState(start.relative(negative));
			if (cannonLength > getMaxCannonLength()) throw cannonTooLarge();
			if (negativeBreech) break;
		}
		BlockPos negativeEndPos = negativeBreech ? start : start.relative(positive);

		if (cannonLength < 2 || positiveBreech && negativeBreech) throw invalidCannon();

		this.startPos = !positiveBreech && !negativeBreech ? pos : negativeBreech ? negativeEndPos : positiveEndPos;
		BlockState breechState = level.getBlockState(this.startPos);
		if (!(breechState.getBlock() instanceof FlakcannonBreechBlock)) throw invalidCannon();
		this.initialOrientation = breechState.getValue(BlockStateProperties.FACING);

		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);

		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos().subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state(), blockInfo.nbt());
			this.blocks.put(localPos, localBlockInfo);

			if (blockInfo.nbt() == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state(), blockInfo.nbt());
			this.presentBlockEntities.put(localPos, be);
			if (blockInfo.state().getBlock() instanceof FlakcannonRecoilSpringBlock)
				this.recoilSpringPositions.add(localPos);
		}

		StructureBlockInfo startInfo = this.blocks.get(this.startPos);
		if (startInfo == null || !(startInfo.state().getBlock() instanceof FlakcannonBreechBlock))
			throw noAutocannonBreech();
		this.isHandle = startInfo.state().hasProperty(FlakcannonBreechBlock.HANDLE) && startInfo.state().getValue(FlakcannonBreechBlock.HANDLE);
		if (this.isHandle) {
			this.getSeats().add(this.startPos.immutable());
		}

		StructureBlockInfo possibleSpring = this.blocks.get(this.startPos.relative(this.initialOrientation));
		if (possibleSpring != null
			&& possibleSpring.state().getBlock() instanceof FlakcannonRecoilSpringBlock springBlock
			&& springBlock.getFacing(possibleSpring.state()) == this.initialOrientation) {
			BlockPos mainRecoilSpringPos = this.startPos.relative(this.initialOrientation).immutable();
			if (this.presentBlockEntities.get(mainRecoilSpringPos) instanceof FlakcannonRecoilSpringBlockEntity springBE) {
				for (int i = 1; i < cannonLength; ++i) {
					BlockPos pos1 = this.startPos.relative(this.initialOrientation, i);
					StructureBlockInfo blockInfo = this.blocks.get(pos1);
					if (blockInfo == null || !(blockInfo.state().getBlock() instanceof MovesWithFlakcannonRecoilSpring springed))
						continue;
					springBE.toAnimate.put(pos1.subtract(mainRecoilSpringPos), springed.getMovingState(blockInfo.state()));
					this.blocks.put(pos1, new StructureBlockInfo(pos1, springed.getStationaryState(blockInfo.state()), blockInfo.nbt()));
				}
				CompoundTag newTag = springBE.saveWithFullMetadata();
				newTag.remove("x");
				newTag.remove("y");
				newTag.remove("z");
				this.blocks.put(mainRecoilSpringPos, new StructureBlockInfo(mainRecoilSpringPos, possibleSpring.state(), newTag));
			}
		}

		this.cannonMaterial = material;

		return true;
	}

	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, FlakcannonMaterial material) {
		FlakcannonBlock cBlock = (FlakcannonBlock) state.getBlock();
		if (cBlock.getAutocannonMaterialInLevel(level, state, pos) != material) return false;
		return level.getBlockEntity(pos) instanceof IFlakcannonBlockEntity cbe
			&& level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof IFlakcannonBlockEntity cbe1
			&& cbe.cannonBehavior().isConnectedTo(connection.getOpposite())
			&& cbe1.cannonBehavior().isConnectedTo(connection);
	}

	public static AssemblyException noAutocannonBreech() {
		return new AssemblyException(Components.translatable("exception." + CreateBigCannons.MOD_ID + ".cannon_mount.noAutocannonBreech"));
	}

	@Override
	public void addBlocksToWorld(Level world, StructureTransform transform) {
		Map<BlockPos, StructureBlockInfo> modifiedBlocks = new HashMap<>();
		for (Map.Entry<BlockPos, StructureBlockInfo> entry : this.blocks.entrySet()) {
			StructureBlockInfo info = entry.getValue();
			BlockState newState = info.state();
			boolean modified = true;

			if (newState.hasProperty(FlakcannonBarrelBlock.ASSEMBLED) && newState.getValue(FlakcannonBarrelBlock.ASSEMBLED)) {
				newState = newState.setValue(FlakcannonBarrelBlock.ASSEMBLED, false);
				modified = true;
			}

			CompoundTag infoNbt = info.nbt();
			if (infoNbt != null) {
				if (infoNbt.contains("AnimateTicks")) {
					infoNbt.remove("AnimateTicks");
					modified = true;
				}
				if (infoNbt.contains("RenderedBlocks")) {
					infoNbt.remove("RenderedBlocks");
					modified = true;
				}
			}

			if (modified) modifiedBlocks.put(info.pos(), new StructureBlockInfo(info.pos(), newState, infoNbt));
		}
		this.blocks.putAll(modifiedBlocks);
		super.addBlocksToWorld(world, transform);
	}

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		//now schedule fire
		if (this.startPos == null
				|| this.cannonMaterial == null
				|| !(this.presentBlockEntities.get(this.startPos) instanceof AbstractFlakcannonBreechBlockEntity breech)
		|| !breech.canFire() || isFiring) return;
		ItemStack foundProjectile = breech.getMagazine();
		if (!(foundProjectile.getItem() instanceof FlakcannonRoundItem round)) return;
		this.bulletCount = round.getAutocannonProperties(foundProjectile).projectileCount();
		isFiring = true;
		breech.setMagazine(ItemStack.EMPTY);
	}

	public void actuallyFire(ServerLevel level, PitchOrientedContraptionEntity entity){

			if (this.startPos == null
					|| this.cannonMaterial == null
					|| !(this.presentBlockEntities.get(this.startPos) instanceof AbstractFlakcannonBreechBlockEntity breech)) return;

			ItemStack foundProjectile = breech.getMagazine();
			if (!(foundProjectile.getItem() instanceof FlakcannonRoundItem round)) return;
			ControlPitchContraption controller = entity.getController();

			//Vec3 ejectPos = entity.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.isHandle ? Direction.DOWN : this.initialOrientation.getOpposite())), 0);
			Vec3 centerPos = entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 0);
			//ItemStack ejectStack = round.getSpentItem(foundProjectile);
//			if (!ejectStack.isEmpty()) {
//				ItemEntity ejectEntity = new ItemEntity(level, ejectPos.x, ejectPos.y, ejectPos.z, ejectStack);
//				Vec3 eject = ejectPos.subtract(centerPos).normalize();
//				ejectEntity.setDeltaMovement(eject.scale(this.isHandle ? 0.1 : 0.5));
//				ejectEntity.setPickUpDelay(20);
//				level.addFreshEntity(ejectEntity);
//			}

			FlakcannonMaterialProperties properties = this.cannonMaterial.properties();
			FlakcannonProjectilePropertiesComponent roundProperties = round.getAutocannonProperties(foundProjectile);

			//boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();

			float speed = properties.baseSpeed();
			float spread = properties.baseSpread();
			//boolean canSquib = roundProperties == null || roundProperties.canSquib();
			//canSquib &= canFail;

			BlockPos currentPos = this.startPos.relative(this.initialOrientation);
			int barrelTravelled = 0;
			boolean squib = false;

//			while (this.presentBlockEntities.get(currentPos) instanceof IFlakcannonBlockEntity autocannon) {
//				ItemCannonBehavior behavior = autocannon.cannonBehavior();
//
//				if (behavior.canLoadItem(foundProjectile)) {
//					++barrelTravelled;
//					if (barrelTravelled <= properties.maxSpeedIncreases())
//						speed += properties.speedIncreasePerBarrel();
//					spread -= properties.spreadReductionPerBarrel();
//					spread = Math.max(spread, 0);
////					if (canSquib && barrelTravelled > properties.maxBarrelLength()) {
////						StructureBlockInfo oldInfo = this.blocks.get(currentPos);
////						if (oldInfo == null) return;
////						behavior.tryLoadingItem(foundProjectile);
////						CompoundTag tag = this.presentBlockEntities.get(currentPos).saveWithFullMetadata();
////						tag.remove("x");
////						tag.remove("y");
////						tag.remove("z");
////						StructureBlockInfo squibInfo = new StructureBlockInfo(currentPos, oldInfo.state(), tag);
////						this.blocks.put(currentPos, squibInfo);
////						Vec3 squibPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos), 0);
////						level.playSound(null, squibPos.x, squibPos.y, squibPos.z, oldInfo.state().getSoundType().getBreakSound(), SoundSource.BLOCKS, 10.0f, 0.0f);
////						squib = true;
////						break;
////					}
//					currentPos = currentPos.relative(this.initialOrientation);
//				} else {
//					behavior.removeItem();
////					if (canFail) {
////						Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(currentPos), 0);
////						level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2, Level.ExplosionInteraction.NONE);
////						for (int i = 0; i < 10; ++i) {
////							BlockPos pos = currentPos.relative(this.initialOrientation, i);
////							this.blocks.remove(pos);
////						}
////						if (controller != null) controller.disassemble();
////						return;
////					}
//				}
//			}
			breech.handleFiring();
			if (squib) return;

			for (BlockPos pos : this.recoilSpringPositions) {
				if (this.presentBlockEntities.get(pos) instanceof FlakcannonRecoilSpringBlockEntity spring)
					spring.handleFiring();
			}
			NetworkPlatform.sendToClientTracking(new ClientboundAnimateCannonContraptionPacket(entity), entity);

			Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 0);
			Vec3 vec1 = spawnPos.subtract(centerPos).normalize();
			spawnPos = spawnPos.subtract(vec1.scale(1.5));
			Vec3 particlePos = spawnPos;

			float recoilMagnitude = properties.baseRecoil();

			boolean isTracer = CBCConfigs.SERVER.munitions.allAutocannonProjectilesAreTracers.get() || round.isTracer();

			AbstractFlakcannonProjectile projectile = round.getAutocannonProjectile(foundProjectile, level);
			if (projectile != null) {
				projectile.setPos(spawnPos);
				projectile.setChargePower(barrelTravelled);
				projectile.setTracer(isTracer);
				projectile.setLifetime(properties.projectileLifetime());
				projectile.shoot(vec1.x, vec1.y, vec1.z, speed, spread);
				projectile.xRotO = projectile.getXRot();
				projectile.yRotO = projectile.getYRot();

				projectile.addUntouchableEntity(entity, 3);
				Entity vehicle = entity.getVehicle();
				if (vehicle != null && CBCEntityTypes.CANNON_CARRIAGE.is(vehicle))
					projectile.addUntouchableEntity(vehicle, 1);

				level.addFreshEntity(projectile);
				if (roundProperties != null) recoilMagnitude += roundProperties.addedRecoil();
			}
		short_reload = Math.max(properties.bullet_reload() + roundProperties.added_reload_tick() - 1, 0);

			recoilMagnitude *= CBCConfigs.SERVER.cannons.autocannonRecoilScale.getF();
			if (controller != null) controller.onRecoil(vec1.scale(-recoilMagnitude), entity);

			Vec3 particleVel = vec1.scale(1.25);
			for (ServerPlayer player : level.players()) {
				if (entity.getControllingPassenger() == player) continue;
				level.sendParticles(player, new AutocannonPlumeParticleData(1f), true, particlePos.x, particlePos.y, particlePos.z, 0, particleVel.x, particleVel.y, particleVel.z, 1.0f);
			}

				CBCUtils.playBlastLikeSoundOnServer(level, spawnPos.x, spawnPos.y, spawnPos.z, CBCSoundEvents.FIRE_AUTOCANNON.getMainEvent(),
						SoundSource.BLOCKS, 12, 1, 5f);


			if (projectile != null && CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
				ChunkPos cpos1 = new ChunkPos(BlockPos.containing(projectile.position()));
				RitchiesProjectileLib.queueForceLoad(level, cpos1.x, cpos1.z);
			}

	}

	@Override
	public void animate() {
		super.animate();
		if (this.presentBlockEntities.get(this.startPos) instanceof AbstractFlakcannonBreechBlockEntity breech)
			breech.handleFiring();
		for (BlockPos pos : this.recoilSpringPositions) {
			if (this.presentBlockEntities.get(pos) instanceof FlakcannonRecoilSpringBlockEntity spring)
				spring.handleFiring();
		}
	}

	@Override
	public void tick(Level level, PitchOrientedContraptionEntity entity) {
		super.tick(level, entity);

		Entity controller = entity.getControllingPassenger();
		if (this.canBeTurnedByPassenger(controller)) {
			Direction dir = entity.getInitialOrientation();
			boolean flag = (dir.getAxisDirection() == Direction.AxisDirection.POSITIVE) == (dir.getAxis() == Direction.Axis.X);
			entity.pitch = flag ? -controller.xRotO : controller.xRotO;
			entity.yaw = Mth.wrapDegrees(controller.yRotO);
			controller.setYBodyRot(controller.getYRot());
			if (CBCEntityTypes.CANNON_CARRIAGE.is(entity.getVehicle())) {
				entity.getVehicle().onPassengerTurned(entity);
			} else if (entity.getController() instanceof CannonMountBlockEntity) {
				entity.setXRot(entity.pitch);
				entity.setYRot(entity.yaw);
			}
		}

		if (CBCEntityTypes.CANNON_CARRIAGE.is(entity.getVehicle())) {
			controller = entity.getVehicle().getControllingPassenger();
		}
		if (!level.isClientSide && controller instanceof Player player) {
			String key = "";
			ControlPitchContraption controllerBlock = entity.getController();
			if (controllerBlock != null) {
				ResourceLocation loc = controllerBlock.getTypeId();
				if (loc != null) key = "." + loc.getNamespace() + "." + loc.getPath();
			}
//			player.displayClientMessage(Components.translatable("block." + CreateBigCannons.MOD_ID + ".cannon_carriage.hotbar.fireRate" + key,
//				this.getReferencedFireRate()), true);
		}

		if (level instanceof ServerLevel slevel && this.canBeFiredOnController(entity.getController())){
			this.fireShot(slevel, entity);
			if(this.bulletCount > 0){
				if(short_reload > 0){
					short_reload--;
				}else{
					this.actuallyFire(slevel, entity);
					this.bulletCount--;
				}
				if(this.bulletCount <= 0){
					this.isFiring = false;
				}
			}
		}


		for (Map.Entry<BlockPos, BlockEntity> entry : this.presentBlockEntities.entrySet()) {
			if (entry.getValue() instanceof IFlakcannonBlockEntity autocannon)
				autocannon.tickFromContraption(level, entity, entry.getKey());
		}
	}

	@Override
	public BlockPos getSeatPos(Entity entity) {
		return entity == this.entity.getControllingPassenger() ? this.startPos.relative(this.initialOrientation.getOpposite()) : super.getSeatPos(entity);
	}

	@Override
	public boolean canBeTurnedByController(ControlPitchContraption control) {
		return !this.isHandle;
	}

	@Override
	public boolean canBeTurnedByPassenger(Entity entity) {
		if (this.entity instanceof PitchOrientedContraptionEntity poce && poce.getController() instanceof FixedCannonMountBlockEntity)
			return false;
		return this.isHandle && entity instanceof Player;
	}

	@Override
	public boolean canBeFiredOnController(ControlPitchContraption control) {
		return !this.isHandle && this.entity.getVehicle() != control;
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
		if (this.presentBlockEntities.get(this.startPos) instanceof AbstractFlakcannonBreechBlockEntity breech) {
			breech.setFireRate(firePower);
			writeAndSyncSingleBlockData(breech, this.blocks.get(this.startPos), entity, this);
		}
	}

	public void trySettingFireRateCarriage(int fireRateAdjustment) {
		if (this.presentBlockEntities.get(this.startPos) instanceof AbstractFlakcannonBreechBlockEntity breech
			&& (fireRateAdjustment > 0 || breech.getFireRate() > 1)) {
			// > 0 because can't turn off carriage autocannon
			breech.setFireRate(breech.getFireRate() + fireRateAdjustment);
			writeAndSyncSingleBlockData(breech, this.blocks.get(this.startPos), entity, this);
		}
	}

	public int getReferencedFireRate() {
		return this.presentBlockEntities.get(this.startPos) instanceof AbstractFlakcannonBreechBlockEntity breech ? breech.getActualFireRate() : 0;
	}

	@Override
	public float getWeightForStress() {
		return this.cannonMaterial == null ? this.blocks.size() : this.blocks.size() * this.cannonMaterial.properties().weight();
	}

	@Override
	public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
		return poce.toGlobalVector(Vec3.atCenterOf(this.startPos), 0);
	}

    @Override
	public ICannonContraptionType getCannonType() {
		return this.isHandle ? CBCCannonContraptionTypes.HANDLE_AUTOCANNON : CBCCannonContraptionTypes.AUTOCANNON;
	}

//	public ItemStack getLoadedMagazine(){
//		return this.magazine.getStackInSlot(0);
//	}
//
//	public void setLoadedMagazine(ItemStack magazine){
//		this.magazine.setStackInSlot(0, magazine);
//	}
//
	public int getBulletCount(){
		return this.bulletCount;
	}

	public void setBulletCount(int bulletCount){
		this.bulletCount = bulletCount;
	}

    @Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("FlakcannonMaterial", this.cannonMaterial == null ? CBCMSFlakcannonMaterials.STEEL.name().toString() : this.cannonMaterial.name().toString());
		if (this.startPos != null) tag.put("StartPos", NbtUtils.writeBlockPos(this.startPos));
		if (!this.recoilSpringPositions.isEmpty()) {
			ListTag positionsTag = new ListTag();
			for (BlockPos pos : this.recoilSpringPositions)
				positionsTag.add(NbtUtils.writeBlockPos(pos));
			tag.put("RecoilSpringPositions", positionsTag);
		}
		tag.putBoolean("IsHandle", this.isHandle);
//		tag.put("magazine", this.magazine.serializeNBT());
//		tag.putInt("bulletCount", this.bulletCount);
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = FlakcannonMaterial.fromNameOrNull(CBCUtils.location(tag.getString("FlakcannonMaterial")));
		if (this.cannonMaterial == null) this.cannonMaterial = CBCMSFlakcannonMaterials.STEEL;
		this.startPos = tag.contains("StartPos") ? NbtUtils.readBlockPos(tag.getCompound("StartPos")) : null;
		this.recoilSpringPositions.clear();
		if (tag.contains("RecoilSpringPositions")) {
			ListTag positionTags = tag.getList("RecoilSpringPositions", Tag.TAG_COMPOUND);
			int sz = positionTags.size();
			for (int i = 0; i < sz; ++i)
				this.recoilSpringPositions.add(NbtUtils.readBlockPos(positionTags.getCompound(i)));
		}
		this.isHandle = tag.getBoolean("IsHandle");
//		this.magazine.deserializeNBT(tag.getCompound("magazine"));
//		this.bulletCount = tag.getInt("bulletCount");
	}

	@Override
	public ContraptionType getType() {
		return CBCContraptionTypes.MOUNTED_AUTOCANNON;
	}

	@Override
	public ItemStack insertItemIntoCannon(ItemStack stack, boolean simulate) {
		return stack;
	}

	@Override
	public ItemStack extractItemFromCannon(boolean simulate) {
		return ItemStack.EMPTY;
	}

}
