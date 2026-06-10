package com.cainiao1053.cbcmoreshells.munitions.flak_cannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record FlakcannonProjectilePropertiesComponent(double addedRecoil, boolean canSquib, int projectileCount, int added_reload_tick, int reload_time) {

	public static final FlakcannonProjectilePropertiesComponent DEFAULT = new FlakcannonProjectilePropertiesComponent(0, false, 8, 0, 80);

	public static FlakcannonProjectilePropertiesComponent fromJson(String id, JsonObject obj) {
		double addedRecoil = Math.max(0, GsonHelper.getAsDouble(obj, "added_recoil", 1));
		boolean canSquib = GsonHelper.getAsBoolean(obj, "can_squib", true);
		int projectileCount = GsonHelper.getAsInt(obj, "projectile_count", 8);
		int added_reload_tick = GsonHelper.getAsInt(obj, "added_reload_tick", 0);
		int reload_time = GsonHelper.getAsInt(obj, "reload_time", 80);
		return new FlakcannonProjectilePropertiesComponent(addedRecoil, canSquib, projectileCount, added_reload_tick, reload_time);
	}

	public static FlakcannonProjectilePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new FlakcannonProjectilePropertiesComponent(buf.readDouble(), buf.readBoolean(), buf.readInt(), buf.readInt(), buf.readInt());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeDouble(this.addedRecoil)
			.writeBoolean(this.canSquib)
		.writeInt(this.projectileCount)
				.writeInt(this.added_reload_tick)
				.writeInt(this.reload_time);
	}

}
