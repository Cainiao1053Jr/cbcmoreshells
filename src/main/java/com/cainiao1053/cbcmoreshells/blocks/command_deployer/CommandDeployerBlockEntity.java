package com.cainiao1053.cbcmoreshells.blocks.command_deployer;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command.CombatCommandBaseItem;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import it.unimi.dsi.fastutil.ints.IntList;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import java.util.ArrayList;
import java.util.List;


public class CommandDeployerBlockEntity extends SmartBlockEntity {

    public CommandDeployerBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    public final SmartInventory inventory = new SmartInventory(1, this);
    private List<BlockPos> cachedPositions = new ArrayList<>();

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }

    @Override
    public void tick() {
        super.tick();
    }

    public void activateCannons() {
        if (level.isClientSide()) return;

        ItemStack stack = inventory.getStackInSlot(0);
        if (stack.isEmpty() || !(stack.getItem() instanceof CombatCommandBaseItem ccb)) return;

        // Read cached positions from item's custom data
        CustomData cd = stack.get(DataComponents.CUSTOM_DATA);
        if (cd != null) {
            CompoundTag tag = cd.copyTag();
            if (tag.contains("Positions", Tag.TAG_LIST)) {
                ListTag positionsTag = tag.getList("Positions", Tag.TAG_INT_ARRAY);
                if (!positionsTag.isEmpty()) {
                    cachedPositions = new ArrayList<>();
//                    for (int i = 0; i < positionsTag.size(); i++) {
//                        cachedPositions.add(NbtUtils.readBlockPos(positionsTag.get(i)));
//                    }
                    for (int i = 0; i < positionsTag.size(); i++) {
                        BlockPos.CODEC.parse(NbtOps.INSTANCE, positionsTag.get(i))
                                .result()
                                .ifPresent(cachedPositions::add);
                    }
                    setChanged();
                }
            }
        }
        if (cachedPositions.isEmpty()) return;

        Vec3 selfPos = Vec3.atCenterOf(this.worldPosition);
        int activatedCannonCount = 0;
        int maxUse = ccb.getMaximumUseAtOnce();
        List<MountedDualCannonContraption> availableDualCannon = new ArrayList<>();

        for (BlockPos mountPos : cachedPositions) {
            BlockEntity be = level.getBlockEntity(mountPos);
            if (!(be instanceof CannonMountBlockEntity mount)) continue;
            PitchOrientedContraptionEntity poce = mount.getContraption();
            if (poce == null) continue;
            if (!(poce.getContraption() instanceof MountedDualCannonContraption dualCannon)) continue;
            if (poce.distanceToSqr(selfPos) > 256.0 * 256.0) continue;
            if (dualCannon.getCommandActivation()) {
                activatedCannonCount++;
                continue;
            }
            if (dualCannon.getCommandCooldown() > 0) continue;
            availableDualCannon.add(dualCannon);
        }

        int activateCannon = 0;
        int availableCannon = maxUse - activatedCannonCount;
        for (MountedDualCannonContraption dualCannon : availableDualCannon) {
            activateCannon++;
            if (activateCannon > availableCannon) {
                activateCannon--;
                break;
            }
            dualCannon.activateCombatCommand();
            dualCannon.setCommandModifiers(ccb.getCommandDurabilityModifier(), ccb.getCommandReloadTimeModifier(),
                    ccb.getCommandLifetimeModifier(), ccb.getCommandSpreadModifier());
            //boolean broke = stack.hurt(1, ((ServerLevel) level).random, null);
            stack.hurtAndBreak(1, (ServerLevel) level, null, brokenItem -> {});
//            if (broke) {
//                stack.shrink(1);
//                break;
//            }
        }

        if (activateCannon > 0) {
            setChanged();
            ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET);
            FireworkExplosion boom = new FireworkExplosion(
                    FireworkExplosion.Shape.LARGE_BALL,
                    IntList.of(0xFBBA1E),
                    IntList.of(0xEC8E0D),
                    true,
                    true
            );
            rocketStack.set(DataComponents.FIREWORKS, new Fireworks(1, List.of(boom)));

            FireworkRocketEntity fw = new FireworkRocketEntity(level, selfPos.x, selfPos.y + 1, selfPos.z, rocketStack);
            fw.setDeltaMovement(0, 0.55, 0);
            level.addFreshEntity(fw);

            int finalActivateCannon = activateCannon;
            for (Player player : level.players()) {
                if (player.distanceToSqr(selfPos) < 32.0 * 32.0) {
                    player.sendSystemMessage(Component.translatable(
                            "item.cbcmoreshells.combat_command_base.on_effect", finalActivateCannon));
                }
            }
        }
    }

    public ItemStack insertStack(ItemStack stack, int slot, boolean simulate) {
        stack = inventory.insertItem(slot, stack, simulate);
        if (stack.getCount() < stack.getMaxStackSize())
            setChangedAndSync();
        return stack;
    }

    private void setChangedAndSync() {
        this.setChanged();
        if (level instanceof ServerLevel sl)
            sl.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), 3);
    }

    public ItemStack extractStack(int slot) {
        return inventory.extractItem(slot, 1, false);
    }

    public static List<MountedDualCannonContraption> findCannons(Level level, AABB box) {
        return level.getEntitiesOfClass(OrientedContraptionEntity.class, box,
                        e -> e.getContraption() instanceof MountedDualCannonContraption)
                .stream()
                .map(OrientedContraptionEntity::getContraption)
                .filter(c -> c instanceof MountedDualCannonContraption)
                .map(c -> (MountedDualCannonContraption) c)
                .distinct()
                .toList();
    }

    public static List<PitchOrientedContraptionEntity> findPitchOrientedEntities(Level level, AABB box) {
        return level.getEntitiesOfClass(
                PitchOrientedContraptionEntity.class,
                box,
                e -> e.getContraption() instanceof MountedDualCannonContraption
        );
    }

//    public static AABB toAABB(AABBdc i) {
//        return new AABB(i.minX(), i.minY(), i.minZ(), i.maxX(), i.maxY(), i.maxZ());
//    }

    public SmartInventory getInventory() {
        return inventory;
    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.write(compound, registries, clientPacket);
        compound.put("Inventory", inventory.serializeNBT(registries));
        ListTag posList = new ListTag();
        for (BlockPos pos : cachedPositions) {
            posList.add(NbtUtils.writeBlockPos(pos));
        }
        compound.put("CachedPositions", posList);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        super.read(compound, registries, clientPacket);
        inventory.deserializeNBT(registries, compound.getCompound("Inventory"));
        cachedPositions = new ArrayList<>();
        if (compound.contains("CachedPositions", Tag.TAG_LIST)) {
            ListTag posList = compound.getList("CachedPositions", Tag.TAG_INT_ARRAY);
//            for (int i = 0; i < posList.size(); i++) {
//                cachedPositions.add(NbtUtils.readBlockPos(posList.get(i)));
//            }
            for (int i = 0; i < posList.size(); i++) {
                BlockPos.CODEC.parse(NbtOps.INSTANCE, posList.get(i))
                        .result()
                        .ifPresent(cachedPositions::add);
            }
        }
    }

    public Vector3d getPositionShip() {
        Vec3 front = getBlockPos().getCenter();
        return new Vector3d(front.x, front.y, front.z);
    }

}
