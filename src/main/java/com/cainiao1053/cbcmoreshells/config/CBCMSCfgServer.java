package com.cainiao1053.cbcmoreshells.config;

import net.createmod.catnip.config.ConfigBase;

public class CBCMSCfgServer extends ConfigBase {

	public final CBCMSCfgKinetics kinetics = nested(0, CBCMSCfgKinetics::new, Comments.kinetics);
	public final ConfigInt extinguisherDistance = i(8, 0, 128, "extinguisherDistance", "Extinguisher Distance");
	public final ConfigInt extinguisherCooldown = i(60, 0, 1000, "extinguisherCooldown", "Extinguisher Cooldown");
	public final ConfigInt extinguisherRange = i(2, 0, 16, "extinguisherRange", "Extinguisher Range");
	public final ConfigBool notifyOnHit = b(true, "notifyOnHit");
	
	@Override public String getName() { return "server"; }

	private static class Comments {
		static String kinetics = "These values affect various miscellaneous contraptions.";
	}
	
}
