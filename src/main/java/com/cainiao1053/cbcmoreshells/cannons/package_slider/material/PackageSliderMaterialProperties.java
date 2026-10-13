package com.cainiao1053.cbcmoreshells.cannons.package_slider.material;

/**
 * @param weight           weight per cannon block, for mount stress
 * @param maxLength        maximum number of cannon blocks, breech included
 * @param reloadPerSection extra reload ticks per cannon block, on top of the breech's base reload time
 */
public record PackageSliderMaterialProperties(float weight, int maxLength, int reloadPerSection) {
}
