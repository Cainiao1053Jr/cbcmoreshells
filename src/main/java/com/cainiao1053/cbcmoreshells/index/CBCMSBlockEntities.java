package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackRenderer;
import com.cainiao1053.cbcmoreshells.blocks.command_deployer.CommandDeployerBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.command_deployer.CommandDeployerRenderer;
import com.cainiao1053.cbcmoreshells.blocks.command_displayer.CommandDisplayerBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.dish_plate.DishPlateBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.dish_plate.DishPlateRenderer;
import com.cainiao1053.cbcmoreshells.blocks.landing_indicator.LandingIndicatorBlockEntity;
import com.cainiao1053.cbcmoreshells.blocks.torpedo_detection_device.TorpedoDetectionDeviceBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.DualCannonBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonQuickfiringBreechBlockEntityRenderer;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonQuickfiringBreechVisual;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.sliding_breech.DualCannonSlidingBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.sliding_breech.DualCannonSlidingBreechBlockEntityRenderer;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.dual_cannon_end.DualCannonEndBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlockRenderer;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackQuickfiringBreechBlockEntityRenderer;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.sliding_breech.ProjectileRackSlidingBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.sliding_breech.ProjectileRackSlidingBreechBlockEntityRenderer;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.projectile_rack_end.ProjectileRackEndBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.TorpedoTubeBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpQuickfiringBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpQuickfiringBreechBlockEntityRenderer;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.sliding_breech.TorpedoSlidingBreechBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.sliding_breech.TorpedoSlidingBreechBlockEntityRenderer;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.torpedo_end.TorpedoTubeEndBlockEntity;
import com.tterrag.registrate.util.entry.BlockEntityEntry;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.quickfiring_breech.QuickfiringBreechVisual;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechBlockEntity;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechBlockEntityRenderer;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechVisual;

import static rbasamoyai.createbigcannons.CreateBigCannons.REGISTRATE;


public class CBCMSBlockEntities {

	public static final BlockEntityEntry<TorpedoTubeBlockEntity> TORPEDO_TUBE_BODY = REGISTRATE
		.blockEntity("torpedo_tube_body", TorpedoTubeBlockEntity::new)
		.validBlocks(
//			CBCMSBlocks.CAST_IRON_TORPEDO_BARREL, CBCMSBlocks.CAST_IRON_TORPEDO_CHAMBER,
//			CBCMSBlocks.BRONZE_TORPEDO_BARREL, CBCMSBlocks.BRONZE_TORPEDO_CHAMBER,
			CBCMSBlocks.STEEL_TORPEDO_BARREL, CBCMSBlocks.STEEL_TORPEDO_CHAMBER)
		.register();

	public static final BlockEntityEntry<TorpedoTubeEndBlockEntity> TORPEDO_TUBE_END = REGISTRATE
			.blockEntity("torpedo_tube_end", TorpedoTubeEndBlockEntity::new)
			.validBlocks(
//					CBCMSBlocks.CAST_IRON_CANNON_END, CBCMSBlocks.BRONZE_CANNON_END
			)
			.register();

	public static final BlockEntityEntry<TorpQuickfiringBreechBlockEntity> TORPEDO_QUICKFIRING_BREECH = REGISTRATE
			.blockEntity("torpedo_quickfiring_breech", TorpQuickfiringBreechBlockEntity::new)
			//.instance(() -> TorpQuickfiringBreechInstance::new)
			.renderer(() -> TorpQuickfiringBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.STEEL_TORPEDO_QUICKFIRING_BREECH)
			.register();

	public static final BlockEntityEntry<TorpedoSlidingBreechBlockEntity> TORPEDO_SLIDING_BREECH = REGISTRATE
			.blockEntity("torpedo_sliding_breech", TorpedoSlidingBreechBlockEntity::new)
			//.instance(() -> TorpedoSlidingBreechInstance::new, false)
			.renderer(() -> TorpedoSlidingBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.STEEL_TORPEDO_SLIDING_BREECH)
			.register();

