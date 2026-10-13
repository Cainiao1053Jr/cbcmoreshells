package com.cainiao1053.cbcmoreshells.config;

import net.createmod.catnip.config.ConfigBase;

public class CBCMSCfgServer extends ConfigBase {

	public final CBCMSCfgKinetics kinetics = nested(0, CBCMSCfgKinetics::new, Comments.kinetics);
	public final ConfigInt extinguisherDistance = i(8, 0, 128, "extinguisherDistance", "Extinguisher Distance");
	public final ConfigInt extinguisherCooldown = i(60, 0, 1000, "extinguisherCooldown", "Extinguisher Cooldown");
	public final ConfigInt extinguisherRange = i(2, 0, 16, "extinguisherRange", "Extinguisher Range");
	public final ConfigBool notifyOnHit = b(true, "notifyOnHit");
	public final ConfigFloat recoilScale = f(1, 0, 100, "recoilScale", "Scale of recoil of all cannon materials");
	public final ConfigInt packageChuteSearchRadius = i(3, 0, 16, "packageChuteSearchRadius",
		"Radius of the cube searched for chutes when a package shell hits the ground");
	public final ConfigInt packageChuteSearchRadiusShip = i(5, 0, 32, "packageChuteSearchRadiusShip",
		"Radius of the cube searched for chutes when a package shell hits a ship");
	public final ConfigFloat packageBreakSpeed = f(120, 0, 10000, "packageBreakSpeed",
		"Impact speed in m/s above which a package shell breaks apart and drops its contents");
	
	@Override public String getName() { return "server"; }

	private static class Comments {
		static String kinetics = "These values affect various miscellaneous contraptions.";
	}
	
}
