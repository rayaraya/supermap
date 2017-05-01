package top.supcar.server.parse;

import info.pavie.basicosmparser.controller.*;
import info.pavie.basicosmparser.model.*;
import org.xml.sax.SAXException;
import top.supcar.server.SessionObjects;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class OSMData {

    private String filepath;
    private Map<String,Element> map;
    private Map<String,Way> siftedMap = new HashMap<String,Way>();
    private String apiURL;
    private SessionObjects sessionObjects;

    public OSMData(String apiURL, SessionObjects sessionObjects) {
        this.apiURL = apiURL;
        this.sessionObjects = sessionObjects;
        filepath = setPath();
    }

    public OSMData() {filepath = setPath();
    }

    public void setApiURL(String apiURL){
        this.apiURL = apiURL;
    }

    public void loadData(){
        try {
            URLReader reader = new URLReader();
            reader.readWrite(apiURL, filepath);
        } catch (Exception e) {//рассмотреть различные варианты возвр ошибок от апи
            e.printStackTrace();
        }
    }
    public void makeMap() {
        OSMParser p = new OSMParser();
        File osmFile = new File(filepath);
        try {
            map = p.parse(osmFile);
        } catch (IOException |SAXException e) {
            e.printStackTrace();
        }
        siftMap();
    }

    private void siftMap() {
        Iterator it = map.entrySet().iterator();
        String key , hkey;
        Element val;
        Way temp;

        List<Node> nodes;

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            key = (String) entry.getKey();
            val = (Element)entry.getValue();
            hkey = val.getTags().get("highway");
            if(key.charAt(0) == 'W' && hkey != null ) {
                switch (hkey) {
                    case "motorway":
                    case "trunk":
                    case "primary":
                    case "secondary":
                    case "tertiary":
                    case "unclassified":
                    case "residential":
                    case "service":
                    case "motorway_link":
                    case "trunk_link":
                    case "primary_link":
                    case "secondary_link":
                    case "tertiary_link":
                    case "living_street":
                    case "raceway":
                    case "road":
                        siftedMap.put((String)entry.getKey(),(Way)entry.getValue());
                        break;
                    default:
                        break;
                }
            }
            else
                it.remove();
        }

        Iterator<Map.Entry<String, Way>> iter = siftedMap.entrySet().iterator();
        Node node;
        while(iter.hasNext()) {
            //System.out.println("next");
            nodes = iter.next().getValue().getNodes();
            Iterator<Node> nodesIter = nodes.iterator();
            while(nodesIter.hasNext()) {
                node = nodesIter.next();
                if(!sessionObjects.getSelectedRect().inRectangle(node)) {
                    nodesIter.remove();
                    //System.out.println("removed node " + node.getLon() + " " + node.getLat());

                }
            }
        }

        map = null;
    }
    public void printSmap(){
        Iterator it = siftedMap.entrySet().iterator();
        String key;
        Element val;
        Way temp;

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            key = (String) entry.getKey();
            val = (Element) entry.getValue();
            temp = (Way) val;
            List<Node> nodes = temp.getNodes();
            System.out.println("Key = " + key + ", tags = " + val.getTags());
            System.out.println(nodes);
        }
    }
    public Map<String,Way> getMap(){
        return siftedMap;
    }

    private String setPath(){
        URL furl = getClass().getResource("/top/supcar/server/parse/map.osm");
        return furl.getPath();
    }

}

