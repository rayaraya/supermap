package top.supcar.server.dijkstra;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.graph.Graph;

import java.util.*;


public class Dijkstra {

    private Map<Node, Double> d = new HashMap<>();
    private Map<Node, Node> p = new HashMap<>();
    private Graph graph;

    private List<Node> notWay;
    private List<Node> way;

    public Dijkstra(Graph graph) {
        this.graph = graph;
        notWay = new ArrayList<>(graph.getVertexList().size()/10);
        way = new ArrayList<>(graph.getVertexList().size()/10);
    }

    private void initializeSingleSource(Node start) {
        Iterator listIter = graph.getVertexList().iterator();
        Node currNode;

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
            if (n1val > n2val)
                return 1;
            else if (n1val < n2val)
                return -1;
            else
                return 0;
        }
    };

    //utility method to add data to queue
    private void addDataToQueue(Queue<Node> queue) {
        Iterator nodeIter = graph.getVertexList().iterator();

        while (nodeIter.hasNext()) {
            queue.add((Node) nodeIter.next());
        }
    }

    //utility method to poll data from queue
    private static void pollDataFromQueue(Queue<Node> queue) {
        while (true) {
            Node currNode = queue.poll();
            if (currNode == null) break;
            System.out.println("Processing with ID=" + currNode.getId());
        }
    }

    private Queue<Node> queue = new PriorityQueue<>(100, nodeComparator);

    private void dijkstra(Node u) {
        initializeSingleSource(u);
        addDataToQueue(queue);
        Node v;
        List<Node> adjU;
        while (!(queue.isEmpty())) {
            u = queue.poll();
            adjU = graph.getAdjList().get(u);
            if (adjU == null)
                adjU = new ArrayList<>();
            Iterator nodeIter = adjU.iterator();
            while (nodeIter.hasNext()) {
                v = (Node) nodeIter.next();
                Double w = graph.getWeightList().get(u.getId() + v.getId());
                relax(u, v, w);
            }
        }
    }

    public List<Node> getWay(Node start, Node end) {
        dijkstra(start);
        notWay.clear();
        way.clear();

        notWay.add(end);

        if(p.get(end) == null) {
            return null;
        }

        while (end != start) {
            end = p.get(end);
            notWay.add(end);
        }
        for (int i = notWay.size() - 1; i >= 0; i--) {
            way.add(notWay.get(i));
        }

        if (way.size() == 0)
            return null;


        return way;
    }
}
