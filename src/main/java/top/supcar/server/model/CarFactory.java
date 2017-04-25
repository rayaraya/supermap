package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.graph.Graph;

/**
	* Created by 1 on 19.04.2017.
	*/
public interface CarFactory {

				Car createCar(Node start, Node destination);
}
