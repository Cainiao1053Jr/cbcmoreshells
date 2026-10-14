package com.cainiao1053.cbcmoreshells.munitions.package_slider;

import com.cainiao1053.cbcmoreshells.index.CBCMSMunitionPropertiesHandlers;
import com.cainiao1053.cbcmoreshells.munitions.package_slider.config.PackageShellProperties;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import rbasamoyai.createbigcannons.munitions.config.components.BallisticPropertiesComponent;
import rbasamoyai.createbigcannons.munitions.config.components.EntityDamagePropertiesComponent;

import javax.annotation.Nonnull;

public class PackageShellProjectile extends AbstractPackageShellProjectile {

	public PackageShellProjectile(EntityType<? extends PackageShellProjectile> type, Level level) {
		super(type, level);
	}

	@Nonnull
	@Override
	public EntityDamagePropertiesComponent getDamageProperties() {
		return EntityDamagePropertiesComponent.DEFAULT;
	}

	@Nonnull
	@Override
	protected BallisticPropertiesComponent getBallisticProperties() {
		return this.getAllProperties().ballistics();
	}

	@Override
	protected int getLifetime() {
		return this.getAllProperties().lifetime();
	}

	protected PackageShellProperties getAllProperties() {
		return CBCMSMunitionPropertiesHandlers.PACKAGE_SHELL.getPropertiesOf(this);
	}

}
