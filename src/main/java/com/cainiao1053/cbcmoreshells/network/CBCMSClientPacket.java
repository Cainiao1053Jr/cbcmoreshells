package com.cainiao1053.cbcmoreshells.network;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import rbasamoyai.createbigcannons.network.RootPacket;

import java.util.function.Supplier;

//public class CBCMSClientPacket {
//
//	private final RootPacket pkt;
//
//	public CBCMSClientPacket(RootPacket pkt) { this.pkt = pkt; }
//
//	public CBCMSClientPacket(FriendlyByteBuf buf) {
//		this.pkt = CBCMSRootNetwork.constructPacket(buf, buf.readVarInt());
//	}
//
//	public void encode(FriendlyByteBuf buf) {
//		CBCMSRootNetwork.writeToBuf(this.pkt, buf);
//	}
//
//	public void handle(Supplier<NetworkEvent.Context> sup) {
//		NetworkEvent.Context ctx = sup.get();
//		ctx.enqueueWork(() -> {
//			this.pkt.handle(ctx::enqueueWork, ctx.getNetworkManager().getPacketListener(), null);
//		});
//		ctx.setPacketHandled(true);
//	}
//
//}

public record CBCMSClientPacket(RootPacket pkt) implements CustomPacketPayload {

	public static final Type<CBCMSClientPacket> TYPE = new Type<>(Cbcmoreshells.resource("neoforge_packet"));

	public static final StreamCodec<RegistryFriendlyByteBuf, CBCMSClientPacket> STREAM_CODEC = CBCMSRootNetwork.PACKET_STREAM_CODEC
			.map(CBCMSClientPacket::new, CBCMSClientPacket::pkt);

	public static void handlePacket(CBCMSClientPacket nfPkt, IPayloadContext ctx) {
		nfPkt.pkt.handle(ctx::enqueueWork, ctx.listener(), ctx.player());
	}

	@Override public Type<? extends CustomPacketPayload> type() { return TYPE; }

}
