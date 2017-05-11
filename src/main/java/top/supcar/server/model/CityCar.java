package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by 1 on 18.04.2017.
 */
public class CityCar extends Car {

	public CityCar(SessionObjects sessionObjects, List<Node> route, Driver driver,
				   double maxAcc, double mu) {
		this.routeList = route;
		this.speed = speed;
		//this.destIndex = route.size() - 1;
		this.sessionObjects = sessionObjects;
		this.mu = mu;
		routeArray = new ArrayList<Node>();

		Iterator it = route.iterator();

		while(it.hasNext()) {
			routeArray.add((Node)it.next());
		}

		//	System.out.println("Citycar, routearr: " + routeArray);

		this.maxAcc = maxAcc;
		this.driver = driver;
		driver.setCar(this);
		type = "C";
		pos = new Node(0, route.get(0).getLat(), route.get(0).getLon());
		toNextNode = sessionObjects.getDistance().distanceBetween(routeArray.get(0),
				routeArray.get(1));
		setMaxSpeeds();

	}



}
