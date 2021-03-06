/*
 * Copyright 2017 SUPMUP
 *
 * This file is part of Supermap.
 *
 * Supermap is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Supermap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Supermap. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package top.supcar.server.parse;

import info.pavie.basicosmparser.controller.*;
import info.pavie.basicosmparser.model.*;
import org.xml.sax.SAXException;
import top.supcar.server.session.SelectedRect;
import top.supcar.server.session.SessionObjects;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

/**
 * This class works with OSM data <br>
 * It gets OSM data with help of {@link URLReader} <br>
 * Data parsed with {@link info.pavie.basicosmparser.BasicOSMParser} and changed to have roads only <br>
 * @author rayaraya
 */

public class OSMData {
//ATTRIBUTES
    /** Output file. **/
    private String filepath;
    /** Parsed data. **/
    private Map<String,Element> map;
    /** Changed parsed data. **/
    private Map<String,Way> siftedMap = new HashMap<String,Way>();
    /** URL generated from coordinates. **/
    private String apiURL;
    /** Objects of this session saved in {@link SessionObjects} **/
    private SessionObjects sessionObjects;

//CONSTRUCTOR
    public OSMData(String apiURL, SessionObjects sessionObjects) {
        this.apiURL = apiURL;
        this.sessionObjects = sessionObjects;
        filepath = setPath();
    }

//CONSTRUCTOR
    public OSMData() {filepath = setPath();
    }

    public void setApiURL(String apiURL){
        this.apiURL = apiURL;
    }

    /**
     * Creates object of {@link URLReader} and loads OSM data.
      */
    public void loadData(){
        try {
            URLReader reader = new URLReader();
            reader.readWrite(apiURL, filepath);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses OSM data in the file and changes it.
      */
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

    /**
     * Makes working data containing ways only.
     */
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
        Node node, nextnode, newnode;
        SelectedRect rect = sessionObjects.getSelectedRect();
        Set<String> addedNodes = new HashSet<>();
        while(iter.hasNext()) {
            //System.out.println("next");
            Way way = iter.next().getValue();
            nodes = way.getNodes();
            for (int i = 0; i < nodes.size() - 1; i++) {
                node = nodes.get(i);
                nextnode = nodes.get(i + 1);
                //добавим точки на пересечении границ и дорог
                boolean currout = false;
                boolean nextout = false;
                if (rect.inRectangle(node) && !rect.inRectangle(nextnode)) {
                    nextout = true;
                } else if (!rect.inRectangle(node) && rect.inRectangle(nextnode)) {
                    currout = true;
                }
                if (nextout || currout) {

                    newnode = nodeOnBoundaryBetween(node, nextnode);
                    if (newnode == null) {

                    }
                    else {
                        String keystr = way.getId() + newnode.getLon() + newnode.getLat();
                        if(!addedNodes.contains(keystr)) {
                            addedNodes.add(keystr);

                            if (nextout) nodes.set(i + 1, newnode);
                            if (currout) nodes.set(i, newnode);

                        }
                    }

                }
            }
        }
        iter = siftedMap.entrySet().iterator();
        while(iter.hasNext()) {
            nodes = iter.next().getValue().getNodes();
            Iterator<Node> nodesIter = nodes.iterator();
            while(nodesIter.hasNext()) {
                node = nodesIter.next();
                if(!sessionObjects.getSelectedRect().inRectangle(node)) {
                    nodesIter.remove();
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
            System.out.println("Key = " + key ); //+ ", tags = " + val.getTags());
            System.out.println(nodes);
        }
    }

    public Map<String,Way> getMap(){
        return siftedMap;
    }

    private String setPath(){
        String uniqPath = sessionObjects.toString() + ".osm";
        return uniqPath;
    }

    private void drawAllNodes() {
        Iterator<Map.Entry<String, Way>> iter = siftedMap.entrySet().iterator();
        List<Node> dbg = new ArrayList<>();
        while(iter.hasNext()) {
            List<Node> nodes = iter.next().getValue().getNodes();
            Iterator<Node> nodesIter = nodes.iterator();
            while(nodesIter.hasNext()) {
                Node node = nodesIter.next();
                dbg.add(node);
            }
        }
        sessionObjects.getClientProcessor().drawNodes(dbg, 100);

    }

    private Node nodeOnBoundaryBetween(Node n1, Node n2) {
        if(n1.getLat() == n2.getLat() && n2.getLon() == n1.getLon())
            return null;
        SelectedRect rect = sessionObjects.getSelectedRect();
        double x1 = rect.getLowerLeft().getLon();
        double x2 = rect.getUpperRight().getLon();
        double y1 = rect.getLowerLeft().getLat();
        double y2 = rect.getUpperRight().getLat();
        double n1x = n1.getLon(), n1y = n1.getLat();
        double n2x = n2.getLon(), n2y = n2.getLat();
        // если одна из точек лежит на границе
        if(rect.inRectangle(n1)) {
            if(n1x == x1 || n1x == x2 || n1y == y1 || n1y == y2)
                return null;
        } else if(rect.inRectangle(n2)) {
            if(n2x == x1 || n2x == x2 || n2y == y1 || n2y == y2)
                return null;
        }

        //получим уравнение прямой, задаваемой n1, n2 (ax + by + c = 0)
        double a = n1y  - n2y;
        double b = n2x - n1x;
        double c = n1x*n2y - n2x*n1y;
        double x, y;
        List<Node> solutions = new ArrayList<>();
        Node solution;
        if(Math.abs(a) != 0) {
            x = (-c - b*y1)/a;
            solution = new Node(-13, y1, x);
            solutions.add(solution);
            x = (-c - (b * y2)) / a;
            solution = new Node(-13, y2, x);
            solutions.add(solution);
        }
        if(Math.abs(b) != 0) {
            y = (-c - a*x1)/b;
            solution = new Node(-13, y, x1);
            solutions.add(solution);
            y = (-c - a*x2)/b;
            solution = new Node(-13, y, x2);
            solutions.add(solution);
        }
        for(Node node : solutions) {
            if(rect.inRectangle(node)) {
                if(inRect(n1, n2, node))
                    return node;
            }
        }
        return null;

    }

    private boolean inRect(Node v1, Node v2, Node nd) {
        double xleft, xright, ylow, yup;
        if(v1.getLat() > v2.getLat()) {
            ylow = v2.getLat();
            yup = v1.getLat();
        } else {
            ylow = v1.getLat();
            yup = v2.getLat();
        }
        if(v1.getLon() > v2.getLon()) {
            xleft = v2.getLon();
            xright = v1.getLon();
        } else {
            xleft = v1.getLon();
            xright = v2.getLon();
        }
        if(nd.getLon() >= xleft && nd.getLon() <= xright && nd.getLat() >= ylow && nd.getLat() <= yup)
            return true;
        else {
            return false;
        }
    }

    public void clear(){
        new File(filepath).delete();
    }

}

