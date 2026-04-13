package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record DualCannonPropertiesComponent(
		float addedChargePower, float minimumChargePower, boolean canSquib, float addedRecoil, float initialVel,
											float projectileSpread, float minimumSpread, float smashToughness,
								int lifetime, float maximumMomentum, float reloadTimeCoef, float cooldownReductionRate
		) {

	public static final DualCannonPropertiesComponent DEFAULT = new DualCannonPropertiesComponent(0, 0, false, 0,
			4, 4, 1, 6,
			30, 250, 1, 2
	);

	public static DualCannonPropertiesComponent fromJson(String id, JsonObject obj) {
		float addedChargePower = Math.max(0, GsonHelper.getAsFloat(obj, "added_charge_power", 0));
		float minimumChargePower = Math.max(0, GsonHelper.getAsFloat(obj, "minimum_charge_power", 1));
		boolean canSquib = GsonHelper.getAsBoolean(obj, "can_squib", true);
		float addedRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "added_recoil", 1));;
		float initialVel = Math.max(0, GsonHelper.getAsFloat(obj, "initial_vel", 4f));
		float projectileSpread = Math.max(0, GsonHelper.getAsFloat(obj, "projectile_spread", 4f));
		float minimumSpread = Math.max(0, GsonHelper.getAsFloat(obj, "minimum_spread", 1f));
		float smashToughness = Math.max(0, GsonHelper.getAsFloat(obj, "smash_toughness", 6f));
		int lifetime = Math.max(0, GsonHelper.getAsInt(obj, "lifetime", 30));
		float maximumMomentum = Math.max(0, GsonHelper.getAsFloat(obj, "maximum_momentum", 250f));
		float reloadTimeCoef = Math.max(0, GsonHelper.getAsFloat(obj, "reload_time_coef", 1f));
		float cooldownReductionRate = Math.max(0, GsonHelper.getAsFloat(obj, "cooldown_reduction_rate", 2f));
		return new DualCannonPropertiesComponent(addedChargePower, minimumChargePower, canSquib, addedRecoil, initialVel, projectileSpread, minimumSpread, smashToughness, lifetime, maximumMomentum, reloadTimeCoef, cooldownReductionRate);
	}

	public static DualCannonPropertiesComponent fromNetwork(FriendlyByteBuf buf) {
		return new DualCannonPropertiesComponent(buf.readFloat(), buf.readFloat(), buf.readBoolean(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readFloat(), buf.readInt(), buf.readFloat(), buf.readFloat(), buf.readFloat());
	}

	public void toNetwork(FriendlyByteBuf buf) {
		buf.writeFloat(this.addedChargePower)
			.writeFloat(this.minimumChargePower)
			.writeBoolean(this.canSquib)
			.writeFloat(this.addedRecoil)
			.writeFloat(this.initialVel)
		    .writeFloat(this.projectileSpread)
		    .writeFloat(this.minimumSpread)
			.writeFloat(this.smashToughness)
			.writeInt(this.lifetime)
			.writeFloat(this.maximumMomentum)
			.writeFloat(this.reloadTimeCoef)
			.writeFloat(this.cooldownReductionRate);
	}

}
