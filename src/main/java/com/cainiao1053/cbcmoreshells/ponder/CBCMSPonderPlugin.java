package com.cainiao1053.cbcmoreshells.ponder;

import com.cainiao1053.cbcmoreshells.Cbcmoreshells;
import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.minecraft.resources.ResourceLocation;

public class CBCMSPonderPlugin implements PonderPlugin {

    @Override
    public String getModId() { return Cbcmoreshells.MODID; }

    @Override
    public void registerScenes(PonderSceneRegistrationHelper<ResourceLocation> helper) {
        CBCMSPonderScenes.register(helper);
    }

    @Override
    public void registerTags(PonderTagRegistrationHelper<ResourceLocation> helper) {
        CBCMSPonderTags.register(helper);
    }
}
