package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.cainiao1053.cbcmoreshells.network.CBCMSNetwork;
import com.cainiao1053.cbcmoreshells.network.CBCMSNetworkImpl;
import com.google.gson.Gson;
import com.google.gson.JsonElement;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class DualCannonMaterialPropertiesHandler {

	public static final Map<DualCannonMaterial, DualCannonMaterialProperties> PROPERTIES = new Reference2ObjectOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() { super(GSON, "dual_cannon_materials"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			PROPERTIES.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					DualCannonMaterial material = DualCannonMaterial.fromName(entry.getKey());
					PROPERTIES.put(material, DualCannonMaterialProperties.fromJson(entry.getKey(), el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
		}
	}

	public static DualCannonMaterialProperties getMaterial(DualCannonMaterial material) { return PROPERTIES.get(material); }

	public static void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(PROPERTIES.size());
		for (Map.Entry<DualCannonMaterial, DualCannonMaterialProperties> entry : PROPERTIES.entrySet()) {
			buf.writeResourceLocation(entry.getKey().name());
			entry.getValue().writeBuf(buf);
		}
	}

	public static void readBuf(FriendlyByteBuf buf) {
		PROPERTIES.clear();
		int sz = buf.readVarInt();

		for (int i = 0; i < sz; ++i) {
			PROPERTIES.put(DualCannonMaterial.fromName(buf.readResourceLocation()), DualCannonMaterialProperties.fromBuf(buf));
		}
	}

	public static void syncTo(ServerPlayer player) {
		CBCMSNetworkImpl.sendToClientPlayer(new ClientboundBigCannonMaterialPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		CBCMSNetworkImpl.sendToClientAll(new ClientboundBigCannonMaterialPropertiesPacket(), server);
	}

	public record ClientboundBigCannonMaterialPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
		public ClientboundBigCannonMaterialPropertiesPacket() { this(null); }

		public static ClientboundBigCannonMaterialPropertiesPacket copyOf(FriendlyByteBuf buf) {
			return new ClientboundBigCannonMaterialPropertiesPacket(new FriendlyByteBuf(buf.copy()));
		}

		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }

		@Override
		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
			if (this.buf != null) readBuf(this.buf);
		}
	}

}
