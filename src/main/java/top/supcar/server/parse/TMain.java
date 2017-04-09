package top.supcar.server.parse;

import info.pavie.basicosmparser.model.Element;
import java.util.Map;

public class TMain {
    public static void main(String[] args) throws Exception {

        String url = "http://api.openstreetmap.org/api/0.6/map?bbox=30.291518484046108,59.937524639867405,30.322846685340053,59.94612279900326";
        Map<String,Element> map;
        OSMData data = new OSMData(url);
        data.loadData();
        data.makeMap();
        map = data.getMap();
        data.printMap();
    }
}
