package com.cainiao1053.cbcmoreshells.cannon_control.contraption;

//import com.cainiao1053.cbcmoreshells.api.vs.ValkyrienSkies;
import com.cainiao1053.cbcmoreshells.cannon_control.cannon_types.CBCMSCannonContraptionTypes;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.ITorpedoTubeBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.TorpedoTubeBlock;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.TorpedoTubeBreechStrengthHandler;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterial;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.material.TorpedoTubeMaterialProperties;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.torpedo_end.TorpedoTubeEnd;
import com.cainiao1053.cbcmoreshells.index.CBCMSContraptionTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSTorpedoTubeMaterials;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.AbstractCannonTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.TorpedoProjectileBlock;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.content.contraptions.AssemblyException;
import com.simibubi.create.content.contraptions.ContraptionType;
import com.simibubi.create.content.contraptions.StructureTransform;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
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
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.Vector3dc;
//import org.valkyrienskies.core.api.ships.Ship;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.cannon_control.ControlPitchContraption;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;
import rbasamoyai.createbigcannons.cannon_control.contraption.AbstractMountedCannonContraption;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.BigCannonBehavior;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.effects.particles.explosions.CannonBlastWaveEffectParticleData;
import rbasamoyai.createbigcannons.effects.particles.plumes.BigCannonPlumeParticleData;
import rbasamoyai.createbigcannons.index.CBCSoundEvents;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.IntegratedPropellantProjectile;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilities;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class MountedTorpedoTubeContraption extends AbstractMountedCannonContraption {

	private TorpedoTubeMaterial cannonMaterial;
	public boolean hasFired = false;
	public boolean hasWeldedPenalty = false;

	//protected int mortarDelay = 0;
	//protected ItemStack cachedMortarRound = ItemStack.EMPTY;

	@Override
	public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
		if (!this.collectCannonBlocks(level, pos)) return false;
		this.bounds = this.createBoundsFromExtensionLengths();
		return !this.blocks.isEmpty();
	}

	private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
		BlockState startState = level.getBlockState(pos);

		if (!(startState.getBlock() instanceof TorpedoTubeBlock startCannon)) {
			return false;
		}
		if (!startCannon.isComplete(startState)) {
			throw hasIncompleteCannonBlocks(pos);
		}
		if (this.hasCannonLoaderInside(level, startState, pos)) {
			throw cannonLoaderInsideDuringAssembly(pos);
		}
		TorpedoTubeMaterial material = startCannon.getCannonMaterial();
		TorpedoTubeEnd startEnd = startCannon.getOpeningType(level, startState, pos);

		List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
		cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));

		int cannonLength = 1;

		Direction cannonFacing = startCannon.getFacing(startState);

		Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
		Direction negative = positive.getOpposite();

		BlockPos start = pos;
		BlockState nextState = level.getBlockState(pos.relative(positive));

		TorpedoTubeEnd positiveEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(positive)) && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
			start = start.relative(positive);

			if (!((TorpedoTubeBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}

			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			this.frontExtensionLength++;
			cannonLength++;

			positiveEnd = ((TorpedoTubeBlock) nextState.getBlock()).getOpeningType(level, nextState, start);

			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}

			nextState = level.getBlockState(start.relative(positive));

			if (cannonLength > 12) {
				throw cannonTooLarge();
			}
			if (positiveEnd != TorpedoTubeEnd.OPEN) break;
		}
		BlockPos positiveEndPos = positiveEnd == TorpedoTubeEnd.OPEN ? start : start.relative(negative);

		start = pos;
		nextState = level.getBlockState(pos.relative(negative));

		TorpedoTubeEnd negativeEnd = startEnd;
		while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
			start = start.relative(negative);

			if (!((TorpedoTubeBlock) nextState.getBlock()).isComplete(nextState)) {
				throw hasIncompleteCannonBlocks(start);
			}

			cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
			this.backExtensionLength++;
			cannonLength++;

			negativeEnd = ((TorpedoTubeBlock) nextState.getBlock()).getOpeningType(level, nextState, start);

			if (this.hasCannonLoaderInside(level, nextState, start)) {
				throw cannonLoaderInsideDuringAssembly(start);
			}

			nextState = level.getBlockState(start.relative(negative));

			if (cannonLength > 12) {
				throw cannonTooLarge();
			}
			if (negativeEnd != TorpedoTubeEnd.OPEN) break;
		}
		BlockPos negativeEndPos = negativeEnd == TorpedoTubeEnd.OPEN ? start : start.relative(positive);

		if (positiveEnd == negativeEnd) {
			throw invalidCannon();
		}

		boolean openEndFlag = positiveEnd == TorpedoTubeEnd.OPEN;
		this.initialOrientation = openEndFlag ? positive : negative;
		this.startPos = openEndFlag ? negativeEndPos : positiveEndPos;
		this.anchor = pos;

		this.startPos = this.startPos.subtract(pos);
		for (StructureBlockInfo blockInfo : cannonBlocks) {
			BlockPos localPos = blockInfo.pos().subtract(pos);
			StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state(), blockInfo.nbt());
			this.getBlocks().put(localPos, localBlockInfo);

			if (blockInfo.nbt() == null) continue;
			BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state(), blockInfo.nbt());
			this.presentBlockEntities.put(localPos, be);
			if (be instanceof ITorpedoTubeBlockEntity cbe && cbe.cannonBehavior().isWelded()) this.hasWeldedPenalty = true;
		}
		this.cannonMaterial = material;

		return true;
	}

	private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
		return state.getBlock() instanceof TorpedoTubeBlock;
	}

	private boolean hasCannonLoaderInside(LevelAccessor level, BlockState state, BlockPos pos) {
		BlockEntity be = level.getBlockEntity(pos);
		if (!(be instanceof ITorpedoTubeBlockEntity cannon)) return false;
		BlockState containedState = cannon.cannonBehavior().block().state();
		return ITorpedoTubeBlockEntity.isValidLoader(null, new StructureBlockInfo(BlockPos.ZERO, containedState, null));
	}

	private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, TorpedoTubeMaterial material) {
		TorpedoTubeBlock cBlock = (TorpedoTubeBlock) state.getBlock();
		if (cBlock.getCannonMaterialInLevel(level, state, pos) != material) return false;
		return level.getBlockEntity(pos) instanceof ITorpedoTubeBlockEntity cbe
			&& level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof ITorpedoTubeBlockEntity cbe1
			&& cbe.cannonBehavior().isConnectedTo(connection.getOpposite())
			&& cbe1.cannonBehavior().isConnectedTo(connection);
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
		if (this.presentBlockEntities.get(endPos) instanceof TorpQuickfiringBreechBlockEntity qfbreech)
			qfbreech.tickAnimation();
	}

	@Override
	public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
		if (!togglePower || firePower <= 0) return;
		this.fireShot(level, entity);
	}

	public int getMaxSafeCharges() {
		TorpedoTubeMaterialProperties properties = this.cannonMaterial.properties();
		StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
		if (breech == null) return 0;
		int materialStrength = properties.maxSafePropellantStress();
		int maxSafeCharges = Math.min(materialStrength, TorpedoTubeBreechStrengthHandler.getStrength(breech.state().getBlock(), materialStrength));
		if (this.hasWeldedPenalty) maxSafeCharges -= properties.weldStressPenalty();
		return Math.max(maxSafeCharges, 0);
	}

	@Override
	public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
		BlockPos endPos = this.startPos.relative(this.initialOrientation.getOpposite());
		if (this.presentBlockEntities.get(endPos) instanceof TorpQuickfiringBreechBlockEntity qfbreech && qfbreech.getOpenProgress() > 0)
			return;
		if (this.isDropMortar()) return;

		ControlPitchContraption controller = entity.getController();

		RandomSource rand = level.getRandom();
		BlockPos currentPos = this.startPos.immutable();
		int count = 0;
		int maxSafeCharges = this.getMaxSafeCharges();
		boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
		float spreadSub = this.cannonMaterial.properties().spreadReductionPerBarrel();
		boolean airGapPresent = false;

		PropellantContext propelCtx = new PropellantContext();

		List<StructureBlockInfo> projectileBlocks = new ArrayList<>();
		AbstractCannonTorpedoProjectile projectile = null;
		BlockPos assemblyPos = null;

		float minimumSpread = this.cannonMaterial.properties().minimumSpread();

		while (this.presentBlockEntities.get(currentPos) instanceof ITorpedoTubeBlockEntity cbe) {
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
			} else if (block instanceof BigCannonPropellantBlock cpropel && !(block instanceof TorpedoProjectileBlock)) {
				// Initial ignition
				if (count == 0 && !cpropel.canBeIgnited(containedBlockInfo, this.initialOrientation))
					return;
				// Incompatible propellant
				if (!propelCtx.addPropellant(cpropel, containedBlockInfo, this.initialOrientation) && canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				this.consumeBlock(behavior, currentPos, cpropel::consumePropellant);
				airGapPresent = false;
			} else if (block instanceof TorpedoProjectileBlock<?> projBlock && projectile == null) {
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
					if (projInfo.state().getBlock() instanceof TorpedoProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, i, this.initialOrientation)) continue;
					if (canFail) this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				}
				this.consumeBlock(behavior, currentPos);
				if (cannonInfo.state().is(CBCTags.CBCBlockTags.REDUCES_SPREAD)) {
					propelCtx.spread = Math.max(propelCtx.spread - spreadSub, minimumSpread);
				}
				if (projBlock.isComplete(projectileBlocks, this.initialOrientation)) {
					projectile = projBlock.getProjectile(level, projectileBlocks);
					propelCtx.chargesUsed += projectile.addedChargePower();
				}
				airGapPresent = false;
			} else {
				if (canFail) {
					this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
					return;
				} else {
					this.consumeBlock(behavior, currentPos);
				}
			}
			currentPos = currentPos.relative(this.initialOrientation);
			BlockState cannonState = cannonInfo.state();
			if (cannonState.getBlock() instanceof TorpedoTubeBlock cannon && cannon.getOpeningType(level, cannonState, currentPos) == TorpedoTubeEnd.OPEN) {
				++count;
			}
		}
		if (projectile == null && !projectileBlocks.isEmpty()) {
			StructureBlockInfo info = projectileBlocks.get(0);
			if (!(info.state().getBlock() instanceof TorpedoProjectileBlock<?> projBlock)) {
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
					if (projInfo.state().getBlock() instanceof TorpedoProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, j, this.initialOrientation)) continue;
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

		if (propelCtx.chargesUsed < minimumSpread) propelCtx.chargesUsed = minimumSpread;

		float recoilMagnitude = 0;

		if (projectile != null) {
			if (projectile instanceof IntegratedPropellantProjectile integPropel && !projectileBlocks.isEmpty()) {
				if (!propelCtx.addIntegratedPropellant(integPropel, projectileBlocks.get(0), this.initialOrientation) && canFail) {
					this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
					return;
				}
			}

			float torpVel = projectile.getTorpedoSpeed();
			//Ship ship = getShipOn(level, this.anchor);
//			if(ship !=null){
//				Vector3dc shipVel = ship.getVelocity().div(20, new Vector3d());
//				Vector3dc shipVelInShip = ship.getWorldToShip().transformDirection(shipVel, new Vector3d());
//				Vec3 shipVelVec3 = new Vec3(shipVelInShip.x(), shipVelInShip.y(), shipVelInShip.z());
//				double scale = shipVelVec3.dot(vec);
//				Vec3 proj = new Vec3(vec.x() * scale, vec.y() * scale, vec.z() * scale);
//				Vec3 rej = shipVelVec3.subtract(proj);
//				Vec3 vecOut = vec.scale(torpVel).subtract(rej); //extract normal
//				torpVel =(float) vecOut.length();
//				vec = vecOut.normalize();
//			}
			StructureBlockInfo muzzleInfo = this.blocks.get(currentPos);
			if (canFail && muzzleInfo != null && !muzzleInfo.state().isAir()) {
				this.fail(currentPos, level, entity, null, (int) propelCtx.chargesUsed);
				return;
			}
			projectile.setPos(spawnPos);
			projectile.setChargePower(propelCtx.chargesUsed);
			projectile.shoot(vec.x, vec.y, vec.z, torpVel, projectile.getTorpedoSpread());
			projectile.xRotO = projectile.getXRot();
			projectile.yRotO = projectile.getYRot();
			projectile.onShoot(this, level);

			projectile.addUntouchableEntity(entity, 1);

			level.addFreshEntity(projectile);
			recoilMagnitude += projectile.addedRecoil();
		}

		recoilMagnitude += propelCtx.recoil;
		recoilMagnitude *= CBCConfigs.SERVER.cannons.bigCannonRecoilScale.getF();
		if (controller != null) controller.onRecoil(vec.scale(-recoilMagnitude), entity);

		this.hasFired = true;

		float soundPower = Mth.clamp(1 / 16f, 0, 1);
		float tone = 2 + soundPower * -8 + level.random.nextFloat() * 4f - 2f;
		float pitch = 1.8f;
		double shakeDistance = propelCtx.chargesUsed * CBCConfigs.SERVER.cannons.bigCannonBlastDistanceMultiplier.getF();
		float volume = 8;
		Vec3 plumePos = spawnPos.subtract(vec);
		propelCtx.smokeScale = Math.max(1, propelCtx.smokeScale);

		BigCannonPlumeParticleData plumeParticle = new BigCannonPlumeParticleData(0, 0, 0);
		CannonBlastWaveEffectParticleData blastEffect = new CannonBlastWaveEffectParticleData(shakeDistance,
			BuiltInRegistries.SOUND_EVENT.wrapAsHolder(CBCSoundEvents.SMOKE_SHELL_DETONATE.getMainEvent()), SoundSource.BLOCKS,
			volume, pitch, 2, 0);
		Packet<?> blastWavePacket = new ClientboundLevelParticlesPacket(blastEffect, true, plumePos.x, plumePos.y, plumePos.z, 0, 0, 0, 1, 0);

		double blastDistSqr = volume * volume * 24 * 1.21;

		for (ServerPlayer player : level.players()) {
			level.sendParticles(player, plumeParticle, true, plumePos.x, plumePos.y, plumePos.z, 0, vec.x, vec.y, vec.z, 1.0f);
			if (player.distanceToSqr(plumePos.x, plumePos.y, plumePos.z) < blastDistSqr)
				player.connection.send(blastWavePacket);
		}

		if (projectile != null && CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
			ChunkPos cpos1 = new ChunkPos(BlockPos.containing(projectile.position()));
			RitchiesProjectileLib.queueForceLoad(level, cpos1.x, cpos1.z);
		}
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos) {
		this.consumeBlock(behavior, pos, BigCannonBehavior::removeBlock);
	}

	private void consumeBlock(BigCannonBehavior behavior, BlockPos pos, Consumer<BigCannonBehavior> action) {
		action.accept(behavior);
		CompoundTag tag = behavior.blockEntity.saveWithFullMetadata();
		tag.remove("x");
		tag.remove("y");
		tag.remove("z");

		StructureBlockInfo oldInfo = this.blocks.get(pos);
		if (oldInfo == null) return;
		StructureBlockInfo consumedInfo = new StructureBlockInfo(oldInfo.pos(), oldInfo.state(), tag);
		this.blocks.put(oldInfo.pos(), consumedInfo);
	}

	private static boolean rollSquib(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.squibChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}

	private void squibBlocks(BlockPos currentPos, List<StructureBlockInfo> projectileBlocks) {
		for (int i = 0; i < projectileBlocks.size(); ++i) {
			BlockPos pos = currentPos.relative(this.initialOrientation, i);
			StructureBlockInfo cannonInfo1 = this.blocks.get(pos);
			BlockEntity be1 = this.presentBlockEntities.get(pos);
			StructureBlockInfo projBlock = projectileBlocks.get(i);

			if (cannonInfo1 != null && be1 instanceof ITorpedoTubeBlockEntity cbe1) {
				BigCannonBehavior behavior1 = cbe1.cannonBehavior();
				behavior1.loadBlock(projBlock);
				CompoundTag tag = behavior1.blockEntity.saveWithFullMetadata();
				tag.remove("x");
				tag.remove("y");
				tag.remove("z");
				StructureBlockInfo squibInfo = new StructureBlockInfo(cannonInfo1.pos(), cannonInfo1.state(), tag);
				this.blocks.put(cannonInfo1.pos(), squibInfo);
			} else {
				CompoundTag tag = projBlock.nbt();
				if (tag != null) {
					tag.remove("x");
					tag.remove("y");
					tag.remove("z");
				}
				this.blocks.put(pos, new StructureBlockInfo(pos, projBlock.state(), tag));
			}
		}
	}

	private static boolean rollBarrelBurst(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.barrelChargeBurstChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}

	private static boolean rollOverloadBurst(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.overloadBurstChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}

	private static boolean rollFailToIgnite(RandomSource random) {
		float f = CBCConfigs.SERVER.failure.interruptedIgnitionChance.getF();
		return f != 0 && random.nextFloat() <= f;
	}

	public void fail(BlockPos localPos, Level level, PitchOrientedContraptionEntity entity, @Nullable BlockEntity failed, int charges) {
		Vec3 failurePoint = entity.toGlobalVector(Vec3.atCenterOf(localPos), 1.0f);
		float failScale = CBCConfigs.SERVER.failure.failureExplosionPower.getF();
		if (this.cannonMaterial.properties().failureMode() == TorpedoTubeMaterialProperties.FailureMode.RUPTURE) {
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

//	protected @Nullable Ship getShipOn(Level level, BlockPos pos) {
//		return ValkyrienSkies.getShipManagingBlock(level, pos);
//	}

	@Override
	public Vec3 getInteractionVec(PitchOrientedContraptionEntity poce) {
		return poce.toGlobalVector(Vec3.atCenterOf(this.startPos.relative(this.initialOrientation.getOpposite())), 1);
	}

	@Override
	public ICannonContraptionType getCannonType() {
		return CBCMSCannonContraptionTypes.TORPEDO_TUBE;
	}

	@Override
	public CompoundTag writeNBT(boolean clientData) {
		CompoundTag tag = super.writeNBT(clientData);
		tag.putString("CannonMaterial", this.cannonMaterial == null ? CBCMSTorpedoTubeMaterials.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
		if (this.hasWeldedPenalty) tag.putBoolean("WeldedCannon", true);
		//if (this.mortarDelay > 0) tag.putInt("MortarDelay", this.mortarDelay);
		//if (this.cachedMortarRound != null && !this.cachedMortarRound.isEmpty()) tag.put("CachedMortarRound", this.cachedMortarRound.save(new CompoundTag()));
		if (this.hasFired) tag.putBoolean("HasFired", true);
		return tag;
	}

	@Override
	public void readNBT(Level level, CompoundTag tag, boolean clientData) {
		super.readNBT(level, tag, clientData);
		this.cannonMaterial = TorpedoTubeMaterial.fromNameOrNull(CBCUtils.location(tag.getString("CannonMaterial")));
		this.hasWeldedPenalty = tag.contains("WeldedCannon");
		if (this.cannonMaterial == null) this.cannonMaterial = CBCMSTorpedoTubeMaterials.CAST_IRON;
		//this.mortarDelay = Math.max(0, tag.getInt("MortarDelay"));
		//this.cachedMortarRound = tag.contains("CachedMortarRound", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("CachedMortarRound")) : ItemStack.EMPTY;
		this.hasFired = tag.contains("HasFired");
	}

	@Override
	public ContraptionType getType() {
		return CBCMSContraptionTypes.TORPEDO_TUBE;
	}

	public boolean isDropMortar() {
		StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
		return breech != null && breech.state().getBlock() instanceof TorpedoTubeBlock cblock && cblock.getCannonShape() == CannonCastShape.DROP_MORTAR_END;
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
