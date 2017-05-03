package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.Car;
import top.supcar.server.model.ModelConstants;
import top.supcar.server.model.RoadThing;
import top.supcar.server.update.WorldUpdater;

import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.util.*;

/**
 * Created by 1 on 26.04.2017.
 */
public class CarSetter {
    private SessionObjects sessionObjects;
    private CityCarFactory ccFactory;
    private int busyLvl;
    private List<Node> sources;
    private List<Node> sinks;
    private List<Double> periodes; //how often cars should arrive at each source
    private Instant lastInstant;
    private List<Double> timePassed;
    private List<Boolean> carArrived;

    public CarSetter(SessionObjects sessionObjects, int busyLvl) {
        this.busyLvl = busyLvl;
        this.sessionObjects = sessionObjects;
        ccFactory = new CityCarFactory(sessionObjects);
        findSrcsSinks();
        setCars();
    }

    private void setPeriodes() {
        if (sources == null || sinks == null) {
            findSrcsSinks();
        }
        if (periodes == null) {
            System.out.println(sources.size());
            periodes = new ArrayList<>(sources.size());
            for(int i = 0; i < sources.size(); i++) {
                periodes.add(0.0);
            }

        }
        Map<Node, List<Way>> nodeWays = sessionObjects.getGraph().getNodeWays();
        Node node;
        double period, newPeriod;
        for (int i = 0; i < sources.size(); i++) {
            node = sources.get(i);
            period = Integer.MAX_VALUE;
            for (Way way : nodeWays.get(node)) {
                switch (way.getTags().get("highway")) {
                    case "service":
                        newPeriod = CreationParams.SERVICE_SPAWN_PERIOD;
                        break;
                    case "living_street":
                        newPeriod = CreationParams.LIVING_STREET_SPAWN_PERIOD;
                        break;
                    case "residential":
                        newPeriod = CreationParams.RESIDENTIAL_SPAWN_PERIOD;
                        break;
                    case "tertiary":
                        newPeriod = CreationParams.TERTIARY_SPAWN_PERIOD;
                        break;
                    case "secondary":
                        newPeriod = CreationParams.SECONDARY_SPAWN_PERIOD;
                        break;
                    case "primary":
                        newPeriod = CreationParams.PRIMARY_SPAWN_PRIOD;
                        break;
                    default:
                        newPeriod = CreationParams.OTHER_SPAWN_PERIOD;
                        break;
                }
                if (newPeriod < period)
                    period = newPeriod;
            }
            periodes.set(i, period);
        }
        timePassed = new ArrayList<>(sources.size());
        for(int i = 0; i < sources.size(); i++) {
            timePassed.add(0.0);
        }
        carArrived = new ArrayList<>(sources.size());
        for (int i = 0; i < sources.size(); i++) {
            carArrived.add(true);
        }


    }

    /**
     * Помещает машины на карту в момент начала моделирования
     */

    private void setCars() {

        double initSpawnProbability = CreationParams.DEFAULT_LVL_INIT_SPAWN_PROBABILITY;
        if (sources == null || sinks == null) {
            findSrcsSinks();
        }
        if (periodes == null) {
            setPeriodes();
        }
        List<Node> nodes = sessionObjects.getGraph().getVertexList();

        int i = 0;
        int cars = 10;
        int sinksSize = sinks.size();
        Node sink;

        for (Node nd : nodes) {
            sink = nd;
            if (i >= cars)
                break;
            if(Math.random() <= initSpawnProbability) {
                for(int j = 0; j < 10 && sink == nd; j++) {
                    sink = sinks.get((int) Math.random() * sinksSize);
                }
                if (ccFactory.createCar(nd, sinks.get((int) Math.random() * sinksSize)) != null) {
                    i++;
                    System.out.println(i);
                }
            }
        }

        //sessionObjects.getCarHolder().dump();
    }

