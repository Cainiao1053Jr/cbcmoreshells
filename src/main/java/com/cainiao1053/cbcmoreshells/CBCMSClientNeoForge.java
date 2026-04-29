package com.cainiao1053.cbcmoreshells;

import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

public class CBCMSClientNeoForge {
	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		modEventBus.addListener(CBCMSClientNeoForge::onClientSetup);
	}

	public static void onClientSetup(FMLClientSetupEvent event) {
		CbcmoreshellsClientCommon.onClientSetup();
	}

}
