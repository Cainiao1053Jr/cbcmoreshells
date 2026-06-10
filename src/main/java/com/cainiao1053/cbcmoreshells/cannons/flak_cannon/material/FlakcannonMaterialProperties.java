package com.cainiao1053.cbcmoreshells.cannons.flak_cannon.material;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.GsonHelper;

public record FlakcannonMaterialProperties(int maxBarrelLength, float weight, float baseSpread,
										   float spreadReductionPerBarrel,
										   float baseSpeed, float speedIncreasePerBarrel, int maxSpeedIncreases,
										   int projectileLifetime, float baseRecoil, boolean connectsInSurvival,
										   boolean isWeldable, int weldDamage, int weldStressPenalty,
										   float damage_coefficient, int bullet_reload, float reload_coefficient) {

	public static FlakcannonMaterialProperties fromJson(JsonObject obj) {
		int maxBarrelLength = Math.max(1, GsonHelper.getAsInt(obj, "maximum_barrel_length"));
		float weight = Math.max(0, GsonHelper.getAsFloat(obj, "weight", 2));
		float baseSpread = Math.max(0.01f, GsonHelper.getAsFloat(obj, "base_spread", 3));
		float spreadReductionPerBarrel = Math.max(0, GsonHelper.getAsFloat(obj, "spread_reduction_per_barrel", 0.5f));
		float baseSpeed = Math.max(0.1f, GsonHelper.getAsFloat(obj, "base_speed", 1));
		float speedIncreasePerBarrel = Math.max(0, GsonHelper.getAsFloat(obj, "speed_increase_per_barrel", 0.5f));
		int maxSpeedIncreases = Math.max(0, GsonHelper.getAsInt(obj, "max_speed_increases", 2));
		int projectileLifetime = Math.max(1, GsonHelper.getAsInt(obj, "projectile_lifetime"));
		float baseRecoil = Math.max(0, GsonHelper.getAsFloat(obj, "base_recoil", 1));
		boolean connectsInSurvival = GsonHelper.getAsBoolean(obj, "connects_in_survival", true);
		boolean isWeldable = GsonHelper.getAsBoolean(obj, "is_weldable", false);
		int weldDamage = Math.max(GsonHelper.getAsInt(obj, "weld_damage", 0), 0);
		int weldStressPenalty = Math.max(GsonHelper.getAsInt(obj, "weld_stress_penalty", 0), 0);
		float damage_coefficient = Math.max(0, GsonHelper.getAsFloat(obj, "damage_coefficient", 1));
		int bullet_reload = GsonHelper.getAsInt(obj, "bullet_reload", 2);
		float reload_coefficient = Math.max(0, GsonHelper.getAsFloat(obj, "reload_coefficient", 1));
		return new FlakcannonMaterialProperties(maxBarrelLength, weight, baseSpread, spreadReductionPerBarrel, baseSpeed,
			speedIncreasePerBarrel, maxSpeedIncreases, projectileLifetime, baseRecoil, connectsInSurvival, isWeldable,
			weldDamage, weldStressPenalty, damage_coefficient, bullet_reload, reload_coefficient);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("maximum_barrel_length", this.maxBarrelLength);
		obj.addProperty("weight", this.weight);
		obj.addProperty("base_spread", this.baseSpread);
		obj.addProperty("spread_reduction_per_barrel", this.spreadReductionPerBarrel);
		obj.addProperty("base_speed", this.baseSpeed);
		obj.addProperty("speed_increase_per_barrel", this.speedIncreasePerBarrel);
		obj.addProperty("max_speed_increases", this.maxSpeedIncreases);
		obj.addProperty("projectile_lifetime", this.projectileLifetime);
		obj.addProperty("base_recoil", this.baseRecoil);
		obj.addProperty("connects_in_survival", this.connectsInSurvival);
		obj.addProperty("is_weldable", this.isWeldable);
		obj.addProperty("weld_damage", this.weldDamage);
		obj.addProperty("weld_stress_penalty", this.weldStressPenalty);
		obj.addProperty("damage_coefficient", this.damage_coefficient);
		obj.addProperty("bullet_reload", this.bullet_reload);
		obj.addProperty("reload_coefficient", this.reload_coefficient);
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeVarInt(this.maxBarrelLength);
		buf.writeFloat(this.weight)
			.writeFloat(this.baseSpread)
			.writeFloat(this.spreadReductionPerBarrel)
			.writeFloat(this.baseSpeed)
			.writeFloat(this.speedIncreasePerBarrel);
		buf.writeVarInt(this.maxSpeedIncreases)
			.writeVarInt(this.projectileLifetime)
			.writeFloat(this.baseRecoil)
			.writeBoolean(this.connectsInSurvival)
			.writeBoolean(this.isWeldable);
		buf.writeVarInt(this.weldDamage)
			.writeVarInt(this.weldStressPenalty)
				.writeFloat(this.damage_coefficient);
		buf.writeVarInt(this.bullet_reload)
				.writeFloat(this.reload_coefficient);
	}

	public static FlakcannonMaterialProperties fromBuf(FriendlyByteBuf buf) {
		int maxBarrelLength = buf.readVarInt();
		float weight = buf.readFloat();
		float baseSpread = buf.readFloat();
		float spreadReductionPerBarrel = buf.readFloat();
		float baseSpeed = buf.readFloat();
		float speedIncreasePerBarrel = buf.readFloat();
		int maxSpeedIncreases = buf.readVarInt();
		int projectileLifetime = buf.readVarInt();
		float baseRecoil = buf.readFloat();
		boolean connectsInSurvival = buf.readBoolean();
		boolean isWeldable = buf.readBoolean();
		int weldDamage = buf.readVarInt();
		int weldStressPenalty = buf.readVarInt();
		float damage_coefficient = buf.readFloat();
		int bullet_reload = buf.readVarInt();
		float reload_coefficient = buf.readFloat();
		return new FlakcannonMaterialProperties(maxBarrelLength, weight, baseSpread, spreadReductionPerBarrel, baseSpeed,
			speedIncreasePerBarrel, maxSpeedIncreases, projectileLifetime, baseRecoil, connectsInSurvival, isWeldable,
			weldDamage, weldStressPenalty, damage_coefficient, bullet_reload, reload_coefficient);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private int maxBarrelLength;
		private float weight;
		private float baseSpread;
		private float spreadReductionPerBarrel;
		private float baseSpeed;
		private float speedIncreasePerBarrel;
		private int maxSpeedIncreases;
		private int projectileLifetime;
		private float baseRecoil;
		private boolean connectsInSurvival;
		private boolean isWeldable;
		private int weldDamage;
		private int weldStressPenalty;
		private float damage_coefficient;
		private int bullet_reload;
		private float reload_coefficient;

		private Builder() {
		}

		public Builder maxBarrelLength(int maxBarrelLength) {
			this.maxBarrelLength = maxBarrelLength;
			return this;
		}

		public Builder weight(float weight) {
			this.weight = weight;
			return this;
		}

		public Builder baseSpread(float baseSpread) {
			this.baseSpread = baseSpread;
			return this;
		}

		public Builder spreadReductionPerBarrel(float spreadReductionPerBarrel) {
			this.spreadReductionPerBarrel = spreadReductionPerBarrel;
			return this;
		}

		public Builder baseSpeed(float baseSpeed) {
			this.baseSpeed = baseSpeed;
			return this;
		}

		public Builder speedIncreasePerBarrel(float speedIncreasePerBarrel) {
			this.speedIncreasePerBarrel = speedIncreasePerBarrel;
			return this;
		}

		public Builder maxSpeedIncreases(int maxSpeedIncreases) {
			this.maxSpeedIncreases = maxSpeedIncreases;
			return this;
		}

		public Builder projectileLifetime(int projectileLifetime) {
			this.projectileLifetime = projectileLifetime;
			return this;
		}

		public Builder baseRecoil(float baseRecoil) {
			this.baseRecoil = baseRecoil;
			return this;
		}

		public Builder connectsInSurvival(boolean connectsInSurvival) {
			this.connectsInSurvival = connectsInSurvival;
			return this;
		}

		public Builder isWeldable(boolean isWeldable) {
			this.isWeldable = isWeldable;
			return this;
		}

		public Builder weldDamage(int weldDamage) {
			this.weldDamage = weldDamage;
			return this;
		}

		public Builder weldStressPenalty(int weldStressPenalty) {
			this.weldStressPenalty = weldStressPenalty;
			return this;
		}

		public Builder damage_coefficient(float damage_coefficient) {
			this.damage_coefficient = damage_coefficient;
			return this;
		}

		public Builder bullet_reload(int bullet_reload) {
			this.bullet_reload = bullet_reload;
			return this;
		}

		public Builder reload_coefficient(float reload_coefficient) {
			this.reload_coefficient = reload_coefficient;
			return this;
		}

		public FlakcannonMaterialProperties build() {
			return new FlakcannonMaterialProperties(this.maxBarrelLength, this.weight, this.baseSpread,
				this.spreadReductionPerBarrel, this.baseSpeed, this.speedIncreasePerBarrel,
				this.maxSpeedIncreases, this.projectileLifetime, this.baseRecoil, this.connectsInSurvival,
				this.isWeldable, this.weldDamage, this.weldStressPenalty, this.damage_coefficient, this.bullet_reload,
					this.reload_coefficient);
		}
	}

}
