package top.supcar.server.graph;

import java.util.*;

import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.dijkstra.Dijkstra;

public class Graph extends PriorityQueue{
    private static final double R = 6371000;
    private static final double TRANS = Math.PI/180;
    private Map<Node, List<Node>> adjList;
    private Map<String, Way> interMap;
    private Map<String, Double> weightList;
    private List<Node> vertexList;
    private Map<Node, List<Way>> nodeWays;
    private Dijkstra dijkstra;

    public Graph(Map<String,Way> map){
        setInterMap(map);
        setMap();
        setVertexList();
        setWeightList();
        setNodeWays();
        dijkstra = new Dijkstra(this);
    }

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
        Way currWay;
        List<Node> road;

        vertexList = new ArrayList<>();
        weightList = new HashMap<>();

        while (interMapIter.hasNext()) {
            currEntry = interMapIter.next();
            currWay = currEntry.getValue();
            String tmp = currWay.getTags().get("oneway");
            boolean oneway = (tmp == null) || (tmp.equals("no"));

            road = currWay.getNodes();

            roadIter = road.listIterator();
            Node currNode;
            Node nextNode;

            if (roadIter.hasNext()) {
                currNode = roadIter.next();

                while (roadIter.hasNext()) {

                    nextNode = roadIter.next();

                    addInAdjList(currNode, nextNode);

                    if (oneway) {
                        addInAdjList(nextNode, currNode);
                    }

                    currNode = nextNode;
                }
            }
        }
    }

    private void addInAdjList(Node a, Node b) {
        List<Node> adjNodes = adjList.get(a);
        if(adjNodes == null) {
            adjNodes = new ArrayList<>();
            adjList.put(a, adjNodes);
        }
        adjNodes.add(b);
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

    public void setVertexList() {
        Iterator<Map.Entry<Node, List<Node>>> listIter = adjList.entrySet().iterator();
        while (listIter.hasNext()) {
            Node cNode = listIter.next().getKey();
            Node nNode;
            List<Node> road = adjList.get(cNode);
            Iterator roadIter = road.iterator();
            if (!vertexList.contains(cNode))
                vertexList.add(cNode);

            while (roadIter.hasNext()){
                nNode = (Node) roadIter.next();
                if (!vertexList.contains(nNode))
                    vertexList.add(nNode);
            }
        }
    }

    public List<Node> getVertexList(){
        return vertexList;
    }

    public void setWeightList(){
        Iterator<Map.Entry<Node, List<Node>>> listIter = adjList.entrySet().iterator();
        while (listIter.hasNext()) {
            Node cNode = listIter.next().getKey();
            Node nNode;
            List<Node> road = adjList.get(cNode);
            Iterator roadIter = road.iterator();

            while (roadIter.hasNext()){
                nNode = (Node) roadIter.next();
                weightList.put(cNode.getId()+nNode.getId(), getDistance(cNode, nNode));
            }
        }
    }

    public void setNodeWays(){
        nodeWays = new HashMap<>();
        Iterator<Map.Entry<String, Way>> interMapIter = interMap.entrySet().iterator();
        Iterator<Node> roadIter;
        Map.Entry<String, Way> currEntry;
        Way currWay;
        List<Node> road;

        while (interMapIter.hasNext()) {
            currEntry = interMapIter.next();
            currWay = currEntry.getValue();
            road = currWay.getNodes();

            roadIter = road.listIterator();
            Node currNode;

            while (roadIter.hasNext()){
                currNode = roadIter.next();
                List<Way> wayList = nodeWays.get(currNode);
                if (wayList==null){
                    wayList = new ArrayList<>();
                    nodeWays.put(currNode, wayList);
                }
                wayList.add(currWay);
            }
        }
    }

    public Map<Node, List<Way>> getNodeWays(){
        return nodeWays;
    }

    public List<Node> getWay(Node a, Node b) {
        return dijkstra.getWay(a, b);
    }
}