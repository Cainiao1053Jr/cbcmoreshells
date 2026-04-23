package com.cainiao1053.cbcmoreshells.network;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.EnvExecute;
import rbasamoyai.createbigcannons.network.RootPacket;

import java.util.concurrent.Executor;

public record ClientboundCBCMSSplashPacket(double x, double y, double z, double xOld, double yOld, double zOld) implements RootPacket {

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCBCMSSplashPacket> STREAM_CODEC =
		StreamCodec.composite(
			ByteBufCodecs.DOUBLE, ClientboundCBCMSSplashPacket::x,
			ByteBufCodecs.DOUBLE, ClientboundCBCMSSplashPacket::y,
			ByteBufCodecs.DOUBLE, ClientboundCBCMSSplashPacket::z,
			ByteBufCodecs.DOUBLE, ClientboundCBCMSSplashPacket::xOld,
			ByteBufCodecs.DOUBLE, ClientboundCBCMSSplashPacket::yOld,
			ByteBufCodecs.DOUBLE, ClientboundCBCMSSplashPacket::zOld,
			ClientboundCBCMSSplashPacket::new
		);

//	@Override
//	public void rootEncode(FriendlyByteBuf buf) {
//		buf.writeDouble(this.x)
//			.writeDouble(this.y)
//			.writeDouble(this.z)
//			.writeDouble(this.xOld)
//			.writeDouble(this.yOld)
//			.writeDouble(this.zOld);
//	}

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCMSClientHandlers.addSplashFromServer(this));
	}

}
