package com.cainiao1053.cbcmoreshells.network;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialPropertiesHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import rbasamoyai.createbigcannons.network.RootPacket;

import java.util.function.Function;

public class CBCMSRootNetwork {

	private static final Int2ObjectMap<StreamCodec<RegistryFriendlyByteBuf, ? extends RootPacket>> ID_TO_STREAM_CODEC = new Int2ObjectOpenHashMap<>();
	private static final Object2IntMap<Class<? extends RootPacket>> TYPE_TO_ID = new Object2IntOpenHashMap<>();

	public static final String VERSION = "15.0.0";

	public static final StreamCodec<RegistryFriendlyByteBuf, RootPacket> PACKET_STREAM_CODEC = ByteBufCodecs.VAR_INT.<RegistryFriendlyByteBuf>cast()
			.dispatch(CBCMSRootNetwork::getPacketId, CBCMSRootNetwork::getStreamCodec);

	public static void init() {
		int id = 0;
		addMsg(id++, ClientboundCBCMSTrailPacket.class, ClientboundCBCMSTrailPacket.STREAM_CODEC);
		addMsg(id++, ClientboundCBCMSSplashPacket.class, ClientboundCBCMSSplashPacket.STREAM_CODEC);
		addMsg(id++, ClientboundCannonCmdSyncPacket.class, ClientboundCannonCmdSyncPacket.STREAM_CODEC);
		addMsg(id++, DualCannonMaterialPropertiesHandler.ClientboundDualCannonMaterialPropertiesPacket.class,
				DualCannonMaterialPropertiesHandler.ClientboundDualCannonMaterialPropertiesPacket.STREAM_CODEC);
	}

	private static <T extends RootPacket> void addMsg(int id, Class<T> clazz, StreamCodec<RegistryFriendlyByteBuf, T> streamCodec) {
		TYPE_TO_ID.put(clazz, id);
		ID_TO_STREAM_CODEC.put(id, streamCodec);
	}

	public static int getPacketId(RootPacket pkt) {
		int id = TYPE_TO_ID.getOrDefault(pkt.getClass(), -1);
		if (id == -1)
			throw new IllegalStateException("Attempted to serialize packet with illegal id: " + id);
		return id;
	}

	public static StreamCodec<RegistryFriendlyByteBuf, ? extends RootPacket> getStreamCodec(int id) {
		if (!ID_TO_STREAM_CODEC.containsKey(id))
			throw new IllegalStateException("Attempted to deserialize packet with illegal id: " + id);
		return ID_TO_STREAM_CODEC.get(id);
	}

	public static void onPlayerJoin(ServerPlayer player) {
		DualCannonMaterialPropertiesHandler.syncTo(player);
	}

//	public static RootPacket constructPacket(FriendlyByteBuf buf, int id) {
//		if (!ID_TO_CONSTRUCTOR.containsKey(id)) throw new IllegalStateException("Attempted to deserialize packet with illegal id: " + id);
//		return ID_TO_CONSTRUCTOR.get(id).apply(buf);
//	}
//
//	public static void writeToBuf(RootPacket pkt, FriendlyByteBuf buf) {
//		int id = TYPE_TO_ID.getOrDefault(pkt.getClass(), -1);
//		if (id == -1) throw new IllegalStateException("Attempted to serialize packet with illegal id: " + id);
//		buf.writeVarInt(id);
//		pkt.rootEncode(buf);
//	}


}
