package com.airsim.view;

import org.controlsfx.dialog.Dialogs;

import com.airsim.MainApp;
import com.airsim.model.Airplane;
import com.airsim.model.Airplane.State;
import com.airsim.model.Airport;
import com.airsim.model.World;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

public class AirportViewController {
	private Airport airport;
	private MainApp mainApp;
	
	// Airplane ViewTable
	@FXML
    private TableView<Airplane> airplaneTable;
    @FXML
    private TableColumn<Airplane, String> nameAriplaneColumn;
    @FXML
    private TableColumn<Airplane, String> AirplaneStateColumn;
    @FXML
    private TableColumn<Airplane, String> AirplaneDestColumn;
    @FXML
    private TableColumn<Airplane, String> AirplaneCheudeledColumn;
    
    // Airport available destinations ViewTable
    @FXML
    private TableView<Airport> airplaneDstTable;
    @FXML
    private TableColumn<Airport, String> nameDestinations;

    // Hover ViewTable
	@FXML
    private TableView<Airplane> airplaneHoverTable;
    @FXML
    private TableColumn<Airplane, String> nameHoverAriplaneColumn;
    
    // Scheduling time TextField`s
    @FXML
    private TextField dayTxtField; 
    @FXML
    private TextField hourTxtField;
    @FXML 
    private TextField minutesTxtField;
    @FXML 
    private TextField secondsTxtField; 
    
    // View initialization
	@FXML
    private void initialize() {
		nameAriplaneColumn.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
		AirplaneStateColumn.setCellValueFactory(
        		cellData -> cellData.getValue().strStateProperty());
		AirplaneDestColumn.setCellValueFactory(
        		cellData -> cellData.getValue().dstAirportProperty());
		AirplaneCheudeledColumn.setCellValueFactory(
        		cellData -> cellData.getValue().getScheduledTime().timeProperty());
		
		
		nameDestinations.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
		
		nameHoverAriplaneColumn.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
    }
	
