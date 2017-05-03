package top.supcar.server.dijkstra;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.parse.OSMData;

import java.util.*;


public class Dijkstra extends Graph {
    private static final Double INFINITY = Double.longBitsToDouble(0x7ff0000000000000L);
    private Map<Node, Double> d = new HashMap<>();
    private Map<Node, Node> p = new HashMap<>();

    private void initializeSingleSource(List<Node> nodeList, Node start) {  // подавать vertexList
        Iterator listIter = nodeList.iterator();
        Iterator roadIter;
        Node currNode;
        List<Node> road;

        while (listIter.hasNext()) {
            currNode = (Node) listIter.next();
            d.put(currNode, 10000000.0);
            p.put(currNode, null);
        }
        d.remove(start);
        d.put(start, 0.0);
    }


    private void relax(Node u, Node v, Double w) {
        if (d.get(v) > d.get(u) + w) {
            double val = d.get(u) + w;
            d.remove(v);
            d.put(v, val);
            p.put(v, u);
            queue.remove(v);
            queue.add(v);
        }

    }

    public Comparator<Node> nodeComparator = new Comparator<Node>() {

        @Override
        public int compare(Node n1, Node n2) {
            double n1val = d.get(n1);
            double n2val = d.get(n2);
            if(n1val > n2val)
                return 1;
            else if(n1val < n2val)
                return -1;
            else
                return 0;
        }
    };

    //utility method to add data to queue
    private void addDataToQueue(List<Node> list, Queue<Node> queue) {
        Iterator nodeIter = list.iterator();

        while (nodeIter.hasNext()){
            queue.add((Node) nodeIter.next());
        }
    }

    //utility method to poll data from queue
    private static void pollDataFromQueue(Queue<Node> queue) {
        while(true){
            Node currNode = queue.poll();
            if(currNode == null) break;
            System.out.println("Processing with ID="+currNode.getId());
        }
    }

    private Queue<Node> queue = new PriorityQueue<>(/*super.vertexList.size()*/100, nodeComparator);

    public void dijkstra(List<Node> nodeList, Map<Node, List<Node>> adjList, Map<String, Double> weightList, Node u) {   // подавать adjList
        initializeSingleSource(nodeList, u);
        //System.out.println("Source: " + u.getId());
        List<Node> s = new ArrayList<>();
        addDataToQueue(nodeList, queue);
        Node v;
        List<Node> adjU;
        while (!(queue.isEmpty())){
            u=queue.poll();
            s.add(u);
            adjU = adjList.get(u);
            if (adjU == null)
                adjU = new ArrayList<>();
            Iterator nodeIter = adjU.iterator();
            while (nodeIter.hasNext()){
                v = (Node) nodeIter.next();
                Double w = weightList.get(u.getId()+v.getId());
                relax(u, v, w);
            }
        }
    }

    public List<Node> getWay(List<Node> nodeList, Map<Node, List<Node>> adjList, Map<String, Double> weightList, Node start, Node end){
        dijkstra(nodeList, adjList, weightList, start);
        List<Node> notWay = new ArrayList<>();
        List<Node> way = new ArrayList<>();
        notWay.add(end);

        while (end != start){
            end = p.get(end);
            notWay.add(end);
        }
        for(int i = notWay.size() - 1; i >= 0; i--) {
            way.add(notWay.get(i));
        }

        if(way.size() == 0)
            return null;

        return way;
    }


    public static void main(String args[]){
        String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";
        Map<String,Way> roads;
        OSMData data = new OSMData(url);
        //data.loadData();
        data.makeMap();
        roads = data.getMap();
        //data.printRoads();
        Distance.setMilestones(roads);

        Graph graph = new Graph();
        Dijkstra dijkstra = new Dijkstra();
        Map<String, Way> testMap;
        testMap = data.getMap();
        graph.setInterMap(testMap);
        Map<String, Way> m;
        m = graph.getInterMap();
        graph.setMap();
        graph.setVertexList();
        graph.setWeightList();
        Node first = graph.getVertexList().get(5);
        Node last = graph.getVertexList().get(1080);
        System.out.println(first +" "+ last);
       // System.out.println(graph.getAdjList().size());
       // System.out.println(graph.vertexList.size());

        List<Node> road = dijkstra.getWay(graph.getVertexList(), graph.getAdjList(), graph.getWeightList(), first, last);
        System.out.println(road);

    }


}
