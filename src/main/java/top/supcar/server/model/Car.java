package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;

import java.util.List;

/**
	* Created by 1 on 16.04.2017.
	*/
public abstract class Car extends RoadThing {
				private final double length;
				private final String model;
				private List<Node> route;
				private double speed;
				private double orientation;

				protected Car(double length, String model, List<Node> route, double speed, double
								orientation) {
								this.length = length;
								this.model = model;
								this.route = route;
								this.speed = speed;
								this.orientation = orientation;
				}


}
