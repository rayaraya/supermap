package top.supcar.server.graph;

import java.util.*;

import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.parse.OSMData;

public class Graph extends PriorityQueue{
    private static final double R = 6371000;
    private static final double TRANS = Math.PI/180;
    private Map<Node, List<Node>> adjList;
    private Map<String, Way> interMap;
    private Map<String, Double> weightList;
    private List<Node> vertexList;
    private Map<Node, List<Way>> nodeWays;

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
                    /*boolean isAdded = false;
                    if (!(vertexList.contains(currNode))) {
                        vertexList.add(currNode);
                        isAdded = true;
                    }*/
                    nextNode = roadIter.next();

                    //double currDistance = getDistance(currNode, nextNode);
                    /*if (isAdded) {
                        toAdd = new ArrayList<>();
                        toAdd.add(nextNode);
                        adjList.put(currNode, toAdd);
                    } else {
                        toAdd = adjList.get(currNode);
                        // adjList.remove(currNode);
                        toAdd.add(nextNode);
                        //  adjList.put(currNode, toAdd);
                    }*/
                    addInAdjList(currNode, nextNode);



             /*   String key = currNode.getId() + nextNode.getId();
                weightList.put(key, currDistance);*/

                    if (oneway) {
                        addInAdjList(nextNode, currNode);
                       /* isAdded = false;
                        if (!(vertexList.contains(nextNode))) {
                            vertexList.add(nextNode);
                            isAdded = true;
                        }
                        if (isAdded) {
                            toAdd = new ArrayList<>();
                            toAdd.add(currNode);
                            adjList.put(nextNode, toAdd);
                        } else {
                            toAdd = adjList.get(nextNode);
                            adjList.remove(nextNode);
                            toAdd.add(currNode);
                            adjList.put(nextNode, toAdd);
                        }
         /*               key = nextNode.getId() + currNode.getId();
                        weightList.put(key, currDistance);      */
                    }

                    currNode = nextNode;
                }

            }
        }
/*        System.out.println("adjList: " + adjList.size());
        System.out.println("vertexList: " + vertexList.size());
        Iterator<Map.Entry<Node,List<Node>>> itt = adjList.entrySet().iterator();
        Map.Entry<Node,List<Node>> entr;
        List<Node> dbgList;
        Node src;

        while(itt.hasNext()) {
            entr = itt.next();
            src = entr.getKey();
            dbgList = entr.getValue();
            for(Node nd: dbgList) {
                if(!weightList.containsKey(src.getId()+nd.getId())) {
                    System.out.println(src.getId() + " " + nd.getId());
                }
            }
        }   */


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

 /*   public List<Node> getWay(Node first, Node last){
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

       // System.out.println(road);
        return road;
    }   */



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


    public static void main(String[] args){
        Graph graph = new Graph();
        String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";
        OSMData data = new OSMData(url);
        //data.loadData();
        data.makeMap();
        Map<String, Way> testMap;
        testMap = data.getMap();
        graph.setInterMap(testMap);
        Map<String, Way> m;
        m = graph.getInterMap();
        graph.setMap();
        graph.setVertexList();
     //   System.out.println(graph.getAdjList().size());
     //   System.out.println(graph.getVertexList().size());
        graph.setWeightList();
        graph.setNodeWays();
        Node first = graph.vertexList.get(100);
        System.out.println(graph.getNodeWays());
     //   System.out.println(graph.getWeightList().size());
     /*   Map<Node, List<Node>> adj = graph.getAdjList();
    //    System.out.println(adj);
        Map<String, Double> w = graph.getWeightList();
        Node first = graph.vertexList.get(1);
        Node last = graph.vertexList.get(2);
 //       List<Node> road = graph.getWay(first,last);
 //       System.out.println(road);
 //       System.out.println(road.size());
 */
    }
}
