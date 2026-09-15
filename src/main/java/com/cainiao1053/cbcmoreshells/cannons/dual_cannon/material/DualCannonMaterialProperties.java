package com.cainiao1053.cbcmoreshells.cannons.dual_cannon.material;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.google.gson.JsonObject;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.StringRepresentable;
import rbasamoyai.createbigcannons.CreateBigCannons;

public record DualCannonMaterialProperties(double minimumVelocityPerBarrel, float weight, int maxSafePropellantStress,
										  FailureMode failureMode, boolean connectsInSurvival, boolean isWeldable,
										  int weldDamage, int weldStressPenalty, float minimumSpread,
										  float spreadReductionPerBarrel, int addedLifetime, float reloadTimeModifier,
										   float durabilityMassModifier, float barrelGap, int combatCommandCooldown, int combatCommandDuration, boolean isSingleBarrel,
										   float recoilMultiplier, int refCaliber
) {

	public DualCannonMaterialProperties {
		Objects.requireNonNull(failureMode, "property :failureMode is required");
	}

	public boolean mayGetStuck(float chargesUsed, int barrelTravelled) {
		if (this.minimumVelocityPerBarrel < 0) return true;
		if (barrelTravelled < 1) return false;
		return this.minimumVelocityPerBarrel > chargesUsed / barrelTravelled;
	}

	public static DualCannonMaterialProperties fromJson(ResourceLocation id, JsonObject obj) {
		double minimumVelocityPerBarrel = GsonHelper.getAsDouble(obj, "minimum_velocity_per_barrel", 1);
		// Legacy config handling
		if (GsonHelper.isNumberValue(obj, "squib_ratio_barrels") && GsonHelper.isNumberValue(obj, "squib_ratio_propellant")) {
			float barrels = (float) GsonHelper.getAsInt(obj, "squib_ratio_barrels");
			float propellant = (float) GsonHelper.getAsInt(obj, "squib_ratio_propellant");
			float ratio = propellant <= 0 ? -1 : barrels / propellant;
			if (ratio < 0) ratio = -1;
			CreateBigCannons.LOGGER.warn("Legacy values \"squib_ratio_barrels\" and \"squib_ratio_propellant\" found in config for {}, please change to \"max_safe_propellant_stress\". Recommended value: {}", id, String.format(".%2f", ratio));
			if (!GsonHelper.isNumberValue(obj, "minimum_velocity_per_barrel")) {
				minimumVelocityPerBarrel = ratio;
			}
		}
		minimumVelocityPerBarrel = Math.max(-1, minimumVelocityPerBarrel);

		float weight = Math.max(0, GsonHelper.getAsFloat(obj, "weight", 2));

		int maxSafeBaseCharges = GsonHelper.getAsInt(obj, "max_safe_propellant_stress", 2);
		// Legacy config handling
		if (GsonHelper.isNumberValue(obj, "max_safe_charges")) {
			CreateBigCannons.LOGGER.warn("Legacy value \"max_safe_charges\" found in config for {}, please change to \"max_safe_propellant_stress\"", id);
			if (!GsonHelper.isNumberValue(obj, "max_safe_propellant_stress"))
				maxSafeBaseCharges = GsonHelper.getAsInt(obj, "max_safe_charges");
		}
		maxSafeBaseCharges = Math.max(0, maxSafeBaseCharges);

		FailureMode failureMode = FailureMode.byId(GsonHelper.getAsString(obj, "failure_mode"));
		boolean connectsInSurvival = GsonHelper.getAsBoolean(obj, "connects_in_survival", false);
		boolean isWeldable = GsonHelper.getAsBoolean(obj, "is_weldable", false);
		int weldDamage = Math.max(GsonHelper.getAsInt(obj, "weld_damage", 0), 0);
		int weldStressPenalty = Math.max(GsonHelper.getAsInt(obj, "weld_stress_penalty", 0), 0);
		float minimumSpread = Math.max(GsonHelper.getAsFloat(obj, "minimum_spread", 0.05f), 0);
		float spreadReductionPerBarrel = Math.max(GsonHelper.getAsFloat(obj, "spread_reduction_per_barrel", 1), 0);
		int addedLifetime = Math.max(GsonHelper.getAsInt(obj, "added_lifetime", 0), -60);
		float reloadTimeModifier = Math.max(GsonHelper.getAsFloat(obj, "reload_time_modifier", 1), 0);
		float durabilityMassModifier = Math.max(GsonHelper.getAsFloat(obj, "durability_mass_modifier", 1), 0);
		float barrelGap = Math.max(GsonHelper.getAsFloat(obj, "barrel_gap", 0.5f), 0);
		int combatCommandCooldown = Math.max(GsonHelper.getAsInt(obj, "combat_command_cooldown", 1800), 0);
		int combatCommandDuration = Math.max(GsonHelper.getAsInt(obj, "combat_command_duration", 400), 0);
		boolean isSingleBarrel = GsonHelper.getAsBoolean(obj, "is_single_barrel", false);
		float recoilMultiplier = Math.max(GsonHelper.getAsFloat(obj, "recoil_multiplier", 1), 0);
		int refCaliber = Math.max(GsonHelper.getAsInt(obj, "ref_caliber", 100), 0);
		return new DualCannonMaterialProperties(minimumVelocityPerBarrel, weight, maxSafeBaseCharges, failureMode,
			connectsInSurvival, isWeldable, weldDamage, weldStressPenalty, minimumSpread, spreadReductionPerBarrel, addedLifetime, reloadTimeModifier, durabilityMassModifier, barrelGap,
				combatCommandCooldown, combatCommandDuration, isSingleBarrel, recoilMultiplier, refCaliber);
	}

	public JsonObject serialize() {
		JsonObject obj = new JsonObject();
		obj.addProperty("minimum_velocity_per_barrel", this.minimumVelocityPerBarrel);
		obj.addProperty("weight", this.weight);
		obj.addProperty("max_safe_propellant_stress", this.maxSafePropellantStress);
		obj.addProperty("failure_mode", this.failureMode.toString());
		obj.addProperty("connects_in_survival", this.connectsInSurvival);
		obj.addProperty("is_weldable", this.isWeldable);
		obj.addProperty("weld_damage", this.weldDamage);
		obj.addProperty("weld_stress_penalty", this.weldStressPenalty);
		obj.addProperty("minimum_spread", this.minimumSpread);
		obj.addProperty("spread_reduction_per_barrel", this.spreadReductionPerBarrel);
		obj.addProperty("added_lifetime", this.addedLifetime);
		obj.addProperty("reload_time_modifier", this.reloadTimeModifier);
		obj.addProperty("durability_mass_modifier", this.durabilityMassModifier);
		obj.addProperty("barrel_gap", this.barrelGap);
		obj.addProperty("combat_command_cooldown", this.combatCommandCooldown);
		obj.addProperty("combat_command_duration", this.combatCommandDuration);
		obj.addProperty("is_single_barrel", this.isSingleBarrel);
		obj.addProperty("recoil_multiplier", this.recoilMultiplier);
		obj.addProperty("ref_caliber", this.refCaliber);
		return obj;
	}

	public void writeBuf(FriendlyByteBuf buf) {
		buf.writeDouble(this.minimumVelocityPerBarrel)
			.writeFloat(this.weight);
		buf.writeVarInt(this.maxSafePropellantStress)
			.writeVarInt(this.failureMode.ordinal())
			.writeBoolean(this.connectsInSurvival)
			.writeBoolean(this.isWeldable);
		buf.writeVarInt(this.weldDamage)
			.writeVarInt(this.weldStressPenalty)
			.writeFloat(this.minimumSpread)
			.writeFloat(this.spreadReductionPerBarrel);
		buf.writeVarInt(this.addedLifetime)
			.writeFloat(this.reloadTimeModifier)
			.writeFloat(this.durabilityMassModifier)
			.writeFloat(this.barrelGap);
		buf.writeVarInt(this.combatCommandCooldown);
		buf.writeVarInt(this.combatCommandDuration)
				.writeBoolean(this.isSingleBarrel)
				.writeFloat(recoilMultiplier)
				.writeInt(refCaliber);
	}

	public static DualCannonMaterialProperties fromBuf(FriendlyByteBuf buf) {
		double minimumVelocityPerBarrel = buf.readDouble();
		float weight = buf.readFloat();
		int maxSafeBaseCharges = buf.readVarInt();
		FailureMode mode = FailureMode.values()[buf.readVarInt()];
		boolean connectsInSurvival = buf.readBoolean();
		boolean isWeldable = buf.readBoolean();
		int weldDamage = buf.readVarInt();
		int weldStressPenalty = buf.readVarInt();
		float minimumSpread = buf.readFloat();
		float spreadReductionPerBarrel = buf.readFloat();
		int addedLifetime = buf.readVarInt();
		float reloadTimeModifier = buf.readFloat();
		float durabilityMassModifier = buf.readFloat();
		float barrelGap = buf.readFloat();
		int combatCommandCooldown = buf.readVarInt();
		int combatCommandDuration = buf.readVarInt();
		boolean isSingleBarrel = buf.readBoolean();
		float recoilMultiplier = buf.readFloat();
		int refCaliber = buf.readInt();
		return new DualCannonMaterialProperties(minimumVelocityPerBarrel, weight, maxSafeBaseCharges, mode, connectsInSurvival,
			isWeldable, weldDamage, weldStressPenalty, minimumSpread, spreadReductionPerBarrel, addedLifetime, reloadTimeModifier, durabilityMassModifier, barrelGap,
				combatCommandCooldown, combatCommandDuration, isSingleBarrel, recoilMultiplier, refCaliber);
	}

	public enum FailureMode implements StringRepresentable {
		RUPTURE,
		FRAGMENT;

		private static final Map<String, FailureMode> BY_ID = Arrays.stream(values())
			.collect(Collectors.toMap(StringRepresentable::getSerializedName, Function.identity()));

		private final String name;

		FailureMode() {
			this.name = this.name().toLowerCase(Locale.ROOT);
		}

		@Override
		public String getSerializedName() {
			return this.name;
		}

		public static FailureMode byId(String id) {
			return BY_ID.getOrDefault(id, RUPTURE);
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private double minimumVelocityPerBarrel;
		private float weight;
		private int maxSafePropellantStress;
		private FailureMode failureMode = FailureMode.FRAGMENT;
		private boolean connectsInSurvival;
		private boolean isWeldable;
		private int weldDamage;
		private int weldStressPenalty;
		private float minimumSpread;
		private float spreadReductionPerBarrel;
		private int addedLifetime;
		private float reloadTimeModifier;
		private float durabilityMassModifier;
		private float barrelGap;
		private int combatCommandCooldown;
		private int combatCommandDuration;
		private boolean isSingleBarrel;
		private float recoilMultiplier;
		private int refCaliber;

		private Builder() {
		}

		public Builder minimumVelocityPerBarrel(double minimumVelocityPerBarrel) {
			this.minimumVelocityPerBarrel = minimumVelocityPerBarrel;
			return this;
		}

		public Builder weight(float weight) {
			this.weight = weight;
			return this;
		}

		public Builder maxSafePropellantStress(int maxSafePropellantStress) {
			this.maxSafePropellantStress = maxSafePropellantStress;
			return this;
		}

		public Builder failureMode(
			FailureMode failureMode) {
			this.failureMode = Objects.requireNonNull(failureMode, "Null failureMode");
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

		public Builder minimumSpread(float minimumSpread) {
			this.minimumSpread = minimumSpread;
			return this;
		}

		public Builder spreadReductionPerBarrel(float spreadReductionPerBarrel) {
			this.spreadReductionPerBarrel = spreadReductionPerBarrel;
			return this;
		}

		public Builder addedLifetime(int addedLifetime) {
			this.addedLifetime = addedLifetime;
			return this;
		}

		public Builder reloadTimeModifier(float reloadTimeModifier) {
			this.reloadTimeModifier = reloadTimeModifier;
			return this;
		}

		public Builder durabilityMassModifier(float durabilityMassModifier) {
			this.durabilityMassModifier = durabilityMassModifier;
			return this;
		}

		public Builder barrelGap(float barrelGap) {
			this.barrelGap = barrelGap;
			return this;
		}

		public Builder combatCommandCooldown(int combatCommandCooldown) {
			this.combatCommandCooldown = combatCommandCooldown;
			return this;
		}

		public Builder combatCommandDuration(int combatCommandDuration) {
			this.combatCommandDuration = combatCommandDuration;
			return this;
		}

		public Builder isSingleBarrel(boolean isSingleBarrel) {
			this.isSingleBarrel = isSingleBarrel;
			return this;
		}

		public Builder recoilMultiplier(float recoilMultiplier){
			this.recoilMultiplier = recoilMultiplier;
			return this;
		}

		public Builder refCaliber(int refCaliber){
			this.refCaliber = refCaliber;
			return this;
		}

		public DualCannonMaterialProperties build() {
			if (this.failureMode == null) {
				throw new IllegalStateException("Missing required property: failureMode");
			}
			return new DualCannonMaterialProperties(this.minimumVelocityPerBarrel, this.weight,
				this.maxSafePropellantStress, this.failureMode, this.connectsInSurvival, this.isWeldable,
				this.weldDamage, this.weldStressPenalty, this.minimumSpread, this.spreadReductionPerBarrel, this.addedLifetime, this.reloadTimeModifier,
					this.durabilityMassModifier, this.barrelGap, this.combatCommandCooldown, this.combatCommandDuration, this.isSingleBarrel, this.recoilMultiplier,
					this.refCaliber);
		}
	}
}
