package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.graph.Graph;
import top.supcar.server.model.Car;

/**
	* Created by 1 on 19.04.2017.
	*/
public interface CarFactory {

				Car createCar(Node start, Node destination);
}
