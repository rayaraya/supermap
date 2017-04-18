package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;

import java.util.List;

/**
	* Created by 1 on 18.04.2017.
	*/
public class CityCar extends Car {

				public CityCar(List<Node> route, double speed,
				                  double orientation) {
								this.route = route;
								this.speed = speed;
								this.orientation = orientation;
				}


}