	public static final BlockEntityEntry<DualCannonBlockEntity> DUAL_CANNON_BODY = REGISTRATE
			.blockEntity("dual_cannon_body", DualCannonBlockEntity::new)
			.validBlocks(
					CBCMSBlocks.STEEL_DUAL_CANNON_BARREL, CBCMSBlocks.STEEL_DUAL_CANNON_CHAMBER, CBCMSBlocks.STEEL_DUAL_CANNON_CHARGER,
					CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_BARREL, CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_CHAMBER, CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_CHARGER,
					CBCMSBlocks.CAST_IRON_DUAL_CANNON_BARREL, CBCMSBlocks.CAST_IRON_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.BRONZE_DUAL_CANNON_BARREL,CBCMSBlocks.BRONZE_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.BRASS_DUAL_CANNON_BARREL,CBCMSBlocks.BRASS_DUAL_CANNON_CHAMBER, CBCMSBlocks.BRASS_DUAL_CANNON_CHARGER,
					CBCMSBlocks.ROSEQUARTZ_BRASS_DUAL_CANNON_BARREL,CBCMSBlocks.ROSEQUARTZ_BRASS_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_BARREL,CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_BARREL, CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_BARREL,CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_BARREL, CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_CHAMBER, CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_CHARGER,
					CBCMSBlocks.WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_BARREL, CBCMSBlocks.WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_BARREL, CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_CHAMBER, CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_CHARGER,
					CBCMSBlocks.STEEL_SINGLE_CANNON_CHAMBER, CBCMSBlocks.STEEL_SINGLE_CANNON_BARREL,
					CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_CHAMBER, CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_MAGAZINE,
					CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER, CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL, CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL, CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER, CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED, CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHARGER,
					CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_BARREL, CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_BARREL, CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER, CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED, CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHARGER,
					CBCMSBlocks.BRASS_SINGLE_CANNON_BARREL, CBCMSBlocks.BRASS_SINGLE_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_CHAMBER, CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_BARREL, CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_CHAMBER, CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_CHARGER,
					CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER, CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL, CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL, CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER, CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_BARREL, CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_CHAMBER,
					CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_BARREL, CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_CHAMBER, CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_BARREL, CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_CHAMBER,CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_BARREL, CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER, CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED,
					CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_BARREL, CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_CHAMBER, CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_CHARGER
			)
			.register();

	public static final BlockEntityEntry<DualCannonEndBlockEntity> DUAL_CANNON_END = REGISTRATE
			.blockEntity("dual_cannon_end", DualCannonEndBlockEntity::new)
			.validBlocks(
//					CBCMSBlocks.CAST_IRON_CANNON_END, CBCMSBlocks.BRONZE_CANNON_END
			)
			.register();

	public static final BlockEntityEntry<DualCannonQuickfiringBreechBlockEntity> DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.blockEntity("dual_cannon_quickfiring_breech", DualCannonQuickfiringBreechBlockEntity::new)
			.visual(() -> DualCannonQuickfiringBreechVisual::new)
			.renderer(() -> DualCannonQuickfiringBreechBlockEntityRenderer::new)
			.validBlocks(
					CBCMSBlocks.STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.CAST_IRON_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.BRONZE_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.ROSEQUARTZ_BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.BRASS_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
					CBCMSBlocks.STEEL_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
					CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH
			)
			.register();

	public static final BlockEntityEntry<DualCannonSlidingBreechBlockEntity> DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.blockEntity("dual_cannon_sliding_breech", DualCannonSlidingBreechBlockEntity::new)
			//.instance(() -> DualCannonSlidingBreechInstance::new, false)
			.renderer(() -> DualCannonSlidingBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.STEEL_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.CAST_IRON_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.BRONZE_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.BRASS_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.STEEL_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.BRASS_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH ,
					CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_SLIDING_BREECH,
					CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH,
					CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_SLIDING_BREECH
			)
			.register();

	public static final BlockEntityEntry<QuickfiringBreechBlockEntity> NETHERSTEELQUICKFIRING_BREECH = REGISTRATE
			.blockEntity("nethersteel_quickfiring_breech", QuickfiringBreechBlockEntity::new)
			//.instance(() -> QuickfiringBreechInstance::new)
			.visual(() -> QuickfiringBreechVisual::new)
			.renderer(() -> QuickfiringBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.NETHERSTEEL_QUICKFIRING_BREECH)
			.register();

	public static final BlockEntityEntry<SlidingBreechBlockEntity> SLIDING_BREECH = REGISTRATE
			.blockEntity("nethersteel_sliding_breech", SlidingBreechBlockEntity::new)
			.visual(() -> SlidingBreechVisual::new, false)
			//.instance(() -> SlidingBreechInstance::new, false)
			.renderer(() -> SlidingBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.NETHERSTEEL_SLIDING_BREECH)
			.register();

