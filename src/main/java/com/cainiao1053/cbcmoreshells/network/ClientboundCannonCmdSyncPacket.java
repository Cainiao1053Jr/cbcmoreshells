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

public record ClientboundCannonCmdSyncPacket(int entityId, boolean effect, int cooldown, int left) implements RootPacket {

	public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundCannonCmdSyncPacket> STREAM_CODEC =
		StreamCodec.composite(
			ByteBufCodecs.VAR_INT,  ClientboundCannonCmdSyncPacket::entityId,
			ByteBufCodecs.BOOL,     ClientboundCannonCmdSyncPacket::effect,
			ByteBufCodecs.VAR_INT,  ClientboundCannonCmdSyncPacket::cooldown,
			ByteBufCodecs.VAR_INT,  ClientboundCannonCmdSyncPacket::left,
			ClientboundCannonCmdSyncPacket::new
		);

//	@Override
//	public void rootEncode(FriendlyByteBuf buf) {
//		buf.writeVarInt(entityId);
//		buf.writeBoolean(effect);
//		buf.writeVarInt(cooldown);
//		buf.writeVarInt(left);
//	}

	@Override
	public void handle(Executor exec, PacketListener listener, Player player) {
		EnvExecute.executeOnClient(() -> () -> CBCMSClientHandlers.applyCannonCmdSync(entityId, effect, cooldown, left));
	}

}
