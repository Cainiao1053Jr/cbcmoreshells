package com.cainiao1053.cbcmoreshells;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import com.simibubi.create.Create;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyItem;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModGroup {

	public static final ResourceKey<CreativeModeTab> MAIN_TAB_KEY = makeKey("shells");

	private static final DeferredRegister<CreativeModeTab> TAB_REGISTER = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Cbcmoreshells.MODID);
	private static Map<ResourceKey<CreativeModeTab>, DeferredHolder<CreativeModeTab, CreativeModeTab>> TABS = new HashMap<>();

	public static final Supplier<CreativeModeTab> GROUP = wrapGroup("shells", () -> createBuilder()
		.title(Component.translatable("itemGroup." + Cbcmoreshells.MODID))
		.icon(CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED::asStack)
		.displayItems((param, output) -> {
			output.acceptAll(Arrays.asList(
				CBCMSBlocks.Inferior_HE_SHELL.asStack(),
				CBCMSBlocks.SAP_SHELL.asStack(),
				CBCMSBlocks.HESH_SHELL.asStack(),
				CBCMSBlocks.APFSDS_SHOT.asStack(),
				CBCMSBlocks.ANTIAIR_HE_SHELL.asStack(),
				CBCMSBlocks.ANTIAIR_SHRAPNEL_SHELL.asStack(),
				CBCMSBlocks.APBC_SHOT.asStack(),
				CBCMSBlocks.APBC_SHELL.asStack(),
				CBCMSBlocks.AP_SUPER_HEAVY_SHOT.asStack(),
				CBCMSBlocks.BAKED_APFSDS_SHOT.asStack(),
				CBCMSBlocks.BAGUETTE_SHOT.asStack(),
				CBCMSBlocks.INCENDIARY_HE_SHELL.asStack(),

				CBCMSBlocks.MEDIUM_RANGE_TORPEDO_TYPEB.asStack(),
				CBCMSBlocks.MEDIUM_RANGE_DEEPWATER_TORPEDO_TYPEB.asStack(),

				CBCMSBlocks.MEDIUM_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.MEDIUM_RANGE_DEEPWATER_TORPEDO.asStack(),
				CBCMSBlocks.HIGHSPEED_TORPEDO.asStack(),
				CBCMSBlocks.LONG_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.REDUCTIVE_MEDIUM_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.LIGHT_HIGH_SPEED_TORPEDO.asStack(),
				CBCMSBlocks.REINFORCED_SHORT_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.SHORT_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.REINFORCED_REDUCTIVE_SHORT_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.REINFORCED_MEDIUM_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.EARLY_TORPEDO.asStack(),
				CBCMSBlocks.PRIMARY_TORPEDO.asStack(),
				CBCMSBlocks.REINFORCED_REDUCTIVE_MEDIUM_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.REDUCTIVE_LONG_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.REINFORCED_LONG_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.GAMBLER_MEDIUM_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.ULTRASPEED_TORPEDO.asStack(),
				CBCMSBlocks.REDUCTIVE_HIGHSPEED_TORPEDO.asStack(),
				CBCMSBlocks.SLOW_LONG_RANGE_TORPEDO.asStack(),
				CBCMSBlocks.HIGHSPEED_LONG_RANGE_TORPEDO.asStack(),

				CBCMSBlocks.CAST_IRON_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.CAST_IRON_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.CAST_IRON_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.BRONZE_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.BRONZE_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.BRONZE_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.STEEL_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.WIDE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.LARGE_STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.BRASS_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.BRASS_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.BRASS_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.BRASS_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.WIDE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.LARGE_BRASS_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.WIDE_NETHER_STEEL_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHAMBER_SHIELDED.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_CHARGER.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_DUAL_CANNON_QUICKFIRING_BREECH.asStack(),

				CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_CAST_IRON_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_BRONZE_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.STEEL_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.STEEL_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.STEEL_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.STEEL_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_STEEL_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.LARGE_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.BRASS_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.BRASS_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.BRASS_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.BRASS_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_CHAMBER_SHIELDED.asStack(),
				CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.LARGE_BRASS_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_CHAMBER_SHIELDED.asStack(),
				CBCMSBlocks.WIDE_TOUGH_STEEL_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.LARGE_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_BARREL.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_CHAMBER_SHIELDED.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.WIDE_MILITARY_SLATE_ALLOY_SINGLE_CANNON_MAGAZINE_BREECH.asStack(),

				CBCMSBlocks.NORMAL_AP_SHOT.asStack(),
				CBCMSBlocks.NORMAL_HE_SHELL.asStack(),
				CBCMSBlocks.NORMAL_SAP_SHELL.asStack(),
				CBCMSBlocks.NORMAL_AP_SHELL.asStack(),
				CBCMSBlocks.EXTENDED_AP_SHOT.asStack(),
				CBCMSBlocks.NORMAL_ANTIAIR_HE_SHELL.asStack(),
				CBCMSBlocks.NORMAL_INCENDIARY_HE_SHELL.asStack(),
				CBCMSBlocks.NORMAL_APBC_SHELL.asStack(),
				CBCMSBlocks.EXTENDED_ANTIAIR_HE_SHELL.asStack(),

				CBCMSItems.COMBAT_COMMAND_INFO.asStack(),
				CBCMSBlocks.COMMAND_DEPLOYER.asStack(),
				CBCMSBlocks.COMMAND_DISPLAYER.asStack(),
				CBCMSItems.DAMAGE_COMBAT_COMMAND.asStack(),
				CBCMSItems.RELOAD_COMBAT_COMMAND.asStack(),
				CBCMSItems.SPREAD_COMBAT_COMMAND.asStack(),
				CBCMSItems.RANGE_COMBAT_COMMAND.asStack(),
				CBCMSItems.GAMBLER_COMBAT_COMMAND.asStack(),
				CBCMSItems.BERSERKER_COMBAT_COMMAND.asStack(),
				CBCMSItems.MYOPIA_COMBAT_COMMAND.asStack(),
				CBCMSItems.SNIPER_COMBAT_COMMAND.asStack(),
				CBCMSItems.ASSASSIN_COMBAT_COMMAND.asStack(),

				CBCMSBlocks.HE_LOITERING_ROCKET.asStack(),
				CBCMSBlocks.APHE_LOITERING_ROCKET.asStack(),
				CBCMSBlocks.HE_BOMB.asStack(),
				CBCMSBlocks.APHE_BOMB.asStack(),
				CBCMSBlocks.HE_BOUNCING_BOMB.asStack(),
				CBCMSBlocks.APHE_BOUNCING_BOMB.asStack(),
				CBCMSBlocks.RACKED_TORPEDO.asStack(),
				CBCMSBlocks.HE_ROCKET.asStack(),
				CBCMSBlocks.APHE_ROCKET.asStack(),
				CBCMSBlocks.DUAL_HE_ROCKET.asStack(),
				CBCMSBlocks.DUAL_APHE_ROCKET.asStack(),
				CBCMSBlocks.DEPTH_CHARGE.asStack(),
				CBCMSItems.ROCKET_BRACKET.asStack(),
				CBCMSItems.ANTIAIR_MACHINE_GUN_ROUND.asStack(),
				CBCMSItems.SHIP_PROXIMITY_FUZE.asStack(),
				CBCMSItems.SENSITIVE_IMPACT_FUZE.asStack(),



				CBCMSBlocks.STEEL_TORPEDO_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.STEEL_TORPEDO_CHAMBER.asStack(),
				CBCMSBlocks.STEEL_TORPEDO_BARREL.asStack(),
				CBCMSBlocks.NETHERSTEEL_SLIDING_BREECH.asStack(),
				CBCMSBlocks.NETHERSTEEL_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.STEEL_PROJECTILE_RACK_QUICKFIRING_BREECH.asStack(),
				CBCMSBlocks.STEEL_PROJECTILE_RACK_CHAMBER.asStack(),

				CBCMSBlocks.TORPEDO_DETECTION_DEVICE.asStack(),
				CBCMSBlocks.AMMO_RACK.asStack(),
				CBCMSBlocks.STEEL_AMMO_RACK.asStack(),
				CBCMSItems.FIRE_EXTINGUISHER.asStack(),
//				CBCMSBlocks.LANDING_INDICATOR.asStack(),
//				CBCMSBlocks.DISH_PLATE.asStack(),
//				CBCMSBlocks.ROUND_DISH_PLATE.asStack(),

//				CBCMSBlocks.ANTIBLAST_COPYCAT_PANEL.asStack(),

					CBCMSItems.TORPEDO_HEAD.asStack(),
					CBCMSItems.REINFORCED_TORPEDO_HEAD.asStack()
			));
			for (ItemEntry<Item> mold : CBCMSItems.TORPEDO_MOLDS.values()) {
				output.accept(mold.asStack());
			}
//			for (ItemEntry<SequencedAssemblyItem> component : CBCMSItems.TORPEDO_COMPONENTS.values()) {
//				output.accept(component.asStack());
//			}
		})
		.build());

