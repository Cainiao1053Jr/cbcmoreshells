package com.cainiao1053.cbcmoreshells;

import static com.simibubi.create.foundation.data.ModelGen.customItemModel;
import static com.simibubi.create.foundation.data.TagGen.axeOrPickaxe;
import static com.cainiao1053.cbcmoreshells.Cbcmoreshells.REGISTRATE;

import java.util.function.Supplier;

import com.cainiao1053.cbcmoreshells.blocks.LootBarrelBlock;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlock;
import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlockItem;
import com.cainiao1053.cbcmoreshells.blocks.command_deployer.CommandDeployerBlock;
import com.cainiao1053.cbcmoreshells.blocks.command_deployer.CommandDeployerBlockItem;
import com.cainiao1053.cbcmoreshells.blocks.command_displayer.CommandDisplayerBlock;
import com.cainiao1053.cbcmoreshells.blocks.command_displayer.CommandDisplayerBlockItem;
import com.cainiao1053.cbcmoreshells.blocks.dish_plate.DishPlateBlock;
import com.cainiao1053.cbcmoreshells.blocks.dish_plate.DishPlateBlockItem;
import com.cainiao1053.cbcmoreshells.blocks.landing_indicator.LandingIndicatorBlock;
import com.cainiao1053.cbcmoreshells.blocks.landing_indicator.LandingIndicatorBlockItem;
import com.cainiao1053.cbcmoreshells.blocks.torpedo_detection_device.TorpedoDetectionDeviceBlock;
import com.cainiao1053.cbcmoreshells.blocks.torpedo_detection_device.TorpedoDetectionDeviceBlockItem;
import com.cainiao1053.cbcmoreshells.cannons.big_cannon.NethersteelQuickfiringBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.big_cannon.NethersteelSlidingBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.*;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.quick_firing_breech.DualCannonQuickfiringBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.breeches.sliding_breech.DualCannonSlidingBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBlockItem;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.ProjectileRackBodyBlock;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.quick_firing_breech.ProjectileRackQuickfiringBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.projectile_rack.breeches.sliding_breech.ProjectileRackSlidingBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.TorpedoTubeBodyBlock;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.quick_firing_breech.TorpQuickfiringBreechBlock;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches.sliding_breech.TorpedoSlidingBreechBlock;
import com.cainiao1053.cbcmoreshells.datagen.assets.CBCMSBuilderTransformers;
import com.cainiao1053.cbcmoreshells.index.CBCMSDualCannonMaterials;
import com.cainiao1053.cbcmoreshells.index.CBCMSProjectileRackMaterials;
import com.cainiao1053.cbcmoreshells.index.CBCMSSpriteShifts;
import com.cainiao1053.cbcmoreshells.index.CBCMSTorpedoTubeMaterials;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_shrapnel_torpedo.AirdroppedShrapnelTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_shrapnel_torpedo.AirdroppedShrapnelTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_torpedo.AirdroppedTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_torpedo.AirdroppedTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_he_shell.AntiairHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_he_shell.AntiairHEShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell.AntiairShrapnelShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell.AntiairShrapnelShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ap_super_heavy.APSuperHeavyBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ap_super_heavy.APSuperHeavyShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shell.APBCShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shell.APBCShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shot.APBCBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shot.APBCShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apfsds_shot.APFSDSProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apfsds_shot.APFSDSShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.aphe_cannon_rocket.APHECannonRocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.baguette_shot.BaguetteProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.baguette_shot.BaguetteShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.baked_apfsds_shot.BakedAPFSDSProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.baked_apfsds_shot.BakedAPFSDSShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.cannon_torpedo.CannonTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.cannon_torpedo.CannonTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.deepwater_shrapnel_torpedo.DeepwaterShrapnelTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.deepwater_shrapnel_torpedo.DeepwaterShrapnelTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.he_cannon_rocket.HECannonRocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.hesh_shell.HESHShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.highspeed_torpedo.HighspeedTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.highspeed_torpedo.HighspeedTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.incendiary_he_shell.IncendiaryHEProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.incendiary_he_shell.IncendiaryHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.inferior_he_shell.InferiorHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_shrapnel_torpedo.LongRangeShrapnelTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_shrapnel_torpedo.LongRangeShrapnelTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_torpedo.LongRangeTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_torpedo.LongRangeTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo.MediumRangeDeepwaterTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo.MediumRangeDeepwaterTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo_typeb.MediumRangeDeepwaterTorpedoTypeBBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo_typeb.MediumRangeDeepwaterTorpedoTypeBBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_torpedo_typeb.MediumRangeTorpedoTypeBBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_torpedo_typeb.MediumRangeTorpedoTypeBBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sap_shell.SAPShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo.SharpnelTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo.SharpnelTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_he_shell.ShellessHEProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_he_shell.ShellessHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_incendiary_he_shell.ShellessIncendiaryHEProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_incendiary_he_shell.ShellessIncendiaryHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_sap_shell.ShellessSAPProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_sap_shell.ShellessSAPShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shellless_ap_shot.ShellessAPProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shellless_ap_shot.ShellessAPShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_antiair_he_shell.ExtendedAntiairHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_ap_shot.ExtendedAPShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_ap_shot.ExtendedAPShotBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_antiair_he_shell.NormalAntiairHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_antiair_he_shell.NormalAntiairHEShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shell.NormalAPShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shell.NormalAPShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shot.NormalAPShotBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shot.NormalAPShotBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_apbc_shell.NormalAPBCShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_apbc_shell.NormalAPBCShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_he_shell.NormalHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_he_shell.NormalHEShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell.NormalIncendiaryHEShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell.NormalIncendiaryHEShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_shell.NormalSAPShellBlock;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_shell.NormalSAPShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bomb.APHEBombBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bomb.APHEBombBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bouncing_bomb.APHEBouncingBombBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bouncing_bomb.APHEBouncingBombBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_loitering_rocket.APHELoiteringRocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_loitering_rocket.APHELoiteringRocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_rocket.APHERocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_rocket.APHERocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.depth_charge.DepthChargeBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.depth_charge.DepthChargeBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_aphe_rocket.DualAPHERocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_aphe_rocket.DualAPHERocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_he_rocket.DualHERocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_he_rocket.DualHERocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bomb.HEBombBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bomb.HEBombBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bouncing_bomb.HEBouncingBombBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bouncing_bomb.HEBouncingBombBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_loitering_rocket.HELoiteringRocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_loitering_rocket.HELoiteringRocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_rocket.HERocketBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_rocket.HERocketBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.racked_torpedo.RackedTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.racked_torpedo.RackedTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.medium_range_torpedo.MediumRangeTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.medium_range_torpedo.MediumRangeTorpedoBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_medium_range_torpedo.ReductiveMediumRangeTorpedoBlock;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_medium_range_torpedo.ReductiveMediumRangeTorpedoBlockItem;
import com.simibubi.create.AllTags;
import com.simibubi.create.content.decoration.copycat.CopycatPanelBlock;
import com.simibubi.create.content.kinetics.BlockStressDefaults;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import com.simibubi.create.foundation.data.BuilderTransformers;
import com.simibubi.create.foundation.data.CreateRegistrate;
import com.simibubi.create.foundation.data.SharedProperties;
import com.simibubi.create.content.decoration.copycat.CopycatPanelModel;
import com.tterrag.registrate.builders.BlockBuilder;
import com.tterrag.registrate.util.DataIngredient;
import com.tterrag.registrate.util.entry.BlockEntry;
import com.tterrag.registrate.util.nullness.NonNullSupplier;
import com.tterrag.registrate.util.nullness.NonNullUnaryOperator;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.inferior_he_shell.InferiorHEShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sap_shell.SAPShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.hesh_shell.HESHShellBlockItem;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.CannonRocketProjectileBlockItem;
import com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.TorpedoTubeBlockItem;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.base.CBCDefaultStress;
import rbasamoyai.createbigcannons.cannons.big_cannons.breeches.sliding_breech.SlidingBreechCTBehavior;
import rbasamoyai.createbigcannons.datagen.assets.CBCBuilderTransformers;
import rbasamoyai.createbigcannons.index.CBCBigCannonMaterials;
import rbasamoyai.createbigcannons.index.CBCSpriteShifts;

