package com.airsim;

import java.io.IOException;

import com.airsim.model.Airplane;
import com.airsim.model.Airport;
import com.airsim.model.AssetLoader;
import com.airsim.model.World;
import com.airsim.view.AddViewController;
import com.airsim.view.AirplaneViewController;
import com.airsim.view.AirportViewController;
import com.airsim.view.RootLayoutController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MainApp extends Application {

	private Stage primaryStage;
	private BorderPane rootLayout;

	public MainApp() {
		AssetLoader.load();
		World.load();
	}

	@Override
	public void start(Stage primaryStage) {
		this.primaryStage = primaryStage;
		this.primaryStage.setTitle("SimPlane");
		this.primaryStage.setResizable(false);
		primaryStage.getIcons().add(AssetLoader.logoIco);
		initRootLayout();
	}

	/**
	 * Initializes the root layout.
	 */
	public void initRootLayout() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/RootLayout.fxml"));
			rootLayout = (BorderPane) loader.load();

			Scene scene = new Scene(rootLayout);
			primaryStage.setScene(scene);
			RootLayoutController controller = loader.getController();
			controller.setMainApp(this);
			primaryStage.show();

			controller.drawStatics();
			controller.start();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAirportView(Airport airport) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/AirportView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(airport.getName());
			dialogStage.initModality(Modality.NONE);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.getIcons().add(AssetLoader.logoIco);
			AirportViewController controller = loader.getController();
			controller.setMainApp(this);
			controller.setAirport(airport);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showAirplaneView(Airplane airplane) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/AirplaneView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle(airplane.getName());
			dialogStage.initModality(Modality.NONE);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.getIcons().add(AssetLoader.logoIco);

			AirplaneViewController controller = loader.getController();
			controller.setAriplane(airplane);

			dialogStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void showAboutView() {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class
					.getResource("view/AboutView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("About");
			dialogStage.initModality(Modality.NONE);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.getIcons().add(AssetLoader.logoIco);
			dialogStage.setResizable(false);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Airplane showAddView(Airport airport) {
		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(MainApp.class.getResource("view/AddView.fxml"));
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("New Airplane");
			dialogStage.initModality(Modality.NONE);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(page);
			dialogStage.setScene(scene);
			dialogStage.getIcons().add(AssetLoader.logoIco);

			AddViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setAirport(airport);

			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();

			return controller.getAirplane();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public static void main(String[] args) {
		launch(args);
	}
}