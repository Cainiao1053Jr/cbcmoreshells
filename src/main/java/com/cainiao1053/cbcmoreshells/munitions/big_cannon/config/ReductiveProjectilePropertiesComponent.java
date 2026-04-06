package com.cainiao1053.cbcmoreshells.munitions.big_cannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record ReductiveProjectilePropertiesComponent(float percentReduction) {

	public static final ReductiveProjectilePropertiesComponent DEFAULT = new ReductiveProjectilePropertiesComponent(0.5f);

	public static ReductiveProjectilePropertiesComponent fromJson(String id, JsonObject obj) {
		float percentReduction = Math.max(0, GsonHelper.getAsFloat(obj, "percent_reduction", 0.5f));
		return new ReductiveProjectilePropertiesComponent(percentReduction);
	}

	public static ReductiveProjectilePropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new ReductiveProjectilePropertiesComponent(buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.percentReduction);
			;
	}

}