    public void maintain() {
        double lastTimeQuant;
        Instant instant;
        int ndIndexInSources;
        double spawnProbability;
        Node currSource;

        Distance distance = sessionObjects.getDistance();
        CarHolder holder = sessionObjects.getCarHolder();
        int sinksSize = sinks.size();
        boolean carsNearby = false;
        Car cr = null;
        List<RoadThing> nearbyList;
        if(lastInstant == null) {
            lastInstant = Instant.now();
            System.out.println("li " + lastInstant);
            lastTimeQuant = WorldUpdater.FIRST_QUANT;
        }
        else {
            instant = Instant.now();
            lastTimeQuant = Duration.between(lastInstant,instant).toMillis();
            lastTimeQuant /= 1000;
            System.out.println("lasttq: " + lastTimeQuant + " duration: " +  Duration.between(lastInstant,instant).toMillis());
            lastInstant = instant;
        }

        for(int i = 0; i < sources.size(); i++) {
            currSource = sources.get(i);
            System.out.println("lasttimeq: " + lastTimeQuant);

            timePassed.set(i, timePassed.get(i) + lastTimeQuant);
            System.out.println("!carArrived: " + i + " timePassed: " + timePassed.get(i) + " period: " + periodes.get(i));
            if (timePassed.get(i) >= periodes.get(i)) {
                carArrived.set(i, false);
                timePassed.set(i, 0.0);
            }
            if (!carArrived.get(i)) {
                System.out.println("!carArrived: " + i + " timePassed: " + timePassed.get(i) + " period: " + periodes.get(i));
                spawnProbability = lastTimeQuant/(periodes.get(i) - timePassed.get(i));
                if (Math.random() <= spawnProbability) {
                    nearbyList = holder.getNearby(currSource);
                    if (nearbyList != null) {
                        for (RoadThing car : holder.getNearby(currSource)) {
                            if (distance.distanceBetween(currSource, car.getPos()) < ModelConstants.RECOMMENDED_DISTANCE)
                                carsNearby = true;
                        }
                    }
                    if (!carsNearby) {
                        for (int j = 0; j < 10 && cr == null; j++) {
                            //		System.out.println("i: " + i + " cr: " + cr);
                            cr = ccFactory.createCar(currSource, sinks.get((int) Math.random() *
                                    sinksSize));
                        }
                    }
                }
            }
        }
    }

    private void findSrcsSinks() {
        Map<Node, List<Node>> adjList = sessionObjects.getGraph().getAdjList();
        List<Node> candidates = new ArrayList<>(); //первые и последние ноды в дорогах
        List<Node> sources = new ArrayList<>();
        //find candidates
        Map<String, Way> map = sessionObjects.getGraph().getInterMap();
        Iterator<Map.Entry<String, Way>> mapIt = map.entrySet().iterator();
        Map.Entry<String, Way> mapEntry;
        List<Node> road;
        Map<Node, List<Way>> nodeWays = sessionObjects.getGraph().getNodeWays();
        List<Way> incWays;
        int sumsize = 0;
        while (mapIt.hasNext()) {
            mapEntry = mapIt.next();
            road = mapEntry.getValue().getNodes();
            int size = road.size();
            //System.out.println("size: "+ size + " way: " + mapEntry.getKey());
            sumsize += size;
            if (size > 0) {
                candidates.add(road.get(0));
                if (size > 1)
                    candidates.add(road.get(size - 1));
            }
        }

        System.out.println("number of candidates to be src or sink:" + candidates.size
                () + " sumsize: " + sumsize);

        List<Node> adjVertexes;
        Iterator<Node> cndIt = candidates.iterator();
        Node nd;
        List<Node> waysNodes;
        boolean ndIsSource;

        while (cndIt.hasNext()) {
            nd = cndIt.next();
            adjVertexes = adjList.get(nd);
            ndIsSource = true;

            if (adjVertexes != null && adjVertexes.size() != 0) {

                incWays = nodeWays.get(nd);
                //проверка на то, что nd не является последней ни для какой дороги
                for (Way way : incWays) {
                    waysNodes = way.getNodes();
                    if (waysNodes.indexOf(nd) == waysNodes.size() - 1)
                        ndIsSource = false;
                }
                if (ndIsSource)
                    sources.add(nd);
                cndIt.remove();
            }
        }
        //теперь в candidates только вершины, из которых нет пути ни в какие другие
        this.sources = sources;
        sinks = candidates;
        //добавим к точкам назначения точки старта, расположенные на маленьких дорогах
        String highway;
        for (Node node : sources) {
            incWays = nodeWays.get(node);
            for (Way way : incWays) {
                highway = way.getTags().get("highway");
                if (highway.equals("service") || highway.equals("living street") || highway.equals("residential"))
                    sinks.add(node);
                else;
                    //System.out.println(highway);
            }
        }


        System.out.println("sources: " + sources.size() + " sinks: " + sinks.size());
    }
}

