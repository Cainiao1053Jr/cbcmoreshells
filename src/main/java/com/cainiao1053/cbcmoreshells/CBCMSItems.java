package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.datagen.assets.CBCMSBuilderTransformers;
import com.cainiao1053.cbcmoreshells.items.cannon_combo.BigCannonComboItem;
import com.cainiao1053.cbcmoreshells.items.cannon_combo.DualCannonComboItem;
import com.cainiao1053.cbcmoreshells.items.cannon_combo.SingleCannonComboItem;
import com.cainiao1053.cbcmoreshells.items.cannon_combo.TorpedoTubeComboItem;
import com.cainiao1053.cbcmoreshells.munitions.autocannon.bullet.AntiairMachineGunRoundItem;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.combat_command.*;
import com.cainiao1053.cbcmoreshells.munitions.fuzes.SensitiveImpactFuzeItem;
import com.cainiao1053.cbcmoreshells.munitions.fuzes.ShipProximityFuzeItem;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import rbasamoyai.createbigcannons.CBCTags;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;
import rbasamoyai.createbigcannons.utils.CBCUtils;

import java.util.Map;

import static com.cainiao1053.cbcmoreshells.Cbcmoreshells.REGISTRATE;

public class CBCMSItems {

    static {
        ModGroup.useModTab(ModGroup.MAIN_TAB_KEY);
    }


    public static final ItemEntry<Item>


            STEEL_TORPEDO_SLIDING_BREECHBLOCK = REGISTRATE.item("steel_torpedo_sliding_breechblock", Item::new)
            .transform(CBCMSBuilderTransformers.torpedoSlidingBreechblock("torpedo_sliding_breech/steel"))
            .register(),

    ROCKET_BRACKET = REGISTRATE.item("rocket_bracket", Item::new)
            .register(),

            TORPEDO_HEAD = REGISTRATE.item("torpedo_head", Item::new)
            .register(),

            REINFORCED_TORPEDO_HEAD = REGISTRATE.item("reinforced_torpedo_head", Item::new)
            .register()
            ;

    public static final ItemEntry<AntiairMachineGunRoundItem> ANTIAIR_MACHINE_GUN_ROUND = REGISTRATE
            .item("antiair_machine_gun_round", AntiairMachineGunRoundItem::new)
            .tag(CBCTags.CBCItemTags.AUTOCANNON_CARTRIDGES)
            .register();

    public static final ItemEntry<ShipProximityFuzeItem> SHIP_PROXIMITY_FUZE = REGISTRATE.item("ship_proximity_fuze", ShipProximityFuzeItem::new)
            .tag(CBCTags.CBCItemTags.FUZES)
            .register();

    public static final ItemEntry<SensitiveImpactFuzeItem> SENSITIVE_IMPACT_FUZE = REGISTRATE.item("sensitive_impact_fuze", SensitiveImpactFuzeItem::new)
            .tag(CBCTags.CBCItemTags.FUZES)
            .register();

    public static final ItemEntry<CombatCommandBaseItem> BASE_COMBAT_COMMAND = REGISTRATE.item("base_combat_command", CombatCommandBaseItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(8))
            .register();

    public static final ItemEntry<ReloadCombatCommandItem> RELOAD_COMBAT_COMMAND = REGISTRATE.item("reload_combat_command", ReloadCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(80)
                    .rarity(Rarity.RARE))
            .register();

    public static final ItemEntry<RangeCombatCommandItem> RANGE_COMBAT_COMMAND = REGISTRATE.item("range_combat_command", RangeCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(80)
                    .rarity(Rarity.RARE))
            .register();

    public static final ItemEntry<SpreadCombatCommandItem> SPREAD_COMBAT_COMMAND = REGISTRATE.item("spread_combat_command", SpreadCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(80)
                    .rarity(Rarity.RARE))
            .register();

    public static final ItemEntry<DamageCombatCommandItem> DAMAGE_COMBAT_COMMAND = REGISTRATE.item("damage_combat_command", DamageCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(80)
                    .rarity(Rarity.RARE))
            .register();

    public static final ItemEntry<GamblerCombatCommandItem> GAMBLER_COMBAT_COMMAND = REGISTRATE.item("gambler_combat_command", GamblerCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(48)
                    .rarity(Rarity.EPIC))
            .register();

    public static final ItemEntry<BerserkerCombatCommandItem> BERSERKER_COMBAT_COMMAND = REGISTRATE.item("berserker_combat_command", BerserkerCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(64)
                    .rarity(Rarity.EPIC))
            .register();

    public static final ItemEntry<MyopiaCombatCommandItem> MYOPIA_COMBAT_COMMAND = REGISTRATE.item("myopia_combat_command", MyopiaCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(64)
                    .rarity(Rarity.EPIC))
            .register();

