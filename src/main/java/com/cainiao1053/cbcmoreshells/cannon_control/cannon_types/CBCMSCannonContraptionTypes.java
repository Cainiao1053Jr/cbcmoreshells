package com.cainiao1053.cbcmoreshells.cannon_control.cannon_types;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import net.minecraft.resources.ResourceLocation;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.CannonContraptionTypeRegistry;
import rbasamoyai.createbigcannons.cannon_control.cannon_types.ICannonContraptionType;

public enum CBCMSCannonContraptionTypes implements ICannonContraptionType {
	TORPEDO_TUBE,
	PROJECTILE_RACK,
	DUAL_CANNON,
	PACKAGE_SLIDER;


	private static final Map<ResourceLocation, CBCMSCannonContraptionTypes> BY_ID =
		Arrays.stream(values()).collect(Collectors.toMap(CBCMSCannonContraptionTypes::getId, Function.identity()));

	private final ResourceLocation id;

	CBCMSCannonContraptionTypes() {
		this.id = Cbcmoreshells.resource(this.name().toLowerCase(Locale.ROOT));
		CannonContraptionTypeRegistry.register(this.id, this);
	}

	@Override public ResourceLocation getId() { return this.id; }

	@Nullable public static CBCMSCannonContraptionTypes byId(ResourceLocation loc) { return (CBCMSCannonContraptionTypes)BY_ID.get(loc); }

	public static void register() {
	}

}
