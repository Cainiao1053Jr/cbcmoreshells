package com.cainiao1053.cbcmoreshells.blocks.torpedo_detection_device;

import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Axis;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

public class TorpedoDetectionDeviceBlock extends DirectionalAxisKineticBlock implements IBE<TorpedoDetectionDeviceBlockEntity> {

    public TorpedoDetectionDeviceBlock(Properties properties) {
        super(properties);
        //registerDefaultState(defaultBlockState());
        registerDefaultState(defaultBlockState().setValue(POWERED, true));
    }

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(POWERED));
    }

    @Override
    public Axis getRotationAxis(BlockState state) {
		//return state.getValue(FACING).getOpposite().getAxis();
        return getShaftFacing(state.getValue(FACING)).getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
                               CollisionContext p_220053_4_) {
        return Block.box(0, 0, 0, 16, 12, 16);
    }

    @Override
    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
                                        CollisionContext p_220071_4_) {
        return Block.box(0, 0, 0, 16, 12, 16);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource pRandom) {
        boolean previouslyPowered = state.getValue(POWERED);
        if (previouslyPowered != level.hasNeighborSignal(pos))
            level.setBlock(pos, state.cycle(POWERED), 2);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block p_220069_4_,
                                BlockPos neighbourPos, boolean p_220069_6_) {
        if (level.isClientSide)
            return;
//        if (!world.getBlockTicks()
//                .willTickThisTick(pos, this))
//            world.scheduleTick(pos, this, 0);
        if(state.getValue(POWERED)) {
            return;
        }
        //boolean previouslyPowered = state.getValue(POWERED);
        if (level.hasNeighborSignal(pos)) {
            //level.setBlock(pos, state.cycle(POWERED), 2 | 16);
            //detachKinetics(worldIn, pos, previouslyPowered);
            TorpedoDetectionDeviceBlockEntity be = getBlockEntity(level,pos);
            if(be.getRestartCooldown()>0){
                return;
            }
            be.setMaxActivation(be.getMaxActivationLeft());
            level.setBlock(pos, state.setValue(POWERED, true), Block.UPDATE_ALL);

            ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
            if(ship != null){
                Vector3dc shipPos = ship.getTransform().getPositionInWorld();
                level.playSound(
                        null,
                        shipPos.x(),
                        shipPos.y(),
                        shipPos.z(),
                        SoundEvents.NOTE_BLOCK_BIT.value(),
                        SoundSource.BLOCKS,
                        3.5f,
                        1.4f
                );
            }
        }
    }

    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
        return super.getStateForPlacement(p_196258_1_).setValue(POWERED, p_196258_1_.getLevel()
                .hasNeighborSignal(p_196258_1_.getClickedPos()));
    }

    @Override
	public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
		//return face == state.getValue(FACING).getOpposite();
        return face == getShaftFacing(state.getValue(FACING));
	}

    @Override
    public Class<TorpedoDetectionDeviceBlockEntity> getBlockEntityClass() {
        return TorpedoDetectionDeviceBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends TorpedoDetectionDeviceBlockEntity> getBlockEntityType() {
        return CBCMSBlockEntities.TORPEDO_DETECTION_DEVICE.get();
    }

    protected Direction getShaftFacing(Direction face) {
        if(face == Direction.DOWN){
            return Direction.SOUTH;
        }else if(face == Direction.UP){
            return Direction.EAST;
        }
        return Direction.DOWN;
    }

}