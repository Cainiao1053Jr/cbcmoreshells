package com.cainiao1053.cbcmoreshells.blocks.dish_plate;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmItem;
import com.simibubi.create.foundation.advancement.AdvancementBehaviour;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.block.render.ReducedDestroyEffects;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
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
import org.slf4j.Logger;

import java.util.function.Consumer;

public class DishPlateBlock extends Block implements IWrenchable, IBE<DishPlateBlockEntity> {

    public DishPlateBlock(Properties p_i48440_1_) {
        super(p_i48440_1_);
        registerDefaultState(defaultBlockState().setValue(POWERED, true));
    }

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    Logger LOGGER = Cbcmoreshells.LOGGER;

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
        withBlockEntityDo(world, pos, DishPlateBlockEntity::onAdded);
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
    public BlockEntityType<? extends DishPlateBlockEntity> getBlockEntityType() {
        return CBCMSBlockEntities.DISH_PLATE.get();
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
//        if (previouslyPowered != level.hasNeighborSignal(pos)) {
//            level.setBlock(pos, state.cycle(POWERED), 2);
//            if (!previouslyPowered) {
//                DishPlateBlockEntity be = (DishPlateBlockEntity) level.getBlockEntity(pos);
//                if (be != null) {
//                    //be.switchFilter();
//                }
//            }
//        }
//		if(!previouslyPowered && level.hasNeighborSignal(pos)){
//			DishPlateBlockEntity be = (DishPlateBlockEntity) level.getBlockEntity(pos);
//			if(be != null){
//				be.switchFilter();
//			}
//		}
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block p_220069_4_,
                                BlockPos neighbourPos, boolean p_220069_6_) {
        if (level.isClientSide)
            return;
        if(state.getValue(POWERED)) {
            return;
        }
        if(level.hasNeighborSignal(pos)){
            level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_ALL);
        }
    }

    public void unpower(Level level, DishPlateBlockEntity be) {
        level.setBlock(be.getBlockPos(), be.getBlockState().setValue(POWERED, false), Block.UPDATE_ALL);
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return Block.box(0, 0, 0, 16, 1, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
                                        CollisionContext p_220071_4_) {
        return Block.box(0, 0, 0, 16, 1, 16);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED).add(FACING));
    }

    @Override
    public Class<DishPlateBlockEntity> getBlockEntityClass() {
        return DishPlateBlockEntity.class;
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
        if (result.getDirection() != Direction.UP) {
            return InteractionResult.PASS;
        }

        Vec3 hitVec = result.getLocation();
        Vec3 relativeHitVec = new Vec3(hitVec.x - pos.getX(), hitVec.y - pos.getY(), hitVec.z - pos.getZ());
        Vec3 rotHitVec = rotateHitVec(relativeHitVec, state.getValue(FACING));
        int hitSlot = 0;

        DishPlateBlockEntity be = (DishPlateBlockEntity) level.getBlockEntity(pos);

        if(rotHitVec.x>0.8125 && rotHitVec.z < 0.1875){
            if(!state.getValue(POWERED)){
                level.setBlock(be.getBlockPos(), be.getBlockState().setValue(POWERED, true), Block.UPDATE_ALL);
                level.playSound(
                        null,
                        pos.getX(),
                        pos.getY(),
                        pos.getZ(),
                        SoundEvents.NOTE_BLOCK_BELL.get(),
                        SoundSource.BLOCKS,
                        2.0f,
                        1f
                );
            }
        }

        if(rotHitVec.x>0.2 && rotHitVec.x<0.8 && rotHitVec.z>0.4 && rotHitVec.z<0.9) {
            if (held.isEmpty()) {
                player.setItemInHand(hand, be.extractStack(hitSlot));
                return InteractionResult.CONSUME;
            }

            if (be != null) {
                ItemStack remain = be.insertStack(held.copy(), hitSlot, false);
                if (remain.getCount() != held.getCount()) {
                    player.setItemInHand(hand, remain);
//					level.playSound(null, pos, SoundEvents.ITEM_PICKUP,
//							SoundSource.BLOCKS, 0.25f, 1.0f);
                    return InteractionResult.CONSUME;
                }
            }
        }
        return InteractionResult.PASS;
    }

    public static Vec3 rotateHitVec(Vec3 local, Direction facing) {
        Vec3 hitVec = new Vec3(local.x,0, local.z);
        switch (facing) {
            case NORTH:
                hitVec = new Vec3(1 - local.x, 0, 1-local.z);
                break;
            case EAST:
                hitVec = new Vec3(1 - local.z, 0, local.x);
                break;
            case SOUTH:
                break;
            case WEST:
                hitVec = new Vec3(local.z, 0, 1-local.x);
                break;
            default:
                break;
        }
        return hitVec;
    }

}
