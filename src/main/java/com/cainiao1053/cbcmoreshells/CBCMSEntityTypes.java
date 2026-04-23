package com.cainiao1053.cbcmoreshells;

import static com.cainiao1053.cbcmoreshells.Cbcmoreshells.REGISTRATE;

import java.util.function.Consumer;

import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.autocannon.bullet.AntiairMachineGunProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.AbstractCannonTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_shrapnel_torpedo.AirdroppedShrapnelTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.airdropped_torpedo.AirdroppedTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_he_shell.AntiairHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell.AntiairShrapnelBurst;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell.AntiairShrapnelBurstRenderer;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.antiair_shrapnel_shell.AntiairShrapnelShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.ap_super_heavy.APSuperHeavyShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shell.APBCShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apbc_shot.APBCShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.apfsds_shot.APFSDSShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.aphe_cannon_rocket.APHECannonRocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.baked_apfsds_shot.BakedAPFSDSShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.cannon_torpedo.CannonTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.deepwater_shrapnel_torpedo.DeepwaterShrapnelTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.he_cannon_rocket.HECannonRocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.hesh_shell.HESHShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.highspeed_torpedo.HighspeedTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.incendiary_he_shell.IncendiaryHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.inferior_he_shell.InferiorHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_shrapnel_torpedo.LongRangeShrapnelTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.long_range_torpedo.LongRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo.MediumRangeDeepwaterTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_deepwater_torpedo_typeb.MediumRangeDeepwaterTorpedoTypeBProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.medium_range_torpedo_typeb.MediumRangeTorpedoTypeBProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sap_shell.SAPShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo.SharpnelTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo.TorpedoBurst;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.sharpnel_torpedo.TorpedoBurstRenderer;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_he_shell.ShellessHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_incendiary_he_shell.ShellessIncendiaryHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shelless_sap_shell.ShellessSAPShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.shellless_ap_shot.ShellessAPShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.CannonTorpedoProjectileRenderer;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.AbstractDualCannonProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.DualCannonProjectileRenderer;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_antiair_he_shell.ExtendedAntiairHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.extended_ap_shot.ExtendedAPShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_antiair_he_shell.NormalAntiairHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shell.NormalAPShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_ap_shot.NormalAPShotProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_apbc_shell.NormalAPBCShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_he_shell.NormalHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_incendiary_he_shell.NormalIncendiaryHEShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.normal_sap_shell.NormalSAPShellProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.AbstractRackedProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.RackedProjectileRenderer;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bomb.APHEBombProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_bouncing_bomb.APHEBouncingBombProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_loitering_rocket.APHELoiteringRocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.aphe_rocket.APHERocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.depth_charge.DepthChargeProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_aphe_rocket.DualAPHERocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.dual_he_rocket.DualHERocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bomb.HEBombProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_bouncing_bomb.HEBouncingBombProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_loitering_rocket.HELoiteringRocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.he_rocket.HERocketProjectile;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.racked_torpedo.RackedTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.early_torpedo.EarlyTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.gambler_medium_range_torpedo.GamblerMediumRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.highspeed_long_range_torpedo.HighspeedLongRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.light_high_speed_torpedo.LightHighSpeedTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.medium_range_torpedo.MediumRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.baguette_shot.BaguetteShotProjectile;


import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.primary_torpedo.PrimaryTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_highspeed_torpedo.ReductiveHighspeedTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_long_range_torpedo.ReductiveLongRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reductive_medium_range_torpedo.ReductiveMediumRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_medium_range_torpedo.ReinforcedMediumRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_reductive_short_range_torpedo.ReinforcedReductiveShortRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reignforced_short_range_torpedo.ReinforcedShortRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reinforced_long_range_torpedo.ReinforcedLongRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.reinforced_reductive_medium_range_torpedo.ReinforcedReductiveMediumRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.short_range_torpedo.ShortRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.slow_long_range_torpedo.SlowLongRangeTorpedoProjectile;
import com.cainiao1053.cbcmoreshells.munitions.torpedo_tube.ultraspeed_torpedo.UltraspeedTorpedoProjectile;
import com.simibubi.create.content.contraptions.render.OrientedContraptionEntityRenderer;
import com.tterrag.registrate.util.entry.EntityEntry;
import com.tterrag.registrate.util.nullness.NonNullConsumer;

