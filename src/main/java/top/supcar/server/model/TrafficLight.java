package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 16.04.2017.
 */
public class TrafficLight {
	private Node sourceNode;
	private List<Node> directions;
	private Way allowedWay;
	private double period; //period in seconds
	private Map<Way, Boolean> state;

	public TrafficLight(Node sourceNode, double period) {
		this.sourceNode = sourceNode;
		this.directions = new LinkedList<>();
		this.period = period;
	}

	public List<Node> getDirections() {
		return this.directions;
	}

	/**
	 * adds direction, if isn't added before
	 * @param direction
	 */
	public void addDirection(Node direction) {
		if(!directions.contains(direction))
			directions.add(direction);
	}



}
