package com.cainiao1053.cbcmoreshells.network;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.MainThreadPayloadHandler;
import net.neoforged.neoforge.network.registration.HandlerThread;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;


//public class CBCMSNetwork {
//
////	public static final String VERSION = "2.0.0";
////
////	public static final SimpleChannel INSTANCE = construct();
////
////	public static SimpleChannel construct() {
////		SimpleChannel channel = NetworkRegistry.ChannelBuilder
////				.named(Cbcmoreshells.resource("network"))
////				.clientAcceptedVersions(VERSION::equals)
////				.serverAcceptedVersions(VERSION::equals)
////				.networkProtocolVersion(() -> VERSION)
////				.simpleChannel();
////
////		int id = 0;
////
//////		channel.messageBuilder(ForgeServerPacket.class, id++)
//////				.encoder(ForgeServerPacket::encode)
//////				.decoder(ForgeServerPacket::new)
//////				.consumerMainThread(ForgeServerPacket::handle)
//////				.add();
////
////		channel.messageBuilder(CBCMSClientPacket.class, id++)
////				.encoder(CBCMSClientPacket::encode)
////				.decoder(CBCMSClientPacket::new)
////				.consumerMainThread(CBCMSClientPacket::handle)
////				.add();
////
////		return channel;
////	}
//
//
//
//	public static void init() {}
//
//}

@EventBusSubscriber(modid = Cbcmoreshells.MODID)
public class CBCMSNetwork {

	public static final String VERSION = "3.0.0";

	@SubscribeEvent
	public static void onRegisterPayloadHandlers(RegisterPayloadHandlersEvent evt) {
		final PayloadRegistrar registrar = evt.registrar(VERSION)
				.executesOn(HandlerThread.MAIN);

		registrar.playBidirectional(CBCMSClientPacket.TYPE, CBCMSClientPacket.STREAM_CODEC, new MainThreadPayloadHandler<>(CBCMSClientPacket::handlePacket));
	}

}
