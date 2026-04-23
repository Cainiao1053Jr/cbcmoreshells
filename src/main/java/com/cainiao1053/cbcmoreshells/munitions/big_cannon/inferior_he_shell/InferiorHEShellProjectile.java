package com.cainiao1053.cbcmoreshells.munitions.big_cannon.inferior_he_shell;

import javax.annotation.Nonnull;

import com.cainiao1053.cbcmoreshells.network.CBCMSNetworkImpl;
import com.cainiao1053.cbcmoreshells.network.ClientboundCBCMSTrailPacket;
import net.minecraft.core.Direction;
import net.minecraft.core.Position;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import rbasamoyai.createbigcannons.CreateBigCannons;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.munitions.ShellExplosion;
import rbasamoyai.createbigcannons.munitions.big_cannon.FuzedBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonCommonShellProperties;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;

public class InferiorHEShellProjectile extends FuzedBigCannonProjectile {

	public InferiorHEShellProjectile(EntityType<? extends InferiorHEShellProjectile> type, Level level) {
		super(type, level);
	}

	public int sendTrail = 20;
	public double xOld;
	public double yOld;
	public double zOld;

//	@Override
//	public void tick() {
//		super.tick();
//		if (sendTrail<0){
//			if(!level().isClientSide && level() instanceof ServerLevel serverLevel){
//				for(ServerPlayer players: serverLevel.players()){
//					sendTrailToClient(this.getX(),this.getY(),this.getZ(),xOld,yOld,zOld, players);
//				}
//				sendTrail = 10;
//				xOld = this.getX();
//				yOld = this.getY();
//				zOld = this.getZ();
//			}
//		}else{sendTrail--;}
//	}

	@Override
	protected void detonate(Position position) {
		ShellExplosion explosion = new ShellExplosion(this.level(), this, this.indirectArtilleryFire(false), position.x(),
			position.y(), position.z(), this.getAllProperties().explosion().explosivePower(), false,
			CBCConfigs.server().munitions.damageRestriction.get().explosiveInteraction());
		CreateBigCannons.handleCustomExplosion(this.level(), explosion);
	}

	@Override
	public BlockState getRenderedBlockState() {
		return CBCMSBlocks.Inferior_HE_SHELL.getDefaultState().setValue(BlockStateProperties.FACING, Direction.NORTH);
	}

	@Nonnull
	@Override
	protected BigCannonFuzePropertiesComponent getFuzeProperties() {
		return this.getAllProperties().fuze();
	}

	@Nonnull
	@Override
	protected BigCannonProjectilePropertiesComponent getBigCannonProjectileProperties() {
		return this.getAllProperties().bigCannonProperties();
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return this.getAllProperties().damage();
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	public void sendTrailToClient(double x, double y ,double z,double xOld, double yOld ,double zOld,ServerPlayer player) {
			//NetworkPlatform.sendToClientPlayer(new ClientboundCBCMSTrailPacket(x,y,z,xOld,yOld,zOld), player);
		CBCMSNetworkImpl.sendToClientPlayer(new ClientboundCBCMSTrailPacket(x,y,z,xOld,yOld,zOld), player);
	}

//	@Override
//	public void onAddedToWorld() {
//		super.onAddedToWorld();
//		xOld = this.getX();
//		yOld = this.getY();
//		zOld = this.getZ();
//	}

	protected BigCannonCommonShellProperties getAllProperties() {
		return CBCMunitionPropertiesHandlers.COMMON_SHELL_BIG_CANNON_PROJECTILE.getPropertiesOf(this);
	}


}
