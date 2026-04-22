package com.cainiao1053.cbcmoreshells.cannon_control.contraption;

//import com.cainiao1053.cbcmoreshells.api.vs.ValkyrienSkies;
import com.cainiao1053.cbcmoreshells.cannon_control.cannon_types.CBCMSCannonContraptionTypes;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.IProjectileRackBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlock;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.ProjectileRackBreechStrengthHandler;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackQuickfiringBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.material.ProjectileRackMaterial;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.material.ProjectileRackMaterialProperties;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.projectile_rack_end.ProjectileRackEnd;
import com.cainiao1053.cbcmoreshells.index.CBCMSContraptionTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSProjectileRackMaterials;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import com.cainiao1053.cbcmoreshells.index.CBCMSTags;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.*;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaterniond;
import org.joml.Quaterniondc;
import org.joml.Vector3d;
import org.joml.Vector3dc;
//import org.valkyrienskies.core.api.ships.Ship;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBlock;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.effects.particles.explosions.CannonBlastWaveEffectParticleData;
import rbasamoyai.createbigcannons.effects.particles.plumes.BigCannonPlumeParticleData;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.IntegratedPropellantProjectile;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilities;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class MountedProjectileRackContraption extends AbstractMountedCannonContraption {

	private ProjectileRackMaterial cannonMaterial;
	public boolean hasFired = false;
	public boolean hasWeldedPenalty = false;
	//Ship ship = null;

	private BlockState breechState = null;
	protected boolean hasStabilizer = false;

	//protected int mortarDelay = 0;
	//protected ItemStack cachedMortarRound = ItemStack.EMPTY;

	//private List<BlockPos> rackPoses = new ArrayList();

	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = this.createBoundsFromExtensionLengths();
		return !this.blocks.isEmpty();
	}

	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		BlockState startState = level.getBlockState(pos);

		if (!(startState.getBlock() instanceof ProjectileRackBlock startCannon)) {
			return false;
		}
		if (!startCannon.isComplete(startState)) {
			throw hasIncompleteCannonBlocks(pos);
		}
		if (this.hasCannonLoaderInside(level, startState, pos)) {
			throw cannonLoaderInsideDuringAssembly(pos);
		}
		ProjectileRackMaterial material = startCannon.getCannonMaterial();
		ProjectileRackEnd startEnd = startCannon.getOpeningType(level, startState, pos);

		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));

		int cannonLength = 1;

		Direction cannonFacing = startCannon.getFacing(startState);

		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
		Direction negative = positive.getOpposite();

		BlockPos start = pos;
		BlockState nextState = level.getBlockState(pos.relative(positive));

		ProjectileRackEnd positiveEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(positive)) && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
			start = start.relative(positive);

			if (!((ProjectileRackBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}

			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			this.frontExtensionLength++;
			cannonLength++;

			positiveEnd = ((ProjectileRackBlock) nextState.getBlock()).getOpeningType(level, nextState, start);

			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}

			nextState = level.getBlockState(start.relative(positive));

			if (cannonLength > 12) {
				throw cannonTooLarge();
			}
			if (positiveEnd != ProjectileRackEnd.OPEN) break;
		}
		BlockPos positiveEndPos = positiveEnd == ProjectileRackEnd.OPEN ? start : start.relative(negative);

		start = pos;
		nextState = level.getBlockState(pos.relative(negative));

		ProjectileRackEnd negativeEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);

			if (!((ProjectileRackBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}

			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			this.backExtensionLength++;
			cannonLength++;

			negativeEnd = ((ProjectileRackBlock) nextState.getBlock()).getOpeningType(level, nextState, start);

			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}

			nextState = level.getBlockState(start.relative(negative));

			if (cannonLength > 12) {
				throw cannonTooLarge();
			}
			if (negativeEnd != ProjectileRackEnd.OPEN) break;
		}
		BlockPos negativeEndPos = negativeEnd == ProjectileRackEnd.OPEN ? start : start.relative(positive);

		if (positiveEnd == negativeEnd) {
			throw invalidCannon();
		}

		boolean openEndFlag = positiveEnd == ProjectileRackEnd.OPEN;
		this.initialOrientation = openEndFlag ? positive : negative;
		this.startPos = openEndFlag ? negativeEndPos : positiveEndPos;
		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);
		HolderLookup.Provider registries = level.registryAccess();

		for (StructureBlockInfo blockInfo : cannonBlocks){

			//this.rackPoses.add(blockInfo.pos());

			if(level.getBlockState(blockInfo.pos()).getBlock() instanceof ProjectileRackQuickfiringBreechBlock){
				this.breechState = level.getBlockState(blockInfo.pos());
			}
		}

		if(this.breechState == null){
			throw invalidCannon();
		}


		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos().subtract(pos);
			BlockState alignedState = blockInfo.state().setValue(BlockStateProperties.FACING, this.breechState.getValue(BlockStateProperties.FACING));
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, alignedState, blockInfo.nbt());
			this.getBlocks().put(localPos, localBlockInfo);

			if (blockInfo.nbt() == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, alignedState, blockInfo.nbt(), registries);
			this.presentBlockEntities.put(localPos, be);
			if (be instanceof IProjectileRackBlockEntity cbe && cbe.cannonBehavior().isWelded()) this.hasWeldedPenalty = true;

			if(blockInfo.state().is(CBCMSTags.CBCMSBlockTags.RACK_STABILIZER)){
				this.hasStabilizer = true;
			}
		}
		this.cannonMaterial = material;

		return true;
	}

	private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
		return state.getBlock() instanceof ProjectileRackBlock;
	}

	private boolean hasCannonLoaderInside(LevelAccessor level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof IProjectileRackBlockEntity cannon)) return false;
		BlockState containedState = cannon.cannonBehavior().block().state();
		return IProjectileRackBlockEntity.isValidLoader(null, new StructureBlockInfo(BlockPos.ZERO, containedState, null));
	}

	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, ProjectileRackMaterial material) {
		ProjectileRackBlock cBlock = (ProjectileRackBlock) state.getBlock();
		if (cBlock.getCannonMaterialInLevel(level, state, pos) != material) return false;
		return level.getBlockEntity(pos) instanceof IProjectileRackBlockEntity cbe
			&& level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof IProjectileRackBlockEntity cbe1;
			//&& cbe.cannonBehavior().isConnectedTo(connection.getOpposite())
			//&& cbe1.cannonBehavior().isConnectedTo(connection);
	}

	public float getWeightForStress() {
		if (this.cannonMaterial == null) {
			return this.blocks.size();
		}
		return this.blocks.size() * this.cannonMaterial.properties().weight();
	}

	@Override
	public void tick(Level level, PitchOrientedContraptionEntity entity) {
		super.tick(level, entity);

		BlockPos endPos = this.startPos.relative(this.initialOrientation.getOpposite());
		if (this.presentBlockEntities.get(endPos) instanceof ProjectileRackQuickfiringBreechBlockEntity qfbreech)
			qfbreech.tickAnimation();
//		if (!level.isClientSide && this.isDropMortar() && this.mortarDelay > 0) {
//			--this.mortarDelay;
//			//if (this.mortarDelay == 0) this.actuallyFireDropMortar();
//		}
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
		if (!togglePower || firePower <= 0) return;
		this.fireShot(level, entity);
	}

	public int getMaxSafeCharges() {
		ProjectileRackMaterialProperties properties = this.cannonMaterial.properties();
		StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
		if (breech == null) return 0;
		int materialStrength = properties.maxSafePropellantStress();
		int maxSafeCharges = Math.min(materialStrength, ProjectileRackBreechStrengthHandler.getStrength(breech.state().getBlock(), materialStrength));
		if (this.hasWeldedPenalty) maxSafeCharges -= properties.weldStressPenalty();
		return Math.max(maxSafeCharges, 0);
	}

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		BlockPos endPos = this.startPos.relative(this.initialOrientation.getOpposite());
		if (this.presentBlockEntities.get(endPos) instanceof ProjectileRackQuickfiringBreechBlockEntity qfbreech && qfbreech.getOpenProgress() > 0)
			return;

		ControlPitchContraption controller = entity.getController();

		List<BlockPos> rackToClear = new ArrayList();


