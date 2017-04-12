package top.supcar.server.parse;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.*;


public class Graph {
    private static final double R = 6371000; // Earth's radius
    private static final double TRANS = Math.PI/180;
    public Map <Node, Map<Double, Node>[]> map;
    private Map<String, Element> interMap;

    public void setInterMap(){
        String url = "http://api.openstreetmap.org/api/0.6/map?bbox=30.291518484046108,59.937524639867405,30.322846685340053,59.94612279900326";
        OSMData data = new OSMData(url);
        data.loadData();
        data.makeMap();
        this.interMap = data.getMap();
    }

    public void setMap(){
        Iterator interMapIter = interMap.entrySet().iterator();
        Iterator roadIter;
        Iterator mapIter;
        Map.Entry currEntry;
        Element currElement;
        List<Node> road;

        while (interMapIter.hasNext()){
            currEntry = (Map.Entry) interMapIter.next();
            currElement = (Element) currEntry.getValue();
            road = ((Way) currElement).getNodes();
            roadIter = road.listIterator();
            Node currNode = (Node) roadIter.next();
            Node nextNode;

            while (roadIter.hasNext()){
                nextNode = (Node) roadIter.next();
                double currDistance = getDistance(currNode, nextNode);
                // adding elements
                mapIter = map.entrySet().iterator();
                while (mapIter.hasNext()){

                }
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

}