//	public static Supplier<CreativeModeTab> wrapGroup(String id, Supplier<CreativeModeTab> sup) {
//		RegistryObject<CreativeModeTab> obj = TAB_REGISTER.register(id, sup);
//		TABS.put(ModGroup.makeKey(id), obj);
//		return obj;
//	}
	public static Supplier<CreativeModeTab> wrapGroup(String id, Supplier<CreativeModeTab> sup) {
	DeferredHolder<CreativeModeTab, CreativeModeTab> obj = TAB_REGISTER.register(id, sup);
	TABS.put(ModGroup.makeKey(id), obj);
	return obj;
	}
	public static CreativeModeTab.Builder createBuilder() {
		return CreativeModeTab.builder().withTabsBefore(Create.asResource("palettes"));
	}

	public static void useModTab(ResourceKey<CreativeModeTab> key) {
		Cbcmoreshells.REGISTRATE.setCreativeTab(TABS.get(key));
	}

	public static ResourceKey<CreativeModeTab> makeKey(String id) {
		return ResourceKey.create(Registries.CREATIVE_MODE_TAB, Cbcmoreshells.resource(id));
	}

//	public static void register(IEventBus modBus) {
//		Cbcmoreshells.REGISTRATE.addRawLang("itemGroup." + Cbcmoreshells.MODID, "CBC More Shells");
//		TAB_REGISTER.register(modBus);
//	}

	public static void register() {
		Cbcmoreshells.REGISTRATE.addRawLang("itemGroup." + Cbcmoreshells.MODID, "CBCMS");
	}

	public static void registerNeoForge(IEventBus modBus) {
		TAB_REGISTER.register(modBus);
	}

	public static void setDefaultTabToNull() {
		Cbcmoreshells.REGISTRATE.defaultCreativeTab((ResourceKey<CreativeModeTab>) null);
	}

}