//
		//ship = getShipOn(level, this.anchor);



		RandomSource rand = level.getRandom();
		BlockPos currentPos = this.startPos.immutable();
		int count = 0;
		//int maxSafeCharges = this.getMaxSafeCharges();
		boolean canFail = !CBCConfigs.server().failure.disableAllFailure.get();
		float spreadSub = this.cannonMaterial.properties().spreadReductionPerBarrel();
		boolean airGapPresent = false;

		PropellantContext propelCtx = new PropellantContext();

		List<StructureBlockInfo> projectileBlocks = new ArrayList<>();
		AbstractRackedProjectile projectile = null;

		RackedProjectileBlock<?> projectileBlockDuplicate = null;

		BlockPos assemblyPos = null;

		float minimumSpread = this.cannonMaterial.properties().minimumSpread();

		HolderLookup.Provider registries = level.registryAccess();

		while (this.presentBlockEntities.get(currentPos) instanceof IProjectileRackBlockEntity cbe) {
			BigCannonBehavior behavior = cbe.cannonBehavior();
			StructureBlockInfo containedBlockInfo = behavior.block();
			StructureBlockInfo cannonInfo = this.blocks.get(currentPos);
			if (cannonInfo == null) break;

			Block block = containedBlockInfo.state().getBlock();

			if (containedBlockInfo.state().isAir()) {
				if (count == 0)
					return;
				if (projectile == null) {
					if (projectileBlocks.isEmpty()) {
						airGapPresent = true;
						propelCtx.chargesUsed = Math.max(propelCtx.chargesUsed - 1, 0);
					} else if (canFail) { // Incomplete projectile
						this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
						return;
					}
				} else {
					++propelCtx.barrelTravelled;
					if (cannonInfo.state().is(CBCTags.CBCBlockTags.REDUCES_SPREAD)) {
						propelCtx.spread = Math.max(propelCtx.spread - spreadSub, minimumSpread);
					}
				}
			} else if (block instanceof BigCannonPropellantBlock cpropel && !(block instanceof RackedProjectileBlock)) {
				// Initial ignition
				if (count == 0 && !cpropel.canBeIgnited(containedBlockInfo, this.initialOrientation))
					return;
				// Incompatible propellant
				if (!propelCtx.addPropellant(cpropel, containedBlockInfo, this.initialOrientation) && canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				this.consumeBlock(behavior, currentPos, cpropel::consumePropellant, registries);
				airGapPresent = false;
			} else if (block instanceof RackedProjectileBlock<?> projBlock && projectile == null) {
				if (canFail && airGapPresent && rollFailToIgnite(rand)) {
					Vec3 failIgnitePos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 0);
					level.playSound(null, failIgnitePos.x, failIgnitePos.y, failIgnitePos.z, cannonInfo.state().getSoundType().getBreakSound(), SoundSource.BLOCKS, 5.0f, 0.0f);
					return;
				}
				projectileBlocks.add(containedBlockInfo);
				if (assemblyPos == null) assemblyPos = currentPos.immutable();

				List<StructureBlockInfo> copy = ImmutableList.copyOf(projectileBlocks);
				for (ListIterator<StructureBlockInfo> projIter = projectileBlocks.listIterator(); projIter.hasNext(); ) {
					int i = projIter.nextIndex();
					StructureBlockInfo projInfo = projIter.next();
					if (projInfo.state().getBlock() instanceof RackedProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, i, this.initialOrientation)) continue;
					if (canFail) this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				this.consumeBlock(behavior, currentPos, registries);

				rackToClear.add(currentPos);

				if (cannonInfo.state().is(CBCTags.CBCBlockTags.REDUCES_SPREAD)) {
					propelCtx.spread = Math.max(propelCtx.spread - spreadSub, minimumSpread);
				}
				if (projBlock.isComplete(projectileBlocks, this.initialOrientation)) {
					projectile = projBlock.getProjectile(level, projectileBlocks);

					projectileBlockDuplicate = projBlock;

					propelCtx.chargesUsed += projectile.addedChargePower();
				}
				airGapPresent = false;
			}else if(propelCtx.chargesUsed >0){
				if (canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				} else {
					this.consumeBlock(behavior, currentPos, registries);
				}
			} else {
				if (canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				} else {
					this.consumeBlock(behavior, currentPos, registries);
				}
			}
			currentPos = currentPos.relative(this.initialOrientation);
			BlockState cannonState = cannonInfo.state();
			if (cannonState.getBlock() instanceof ProjectileRackBlock cannon && cannon.getOpeningType(level, cannonState, currentPos) == ProjectileRackEnd.OPEN) {
				++count;
			}
		}
		if (projectile == null && !projectileBlocks.isEmpty()) {
			StructureBlockInfo info = projectileBlocks.get(0);
			if (!(info.state().getBlock() instanceof RackedProjectileBlock<?> projBlock)) {
				if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
			int remaining = projBlock.getExpectedSize() - projectileBlocks.size();
			if (remaining < 1) {
				if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
			for (int i = 0; i < remaining; ++i) {
				StructureBlockInfo additionalInfo = this.blocks.remove(currentPos);
				if (additionalInfo == null) {
					if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
				projectileBlocks.add(additionalInfo);

				List<StructureBlockInfo> copy = ImmutableList.copyOf(projectileBlocks);
				for (ListIterator<StructureBlockInfo> projIter = projectileBlocks.listIterator(); projIter.hasNext(); ) {
					int j = projIter.nextIndex();
					StructureBlockInfo projInfo = projIter.next();
					if (projInfo.state().getBlock() instanceof RackedProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, j, this.initialOrientation)) continue;
					if (canFail) this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
				currentPos = currentPos.relative(this.initialOrientation);
			}
			assemblyPos = currentPos.immutable().relative(this.initialOrientation.getOpposite());
			if (projBlock.isComplete(projectileBlocks, this.initialOrientation)) {
				projectile = projBlock.getProjectile(level, projectileBlocks);
				propelCtx.chargesUsed += projectile.addedChargePower();
			} else if (canFail) {
				this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
		}

		Vec3 spawnPos = entity.toGlobalVector(Vec3.atCenterOf(currentPos.relative(this.initialOrientation)), 0);
		Vec3 vec = spawnPos.subtract(entity.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 0)).normalize();
		spawnPos = spawnPos.subtract(vec.scale(2));
		//float initVel = projectile.initialVelocity();


		if (propelCtx.chargesUsed < minimumSpread) propelCtx.chargesUsed = minimumSpread;

		float recoilMagnitude = 0;

		if (projectile != null) {
			float initVel = projectile.initialVelocity();
//			if(ship != null) {
//				Vector3d vecV3d = new Vector3d(vec.x, vec.y, vec.z);
//				Vector3d shipVel = new Vector3d(ship.getVelocity().x(), ship.getVelocity().y(), ship.getVelocity().z());
//
//				if(this.hasStabilizer){ //some condition, pending in future
//					Vector3dc shipVelForFix = ship.getVelocity().div(20, new Vector3d());
//					Vector3dc shipVelInShip = ship.getWorldToShip().transformDirection(shipVelForFix, new Vector3d());
//					Vec3 shipVelVec3 = new Vec3(shipVelInShip.x(), shipVelInShip.y(), shipVelInShip.z());
//					double scale = shipVelVec3.dot(vec);
//					Vec3 proj = new Vec3(vec.x() * scale, vec.y() * scale, vec.z() * scale);
//					Vec3 rej = shipVelVec3.subtract(proj);
//					Vec3 vecOut = vec.scale(initVel).subtract(rej); //extract normal
//					initVel =(float) vecOut.length();
//					vec = vecOut.normalize();
//				}
//
//				Quaterniondc trf = ship.getTransform().getShipToWorldRotation().conjugate(new Quaterniond());
//				Vector3d velInShip = trf.transform(shipVel);
//				double projScale = velInShip.dot(vecV3d)*0.025;
//				if(projScale<0){
//					projScale = 0;
//				}
//				Vector3d velPrjGunInShip = vecV3d.mul(projScale);
//				spawnPos = new Vec3(spawnPos.x +velPrjGunInShip.x, spawnPos.y + velPrjGunInShip.y, spawnPos.z + velPrjGunInShip.z);
//			}

			if (projectile instanceof IntegratedPropellantProjectile integPropel && !projectileBlocks.isEmpty()) {
				if (!propelCtx.addIntegratedPropellant(integPropel, projectileBlocks.get(0), this.initialOrientation) && canFail) {
					this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
			}
			StructureBlockInfo muzzleInfo = this.blocks.get(currentPos);
			if (canFail && muzzleInfo != null && !muzzleInfo.state().isAir()) {
				this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}

//			Direction breechDirection = this.breechState.getValue(BlockStateProperties.FACING);
//			if(breechDirection != Direction.DOWN && breechDirection != Direction.UP){
//				if(this.breechState.getValue(CEILING)){
//					spawnPos = spawnPos.subtract(0,0.5*(1-Math.max(vec.y,0)),0);
//				}else{spawnPos = spawnPos.add(0,0.5*(1-Math.max(vec.y,0)),0);}
//			}

			if(projectile instanceof AbstractDualRackedRocketProjectile && projectileBlockDuplicate!=null) {
				Vec3 vecRot = vec.yRot((float)Math.PI/2).scale(0.35);
				multiShoot(level, entity, projectileBlockDuplicate, projectileBlocks, propelCtx.chargesUsed, spawnPos, vec, vecRot, initVel);
				spawnPos = spawnPos.subtract(vecRot);
			}

			projectile.setPos(spawnPos);
			projectile.setChargePower(propelCtx.chargesUsed);
			//AbstractRackedProjectile projectile1 = projectile;
			projectile.shoot(vec.x, vec.y, vec.z, initVel, projectile.projectileSpread());
			//projectile1.shoot(vec.x, vec.y, vec.z, projectile.initialVelocity()+0.1f, projectile.projectileSpread());
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();

			projectile.addUntouchableEntity(entity, 6);

			level.addFreshEntity(projectile);
			recoilMagnitude += projectile.addedRecoil();
		}

		recoilMagnitude += propelCtx.recoil;
		recoilMagnitude *= CBCConfigs.server().cannons.bigCannonRecoilScale.getF();
		if (controller != null) controller.onRecoil(vec.scale(-recoilMagnitude), entity);

		this.hasFired = true;

		float soundPower = Mth.clamp(1 / 16f, 0, 1);
		float tone = 2 + soundPower * -8 + level.random.nextFloat() * 4f - 2f;
		float pitch = 1.2f;
		double shakeDistance = propelCtx.chargesUsed * CBCConfigs.server().cannons.bigCannonBlastDistanceMultiplier.getF();
		float volume = 6;
		float distMul = 1;
		Vec3 plumePos = spawnPos.subtract(vec);
		propelCtx.smokeScale = Math.max(1, propelCtx.smokeScale);

		SoundEvent soundSource;
		if(projectile instanceof AbstractDualRackedRocketProjectile || projectile instanceof AbstractRackedRocketProjectile
		|| projectile instanceof AbstractRackedLoiteringRocketProjectile) {
			soundSource = CBCMSSoundEvents.ROCKET_LAUNCH.getMainEvent();
			volume = 2f;
			pitch = 1f;
			distMul = 8;
		}else{
			soundSource = SoundEvents.STONE_BUTTON_CLICK_OFF;
		}

		BigCannonPlumeParticleData plumeParticle = new BigCannonPlumeParticleData(0, 0, 0);
		CannonBlastWaveEffectParticleData blastEffect = new CannonBlastWaveEffectParticleData(shakeDistance,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(soundSource), SoundSource.BLOCKS,
			volume, pitch, 2f, 0);
		Packet<?> blastWavePacket = new ClientboundLevelParticlesPacket(blastEffect, true, plumePos.x, plumePos.y, plumePos.z, 0, 0, 0, 1, 0);

		double blastDistSqr = volume * volume * 24 * 1.21 * distMul;

		for (ServerPlayer player : level.players()) {
			level.sendParticles(player, plumeParticle, true, plumePos.x, plumePos.y, plumePos.z, 0, vec.x, vec.y, vec.z, 1.0f);
			if (player.distanceToSqr(plumePos.x, plumePos.y, plumePos.z) < blastDistSqr)
				player.connection.send(blastWavePacket);
		}

		if (projectile != null && CBCConfigs.server().munitions.projectilesCanChunkload.get()) {
			ChunkPos cpos1 = new ChunkPos(BlockPos.containing(projectile.position()));
			RitchiesProjectileLib.queueForceLoad(level, cpos1.x, cpos1.z);
		}

		for(BlockPos c : rackToClear) {
			Object presentEntity = this.presentBlockEntities.get(c);
			if (!(presentEntity instanceof IProjectileRackBlockEntity)) {
				return;
			}

			IProjectileRackBlockEntity cbe = (IProjectileRackBlockEntity) presentEntity;
			StructureTemplate.StructureBlockInfo containedBlockInfo = ((BigCannonBehavior)cbe.cannonBehavior()).block();

			((BigCannonBehavior)cbe.cannonBehavior()).removeBlock();
			BigCannonBlock.writeAndSyncSingleBlockData((BlockEntity)this.presentBlockEntities.get(c), (StructureTemplate.StructureBlockInfo)this.blocks.get(c), entity, this);

		}
	}

	public void multiShoot(ServerLevel level, PitchOrientedContraptionEntity entity, RackedProjectileBlock<?> prjB, List<StructureBlockInfo> projectileBlocks, float chargesUsed, Vec3 spawnPos, Vec3 vec, Vec3 vecRot, float initVelMag) {
		AbstractRackedProjectile p = prjB.getProjectile(level, projectileBlocks);
		p.setPos(spawnPos.add(vecRot));
		p.setChargePower(chargesUsed);
		p.shoot(vec.x, vec.y, vec.z, initVelMag, p.projectileSpread()+0.5f);
		p.xRotO = p.getXRot();
		p.yRotO = p.getYRot();
		p.addUntouchableEntity(entity, 6);
		level.addFreshEntity(p);
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos, HolderLookup.Provider registries) {
		this.consumeBlock(behavior, pos, BigCannonBehavior::removeBlock, registries);
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos, Consumer<BigCannonBehavior> action, HolderLookup.Provider registries) {
		action.accept(behavior);
		CompoundTag tag = behavior.blockEntity.saveWithFullMetadata(registries);
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");

		StructureBlockInfo oldInfo = this.blocks.get(pos);
		if (oldInfo == null) return;
		StructureBlockInfo consumedInfo = new StructureBlockInfo(oldInfo.pos(), oldInfo.state(), tag);
		this.blocks.put(oldInfo.pos(), consumedInfo);
	}

	private static boolean rollFailToIgnite(RandomSource random) {
		float f = CBCConfigs.server().failure.interruptedIgnitionChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}

	public void fail(BlockPos localPos, Level level, PitchOrientedContraptionEntity entity, @Nullable BlockEntity failed, int charges) {
		Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(localPos), 1.0f);
		float failScale = CBCConfigs.server().failure.failureExplosionPower.getF();
		if (this.cannonMaterial.properties().failureMode() == ProjectileRackMaterialProperties.FailureMode.RUPTURE) {
			int failInt = Mth.ceil(failScale);
			BlockPos startPos = localPos.relative(this.initialOrientation.getOpposite(), failInt);
			for (int i = 0; i < failInt * 2 + 1; ++i) {
				BlockPos pos = startPos.relative(this.initialOrientation, i);
				this.blocks.remove(pos);
				this.presentBlockEntities.remove(pos);
			}
			ControlPitchContraption controller = entity.getController();
			if (controller != null) controller.disassemble();

			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, 2 * failScale + 1, Level.ExplosionInteraction.NONE);
		} else {
			for (Iterator<Map.Entry<BlockPos, StructureBlockInfo>> iter = this.blocks.entrySet().iterator(); iter.hasNext(); ) {
				Map.Entry<BlockPos, StructureBlockInfo> entry = iter.next();
				this.presentBlockEntities.remove(entry.getKey());
				iter.remove();
			}

			float power = (float) charges * failScale;
			level.explode(null, failurePoint.x, failurePoint.y, failurePoint.z, power, Level.ExplosionInteraction.BLOCK);
			entity.discard();
		}
	}

	@Override
	public void addPassengersToWorld(Level world, StructureTransform transform, List<Entity> seatedEntities) {
		super.addPassengersToWorld(world, transform, seatedEntities);
//		if (!world.isClientSide && this.isDropMortar() && this.cachedMortarRound != null && !this.cachedMortarRound.isEmpty() && this.entity != null) {
//			Vec3 pos = this.entity.toGlobalVector(Vec3.atCenterOf(this.startPos), 0);
//			world.addFreshEntity(new ItemEntity(world, pos.x, pos.y, pos.z, this.cachedMortarRound.copy()));
//		}
	}

	@Override
	public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
		return poce.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.initialOrientation.getOpposite())), 1);
	}

	@Override
	public ICannonContraptionType getCannonType() {
		return CBCMSCannonContraptionTypes.PROJECTILE_RACK;
	}

	@Override
	public CompoundTag writeNBT(HolderLookup.Provider registries, boolean spawnPacket) {
		CompoundTag tag = super.writeNBT(registries, spawnPacket);
		tag.putString("CannonMaterial", this.cannonMaterial == null ? CBCMSProjectileRackMaterials.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.hasWeldedPenalty) tag.putBoolean("WeldedCannon", true);
		//if (this.mortarDelay > 0) tag.putInt("MortarDelay", this.mortarDelay);
		//if (this.cachedMortarRound != null && !this.cachedMortarRound.isEmpty()) tag.put("CachedMortarRound", this.cachedMortarRound.save(new CompoundTag()));
		if (this.hasFired) tag.putBoolean("HasFired", true);
		tag.putBoolean("hasStabilizer", this.hasStabilizer);
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = ProjectileRackMaterial.fromNameOrNull(CBCUtils.location(tag.getString("CannonMaterial")));
		this.hasWeldedPenalty = tag.contains("WeldedCannon");
		if (this.cannonMaterial == null) this.cannonMaterial = CBCMSProjectileRackMaterials.CAST_IRON;
		//this.mortarDelay = Math.max(0, tag.getInt("MortarDelay"));
		//this.cachedMortarRound = tag.contains("CachedMortarRound", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("CachedMortarRound")) : ItemStack.EMPTY;
		this.hasFired = tag.contains("HasFired");
		this.hasStabilizer = tag.getBoolean("hasStabilizer");
	}

	@Override
	public ContraptionType getType() {
		return CBCMSContraptionTypes.PROJECTILE_RACK.value();
	}

	public boolean isDropMortar() {
		StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
		return breech != null && breech.state().getBlock() instanceof ProjectileRackBlock cblock && cblock.getCannonShape() == CannonCastShape.DROP_MORTAR_END;
	}

