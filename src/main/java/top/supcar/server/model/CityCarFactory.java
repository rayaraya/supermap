package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.holder.Holder;

import java.util.List;

/**
	* Created by 1 on 19.04.2017.
	*/
public class CityCarFactory implements CarFactory {

				SessionObjects sessionObjects;

				public CityCarFactory(SessionObjects sessionObjects) {
								this.sessionObjects = sessionObjects;
				}

				@Override
				public CityCar createCar(Node start, Node destination) {

								Graph graph = sessionObjects.getGraph();
								Distance distance = sessionObjects.getDistance();
								CarHolder carHolder = sessionObjects.getCarHolder();
								Driver driver = new Driver();
								double angle = 0;
								List<Node> route = graph.getWay(start, destination);


								if(route.get(1) != null && route.get(0) != null) {
												double dx = distance.latDegToMeters(route.get(1).getLat() - route.get(0)
																.getLat());
												double dy = distance.lonDegToMeters(route.get(1).getLon() - route.get(0)
																.getLon());

												angle = Math.acos(dx/Math.sqrt(dx*dx+dy*dy));

												if(dy < 0)
																angle *= -1;
								}

								CityCar car = new CityCar(sessionObjects, route, driver, ModelConstants
												.CITY_CAR_DEF_SPEED, angle);

								carHolder.updatePosition(car);
								car.setToNextNode(distance.distanceBetween(route.get(0), route.get(1)));

								return car;

				}
}
