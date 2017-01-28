package com.airsim.model;

import java.util.HashMap;
import java.util.Map;

public class PathManager {

	private Map<String, FlightPath> flightPaths = new HashMap<String, FlightPath>();
	private Map<String, HoverPath> hoverPaths = new HashMap<String, HoverPath>();
	public static double padding =  (AssetLoader.airport.widthProperty().get()/2);

	public void addFlightPath(Airport air1, Airport air2, double controlX, double controlY) {
		flightPaths.put(air1.getName() + air2.getName(), 
				new FlightPath(air1.getX() + padding, air1.getY() + padding, 
						controlX, controlY, air2.getX() + padding, air2.getY() + padding) );
	}
	
	public void addHoverPath(Airport air1) {
		hoverPaths.put(air1.getName(), 
				new HoverPath(air1.getX() + AssetLoader.airport.getWidth(), air1.getY() + AssetLoader.airport.getHeight()/2, 30, 30, 0));
	}
	
	public Map<String, FlightPath> getFlightPaths() {
		return flightPaths;
	}
	
	public FlightPath getFlightPath(Airport air1, Airport air2) {
		return flightPaths.get(air1.getName() + air2.getName());
	}
	
	public HoverPath getHoverPath(Airport air) {
		return hoverPaths.get(air.getName());
	}
	
	public Map<String, HoverPath> getHoverPaths() {
		return hoverPaths;
	}
	
}
