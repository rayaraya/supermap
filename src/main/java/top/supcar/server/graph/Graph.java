package top.supcar.server.graph;

import java.util.*;

import com.sun.org.apache.xpath.internal.operations.Mod;
import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;
import top.supcar.server.dijkstra.Dijkstra;
import top.supcar.server.model.ModelConstants;

public class Graph extends PriorityQueue{
    private static final double R = 6371000;
    private static final double TRANS = Math.PI/180;
    private Map<Node, List<Node>> adjList;
    private Map<String, Way> interMap;
    private Map<String, Double> weightList;
    private List<Node> vertexList;
    private Map<Node, List<Way>> nodeWays;
    private Dijkstra dijkstra;
    private SessionObjects sessionObjects;

    public Graph(Map<String,Way> map, SessionObjects sessionObjects){
        this.sessionObjects = sessionObjects;
        setInterMap(map);
        setMap();
        setVertexList();
        setNodeWays();
        setWeightList();
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
            boolean twoway = (tmp == null) || (tmp.equals("no"));

            road = currWay.getNodes();

            roadIter = road.listIterator();
            Node currNode;
            Node nextNode;

            if (roadIter.hasNext()) {
                currNode = roadIter.next();

                while (roadIter.hasNext()) {

                    nextNode = roadIter.next();

                    addInAdjList(currNode, nextNode);

                    if (twoway) {
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
                weightList.put(cNode.getId()+nNode.getId(), getWeight(cNode, nNode));
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

    private double getWeight(Node currNode, Node nextNode) {
        Way wayWhereAreBothNodesArePlaced = null;
        double speed;
        List<Way> list1 = nodeWays.get(currNode), list2 = nodeWays.get(nextNode);
        for(Way way1  : list1) {
            for(Way way2 : list2) {
                if(way1 == way2)
                    wayWhereAreBothNodesArePlaced = way1;
            }
        }
        speed = getAvgSpeed(wayWhereAreBothNodesArePlaced);

        return sessionObjects.getDistance().distanceBetween(currNode, nextNode)/speed;
    }

    private double getAvgSpeed(Way way) {
        double speed = ModelConstants.CITY_MAX_SPEED/2;
        String highway = way.getTags().get("highway");
        if(highway.equals("service") || highway.equals("living street") || highway.equals("residential")) {
            speed /= 3;
        }
        return speed;

    }
}