import net.minecraft.client.renderer.entity.NoopRenderer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityType.EntityFactory;
import net.minecraft.world.entity.MobCategory;
import rbasamoyai.createbigcannons.index.CBCMunitionPropertiesHandlers;
import rbasamoyai.createbigcannons.multiloader.EntityTypeConfigurator;
import rbasamoyai.createbigcannons.munitions.autocannon.AbstractAutocannonProjectile;
import rbasamoyai.createbigcannons.munitions.autocannon.AutocannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.AbstractBigCannonProjectile;
import rbasamoyai.createbigcannons.munitions.big_cannon.BigCannonProjectileRenderer;
import rbasamoyai.createbigcannons.munitions.big_cannon.ap_shot.APShotProjectile;
import rbasamoyai.createbigcannons.munitions.config.MunitionPropertiesHandler;
import rbasamoyai.createbigcannons.munitions.config.PropertiesTypeHandler;
import rbasamoyai.ritchiesprojectilelib.RPLTags;

public class CBCMSEntityTypes {




	public static final EntityEntry<InferiorHEShellProjectile> Inferior_HE_SHELL = cannonProjectile("inferior_he_shell", InferiorHEShellProjectile::new, "Inferior High Explosive Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<SAPShellProjectile> SAP_SHELL = cannonProjectile("sap_shell", SAPShellProjectile::new, "SAP Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<HESHShellProjectile> HESH_SHELL = cannonProjectile("hesh_shell", HESHShellProjectile::new, "HESH Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<ShellessHEShellProjectile> SHELLESS_HE_SHELL = cannonProjectile("shelless_he_shell", ShellessHEShellProjectile::new, "Shelless HE Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<APHECannonRocketProjectile> APHE_CANNON_ROCKET = cannonProjectile("aphe_cannon_rocket", APHECannonRocketProjectile::new, "APHE Cannon Rocket", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<ShellessAPShotProjectile> SHELLESS_AP_SHOT = cannonProjectile("shelless_ap_shot", ShellessAPShotProjectile::new, "Shelless AP Shot", CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<ShellessSAPShellProjectile> SHELLESS_SAP_SHELL = cannonProjectile("shelless_sap_shell", ShellessSAPShellProjectile::new, "Shelless SAP Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<HECannonRocketProjectile> HE_CANNON_ROCKET = cannonProjectile("he_cannon_rocket", HECannonRocketProjectile::new, "HE Cannon Rocket", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<APBCShotProjectile> APBC_SHOT = cannonProjectile("apbc_shot", APBCShotProjectile::new, "APBC Shot", CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<APSuperHeavyShotProjectile> AP_SUPER_HEAVY_SHOT = cannonProjectile("ap_super_heavy_shot", APSuperHeavyShotProjectile::new, "AP Super Heavy Shot", CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<BakedAPFSDSShotProjectile> BAKED_APFSDS_SHOT = cannonProjectile("baked_apfsds_shot",BakedAPFSDSShotProjectile::new, "Baked APFSDS Shot",CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<BaguetteShotProjectile> BAGUETTE_SHOT = cannonProjectile("baguette_shot",BaguetteShotProjectile::new, "Baguette Shot",CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<IncendiaryHEShellProjectile> INCENDIARY_HE_SHELL = cannonProjectile("incendiary_he_shell", IncendiaryHEShellProjectile::new, "Incendiary HE Shell", CBCMSMunitionPropertiesHandlers.INCENDIARY_SHELL_PROJECTILE);
	public static final EntityEntry<ShellessIncendiaryHEShellProjectile> SHELLESS_INCENDIARY_HE_SHELL = cannonProjectile("shelless_incendiary_he_shell", ShellessIncendiaryHEShellProjectile::new, "Shelless Incendiary HE Shell", CBCMSMunitionPropertiesHandlers.INCENDIARY_SHELL_PROJECTILE);
	public static final EntityEntry<AntiairShrapnelShellProjectile> ANTIAIR_SHRAPNEL_SHELL = cannonProjectile("antiair_shrapnel_shell", AntiairShrapnelShellProjectile::new, "Antiair Shrapnel Shell", CBCMSMunitionPropertiesHandlers.SHRAPNEL_SHELLESS_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<APBCShellProjectile> APBC_SHELL = cannonProjectile("apbc_shell", APBCShellProjectile::new, "APBC Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);


	public static final EntityEntry<CannonTorpedoProjectile> CANNON_TORPEDO = torpedoProjectile("cannon_torpedo", CannonTorpedoProjectile::new, "Cannon Torpedo", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<MediumRangeTorpedoProjectile> MEDIUM_RANGE_TORPEDO = torpedoProjectile("medium_range_torpedo", MediumRangeTorpedoProjectile::new, "Medium Range Torpedo", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<MediumRangeDeepwaterTorpedoProjectile> MEDIUM_RANGE_DEEPWATER_TORPEDO = torpedoProjectile("medium_range_deepwater_torpedo", MediumRangeDeepwaterTorpedoProjectile::new, "Medium Range Deepwater Torpedo", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<HighspeedTorpedoProjectile> HIGHSPEED_TORPEDO = torpedoProjectile("highspeed_torpedo", HighspeedTorpedoProjectile::new, "Highspeed Torpedo", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<AirdroppedTorpedoProjectile> AIRDROPPED_TORPEDO = torpedoProjectile("airdropped_torpedo", AirdroppedTorpedoProjectile::new, "Airdropped Torpedo", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<APFSDSShotProjectile> APFSDS_SHOT = cannonProjectile("apfsds_shot",APFSDSShotProjectile::new, "APFSDS Shot",CBCMSMunitionPropertiesHandlers.SHELLESS_INERT_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<AntiairHEShellProjectile> ANTIAIR_HE_SHELL = cannonProjectile("antiair_he_shell", AntiairHEShellProjectile::new, "Antiair HE Shell", CBCMSMunitionPropertiesHandlers.SHELLESS_SHELL_BIG_CANNON_PROJECTILE);
	public static final EntityEntry<LongRangeTorpedoProjectile> LONG_RANGE_TORPEDO = torpedoProjectile("long_range_torpedo", LongRangeTorpedoProjectile::new, "Long Range Torpedo", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<SharpnelTorpedoProjectile> SHARPNEL_TORPEDO = torpedoProjectile("sharpnel_torpedo", SharpnelTorpedoProjectile::new, "Sharpnel Torpedo", CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE);
	public static final EntityEntry<AirdroppedShrapnelTorpedoProjectile> AIRDROPPED_SHRAPNEL_TORPEDO = torpedoProjectile("airdropped_shrapnel_torpedo", AirdroppedShrapnelTorpedoProjectile::new, "Airdropped Shrapnel Torpedo", CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE);
	public static final EntityEntry<MediumRangeTorpedoTypeBProjectile> MEDIUM_RANGE_TORPEDO_TYPEB = torpedoProjectile("medium_range_torpedo_typeb", MediumRangeTorpedoTypeBProjectile::new, "Medium Range Torpedo Type B", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<MediumRangeDeepwaterTorpedoTypeBProjectile> MEDIUM_RANGE_DEEPWATER_TORPEDO_TYPEB = torpedoProjectile("medium_range_deepwater_torpedo_typeb", MediumRangeDeepwaterTorpedoTypeBProjectile::new, "Medium Range Deepwater Torpedo Type B", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<DeepwaterShrapnelTorpedoProjectile> DEEPWATER_SHRAPNEL_TORPEDO = torpedoProjectile("deepwater_shrapnel_torpedo", DeepwaterShrapnelTorpedoProjectile::new, "Deepwater Shrapnel Torpedo", CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE);
	public static final EntityEntry<LongRangeShrapnelTorpedoProjectile> LONG_RANGE_SHRAPNEL_TORPEDO = torpedoProjectile("long_range_shrapnel_torpedo", LongRangeShrapnelTorpedoProjectile::new, "Long Range Shrapnel Torpedo", CBCMSMunitionPropertiesHandlers.SHRAPNEL_TORPEDO_PROJECTILE);
	public static final EntityEntry<ReductiveMediumRangeTorpedoProjectile> REDUCTIVE_MEDIUM_RANGE_TORPEDO = torpedoProjectile("reductive_medium_range_torpedo", ReductiveMediumRangeTorpedoProjectile::new, "Reductive Medium Range Torpedo", CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE);
	public static final EntityEntry<LightHighSpeedTorpedoProjectile> LIGHT_HIGH_SPEED_TORPEDO = torpedoProjectile("light_high_speed_torpedo", LightHighSpeedTorpedoProjectile::new, "LIGHT_HIGH_SPEED_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<ReinforcedShortRangeTorpedoProjectile> REINFORCED_SHORT_RANGE_TORPEDO = torpedoProjectile("reinforced_short_range_torpedo", ReinforcedShortRangeTorpedoProjectile::new, "REINFORCED_SHORT_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<ShortRangeTorpedoProjectile> SHORT_RANGE_TORPEDO = torpedoProjectile("short_range_torpedo", ShortRangeTorpedoProjectile::new, "SHORT_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<ReinforcedReductiveShortRangeTorpedoProjectile> REINFORCED_REDUCTIVE_SHORT_RANGE_TORPEDO = torpedoProjectile("reinforced_reductive_short_range_torpedo", ReinforcedReductiveShortRangeTorpedoProjectile::new, "REINFORCED_REDUCTIVE_SHORT_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE);
	public static final EntityEntry<ReinforcedMediumRangeTorpedoProjectile> REINFORCED_MEDIUM_RANGE_TORPEDO = torpedoProjectile("reinforced_medium_range_torpedo", ReinforcedMediumRangeTorpedoProjectile::new, "REINFORCED_MEDIUM_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<EarlyTorpedoProjectile> EARLY_TORPEDO = torpedoProjectile("early_torpedo", EarlyTorpedoProjectile::new, "EARLY_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<PrimaryTorpedoProjectile> PRIMARY_TORPEDO = torpedoProjectile("primary_torpedo", PrimaryTorpedoProjectile::new, "PRIMARY_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<ReinforcedReductiveMediumRangeTorpedoProjectile> REINFORCED_REDUCTIVE_MEDIUM_RANGE_TORPEDO = torpedoProjectile("reinforced_reductive_medium_range_torpedo", ReinforcedReductiveMediumRangeTorpedoProjectile::new, "REINFORCED_REDUCTIVE_MEDIUM_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE);
	public static final EntityEntry<ReductiveLongRangeTorpedoProjectile> REDUCTIVE_LONG_RANGE_TORPEDO = torpedoProjectile("reductive_long_range_torpedo", ReductiveLongRangeTorpedoProjectile::new, "REDUCTIVE_LONG_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE);
	public static final EntityEntry<ReinforcedLongRangeTorpedoProjectile> REINFORCED_LONG_RANGE_TORPEDO = torpedoProjectile("reinforced_long_range_torpedo", ReinforcedLongRangeTorpedoProjectile::new, "REINFORCED_LONG_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<GamblerMediumRangeTorpedoProjectile> GAMBLER_MEDIUM_RANGE_TORPEDO = torpedoProjectile("gambler_medium_range_torpedo", GamblerMediumRangeTorpedoProjectile::new, "GAMBLER_MEDIUM_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE);
	public static final EntityEntry<UltraspeedTorpedoProjectile> ULTRASPEED_TORPEDO = torpedoProjectile("ultraspeed_torpedo", UltraspeedTorpedoProjectile::new, "ULTRASPEED_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<ReductiveHighspeedTorpedoProjectile> REDUCTIVE_HIGHSPEED_TORPEDO = torpedoProjectile("reductive_highspeed_torpedo", ReductiveHighspeedTorpedoProjectile::new, "REDUCTIVE_HIGHSPEED_TORPEDO", CBCMSMunitionPropertiesHandlers.REDUCTIVE_TORPEDO_PROJECTILE);
	public static final EntityEntry<SlowLongRangeTorpedoProjectile> SLOW_LONG_RANGE_TORPEDO = torpedoProjectile("slow_long_range_torpedo", SlowLongRangeTorpedoProjectile::new, "SLOW_LONG_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);
	public static final EntityEntry<HighspeedLongRangeTorpedoProjectile> HIGHSPEED_LONG_RANGE_TORPEDO = torpedoProjectile("highspeed_long_range_torpedo", HighspeedLongRangeTorpedoProjectile::new, "HIGHSPEED_LONG_RANGE_TORPEDO", CBCMSMunitionPropertiesHandlers.TORPEDO_PROJECTILE);


	public static final EntityEntry<HEBombProjectile> HE_BOMB = rackedProjectile("he_bomb", HEBombProjectile::new, "HE Bomb", CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE);
	public static final EntityEntry<HEBouncingBombProjectile> HE_BOUNCING_BOMB = rackedProjectile("he_bouncing_bomb", HEBouncingBombProjectile::new, "HE Bouncing Bomb", CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE);
	public static final EntityEntry<RackedTorpedoProjectile> RACKED_TORPEDO = rackedProjectile("racked_torpedo", RackedTorpedoProjectile::new, "Racked Torpedo", CBCMSMunitionPropertiesHandlers.RACKED_TORPEDO);
	public static final EntityEntry<HERocketProjectile> HE_ROCKET = rackedProjectile("he_rocket", HERocketProjectile::new, "HE Rocket", CBCMSMunitionPropertiesHandlers.RACKED_ROCKET);
	public static final EntityEntry<APHEBombProjectile> APHE_BOMB = rackedProjectile("aphe_bomb", APHEBombProjectile::new, "APHE Bomb", CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE);
	public static final EntityEntry<APHEBouncingBombProjectile> APHE_BOUNCING_BOMB = rackedProjectile("aphe_bouncing_bomb", APHEBouncingBombProjectile::new, "APHE Bouncing Bomb", CBCMSMunitionPropertiesHandlers.RACKED_PROJECTILE);
	public static final EntityEntry<APHERocketProjectile> APHE_ROCKET = rackedProjectile("aphe_rocket", APHERocketProjectile::new, "APHE Rocket", CBCMSMunitionPropertiesHandlers.RACKED_ROCKET);
	public static final EntityEntry<DualHERocketProjectile> DUAL_HE_ROCKET = rackedProjectile("dual_he_rocket", DualHERocketProjectile::new, "Dual HE Rocket", CBCMSMunitionPropertiesHandlers.RACKED_ROCKET);
	public static final EntityEntry<DualAPHERocketProjectile> DUAL_APHE_ROCKET = rackedProjectile("dual_aphe_rocket", DualAPHERocketProjectile::new, "Dual APHE Rocket", CBCMSMunitionPropertiesHandlers.RACKED_ROCKET);
	public static final EntityEntry<DepthChargeProjectile> DEPTH_CHARGE = rackedProjectile("depth_charge", DepthChargeProjectile::new, "Depth Charge", CBCMSMunitionPropertiesHandlers.RACKED_SHRAPNEL_PROJECTILE);
	public static final EntityEntry<HELoiteringRocketProjectile> HE_LOITERING_ROCKET = rackedProjectile("beef_noodle", HELoiteringRocketProjectile::new, "HE Rocket", CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET);
	public static final EntityEntry<APHELoiteringRocketProjectile> APHE_LOITERING_ROCKET = rackedProjectile("bubble_drink", APHELoiteringRocketProjectile::new, "APHE Rocket", CBCMSMunitionPropertiesHandlers.LOITERING_ROCKET);

	public static final EntityEntry<NormalAPShotProjectile> NORMAL_AP_SHOT = dualCannonProjectile("normal_ap_shot", NormalAPShotProjectile::new, "Normal AP Shot", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<NormalHEShellProjectile> NORMAL_HE_SHELL = dualCannonProjectile("normal_he_shell", NormalHEShellProjectile::new, "Normal HE Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<NormalSAPShellProjectile> NORMAL_SAP_SHELL = dualCannonProjectile("normal_sap_shell", NormalSAPShellProjectile::new, "Normal SAP Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<NormalAPShellProjectile> NORMAL_AP_SHELL = dualCannonProjectile("normal_ap_shell", NormalAPShellProjectile::new, "Normal AP Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<ExtendedAPShotProjectile> EXTENDED_AP_SHOT = dualCannonProjectile("extended_ap_shot", ExtendedAPShotProjectile::new, "Extended AP Shot", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<NormalAntiairHEShellProjectile> NORMAL_ANTIAIR_HE_SHELL = dualCannonProjectile("normal_antiair_he_shell", NormalAntiairHEShellProjectile::new, "Normal Antiair HE Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<NormalIncendiaryHEShellProjectile> NORMAL_INCENDIARY_HE_SHELL = dualCannonProjectile("normal_incendiary_he_shell", NormalIncendiaryHEShellProjectile::new, "Normal HE Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_INCENDIARY_PROPERTIES);
	public static final EntityEntry<NormalAPBCShellProjectile> NORMAL_APBC_SHELL = dualCannonProjectile("normal_apbc_shell", NormalAPBCShellProjectile::new, "Normal APBC Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);
	public static final EntityEntry<ExtendedAntiairHEShellProjectile> EXTENDED_ANTIAIR_HE_SHELL = dualCannonProjectile("extended_antiair_he_shell", ExtendedAntiairHEShellProjectile::new, "Extended Antiair HE Shell", CBCMSMunitionPropertiesHandlers.DUAL_CANNON_PROPERTIES);


	public static final EntityEntry<AntiairMachineGunProjectile> ANTIAIR_MACHINE_GUN_BULLET = autocannonProjectile("antiair_machine_gun_bullet", AntiairMachineGunProjectile::new, CBCMSMunitionPropertiesHandlers.ANTIAIR_AUTOCANNON);



	public static final EntityEntry<TorpedoBurst> TORPEDO_BURST = REGISTRATE
			.entity("torpedo_burst", TorpedoBurst::new, MobCategory.MISC)
			.properties(shrapnel())
			.renderer(() -> TorpedoBurstRenderer::new)
			.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.PROJECTILE_BURST))
			.register();

	public static final EntityEntry<AntiairShrapnelBurst> ANTIAIR_SHRAPNEL_BURST = REGISTRATE
			.entity("antiair_shrapnel_burst", AntiairShrapnelBurst::new, MobCategory.MISC)
			.properties(shrapnel())
			.renderer(() -> AntiairShrapnelBurstRenderer::new)
			.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, CBCMunitionPropertiesHandlers.PROJECTILE_BURST))
			.register();


	private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
	cannonProjectile(String id, EntityFactory<T> factory, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> BigCannonProjectileRenderer::new)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T extends AbstractBigCannonProjectile> EntityEntry<T>
	cannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> BigCannonProjectileRenderer::new)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T extends AbstractCannonTorpedoProjectile> EntityEntry<T>
	torpedoProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> CannonTorpedoProjectileRenderer::new)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T extends AbstractRackedProjectile> EntityEntry<T>
	rackedProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> RackedProjectileRenderer::new)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T extends AbstractDualCannonProjectile> EntityEntry<T>
	dualCannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(cannonProperties())
				.renderer(() -> DualCannonProjectileRenderer::new)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T> NonNullConsumer<T> configure(Consumer<EntityTypeConfigurator> cons) {
		return b -> cons.accept(EntityTypeConfigurator.of(b));
	}

	private static <T> NonNullConsumer<T> autocannonProperties() {
		return configure(c -> c.size(0.2f, 0.2f)
			.fireImmune()
			.updateInterval(1)
			.updateVelocity(false) // Mixin ServerEntity to not track motion
			.trackingRange(16));
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T>
	autocannonProjectile(String id, EntityFactory<T> factory, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(autocannonProperties())
				.renderer(() -> AutocannonProjectileRenderer::new)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T extends AbstractAutocannonProjectile> EntityEntry<T>
	autocannonProjectile(String id, EntityFactory<T> factory, String enUSdiffLang, PropertiesTypeHandler<EntityType<?>, ?> handler) {
		return REGISTRATE
				.entity(id, factory, MobCategory.MISC)
				.properties(autocannonProperties())
				.renderer(() -> AutocannonProjectileRenderer::new)
				.lang(enUSdiffLang)
				.tag(RPLTags.PRECISE_MOTION)
				.onRegister(type -> MunitionPropertiesHandler.registerProjectileHandler(type, handler))
				.register();
	}

	private static <T> NonNullConsumer<T> cannonProperties() {
		return configure(c -> c.size(0.8f, 0.8f)
			.fireImmune()
			.updateInterval(1)
			.updateVelocity(false) // Ditto
			.trackingRange(16));
	}

	private static <T> NonNullConsumer<T> shrapnel() {
		return configure(c -> c.size(0.8f, 0.8f)
			.fireImmune()
			.updateInterval(1)
			.updateVelocity(true)
			.trackingRange(16));
	}

	public static void register() {
	}

}
