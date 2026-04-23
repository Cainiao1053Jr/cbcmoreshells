package com.cainiao1053.cbcmoreshells.network;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.PacketDistributor;
import rbasamoyai.createbigcannons.network.RootPacket;

public class CBCMSNetworkImpl {

	public static void sendToClientPlayer(RootPacket pkt, ServerPlayer player) {
		PacketDistributor.sendToPlayer(player, new CBCMSClientPacket(pkt));
	}

	public static void sendToClientTracking(RootPacket pkt, Entity tracked) {
		PacketDistributor.sendToPlayersTrackingEntity(tracked, new CBCMSClientPacket(pkt));
	}

	public static void sendToClientAll(RootPacket pkt, MinecraftServer server) {
		PacketDistributor.sendToAllPlayers(new CBCMSClientPacket(pkt));
	}

}