	public static final BlockEntityEntry<ProjectileRackBlockEntity> PROJECTILE_RACK_BODY = REGISTRATE
			.blockEntity("projectile_rack_body", ProjectileRackBlockEntity::new)
			.validBlocks(
//			CBCMSBlocks.CAST_IRON_TORPEDO_BARREL, CBCMSBlocks.CAST_IRON_TORPEDO_CHAMBER,
//			CBCMSBlocks.BRONZE_TORPEDO_BARREL, CBCMSBlocks.BRONZE_TORPEDO_CHAMBER,
					CBCMSBlocks.STEEL_PROJECTILE_RACK_BARREL, CBCMSBlocks.STEEL_PROJECTILE_RACK_CHAMBER,
					CBCMSBlocks.STEEL_PROJECTILE_RACK_STABILIZER)
			.renderer(()-> ProjectileRackBlockRenderer::new)
			.register();

	public static final BlockEntityEntry<ProjectileRackEndBlockEntity> PROJECTILE_RACK_END = REGISTRATE
			.blockEntity("projectile_rack_end", ProjectileRackEndBlockEntity::new)
			.validBlocks(
//					CBCMSBlocks.CAST_IRON_CANNON_END, CBCMSBlocks.BRONZE_CANNON_END
			)
			.register();

	public static final BlockEntityEntry<ProjectileRackQuickfiringBreechBlockEntity> PROJECTILE_RACK_QUICKFIRING_BREECH = REGISTRATE
			.blockEntity("projectile_rack_quickfiring_breech", ProjectileRackQuickfiringBreechBlockEntity::new)
			//.instance(() -> ProjectileRackQuickfiringBreechInstance::new)
			.renderer(() -> ProjectileRackQuickfiringBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.STEEL_PROJECTILE_RACK_QUICKFIRING_BREECH)
			.register();

	public static final BlockEntityEntry<ProjectileRackSlidingBreechBlockEntity> PROJECTILE_RACK_SLIDING_BREECH = REGISTRATE
			.blockEntity("projectile_rack_sliding_breech", ProjectileRackSlidingBreechBlockEntity::new)
			//.instance(() -> ProjectileRackSlidingBreechInstance::new, false)
			.renderer(() -> ProjectileRackSlidingBreechBlockEntityRenderer::new)
			.validBlocks(CBCMSBlocks.STEEL_PROJECTILE_RACK_SLIDING_BREECH)
			.register();

	public static final BlockEntityEntry<TorpedoDetectionDeviceBlockEntity> TORPEDO_DETECTION_DEVICE = REGISTRATE
			.blockEntity("torpedo_detection_device", TorpedoDetectionDeviceBlockEntity::new)
			.validBlocks(CBCMSBlocks.TORPEDO_DETECTION_DEVICE)
			.register();

	public static final BlockEntityEntry<AmmoRackBlockEntity> AMMO_RACK = REGISTRATE
			.blockEntity("ammo_rack", AmmoRackBlockEntity::new)
			.validBlocks(CBCMSBlocks.AMMO_RACK, CBCMSBlocks.STEEL_AMMO_RACK)
			.renderer(()-> AmmoRackRenderer::new)
			.register();

	public static final BlockEntityEntry<DishPlateBlockEntity> DISH_PLATE = REGISTRATE
			.blockEntity("dish_plate", DishPlateBlockEntity::new)
			.validBlocks(CBCMSBlocks.DISH_PLATE, CBCMSBlocks.ROUND_DISH_PLATE)
			.renderer(()-> DishPlateRenderer::new)
			.register();

	public static final BlockEntityEntry<LandingIndicatorBlockEntity> LANDING_INDICATOR = REGISTRATE
			.blockEntity("landing_indicator", LandingIndicatorBlockEntity::new)
			//.instance(() -> TorpedoDetectionDeviceInstance::new, false)
			.validBlocks(CBCMSBlocks.LANDING_INDICATOR)
			//.renderer(() -> ShaftRenderer::new)
			.register();

	public static final BlockEntityEntry<CommandDisplayerBlockEntity> COMMAND_DISPLAYER = REGISTRATE
			.blockEntity("command_displayer", CommandDisplayerBlockEntity::new)
			//.instance(() -> TorpedoDetectionDeviceInstance::new, false)
			.validBlocks(CBCMSBlocks.COMMAND_DISPLAYER)
			//.renderer(() -> ShaftRenderer::new)
			.register();

	public static final BlockEntityEntry<CommandDeployerBlockEntity> COMMAND_DEPLOYER = REGISTRATE
			.blockEntity("command_deployer", CommandDeployerBlockEntity::new)
			.validBlocks(CBCMSBlocks.COMMAND_DEPLOYER)
			.renderer(() -> CommandDeployerRenderer::new)
			.register();


	public static void register() {
	}

}
