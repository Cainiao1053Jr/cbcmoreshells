package com.cainiao1053.cbcmoreshells.cannon_control.contraption;

import com.cainiao1053.cbcmoreshells.cannon_control.cannon_types.CBCMSCannonContraptionTypes;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.IDualCannonBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.DualCannonBreechStrengthHandler;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonMountPoint;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEnd;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialProperties;
import com.cainiao1053.cbcmoreshells.index.CBCMSContraptionTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSDualCannonMaterials;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import com.cainiao1053.cbcmoreshells.index.CBCMSTags;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.AbstractDualCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.DualCannonProjectileBlock;
import com.cainiao1053.cbcmoreshells.network.CBCMSNetworkImpl;
import com.cainiao1053.cbcmoreshells.network.ClientboundCannonCmdSyncPacket;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.items.ItemStackHandler;
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
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.ProjectileBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.BigCannonPropellantBlock;
import rbasamoyai.createbigcannons.munitions.big_cannon.propellant.IntegratedPropellantProjectile;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilities;
import rbasamoyai.createbigcannons.munitions.config.BigCannonPropellantCompatibilityHandler;
import rbasamoyai.createbigcannons.utils.CBCUtils;
import rbasamoyai.ritchiesprojectilelib.RitchiesProjectileLib;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Consumer;

public class MountedDualCannonContraption extends AbstractMountedCannonContraption {

    private DualCannonMaterial cannonMaterial;
    public boolean hasFired = false;
    public boolean hasWeldedPenalty = false;
    public boolean commandEffect = false;
    public int commandCooldown = 1800;
    public int commandLeft = 0;
    public float commandDurabilityMassModifier = 1;
    public float commandSpreadModifier = 1;
    public float commandReloadTimeModifier = 1;
    public float commandLifetimeModifier = 1;
    private float equipmentDurabilityModifier = 1;
    private float equipmentSpreadModifier = 1;
    private float equipmentReloadModifier = 1;
    private float equipmentLifetimeModifier = 1;
    private float commandCooldownModifier = 1;
    private float commandDurationModifier = 1;
    private int barrelInverter = 1;
    private float additionalSpreadCoef = 1;
    private boolean reduceCooldownOnHit = true;

    private boolean hasMagazine = false;
    private int magazineReloadDelay = 20;
    private ItemStackHandler cachedMunition = new ItemStackHandler(3);

    @Override
    public boolean assemble(Level level, BlockPos pos) throws AssemblyException {
        if (!this.collectCannonBlocks(level, pos)) return false;
        this.bounds = this.createBoundsFromExtensionLengths();
        //this.commandCooldown = this.cannonMaterial.properties().combatCommandCooldown();
        return !this.blocks.isEmpty();
    }

