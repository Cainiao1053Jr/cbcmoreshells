package com.cainiao1053.cbcmoreshells.index;

import com.cainiao1053.cbcmoreshells.munitions.autocannon.config.AntiairAutocannonProjectilePropertiesHandler;
import com.cainiao1053.cbcmoreshells.munitions.big_cannon.config.*;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonIncendiaryPropertiesHandler;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonProperties;
import com.cainiao1053.cbcmoreshells.munitions.dual_cannon.config.DualCannonPropertiesHandler;
import com.cainiao1053.cbcmoreshells.munitions.racked_projectile.config.*;

public class CBCMSMunitionPropertiesHandlers {

	public static final BigCannonShellessShellPropertiesHandler SHELLESS_SHELL_BIG_CANNON_PROJECTILE = new BigCannonShellessShellPropertiesHandler();
	public static final ShellessInertBigCannonPropertiesHandler SHELLESS_INERT_BIG_CANNON_PROJECTILE = new ShellessInertBigCannonPropertiesHandler();
	public static final TorpedoPropertiesHandler TORPEDO_PROJECTILE = new TorpedoPropertiesHandler();
	public static final SharpnelTorpedoPropertiesHandler SHRAPNEL_TORPEDO_PROJECTILE = new SharpnelTorpedoPropertiesHandler();
	public static final BigCannonIncendiaryShellPropertiesHandler INCENDIARY_SHELL_PROJECTILE = new BigCannonIncendiaryShellPropertiesHandler();
	public static final RackedProjectilePropertiesHandler RACKED_PROJECTILE = new RackedProjectilePropertiesHandler();
	public static final RackedTorpedoProjectilePropertiesHandler RACKED_TORPEDO = new RackedTorpedoProjectilePropertiesHandler();
	public static final RackedRocketProjectilePropertiesHandler RACKED_ROCKET = new RackedRocketProjectilePropertiesHandler();
	public static final RackedLoiteringRocketProjectilePropertiesHandler LOITERING_ROCKET = new RackedLoiteringRocketProjectilePropertiesHandler();
	public static final ShrapnelShellessShellPropertiesHandler SHRAPNEL_SHELLESS_BIG_CANNON_PROJECTILE = new ShrapnelShellessShellPropertiesHandler();
	public static final RackedShrapnelProjectilePropertiesHandler RACKED_SHRAPNEL_PROJECTILE = new RackedShrapnelProjectilePropertiesHandler();
	public static final DualCannonPropertiesHandler DUAL_CANNON_PROPERTIES = new DualCannonPropertiesHandler();
	public static final DualCannonIncendiaryPropertiesHandler DUAL_CANNON_INCENDIARY_PROPERTIES = new DualCannonIncendiaryPropertiesHandler();
	public static final ReductiveTorpedoPropertiesHandler REDUCTIVE_TORPEDO_PROJECTILE = new ReductiveTorpedoPropertiesHandler();

	public static final AntiairAutocannonProjectilePropertiesHandler ANTIAIR_AUTOCANNON = new AntiairAutocannonProjectilePropertiesHandler();


	public static void init() {}

}
