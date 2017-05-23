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
