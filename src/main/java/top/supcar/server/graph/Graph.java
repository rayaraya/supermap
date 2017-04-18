package top.supcar.server.graph;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.parse.OSMData;

public class Graph{
    private static final double R = 6371000;
    private static final double TRANS = Math.PI/180;
    private Map<Node, List<Node>> adjList;
    private Map<String, Way> interMap;
    private Map<String, Double> weightList;
    public List<Node> vertexList;

    public void setInterMap(Map<String,Way> map) {
        interMap = map;
    }

    public Map<String, Way> getInterMap(){
        return this.interMap;
    }

    public void setMap(){

        adjList = new HashMap<>();
        Iterator<Map.Entry<String, Way>> interMapIter = interMap.entrySet().iterator();
        Iterator<Node> roadIter;
        Map.Entry<String, Way> currEntry;
        Element currElement;
        List<Node> road;

        vertexList = new ArrayList<>();
        weightList = new HashMap<>();

        while (interMapIter.hasNext()){
            currEntry = interMapIter.next();
            currElement = currEntry.getValue();
            String tmp = currElement.getTags().get("oneway");
          //  System.out.println(tmp);
            road = ((Way) currElement).getNodes();
            roadIter = road.listIterator();
            Node currNode = roadIter.next();
          //  System.out.println(currNode);
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
                    toAdd = adjList.get(currNode);
                    adjList.remove(currNode);
                    toAdd.add(nextNode);
                    adjList.put(currNode, toAdd);
                }

                String key = currNode.getId() + nextNode.getId();
                weightList.put(key, currDistance);

                if ((tmp != null)&&(tmp.equals("yes"))){
                    isAdded = false;
                    if (!(vertexList.contains(nextNode))) {
                        vertexList.add(nextNode);
                        isAdded = true;
                    }
                    toAdd.clear();
                    if (isAdded) {
                        toAdd.add(currNode);
                        adjList.put(nextNode, toAdd);
                    }
                    else {
                        toAdd = adjList.get(nextNode);
                        adjList.remove(nextNode);
                        toAdd.add(currNode);
                        adjList.put(nextNode, toAdd);
                    }
                    key = nextNode.getId() + currNode.getId();
                    weightList.put(key, currDistance);
                }

                currNode=nextNode;
            }
        }
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

    public List<Node> getWay(Node first, Node last){
        Iterator<Map.Entry<String, Way>> interMapIter = interMap.entrySet().iterator();
        Map.Entry<String, Way> currEntry;
        Element currElement;
        List<Node> road;
        currEntry = interMapIter.next();
        currElement = currEntry.getValue();
        road = ((Way) currElement).getNodes();

        while ((interMapIter.hasNext())&&(road.size()<10)) {
            currEntry = interMapIter.next();
            currElement = currEntry.getValue();
            road = ((Way) currElement).getNodes();
        }
        return road;
    }
}