    public static final ItemEntry<SniperCombatCommandItem> SNIPER_COMBAT_COMMAND = REGISTRATE.item("sniper_combat_command", SniperCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(64)
                    .rarity(Rarity.EPIC))
            .register();

    public static final ItemEntry<AssasinCombatCommandItem> ASSASSIN_COMBAT_COMMAND = REGISTRATE.item("assassin_combat_command", AssasinCombatCommandItem::new)
            .properties(properties -> properties.stacksTo(1)
                    .durability(48)
                    .rarity(Rarity.EPIC))
            .register();

    public static final ItemEntry<CombatCommandInfoItem> COMBAT_COMMAND_INFO = REGISTRATE.item("combat_command_info", CombatCommandInfoItem::new)
            .properties(properties -> properties.stacksTo(1))
            .register();

    public static final ItemEntry<DualCannonComboItem> DUAL_CANNON_COMBO = REGISTRATE.item("dual_cannon_combo", DualCannonComboItem::new)
            .properties(properties -> properties.stacksTo(8))
            .register();

    public static final ItemEntry<SingleCannonComboItem> SINGLE_CANNON_COMBO = REGISTRATE.item("single_cannon_combo", SingleCannonComboItem::new)
            .properties(properties -> properties.stacksTo(8))
            .register();

    public static final ItemEntry<BigCannonComboItem> BIG_CANNON_COMBO = REGISTRATE.item("big_cannon_combo", BigCannonComboItem::new)
            .properties(properties -> properties.stacksTo(8))
            .register();

    public static final ItemEntry<TorpedoTubeComboItem> TORPEDO_TUBE_COMBO = REGISTRATE.item("torpedo_tube_combo", TorpedoTubeComboItem::new)
            .properties(properties -> properties.stacksTo(8))
            .register();

    public static final ItemEntry<SequencedAssemblyItem> ASSEMBLED_TORPEDO_HEAD = sequencedIngredient("assembled_torpedo_head");

    public static final ItemEntry<SequencedAssemblyItem> ASSEMBLED_REINFORCED_TORPEDO_HEAD = sequencedIngredient("assembled_reinforced_torpedo_head");

    public static final Map<String, ItemEntry<Item>> TORPEDO_MOLDS;
    public static final Map<String, ItemEntry<Item>> TORPEDO_COMPONENTS;
    public static final Map<String, ItemEntry<SequencedAssemblyItem>> ASSEMBLED_TORPEDO_COMPONENTS;

    static {
        String[] torpedoIds = {
                "medium_range_torpedo",
                "long_range_torpedo",
                "medium_range_deepwater_torpedo",
                "highspeed_torpedo",
                "reductive_medium_range_torpedo",
                "light_high_speed_torpedo",
                "reinforced_short_range_torpedo",
                "short_range_torpedo",
                "reinforced_reductive_short_range_torpedo",
                "early_torpedo",
                "reinforced_medium_range_torpedo",
                "primary_torpedo",
                "reinforced_reductive_medium_range_torpedo",
                "reductive_long_range_torpedo",
                "reinforced_long_range_torpedo",
                "gambler_medium_range_torpedo",
                "ultraspeed_torpedo",
                "reductive_highspeed_torpedo",
                "slow_long_range_torpedo",
                "highspeed_long_range_torpedo"
        };
        Map<String, ItemEntry<Item>> molds = new java.util.LinkedHashMap<>();
        Map<String, ItemEntry<Item>> raw_components = new java.util.LinkedHashMap<>();
        Map<String, ItemEntry<SequencedAssemblyItem>> assembled_components = new java.util.LinkedHashMap<>();
        for (String id : torpedoIds) {
            molds.put(id, REGISTRATE.item(id + "_mold", Item::new)
                    .properties(properties -> properties.stacksTo(4))
                    .register()
            );

            raw_components.put(id, REGISTRATE.item(id + "_component", Item::new)
                    .register()
            );

            assembled_components.put(
                    id, sequencedIngredient("assembled_" + id + "_component")
            );
        }
        TORPEDO_MOLDS = java.util.Collections.unmodifiableMap(molds);
        TORPEDO_COMPONENTS = java.util.Collections.unmodifiableMap(raw_components);
        ASSEMBLED_TORPEDO_COMPONENTS = java.util.Collections.unmodifiableMap(assembled_components);
    }


    //public static final ItemEntry<SequencedAssemblyItem>


    public static void register() {
    }

    public static TagKey<Item> tag(ResourceLocation loc) {
        return CBCRegistryUtils.createItemTag(loc);
    }

    private static TagKey<Item> forgeTag(String loc) {
        return tag(CBCUtils.location("forge", loc));
    }

    private static TagKey<Item> fabricTag(String loc) {
        return tag(CBCUtils.location("c", loc));
    }

    private static ItemEntry<SequencedAssemblyItem> sequencedIngredient(String name) {
        return REGISTRATE.item(name, SequencedAssemblyItem::new)
                .register();
    }

}
