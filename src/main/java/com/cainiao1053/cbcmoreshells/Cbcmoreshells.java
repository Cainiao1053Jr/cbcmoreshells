package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.cannon_control.cannon_types.CBCMSCannonContraptionTypes;
import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.placement.DualCannonPlacementHelper;
import com.cainiao1053.cbcmoreshells.index.*;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonProjectiles;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSDualCannonPropertyTypes;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.shaolib.CBCMSIncendiaryEffectService;
import com.cainiao1053.cbcmoreshells.network.CBCMSNetwork;
import com.cainiao1053.cbcmoreshells.network.CBCMSRootNetwork;
import com.mojang.logging.LogUtils;
import com.simibubi.create.foundation.data.CreateRegistrate;
import net.minecraft.resources.ResourceLocation;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import rbasamoyai.createbigcannons.utils.CBCUtils;

@Mod(Cbcmoreshells.MODID)
public class Cbcmoreshells {

    public static final String MODID = "cbcmoreshells";
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(MODID);
    public static final Logger LOGGER = LogUtils.getLogger();


    public Cbcmoreshells() {

//        IEventBus forgeEventBus = NeoForge.EVENT_BUS;
//        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
//        REGISTRATE.registerEventListeners(modEventBus);
//
//        modEventBus.addListener(this::addCreative);
//        // Register the commonSetup method for modloading
//        modEventBus.addListener(this::commonSetup);
//        modEventBus.addListener(Cbcmoreshells::init);
//
//        ModGroup.register(modEventBus);
//
//        CBCMSBlocks.register();
//        CBCMSEntityTypes.register();
//        CBCMSBlockEntities.register();
//        CBCMSBlockPartials.init();
//        CBCMSItems.register();
//        CBCMSCannonContraptionTypes.register();
//        CBCMSContraptionTypes.prepare();
//        CBCMSSoundEvents.prepare();
//        CBCMSRootNetwork.init();
//        CBCMSArmInteractionPointTypes.register();
//
//        MinecraftForge.EVENT_BUS.register(this);


    }

    public static void init() {
        ModGroup.register();
        ModGroup.setDefaultTabToNull();
        CBCMSBlocks.register();
        CBCMSEntityTypes.register();
        CBCMSBlockEntities.register();
        CBCMSBlockPartials.init();
        CBCMSItems.register();
        CBCMSCannonContraptionTypes.register();
        //CBCMSRecipeTypes.register();

        //CBCMSContraptionTypes.prepare();
        //CBCMSLangGen.prepare();
        CBCMSSoundEvents.prepare();
        CBCMSRootNetwork.init();
        DualCannonPlacementHelper.init();
        //CBCMSArmInteractionPointTypes.register();
        //CBCMSDataComponents.init();

        // Shaolib-backed dual cannon munitions. Property types must initialise before the
        // projectile types that resolve against them.
        CBCMSDualCannonPropertyTypes.register();
        CBCMSDualCannonProjectiles.register();
        CBCMSIncendiaryEffectService.install();

    }

    public static ResourceLocation resource(String path) {
        return CBCUtils.location(MODID, path);
    }

    public static void onCommonSetup() {

    }

}