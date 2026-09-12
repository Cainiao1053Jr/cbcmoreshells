package com.cainiao1053.cbcmoreshells.munitions.dual_cannon.table;

public record BallisticPoint(double range, double elevation, double flightTicks, double impactSpeed) {

	public double flightSeconds() {
		return this.flightTicks / 20.0;
	}

	public boolean reachableWithin(int lifetimeTicks) {
		return this.flightTicks <= lifetimeTicks;
	}

}
