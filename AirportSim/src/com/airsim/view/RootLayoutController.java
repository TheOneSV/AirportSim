package com.airsim.view;


import org.controlsfx.dialog.Dialogs;

import com.airsim.MainApp;
import com.airsim.model.Airplane;
import com.airsim.model.Airport;
import com.airsim.model.AssetLoader;
import com.airsim.model.FlightPath;
import com.airsim.model.HoverPath;
import com.airsim.model.World;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

public class RootLayoutController {
	
	@FXML
    private TableView<Airport> airportTable;
    @FXML
    private TableColumn<Airport, String> nameColumn;
    @FXML
    private TableColumn<Airport, Number> emptyPlacesColumn;
    
    @FXML
    private TableView<Airplane> airplaneTable;
    @FXML
    private TableColumn<Airplane, String> nameAriplaneColumn;
    @FXML
    private TableColumn<Airplane, String> AirplaneStateColumn;
    
    @FXML
    private Group groupDraw;
    
    @FXML
    private Label daysLabel;
    @FXML
    private Label hoursLabel;
    @FXML
    private Label minutesLabel;
    @FXML
    private Label secondsLabel;
    @FXML
    private Label scaleTimeLabel;
    @FXML
    private Label crashesLabel;
    
	private MainApp mainApp;
	private Timeline timeline;
	final public static int MILLIUPDATE = 34;
	
	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		
		airportTable.setItems(World.getAirportData());
		airplaneTable.setItems(World.getAirplaneData());
	}
	
	public RootLayoutController() {
		this.timeline = new Timeline(new KeyFrame(
		        Duration.millis(MILLIUPDATE),
		        ae -> update()));
		timeline.setCycleCount(Animation.INDEFINITE);
	}
	
	@FXML
    private void initialize() {
		nameColumn.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
		emptyPlacesColumn.setCellValueFactory(
        		cellData -> cellData.getValue().emptyPlacesProperty());
        
		nameAriplaneColumn.setCellValueFactory(
        		cellData -> cellData.getValue().nameProperty());
		AirplaneStateColumn.setCellValueFactory(
        		cellData -> cellData.getValue().strStateProperty());
		
		secondsLabel.textProperty().bind(World.clock.timeProperty());
		crashesLabel.textProperty().bind(World.crashesProperty());

    }
	@FXML
	private void showAirportView() {
		Airport selectedAirport = airportTable.getSelectionModel().getSelectedItem();
		if (selectedAirport != null) {
			 mainApp.showAirportView(selectedAirport);
		} else {
			// Nothing selected.
			Dialogs.create()
				.title("No Selection")
				.masthead("No Airport Selected")
				.message("Please select an airport in the table.")
				.showWarning();
		}
	}
	
	@FXML
	private void showAirplaneView() {
		Airplane selectedAirplane = airplaneTable.getSelectionModel().getSelectedItem();
		if (selectedAirplane != null) {
			 mainApp.showAirplaneView(selectedAirplane);
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
	private void showAbout() {
		mainApp.showAboutView();
	}
	
	public void start() {
		timeline.play();
	}
	
	public void update() {
		World.clock.addMilliseconds(MILLIUPDATE);
		try {
			for(Airplane airplane: World.getAirplaneData()) {
				airplane.update(groupDraw);
			}
		}
		catch(Exception e) {
			System.out.println("Dead Airplane");
		}
	}
	
	public void drawStatics() {
		drawBackground();
		drawAirports();
		drawPaths();
	}
	
	private void drawBackground() {
		ImageView imageView = new ImageView();
		imageView.setImage(AssetLoader.background);
		imageView.setFitWidth(900);
		imageView.setFitHeight(550);
		groupDraw.getChildren().add(imageView);
	}
	
	private void drawAirports() {
		for(Airport airport: World.getAirportData()) {
			groupDraw.getChildren().add(airport.getImageView());
			groupDraw.getChildren().add(airport.getTextName());
		}
	}
	
	private void drawPaths() {
		for(FlightPath flightPath: World.pathManager.getFlightPaths().values()) {
			flightPath.drawPath(groupDraw);
		}
		for(HoverPath hoverPath: World.pathManager.getHoverPaths().values()) {
			hoverPath.drawPath(groupDraw);
		}
	}
}
