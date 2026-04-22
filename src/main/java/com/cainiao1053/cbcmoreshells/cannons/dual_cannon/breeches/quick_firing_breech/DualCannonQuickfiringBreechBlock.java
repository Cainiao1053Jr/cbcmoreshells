package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech;

import java.util.HashSet;
import java.util.Set;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBaseBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBodyBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.IDualCannonBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEnd;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.simibubi.create.api.contraption.transformable.TransformableBlock;

import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.config.CBCConfigs;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonMunitionBlock;

public class DualCannonQuickfiringBreechBlock extends DualCannonBaseBlock implements IBE<DualCannonQuickfiringBreechBlockEntity>, TransformableBlock, IWrenchable {

	public static final BooleanProperty AXIS = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;
	private final MapCodec<? extends DirectionalBlock> codec;

	private final NonNullSupplier<? extends Block> slidingConversion;
	private final VoxelShaper visualShapes;
	private final VoxelShaper collisionShapes;

	public DualCannonQuickfiringBreechBlock(Properties properties,DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion, VoxelShape base) {
		this(properties, material, slidingConversion, base, base);
	}

	public DualCannonQuickfiringBreechBlock(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion, VoxelShape visualShape, VoxelShape collisionShape
	) {
		super(properties, material);
		this.slidingConversion = slidingConversion;
		this.visualShapes = new AllShapes.Builder(visualShape).forDirectional();
		this.collisionShapes = new AllShapes.Builder(collisionShape).forDirectional();
		this.codec = simpleCodec(this::fromSelf);
	}

	private DualCannonQuickfiringBreechBlock fromSelf(Properties properties) {
		return new DualCannonQuickfiringBreechBlock(properties, this.getCannonMaterial(), this.slidingConversion, this.visualShapes.get(Direction.UP));
	}

	@Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

	public static DualCannonQuickfiringBreechBlock medium(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
		return new DualCannonQuickfiringBreechBlock(properties, material, slidingConversion, Block.box(0, 0, 4, 16, 16, 12));
	}

	public static DualCannonQuickfiringBreechBlock wide(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
		return new DualCannonQuickfiringBreechBlock(properties, material, slidingConversion, Block.box(-5, 0, 3, 21, 16, 13));
	}

	public static DualCannonQuickfiringBreechBlock large(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
			return new DualCannonQuickfiringBreechBlock(properties, material, slidingConversion, Block.box(-7, 0, 1, 23, 16, 15));
	}

	public static DualCannonQuickfiringBreechBlock singleMedium(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
		return new DualCannonQuickfiringBreechBlock(properties, material, slidingConversion, Block.box(4, 0, 4, 12, 16, 12));
	}

	public static DualCannonQuickfiringBreechBlock singleWide(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
		return new DualCannonQuickfiringBreechBlock(properties, material, slidingConversion, Block.box(3, 0, 3, 13, 16, 13));
	}

	public static DualCannonQuickfiringBreechBlock singleLarge(Properties properties, DualCannonMaterial material, NonNullSupplier<? extends Block> slidingConversion) {
		return new DualCannonQuickfiringBreechBlock(properties, material, slidingConversion, Block.box(2, 0, 2, 14, 16, 14));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		super.createBlockStateDefinition(builder);
		builder.add(AXIS);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return super.getStateForPlacement(context).setValue(AXIS, context.getNearestLookingDirection().getAxis() == Direction.Axis.Z);
	}

	@Override
	public Class<DualCannonQuickfiringBreechBlockEntity> getBlockEntityClass() {
		return DualCannonQuickfiringBreechBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends DualCannonQuickfiringBreechBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.DUAL_CANNON_QUICKFIRING_BREECH.get();
	}

	@Override
	public CannonCastShape getCannonShape() {
		return CannonCastShape.SLIDING_BREECH;
	}

	@Override
	public boolean isComplete(BlockState state) {
		return true;
	}

	@Override public DualCannonEnd getDefaultOpeningType() { return DualCannonEnd.CLOSED; }

