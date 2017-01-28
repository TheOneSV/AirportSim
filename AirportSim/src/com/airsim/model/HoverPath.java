package com.airsim.model;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.ArcTo;
import javafx.scene.shape.ClosePath;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.PathBuilder;

@SuppressWarnings({ "deprecation" })
public class HoverPath {

	private Path path = new Path();

	public HoverPath(double centerX, double centerY, double radiusX,
			double radiusY, double rotate) {
		path.getElements().clear();
		path = createEllipsePath(centerX + PathManager.padding / 2, centerY, radiusX,
				radiusY, rotate);
	}

	private Path createEllipsePath(double centerX, double centerY,
			double radiusX, double radiusY, double rotate) {
		ArcTo arcTo = new ArcTo();
		arcTo.setX(centerX - radiusX + 1); // to simulate a full 360 degree
											// celcius circle.
		arcTo.setY(centerY - radiusY);
		arcTo.setSweepFlag(false);
		arcTo.setLargeArcFlag(true);
		arcTo.setRadiusX(radiusX);
		arcTo.setRadiusY(radiusY);
		arcTo.setXAxisRotation(rotate);

		Path path = PathBuilder
				.create()
				.elements(new MoveTo(centerX - radiusX, centerY - radiusY),
						arcTo, new ClosePath()) // close 1 px gap.
				.build();
		path.setStroke(Color.DODGERBLUE);
		path.setStrokeWidth(0.5);
		path.getStrokeDashArray().setAll(5d, 5d);
		return path;
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