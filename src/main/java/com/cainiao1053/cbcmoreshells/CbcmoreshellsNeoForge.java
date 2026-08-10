package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlockEntity;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialPropertiesHandler;
import com.cainiao1053.cbcmoreshells.compat.sable.CBCMSSableCompat;
import com.cainiao1053.cbcmoreshells.config.CBCMSConfigs;
import com.cainiao1053.cbcmoreshells.index.CBCMSArmInteractionPointTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.cainiao1053.cbcmoreshells.index.CBCMSContraptionTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSDataComponents;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import com.cainiao1053.cbcmoreshells.network.CBCMSRootNetwork;
import net.createmod.catnip.platform.CatnipServices;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;


@Mod(Cbcmoreshells.MODID)
public class CbcmoreshellsNeoForge {

    //public static final DeferredRegister<ParticleType<?>> PARTICLE_REGISTER = DeferredRegister.create(Registries.PARTICLE_TYPE, Cbcmoreshells.MODID);
    public static final DeferredRegister<RecipeSerializer<?>> RECIPE_SERIALIZER_REGISTER = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Cbcmoreshells.MODID);
    public static final DeferredRegister<RecipeType<?>> RECIPE_TYPE_REGISTER = DeferredRegister.create(Registries.RECIPE_TYPE, Cbcmoreshells.MODID);

    public CbcmoreshellsNeoForge(IEventBus modEventBus) {
        IEventBus forgeEventBus = NeoForge.EVENT_BUS;
        ModContainer mlContext = ModLoadingContext.get().getActiveContainer();

        RECIPE_SERIALIZER_REGISTER.register(modEventBus);
        RECIPE_TYPE_REGISTER.register(modEventBus);
        ModGroup.registerNeoForge(modEventBus);

        Cbcmoreshells.REGISTRATE.registerEventListeners(modEventBus);
        Cbcmoreshells.init();
        CBCMSConfigs.register(mlContext::registerConfig);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onNewRegistry);
        modEventBus.addListener(this::onLoadConfig);
        modEventBus.addListener(this::onReloadConfig);
        //modEventBus.addListener(this::onRegisterSounds);
		modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::onRegisterCapabilities);

        forgeEventBus.addListener(this::onAddReloadListeners);
        forgeEventBus.addListener(this::onPlayerLoggedIn);

        CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> CBCMSClientNeoForge.prepareClient(modEventBus, forgeEventBus));

    }

    private void onCommonSetup(FMLCommonSetupEvent event) {

		Cbcmoreshells.onCommonSetup();
        CBCMSModsNeoForge.SABLE.executeIfInstalled(() -> CBCMSSableCompat::init);
    }

    private void onNewRegistry(NewRegistryEvent evt) {
    }

	private void onRegister(RegisterEvent evt) {
		ResourceKey<? extends Registry<?>> key = evt.getRegistryKey();
        CBCMSContraptionTypes.prepare();
        CBCMSArmInteractionPointTypes.register();
        CBCMSDataComponents.init();
	}

    private void onRegisterSounds(RegisterEvent event) {
        event.register(Registries.SOUND_EVENT, helper -> CBCMSSoundEvents.register(soundEntry -> soundEntry.register(helper)));
    }

    private void onRegisterCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(
            Capabilities.ItemHandler.BLOCK,
            CBCMSBlockEntities.AMMO_RACK.get(),
            (be, side) -> be.getItemHandler()
        );
    }

    private void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(DualCannonMaterialPropertiesHandler.ReloadListener.INSTANCE);
    }

    private void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            CBCMSRootNetwork.onPlayerJoin(player);
        }
    }

    private void onLoadConfig(ModConfigEvent.Loading evt) {
        CBCMSConfigs.onLoad(evt.getConfig());
    }

    private void onReloadConfig(ModConfigEvent.Reloading evt) {
        CBCMSConfigs.onReload(evt.getConfig());
    }

}