	@Override
	public boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand,
											Level level, Contraption contraption, BlockEntity be,
											StructureBlockInfo info, PitchOrientedContraptionEntity entity) {
		if (!(contraption instanceof MountedDualCannonContraption cannon)
			|| !(be instanceof DualCannonQuickfiringBreechBlockEntity breech)
			|| ((DualCannonBlock) info.state().getBlock()).getFacing(info.state()).getAxis() != side.getAxis()
			|| breech.cannonBehavior().isConnectedTo(side)) return false;
		ItemStack stack = player.getItemInHand(interactionHand);

		Direction pushDirection = side.getOpposite();
		BlockPos nextPos = localPos.relative(pushDirection);

		if (stack.isEmpty()) {
			if (level instanceof ServerLevel slevel) {
				if (!breech.onInteractionCooldown()) {
					SoundEvent sound = breech.getOpenProgress() == 0 ? SoundEvents.IRON_TRAPDOOR_OPEN : SoundEvents.IRON_TRAPDOOR_CLOSE;
					level.playSound(null, player.blockPosition(), sound, SoundSource.BLOCKS, 1.0f, 1.5f);
				}
				breech.toggleOpening();
				Set<BlockPos> changed = new HashSet<>(2);
				changed.add(localPos);

				if (breech.getOpenDirection() > 0) {
					BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
					if (be1 instanceof IDualCannonBlockEntity cbe1) {
						StructureBlockInfo info1 = cbe1.cannonBehavior().block();
						ItemStack extract = info1.state().getBlock() instanceof BigCannonMunitionBlock munition ? munition.getExtractedItem(info1, level.registryAccess()) : ItemStack.EMPTY;
						Vec3 normal = new Vec3(side.step());
						Vec3 dir = contraption.entity.applyRotation(normal, 0);
						if (!extract.isEmpty()) {
							Vec3 ejectPos = Vec3.atCenterOf(localPos).add(normal.scale(1.1));
							Vec3 globalPos = entity.toGlobalVector(ejectPos, 0);
							if (CBCConfigs.server().munitions.quickFiringBreechItemGoesToInventory.get()) {
								if (!player.addItem(extract) && !player.isCreative()) {
									ItemEntity item = player.drop(extract, false);
									if (item != null) {
										item.setNoPickUpDelay();
										item.setTarget(player.getUUID());
									}
								}
							} else {
								Vec3 vel = dir.scale(0.075);
								ItemEntity item = new ItemEntity(level, globalPos.x, globalPos.y, globalPos.z, extract, vel.x, vel.y, vel.z);
								item.setPickUpDelay(CBCConfigs.server().munitions.quickFiringBreechItemPickupDelay.get());
								level.addFreshEntity(item);
							}
						}
						cbe1.cannonBehavior().removeBlock();
						changed.add(nextPos);

					}
				}
				DualCannonBlock.writeAndSyncMultipleBlockData(changed, entity, cannon);
			}
			return true;
		}
		if (!breech.isOpen() || breech.onInteractionCooldown()) return false;

		if (Block.byItem(stack.getItem()) instanceof BigCannonMunitionBlock munition) {
			BlockEntity be1 = cannon.presentBlockEntities.get(nextPos);
			if (!(be1 instanceof IDualCannonBlockEntity cbe1)) return false;

			StructureBlockInfo loadInfo = munition.getHandloadingInfo(stack, nextPos, pushDirection, level.registryAccess());
			StructureBlockInfo info1 = cbe1.cannonBehavior().block();

			if (!level.isClientSide) {
				Set<BlockPos> changes = new HashSet<>(2);

				if (!info1.state().isAir()) {
					BlockPos posAfter = nextPos.relative(pushDirection);
					BlockEntity be2 = cannon.presentBlockEntities.get(posAfter);
					if (!(be2 instanceof IDualCannonBlockEntity cbe2) || !cbe2.cannonBehavior().canLoadBlock(info1))
						return false;
					cbe2.cannonBehavior().loadBlock(info1);
					cbe1.cannonBehavior().removeBlock();
					changes.add(posAfter);
				}
				cbe1.cannonBehavior().tryLoadingBlock(loadInfo);
				changes.add(nextPos);

				DualCannonBlock.writeAndSyncMultipleBlockData(changes, entity, cannon);

				SoundType sound = loadInfo.state().getSoundType();
				level.playSound(null, player.blockPosition(), sound.getPlaceSound(), SoundSource.BLOCKS, sound.getVolume(), sound.getPitch());
				if (!player.isCreative()) stack.shrink(1);
			}
			return true;
		}

		return false;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return this.collisionShapes.get(this.getFacing(state));
	}

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return this.visualShapes.get(this.getFacing(state));
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot) {
		if (rot.ordinal() % 2 == 1) state = state.cycle(AXIS);
		return super.rotate(state, rot);
	}

	@Override
	public BlockState transform(BlockState state, StructureTransform transform) {
		if (transform.mirror != null) {
			state = mirror(state, transform.mirror);
		}

		if (transform.rotationAxis == Direction.Axis.Y) {
			return rotate(state, transform.rotation);
		}

		Direction newFacing = transform.rotateFacing(state.getValue(FACING));
		if (transform.rotationAxis == newFacing.getAxis() && transform.rotation.ordinal() % 2 == 1) {
			state = state.cycle(AXIS);
		}
		return state.setValue(FACING, newFacing);
	}

	protected BlockState getConversion(BlockState old) {
		return this.slidingConversion.get().defaultBlockState()
			.setValue(FACING, old.getValue(FACING))
			.setValue(AXIS, old.getValue(AXIS));
	}

	@Override
	public InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
		return InteractionResult.PASS;
	}

}
