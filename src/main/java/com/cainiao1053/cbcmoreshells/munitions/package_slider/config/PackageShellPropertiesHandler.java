package com.cainiao1053.cbcmoreshells.munitions.package_slider.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import rbasamoyai.createbigcannons.munitions.config.EntityPropertiesTypeHandler;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;

public class PackageShellPropertiesHandler extends EntityPropertiesTypeHandler<PackageShellProperties> {

	private static final PackageShellProperties DEFAULT = new PackageShellProperties(BallisticPropertiesComponent.DEFAULT, 400);

	@Override
	protected PackageShellProperties parseJson(ResourceLocation location, JsonObject obj) throws JsonParseException {
		String id = location.toString();
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromJson(id, obj);
		int lifetime = Math.max(0, getOrWarn(obj, "lifetime", id, 400, JsonElement::getAsInt));
		return new PackageShellProperties(ballistics, lifetime);
	}

	@Override
	protected PackageShellProperties readPropertiesFromNetwork(EntityType<?> entityType, FriendlyByteBuf buf) {
		BallisticPropertiesComponent ballistics = BallisticPropertiesComponent.fromNetwork(buf);
		int lifetime = buf.readInt();
		return new PackageShellProperties(ballistics, lifetime);
	}

	@Override
	protected void writePropertiesToNetwork(PackageShellProperties properties, FriendlyByteBuf buf) {
		properties.ballistics().toNetwork(buf);
		buf.writeInt(properties.lifetime());
	}

	@Override protected PackageShellProperties getNoPropertiesValue() { return DEFAULT; }

}
