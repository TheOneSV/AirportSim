package com.airsim.model;

import javafx.scene.image.Image;

public class AssetLoader {
	public static Image airliner, freighter, airport, background, logoIco;

	public static void load() {
		airliner = new Image("airliner.png");
		freighter = new Image("air_freighter.png");
		airport = new Image("Airport.png");
		background = new Image("WorldMap.jpg");
		logoIco = new Image("logo3.png");
	}

}
