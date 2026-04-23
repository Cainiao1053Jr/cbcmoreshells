package com.cainiao1053.cbcmoreshells.config;

import net.createmod.catnip.config.ConfigBase;

public class CBCMSCfgServer extends ConfigBase {

	public final CBCMSCfgKinetics kinetics = nested(0, CBCMSCfgKinetics::new, Comments.kinetics);
	
	@Override public String getName() { return "server"; }

	private static class Comments {
		static String kinetics = "These values affect various miscellaneous contraptions.";
	}
	
}
