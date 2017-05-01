package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;

/**
 * Created by 1 on 16.04.2017.
 */
public abstract class RoadThing {
	protected Node pos;
	protected String type;

	public Node getPos() {
		return pos;
	}

	public String getType() {
		return type;
	}
}
