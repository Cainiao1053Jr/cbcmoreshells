package com.cainiao1053.cbcmoreshells;

import com.cainiao1053.cbcmoreshells.ponder.CBCMSPonderPlugin;
import net.createmod.ponder.foundation.PonderIndex;

public class CbcmoreshellsClientCommon {
    public static void onClientSetup() {
        PonderIndex.addPlugin(new CBCMSPonderPlugin());
    }
}