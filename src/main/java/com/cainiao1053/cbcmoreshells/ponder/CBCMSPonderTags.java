package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;


public class CBCMSPonderTags {
    public static final ResourceLocation
            DUAL_CANNON = Cbcmoreshells.resource("dual_cannon");


    public static void register(PonderTagRegistrationHelper<ResourceLocation> helper) {
        PonderTagRegistrationHelper<RegistryEntry<?, ?>> HELPER = helper.withKeyFunction(RegistryEntry::getId);

    }
}
