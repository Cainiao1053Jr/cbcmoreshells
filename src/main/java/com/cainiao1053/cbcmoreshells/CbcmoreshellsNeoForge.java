package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.blocks.ammo_rack.AmmoRackBlockEntity;
import com.cainiao1053.cbcmoreshells.config.CBCMSConfigs;
import com.cainiao1053.cbcmoreshells.index.CBCMSArmInteractionPointTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSBlockEntities;
import com.cainiao1053.cbcmoreshells.index.CBCMSContraptionTypes;
import com.cainiao1053.cbcmoreshells.index.CBCMSDataComponents;
import com.cainiao1053.cbcmoreshells.index.CBCMSSoundEvents;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
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
        //CBCMSParticleTypes.register();
        //PARTICLE_REGISTER.register(modEventBus);
        CBCMSConfigs.register(mlContext::registerConfig);

        modEventBus.addListener(this::onCommonSetup);
        modEventBus.addListener(this::onNewRegistry);
        modEventBus.addListener(this::onLoadConfig);
        modEventBus.addListener(this::onReloadConfig);
        modEventBus.addListener(this::onRegisterSounds);
		modEventBus.addListener(this::onRegister);
        modEventBus.addListener(this::onRegisterCapabilities);

        //CBCCommonNeoForgeEvents.register(modEventBus, forgeEventBus);

		//CBCModsNeoForge.CURIOS.executeIfInstalled(() -> () -> CBCCuriosIntegration.init(modEventBus, forgeEventBus));

        //CatnipServices.PLATFORM.executeOnClientOnly(() -> () -> CBCClientNeoForge.prepareClient(modEventBus, forgeEventBus));
    }

    private void onCommonSetup(FMLCommonSetupEvent event) {
//		BlockArmorInspectionToolItem.registerDefaultHandlers();
//		GasMaskItem.registerDefaultHandlers();
//        DefaultFluidCompat.registerMinecraftBlobEffects();
//        DefaultFluidCompat.registerCreateBlobEffects();

		Cbcmoreshells.onCommonSetup();
		//DefaultCreateCompat.init();
		//DefaultCannonMountPropertiesSerializers.init();
		//CBCModsNeoForge.COPYCATS.executeIfInstalled(() -> () -> CopycatsCompat.init(CBCModsNeoForge.COPYCATS::getBlock));
		//CBCModsNeoForge.FRAMEDBLOCKS.executeIfInstalled(() -> () -> FramedBlocksCompat.init());
        //CBCModsNeoForge.SABLE.executeIfInstalled(() -> () -> SableCompat.init());
    }

    private void onNewRegistry(NewRegistryEvent evt) {
//        evt.create(new RegistryBuilder<>(CBCRegistries.BLOCK_RECIPE_SERIALIZERS)
//			.defaultKey(CreateBigCannons.resource("cannon_casting"))
//            .sync(true));
//
//		evt.create(new RegistryBuilder<>(CBCRegistries.BLOCK_RECIPE_TYPES)
//			.defaultKey(CreateBigCannons.resource("cannon_casting"))
//            .sync(true));
//
//		evt.create(new RegistryBuilder<>(CBCRegistries.CANNON_CAST_SHAPES)
//            .defaultKey(CreateBigCannons.resource("very_small"))
//            .sync(true));
    }

	private void onRegister(RegisterEvent evt) {
		ResourceKey<? extends Registry<?>> key = evt.getRegistryKey();
//		if (CBCRegistries.BLOCK_RECIPE_SERIALIZERS.equals(key)) {
//			BlockRecipeSerializer.register();
//		} else if (CBCRegistries.BLOCK_RECIPE_TYPES.equals(key)) {
//			BlockRecipeType.register();
//		} else if (CBCRegistries.CANNON_CAST_SHAPES.equals(key)) {
//			CannonCastShape.register();
//		}
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

    private void onLoadConfig(ModConfigEvent.Loading evt) {
        CBCMSConfigs.onLoad(evt.getConfig());
    }

    private void onReloadConfig(ModConfigEvent.Reloading evt) {
        CBCMSConfigs.onReload(evt.getConfig());
    }

}
