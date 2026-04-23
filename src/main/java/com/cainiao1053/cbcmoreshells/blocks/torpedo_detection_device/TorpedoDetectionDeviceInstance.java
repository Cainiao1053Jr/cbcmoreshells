//package com.cainiao1053.cbcmoreshells.blocks.torpedo_detection_device;
//
//import com.jozufozu.flywheel.api.Instancer;
//import com.jozufozu.flywheel.api.MaterialManager;
//import com.simibubi.create.AllPartialModels;
//import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
//import com.simibubi.create.content.kinetics.base.SingleRotatingInstance;
//import com.simibubi.create.content.kinetics.base.flwdata.RotatingData;
//
//import net.minecraft.core.Direction;
//import net.minecraft.world.level.block.state.properties.BlockStateProperties;
//
//public class TorpedoDetectionDeviceInstance<T extends KineticBlockEntity> extends SingleRotatingInstance<T> {
//    public TorpedoDetectionDeviceInstance(MaterialManager materialManager, T blockEntity) {
//        super(materialManager, blockEntity);
//    }
//
//    @Override
//    protected Instancer<RotatingData> getModel() {
//		Direction dir = getShaftDirection();
//		return getRotatingMaterial().getModel(AllPartialModels.SHAFT_HALF, blockState, dir);
//	}
//
//    protected Direction getShaftDirection() {
//        //return blockState.getValue(BlockStateProperties.FACING).getOpposite();
//        return getShaftFacing(blockState.getValue(BlockStateProperties.FACING));
//    }
//
//    protected Direction getShaftFacing(Direction face) {
//        if(face == Direction.DOWN){
//            return Direction.SOUTH;
//        }else if(face == Direction.UP){
//            return Direction.NORTH;
//        }
//        return Direction.DOWN;
//    }
//}
