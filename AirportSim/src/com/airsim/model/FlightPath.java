package com.airsim.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.QuadCurveTo;

public class FlightPath {

	private Path path = new Path();

	public FlightPath(double startX, double startY, double controlX,
			double controlY, double endX, double endY) {
		path.getElements().clear();
		path.setStrokeWidth(0.1);
		path.setStroke(Color.BLACK);
		path.getElements().add(new MoveTo(startX, startY));
		path.getElements().add(new QuadCurveTo(controlX, controlY, endX, endY));
	}

	public void drawPath(Group group) {
		group.getChildren().add(path);
	}

	public void clearPath(Group group) {
		group.getChildren().remove(path);
	}

	public Path getPath() {
		return path;
	}
}
