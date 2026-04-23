package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bomb;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.GeneralRackedProjectileBlock;
import com.mojang.serialization.MapCodec;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.DirectionalBlock;


public class APHEBombBlock extends GeneralRackedProjectileBlock<APHEBombProjectile> {

	private static final MapCodec<APHEBombBlock> CODEC = simpleCodec(APHEBombBlock::new);

	public APHEBombBlock(Properties properties) {
		super(properties);
	}

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

//	@Override
//	public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos neighbour, boolean p_60514_) {
//		super.neighborChanged(state, level, pos, block, neighbour, p_60514_);
//		if (!level.isClientSide()) {
//			if(level.hasNeighborSignal(pos)){
//				FuzedRackedProjectile projectile = (FuzedRackedProjectile) this.getProjectile(level,pos,state);
//				projectile.setFuze(getFuzeFromBlock(level,pos));
//				projectile.setLifetime(getLifetimeFromBlock()-20);
//				Vec3 initVel = getInitialVelDir(state);
//				projectile.setPos(pos.getX() + 1.5*initVel.x,pos.getY(),pos.getZ() + 1.5*initVel.z);
//				projectile.shoot(initVel.x, initVel.y - 0.25, initVel.z, 0.2f, 0.8f);
//				level.addFreshEntity(projectile);
//				level.setBlock(pos, Blocks.AIR.defaultBlockState(),2);
//			}
//		}
//	}


	@Override
	public int getLifetimeFromBlock() {
		return CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE.getPropertiesOf(this.getAssociatedEntityType()).lifetime();
	}

	@Override
	public EntityType<? extends APHEBombProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.APHE_BOMB.get();
	}

}
