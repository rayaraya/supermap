package top.supcar.server.dijkstra;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.graph.Graph;

import java.util.*;

/**
 * This class allows to calculate the shortest way between the vertexes of the {@link Graph} objects.
 * The type of objects is {@link Node}.
 * Dijkstra algorithm is used.
 * @author nataboll
 */

public class Dijkstra {
//ATTRIBUTES
    /** The list of upper way estimates from start vertex for every vertex of the {@link Graph} object. **/
    private Map<Node, Double> d = new HashMap<>();
    /** The list of ancestors for every vertex. **/
    private Map<Node, Node> p = new HashMap<>();
    /** The {@link Graph} object. **/
    private Graph graph;

    /** The way from the last to the first. **/
    private List<Node> notWay;
    /** The way from the first to the last which is needed. **/
    private List<Node> way;

//CONSTRUCTOR
    public Dijkstra(Graph graph) {
        this.graph = graph;
        notWay = new ArrayList<>(graph.getVertexList().size()/10);
        way = new ArrayList<>(graph.getVertexList().size()/10);
    }

//OTHER METHODS

    /**
     * Initializing lists for the search (two fields).
     * @param start is start vertex.
     */
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

    /**
     * Relaxation procedure.
     * Updates the length of the shortest way if necessary.
     * @param u is the current vertex.
     * @param v is the next vertex the current is connected with.
     * @param w is the weight of the current edge.
     */
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

    /**
     * Utility method to add data to queue.
     */
    private void addDataToQueue(Queue<Node> queue) {
        Iterator nodeIter = graph.getVertexList().iterator();

        while (nodeIter.hasNext()) {
            queue.add((Node) nodeIter.next());
        }
    }

    /**
     * Utility method to poll data from queue.
     * @param queue is the queue to be analyzed.
     */
    private static void pollDataFromQueue(Queue<Node> queue) {
        while (true) {
            Node currNode = queue.poll();
            if (currNode == null) break;
            System.out.println("Processing with ID=" + currNode.getId());
        }
    }

    /** The queue of the vertexes formed with comparator. **/
    private Queue<Node> queue = new PriorityQueue<>(100, nodeComparator);

    /**
     * Initializes lists for the search (two fields).
     * Fills the queue with all the vertexes.
     * Realises Dijkstra algorithm.
     * @param u is the first vertex.
     */
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

    /**
     * Makes initialisation.
     * Fills the queue with all the vertexes.
     * Realises Dijkstra algorithm.
     * Finds the way according to ancestors.
     * @param start is the vertex to start.
     * @param end is the finish.
     * @return the shortest way between start and finish.
     */
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
