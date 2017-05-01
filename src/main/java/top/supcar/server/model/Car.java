package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;
import top.supcar.server.physics.Physics;
import top.supcar.server.update.WorldUpdater;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 16.04.2017.
 */
public abstract class Car extends RoadThing {
	protected List<Node> routeList;
	protected SessionObjects sessionObjects;
	protected double speed;
	protected double orientation;
	protected int destIndex;
	protected Driver driver;
	protected ArrayList<Node> routeArray;
	protected double maxAcc;
	protected double currStep;
	protected double toNextNode;
	protected int prevNodeIndex = 0;

	public void updatePos() {
		double requestedAcc = driver.pushPedal()*maxAcc;
		double ratio, dx, dy;
		Physics physics = sessionObjects.getPhysics();
		Distance distance = sessionObjects.getDistance();
		Node prev, next;

		physics.getStepSpeed(this, requestedAcc);

		while (currStep >= toNextNode && prevNodeIndex < destIndex - 1) {
			currStep -= toNextNode;
			prevNodeIndex++;
			toNextNode = distance.distanceBetween(routeArray.get(prevNodeIndex),
					routeArray.get(prevNodeIndex + 1));
		}

		prev = routeArray.get(prevNodeIndex);
		next = routeArray.get(prevNodeIndex + 1);
		ratio = currStep / distance.distanceBetween(prev, next);
		dy = distance.latDegToMeters(next.getLat() - prev.getLat()) * ratio;
		dx = distance.lonDegToMeters(next.getLon() - prev.getLon()) * ratio;
		pos.setLat(pos.getLat() + distance.metersToLatDeg(dy));
		pos.setLon(pos.getLon() + distance.metersToLonDeg(dx));
		toNextNode -= currStep;
	};

	public void setSpeed(double speed) {
		this.speed = speed;
	}
	public void setCurrStep(double currStep) {
		this.currStep = currStep;
	}

	public double getSpeed() {
		return speed;
	}

	public List<Node> getRouteList() {
		return routeList;
	}
}
