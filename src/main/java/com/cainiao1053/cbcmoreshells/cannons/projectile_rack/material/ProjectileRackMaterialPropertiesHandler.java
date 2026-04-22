package com.cainiao1053.cbcmoreshells.cannons.projectile_rack.material;

import java.util.Map;
import java.util.concurrent.Executor;

import javax.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import it.unimi.dsi.fastutil.objects.Reference2ObjectOpenHashMap;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.PacketListener;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.player.Player;
import rbasamoyai.createbigcannons.multiloader.NetworkPlatform;
import rbasamoyai.createbigcannons.network.RootPacket;

public class ProjectileRackMaterialPropertiesHandler {

	public static final Map<ProjectileRackMaterial, ProjectileRackMaterialProperties> PROPERTIES = new Reference2ObjectOpenHashMap<>();

	public static class ReloadListener extends SimpleJsonResourceReloadListener {
		private static final Gson GSON = new Gson();
		public static final ReloadListener INSTANCE = new ReloadListener();

		public ReloadListener() { super(GSON, "projectile_rack_materials"); }

		@Override
		protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager resourceManager, ProfilerFiller profiler) {
			PROPERTIES.clear();

			for (Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
				JsonElement el = entry.getValue();
				if (!el.isJsonObject()) continue;
				try {
					ProjectileRackMaterial material = ProjectileRackMaterial.fromName(entry.getKey());
					PROPERTIES.put(material, ProjectileRackMaterialProperties.fromJson(entry.getKey(), el.getAsJsonObject()));
				} catch (Exception e) {

				}
			}
		}
	}

	public static ProjectileRackMaterialProperties getMaterial(ProjectileRackMaterial material) { return PROPERTIES.get(material); }

//	public static void writeBuf(FriendlyByteBuf buf) {
//		buf.writeVarInt(PROPERTIES.size());
//		for (Map.Entry<ProjectileRackMaterial, ProjectileRackMaterialProperties> entry : PROPERTIES.entrySet()) {
//			buf.writeResourceLocation(entry.getKey().name());
//			entry.getValue().writeBuf(buf);
//		}
//	}
//
//	public static void readBuf(FriendlyByteBuf buf) {
//		PROPERTIES.clear();
//		int sz = buf.readVarInt();
//
//		for (int i = 0; i < sz; ++i) {
//			PROPERTIES.put(ProjectileRackMaterial.fromName(buf.readResourceLocation()), ProjectileRackMaterialProperties.fromBuf(buf));
//		}
//	}

	public static void writeBuf(RegistryFriendlyByteBuf buf, ClientboundProjectileRackMaterialPropertiesPacket pkt) {
		buf.writeVarInt(pkt.properties.size());
		for (Map.Entry<ProjectileRackMaterial, ProjectileRackMaterialProperties> entry : pkt.properties.entrySet()) {
			buf.writeResourceLocation(entry.getKey().name());
			entry.getValue().writeBuf(buf);
		}
	}

	public static ClientboundProjectileRackMaterialPropertiesPacket readBuf(RegistryFriendlyByteBuf buf) {
		int sz = buf.readVarInt();
		Map<ProjectileRackMaterial, ProjectileRackMaterialProperties> properties = new Reference2ObjectOpenHashMap<>();
		for (int i = 0; i < sz; ++i)
			properties.put(ProjectileRackMaterial.fromName(buf.readResourceLocation()), ProjectileRackMaterialProperties.fromBuf(buf));
		return new ClientboundProjectileRackMaterialPropertiesPacket(properties);
	}

	public static void syncTo(ServerPlayer player) {
		NetworkPlatform.sendToClientPlayer(new ClientboundProjectileRackMaterialPropertiesPacket(), player);
	}

	public static void syncToAll(MinecraftServer server) {
		NetworkPlatform.sendToClientAll(new ClientboundProjectileRackMaterialPropertiesPacket(), server);
	}

//	public record ClientboundBigCannonMaterialPropertiesPacket(@Nullable FriendlyByteBuf buf) implements RootPacket {
//		public ClientboundBigCannonMaterialPropertiesPacket() { this(null); }
//
//		public static ClientboundBigCannonMaterialPropertiesPacket copyOf(FriendlyByteBuf buf) {
//			return new ClientboundBigCannonMaterialPropertiesPacket(new FriendlyByteBuf(buf.copy()));
//		}
//
//		@Override public void rootEncode(FriendlyByteBuf buf) { writeBuf(buf); }
//
//		@Override
//		public void handle(Executor exec, PacketListener listener, @Nullable ServerPlayer sender) {
//			if (this.buf != null) readBuf(this.buf);
//		}
//	}

	public record ClientboundProjectileRackMaterialPropertiesPacket(Map<ProjectileRackMaterial, ProjectileRackMaterialProperties> properties) implements RootPacket {
		public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundProjectileRackMaterialPropertiesPacket> STREAM_CODEC =
				StreamCodec.of(ProjectileRackMaterialPropertiesHandler::writeBuf, ProjectileRackMaterialPropertiesHandler::readBuf);

		public ClientboundProjectileRackMaterialPropertiesPacket() { this(new Reference2ObjectOpenHashMap<>(PROPERTIES)); }

		@Override
		public void handle(Executor exec, PacketListener listener, Player player) {
			PROPERTIES.clear();
			PROPERTIES.putAll(this.properties);
		}
	}

}
