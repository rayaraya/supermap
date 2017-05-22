package top.supcar.server.graph;

import java.util.*;

import info.pavie.basicosmparser.model.*;
import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;
import top.supcar.server.dijkstra.Dijkstra;
import top.supcar.server.model.ModelConstants;

/**
 * This class allows you to make graph which consists of {@link Node} and edges between them.
 * The graph are being built according to {@link info.pavie.basicosmparser.BasicOSMParser} data.
 * The edges correspond to {@link Relation} between {@link Node}.
 * Four objects are being created for one graph.
 * @author nataboll
*/

public class Graph extends PriorityQueue{
//ATTRIBUTES
    /** The Earth radius **/
    private static final double R = 6371000;
    /** Is used to convert angular measure **/
    private static final double TRANS = Math.PI/180;
    /** An adjacency list **/
    private Map<Node, List<Node>> adjList;
    /** For getting data from {@link info.pavie.basicosmparser.BasicOSMParser}**/
    private Map<String, Way> interMap;
    /** List of weights, the keys are {@link Node} ID concatenation **/
    private Map<String, Double> weightList;
    /** List of vertexes of {@link Node} type **/
    private List<Node> vertexList;
    /** Contains all the roads for every vertex **/
    private Map<Node, List<Way>> nodeWays;
    /** The class is connected with {@link Dijkstra} having it as a field **/
    private Dijkstra dijkstra;
    /** The field is for getting the saved parameters of this session **/
    private SessionObjects sessionObjects;

//CONSTRUCTOR
    public Graph(Map<String,Way> map, SessionObjects sessionObjects){
        this.sessionObjects = sessionObjects;
        setInterMap(map);
        setMap();
        setVertexList();
        setNodeWays();
        setWeightList();
        dijkstra = new Dijkstra(this);
    }

//OTHER METHODS
    /**
     * @param map is created in setMap
     */
    public void setInterMap(Map<String,Way> map) {
        interMap = map;
    }

    /**
     * @return data of {@link info.pavie.basicosmparser.BasicOSMParser}
     */
    public Map<String, Way> getInterMap(){
        return this.interMap;
    }

    /**
     * Generates adjList iterating by interMap (after {@link info.pavie.basicosmparser.BasicOSMParser})
     */
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

    /**
     * Adds 'b' in the list of 'a' is connected with.
     * @param a is where to add.
     * @param b is what to add.
     */
    private void addInAdjList(Node a, Node b) {
        List<Node> adjNodes = adjList.get(a);
        if(adjNodes == null) {
            adjNodes = new ArrayList<>();
            adjList.put(a, adjNodes);
        }
        adjNodes.add(b);
    }

    /**
     * @return list of edges' weights.
     */
    public Map<String, Double> getWeightList(){
        return weightList;
    }

    /**
     * @return adjacency list.
     */
    public Map<Node, List<Node>> getAdjList(){
        return this.adjList;
    }

    /**
     * Creating list of vertexes iterating by adjList.
     */
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

    /**
     * @return list of vertexes.
     */
    public List<Node> getVertexList(){
        return vertexList;
    }

    /**
     * Creates weightList iterating by adjList.
     */
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

    /**
     * Finds all the roads for every vertex.
     */
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

    /**
     * Finds the shortest way between two vertexes using {@link Dijkstra}.
     * @param a is the start.
     * @param b is the end.
     * @return the way.
     */
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

    /**
     * @param way is the chosen way where an average speed is needed to be calculated.
     * @return the average speed in the road.
     */
    private double getAvgSpeed(Way way) {
        double speed = ModelConstants.CITY_MAX_SPEED/2;
        String highway = way.getTags().get("highway");
        if(highway.equals("service") || highway.equals("living street") || highway.equals("residential")) {
            speed /= 3;
        }
        return speed;
    }
}