	@FXML
	private void handleNewAirplane() {
		if(airport.emptyPlacesProperty().get() != 0) {
			Airplane airplane;
			airplane = mainApp.showAddView(airport);
			if (airplane != null) {
				World.getAirplaneData().add(airplane);
				airport.addAirplane(airplane);
			}
		} else {
			Dialogs.create()
			.title("Error!")
			.masthead("No empty places!")
			.message("Please wait for empty place.")
			.showWarning();
		}
	}
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
	}
	
	@FXML
	private void handleTakeOff() {
		String errorMessage = "";
		Airport selectedAirport = airplaneDstTable.getSelectionModel().getSelectedItem();
		Airplane selectedAirplane = airplaneTable.getSelectionModel().getSelectedItem();
		
		if (selectedAirplane != null) {
				if (selectedAirport != null) {
					if (selectedAirplane.isInRange(selectedAirport)) {
						if (selectedAirplane.canFlyNow(selectedAirport)) {
							selectedAirplane.setDstAirport(selectedAirport);
							selectedAirplane.changeState(State.TAKEOF);
						} else {
							errorMessage = "Fuel is not enought!";
						}
					} else {
						errorMessage = "Selected airport is not in range for this airplane";
					}
				} else {
					errorMessage = "Please select an airport from the table.";
				}
		} else {
			errorMessage = "Please select an airplane from the table.";
		}
		
		if (errorMessage.length() != 0) {
			Dialogs.create()
	        .title("Invalid data")
	        .masthead("Please correct your choice")
		        .message(errorMessage)
		        .showError();
		}
	}
	
	@FXML
	private void handleLandAirplane() {
		Airplane selectedAirplane = airplaneHoverTable.getFocusModel().getFocusedItem();
		if (selectedAirplane != null) {
				if(airport.emptyPlacesProperty().get() > 0) {
					airport.removeHoverPlane(selectedAirplane);
					selectedAirplane.changeState(State.LANDING);
				} else {
					Dialogs.create()
					.title("Error!")
					.masthead("No empty places!")
					.message("Please wait for empty place.")
					.showWarning();
				}
		} else {
			// Nothing selected.
			Dialogs.create()
				.title("No Selection")
				.masthead("No Airplane Selected")
				.message("Please select an airplane in the table.")
				.showWarning();
		}
	}
	
	@FXML
	private void handleClearChedule() {
		Airplane selectedAirplane = airplaneTable.getSelectionModel().getSelectedItem();
		
		if (selectedAirplane != null) {
			if (selectedAirplane.getState() == Airplane.State.SCHEDULED) {
				selectedAirplane.changeState(Airplane.State.REFUEL);
				selectedAirplane.setDstAirport(null);
				selectedAirplane.getScheduledTime().resset();
			}
		} else {
			Dialogs.create()
			.title("No Selection")
			.masthead("No Airplane Selected")
			.message("Please select an airplane or airport from the table.")
			.showWarning();
		}
	}
	
	@FXML
	private void handleMakeChedule() {
		String errorMessage = "";
		Airport selectedAirport = airplaneDstTable.getSelectionModel().getSelectedItem();
		Airplane selectedAirplane = airplaneTable.getSelectionModel().getSelectedItem();
		
		if (selectedAirplane != null) {
			if (selectedAirport != null) {
				
				if(selectedAirplane.isInRange(selectedAirport)) {
					int iDay = 0, iHours = 0, iMinutes = 0, iSeconds = 0;
				    try {
						iDay = Integer.parseInt(dayTxtField.getText());
						iHours = Integer.parseInt(hourTxtField.getText());
						iMinutes = Integer.parseInt(minutesTxtField.getText());
						iSeconds = Integer.parseInt(secondsTxtField.getText());
					}catch (NumberFormatException e) {
						System.out.println("Wrong input format!");
		            }

					if (iDay >= 1 && iDay < Integer.MAX_VALUE) {
						if (iHours >= 0 && iHours <= 60) {
							if (iMinutes >= 0 && iMinutes <= 60) {
								if (iSeconds >= 0 && iSeconds <= 60) {
									
									selectedAirplane.getScheduledTime().setDays(iDay);
									selectedAirplane.getScheduledTime().setHours(iHours);
									selectedAirplane.getScheduledTime().setMinutes(iMinutes);
									selectedAirplane.getScheduledTime().setSeconds(iSeconds);
									
									if(selectedAirplane.getScheduledTime().compareTo(World.clock) > 0) {		
										double diff_sec = (selectedAirplane.getScheduledTime().getTimeInSeconds() - World.clock.getTimeInSeconds());
										double totalUpdates = (diff_sec / RootLayoutController.MILLIUPDATE)*1000;
										double f_fuel = (totalUpdates * selectedAirplane.getRefuelSpeed())/World.clock.getScale() + selectedAirplane.getFuel();
										
										if (f_fuel > 1) {
											f_fuel = 1;
										}
										
										if(selectedAirplane.calcDistance(selectedAirport) <= selectedAirplane.getMaxRange()*f_fuel) {
											selectedAirplane.setDstAirport(selectedAirport);
											selectedAirplane.changeState(Airplane.State.SCHEDULED);
										} else {
											selectedAirplane.getScheduledTime().resset();
											errorMessage = "Fuel is not enought!";
										}
										
									} else {
										errorMessage = "The time wich you input is older than present time!";
									}
								} else {
									errorMessage = "Invalid seconds!";
								}
							} else {
								errorMessage = "Invalid minutes!";
							}
						} else {
							errorMessage = "Invalid hour!";
						}
					} else {
						errorMessage = "Invalid day!";
					}
					
				} else {
					errorMessage = "Selected airport is not in range for this airplane";
				}
			} else {
				errorMessage = "Please select an airport from the table.";
			}
		} else {
			errorMessage = "Please select an airplane from the table.";
		}
		
		if (errorMessage.length() != 0) {
			Dialogs.create()
		        .title("Invalid data")
		        .masthead("Please correct your choice")
			        .message(errorMessage)
			        .showError();
		}
	}
    
	@FXML
	private void handleClickAirplaneTable() {
		Airplane selectedAirplane = airplaneTable.getSelectionModel().getSelectedItem();
		if (selectedAirplane != null) {
			if(selectedAirplane.getState() == Airplane.State.SCHEDULED) {
			    dayTxtField.setText(Integer.toString(selectedAirplane.getScheduledTime().getDays()));
			    hourTxtField.setText(Integer.toString(selectedAirplane.getScheduledTime().getHours()));
			    minutesTxtField.setText(Integer.toString(selectedAirplane.getScheduledTime().getMinutes()));
			    secondsTxtField.setText(Integer.toString(selectedAirplane.getScheduledTime().getSeconds())); 
			} else {
			    dayTxtField.setText(Integer.toString(World.clock.getDays()));
			    hourTxtField.setText(Integer.toString(World.clock.getHours()));
			    minutesTxtField.setText(Integer.toString(World.clock.getMinutes()));
			    secondsTxtField.setText(Integer.toString(World.clock.getSeconds())); 
			}
		}
	}
	
    public void setAirport(Airport airport) {
    	this.airport = airport;
    	airplaneTable.setItems(this.airport.getAriplanes());
    	airplaneHoverTable.setItems(this.airport.getHoverAriplanes());
    	for(Airport tmpAirport: World.getAirportData()) {
    		if(tmpAirport != airport) {
    			airplaneDstTable.getItems().add(tmpAirport);
    		}
    	}
    }
}
