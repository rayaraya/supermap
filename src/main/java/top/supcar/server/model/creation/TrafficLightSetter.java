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

package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.session.SessionObjects;
import top.supcar.server.graph.Graph;
import top.supcar.server.model.TrafficLight;

import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 01.05.2017.
 */
public class TrafficLightSetter {
	public SessionObjects sessionObjects;

	public TrafficLightSetter(SessionObjects sessionObjects) {
		this.sessionObjects = sessionObjects;
	}

	public void setTls() {
		Graph graph = sessionObjects.getGraph();
		List<Node> nodes = graph.getVertexList();
        Map<Node, List<Node>> adjList = graph.getAdjList();
        List<Node> adjNodes;
        TrafficLight tl;

        for(Node nd: nodes) {
            if(isTlNode(nd)) {
                initTlIfNecessary(nd);
            }
            adjNodes = adjList.get(nd);
            for(Node adjnd : adjNodes) {
                if(isTlNode(adjnd)) {
                    initTlIfNecessary(adjnd);
                    tl = sessionObjects.getTlKunteynir().getTl(adjnd);
                    tl.addDirection(nd);
                }
            }
        }

	}

	private void initTlIfNecessary(Node nd) {
	    if(sessionObjects.getTlKunteynir().getTl(nd) == null) {
            Map<Node, List<Node>> adjList = sessionObjects.getGraph().getAdjList();
            List<Node> adjNodes = adjList.get(nd);
            double defPeriod = CreationParams.DEF_TL_PERIOD;
            double period;
            period = defPeriod + (int) ((Math.random() - 0.5) * defPeriod / 2);
            TrafficLight tl = new TrafficLight(nd, period);
            for (Node adjnd : adjNodes) {
                tl.addDirection(adjnd);
            }
            sessionObjects.getTlKunteynir().addTl(nd, tl);
        }

    }
    private boolean isTlNode(Node nd) {
        if(nd.getTags().get("highway") == "traffic_signals") return true;
        else return false;
    }

}
