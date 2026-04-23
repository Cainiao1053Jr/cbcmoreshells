package com.cainiao1053.cbcmoreshells.config;

import net.createmod.catnip.config.ConfigBase;

public class CBCMSCfgKinetics extends ConfigBase {


	public final CBCMSCfgStress stress = nested(1, CBCMSCfgStress::new, Comments.stress);

    @Override
	public String getName() {
		return "kinetics";
	}

	private static class Comments {
		static String stress = "Stressful, I know.";
	}

}
