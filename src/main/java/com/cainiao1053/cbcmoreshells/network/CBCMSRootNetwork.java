package com.cainiao1053.cbcmoreshells.network;

import com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material.DualCannonMaterialPropertiesHandler;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.network.RootPacket;

import java.util.function.Function;

public class CBCMSRootNetwork {

	private static final Int2ObjectMap<Function<FriendlyByteBuf, ? extends RootPacket>> ID_TO_CONSTRUCTOR = new Int2ObjectOpenHashMap<>();
	private static final Object2IntMap<Class<? extends RootPacket>> TYPE_TO_ID = new Object2IntOpenHashMap<>();

	public static final String VERSION = "12.2.0";

	public static void init() {
		int id = 0;
		addMsg(id++, ClientboundCBCMSTrailPacket.class, ClientboundCBCMSTrailPacket::new);
		addMsg(id++, ClientboundCBCMSSplashPacket.class, ClientboundCBCMSSplashPacket::new);
		addMsg(id++, ClientboundCannonCmdSyncPacket.class, ClientboundCannonCmdSyncPacket::new);
		addMsg(id++, DualCannonMaterialPropertiesHandler.ClientboundBigCannonMaterialPropertiesPacket.class,
				DualCannonMaterialPropertiesHandler.ClientboundBigCannonMaterialPropertiesPacket::copyOf);

	}

	private static <T extends RootPacket> void addMsg(int id, Class<T> clazz, Function<FriendlyByteBuf, T> decoder) {
		TYPE_TO_ID.put(clazz, id);
		ID_TO_CONSTRUCTOR.put(id, decoder);
	}

	public static RootPacket constructPacket(FriendlyByteBuf buf, int id) {
		if (!ID_TO_CONSTRUCTOR.containsKey(id)) throw new IllegalStateException("Attempted to deserialize packet with illegal id: " + id);
		return ID_TO_CONSTRUCTOR.get(id).apply(buf);
	}

	public static void writeToBuf(RootPacket pkt, FriendlyByteBuf buf) {
		int id = TYPE_TO_ID.getOrDefault(pkt.getClass(), -1);
		if (id == -1) throw new IllegalStateException("Attempted to serialize packet with illegal id: " + id);
		buf.writeVarInt(id);
		pkt.rootEncode(buf);
	}


}
