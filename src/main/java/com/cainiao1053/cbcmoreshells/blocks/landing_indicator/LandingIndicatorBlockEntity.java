package com.cainiao1053.cbcmoreshells.blocks.landing_indicator;


import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.CbcmoreshellsClient;
//import com.cainiao1053.cbcmoreshells.api.vs.ValkyrienSkies;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.utility.Color;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Vector3d;
import org.joml.Vector3f;
import org.slf4j.Logger;
import org.valkyrienskies.core.api.ships.Ship;

import javax.annotation.Nullable;
import java.util.List;


public class LandingIndicatorBlockEntity extends SmartBlockEntity {


    public LandingIndicatorBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    private int castRange = 350;
    private int colorIndex;
    Color BLUE = new Color(0,0,245).setImmutable().setAlpha(0.5f);
    Color GOLD = new Color(248,189,27).setImmutable().setAlpha(0.5f);
    Color LIGHT_BLUE = new Color(34,177,250).setImmutable().setAlpha(0.5f);
    Color RED = new Color(250,0,0).setImmutable().setAlpha(0.5f);
    Color GREEN = new Color(0,250,0).setImmutable().setAlpha(0.5f);
    Color YELLOW = new Color(249,246,44).setImmutable().setAlpha(0.5f);
    List<Color> PALETTE = List.of(BLUE, GOLD, LIGHT_BLUE, RED, GREEN,YELLOW);
    Color castColor = PALETTE.get(colorIndex);

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> list) {
    }

    Logger LOGGER = Cbcmoreshells.LOGGER;

    @Override
    public void tick() {
        super.tick();
        if(level.isClientSide){
            tickClient();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void outlineClipRayShip(){
        Vec3 start = getBlockPos().getCenter();
        Vector3f step = getBlockState().getValue(BlockStateProperties.FACING).step();
        Vec3 cast = new Vec3(step.x, step.y, step.z).scale(castRange);
        Vec3 end = start.add(cast);


        CbcmoreshellsClient.CLIENT_LERPED_OUTLINER.showLine(
                        "clip_ray_" + getBlockPos().asLong(),
                        start,
                        end
                )
                .colored(PALETTE.get(colorIndex))
                .disableCull()
                .lineWidth(0.2f);
    }

    @OnlyIn(Dist.CLIENT)
    protected void tickClient(){
//        if(getShipOn() == null){
//            return;
//        }
        if(!getBlockState().getValue(BlockStateProperties.POWERED)){
            return;
        }
        outlineClipRayShip();
    }

    public void setCastColor(Color color){
        this.castColor = color;
    }

    public void switchColor(){
        if(colorIndex>=PALETTE.size()-1){
            this.colorIndex = 0;
        }else {
            this.colorIndex++;
        }
        setCastColor(PALETTE.get(colorIndex));
    }

    public void changeCastRange(int range){
        this.castRange = range;
    }
    public void switchRange(){
        if(this.castRange>160){
            this.castRange = 128;
        }else{
            this.castRange = 350;
        }
    }

    @Override
    public void write(CompoundTag compound, boolean clientPacket) {
        compound.putInt("CastRange", castRange);
        compound.putInt("Color", colorIndex);
        compound.putInt("CastRange", castRange);
        super.write(compound, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, boolean clientPacket) {
        castRange = compound.getInt("CastRange");
        colorIndex = compound.getInt("Color");
        castRange = compound.getInt("CastRange");
        super.read(compound, clientPacket);
    }

    public Vector3d getPositionShip(){
        Vec3 front = getBlockPos().getCenter();
        return new Vector3d(front.x, front.y, front.z);
    }

//    public @Nullable Ship getShipOn(){
//        return ValkyrienSkies.getShipManagingBlock(level, getBlockPos());
//    }

}
