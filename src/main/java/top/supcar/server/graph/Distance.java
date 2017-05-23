/*
    Copyright 2017 SUPMUP

    This file is part of Supermap.

    Supermap is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    Supermap is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with Supermap. If not, see <http://www.gnu.org/licenses/>.
*/

package top.supcar.server.graph;

import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.SelectedRect;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Operations with coordinates and distances
 *
 */
public class Distance {



	private double metersPerDegLat;
	private double metersPerDegLon;

	public Distance(SelectedRect selectedRect) {

		Node x0y0 = new Node(0, selectedRect.getLowerLeft().getLat(), selectedRect
				.getLowerLeft().getLon());
		Node x1y0 = new Node(0, selectedRect.getUpperRight().getLat(), selectedRect
				.getLowerLeft().getLon());

		Node x0y1 = new Node(0, selectedRect.getLowerLeft().getLat(), selectedRect
				.getUpperRight().getLon());

		metersPerDegLat = distanceBetween(x0y0, x1y0)/(x1y0.getLat() - x0y0.getLat());
		metersPerDegLon = distanceBetween(x0y0, x0y1)/(x0y1.getLon() - x0y0.getLon());
	}
	/**
	 * @return distance in meters
	 */
	public double distanceBetween(Node a, Node b) {
		double R = 6371000; // Earth's radius
		double TRANS = Math.PI/180;

		double lat1 = a.getLat()*TRANS;
		double lat2 = b.getLat()*TRANS;
		double dlat = lat2 - lat1;
		double dlon = (b.getLon()- a.getLon())*TRANS;

		double x = Math.sin(dlat/2) * Math.sin(dlat/2) +
				Math.cos(lat1) * Math.cos(lat2) *
						Math.sin(dlon/2) * Math.sin(dlon/2);
		double c = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1-x));

		double d = R * c;
		return  d;
	}

	public double latDegToMeters(double deg) {
		return deg*metersPerDegLat;
	}
	public double lonDegToMeters(double deg) {
		return deg*metersPerDegLon;
	}
	public double metersToLatDeg(double meters) {
		return meters/metersPerDegLat;
	}
	public double metersToLonDeg(double meters) {
		return meters/metersPerDegLon;
	}
				/*
				/**
					*
					* For each pair Way, Node
					* counts distance between this node(along this way) from the fist node of the way
					* The key is concatenation of WayId and NodeInd(Use method getId() )
					*
					* @param  roads from OSMData.getRoads
					* @return  Map with concatenation (WayId+NodeId) as key and way length in meters
					* as value
					*/
/*
				public static Map< String, Double> setMilestones(Map<String,Way> roads) {

								Map< String, Double> milestones = new HashMap< String, Double>();

								Map.Entry currEntry;
								Way currWay;
								Node node;
								String pair;
								int first_iteration;
								double wayLenght;

								Iterator roadsIt = roads.entrySet().iterator();
								Iterator dorogaIt;
								List<Node> dorogaAsList;
								Node prev = null;

								while (roadsIt.hasNext()) {
												currEntry = (Map.Entry) roadsIt.next();
												currWay = (Way) currEntry.getValue();
												dorogaAsList = currWay.getNodes();
												dorogaIt = dorogaAsList.listIterator();
												first_iteration = 1;
												wayLenght = 0;

												while (dorogaIt.hasNext()) {
																node = (Node) dorogaIt.next();
																pair = currWay.getId();
																pair += node.getId();
																if (first_iteration == 1) {
																				prev = node;
																				milestones.put(pair, (double) 0);

																}
																wayLenght += distanceBetween(node, prev);
																if (!milestones.containsKey(pair))
																				milestones.put(pair, wayLenght);

																prev = node;
																first_iteration = 0;
												}


								}

								return milestones;
				}
*/
}
