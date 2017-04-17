package top.supcar.server.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.parse.OSMData;


public class Graph {
    private static final double R = 6371000; // Earth's radius
    private static final double TRANS = Math.PI/180;
    private Map<Node, List<Node>> adjList;
    private Map<String, Way> interMap;
    private Map<String, Double> weightList;

    public void setInterMap(Map<String,Way> map) {
        interMap = map;
    }

    private Map<String, Way> getInterMap(){
        return this.interMap;
    }

    public void setMap(){
        adjList = new HashMap<>();
        Iterator<Map.Entry<String, Way>> interMapIter = interMap.entrySet().iterator();
        Iterator<Node> roadIter;
        Map.Entry<String, Way> currEntry;
        Element currElement;
        List<Node> road;
        List<Node> vertexList = new ArrayList<>();
        weightList = new HashMap<>();

        while (interMapIter.hasNext()){
            currEntry = interMapIter.next();
            currElement = currEntry.getValue();
            road = ((Way) currElement).getNodes();
            roadIter = road.listIterator();
            Node currNode = roadIter.next();
            //System.out.println(currNode);
            vertexList.add(currNode);
            Node nextNode;

            while (roadIter.hasNext()){
                boolean isAdded = false;
                if (!(vertexList.contains(currNode))) {
                    vertexList.add(currNode);
                    isAdded = true;
                }
                nextNode = roadIter.next();
                List<Node> toAdd = new ArrayList<>();
                toAdd.clear();
                double currDistance = getDistance(currNode, nextNode);
                if (isAdded) {
                    toAdd.add(nextNode);
                    adjList.put(currNode, toAdd);
                }
                else {
                    //toAdd = adjList.get(currNode);
                    adjList.remove(currNode);
                    toAdd.add(nextNode);
                    adjList.put(currNode, toAdd);
                }
                String key = currNode.getId() + nextNode.getId();
                weightList.put(key, currDistance);
                currNode=nextNode;
            }
        }
        //System.out.println(vertexList);
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

    public Map<String, Double> getWeightList(){
        return weightList;
    }
    
    public Map<Node, List<Node>> getAdjList(){
        return this.adjList;
    }

    //больше никаких отдельных тестовых мейнов, используем один, потом его не будет, только запуск сервера
}