//	protected @Nullable Ship getShipOn(Level level, BlockPos pos) {
//		return ValkyrienSkies.getShipManagingBlock(level, pos);
//	}

	protected Vector3dc getVecFromAngle(double xrot, double yrot){
		double pitchAngle = Math.toRadians(xrot);
		double yawAngle = Math.toRadians(yrot);
		double cosPitch = Math.cos(pitchAngle);
		return new Vector3d(-Math.sin(yawAngle)*cosPitch, Math.sin(pitchAngle), -Math.cos(yawAngle)*cosPitch);
	}



	protected static class PropellantContext {
		public float chargesUsed = 0;
		public float recoil = 0;
		public float stress = 0;
		public float smokeScale = 0;
		public int barrelTravelled = 0;
		public float spread = 0.0f;
		public List<StructureBlockInfo> propellantBlocks = new ArrayList<>();

		public boolean addPropellant(BigCannonPropellantBlock propellant, StructureBlockInfo info, Direction initialOrientation) {
			this.propellantBlocks.add(info);
			if (!safeLoad(ImmutableList.copyOf(this.propellantBlocks), initialOrientation)) return false;
			float power = Math.max(0, propellant.getChargePower(info));
			this.chargesUsed += power;
			this.smokeScale += power;
			this.recoil = Math.max(0, propellant.getRecoil(info));
			this.stress += propellant.getStressOnCannon(info);
			this.spread += propellant.getSpread(info);
			return true;
		}

		public boolean addIntegratedPropellant(IntegratedPropellantProjectile propellant, StructureBlockInfo firstInfo, Direction initialOrientation) {
			List<StructureBlockInfo> copy = ImmutableList.<StructureBlockInfo>builder().addAll(this.propellantBlocks).add(firstInfo).build();
			if (!safeLoad(copy, initialOrientation)) return false;
			float power = Math.max(0, propellant.getChargePower());
			this.chargesUsed += power;
			this.smokeScale += power;
			this.stress += propellant.getStressOnCannon();
			this.spread += propellant.getSpread();
			return true;
		}

		public static boolean safeLoad(List<StructureBlockInfo> propellant, Direction orientation) {
			Map<Block, Integer> allowedCounts = new HashMap<>();
			Map<Block, Integer> actualCounts = new HashMap<>();
			for (ListIterator<StructureBlockInfo> iter = propellant.listIterator(); iter.hasNext(); ) {
				int index = iter.nextIndex();
				StructureBlockInfo info = iter.next();

				Block block = info.state().getBlock();
				if (!(block instanceof BigCannonPropellantBlock cpropel) || !(cpropel.isValidAddition(info, index, orientation))) return false;
				if (actualCounts.containsKey(block)) {
					actualCounts.put(block, actualCounts.get(block) + 1);
				} else {
					actualCounts.put(block, 1);
				}
				BigCannonPropellantCompatibilities compatibilities = BigCannonPropellantCompatibilityHandler.getCompatibilities(block);
				for (Map.Entry<Block, Integer> entry : compatibilities.validPropellantCounts().entrySet()) {
					Block block1 = entry.getKey();
					int oldCount = allowedCounts.getOrDefault(block1, -1);
					int newCount = entry.getValue();
					if (newCount >= 0 && (oldCount < 0 || newCount < oldCount)) allowedCounts.put(block1, newCount);
				}
			}
			for (Map.Entry<Block, Integer> entry : actualCounts.entrySet()) {
				Block block = entry.getKey();
				if (allowedCounts.containsKey(block) && allowedCounts.get(block) < entry.getValue()) return false;
			}
			return true;
		}

	}

}
