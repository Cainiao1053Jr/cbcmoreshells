package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedDualCannonContraption;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedProjectileRackContraption;
import com.cainiao1053.cbcmoreshells.cannon_control.contraption.MountedTorpedoTubeContraption;
import com.simibubi.create.api.contraption.ContraptionType;
import com.simibubi.create.api.registry.CreateBuiltInRegistries;
import com.simibubi.create.content.contraptions.Contraption;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import java.util.function.Supplier;

import static com.simibubi.create.AllContraptionTypes.BY_LEGACY_NAME;


public class CBCMSContraptionTypes {

	public static final Holder.Reference<ContraptionType>
		TORPEDO_TUBE = register("mounted_torpedo_tube", MountedTorpedoTubeContraption::new),
	    PROJECTILE_RACK = register("mounted_projectile_rack", MountedProjectileRackContraption::new),
		DUAL_CANNON = register("mounted_dual_cannon", MountedDualCannonContraption::new);

	private static Holder.Reference<ContraptionType> register(String name, Supplier<? extends Contraption> factory) {
		ContraptionType type = new ContraptionType(factory);
		BY_LEGACY_NAME.put(name, type);

		return Registry.registerForHolder(CreateBuiltInRegistries.CONTRAPTION_TYPE, Cbcmoreshells.resource(name), type);
	}

	public static void prepare() {
	}

}
