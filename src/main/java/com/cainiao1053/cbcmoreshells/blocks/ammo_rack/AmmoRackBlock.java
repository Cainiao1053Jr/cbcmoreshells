package com.cainiao1053.cbcmoreshells.blocks.ammo_rack;

import java.util.function.Consumer;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmItem;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.render.ReducedDestroyEffects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientBlockExtensions;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.munitions.AbstractCannonProjectile;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;

public class AmmoRackBlock extends Block implements IWrenchable, IBE<AmmoRackBlockEntity> {

    public AmmoRackBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(defaultBlockState().setValue(POWERED, true));
    }

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    @OnlyIn(Dist.CLIENT)
    public void initializeClient(Consumer<IClientBlockExtensions> consumer) {
        consumer.accept(new ReducedDestroyEffects());
    }

    public static Direction getFacing(BlockState state) {
        return state.getValue(FACING);
    }

    @Override
    public void setPlacedBy(Level pLevel, BlockPos pPos, BlockState pState, LivingEntity pPlacer, ItemStack pStack) {
        super.setPlacedBy(pLevel, pPos, pState, pPlacer, pStack);
        AdvancementBehaviour.setPlacedBy(pLevel, pPos, pPlacer);
    }

    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        withBlockEntityDo(world, pos, AmmoRackBlockEntity::onAdded);
        updateDiagonalNeighbour(state, world, pos);
    }

    protected void updateDiagonalNeighbour(BlockState state, Level world, BlockPos pos) {
        //if (!isChute(state))
        //return;
    }


    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        return super.getStateForPlacement(p_196258_1_).setValue(POWERED, p_196258_1_.getLevel()
                .hasNeighborSignal(p_196258_1_.getClickedPos())).setValue(FACING, p_196258_1_.getHorizontalDirection().getOpposite());
    }

    @Override
    public BlockEntityType<? extends AmmoRackBlockEntity> getBlockEntityType() {
        return CBCMSBlockEntities.AMMO_RACK.get();
    }

    @Override
    public void onProjectileHit(Level level, BlockState p_60454_, BlockHitResult result, Projectile projectile) {
        super.onProjectileHit(level, p_60454_, result, projectile);
        if (!canExplode()) {
            return;
        }
        if (level.isClientSide()) {
            return;
        }
        if (projectile instanceof AbstractCannonProjectile) {
            BlockPos position = result.getBlockPos();
            ShellExplosion explosion = new ShellExplosion(level, null, null, position.getX(),
                    position.getY(), position.getZ(), 5, false,
                    CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
            CreateBigCannons.handleCustomExplosion(level, explosion);
        }
    }

    @Override
    public void onBlockExploded(BlockState state, Level level, BlockPos pos, Explosion explosion) {
        super.onBlockExploded(state, level, pos, explosion);
        if (!canExplode()) {
            return;
        }
        if (level.isClientSide()) {
            return;
        }
        ShellExplosion rackExplosion = new ShellExplosion(level, null, null, pos.getX(),
                pos.getY(), pos.getZ(), 5, false,
                CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
        CreateBigCannons.handleCustomExplosion(level, rackExplosion);
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean isMoving) {
        IBE.onRemove(state, world, pos, newState);
        if (state.is(newState.getBlock()))
            return;
        updateDiagonalNeighbour(state, world, pos);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource pRandom) {
        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != level.hasNeighborSignal(pos)) {
            level.setBlock(pos, state.cycle(POWERED), 2);
            if (!previouslyPowered) {
                AmmoRackBlockEntity be = (AmmoRackBlockEntity) level.getBlockEntity(pos);
                if (be != null) {
                    be.switchFilter();
                }
            }
        }
//		if(!previouslyPowered && level.hasNeighborSignal(pos)){
//			AmmoRackBlockEntity be = (AmmoRackBlockEntity) level.getBlockEntity(pos);
//			if(be != null){
//				be.switchFilter();
//			}
//		}
    }

    @Override
    public void neighborChanged(BlockState p_220069_1_, Level world, BlockPos pos, Block p_220069_4_,
                                BlockPos neighbourPos, boolean p_220069_6_) {
        if (world.isClientSide)
            return;
        if (!world.getBlockTicks()
                .willTickThisTick(pos, this))
            world.scheduleTick(pos, this, 0);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
                                        CollisionContext p_220071_4_) {
        return Block.box(0, 0, 0, 16, 16, 16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED).add(FACING));
    }

    @Override
    public Class<AmmoRackBlockEntity> getBlockEntityClass() {
        return AmmoRackBlockEntity.class;
    }

    public boolean canExplode() {
        return false;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult result) {
        if (level.isClientSide) {
            return InteractionResult.SUCCESS;
        }
//		LOGGER.info("InteractPos"+rotHitVec);
//		LOGGER.info("HitXIndex: "+ Math.floor((rotHitVec.x)/0.334+1));
//		LOGGER.info("HitYIndex: "+ Math.floor(Math.max(rotHitVec.y-0.375,0)/0.3125+1));
//		LOGGER.info("hitSlot "+hitSlot);
        ItemStack held = player.getItemInHand(hand);
        if (held.getItem() instanceof ArmItem) {
            return InteractionResult.PASS;
        }
        if (result.getDirection() != state.getValue(FACING)) {
            return InteractionResult.PASS;
        }

        Vec3 hitVec = result.getLocation();
        Vec3 relativeHitVec = new Vec3(hitVec.x - pos.getX(), hitVec.y - pos.getY(), hitVec.z - pos.getZ());
        Vec3 rotHitVec = rotateHitVec(relativeHitVec, state.getValue(FACING));
        Vec3i slotIndex = new Vec3i((int) Math.floor((rotHitVec.x) / 0.334 + 1), (int) Math.floor(Math.max(rotHitVec.y - 0.375, 0) / 0.3125 + 1), 0);
        int hitSlot = slotIndex.getX() + 3 * (slotIndex.getY() - 1) - 1;

        AmmoRackBlockEntity be = (AmmoRackBlockEntity) level.getBlockEntity(pos);
        if (held.isEmpty()) {
            player.setItemInHand(hand, be.extractStack(hitSlot));
            return InteractionResult.CONSUME;
        }

        if (be != null) {
            ItemStack remain = be.insertStack(held.copy(), hitSlot);
            if (remain.getCount() != held.getCount()) {
                player.setItemInHand(hand, remain);
//					level.playSound(null, pos, SoundEvents.ITEM_PICKUP,
//							SoundSource.BLOCKS, 0.25f, 1.0f);
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    public static Vec3 rotateHitVec(Vec3 local, Direction facing) {
        Vec3 hitVec = new Vec3(local.x, local.y, 0);
        switch (facing) {
            case NORTH:
                hitVec = new Vec3(1 - local.x, local.y, 0);
                break;
            case EAST:
                hitVec = new Vec3(1 - local.z, local.y, 0);
                break;
            case SOUTH:
                break;
            case WEST:
                hitVec = new Vec3(local.z, local.y, 0);
                break;
            default:
                break;
        }
        return hitVec;
    }

}
