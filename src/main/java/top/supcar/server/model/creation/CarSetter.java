package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.Car;
import top.supcar.server.model.RoadThing;

import java.util.*;

/**
 * Created by 1 on 26.04.2017.
 */
public class CarSetter {
	private SessionObjects sessionObjects;
	private CityCarFactory ccFactory;
	private int busyLvl;
	List<Node> sources;
	List<Node> sinks;

	public CarSetter(SessionObjects sessionObjects, int busyLvl) {
		this.busyLvl = busyLvl;
		this.sessionObjects = sessionObjects;
		ccFactory = new CityCarFactory(sessionObjects);
		findSrcsSinks();
		setCars();
	}

	/**
	 * Помещает машины на карту в момент начала моделирования
	 */

	private void setCars() {
		if(sources == null || sinks == null) {
			findSrcsSinks();
		}
		Map.Entry<String, Way> entry;
		List<Node> road;
		Iterator<Map.Entry<String, Way>> it = sessionObjects.getGraph().getInterMap().entrySet().iterator();

		int i = 0;
		int cars = 5;

		while(it.hasNext() && i < cars) {
			entry = it.next();
			road = entry.getValue().getNodes();
			for(Node nd : road) {
				if(i >= cars)
					break;
				if(placeCar(nd) != null)
					i++;
			}
		}
		//sessionObjects.getCarHolder().dump();
	}

	public void maintain() {
		for (Node nd : sources) {
			//	placeCar(nd);
		}
	}

	private void findSrcsSinks() {
		Map<Node, List<Node>> adjList = sessionObjects.getGraph().getAdjList();
		List<Node> candidates = new ArrayList<>();
		List<Node> sources = new ArrayList<>();
		//find candidates
		Map<String, Way> map = sessionObjects.getGraph().getInterMap();
		Iterator<Map.Entry<String, Way>> mapIt = map.entrySet().iterator();
		Map.Entry<String, Way> mapEntry;
		List<Node> road;
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
		while (cndIt.hasNext()) {
			nd = cndIt.next();
			adjVertexes = adjList.get(nd);
			if (adjVertexes != null) {
				//TODO if(она не последняя ни для какой дороги)
				sources.add(nd);
				cndIt.remove();
			}
		}
		this.sources = sources;
		sinks = candidates;

		System.out.println("sources: " + sources.size() +" sinks: " + sinks.size());
	}

	private Car placeCar(Node nd) {

		Distance distance = sessionObjects.getDistance();
		CarHolder holder = sessionObjects.getCarHolder();
		int sinksSize = sinks.size();
		boolean carsNearby = false;
		Car cr = null;
		List<RoadThing> nearbyList;

		if (Math.random() <= CreationConstants.spawnProbability.get(busyLvl)) {
			nearbyList = holder.getNearby(nd);
			if(nearbyList != null) {
				for (RoadThing car : holder.getNearby(nd)) {
					if (distance.distanceBetween(nd, car.getPos()) > 20)
						carsNearby = true;
				}
			}
																								/*TODO: change 20 to RECOMMENDED_DIST*/
			if (carsNearby) {
				return null;
			}else	{
				for(int i = 0; i < 10 && cr == null; i++) {

					//		System.out.println("i: " + i + " cr: " + cr);

					cr = ccFactory.createCar(nd, sinks.get((int) Math.random() *
							sinksSize));
				}
			}
		}

		return cr;

	}
}

