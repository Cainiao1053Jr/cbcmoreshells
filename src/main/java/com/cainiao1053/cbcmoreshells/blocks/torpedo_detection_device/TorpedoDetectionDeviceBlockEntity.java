package com.cainiao1053.cbcmoreshells.blocks.torpedo_detection_device;


import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;


public class TorpedoDetectionDeviceBlockEntity extends KineticBlockEntity {


    public TorpedoDetectionDeviceBlockEntity(BlockEntityType<?> typeIn, BlockPos pos, BlockState state) {
        super(typeIn, pos, state);
    }

    Logger LOGGER = Cbcmoreshells.LOGGER;
    private int cooldown = 30;
    private double minDist;
    private int restartCooldown = getMaxRestartCooldown();
    private int activationLeft = 0;
    @Override
    public void onSpeedChanged(float prevSpeed) {
        super.onSpeedChanged(prevSpeed);
    }

    @Override
    public void tick() {
        super.tick();
        if (level.isClientSide){return;}
        float speedAbs = Math.abs(getSpeed());
        if(speedAbs<96){
            return;
        }

//        ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) getLevel(), this.getBlockPos());
//        if(ship == null){return;}

        if(restartCooldown>0){
            restartCooldown--;
            return;
        }else if(restartCooldown==0){
            restartCooldown--;
            //ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) getLevel(), this.getBlockPos());
            //if(ship != null){
//                Vector3dc shipPos = ship.getTransform().getPositionInWorld();
//                level.playSound(
//                        null,
//                        shipPos.x(),
//                        shipPos.y(),
//                        shipPos.z(),
//                        SoundEvents.NOTE_BLOCK_BELL.get(),
//                        SoundSource.BLOCKS,
//                        3.5f,
//                        1.2f
//                );
            //}
            return;
        }else if(activationLeft>0){
            activationLeft--;
        }else{
            if(!getBlockState().getValue(BlockStateProperties.POWERED)){
                return;
            }
            restartCooldown = getMaxRestartCooldown();
            level.setBlock(getBlockPos(), getBlockState().setValue(BlockStateProperties.POWERED, false), 3);
            //ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) getLevel(), this.getBlockPos());
            //if(ship !=null){
//                Vector3dc shipPos = ship.getTransform().getPositionInWorld();
//                level.playSound(
//                        null,
//                        shipPos.x(),
//                        shipPos.y(),
//                        shipPos.z(),
//                        SoundEvents.NOTE_BLOCK_BIT.get(),
//                        SoundSource.BLOCKS,
//                        3.5f,
//                        0.9f
//                );
            //}
            return;
        }

        if(cooldown>0){
            cooldown--;
            return;
        }else {
            cooldown=30;
        }

//                Vector3dc shipPos = ship.getTransform().getPositionInWorld();
                ServerLevel serverLevel = (ServerLevel) level;
                double searchRange = speedAbs*1.5;
                minDist = searchRange;
//                AABB box = new AABB(shipPos.x() - searchRange, shipPos.y() - searchRange, shipPos.z() - searchRange,
//                        shipPos.x() + searchRange, shipPos.y() + searchRange, shipPos.z() + searchRange);

//                List<AbstractCannonTorpedoProjectile> torpedoes =
//                        serverLevel.getEntitiesOfClass(
//                                AbstractCannonTorpedoProjectile.class,
//                                box,
//                                torp -> {
//                                    Vec3 torpVel = torp.getDeltaMovement();
//                                    Vec3 torpDistVec = new Vec3(torp.getX() - shipPos.x(), torp.getY() - shipPos.y(), torp.getZ()-shipPos.z());
//                                    boolean torpInAlerting = vecDot(torpVel,torpDistVec)<0;
//                                    double manhattan =
//                                            Math.abs(torpDistVec.x)
//                                                    + Math.abs(torpDistVec.y)
//                                                    + Math.abs(torpDistVec.z);
//                                    if(torpInAlerting){
//                                        if(manhattan < minDist){
//                                            minDist = manhattan;
//                                        }
//                                    }
//                                    return manhattan <= searchRange && torpInAlerting;
//                                });

//                if(!torpedoes.isEmpty()){
//                    float pitch = torpPitchLerp(minDist,searchRange);
//                    level.playSound(
//                            null,
//                            shipPos.x(),
//                            shipPos.y(),
//                            shipPos.z(),
//                            CBCMSSoundEvents.SONAR_PULSE.getMainEvent(),
//                            SoundSource.BLOCKS,
//                            5F*pitch,
//                            pitch
//                    );
//                    for (AbstractCannonTorpedoProjectile torp : torpedoes) {
//                        torp.setGlowingTag(true);
//                    }
//                }

    }

    @Override
    public void write(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        compound.putInt("RestartCooldown", restartCooldown);
        compound.putInt("ActivationLeft", activationLeft);
        super.write(compound, registries, clientPacket);
    }

    @Override
    protected void read(CompoundTag compound, HolderLookup.Provider registries, boolean clientPacket) {
        restartCooldown = compound.getInt("RestartCooldown");
        activationLeft = compound.getInt("ActivationLeft");
        super.read(compound, registries, clientPacket);
    }

    public int getMaxRestartCooldown() {
        return 1800;
    }

    public int getMaxActivationLeft() {
        return 3600;
    }

    public void setMaxActivation(int activation){
        this.activationLeft = activation;
    }

    public int getRestartCooldown() {
        return this.restartCooldown;
    }

    protected float torpPitchLerp(double dist, double maxDist){
        if (maxDist>250){
            maxDist=250;
        }
        if(dist>maxDist){
            dist=maxDist;
        }
        double lerp = (maxDist-dist)/maxDist;
        return (float)(0.8+0.7*lerp);
    }

    public static double vecDot(Vec3 a, Vec3 b) {
        return a.x * b.x + a.y * b.y + a.z * b.z;
    }
}
