package top.supcar.server.graph;

import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
	* Created by 1 on 12.04.2017.
	*/
public class Distance {
				private static final double R = 6371000; // Earth's radius
				private static final double TRANS = Math.PI/180;
				/**
					* @return distance in meters
					*/
				private static double distanceBetween(Node a, Node b) {

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

}
