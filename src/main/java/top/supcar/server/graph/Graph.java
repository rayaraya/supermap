package top.supcar.server.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.parse.OSMData;


public class Graph {
    private static final double R = 6371000; // Earth's radius
    private static final double TRANS = Math.PI/180;
    public Map <Node, List<Node>> adjList;
    private Map<String, Way> interMap;

    public void setInterMap(Map<String,Way> map) {
        interMap = map;
    }

    private Map<String, Way> getInterMap(){
        return this.interMap;
    }

    public void setMap(){
        Iterator<Map.Entry<String, Way>> interMapIter = interMap.entrySet().iterator();
        Iterator<Node> roadIter;
        Map.Entry<String, Way> currEntry;
        Element currElement;
        List<Node> road;
        List<Node> vertexList = new ArrayList<>();
        Iterator vertexListIter;
      //  vertexListIter = vertexList.iterator();

        while (interMapIter.hasNext()){
            currEntry = interMapIter.next();
            currElement = currEntry.getValue();
            road = ((Way) currElement).getNodes();
            roadIter = road.listIterator();
            Node currNode = roadIter.next();
            vertexList.add(currNode);
            Node nextNode;

            while (roadIter.hasNext()){
                nextNode = roadIter.next();
                vertexList.add(nextNode);
                // double currDistance = getDistance(currNode, nextNode);
                // adding elements
                // Map<Node, List<Node>> currV, nextV;
            }
        }
        System.out.println(vertexList);
    }

    private double getDistance(Node currNode, Node nextNode) {
        double lat1 = currNode.getLat()*TRANS;
        double lat2 = nextNode.getLat()*TRANS;
        double dLat = lat2 - lat1;
        double dLon = (nextNode.getLon()- currNode.getLon())*TRANS;

        double x = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);

        double p = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(1-x));

        return  R*p;
    }


    public static void main(String[] args){
        Graph graph = new Graph();
        OSMData data = new OSMData();
        data.loadData();
        data.makeMap();
        Map<String, Way> testMap;
        testMap = data.getMap();
        graph.setInterMap(testMap);
        Map<String, Way> m;
        m = graph.getInterMap();
        System.out.println(m);
        graph.setMap();
    }
}