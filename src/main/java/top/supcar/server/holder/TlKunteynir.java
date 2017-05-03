package top.supcar.server.holder;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.model.TrafficLight;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 01.05.2017.
 */
public class TlKunteynir {
	private Map<Node, TrafficLight> trafficLights;
	public TlKunteynir() {
		this.trafficLights = new HashMap<>();
	}
	public TrafficLight getTl(Node node) {
		return trafficLights.get(node);
	}
	public void addTl(Node node, TrafficLight TL) {
		trafficLights.put(node, TL);
	}
}
