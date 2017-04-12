package top.supcar.server.parse;

import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.graph.Distance;

import java.util.Map;

public class TMain {
    public static void main(String[] args) throws Exception {

        String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";
        Map<String,Way> roads;
        OSMData data = new OSMData(url);
        data.loadData();
        data.makeMap();
        roads = data.getRoads();
        //data.printRoads();
        Distance.setMilestones(roads);
    }
}
