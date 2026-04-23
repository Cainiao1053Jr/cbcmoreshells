package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_loitering_rocket;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.GeneralRackedProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;


public class HELoiteringRocketBlock extends GeneralRackedProjectileBlock<HELoiteringRocketProjectile> {

	private static final MapCodec<HELoiteringRocketBlock> CODEC = simpleCodec(HELoiteringRocketBlock::new);

	public HELoiteringRocketBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public int getLifetimeFromBlock() {
		return CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET.getPropertiesOf(this.getAssociatedEntityType()).lifetime();
	}

	@Override
	public float getVerticalInitVelRefine() {
		return 0.1f;
	}

	@Override
	public float extraInitVel() {
		return CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET.getPropertiesOf(this.getAssociatedEntityType()).rackedProjectileProperties().initialVel();
	}

	@Override
	public void playSoundForLaunching(Level level, BlockPos pos) {
		if(level.isClientSide()){return;}
		ServerShip ship = VSGameUtilsKt.getShipManagingPos((ServerLevel) level, pos);
		Vector3dc actualPos = new Vector3d(pos.getX(), pos.getY(), pos.getZ());
		if(ship != null){
			actualPos = ship.getTransform().getPositionInWorld();
		}
		level.playSound(
				null,
				actualPos.x(),
				actualPos.y(),
				actualPos.z(),
				CBCMSSoundEvents.ROCKET_LAUNCH.getMainEvent(),
				SoundSource.BLOCKS,
				2.4f,
				1
		);
	}

	@Override
	public EntityType<? extends HELoiteringRocketProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.HE_LOITERING_ROCKET.get();
	}

	@Override
	public boolean directDrop() {
		return false;
	}
}
