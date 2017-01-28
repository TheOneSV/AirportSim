package com.airsim.view;

import com.airsim.model.Airplane;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;

public class AirplaneViewController {
	
	@FXML
	private Label speedLabel;
	@FXML
	private Label stateLabel;
	@FXML
	private Label startLabel;
	@FXML
	private Label endLabel;
	@FXML
	private ProgressBar progressFuel;
	@FXML
	private ProgressBar progressDist;
	@FXML
	private Label maxRangeLabel;
	@FXML
	private Label infoLabel;
	
	@FXML
    private void initialize() {
    }
    
    public void setAriplane(Airplane airplane) {
    	speedLabel.setText(Double.toString(airplane.getSpeed()));
    	maxRangeLabel.setText(Integer.toString(airplane.getMaxRange()));
    	stateLabel.textProperty().bind(airplane.strStateProperty());
    	startLabel.textProperty().bind(airplane.srcAirportProperty());
    	endLabel.textProperty().bind(airplane.dstAirportProperty());
    	infoLabel.textProperty().setValue(airplane.getInfo());
    	progressFuel.progressProperty().bind(airplane.fuelProperty());
    	progressDist.progressProperty().bind(airplane.travelledDistanceProperty());
    }
}
