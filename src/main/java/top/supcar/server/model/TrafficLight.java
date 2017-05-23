/*
 * Copyright 2017 SUPMUP
 *
 * This file is part of Supermap.
 *
 * Supermap is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Supermap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Supermap. If not, see <http://www.gnu.org/licenses/>.
 *
 */

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
	 * @param direction direction
	 */
	public void addDirection(Node direction) {
		if(!directions.contains(direction))
			directions.add(direction);
	}



}
