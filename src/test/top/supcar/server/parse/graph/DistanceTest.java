package top.supcar.server.parse.graph;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.graph.Distance;
import top.supcar.server.parse.OSMData;

import java.util.Map;

/**
	* Created by 1 on 18.04.2017.
	*/
public class DistanceTest {
				public static void main(String[] args) {
								String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";
								Map<String,Way> roads;
								OSMData data = new OSMData(url);
								Node a = new Node(0,0,0);
								Node b = new Node(1,1,1);
								System.out.println(Distance.distanceBetween(a,b));
							 //data.loadData();
								data.makeMap();
								//roads = data.getMap();
								data.printSmap();
								//Distance.setMilestones(roads);
				}
}
