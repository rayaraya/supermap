package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;

import java.util.List;

/**
	* Created by 1 on 16.04.2017.
	*/
public abstract class Car extends RoadThing {
			//	private final String model;
				protected List<Node> route;
				protected double speed;
				protected double orientation;


}
