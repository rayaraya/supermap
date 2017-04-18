package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;

/**
	* Created by 1 on 19.04.2017.
	*/
public interface CarFactory {
				Car createCar(Node start, Node destination, double speed);
}