    private boolean collectCannonBlocks(Level level, BlockPos pos) throws AssemblyException {
        BlockState startState = level.getBlockState(pos);

        if (!(startState.getBlock() instanceof DualCannonBlock startCannon)) {
            return false;
        }
        if (!startCannon.isComplete(startState)) {
            throw hasIncompleteCannonBlocks(pos);
        }
        if (this.hasCannonLoaderInside(level, startState, pos)) {
            throw cannonLoaderInsideDuringAssembly(pos);
        }
        DualCannonMaterial material = startCannon.getCannonMaterial();
        DualCannonEnd startEnd = startCannon.getOpeningType(level, startState, pos);

        List<StructureBlockInfo> cannonBlocks = new ArrayList<>();
        cannonBlocks.add(new StructureBlockInfo(pos, startState, this.getBlockEntityNBT(level, pos)));

        int cannonLength = 1;

        Direction cannonFacing = startCannon.getFacing(startState);

        Direction positive = Direction.get(Direction.AxisDirection.POSITIVE, cannonFacing.getAxis());
        Direction negative = positive.getOpposite();

        BlockPos start = pos;
        BlockState nextState = level.getBlockState(pos.relative(positive));

        DualCannonEnd positiveEnd = startEnd;
        while (this.isValidCannonBlock(level, nextState, start.relative(positive)) && this.isConnectedToCannon(level, nextState, start.relative(positive), positive, material)) {
            start = start.relative(positive);

            if (!((DualCannonBlock) nextState.getBlock()).isComplete(nextState)) {
                throw hasIncompleteCannonBlocks(start);
            }

            cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
            this.frontExtensionLength++;
            cannonLength++;

            positiveEnd = ((DualCannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);

            if (this.hasCannonLoaderInside(level, nextState, start)) {
                throw cannonLoaderInsideDuringAssembly(start);
            }

            nextState = level.getBlockState(start.relative(positive));

            if (cannonLength > 12) {
                throw cannonTooLarge();
            }
            if (positiveEnd != DualCannonEnd.OPEN) break;
        }
        BlockPos positiveEndPos = positiveEnd == DualCannonEnd.OPEN ? start : start.relative(negative);

        start = pos;
        nextState = level.getBlockState(pos.relative(negative));

        DualCannonEnd negativeEnd = startEnd;
        while (this.isValidCannonBlock(level, nextState, start.relative(negative)) && this.isConnectedToCannon(level, nextState, start.relative(negative), negative, material)) {
            start = start.relative(negative);

            if (!((DualCannonBlock) nextState.getBlock()).isComplete(nextState)) {
                throw hasIncompleteCannonBlocks(start);
            }

            cannonBlocks.add(new StructureBlockInfo(start, nextState, this.getBlockEntityNBT(level, start)));
            this.backExtensionLength++;
            cannonLength++;

            negativeEnd = ((DualCannonBlock) nextState.getBlock()).getOpeningType(level, nextState, start);

            if (this.hasCannonLoaderInside(level, nextState, start)) {
                throw cannonLoaderInsideDuringAssembly(start);
            }

            nextState = level.getBlockState(start.relative(negative));

            if (cannonLength > 20) {
                throw cannonTooLarge();
            }
            if (negativeEnd != DualCannonEnd.OPEN) break;
        }
        BlockPos negativeEndPos = negativeEnd == DualCannonEnd.OPEN ? start : start.relative(positive);

        if (positiveEnd == negativeEnd) {
            throw invalidCannon();
        }

        boolean openEndFlag = positiveEnd == DualCannonEnd.OPEN;
        this.initialOrientation = openEndFlag ? positive : negative;
        this.startPos = openEndFlag ? negativeEndPos : positiveEndPos;
        this.anchor = pos;

        this.startPos = this.startPos.subtract(pos);
        boolean combatCommandEquipmentPresent = false;
        boolean magazineEquipmentPresent = false;
        for (StructureBlockInfo blockInfo : cannonBlocks) {
            BlockPos localPos = blockInfo.pos().subtract(pos);
            StructureBlockInfo localBlockInfo = new StructureBlockInfo(localPos, blockInfo.state(), blockInfo.nbt());
            this.getBlocks().put(localPos, localBlockInfo);

            if (blockInfo.nbt() == null) continue;
            BlockEntity be = BlockEntity.loadStatic(localPos, blockInfo.state(), blockInfo.nbt());
            this.presentBlockEntities.put(localPos, be);
            if (be instanceof IDualCannonBlockEntity cbe && cbe.cannonBehavior().isWelded())
                this.hasWeldedPenalty = true;
            if (blockInfo.state().is(CBCMSTags.CBCMSBlockTags.COMBAT_COMMAND_EQUIPMENT)) {
                combatCommandEquipmentPresent = true;
            }
            if (blockInfo.state().is(CBCMSTags.CBCMSBlockTags.MAGAZINE_EQUIPMENT)) {
                magazineEquipmentPresent = true;
            }
        }
        this.cannonMaterial = material;
        if (combatCommandEquipmentPresent) {
            addCommandEquipmentModification();
        }
        if(magazineEquipmentPresent) {
            installMagazine();
        }
        this.commandCooldown = (int) (this.cannonMaterial.properties().combatCommandCooldown() * this.commandCooldownModifier);

        return true;
    }

    public void addCommandEquipmentModification() {
        this.equipmentLifetimeModifier += -0.07F;
        this.equipmentReloadModifier += 0.1F;
        this.equipmentSpreadModifier += 0.1F;
        this.equipmentDurabilityModifier += -0.08F;
        this.commandCooldownModifier = 2.0F;
        this.commandDurationModifier = 3.0F;
    }

    public void installMagazine() {
        this.hasMagazine = true;
        this.equipmentReloadModifier += 0.18F;
    }

    private boolean isValidCannonBlock(LevelAccessor level, BlockState state, BlockPos pos) {
        return state.getBlock() instanceof DualCannonBlock;
    }

    private boolean hasCannonLoaderInside(LevelAccessor level, BlockState state, BlockPos pos) {
        BlockEntity be = level.getBlockEntity(pos);
        if (!(be instanceof IDualCannonBlockEntity cannon)) return false;
        BlockState containedState = cannon.cannonBehavior().block().state();
        return IDualCannonBlockEntity.isValidLoader(null, new StructureBlockInfo(BlockPos.ZERO, containedState, null));
    }

    private boolean isConnectedToCannon(LevelAccessor level, BlockState state, BlockPos pos, Direction connection, DualCannonMaterial material) {
        DualCannonBlock cBlock = (DualCannonBlock) state.getBlock();
        if (cBlock.getCannonMaterialInLevel(level, state, pos) != material) return false;
        return level.getBlockEntity(pos) instanceof IDualCannonBlockEntity cbe
                && level.getBlockEntity(pos.relative(connection.getOpposite())) instanceof IDualCannonBlockEntity cbe1
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
        if (this.presentBlockEntities.get(endPos) instanceof DualCannonQuickfiringBreechBlockEntity qfbreech)
            qfbreech.tickAnimation();
//		if (!level.isClientSide && this.isDropMortar() && this.mortarDelay > 0) {
//			--this.mortarDelay;
//			//if (this.mortarDelay == 0) this.actuallyFireDropMortar();
//		}
        if (this.commandCooldown > 0) {
            if (!this.reduceCooldownOnHit) {
                commandCooldown--;
            }
        } else if (this.commandLeft > 0) {
            commandLeft--;
            if (commandLeft == 0) {
                //this.commandEffect = false;
                this.commandCooldown = (int) (this.cannonMaterial.properties().combatCommandCooldown() * this.commandCooldownModifier);
                deactivateCombatCommand();
            }
        }
        magazineAutoLoad();
    }

    protected void magazineAutoLoad() {
        if (this.hasMagazine) {
            if (this.magazineReloadDelay > 0) {
                --this.magazineReloadDelay;
            } else {
                this.magazineReloadDelay = Math.min((int)(this.getCannonMaterial().properties().reloadTimeModifier() * 12.0F), 40);
                ItemStack loaded = this.getLoadedMunition();
                if (loaded != null && loaded.isEmpty()) {
                    int magazineSlots = this.cachedMunition.getSlots();

                    for(int i = 0; i < magazineSlots; ++i) {
                        ItemStack stack = this.cachedMunition.getStackInSlot(i);
                        if (!stack.isEmpty()) {
                            Item var7 = stack.getItem();
                            if (var7 instanceof BlockItem) {
                                BlockItem blockItem = (BlockItem)var7;
                                Block var8 = blockItem.getBlock();
                                if (var8 instanceof BigCannonMunitionBlock) {
                                    BigCannonMunitionBlock munition = (BigCannonMunitionBlock)var8;
                                    if (munition instanceof DualCannonProjectileBlock) {
                                        DualCannonMountPoint.loadProjectile(stack, munition, this.entity, this);
                                    }
                                }
                            }

                            this.cachedMunition.setStackInSlot(i, ItemStack.EMPTY);
                            return;
                        }
                    }

                }
            }
        }
    }

    @Override
    public void onRedstoneUpdate(ServerLevel level, PitchOrientedContraptionEntity entity, boolean togglePower, int firePower, ControlPitchContraption controller) {
        if (!togglePower || firePower <= 0) return;
        this.fireShot(level, entity);
    }

    public int getMaxSafeCharges() {
        DualCannonMaterialProperties properties = this.cannonMaterial.properties();
        StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
        if (breech == null) return 0;
        int materialStrength = properties.maxSafePropellantStress();
        int maxSafeCharges = Math.min(materialStrength, DualCannonBreechStrengthHandler.getStrength(breech.state().getBlock(), materialStrength));
        if (this.hasWeldedPenalty) maxSafeCharges -= properties.weldStressPenalty();
        return Math.max(maxSafeCharges, 0);
    }

    @Override
    public void fireShot(ServerLevel level, PitchOrientedContraptionEntity entity) {
        BlockPos endPos = this.startPos.relative(this.initialOrientation.getOpposite());
        if (this.presentBlockEntities.get(endPos) instanceof DualCannonQuickfiringBreechBlockEntity qfbreech && qfbreech.getOpenProgress() > 0)
            return;
        if (this.isDropMortar()) return;

        ControlPitchContraption controller = entity.getController();

        RandomSource rand = level.getRandom();
        BlockPos currentPos = this.startPos.immutable();
        int count = 0;
        int maxSafeCharges = this.getMaxSafeCharges();
        boolean canFail = !CBCConfigs.SERVER.failure.disableAllFailure.get();
        float spreadSub = this.cannonMaterial.properties().spreadReductionPerBarrel();
        int subLength = 0;
        boolean airGapPresent = false;

        PropellantContext propelCtx = new PropellantContext();

        List<StructureBlockInfo> projectileBlocks = new ArrayList<>();
        AbstractDualCannonProjectile projectile = null;
        AbstractDualCannonProjectile secondary_projectile = null;
        BlockPos assemblyPos = null;

        float minimumSpread = this.cannonMaterial.properties().minimumSpread();

        while (this.presentBlockEntities.get(currentPos) instanceof IDualCannonBlockEntity cbe) {
            BigCannonBehavior behavior = cbe.cannonBehavior();
            StructureBlockInfo containedBlockInfo = behavior.block();
            StructureBlockInfo cannonInfo = this.blocks.get(currentPos);
            if (cannonInfo == null) break;

            Block block = containedBlockInfo.state().getBlock();

            if (containedBlockInfo.state().isAir()) {
                if (count == 0)
                    return;
                if (projectile == null && secondary_projectile == null) {
                    if (projectileBlocks.isEmpty()) {
                        airGapPresent = true;
                        //propelCtx.chargesUsed = Math.max(propelCtx.chargesUsed - 1, 0);
                    } else if (canFail) { // Incomplete projectile
                        this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
                        return;
                    }
                } else {
                    ++propelCtx.barrelTravelled;
                    if (cannonInfo.state().is(CBCTags.CBCBlockTags.REDUCES_SPREAD)) {
                        //propelCtx.spread = Math.max(propelCtx.spread - spreadSub, minimumSpread);
                        subLength++;
                    }
                }
            } else if (block instanceof DualCannonProjectileBlock<?> projBlock && (projectile == null || secondary_projectile == null)) {
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
                    if (projInfo.state().getBlock() instanceof DualCannonProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, i, this.initialOrientation))
                        continue;
                    if (canFail)
                        this.fail(currentPos, level, entity, behavior.blockEntity, (int) propelCtx.chargesUsed);
                    return;
                }
                this.consumeBlock(behavior, currentPos);
                if (cannonInfo.state().is(CBCTags.CBCBlockTags.REDUCES_SPREAD)) {
                    //propelCtx.spread = Math.max(propelCtx.spread - spreadSub, minimumSpread);
                    subLength++;
                }
                if (projBlock.isComplete(projectileBlocks, this.initialOrientation)) {
                    if (projectile == null) {
                        projectile = projBlock.getProjectile(level, projectileBlocks);
                        projectileBlocks.clear();
                    } else {
                        secondary_projectile = projBlock.getProjectile(level, projectileBlocks); //put munition in any of slot
                    }
                    propelCtx.chargesUsed += projectile.addedChargePower();
                }
                airGapPresent = false;
            } else {
                if (canFail) {
                    this.fail(currentPos, level, entity, behavior.blockEntity, (int) 4);
                    return;
                } else {
                    this.consumeBlock(behavior, currentPos);
                }
            }
            currentPos = currentPos.relative(this.initialOrientation);
            BlockState cannonState = cannonInfo.state();
            if (cannonState.getBlock() instanceof DualCannonBlock cannon && cannon.getOpeningType(level, cannonState, currentPos) == DualCannonEnd.OPEN) {
                ++count;
            }
        }
        if ((projectile == null && secondary_projectile == null) && !projectileBlocks.isEmpty()) {
            StructureBlockInfo info = projectileBlocks.get(0);
            if (!(info.state().getBlock() instanceof DualCannonProjectileBlock<?> projBlock)) {
                if (canFail) this.fail(currentPos, level, entity, null, (int) 4);
                return;
            }
            int remaining = projBlock.getExpectedSize() - projectileBlocks.size();
            if (remaining < 1) {
                if (canFail) this.fail(currentPos, level, entity, null, (int) 4);
                return;
            }
            for (int i = 0; i < remaining; ++i) {
                StructureBlockInfo additionalInfo = this.blocks.remove(currentPos);
                if (additionalInfo == null) {
                    if (canFail) this.fail(currentPos, level, entity, null, (int) 4);
                    return;
                }
                projectileBlocks.add(additionalInfo);

                List<StructureBlockInfo> copy = ImmutableList.copyOf(projectileBlocks);
                for (ListIterator<StructureBlockInfo> projIter = projectileBlocks.listIterator(); projIter.hasNext(); ) {
                    int j = projIter.nextIndex();
                    StructureBlockInfo projInfo = projIter.next();
                    if (projInfo.state().getBlock() instanceof DualCannonProjectileBlock<?> cproj1 && cproj1.isValidAddition(copy, projInfo, j, this.initialOrientation))
                        continue;
                    if (canFail) this.fail(currentPos, level, entity, null, (int) 4);
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
        float horizontalOffset = this.cannonMaterial.properties().barrelGap() / 2 * this.barrelInverter;
        Vec3 horizontalOffsetVec = vec.yRot((float) Math.PI / 2).scale(horizontalOffset);

        float recoilMagnitude = 0;

        int barrelLifetime = (this.cannonMaterial.properties().addedLifetime());
        float durabilityMassModifier = this.cannonMaterial.properties().durabilityMassModifier() * this.commandDurabilityMassModifier * this.equipmentDurabilityModifier;
        if (projectile != null) {
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
            projectile.setPos(spawnPos.add(horizontalOffsetVec));
            projectile.setChargePower(propelCtx.chargesUsed);
            projectile.setLifetime((int) ((projectile.getLifetime() + barrelLifetime) * this.commandLifetimeModifier * this.equipmentLifetimeModifier));
            projectile.setDurabilityModifier(durabilityMassModifier);
            projectile.setProjectileMass(durabilityMassModifier * projectile.getMaximumMass());

            projectile.shoot(vec.x, vec.y, vec.z,
                    projectile.getInitVel(), //init vel
                    (Math.max(projectile.getProjectileSpread() - spreadSub * subLength,
                            projectile.getProjectileMinimumSpread() + minimumSpread)) * this.commandSpreadModifier * this.equipmentSpreadModifier * this.additionalSpreadCoef
            ); //spread
            projectile.onShoot(this, level);

            projectile.xRotO = projectile.getXRot();
            projectile.yRotO = projectile.getYRot();

            projectile.addUntouchableEntity(entity, 1);

            level.addFreshEntity(projectile);
            recoilMagnitude += projectile.addedRecoil();
        }

        if (secondary_projectile != null && !cannonMaterial.properties().isSingleBarrel()) {
            if (secondary_projectile instanceof IntegratedPropellantProjectile integPropel && !projectileBlocks.isEmpty()) {
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
            secondary_projectile.setPos(spawnPos.subtract(horizontalOffsetVec));
            secondary_projectile.setChargePower(propelCtx.chargesUsed);
            secondary_projectile.setLifetime((int) ((secondary_projectile.getLifetime() + barrelLifetime) * this.commandLifetimeModifier * this.equipmentLifetimeModifier));
            secondary_projectile.setDurabilityModifier(durabilityMassModifier);
            secondary_projectile.setProjectileMass(durabilityMassModifier * secondary_projectile.getMaximumMass());

            secondary_projectile.shoot(vec.x, vec.y, vec.z,
                    secondary_projectile.getInitVel(), //init vel
                    (secondary_projectile.getProjectileMinimumSpread() + Math.max(secondary_projectile.getProjectileSpread()
                            - spreadSub * subLength, minimumSpread)) * this.commandSpreadModifier * this.equipmentSpreadModifier * this.additionalSpreadCoef
            ); //spread
            secondary_projectile.onShoot(this, level);

            secondary_projectile.xRotO = secondary_projectile.getXRot();
            secondary_projectile.yRotO = secondary_projectile.getYRot();

            secondary_projectile.addUntouchableEntity(entity, 1);

            level.addFreshEntity(secondary_projectile);
            recoilMagnitude += secondary_projectile.addedRecoil();
        }

        recoilMagnitude += propelCtx.recoil;
        recoilMagnitude *= CBCConfigs.SERVER.cannons.bigCannonRecoilScale.getF();
        if (controller != null) controller.onRecoil(vec.scale(-recoilMagnitude), entity);

        this.hasFired = true;
        this.barrelInverter *= -1;

        float soundPower = Mth.clamp(1 / 16f, 0, 1);
        float tone = 2 + soundPower * -8 + level.random.nextFloat() * 4f - 2f;
        //float pitch = Math.min(1.5f/((this.cannonMaterial.properties().durabilityMassModifier()-1)/1.6f+1),2);
        float pitch = 1;
        double shakeDistance = propelCtx.chargesUsed * CBCConfigs.SERVER.cannons.bigCannonBlastDistanceMultiplier.getF();
        float volume = 14;
        Vec3 plumePos = spawnPos.subtract(vec);
        propelCtx.smokeScale = Math.max(1, propelCtx.smokeScale);

        BigCannonPlumeParticleData plumeParticle = new BigCannonPlumeParticleData(0.5f, 4, 10);
        CannonBlastWaveEffectParticleData blastEffect = new CannonBlastWaveEffectParticleData(shakeDistance,
                BuiltInRegistries.SOUND_EVENT.wrapAsHolder(getSound(this.cannonMaterial.properties().durabilityMassModifier())), SoundSource.BLOCKS,
                volume, pitch, 2, 0);
        Packet<?> blastWavePacket = new ClientboundLevelParticlesPacket(blastEffect, true, plumePos.x, plumePos.y, plumePos.z, 0, 0, 0, 1, 0);

        double blastDistSqr = volume * volume * 256 * 1.21;

        for (ServerPlayer player : level.players()) {
            level.sendParticles(player, plumeParticle, true, plumePos.x, plumePos.y, plumePos.z, 0, vec.x, vec.y, vec.z, 1.0f);
            if (player.distanceToSqr(plumePos.x, plumePos.y, plumePos.z) < blastDistSqr)
                player.connection.send(blastWavePacket);
        }

        if (projectile != null && CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
            ChunkPos cpos1 = new ChunkPos(BlockPos.containing(projectile.position()));
            RitchiesProjectileLib.queueForceLoad(level, cpos1.x, cpos1.z);
        }

        if (secondary_projectile != null && CBCConfigs.SERVER.munitions.projectilesCanChunkload.get()) {
            ChunkPos cpos2 = new ChunkPos(BlockPos.containing(secondary_projectile.position()));
            RitchiesProjectileLib.queueForceLoad(level, cpos2.x, cpos2.z);
        }
    }

    private SoundEvent getSound(float durabilityMultiplier) {
        if (durabilityMultiplier < 0.95) {
            return CBCMSSoundEvents.DUAL_CANNON_1.getMainEvent();
        } else if (durabilityMultiplier < 1.5) {
            return CBCMSSoundEvents.DUAL_CANNON_2.getMainEvent();
        }
        return CBCMSSoundEvents.DUAL_CANNON_3.getMainEvent();
    }

    public boolean hasMagazine() {
        return this.hasMagazine;
    }

    public ItemStackHandler getCachedMunition() {
        return this.cachedMunition;
    }

    public ItemStack getLoadedMunition() {
        if (!(this.presentBlockEntities.get(this.startPos) instanceof IDualCannonBlockEntity cbe)) return null;
        StructureBlockInfo contained = cbe.cannonBehavior().block();
        if (contained.state().getBlock() instanceof DualCannonProjectileBlock<?> projBlock)
            return projBlock.getExtractedItem(contained);
        return ItemStack.EMPTY;
    }

    public float getAdditionalSpreadCoef() {
        return this.additionalSpreadCoef;
    }

    public void setAdditionalSpreadCoef(float additionalSpreadCoef) {
        this.additionalSpreadCoef = additionalSpreadCoef;
    }


    private void consumeBlock(BigCannonBehavior behavior, BlockPos pos) {
        this.consumeBlock(behavior, pos, BigCannonBehavior::removeBlock);
    }

    public DualCannonMaterial getCannonMaterial() {
        return this.cannonMaterial;
    }

    public float getReloadTimeModifier() {
        return this.cannonMaterial.properties().reloadTimeModifier() * this.commandReloadTimeModifier * this.equipmentReloadModifier;
    }

    public record CannonPose(Vec3 muzzleWorld, Vec3 forwardUnit, int munitionCount) {
    }

    @Nullable
    public static CannonPose getCannonPoseWorld(PitchOrientedContraptionEntity poce) {
        if (!(poce.getContraption() instanceof MountedDualCannonContraption c)) return null;

        int munitionCount = 0;
        BlockPos p = c.startPos.immutable();
        Direction dir = c.initialOrientation;
        while (c.presentBlockEntities.get(p) instanceof IDualCannonBlockEntity) {
            if (c.presentBlockEntities.get(p).getBlockState().getBlock() instanceof ProjectileBlock) {
                munitionCount++;
            }
            p = p.relative(dir);
        }
        //p = p.relative(dir);
        Vec3 muzzleWorld = poce.toGlobalVector(Vec3.atCenterOf(p), 0);
        Vec3 originWorld = poce.toGlobalVector(Vec3.atCenterOf(BlockPos.ZERO), 0);
        Vec3 forwardUnit = muzzleWorld.subtract(originWorld).normalize();

        return new CannonPose(muzzleWorld, forwardUnit, munitionCount);
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

            if (cannonInfo1 != null && be1 instanceof IDualCannonBlockEntity cbe1) {
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

    public void activateCombatCommand() {
        this.commandEffect = true;
        this.commandLeft = (int) (this.cannonMaterial.properties().combatCommandDuration() * this.commandDurationModifier);
        if (this.entity != null && this.entity.level() instanceof ServerLevel slevel) {
            CBCMSNetworkImpl.sendToClientTracking(
                    new ClientboundCannonCmdSyncPacket(this.entity.getId(), this.commandEffect, this.commandCooldown, this.commandLeft),
                    this.entity
            );
        }
    }

    public void deactivateCombatCommand() {
        this.commandDurabilityMassModifier = 1;
        this.commandReloadTimeModifier = 1;
        this.commandLifetimeModifier = 1;
        this.commandSpreadModifier = 1;
        this.commandEffect = false;
    }

    public int getCommandCooldown() {
        return this.commandCooldown;
    }

    public int getCommandLeft() {
        return this.commandLeft;
    }

    public void reduceCooldown(float reductionRate) {
        int reduction = (int) (this.cannonMaterial.properties().reloadTimeModifier() * 50 * reductionRate);
        if (this.reduceCooldownOnHit && this.commandCooldown > 0) {
            this.commandCooldown -= reduction;
        }
    }

    public boolean getCommandActivation() {
        return this.commandEffect;
    }

    public void setCommandDurabilityMassModifier(float durability) {
        this.commandDurabilityMassModifier = durability;
    }

    public void setCommandReloadTimeModifier(float reloadTimeModifier) {
        this.commandReloadTimeModifier = reloadTimeModifier;
    }

    public void setCommandLifetimeModifier(float lifetimeModifier) {
        this.commandLifetimeModifier = lifetimeModifier;
    }

    public void setCommandSpreadModifier(float spreadModifier) {
        this.commandSpreadModifier = spreadModifier;
    }

    public void setCommandModifiers(float durability, float reloadTimeModifier, float lifetimeModifier, float spreadModifier) {
        this.commandDurabilityMassModifier = durability;
        this.commandReloadTimeModifier = reloadTimeModifier;
        this.commandLifetimeModifier = lifetimeModifier;
        this.commandSpreadModifier = spreadModifier;
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
        if (this.cannonMaterial.properties().failureMode() == DualCannonMaterialProperties.FailureMode.RUPTURE) {
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
        return CBCMSCannonContraptionTypes.DUAL_CANNON;
    }

    @Override
    public CompoundTag writeNBT(boolean clientData) {
        CompoundTag tag = super.writeNBT(clientData);
        tag.putString("CannonMaterial", this.cannonMaterial == null ? CBCMSDualCannonMaterials.CAST_IRON.name().toString() : this.cannonMaterial.name().toString());
        if (this.hasWeldedPenalty) tag.putBoolean("WeldedCannon", true);
        //if (this.mortarDelay > 0) tag.putInt("MortarDelay", this.mortarDelay);
        //if (this.cachedMortarRound != null && !this.cachedMortarRound.isEmpty()) tag.put("CachedMortarRound", this.cachedMortarRound.save(new CompoundTag()));
        if (this.hasFired) tag.putBoolean("HasFired", true);
        if (this.commandEffect) tag.putBoolean("CommandEffect", true);
        tag.putInt("CommandCooldown", this.commandCooldown);
        tag.putInt("CommandLeft", this.commandLeft);
        tag.putFloat("equipmentDurabilityModifier", this.equipmentDurabilityModifier);
        tag.putFloat("equipmentReloadModifier", this.equipmentReloadModifier);
        tag.putFloat("equipmentSpreadModifier", this.equipmentSpreadModifier);
        tag.putFloat("equipmentLifetimeModifier", this.equipmentLifetimeModifier);
        tag.putFloat("commandCooldownModifier", this.commandCooldownModifier);
        tag.putFloat("commandDurationModifier", this.commandDurationModifier);
        tag.putBoolean("reduceCooldownOnHit", this.reduceCooldownOnHit);
        tag.putBoolean("hasMagazine", this.hasMagazine);
        if (this.hasMagazine){
            tag.put("magazine", cachedMunition.serializeNBT());
        }
        return tag;
    }

    @Override
    public void readNBT(Level level, CompoundTag tag, boolean clientData) {
        super.readNBT(level, tag, clientData);
        this.cannonMaterial = DualCannonMaterial.fromNameOrNull(CBCUtils.location(tag.getString("CannonMaterial")));
        this.hasWeldedPenalty = tag.contains("WeldedCannon");
        if (this.cannonMaterial == null) this.cannonMaterial = CBCMSDualCannonMaterials.CAST_IRON;
        //this.mortarDelay = Math.max(0, tag.getInt("MortarDelay"));
        //this.cachedMortarRound = tag.contains("CachedMortarRound", Tag.TAG_COMPOUND) ? ItemStack.of(tag.getCompound("CachedMortarRound")) : ItemStack.EMPTY;
        this.hasFired = tag.contains("HasFired");
        this.commandEffect = tag.contains("CommandEffect");
        this.commandCooldown = tag.getInt("CommandCooldown");
        this.commandLeft = tag.getInt("CommandLeft");
        this.equipmentDurabilityModifier = tag.getFloat("equipmentDurabilityModifier");
        this.equipmentReloadModifier = tag.getFloat("equipmentReloadModifier");
        this.equipmentSpreadModifier = tag.getFloat("equipmentSpreadModifier");
        this.equipmentLifetimeModifier = tag.getFloat("equipmentLifetimeModifier");
        this.commandCooldownModifier = tag.getFloat("commandCooldownModifier");
        this.commandDurationModifier = tag.getFloat("commandDurationModifier");
        this.reduceCooldownOnHit = tag.getBoolean("reduceCooldownOnHit");
        this.hasMagazine = tag.getBoolean("hasMagazine");
        if(this.hasMagazine){
            this.cachedMunition.deserializeNBT(tag.getCompound("magazine"));
        }
    }

    @Override
    public ContraptionType getType() {
        return CBCMSContraptionTypes.DUAL_CANNON;
    }

    public boolean isDropMortar() {
        StructureBlockInfo breech = this.blocks.get(this.startPos.relative(this.initialOrientation.getOpposite()));
        return breech != null && breech.state().getBlock() instanceof DualCannonBlock cblock && cblock.getCannonShape() == CannonCastShape.DROP_MORTAR_END;
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
                if (!(block instanceof BigCannonPropellantBlock cpropel) || !(cpropel.isValidAddition(info, index, orientation)))
                    return false;
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
