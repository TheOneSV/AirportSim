package com.airsim.model;

public class Freighter extends Airplane {
	
	public static final int MAX_CARGO_WEIGHT = 10000;
	private static float threshold = 0.1f;
	private int cargoWeight = 0;

	public Freighter() {
		this("Unknown", null, 0, 0, 0);
	}
	
	/**
     * Freighter constructor  
     * @param  name - name of the airplane
     * @param  airport - source airport where the airplane stands
     * @param  speed - speed of the airplane
     * @param  maxRange - maximum range distance of the plane
     * @param  cargoWeight - cargo weight
     * @see Airport
     */
	public Freighter(String name, Airport srsAirport, float speed, int maxRange, int cargoWeight) {
		super(name, srsAirport, speed, maxRange, AssetLoader.freighter);
		setCargoWeight(cargoWeight);
	}

	public int getCargoWeight() {
		return cargoWeight;
	}
	public boolean setCargoWeight(int cargoWeight) {
		if (cargoWeight > MAX_CARGO_WEIGHT) {
			return false;
		}
		this.cargoWeight = cargoWeight;
		updateRefuelSpeed();
		return true;
	}
	
	private void updateRefuelSpeed() {
		refuelSpeed *= (1 - threshold*(cargoWeight/MAX_CARGO_WEIGHT));
	}
	
	@Override
	public String getInfo() {
		return "Type: Freighter | Cargo: " + Integer.toString(cargoWeight) + "/" + Integer.toString(MAX_CARGO_WEIGHT);
	}
}
