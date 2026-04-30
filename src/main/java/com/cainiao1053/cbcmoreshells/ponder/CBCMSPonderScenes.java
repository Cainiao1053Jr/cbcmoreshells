package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.CBCMSBlocks;
import com.cainiao1053.cbcmoreshells.CBCMSItems;
import com.tterrag.registrate.util.entry.ItemProviderEntry;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CBCMSPonderScenes {
    public static void register(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        PonderSceneRegistrationHelper<ItemProviderEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

        HELPER.forComponents(
                        CBCMSBlocks.CAST_IRON_DUAL_CANNON_BARREL,
                        CBCMSBlocks.CAST_IRON_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.CAST_IRON_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.BRONZE_DUAL_CANNON_BARREL,
                        CBCMSBlocks.BRONZE_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.BRONZE_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.STEEL_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.BRASS_DUAL_CANNON_BARREL,
                        CBCMSBlocks.BRASS_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.BRASS_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_BARREL,
                        CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_BARREL,
                        CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_BARREL,
                        CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_BARREL,
                        CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_BARREL,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHARGER,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.STEEL_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.STEEL_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.STEEL_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.BRASS_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.BRASS_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.BRASS_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_CHAMBER_SHIELDED,
                        CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER_SHIELDED,
                        CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH,
                        CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH
                )
                .addStoryBoard("dual_cannon/dual_cannon_intro", DualCannonScenes::dualCannonIntro)
                .addStoryBoard("dual_cannon/dual_cannon_setting", DualCannonScenes::dualCannonSetting)
                .addStoryBoard("dual_cannon/dual_cannon_command", DualCannonScenes::dualCannonCommand);

        HELPER.forComponents(
                CBCMSBlocks.STEEL_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH,
                CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH
                )
                .addStoryBoard("dual_cannon/dual_cannon_magazine", DualCannonScenes::dualCannonMagazine);

        HELPER.forComponents(
                CBCMSBlocks.COMMAND_DEPLOYER,
                CBCMSBlocks.COMMAND_DISPLAYER,
                CBCMSItems.DAMAGE_COMBAT_COMMAND,
                CBCMSItems.RELOAD_COMBAT_COMMAND,
                CBCMSItems.SPREAD_COMBAT_COMMAND,
                CBCMSItems.RANGE_COMBAT_COMMAND,
                CBCMSItems.GAMBLER_COMBAT_COMMAND,
                CBCMSItems.BERSERKER_COMBAT_COMMAND,
                CBCMSItems.MYOPIA_COMBAT_COMMAND,
                CBCMSItems.SNIPER_COMBAT_COMMAND,
                CBCMSItems.ASSASSIN_COMBAT_COMMAND
        ).addStoryBoard("dual_cannon/dual_cannon_command", DualCannonScenes::dualCannonCommand);

        HELPER.forComponents(
                CBCMSBlocks.STEEL_PROJECTILE_RACK_CHAMBER,
                CBCMSBlocks.STEEL_PROJECTILE_RACK_QUICKFIRING_BREECH
        ).addStoryBoard("projectile_rack/projectile_rack_intro", ProjectileRackScenes::projectileRackIntro);

        HELPER.forComponents(
                CBCMSBlocks.AMMO_RACK,
                CBCMSBlocks.STEEL_AMMO_RACK
        ).addStoryBoard("ammo_rack/ammo_rack_intro", AmmoRackScenes::ammoRackIntro);
    }
}
