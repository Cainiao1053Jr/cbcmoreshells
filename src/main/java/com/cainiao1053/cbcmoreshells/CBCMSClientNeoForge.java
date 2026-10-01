package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.items.cannon_combo.CannonComboItem;
import com.cainiao1053.cbcmoreshells.items.cannon_combo.CannonComboItemRenderer;
import com.simibubi.create.foundation.item.render.SimpleCustomRenderer;
import com.tterrag.registrate.util.entry.ItemEntry;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.extensions.common.RegisterClientExtensionsEvent;

import java.util.List;

public class CBCMSClientNeoForge {
	public static void prepareClient(IEventBus modEventBus, IEventBus forgeEventBus) {
		modEventBus.addListener(CBCMSClientNeoForge::onClientSetup);
		modEventBus.addListener(CBCMSClientNeoForge::onRegisterClientExtensions);
	}

	public static void onClientSetup(FMLClientSetupEvent event) {
		CbcmoreshellsClientCommon.onClientSetup();
	}

	public static void onRegisterClientExtensions(RegisterClientExtensionsEvent event) {
		CannonComboItemRenderer comboRenderer = new CannonComboItemRenderer();
		for (ItemEntry<? extends CannonComboItem> combo : List.of(CBCMSItems.DUAL_CANNON_COMBO, CBCMSItems.SINGLE_CANNON_COMBO)) {
			event.registerItem(SimpleCustomRenderer.create(combo.get(), comboRenderer), combo.get());
		}
	}

}
