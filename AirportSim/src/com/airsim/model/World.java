package com.airsim.model;

import com.airsim.util.ScaledClock;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;

@SuppressWarnings("unused")
public class World {
    private static ObservableList<Airport> airportData = FXCollections.observableArrayList();
    private static ObservableList<Airplane> airplaneData = FXCollections.observableArrayList();
    private static StringProperty crashes = new SimpleStringProperty("0");
    public static ScaledClock clock = new ScaledClock(10);
    public static PathManager pathManager = new PathManager();
    
    public World() {
    	
    }
    
	public static void load() {
    	Airport a1 = new Airport("Hans", 110, 60, 5, AssetLoader.airport);
    	Airport a2 = new Airport("Ruth", 100, 400, 4, AssetLoader.airport);
    	Airport a3 = new Airport("Heinz", 800, 60, 2, AssetLoader.airport);
    	Airport a4 = new Airport("Cornelia", 720, 450, 2, AssetLoader.airport);
    	airportData.add(a1);
    	airportData.add(a2);
    	airportData.add(a3);
    	airportData.add(a4);
    	
    	pathManager.addFlightPath(a1, a2, 250, 0);
    	pathManager.addFlightPath(a1, a3, 300, 0);
    	pathManager.addFlightPath(a1, a4, 300, 0);
    	
    	pathManager.addFlightPath(a2, a1, 0, 250);
    	pathManager.addFlightPath(a2, a3, 200, 0);
    	pathManager.addFlightPath(a2, a4, 0, 300);
    	
    	pathManager.addFlightPath(a3, a1, 0, 300);
    	pathManager.addFlightPath(a3, a2, 0, 200);
    	pathManager.addFlightPath(a3, a4, 0, 200);
    	
    	pathManager.addFlightPath(a4, a1, 0, 200);
    	pathManager.addFlightPath(a4, a2, 200, 0);
    	pathManager.addFlightPath(a4, a3, 300, 0);
    	pathManager.addHoverPath(a4);
    	pathManager.addHoverPath(a3);
    	pathManager.addHoverPath(a2);
    	pathManager.addHoverPath(a1);
    	
    }
    
	public static ObservableList<Airport> getAirportData() {
		return airportData;
	}
	public static void setAirportData(ObservableList<Airport> airportData) {
		World.airportData = airportData;
	}
	public static ObservableList<Airplane> getAirplaneData() {
		return airplaneData;
	}
	public static void setAirplaneData(ObservableList<Airplane> airplaneData) {
		World.airplaneData = airplaneData;
	}
    
	public static StringProperty crashesProperty() {
		return crashes;
	}
	
	public static void incrementCrashes() {
		World.crashes.set(Integer.toString((Integer.parseInt(World.crashes.get()) + 1)));
	}
}
