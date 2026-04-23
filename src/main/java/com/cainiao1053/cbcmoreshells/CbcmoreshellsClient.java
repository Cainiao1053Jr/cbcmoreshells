package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.client.render.DynamicOutliner;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.common.EventBusSubscriber;

//@EventBusSubscriber(modid = Cbcmoreshells.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class CbcmoreshellsClient {

    public static final DynamicOutliner CLIENT_LERPED_OUTLINER = new DynamicOutliner();

    public static void clientInit(){}

}