public class CBCMSBlocks {

	static { ModGroup.useModTab(ModGroup.MAIN_TAB_KEY); }

	//////// Log cannon blocks ////////
	public static final BlockEntry<InferiorHEShellBlock> Inferior_HE_SHELL = REGISTRATE
			.block("inferior_he_shell", InferiorHEShellBlock::new)
			.transform(shell(MapColor.COLOR_RED))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/inferior_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Inferior High Explosive (HE) Shell")
			.item(InferiorHEShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<SAPShellBlock> SAP_SHELL = REGISTRATE
			.block("sap_shell", SAPShellBlock::new)
			.transform(shell(MapColor.COLOR_RED))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/sap_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("SAP Shell")
			.item(SAPShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HESHShellBlock> HESH_SHELL = REGISTRATE
			.block("hesh_shell", HESHShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/hesh_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("HESH Shell")
			.item(HESHShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<ShellessHEShellBlock> SHELLESS_HE_SHELL = REGISTRATE
			.block("shelless_he_shell", ShellessHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/shelless_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Shelless HE Shell")
			.item(ShellessHEProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APHECannonRocketBlock> APHE_CANNON_ROCKET = REGISTRATE
			.block("aphe_cannon_rocket", APHECannonRocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/aphe_cannon_rocket"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APHE Cannon Rocket")
			.item(CannonRocketProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<ShellessAPShotBlock> SHELLESS_AP_SHOT = REGISTRATE
			.block("shelless_ap_shot", ShellessAPShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/shelless_ap_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Shelless AP Shot")
			.item(ShellessAPProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APFSDSShotBlock> APFSDS_SHOT = REGISTRATE
			.block("apfsds_shot", APFSDSShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/apfsds_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APFSDS Shot")
			.item(APFSDSProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<BakedAPFSDSShotBlock> BAKED_APFSDS_SHOT = REGISTRATE
			.block("baked_apfsds_shot", BakedAPFSDSShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/baked_apfsds_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Baked APFSDS Shot")
			.item(BakedAPFSDSProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<BaguetteShotBlock> BAGUETTE_SHOT = REGISTRATE
			.block("baguette_shot", BaguetteShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/baguette_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Baguette Shot")
			.item(BaguetteProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<Block> APFSDS_SHOT_PROJECTILE = REGISTRATE
			.block("apfsds_shot_projectile", Block::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion())
			.blockstate(CBCBuilderTransformers.simpleBlock("block/apfsds_shot_projectile"))
			.simpleItem()
			.register();

	public static final BlockEntry<Block> BAKED_APFSDS_SHOT_PROJECTILE = REGISTRATE
			.block("baked_apfsds_shot_projectile", Block::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion())
			.blockstate(CBCBuilderTransformers.simpleBlock("block/baked_apfsds_shot_projectile"))
			.simpleItem()
			.register();

	public static final BlockEntry<Block> BAGUETTE_SHOT_PROJECTILE = REGISTRATE
			.block("baguette_shot_projectile", Block::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p.noOcclusion())
			.blockstate(CBCBuilderTransformers.simpleBlock("block/baguette_shot_projectile"))
			.simpleItem()
			.register();

	public static final BlockEntry<ShellessSAPShellBlock> SHELLESS_SAP_SHELL = REGISTRATE
			.block("shelless_sap_shell", ShellessSAPShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/shelless_sap_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Shelless SAP Shell")
			.item(ShellessSAPProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HECannonRocketBlock> HE_CANNON_ROCKET = REGISTRATE
			.block("he_cannon_rocket", HECannonRocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/he_cannon_rocket"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("HE Cannon Rocket")
			.item(CannonRocketProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<AntiairHEShellBlock> ANTIAIR_HE_SHELL = REGISTRATE
			.block("antiair_he_shell", AntiairHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/antiair_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Antiair HE Shell")
			.item(AntiairHEShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APBCShotBlock> APBC_SHOT = REGISTRATE
			.block("apbc_shot", APBCShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/apbc_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APBC Shot")
			.item(APBCBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APBCShellBlock> APBC_SHELL = REGISTRATE
			.block("apbc_shell", APBCShellBlock::new)
			.transform(shell(MapColor.COLOR_RED))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/apbc_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("SAP Shell")
			.item(APBCShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APSuperHeavyShotBlock> AP_SUPER_HEAVY_SHOT = REGISTRATE
			.block("ap_super_heavy_shot", APSuperHeavyShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/ap_super_heavy_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("AP Super Heavy Shot")
			.item(APSuperHeavyBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<IncendiaryHEShellBlock> INCENDIARY_HE_SHELL = REGISTRATE
			.block("incendiary_he_shell", IncendiaryHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/incendiary_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Incendiary HE Shell")
			.item(IncendiaryHEProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<ShellessIncendiaryHEShellBlock> SHELLESS_INCENDIARY_HE_SHELL = REGISTRATE
			.block("shelless_incendiary_he_shell", ShellessIncendiaryHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/shelless_incendiary_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Shelless Incendiary HE Shell")
			.item(ShellessIncendiaryHEProjectileBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<AntiairShrapnelShellBlock> ANTIAIR_SHRAPNEL_SHELL = REGISTRATE
			.block("antiair_shrapnel_shell", AntiairShrapnelShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/antiair_shrapnel_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Antiair Shrapnel Shell")
			.item(AntiairShrapnelShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<CannonTorpedoBlock> CANNON_TORPEDO = REGISTRATE
			.block("cannon_torpedo", CannonTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/cannon_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Cannon Torpedo")
			.item(CannonTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<MediumRangeTorpedoBlock> MEDIUM_RANGE_TORPEDO = REGISTRATE
			.block("medium_range_torpedo", MediumRangeTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/medium_range_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Medium Range Torpedo")
			.item(MediumRangeTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<MediumRangeTorpedoTypeBBlock> MEDIUM_RANGE_TORPEDO_TYPEB = REGISTRATE
			.block("medium_range_torpedo_typeb", MediumRangeTorpedoTypeBBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/medium_range_torpedo_typeb"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Medium Range Torpedo Type B")
			.item(MediumRangeTorpedoTypeBBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();


	public static final BlockEntry<LongRangeTorpedoBlock> LONG_RANGE_TORPEDO = REGISTRATE
			.block("long_range_torpedo", LongRangeTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/long_range_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Long Range Torpedo")
			.item(LongRangeTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<MediumRangeDeepwaterTorpedoBlock> MEDIUM_RANGE_DEEPWATER_TORPEDO = REGISTRATE
			.block("medium_range_deepwater_torpedo", MediumRangeDeepwaterTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/medium_range_deepwater_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Medium Range Deepwater Torpedo")
			.item(MediumRangeDeepwaterTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<MediumRangeDeepwaterTorpedoTypeBBlock> MEDIUM_RANGE_DEEPWATER_TORPEDO_TYPEB = REGISTRATE
			.block("medium_range_deepwater_torpedo_typeb", MediumRangeDeepwaterTorpedoTypeBBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/medium_range_deepwater_torpedo_typeb"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Medium Range Deepwater Torpedo")
			.item(MediumRangeDeepwaterTorpedoTypeBBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HighspeedTorpedoBlock> HIGHSPEED_TORPEDO = REGISTRATE
			.block("highspeed_torpedo", HighspeedTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/highspeed_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Highspeed Torpedo")
			.item(HighspeedTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<AirdroppedTorpedoBlock> AIRDROPPED_TORPEDO = REGISTRATE
			.block("airdropped_torpedo", AirdroppedTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/airdropped_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Airdropped Torpedo")
			.item(AirdroppedTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<SharpnelTorpedoBlock> SHARPNEL_TORPEDO = REGISTRATE
			.block("sharpnel_torpedo", SharpnelTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/sharpnel_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Sharpnel Torpedo")
			.item(SharpnelTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<AirdroppedShrapnelTorpedoBlock> AIRDROPPED_SHRAPNEL_TORPEDO = REGISTRATE
			.block("airdropped_shrapnel_torpedo", AirdroppedShrapnelTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/airdropped_shrapnel_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Airdropped Shrapnel Torpedo")
			.item(AirdroppedShrapnelTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<DeepwaterShrapnelTorpedoBlock> DEEPWATER_SHRAPNEL_TORPEDO = REGISTRATE
			.block("deepwater_shrapnel_torpedo", DeepwaterShrapnelTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/deepwater_shrapnel_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Deepwater Shrapnel Torpedo")
			.item(DeepwaterShrapnelTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<LongRangeShrapnelTorpedoBlock> LONG_RANGE_SHRAPNEL_TORPEDO = REGISTRATE
			.block("long_range_shrapnel_torpedo", LongRangeShrapnelTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/long_range_shrapnel_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Long Range Shrapnel Torpedo")
			.item(LongRangeShrapnelTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<ReductiveMediumRangeTorpedoBlock> REDUCTIVE_MEDIUM_RANGE_TORPEDO = REGISTRATE
			.block("reductive_medium_range_torpedo", ReductiveMediumRangeTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/reductive_medium_range_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Medium Range Torpedo")
			.item(ReductiveMediumRangeTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HEBombBlock> HE_BOMB = REGISTRATE
			.block("he_bomb", HEBombBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/he_bomb"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("HE Bomb")
			.item(HEBombBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HEBouncingBombBlock> HE_BOUNCING_BOMB = REGISTRATE
			.block("he_bouncing_bomb", HEBouncingBombBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/he_bouncing_bomb"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("HE Bouncing Bomb")
			.item(HEBouncingBombBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<RackedTorpedoBlock> RACKED_TORPEDO = REGISTRATE
			.block("racked_torpedo", RackedTorpedoBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/racked_torpedo"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Racked Torpedo")
			.item(RackedTorpedoBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HERocketBlock> HE_ROCKET = REGISTRATE
			.block("he_rocket", HERocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/he_rocket"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("HE Rocket")
			.item(HERocketBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APHEBombBlock> APHE_BOMB = REGISTRATE
			.block("aphe_bomb", APHEBombBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/aphe_bomb"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APHE Bomb")
			.item(APHEBombBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APHEBouncingBombBlock> APHE_BOUNCING_BOMB = REGISTRATE
			.block("aphe_bouncing_bomb", APHEBouncingBombBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/aphe_bouncing_bomb"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APHE Bouncing Bomb")
			.item(APHEBouncingBombBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APHERocketBlock> APHE_ROCKET = REGISTRATE
			.block("aphe_rocket", APHERocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/aphe_rocket"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APHE Rocket")
			.item(APHERocketBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<DualHERocketBlock> DUAL_HE_ROCKET = REGISTRATE
			.block("dual_he_rocket", DualHERocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/dual_he_rocket"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Dual HE Rocket")
			.item(DualHERocketBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<DualAPHERocketBlock> DUAL_APHE_ROCKET = REGISTRATE
			.block("dual_aphe_rocket", DualAPHERocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/dual_aphe_rocket"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Dual APHE Rocket")
			.item(DualAPHERocketBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<DepthChargeBlock> DEPTH_CHARGE = REGISTRATE
			.block("depth_charge", DepthChargeBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/depth_charge"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Depth Charge")
			.item(DepthChargeBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<HELoiteringRocketBlock> HE_LOITERING_ROCKET = REGISTRATE
			.block("beef_noodle", HELoiteringRocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/beef_noodle"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("HE Rocket")
			.item(HELoiteringRocketBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<APHELoiteringRocketBlock> APHE_LOITERING_ROCKET = REGISTRATE
			.block("bubble_drink", APHELoiteringRocketBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/bubble_drink"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("APHE Rocket")
			.item(APHELoiteringRocketBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();



	public static final BlockEntry<NormalAPShotBlock> NORMAL_AP_SHOT = REGISTRATE
			.block("normal_ap_shot", NormalAPShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_ap_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal AP Shot")
			.item(NormalAPShotBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<NormalHEShellBlock> NORMAL_HE_SHELL = REGISTRATE
			.block("normal_he_shell", NormalHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal HE Shell")
			.item(NormalHEShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<NormalSAPShellBlock> NORMAL_SAP_SHELL = REGISTRATE
			.block("normal_sap_shell", NormalSAPShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_sap_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal SAP Shell")
			.item(NormalSAPShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<NormalAPShellBlock> NORMAL_AP_SHELL = REGISTRATE
			.block("normal_ap_shell", NormalAPShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_ap_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal AP Shell")
			.item(NormalAPShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<NormalAPBCShellBlock> NORMAL_APBC_SHELL = REGISTRATE
			.block("normal_apbc_shell", NormalAPBCShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_apbc_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal APBC Shell")
			.item(NormalAPBCShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<ExtendedAPShotBlock> EXTENDED_AP_SHOT = REGISTRATE
			.block("extended_ap_shot", ExtendedAPShotBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/extended_ap_shot"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Extended AP Shot")
			.item(ExtendedAPShotBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<NormalAntiairHEShellBlock> NORMAL_ANTIAIR_HE_SHELL = REGISTRATE
			.block("normal_antiair_he_shell", NormalAntiairHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_antiair_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal Antiair HE Shell")
			.item(NormalAntiairHEShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<NormalIncendiaryHEShellBlock> NORMAL_INCENDIARY_HE_SHELL = REGISTRATE
			.block("normal_incendiary_he_shell", NormalIncendiaryHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/normal_incendiary_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Normal Incendiary HE Shell")
			.item(NormalIncendiaryHEShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();

	public static final BlockEntry<ExtendedAntiairHEShellBlock> EXTENDED_ANTIAIR_HE_SHELL = REGISTRATE
			.block("extended_antiair_he_shell", ExtendedAntiairHEShellBlock::new)
			.transform(shell(MapColor.COLOR_YELLOW))
			.transform(axeOrPickaxe())
			.transform(CBCBuilderTransformers.projectile("projectile/extended_antiair_he_shell"))
			.transform(CBCBuilderTransformers.safeNbt())
			.loot(CBCBuilderTransformers.shellLoot())
			.lang("Extended Antiair HE Shell")
			.item(NormalAntiairHEShellBlockItem::new)
			.tag(CBCTags.CBCItemTags.BIG_CANNON_PROJECTILES)
			.build()
			.register();




	public static final BlockEntry<Block> NORMAL_AP_SHOT_BLOCK = REGISTRATE
			.block("normal_ap_shot_block", Block::new)
			.register();

	public static final BlockEntry<Block> NORMAL_HE_SHELL_BLOCK = REGISTRATE
			.block("normal_he_shell_block", Block::new)
			.register();

	public static final BlockEntry<Block> NORMAL_SAP_SHELL_BLOCK = REGISTRATE
			.block("normal_sap_shell_block", Block::new)
			.register();

	public static final BlockEntry<Block> NORMAL_AP_SHELL_BLOCK = REGISTRATE
			.block("normal_ap_shell_block", Block::new)
			.register();

	public static final BlockEntry<Block> NORMAL_APBC_SHELL_BLOCK = REGISTRATE
			.block("normal_apbc_shell_block", Block::new)
			.register();

	public static final BlockEntry<Block> NORMAL_ANTIAIR_HE_SHELL_BLOCK = REGISTRATE
			.block("normal_antiair_he_shell_block", Block::new)
			.register();

	public static final BlockEntry<Block> NORMAL_INCENDIARY_HE_SHELL_BLOCK = REGISTRATE
			.block("normal_incendiary_he_shell_block", Block::new)
			.register();





	// Torpedo Blocks
	public static final BlockEntry<TorpedoTubeBodyBlock> STEEL_TORPEDO_BARREL = REGISTRATE
			.block("steel_torpedo_tube_barrel", p -> TorpedoTubeBodyBlock.verySmall(p, CBCMSTorpedoTubeMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.METAL))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(TorpedoTubeBlockItem::new).build()
			.register();

	public static final BlockEntry<TorpedoTubeBodyBlock> STEEL_TORPEDO_CHAMBER = REGISTRATE
			.block("steel_torpedo_tube_chamber", p -> TorpedoTubeBodyBlock.medium(p, CBCMSTorpedoTubeMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.METAL))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(TorpedoTubeBlockItem::new).build()
			.register();

	public static final BlockEntry<TorpQuickfiringBreechBlock> STEEL_TORPEDO_QUICKFIRING_BREECH = REGISTRATE
			.block("steel_torpedo_quickfiring_breech", p -> new TorpQuickfiringBreechBlock(p, CBCMSTorpedoTubeMaterials.STEEL, steelSlidingBreech()))
			.lang("Steel Torpedo Quick-Firing Breech")
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.torpedoSlidingBreech("torpedo_sliding_breech/steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.METAL))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() ->
					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.item(TorpedoTubeBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> steelSlidingBreech() {
		return STEEL_TORPEDO_SLIDING_BREECH;
	}

	public static final BlockEntry<TorpedoSlidingBreechBlock> STEEL_TORPEDO_SLIDING_BREECH = REGISTRATE
			.block("steel_torpedo_sliding_breech", p -> new TorpedoSlidingBreechBlock(p, CBCMSTorpedoTubeMaterials.STEEL, STEEL_TORPEDO_QUICKFIRING_BREECH))
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			//.transform(CBCMSBuilderTransformers.torpedoSlidingBreech("torpedo_sliding_breech/steel"))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.onRegister(CreateRegistrate.connectedTextures(() ->
					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.register();

	public static final BlockEntry<CopycatPanelBlock> ANTIBLAST_COPYCAT_PANEL =
			REGISTRATE.block("antiblast_panel", CopycatPanelBlock::new)
					.transform(BuilderTransformers.copycat())
					.onRegister(CreateRegistrate.blockModel(() -> CopycatPanelModel::new))
					.item()
					.transform(customItemModel("copycat_base", "panel"))
					.properties(p -> p.strength(5.0f))
					.properties(p -> p.explosionResistance(24f))
					.properties(BlockBehaviour.Properties::noCollission)
					.register();

	public static final BlockEntry<LootBarrelBlock> LOOT_BARREL = REGISTRATE
			.block("loot_barrel", LootBarrelBlock::new)
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.WOOD))
			.item().build()
			.register();

	public static final BlockEntry<NethersteelQuickfiringBreechBlock> NETHERSTEEL_QUICKFIRING_BREECH = REGISTRATE
			.block("nethersteel_quickfiring_breech", p -> new NethersteelQuickfiringBreechBlock(p, CBCBigCannonMaterials.NETHERSTEEL, nethersteelSlidingBreech()))
			.lang("Nethersteel Quick-Firing Breech")
			.properties(p -> p.strength(4.0f,16f))
			.transform(strongCannonBlock(false))
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/nethersteel"))
			.onRegister(CreateRegistrate.connectedTextures(() ->
					new SlidingBreechCTBehavior(CBCMSSpriteShifts.NETHERSTEEL_SLIDING_BREECH_SIDE, CBCMSSpriteShifts.NETHERSTEEL_SLIDING_BREECH_SIDE_HOLE)))
			.register();

	public static final BlockEntry<NethersteelSlidingBreechBlock> NETHERSTEEL_SLIDING_BREECH = REGISTRATE
			.block("nethersteel_sliding_breech", p -> new NethersteelSlidingBreechBlock(p, CBCBigCannonMaterials.NETHERSTEEL, NETHERSTEEL_QUICKFIRING_BREECH))
			.transform(strongCannonBlock(false))
			.properties(p -> p.strength(4.0f,16f))
			.transform(CBCBuilderTransformers.slidingBreech("sliding_breech/nethersteel"))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.onRegister(CreateRegistrate.connectedTextures(() ->
					new SlidingBreechCTBehavior(CBCMSSpriteShifts.NETHERSTEEL_SLIDING_BREECH_SIDE, CBCMSSpriteShifts.NETHERSTEEL_SLIDING_BREECH_SIDE_HOLE)))
			.register();

	public static final BlockEntry<ProjectileRackBodyBlock> STEEL_PROJECTILE_RACK_BARREL = REGISTRATE
			.block("steel_projectile_rack_barrel", p -> ProjectileRackBodyBlock.verySmall(p, CBCMSProjectileRackMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(ProjectileRackBlockItem::new).build()
			.register();

	public static final BlockEntry<ProjectileRackBodyBlock> STEEL_PROJECTILE_RACK_STABILIZER = REGISTRATE
			.block("steel_projectile_rack_stabilizer", p -> ProjectileRackBodyBlock.normalRack(p, CBCMSProjectileRackMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(ProjectileRackBlockItem::new).build()
			.register();

	public static final BlockEntry<ProjectileRackBodyBlock> STEEL_PROJECTILE_RACK_CHAMBER = REGISTRATE
			.block("steel_projectile_rack_chamber", p -> ProjectileRackBodyBlock.normalRack(p, CBCMSProjectileRackMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(ProjectileRackBlockItem::new).build()
			.register();

	public static final BlockEntry<ProjectileRackQuickfiringBreechBlock> STEEL_PROJECTILE_RACK_QUICKFIRING_BREECH = REGISTRATE
			.block("steel_projectile_rack_quickfiring_breech", p -> new ProjectileRackQuickfiringBreechBlock(p, CBCMSProjectileRackMaterials.STEEL, steelSlidingBreech()))
			.lang("Steel Projectile Rack Quick-Firing Breech")
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.projectileRackSlidingBreech("projectile_rack_sliding_breech/steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
//			.onRegister(CreateRegistrate.connectedTextures(() ->
//					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.item(ProjectileRackBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> steelProjectileRackSlidingBreech() {
		return STEEL_PROJECTILE_RACK_SLIDING_BREECH;
	}

	public static final BlockEntry<ProjectileRackSlidingBreechBlock> STEEL_PROJECTILE_RACK_SLIDING_BREECH = REGISTRATE
			.block("steel_projectile_rack_sliding_breech", p -> new ProjectileRackSlidingBreechBlock(p, CBCMSProjectileRackMaterials.STEEL, STEEL_TORPEDO_QUICKFIRING_BREECH))
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			//.transform(CBCMSBuilderTransformers.torpedoSlidingBreech("torpedo_sliding_breech/steel"))
			.transform(CBCDefaultStress.setImpact(32.0d))
//			.onRegister(CreateRegistrate.connectedTextures(() ->
//					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.register();











	public static final BlockEntry<DualCannonBodyBlock> STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("steel_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("steel_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> STEEL_DUAL_CANNON_CHARGER = REGISTRATE
			.block("steel_dual_cannon_charger", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.STEEL, steelDualSlidingBreech()))
			.lang("Steel Dual Cannon Quick-Firing Breech")
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
//			.onRegister(CreateRegistrate.connectedTextures(() ->
//					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> steelDualSlidingBreech() {
		return STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.STEEL, STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			//.transform(CBCMSBuilderTransformers.torpedoSlidingBreech("torpedo_sliding_breech/steel"))
			.transform(CBCDefaultStress.setImpact(32.0d))
//			.onRegister(CreateRegistrate.connectedTextures(() ->
//					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> STEEL_SINGLE_CANNON_BARREL = REGISTRATE
			.block("steel_single_cannon_barrel", p -> DualCannonBodyBlock.singleSmall(p, CBCMSDualCannonMaterials.SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("single_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> STEEL_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("steel_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("single_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> STEEL_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("steel_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_STEEL, steelSingleSlidingBreech()))
			.lang("Steel Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/single_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> steelSingleSlidingBreech() {
		return STEEL_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> STEEL_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("steel_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.SINGLE_STEEL, STEEL_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_steel_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_steel_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_STEEL_DUAL_CANNON_CHARGER = REGISTRATE
			.block("wide_steel_dual_cannon_charger", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_STEEL, wideSteelDualSlidingBreech()))
			.lang("Wide Steel Dual Cannon Quick-Firing Breech")
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
//			.onRegister(CreateRegistrate.connectedTextures(() ->
//					new SlidingBreechCTBehavior(CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE, CBCSpriteShifts.STEEL_SLIDING_BREECH_SIDE_HOLE)))
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideSteelDualSlidingBreech() {
		return WIDE_STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_STEEL, WIDE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("large_steel_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.LARGE_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("large_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("large_steel_dual_cannon_chamber", p -> DualCannonBodyBlock.large(p, CBCMSDualCannonMaterials.LARGE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("large_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_STEEL_DUAL_CANNON_CHARGER = REGISTRATE
			.block("large_steel_dual_cannon_charger", p -> DualCannonBodyBlock.large(p, CBCMSDualCannonMaterials.LARGE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("large_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> LARGE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("large_steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.large(p, CBCMSDualCannonMaterials.LARGE_STEEL, largeSteelDualSlidingBreech()))
			//.lang("Large Steel Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/large_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> largeSteelDualSlidingBreech() {
		return LARGE_STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> LARGE_STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("large_steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.LARGE_STEEL, LARGE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_STEEL_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_steel_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_STEEL_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_steel_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_steel_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_STEEL, wideSteelSingleSlidingBreech()))
			.lang("Wide Steel Single Cannon Quick-Firing Breech")
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideSteelSingleSlidingBreech() {
		return WIDE_STEEL_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_STEEL_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_steel_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_STEEL, WIDE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_STEEL_SINGLE_CANNON_BARREL = REGISTRATE
			.block("large_steel_single_cannon_barrel", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("large_single_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_STEEL_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("large_steel_single_cannon_chamber", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("large_single_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_STEEL_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("large_steel_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("large_single_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.STEEL_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> LARGE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("large_steel_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_STEEL, largeSteelSingleSlidingBreech()))
			//.lang("Wide Steel Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/large_single_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> largeSteelSingleSlidingBreech() {
		return LARGE_STEEL_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> LARGE_STEEL_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("large_steel_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.LARGE_SINGLE_STEEL, LARGE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();



	public static final BlockEntry<DualCannonBodyBlock> CAST_IRON_DUAL_CANNON_BARREL = REGISTRATE
			.block("cast_iron_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.CAST_IRON))
			.transform(CBCBuilderTransformers.cannonBarrel("cast_iron", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> CAST_IRON_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("cast_iron_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.CAST_IRON))
			.transform(CBCBuilderTransformers.cannonChamber("cast_iron", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> CAST_IRON_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("cast_iron_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.CAST_IRON, castIronDualSlidingBreech()))
			.lang("Cast Iron Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/cast_iron"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> castIronDualSlidingBreech() {
		return CAST_IRON_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> CAST_IRON_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("cast_iron_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.CAST_IRON, CAST_IRON_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_CAST_IRON_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_cast_iron_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_CAST_IRON))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_cast_iron", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_CAST_IRON_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_cast_iron_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_CAST_IRON))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_cast_iron", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_CAST_IRON_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_cast_iron_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_CAST_IRON, wideCastIronSingleSlidingBreech()))
			.lang("Wide Cast Iron Single Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_cast_iron"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideCastIronSingleSlidingBreech() {
		return WIDE_CAST_IRON_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_CAST_IRON_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_cast_iron_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_CAST_IRON, WIDE_CAST_IRON_SINGLE_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRONZE_DUAL_CANNON_BARREL = REGISTRATE
			.block("bronze_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.BRONZE))
			.transform(CBCBuilderTransformers.cannonBarrel("bronze", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRONZE_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("bronze_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.BRONZE))
			.transform(CBCBuilderTransformers.cannonChamber("bronze", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> BRONZE_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("bronze_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.BRONZE, bronzeDualSlidingBreech()))
			.lang("Bronze Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/bronze"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> bronzeDualSlidingBreech() {
		return BRONZE_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> BRONZE_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("bronze_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.BRONZE, BRONZE_DUAL_CANNON_QUICKFIRING_BREECH))
			//.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRONZE_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_bronze_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRONZE))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_bronze", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRONZE_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_bronze_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRONZE))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_bronze", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_BRONZE_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_bronze_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRONZE, wideBronzeSingleSlidingBreech()))
			.lang("Bronze Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_bronze"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideBronzeSingleSlidingBreech() {
		return WIDE_BRONZE_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_BRONZE_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_bronze_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRONZE, WIDE_BRONZE_SINGLE_CANNON_QUICKFIRING_BREECH))
			//.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRASS_DUAL_CANNON_BARREL = REGISTRATE
			.block("brass_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRASS_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("brass_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRASS_DUAL_CANNON_CHARGER = REGISTRATE
			.block("brass_dual_cannon_charger", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> BRASS_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("brass_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.BRASS, brassDualSlidingBreech()))
			.lang("Brass Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> brassDualSlidingBreech() {
		return BRASS_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> BRASS_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("brass_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.BRASS, BRASS_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRASS_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_brass_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRASS_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_brass_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("wide_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRASS_DUAL_CANNON_CHARGER = REGISTRATE
			.block("wide_brass_dual_cannon_charger", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("wide_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_brass_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_BRASS, widebrassDualSlidingBreech()))
			.lang("Wide Brass Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> widebrassDualSlidingBreech() {
		return WIDE_BRASS_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_BRASS_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_brass_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_BRASS, WIDE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_BRASS_DUAL_CANNON_BARREL = REGISTRATE
			.block("large_brass_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.LARGE_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("large_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_BRASS_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("large_brass_dual_cannon_chamber", p -> DualCannonBodyBlock.large(p, CBCMSDualCannonMaterials.LARGE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("large_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_BRASS_DUAL_CANNON_CHARGER = REGISTRATE
			.block("large_brass_dual_cannon_charger", p -> DualCannonBodyBlock.large(p, CBCMSDualCannonMaterials.LARGE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("large_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> LARGE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("large_brass_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.large(p, CBCMSDualCannonMaterials.LARGE_BRASS, largeBrassDualSlidingBreech()))
			//.lang("Large Brass Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/large_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> largeBrassDualSlidingBreech() {
		return LARGE_BRASS_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> LARGE_BRASS_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("large_brass_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.LARGE_BRASS, LARGE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRASS_SINGLE_CANNON_BARREL = REGISTRATE
			.block("brass_single_cannon_barrel", p -> DualCannonBodyBlock.singleSmall(p, CBCMSDualCannonMaterials.SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("single_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> BRASS_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("brass_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("single_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> BRASS_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("brass_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_BRASS, brassSingleSlidingBreech()))
			.lang("Brass Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/single_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> brassSingleSlidingBreech() {
		return BRASS_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> BRASS_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("brass_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.SINGLE_BRASS, BRASS_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRASS_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_brass_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRASS_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_brass_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_BRASS_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("wide_brass_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_brass_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRASS, wideBrassSingleSlidingBreech()))
			.lang("Wide Brass Single Cannon Quick-Firing Breech")
			//.transform(strongCannonBlock(false))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideBrassSingleSlidingBreech() {
		return WIDE_BRASS_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_BRASS_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_brass_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_BRASS, WIDE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_BRASS_SINGLE_CANNON_BARREL = REGISTRATE
			.block("large_brass_single_cannon_barrel", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("large_single_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_BRASS_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("large_brass_single_cannon_chamber", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("large_single_brass", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_BRASS_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("large_brass_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("large_single_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.BRASS_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> LARGE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("large_brass_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_BRASS, largeBrassSingleSlidingBreech()))
			//.lang("Wide Brass Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/large_single_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> largeBrassSingleSlidingBreech() {
		return LARGE_BRASS_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> LARGE_BRASS_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("large_brass_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.LARGE_SINGLE_BRASS, LARGE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> ROSEQUARTZ_BRASS_DUAL_CANNON_BARREL = REGISTRATE
			.block("rosequartz_brass_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.ROSEQUARTZ_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("rosequartz_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.ROSEQUARTZ_BRASS_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> ROSEQUARTZ_BRASS_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("rosequartz_brass_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.ROSEQUARTZ_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("rosequartz_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.ROSEQUARTZ_BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> ROSEQUARTZ_BRASS_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("rosequartz_brass_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.ROSEQUARTZ_BRASS, rosequartzBrassDualSlidingBreech()))
			.lang("Rosequartz Brass Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/rosequartz_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> rosequartzBrassDualSlidingBreech() {
		return ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("rosequartz_brass_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.ROSEQUARTZ_BRASS, ROSEQUARTZ_BRASS_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_rosequartz_brass_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_ROSEQUARTZ_BRASS))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_rosequartz_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.ROSEQUARTZ_BRASS_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_rosequartz_brass_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_ROSEQUARTZ_BRASS))
			.transform(CBCBuilderTransformers.cannonChamber("wide_rosequartz_brass", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.ROSEQUARTZ_BRASS_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_rosequartz_brass_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_ROSEQUARTZ_BRASS, widerosequartzBrassDualSlidingBreech()))
			.lang("Rosequartz Brass Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_rosequartz_brass"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> widerosequartzBrassDualSlidingBreech() {
		return WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_rosequartz_brass_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_ROSEQUARTZ_BRASS, WIDE_ROSEQUARTZ_BRASS_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> TOUGH_STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("tough_steel_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> TOUGH_STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("tough_steel_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("tough_steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.TOUGH_STEEL, toughSteelDualSlidingBreech()))
			.lang("Tough Steel Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/tough_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> toughSteelDualSlidingBreech() {
		return TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("tough_steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.TOUGH_STEEL, TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_TOUGH_STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_tough_steel_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_TOUGH_STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_tough_steel_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_tough_steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_TOUGH_STEEL, wideToughSteelDualSlidingBreech()))
			.lang("Wide Tough Steel Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_tough_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideToughSteelDualSlidingBreech() {
		return WIDE_TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_TOUGH_STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_tough_steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL, WIDE_TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	//

	public static final BlockEntry<DualCannonBodyBlock> WIDE_TOUGH_STEEL_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_tough_steel_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_tough_steel_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("wide_tough_steel_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_tough_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.TOUGH_STEEL_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_TOUGH_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_tough_steel_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL, wideToughSteelSingleSlidingBreech()))
			.lang("Wide Tough Steel Single Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_tough_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideToughSteelSingleSlidingBreech() {
		return WIDE_TOUGH_STEEL_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_TOUGH_STEEL_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_tough_steel_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_TOUGH_STEEL, WIDE_TOUGH_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	//

	public static final BlockEntry<DualCannonBodyBlock> NETHER_STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("nether_steel_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.NETHER_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("nether_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.NETHER_STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> NETHER_STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("nether_steel_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.NETHER_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("nether_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.NETHER_STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("nether_steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.NETHER_STEEL, netherSteelDualSlidingBreech()))
			.lang("Nether Steel Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/nether_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> netherSteelDualSlidingBreech() {
		return NETHER_STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> NETHER_STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("nether_steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.NETHER_STEEL, NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_NETHER_STEEL_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_nether_steel_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_NETHER_STEEL))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_nether_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.NETHER_STEEL_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_NETHER_STEEL_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_nether_steel_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_NETHER_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_nether_steel", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.NETHER_STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_NETHER_STEEL_DUAL_CANNON_CHARGER = REGISTRATE
			.block("wide_nether_steel_dual_cannon_charger", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_NETHER_STEEL))
			.transform(CBCBuilderTransformers.cannonChamber("wide_nether_steel", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.NETHER_STEEL_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_nether_steel_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_NETHER_STEEL, widenetherSteelDualSlidingBreech()))
			.lang("Nether Steel Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_nether_steel"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> widenetherSteelDualSlidingBreech() {
		return WIDE_NETHER_STEEL_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_NETHER_STEEL_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_nether_steel_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_NETHER_STEEL, WIDE_NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL = REGISTRATE
			.block("military_slate_alloy_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("military_slate_alloy_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("military_slate_alloy_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.MILITARY_SLATE_ALLOY, militarySlateAlloySteelDualSlidingBreech()))
			.lang("Military Slate Alloy Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/military_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> militarySlateAlloySteelDualSlidingBreech() {
		return MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("military_slate_alloy_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.MILITARY_SLATE_ALLOY, MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	//

	public static final BlockEntry<DualCannonBodyBlock> MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL = REGISTRATE
			.block("military_slate_alloy_single_cannon_barrel", p -> DualCannonBodyBlock.singleSmall(p, CBCMSDualCannonMaterials.SINGLE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("single_military_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("military_slate_alloy_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("single_military_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("military_slate_alloy_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_MILITARY_SLATE_ALLOY, militarySlateAlloySingleSlidingBreech()))
			.lang("Military Slate Alloy Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/single_military_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> militarySlateAlloySingleSlidingBreech() {
		return MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("military_slate_alloy_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.SINGLE_MILITARY_SLATE_ALLOY, MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();
	//

	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_military_slate_alloy_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_military_slate_alloy_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHARGER = REGISTRATE
			.block("wide_military_slate_alloy_dual_cannon_charger", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_military_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("wide_military_slate_alloy_dual_cannon_chamber_shielded", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_military_slate_alloy_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY, wideMilitarySlateAlloySteelDualSlidingBreech()))
			.lang("Wide Military Slate Alloy Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_military_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideMilitarySlateAlloySteelDualSlidingBreech() {
		return WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_military_slate_alloy_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_MILITARY_SLATE_ALLOY, WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	//
	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_military_slate_alloy_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_military_slate_alloy_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("wide_military_slate_alloy_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_MILITARY_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_military_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_military_slate_alloy_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_MILITARY_SLATE_ALLOY, wideMilitarySlateAlloySingleSlidingBreech()))
			.lang("Wide military slate alloy Single Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_military_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideMilitarySlateAlloySingleSlidingBreech() {
		return WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_military_slate_alloy_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_MILITARY_SLATE_ALLOY, WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();
	//

	public static final BlockEntry<DualCannonBodyBlock> SLATE_ALLOY_DUAL_CANNON_BARREL = REGISTRATE
			.block("slate_alloy_dual_cannon_barrel", p -> DualCannonBodyBlock.verySmall(p, CBCMSDualCannonMaterials.SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> SLATE_ALLOY_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("slate_alloy_dual_cannon_chamber", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("slate_alloy_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.medium(p, CBCMSDualCannonMaterials.SLATE_ALLOY, slateAlloySteelDualSlidingBreech()))
			.lang("Slate Alloy Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> slateAlloySteelDualSlidingBreech() {
		return SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("slate_alloy_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.SLATE_ALLOY, SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> SLATE_ALLOY_SINGLE_CANNON_BARREL = REGISTRATE
			.block("slate_alloy_single_cannon_barrel", p -> DualCannonBodyBlock.singleSmall(p, CBCMSDualCannonMaterials.SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("single_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> SLATE_ALLOY_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("slate_alloy_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("single_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> SLATE_ALLOY_DUAL_CANNON_CHARGER = REGISTRATE
			.block("slate_alloy_dual_cannon_charger", p -> DualCannonBodyBlock.medium(p, CBCMSDualCannonMaterials.SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("slate_alloy_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleMedium(p, CBCMSDualCannonMaterials.SINGLE_SLATE_ALLOY, slateAlloySingleSlidingBreech()))
			.lang("Slate Alloy Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/single_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> slateAlloySingleSlidingBreech() {
		return SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("slate_alloy_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.SINGLE_SLATE_ALLOY, SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_DUAL_CANNON_BARREL = REGISTRATE
			.block("wide_slate_alloy_dual_cannon_barrel", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER = REGISTRATE
			.block("wide_slate_alloy_dual_cannon_chamber", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_DUAL_CANNON_CHARGER = REGISTRATE
			.block("wide_slate_alloy_dual_cannon_charger", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonChargerBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("wide_slate_alloy_dual_cannon_chamber_shielded", p -> DualCannonBodyBlock.wide(p, CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(DualCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_slate_alloy_dual_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.wide(p, CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY, wideSlateAlloySteelDualSlidingBreech()))
			.lang("Wide Slate Alloy Dual Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(DualCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideSlateAlloySteelDualSlidingBreech() {
		return WIDE_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_SLATE_ALLOY_DUAL_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_slate_alloy_dual_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SLATE_ALLOY, WIDE_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	//
	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_SINGLE_CANNON_BARREL = REGISTRATE
			.block("wide_slate_alloy_single_cannon_barrel", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("wide_single_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("wide_slate_alloy_single_cannon_chamber", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("wide_slate_alloy_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleMedium(p, CBCMSDualCannonMaterials.WIDE_SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("wide_single_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> WIDE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("wide_slate_alloy_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleWide(p, CBCMSDualCannonMaterials.WIDE_SINGLE_SLATE_ALLOY, wideSlateAlloySingleSlidingBreech()))
			.lang("Wide slate alloy Single Cannon Quick-Firing Breech")
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/wide_single_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> wideSlateAlloySingleSlidingBreech() {
		return WIDE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> WIDE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("wide_slate_alloy_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.WIDE_SINGLE_SLATE_ALLOY, WIDE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_SLATE_ALLOY_SINGLE_CANNON_BARREL = REGISTRATE
			.block("large_slate_alloy_single_cannon_barrel", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonBarrel("large_single_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_BARREL)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER = REGISTRATE
			.block("large_slate_alloy_single_cannon_chamber", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("large_single_slate_alloy", true))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonBodyBlock> LARGE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED = REGISTRATE
			.block("large_slate_alloy_single_cannon_chamber_shielded", p -> DualCannonBodyBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_SLATE_ALLOY))
			.transform(CBCBuilderTransformers.cannonChamber("large_single_slate_alloy", true))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.onRegister(CreateRegistrate.connectedTextures(() -> new DualCannonCTBehavior(CBCMSSpriteShifts.SLATE_ALLOY_DUAL_CANNON_CHAMBER)))
			.item(SingleCannonBlockItem::new).build()
			.register();

	public static final BlockEntry<DualCannonQuickfiringBreechBlock> LARGE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH = REGISTRATE
			.block("large_slate_alloy_single_cannon_quickfiring_breech", p -> DualCannonQuickfiringBreechBlock.singleLarge(p, CBCMSDualCannonMaterials.LARGE_SINGLE_SLATE_ALLOY, largeSlateAlloySingleSlidingBreech()))
			//.lang("Wide Slate Alloy Single Cannon Quick-Firing Breech")
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCMSBuilderTransformers.dualCannonSlidingBreech("dual_cannon_sliding_breech/large_single_slate_alloy"))
			.properties(p -> p.strength(5.0f,14f))
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
			.tag(BlockTags.MINEABLE_WITH_PICKAXE)
			.item(SingleCannonBlockItem::new).build()
			.register();

	private static NonNullSupplier<? extends Block> largeSlateAlloySingleSlidingBreech() {
		return LARGE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH;
	}

	public static final BlockEntry<DualCannonSlidingBreechBlock> LARGE_SLATE_ALLOY_SINGLE_CANNON_SLIDING_BREECH = REGISTRATE
			.block("large_slate_alloy_single_cannon_sliding_breech", p -> new DualCannonSlidingBreechBlock(p, CBCMSDualCannonMaterials.LARGE_SINGLE_SLATE_ALLOY, LARGE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH))
			.loot(CBCBuilderTransformers.steelScrapLoot(10))
			.transform(CBCDefaultStress.setImpact(32.0d))
			.register();
	//



	public static final BlockEntry<TorpedoDetectionDeviceBlock> TORPEDO_DETECTION_DEVICE = REGISTRATE
			.block("torpedo_detection_device", TorpedoDetectionDeviceBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p
					.mapColor(MapColor.DEEPSLATE)
					.requiresCorrectToolForDrops()
					.strength(4f,16f)
					.noOcclusion())
			.addLayer(() -> RenderType::cutoutMipped)
			.transform(BlockStressDefaults.setImpact(32.0f))
			.transform(axeOrPickaxe())
			.item(TorpedoDetectionDeviceBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<AmmoRackBlock> AMMO_RACK = REGISTRATE.block("ammo_rack", AmmoRackBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).noOcclusion())
			.item(AmmoRackBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<AmmoRackBlock> STEEL_AMMO_RACK = REGISTRATE.block("steel_ammo_rack", AmmoRackBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).noOcclusion())
			.item(AmmoRackBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<DishPlateBlock> DISH_PLATE = REGISTRATE.block("dish_plate", DishPlateBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).noOcclusion())
			.item(DishPlateBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<DishPlateBlock> ROUND_DISH_PLATE = REGISTRATE.block("round_dish_plate", DishPlateBlock::new)
			.initialProperties(SharedProperties::softMetal)
			.properties(p -> p.sound(SoundType.NETHERITE_BLOCK).noOcclusion())
			.item(DishPlateBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<LandingIndicatorBlock> LANDING_INDICATOR = REGISTRATE
			.block("landing_indicator", LandingIndicatorBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p
					.mapColor(MapColor.DEEPSLATE)
					.requiresCorrectToolForDrops()
					.strength(4f,19f)
					.noOcclusion())
			//.addLayer(() -> RenderType::cutoutMipped)
			.transform(axeOrPickaxe())
			.item(LandingIndicatorBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<CommandDisplayerBlock> COMMAND_DISPLAYER = REGISTRATE
			.block("command_displayer", CommandDisplayerBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p
					.mapColor(MapColor.DEEPSLATE)
					.requiresCorrectToolForDrops()
					.strength(4f,19f)
					.noOcclusion())
			.transform(axeOrPickaxe())
			.item(CommandDisplayerBlockItem::new)
			.transform(customItemModel())
			.register();

	public static final BlockEntry<CommandDeployerBlock> COMMAND_DEPLOYER = REGISTRATE
			.block("command_deployer", CommandDeployerBlock::new)
			.initialProperties(SharedProperties::stone)
			.properties(p -> p
					.mapColor(MapColor.DEEPSLATE)
					.requiresCorrectToolForDrops()
					.strength(4f,19f)
					.noOcclusion())
			.addLayer(() -> RenderType::translucent)
			.transform(axeOrPickaxe())
			.item(CommandDeployerBlockItem::new)
			.transform(customItemModel())
			.register();



	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> shell(MapColor color) {
		return b -> b.addLayer(() -> RenderType::solid)
				.properties(p -> p.mapColor(color))
				.properties(p -> p.strength(2.0f, 3.0f))
				.properties(p -> p.sound(SoundType.STONE));
	}

	private static <T extends Block, P> NonNullUnaryOperator<BlockBuilder<T, P>> strongCannonBlock(boolean canPassThrough) {
		NonNullUnaryOperator<BlockBuilder<T, P>> transform = b -> b.properties(p -> p.strength(50.0f, 1200.0f))
				.properties(p -> p.sound(SoundType.NETHERITE_BLOCK))
				.properties(p -> p.requiresCorrectToolForDrops())
				.tag(BlockTags.MINEABLE_WITH_PICKAXE)
				.tag(BlockTags.NEEDS_DIAMOND_TOOL);
		return canPassThrough ? transform.andThen(b -> b.tag(CBCTags.CBCBlockTags.DRILL_CAN_PASS_THROUGH)) : transform;
	}

	private static NonNullSupplier<? extends Block> nethersteelSlidingBreech() {
		return NETHERSTEEL_SLIDING_BREECH;
	}


	public static void register() {
	}

}
