package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.cannon_control.cannon_types.CBCMSCannonContraptionTypes;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialPropertiesHandler;
import com.cainiao1053.cbcmoreshells.index.*;
import com.cainiao1053.cbcmoreshells.network.CBCMSNetwork;
import com.cainiao1053.cbcmoreshells.network.CBCMSRootNetwork;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.OnDatapackSyncEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.utils.CBCUtils;

@Mod(Cbcmoreshells.MODID)
public class Cbcmoreshells {

    public static final String MODID = "cbcmoreshells";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public static final Logger LOGGER = LogUtils.getLogger();


    public Cbcmoreshells() {

        IEventBus forgeEventBus = MinecraftForge.EVENT_BUS;
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        REGISTRATE.registerEventListeners(modEventBus);

        modEventBus.addListener(this::addCreative);
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(Cbcmoreshells::init);

        ModGroup.register(modEventBus);

        CBCMSBlocks.register();
        CBCMSEntityTypes.register();
        CBCMSBlockEntities.register();
        CBCMSBlockPartials.init();
        CBCMSItems.register();
        CBCMSCannonContraptionTypes.register();
        //CBCMSRecipeTypes.register();

        //CBCMSArmInteractionPointTypes.register();

        CBCMSContraptionTypes.prepare();
        //CBCMSLangGen.prepare();
        CBCMSSoundEvents.prepare();
        CBCMSRootNetwork.init();
        CBCMSArmInteractionPointTypes.register();

        DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> CbcmoreshellsClient::clientInit);

        MinecraftForge.EVENT_BUS.register(this);


    }

    public static void init(final FMLCommonSetupEvent event) {

        event.enqueueWork(() -> {
            // TODO: custom registration should all happen in one place
            // Most registration happens in the constructor.
            // These registrations use Create's registered objects directly so they must run after registration has finished.
            // --


        });
    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {
        // Some common setup code
        CBCMSNetwork.init();

    }

    private void clientSetup(FMLClientSetupEvent event)
    {

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {

    }

    @SubscribeEvent
    public void onAddReloadListeners(AddReloadListenerEvent event) {
        event.addListener(DualCannonMaterialPropertiesHandler.ReloadListener.INSTANCE);
    }

    @SubscribeEvent
    public void onDatapackSync(OnDatapackSyncEvent event) {
        if (event.getPlayer() == null) {
            DualCannonMaterialPropertiesHandler.syncToAll(event.getPlayerList().getServer());
        } else {
            DualCannonMaterialPropertiesHandler.syncTo(event.getPlayer());
        }
    }

    public static ResourceLocation resource(String path) {
        return CBCUtils.location(MODID, path);
    }

}