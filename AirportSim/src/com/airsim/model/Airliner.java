package com.airsim.model;

public class Airliner extends Airplane{

	public static final int MAX_PASSANGER_PLACES = 550;
	private static float threshold = 0.1f;
	private int takenPlaces = 0;

	public Airliner() {
		this("Unknown", null, 0, 0, 0);
	}
	
	/**
     * Airliner constructor  
     * @param  name - name of the airplane
     * @param  airport - source airport where the airplane stands
     * @param  speed - speed of the airplane
     * @param  maxRange - maximum range distance of the plane
     * @param  takenPlaces - places taken by passangers
     * @see Airport
     */
	public Airliner(String name, Airport srsAirport, float speed, int maxRange, int takenPlaces) {
		super(name, srsAirport, speed, maxRange, AssetLoader.airliner);
		setTakenPlaces(takenPlaces);
	}

	public int getTakenPlaces() {
		return takenPlaces;
	}
	public boolean setTakenPlaces(int takenPlaces) {
		if (takenPlaces > MAX_PASSANGER_PLACES) {
			return false;
		}
		this.takenPlaces = takenPlaces;
		updateRefuelSpeed();
		return true;
	}
	
	private void updateRefuelSpeed() {
		refuelSpeed *= (1 - threshold*(takenPlaces/MAX_PASSANGER_PLACES));
	}
	
	@Override
	public String getInfo() {
		return "Type: Airliner | Passangers: " + Integer.toString(takenPlaces) + "/" + Integer.toString(MAX_PASSANGER_PLACES);
	}
}
