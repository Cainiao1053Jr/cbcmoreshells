package com.cainiao1053.cbcmoreshells.cannons.torpedo_tube.breeches;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSyntaxException;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Block;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;
import rbasamoyai.createbigcannons.utils.CBCRegistryUtils;

import java.util.Map;
import java.util.concurrent.Executor;

public class TorpedoTubeBreechStrengthHandler {

	private static final Map<Block, Integer> BREECH_STRENGTHS = new Reference2IntOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		ReloadListener() { super(GSON, "torpedo_tube_breech_strength"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			BREECH_STRENGTHS.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					Block block = CBCRegistryUtils.getOptionalBlock(entry.getKey()).orElseThrow(() -> {
						return new JsonSyntaxException("Could not find big cannon breech block '" + entry.getKey() + "'");
					});
					int strength = Math.max(0, GsonHelper.getAsInt(el.getAsJsonObject(), "breech_strength"));
					BREECH_STRENGTHS.put(block, strength);
				} catch (Exception e) {

				}
			}
		}
	}

	public static int getStrength(Block block, int defaultStrength) { return BREECH_STRENGTHS.getOrDefault(block, defaultStrength); }

//	public static void writeBuf(FriendlyByteBuf buf) {
//		buf.writeVarInt(BREECH_STRENGTHS.size());
//		for (Map.Entry<Block, Integer> entry : BREECH_STRENGTHS.entrySet()) {
//			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()))
//				.writeVarInt(entry.getValue());
//		}
//	}
//
//	public static void readBuf(FriendlyByteBuf buf) {
//		BREECH_STRENGTHS.clear();
//		int sz = buf.readVarInt();
//
//		for (int i = 0; i < sz; ++i) {
//			BREECH_STRENGTHS.put(CBCRegistryUtils.getBlock(buf.readResourceLocation()), buf.readVarInt());
//		}
//	}

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundTorpedoTubeBreechStrengthPacket pkt) {
		buf.writeVarInt(pkt.strengths.size());
		for (Map.Entry<Block, Integer> entry : pkt.strengths.entrySet()) {
			buf.writeResourceLocation(CBCRegistryUtils.getBlockLocation(entry.getKey()))
					.writeVarInt(entry.getValue());
		}
	}

	public static ClientboundTorpedoTubeBreechStrengthPacket readBuf(RegistryFriendlyByteBuf buf) {
		int sz = buf.readVarInt();
		Map<Block, Integer> strengths = new Reference2IntOpenHashMap<>();
		for (int i = 0; i < sz; ++i)
			strengths.put(CBCRegistryUtils.getBlock(buf.readResourceLocation()), buf.readVarInt());
		return new ClientboundTorpedoTubeBreechStrengthPacket(strengths);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundTorpedoTubeBreechStrengthPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundTorpedoTubeBreechStrengthPacket(), server);
	}

//	public record ClientboundBigCannonBreechStrengthPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
//		public ClientboundBigCannonBreechStrengthPacket() { this(null); }
//
//		public static ClientboundBigCannonBreechStrengthPacket copyOf(FriendlyByteBuf buf) {
//			return new ClientboundBigCannonBreechStrengthPacket(new FriendlyByteBuf(buf.copy()));
//		}
//
//		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }
//
//		@Override
//		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
//			if (this.buf != null) readBuf(this.buf);
//		}
//	}

	public record ClientboundTorpedoTubeBreechStrengthPacket(Map<Block, Integer> strengths) implements RootPacket {
		public static final StreamCodec<RegistryFriendlyByteBuf, TorpedoTubeBreechStrengthHandler.ClientboundTorpedoTubeBreechStrengthPacket> STREAM_CODEC =
				StreamCodec.of(TorpedoTubeBreechStrengthHandler::writeBuf, TorpedoTubeBreechStrengthHandler::readBuf);

		public ClientboundTorpedoTubeBreechStrengthPacket() { this(new Reference2IntOpenHashMap<>(BREECH_STRENGTHS)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
			BREECH_STRENGTHS.clear();
			BREECH_STRENGTHS.putAll(this.strengths);
		}
	}

}
