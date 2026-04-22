package com.cainiao1053.cbcmoreshells.network;

import net.minecraft.network.FriendlyByteBuf;
import rbasamoyai.createbigcannons.network.RootPacket;

import java.util.function.Supplier;

public class CBCMSClientPacket {

	private final RootPacket pkt;

	public CBCMSClientPacket(RootPacket pkt) { this.pkt = pkt; }

	public CBCMSClientPacket(FriendlyByteBuf buf) {
		this.pkt = CBCMSRootNetwork.constructPacket(buf, buf.readVarInt());
	}

	public void encode(FriendlyByteBuf buf) {
		CBCMSRootNetwork.writeToBuf(this.pkt, buf);
	}

	public void handle(Supplier<NetworkEvent.Context> sup) {
		NetworkEvent.Context ctx = sup.get();
		ctx.enqueueWork(() -> {
			this.pkt.handle(ctx::enqueueWork, ctx.getNetworkManager().getPacketListener(), null);
		});
		ctx.setPacketHandled(true);
	}

}
