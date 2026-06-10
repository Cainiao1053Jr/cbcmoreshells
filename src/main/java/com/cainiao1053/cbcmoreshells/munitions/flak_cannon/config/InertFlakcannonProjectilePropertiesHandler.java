package com.cainiao1053.cbcmoreshells.munitions.flak_cannon.config;

import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

public class InertFlakcannonProjectilePropertiesHandler extends EntityPropertiesTypeHandler<InertFlakcannonProjectileProperties> {

	private static final InertFlakcannonProjectileProperties DEFAULT = new InertFlakcannonProjectileProperties(BallisticPropertiesComponent.DEFAULT,
		EntityDamagePropertiesComponent.DEFAULT, FlakcannonProjectilePropertiesComponent.DEFAULT);

	@Override
	protected InertFlakcannonProjectileProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromJson(id, obj);
		FlakcannonProjectilePropertiesComponent autocannonProperties = FlakcannonProjectilePropertiesComponent.fromJson(id, obj);
		return new InertFlakcannonProjectileProperties(ballistics, damage, autocannonProperties);
	}

	@Override
	protected InertFlakcannonProjectileProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		EntityDamagePropertiesComponent damage = EntityDamagePropertiesComponent.fromNetwork(buf);
		FlakcannonProjectilePropertiesComponent autocannonProperties = FlakcannonProjectilePropertiesComponent.fromNetwork(buf);
		return new InertFlakcannonProjectileProperties(ballistics, damage, autocannonProperties);
	}

	@Override
	protected void writePropertiesToNetwork(InertFlakcannonProjectileProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		properties.damage().toNetwork(buf);
		properties.autocannonProperties().toNetwork(buf);
	}

	@Override protected InertFlakcannonProjectileProperties getNoPropertiesValue() { return DEFAULT; }

}
