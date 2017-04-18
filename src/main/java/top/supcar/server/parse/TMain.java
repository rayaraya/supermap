package top.supcar.server.parse;

import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.WSServer;
import top.supcar.server.graph.Distance;

import java.util.Map;

public class TMain {
    public static void main(String[] args) throws Exception {

        String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";
        Map<String,Way> roads;
        OSMData data = new OSMData(url);
        //data.loadData();
        data.makeMap();
        roads = data.getMap();
        //data.printSmap();
        Distance.setMilestones(roads);
       // WSServer server = new WSServer();
        //server.run();
        
        // graph
        Graph graph = new Graph();
        Map<String, Way> testMap;
        testMap = data.getMap();
        graph.setInterMap(testMap);
        Map<String, Way> m;
        m = graph.getInterMap();
        graph.setMap();
        Map<Node, List<Node>> adj = graph.getAdjList();
        //    System.out.println(adj);
        Map<String, Double> w = graph.getWeightList();
        Node first = graph.vertexList.get(1);
        Node last = graph.vertexList.get(2);
        List<Node> road = graph.getWay(first,last);
        System.out.println(road);
    }
}
