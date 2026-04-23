package com.cainiao1053.cbcmoreshells.munitions.big_cannon.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;

import rbasamoyai.createbigcannons.munitions.big_cannon.config.BigCannonFuzePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.ExplosionPropertiesComponent;

public class TorpedoPropertiesHandler extends EntityPropertiesTypeHandler<TorpedoProperties> {

	private static final TorpedoProperties DEFAULT = new TorpedoProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, TorpedoProjectilePropertiesComponent.DEFAULT, BigCannonFuzePropertiesComponent.DEFAULT,
		ExplosionPropertiesComponent.DEFAULT, 4, 30);

	@Override
	protected TorpedoProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		TorpedoProjectilePropertiesComponent torpedoProperties = TorpedoProjectilePropertiesComponent.fromJson(id, obj);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromJson(id, obj);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromJson(id, obj);
		float maxCharges = Math.max(-1, getOrWarn(obj, "max_charges", id, 4f, JsonElement::getAsFloat));
		int lifetime = Math.max(0, getOrWarn(obj, "lifetime", id, 30, JsonElement::getAsInt));
		return new TorpedoProperties(ballistics, damage, torpedoProperties, fuze, explosion, maxCharges, lifetime);
	}

	@Override
	protected TorpedoProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		TorpedoProjectilePropertiesComponent torpedoProperties = TorpedoProjectilePropertiesComponent.fromNetwork(buf);
		BigCannonFuzePropertiesComponent fuze = BigCannonFuzePropertiesComponent.fromNetwork(buf);
		ExplosionPropertiesComponent explosion = ExplosionPropertiesComponent.fromNetwork(buf);
		float maxCharges = buf.readFloat();
		int lifetime = buf.readInt();
		return new TorpedoProperties(ballistics, damage, torpedoProperties, fuze, explosion, maxCharges, lifetime);
	}

	@Override
	protected void writePropertiesToNetwork(TorpedoProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.torpedoProperties().toNetwork(buf);
		properties.fuze().toNetwork(buf);
		properties.explosion().toNetwork(buf);
		buf.writeFloat(properties.maxCharges());
		buf.writeInt(properties.lifetime());
	}

	@Override protected TorpedoProperties getNoPropertiesValue() { return DEFAULT; }

}
