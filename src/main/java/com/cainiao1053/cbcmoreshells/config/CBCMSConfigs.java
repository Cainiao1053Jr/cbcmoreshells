package com.cainiao1053.cbcmoreshells.config;

import java.util.EnumMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

import org.apache.commons.lang3.tuple.Pair;

import com.simibubi.create.api.stress.BlockStressValues;

import net.createmod.catnip.config.ConfigBase;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

public class CBCMSConfigs {

	/*
	 * A copy of CBCConfigs. Yea, was also a bit lazy in making config setup LOL.
	 */


    private static final Map<ModConfig.Type, ConfigBase> CONFIGS = new EnumMap<>(ModConfig.Type.class);

//    private static CBCCfgClient client;
//    private static CBCCfgCommon common;
    private static CBCMSCfgServer server;

//    public static CBCCfgClient client() {
//        return client;
//    }
//
//    public static CBCCfgCommon common() {
//        return common;
//    }

    public static CBCMSCfgServer server() {
        return server;
    }

    public static ConfigBase byType(ModConfig.Type type) {
        return CONFIGS.get(type);
    }

    private static <T extends ConfigBase> T register(Supplier<T> factory, ModConfig.Type side) {
        Pair<T, ModConfigSpec> specPair = new ModConfigSpec.Builder().configure(builder -> {
            T config = factory.get();
            config.registerAll(builder);
            return config;
        });

        T config = specPair.getLeft();
        config.specification = specPair.getRight();
        CONFIGS.put(side, config);
        return config;
    }

    public static void register(BiConsumer<ModConfig.Type, ModConfigSpec> cons) {
//        client = register(CBCCfgClient::new, ModConfig.Type.CLIENT);
//        common = register(CBCCfgCommon::new, ModConfig.Type.COMMON);
        server = register(CBCMSCfgServer::new, ModConfig.Type.SERVER);

        for (Entry<ModConfig.Type, ConfigBase> pair : CONFIGS.entrySet())
            cons.accept(pair.getKey(), pair.getValue().specification);

        CBCMSCfgStress stress = server().kinetics.stress;
        BlockStressValues.IMPACTS.registerProvider(stress::getImpact);
        BlockStressValues.CAPACITIES.registerProvider(stress::getCapacity);
    }

    public static void onLoad(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                .getSpec())
                config.onLoad();
    }

    public static void onReload(ModConfig modConfig) {
        for (ConfigBase config : CONFIGS.values())
            if (config.specification == modConfig
                .getSpec())
                config.onReload();
    }

}
