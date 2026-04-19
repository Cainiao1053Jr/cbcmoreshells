package com.cainiao1053.cbcmoreshells.blocks.command_deployer;


//import com.cainiao1053.cbcmoreshells.api.vs.ValkyrienSkies;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command.CombatCommandBaseItem;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.OrientedContraptionEntity;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.item.SmartInventory;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3d;
import org.joml.primitives.AABBdc;
import org.valkyrienskies.core.api.ships.Ship;
import rbasamoyai.createbigcannons.cannon_control.cannon_mount.CannonMountBlockEntity;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;


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


    public void activateCannons(){
        if(level.isClientSide()) {
            return;
        }
        ItemStack stack = inventory.getItem(0);
        if(stack.isEmpty() || !(stack.getItem() instanceof CombatCommandBaseItem ccb)) {
            return;
        }
        CompoundTag tag = stack.getOrCreateTag();
        if (tag.contains("Positions", Tag.TAG_LIST)) {
            ListTag positionsTag = tag.getList("Positions", Tag.TAG_COMPOUND);
            if (!positionsTag.isEmpty()) {
                cachedPositions = new ArrayList<>();
                for (int i = 0; i < positionsTag.size(); i++) {
                    cachedPositions.add(NbtUtils.readBlockPos(positionsTag.getCompound(i)));
                }
                setChanged();
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
            if(dualCannon.getCommandCooldown() > 0){
                continue;
            }
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
            dualCannon.setCommandModifiers(ccb.getCommandDurabilityModifier(), ccb.getCommandReloadTimeModifier(), ccb.getCommandLifetimeModifier(), ccb.getCommandSpreadModifier());
            boolean broke = stack.hurt(1, ((ServerLevel) level).random, null);
            if (broke) {
                stack.shrink(1);
                break;
            }
        }

        if (activateCannon > 0) {
            setChanged();
            ItemStack rocketStack = new ItemStack(Items.FIREWORK_ROCKET);
            CompoundTag fireworks = new CompoundTag();
            fireworks.putByte("Flight", (byte) 1);
            ListTag explosions = new ListTag();
            CompoundTag boom = new CompoundTag();
            boom.putByte("Type", (byte) 1);
            boom.putIntArray("Colors", new int[]{0xFBBA1E});
            boom.putIntArray("FadeColors", new int[]{0xEC8E0D});
            boom.putBoolean("Trail", true);
            boom.putBoolean("Flicker", true);
            explosions.add(boom);
            fireworks.put("Explosions", explosions);
            rocketStack.getOrCreateTag().put("Fireworks", fireworks);
            FireworkRocketEntity fw = new FireworkRocketEntity(level, selfPos.x, selfPos.y + 1, selfPos.z, rocketStack);
            fw.setDeltaMovement(0, 0.55, 0);
            level.addFreshEntity(fw);
            int finalActivateCannon = activateCannon;
            for (Player player : level.players()) {
                if (player.distanceToSqr(selfPos) < 32.0 * 32.0) {
                    player.sendSystemMessage(Component.translatable("item.cbcmoreshells.combat_command_base.on_effect", finalActivateCannon));
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
        ItemStack stack = ItemStack.EMPTY;
        stack = inventory.extractItem(slot, 1, false);
        return stack;
    }

    public static List<MountedDualCannonContraption> findCannons(Level level, AABB box) {
        //AABB box = AABB.ofSize(center, 2 * radius, 2 * radius, 2 * radius);
        //if (level.isClientSide()) return List.of();

        Predicate<OrientedContraptionEntity> isMountedCannon = e -> {
            Contraption c = e.getContraption();
            return c instanceof MountedDualCannonContraption;
        };

        List<OrientedContraptionEntity> carriers =
                level.getEntitiesOfClass(OrientedContraptionEntity.class, box, isMountedCannon);

        return carriers.stream()
                .map(OrientedContraptionEntity::getContraption)
                .filter(c -> c instanceof MountedDualCannonContraption)
                .map(c -> (MountedDualCannonContraption) c)
                .distinct()
                .toList();
    }

    public static List<PitchOrientedContraptionEntity> findPitchOrientedEntities(Level level, AABB box) {
        //if (level.isClientSide()) return List.of();

        return level.getEntitiesOfClass(
                PitchOrientedContraptionEntity.class,
                box,
                e -> e.getContraption() instanceof MountedDualCannonContraption
        );
    }

    public static AABB toAABB(AABBdc i) {
        return new AABB(
                i.minX(), i.minY(), i.minZ(),
                i.maxX(), i.maxY(), i.maxZ()
        );
    }

    public SmartInventory getInventory() {
        return inventory;
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        super.write(compound, clientPacket);
        compound.put("Inventory", inventory.serializeNBT());
        ListTag posList = new ListTag();
        for (BlockPos pos : cachedPositions) {
            posList.add(NbtUtils.writeBlockPos(pos));
        }
        compound.put("CachedPositions", posList);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        super.read(compound, clientPacket);
        inventory.deserializeNBT(compound.getCompound("Inventory"));
        cachedPositions = new ArrayList<>();
        if (compound.contains("CachedPositions", Tag.TAG_LIST)) {
            ListTag posList = compound.getList("CachedPositions", Tag.TAG_COMPOUND);
            for (int i = 0; i < posList.size(); i++) {
                cachedPositions.add(NbtUtils.readBlockPos(posList.getCompound(i)));
            }
        }
    }

    public Vector3d getPositionShip() {
        Vec3 front = getBlockPos().getCenter();
        return new Vector3d(front.x, front.y, front.z);
    }

//    public @Nullable Ship getShipOn() {
//        return ValkyrienSkies.getShipManagingBlock(level, getBlockPos());
//    }

}
