package com.cainiao1053.cbcmoreshells.index;

import java.util.function.Function;

import javax.annotation.Nullable;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackInteractionPoint;
import com.cainiao1053.cbcmoreshells.blocks.dish_plate.DishPlateBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.dish_plate.DishPlateInteractionPoint;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpedoCannonMountPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPoint;
import com.simibubi.create.content.kinetics.mechanicalArm.ArmInteractionPointType;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;


public class CBCMSArmInteractionPointTypes {

	//public static final CannonMountType TORPEDO_TUBE = register("torpedo_cannon_mount", CannonMountType::new);
	public static final AmmoRackType AMMO_RACK = register("ammo_rack", AmmoRackType::new);
	public static final DishPlateType DISH_PLATE = register("dish_plate", DishPlateType::new);


	private static <T extends ArmInteractionPointType> T register(String id, Function<ResourceLocation, T> factory) {
		T type = factory.apply(Cbcmoreshells.resource(id));
		ArmInteractionPointType.register(type);
		return type;
	}

	public static class CannonMountType extends ArmInteractionPointType {
		public CannonMountType(ResourceLocation id) {
			super();
		}

		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			if (CBCMSBlocks.AMMO_RACK.has(state)) {
				return level.getBlockEntity(pos) instanceof AmmoRackBlockEntity;
			}
			return false;
		}

		@Nullable
		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return new TorpedoCannonMountPoint(this, level, pos, state);
		}
	}

	public static class AmmoRackType extends ArmInteractionPointType {
		public AmmoRackType(ResourceLocation id) {
			super(id);
		}

		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			if (CBCMSBlocks.AMMO_RACK.has(state)) {
				return level.getBlockEntity(pos) instanceof AmmoRackBlockEntity;
			}else if(CBCMSBlocks.STEEL_AMMO_RACK.has(state)) {
				return level.getBlockEntity(pos) instanceof AmmoRackBlockEntity;
			}
			return false;
		}

		@Nullable
		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return new AmmoRackInteractionPoint(this, level, pos, state);
		}
	}

	public static class DishPlateType extends ArmInteractionPointType {
		public DishPlateType(ResourceLocation id) {
			super(id);
		}

		@Override
		public boolean canCreatePoint(Level level, BlockPos pos, BlockState state) {
			if (CBCMSBlocks.DISH_PLATE.has(state)) {
				return level.getBlockEntity(pos) instanceof DishPlateBlockEntity;
			}else if(CBCMSBlocks.ROUND_DISH_PLATE.has(state)){
				return level.getBlockEntity(pos) instanceof DishPlateBlockEntity;
			}
			return false;
		}

		@Nullable
		@Override
		public ArmInteractionPoint createPoint(Level level, BlockPos pos, BlockState state) {
			return new DishPlateInteractionPoint(this, level, pos, state);
		}
	}

	public static void register() {
	}

}
