package com.cainiao1053.cbcmoreshells.cannons.package_slider.breeches;

import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedPackageSliderContraption;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBaseBlock;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderBodyBlock;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.PackageSliderEnd;
import com.cainiao1053.cbcmoreshells.cannons.package_slider.material.PackageSliderMaterial;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.mojang.serialization.MapCodec;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.logistics.box.PackageItem;
import com.simibubi.create.foundation.block.IBE;
import net.createmod.catnip.math.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import rbasamoyai.createbigcannons.cannon_control.contraption.PitchOrientedContraptionEntity;
import rbasamoyai.createbigcannons.crafting.casting.CannonCastShape;
import com.simibubi.create.content.contraptions.Contraption;

public class PackageSliderQuickfiringBreechBlock extends PackageSliderBaseBlock implements IBE<PackageSliderQuickfiringBreechBlockEntity> {

	public static final int DEFAULT_BASE_RELOAD_TICKS = 20;
	private static final VoxelShaper SHAPES = new AllShapes.Builder(PackageSliderBodyBlock.BARREL_SHAPE).forDirectional();

	private final int baseReloadTicks;
	private final MapCodec<? extends DirectionalBlock> codec;

	public PackageSliderQuickfiringBreechBlock(Properties properties, PackageSliderMaterial material) {
		this(properties, material, DEFAULT_BASE_RELOAD_TICKS);
	}

	public PackageSliderQuickfiringBreechBlock(Properties properties, PackageSliderMaterial material, int baseReloadTicks) {
		super(properties, material);
		this.baseReloadTicks = baseReloadTicks;
		this.codec = simpleCodec(p -> new PackageSliderQuickfiringBreechBlock(p, this.getCannonMaterial(), this.baseReloadTicks));
	}

	public int getBaseReloadTicks() {
		return this.baseReloadTicks;
	}

	@Override protected MapCodec<? extends DirectionalBlock> codec() { return this.codec; }

	@Override
	public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES.get(this.getFacing(state));
	}

	@Override public PackageSliderEnd getDefaultOpeningType() { return PackageSliderEnd.CLOSED; }

	@Override public CannonCastShape getCannonShape() { return CannonCastShape.SLIDING_BREECH; }

	@Override
	public BlockState getAssistedPlacementState(BlockState state, Direction placementDir, Direction cannonFacing) {
		// The breech sits at the closed end and faces back into the barrel, which is how
		// MountedPackageSliderContraption orients the assembled cannon.
		return state.setValue(FACING, placementDir.getOpposite());
	}

	@Override
	public boolean onInteractWhileAssembled(Player player, BlockPos localPos, Direction side, InteractionHand interactionHand,
											Level level, Contraption contraption, BlockEntity be,
											StructureBlockInfo info, PitchOrientedContraptionEntity entity) {
		if (!(contraption instanceof MountedPackageSliderContraption cannon)
			|| !(be instanceof PackageSliderQuickfiringBreechBlockEntity)) return false;
		ItemStack stack = player.getItemInHand(interactionHand);

		if (stack.isEmpty()) {
			// Empty hand takes the loaded package back out.
			if (!level.isClientSide) {
				ItemStack unloaded = cannon.unloadPackage(entity);
				if (!unloaded.isEmpty()) {
					if (!player.addItem(unloaded)) {
						ItemEntity item = player.drop(unloaded, false);
						if (item != null) {
							item.setNoPickUpDelay();
							item.setTarget(player.getUUID());
						}
					}
					level.playSound(null, player.blockPosition(), SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.5f, 1.0f);
				}
			}
			return true;
		}

		if (!PackageItem.isPackage(stack)) return false;
		if (!level.isClientSide && cannon.tryLoadPackage(stack, entity)) {
			level.playSound(null, player.blockPosition(), SoundEvents.IRON_TRAPDOOR_CLOSE, SoundSource.BLOCKS, 1.0f, 1.5f);
			if (!player.isCreative()) stack.shrink(1);
		}
		return true;
	}

	@Override
	public Class<PackageSliderQuickfiringBreechBlockEntity> getBlockEntityClass() {
		return PackageSliderQuickfiringBreechBlockEntity.class;
	}

	@Override
	public BlockEntityType<? extends PackageSliderQuickfiringBreechBlockEntity> getBlockEntityType() {
		return CBCMSBlockEntities.PACKAGE_SLIDER_QUICKFIRING_BREECH.get();
	}

}
