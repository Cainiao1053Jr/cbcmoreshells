package com.cainiao1053.cbcmoreshells.munitions.big_cannon.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
//import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonProjectilePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public class ReductiveTorpedoPropertiesHandler extends EntityPropertiesTypeHandler<ReductiveTorpedoProperties> {

	private static final ReductiveTorpedoProperties DEFAULT = new ReductiveTorpedoProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, TorpedoProjectilePropertiesComponent.DEFAULT, BigCannonFuzePropertiesComponent.DEFAULT,
		ExplosionPropertiesComponent.DEFAULT, 4, 30, ReductiveProjectilePropertiesComponent.DEFAULT);

	@Override
	protected ReductiveTorpedoProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		TorpedoProjectilePropertiesComponent torpedoProperties = TorpedoProjectilePropertiesComponent.fromJson(id, obj);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromJson(id, obj);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromJson(id, obj);
		float maxCharges = Math.max(-1, getOrWarn(obj, "max_charges", id, 4f, JsonElement::getAsFloat));
		int lifetime = Math.max(0, getOrWarn(obj, "lifetime", id, 30, JsonElement::getAsInt));
		ReductiveProjectilePropertiesComponent reductiveProjectileProperties = ReductiveProjectilePropertiesComponent.fromJson(id, obj);
		return new ReductiveTorpedoProperties(ballistics, damage, torpedoProperties, fuze, explosion, maxCharges, lifetime, reductiveProjectileProperties);
	}

	@Override
	protected ReductiveTorpedoProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		TorpedoProjectilePropertiesComponent torpedoProperties = TorpedoProjectilePropertiesComponent.fromNetwork(buf);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromNetwork(buf);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromNetwork(buf);
		float maxCharges = buf.readFloat();
		int lifetime = buf.readInt();
		ReductiveProjectilePropertiesComponent reductiveProjectileProperties = ReductiveProjectilePropertiesComponent.fromNetwork(buf);
		return new ReductiveTorpedoProperties(ballistics, damage, torpedoProperties, fuze, explosion, maxCharges, lifetime, reductiveProjectileProperties);
	}

	@Override
	protected void writePropertiesToNetwork(ReductiveTorpedoProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.torpedoProperties().toNetwork(buf);
		properties.fuze().toNetwork(buf);
		properties.explosion().toNetwork(buf);
		buf.writeFloat(properties.maxCharges());
		buf.writeInt(properties.lifetime());
		properties.reductiveProjectileProperties().toNetwork(buf);
	}

	@Override protected ReductiveTorpedoProperties getNoPropertiesValue() { return DEFAULT; }

}
