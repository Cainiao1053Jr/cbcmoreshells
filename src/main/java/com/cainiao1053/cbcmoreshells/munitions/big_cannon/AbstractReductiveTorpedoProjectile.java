package com.cainiao1053.cbcmoreshells.munitions.big_cannon;

import com.cainiao1053.cbcmoreshells.api.vs.ValkyrienSkies;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedTorpedoTubeContraption;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.valkyrienskies.core.api.ships.Ship;


public abstract class AbstractReductiveTorpedoProjectile extends FuzedCannonTorpedoProjectile {
	protected AbstractReductiveTorpedoProjectile(EntityType<? extends FuzedCannonTorpedoProjectile> type, Level level) {
		super(type, level);
	}
	public MountedTorpedoTubeContraption torpedoTube;

	public void reduceReloadTime() {
		if(this.torpedoTube == null) {
			return;
		}
		BlockPos breechPos = getBreechPos(this.torpedoTube);
		if(this.torpedoTube.presentBlockEntities.get(breechPos) instanceof TorpQuickfiringBreechBlockEntity breech){
			int currentCooldown  = breech.getLoadingCooldown();
			if(currentCooldown < 20) {
				return;
			}
			int newReloadTime = Math.max(currentCooldown - (int)(this.getReloadTime() * getReductionPercentage()), 20);
			breech.setLoadingCooldown(newReloadTime);
		}
	}

	public boolean explodeOnShip(Vec3 pos, Level level) {
		AABB box = AABB.ofSize(pos, 4, 4, 4);
		Iterable<Ship> ships = ValkyrienSkies.getShipsIntersecting(level, box);
		for(Ship ship : ships) {
			return true;
		}
		return false;
	}

	public void playSoundOnHit(Level level) {
		Vec3 pos = this.torpedoTube.entity.position();
		level.playLocalSound(pos.x, pos.y, pos.z, CBCMSSoundEvents.SERVICEBELL.getMainEvent(), SoundSource.AMBIENT, 3.5f, 1.2f, false);
	}

	public float getReductionPercentage(){
		return 0.5f;
	}

	@Override
	public void onShoot(MountedTorpedoTubeContraption contraption, ServerLevel level) {
		super.onShoot(contraption, level);
		this.torpedoTube = contraption;
	}

	public MountedTorpedoTubeContraption getInitTorpedoTube() {
		return torpedoTube;
	}
}
