//package com.cainiao1053.cbcmoreshells.blocks.landing_indicator;
//
//import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
//import com.simibubi.create.foundation.block.IBE;
//import net.minecraft.core.BlockPos;
//import net.minecraft.server.level.ServerLevel;
//import net.minecraft.util.RandomSource;
//import net.minecraft.world.InteractionHand;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.context.BlockPlaceContext;
//import net.minecraft.world.level.BlockGetter;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.DirectionalBlock;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.block.state.StateDefinition;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//import net.minecraft.world.level.block.state.properties.BooleanProperty;
//import net.minecraft.world.phys.BlockHitResult;
//import net.minecraft.world.phys.shapes.CollisionContext;
//import net.minecraft.world.phys.shapes.VoxelShape;
//
//public class LandingIndicatorBlock extends DirectionalBlock implements IBE<LandingIndicatorBlockEntity> {
//
//    public LandingIndicatorBlock(Properties properties) {
//        super(properties);
//        //registerDefaultState(defaultBlockState());
//        registerDefaultState(defaultBlockState().setValue(POWERED, true));
//    }
//
//    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
//
//    @Override
//    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
//        builder.add(POWERED);
//        builder.add(FACING);
//        super.createBlockStateDefinition(builder);
//    }
//
//    @Override
//    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_,
//                               CollisionContext p_220053_4_) {
//        return Block.box(0, 0, 0, 16, 16, 16);
//    }
//
//    @Override
//    public VoxelShape getCollisionShape(BlockState p_220071_1_, BlockGetter p_220071_2_, BlockPos p_220071_3_,
//                                        CollisionContext p_220071_4_) {
//        return Block.box(0, 0, 0, 16, 16, 16);
//    }
//
//    @Override
//    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource pRandom) {
//        boolean previouslyPowered = state.getValue(POWERED);
//        if (previouslyPowered != level.hasNeighborSignal(pos))
//            level.setBlock(pos, state.cycle(POWERED), 2);
//    }
//
//    @Override
//    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block p_220069_4_,
//                                BlockPos neighbourPos, boolean p_220069_6_) {
//        if (level.isClientSide)
//            return;
//
//        boolean previouslyPowered = state.getValue(POWERED);
//        if (previouslyPowered != level.hasNeighborSignal(pos)) {
//            level.setBlock(pos, state.cycle(POWERED), 2);
//        }
//    }
//
//    @Override
//    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult result) {
//        if(state.getValue(POWERED)) {
//            return InteractionResult.PASS;
//        }
//        if (level.isClientSide) {
//            return InteractionResult.SUCCESS;
//        }
//        ItemStack held = player.getItemInHand(hand);
//        LandingIndicatorBlockEntity be = (LandingIndicatorBlockEntity) level.getBlockEntity(pos);
//        if(held == ItemStack.EMPTY){
//            be.switchColor();
//            return InteractionResult.SUCCESS;
//        }
//        return InteractionResult.PASS;
//    }
//
//    public BlockState getStateForPlacement(BlockPlaceContext p_196258_1_) {
//        return super.getStateForPlacement(p_196258_1_).setValue(POWERED, p_196258_1_.getLevel()
//                .hasNeighborSignal(p_196258_1_.getClickedPos())).setValue(FACING, p_196258_1_.getNearestLookingDirection().getOpposite());
//    }
//
//    @Override
//    public Class<LandingIndicatorBlockEntity> getBlockEntityClass() {
//        return LandingIndicatorBlockEntity.class;
//    }
//
//    @Override
//    public BlockEntityType<? extends LandingIndicatorBlockEntity> getBlockEntityType() {
//        return CBCMSBlockEntities.LANDING_INDICATOR.get();
//    }
//
//}