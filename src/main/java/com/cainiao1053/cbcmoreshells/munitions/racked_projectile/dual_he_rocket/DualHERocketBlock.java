package com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_he_rocket;

import com.cainiao1053.cbcmoreshells.CBCMSEntityTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.GeneralRackedProjectileBlock;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;
import org.joml.Vector3dc;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.mod.common.VSGameUtilsKt;


public class DualHERocketBlock extends GeneralRackedProjectileBlock<DualHERocketProjectile> {

	private static final MapCodec<DualHERocketBlock> CODEC = simpleCodec(DualHERocketBlock::new);

	public DualHERocketBlock(Properties properties) {
		super(properties);
		this.shapes = createShapes();
	}

	protected VoxelShaper shapes;

	@Override
	protected MapCodec<? extends DirectionalBlock> codec() {
		return CODEC;
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		Direction direction = state.getValue(FACING);
		return this.shapes.get(direction);
	}

	@Override
	public boolean isBaseFuze() {
		return CBCMSMunitionPropertiesHandlers.RACKED_ROCKET.getPropertiesOf(this.getAssociatedEntityType()).fuze().baseFuze();
	}

	@Override
	public int getLifetimeFromBlock() {
		return CBCMSMunitionPropertiesHandlers.RACKED_ROCKET.getPropertiesOf(this.getAssociatedEntityType()).lifetime();
	}

	@Override
	public boolean directDrop() {
		return false;
	}

	@Override
	public float getVerticalInitVelRefine() {
		return 0.1f;
	}

	@Override
	public float extraInitVel() {
		return CBCMSMunitionPropertiesHandlers.RACKED_ROCKET.getPropertiesOf(this.getAssociatedEntityType()).rackedProjectileProperties().initialVel();
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
	public EntityType<? extends DualHERocketProjectile> getAssociatedEntityType() {
		return CBCMSEntityTypes.DUAL_HE_ROCKET.get();
	}

	public VoxelShaper createShapes() {
		VoxelShape base = box(0, 0, 3, 16, 16, 13);
		return new AllShapes.Builder(base).forDirectional();
	}

}
