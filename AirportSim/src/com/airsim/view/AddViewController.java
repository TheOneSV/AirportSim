package com.airsim.view;

import org.controlsfx.dialog.Dialogs;

import com.airsim.model.Airliner;
import com.airsim.model.Airplane;
import com.airsim.model.Airport;
import com.airsim.model.AssetLoader;
import com.airsim.model.Freighter;
import com.airsim.model.World;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class AddViewController {

	private static enum AIRTYPE{AIRLANER, FREIGHTER};

	private Stage dialogStage;
    private Airplane airplane;
    private Airport airport;
    final ToggleGroup group = new ToggleGroup();
    private AIRTYPE airtype; 
   
    
    @FXML
    private TextField nameField;
    @FXML
    private TextField speedField;
    @FXML
    private TextField maxKMField;
    @FXML
    private RadioButton freighterRadio;
    @FXML
    private RadioButton airlinerRadio;
    @FXML
    private ImageView airplaneView;
    @FXML
    private Label typeLabel;
    @FXML
    private TextField typeField;
    
    
	@FXML
    private void initialize() {
		freighterRadio.setToggleGroup(group);
		airlinerRadio.setToggleGroup(group);
		airtype = AIRTYPE.AIRLANER;
    	typeField.textProperty().setValue("0/" + Airliner.MAX_PASSANGER_PLACES);
		
		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
	        @Override
	        public void changed(ObservableValue<? extends Toggle> ov, Toggle t, Toggle t1) {

	            RadioButton chk = (RadioButton)t1.getToggleGroup().getSelectedToggle();
	            if(chk.equals(airlinerRadio)) {
	            	airplaneView.setImage(AssetLoader.airliner);
	            	typeLabel.textProperty().setValue("Passangers:");
	            	typeField.textProperty().setValue("0/" + Integer.toString(Airliner.MAX_PASSANGER_PLACES));
	        		airtype = AIRTYPE.AIRLANER;
	            } else {
	            	airplaneView.setImage(AssetLoader.freighter);
	            	typeLabel.textProperty().setValue("Cargo weight:");
	            	typeField.textProperty().setValue("0/" + Integer.toString(Freighter.MAX_CARGO_WEIGHT));
	        		airtype = AIRTYPE.FREIGHTER;
	            }
	        }
	    });
    }

    @FXML
    private void handleOk() {
    	if (isInputValid()) {
    		if (airtype == AIRTYPE.AIRLANER) {
    			airplane = new Airliner();
    			airplane.setSrcAirport(airport);
            	airplane.setFuel(1);
            	airplane.setName(nameField.getText());
            	airplane.setSpeed(Integer.parseInt(speedField.getText()));
            	airplane.setMaxRange(Integer.parseInt(maxKMField.getText()));
            	((Airliner)airplane).setTakenPlaces(Integer.parseInt(typeField.getText()));
    		} else {
    			airplane = new Freighter();
    			airplane.setSrcAirport(airport);
            	airplane.setFuel(1);
            	airplane.setName(nameField.getText());
            	airplane.setSpeed(Integer.parseInt(speedField.getText()));
            	airplane.setMaxRange(Integer.parseInt(maxKMField.getText()));
            	((Freighter)airplane).setCargoWeight(Integer.parseInt(typeField.getText()));
    		}
    
            dialogStage.close();
    	}
    }
    
    @FXML
	private void handleTypeFieldClick() {
    	typeField.textProperty().setValue("");
    }
    
    public void setAirport(Airport airport) {
    	this.airport = airport;
    }
    
    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    
	private boolean isInputValid() {
        String errorMessage = "";

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid name!\n"; 
        } else {
        	for (Airplane airplane: World.getAirplaneData()) {
        		System.out.println(airplane.getName());
        		if (airplane.getName().equalsIgnoreCase(nameField.getText())) {
        			errorMessage += "Already existed airplane with this name!";
        		}
        	}
        }
        if (speedField.getText() == null || speedField.getText().length() == 0 || 
        		speedField.getText() == "" || (Double.parseDouble(speedField.getText()) < 1)) {
            errorMessage += "No valid speed!\n"; 
        } 
        if (maxKMField.getText() == null || maxKMField.getText().length() == 0 || 
        		maxKMField.getText() == "" || (Double.parseDouble(maxKMField.getText()) < 1)) {
            errorMessage += "No valid max km!\n"; 
        }
        
        if (airtype == AIRTYPE.AIRLANER) {
        	if (typeField.getText() == null || typeField.getText().length() == 0 || 
        			typeField.getText() == "" || (Integer.parseInt(typeField.getText()) > Airliner.MAX_PASSANGER_PLACES)
        					|| Integer.parseInt(typeField.getText()) < 0) {
                errorMessage += "Data is not valid! Must be between 0 and " + Airliner.MAX_PASSANGER_PLACES + "\n";
        	}
        } else {
        	if (typeField.getText() == null || typeField.getText().length() == 0 || 
        			typeField.getText() == "" || (Integer.parseInt(typeField.getText()) > Freighter.MAX_CARGO_WEIGHT)
        					|| Integer.parseInt(typeField.getText()) < 0) {
                errorMessage += "Data is not valid! Must be between 0 and " + Freighter.MAX_CARGO_WEIGHT + "\n";
        	}
        }
        
        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message.
        	Dialogs.create()
		        .title("Invalid Fields")
		        .masthead("Please correct invalid fields")
		        .message(errorMessage)
		        .showError();
            return false;
        }
    }

	public Airplane getAirplane() { 
		return airplane;
	}
}
