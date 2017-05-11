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
	//protected int destIndex;
	protected Driver driver;
	protected ArrayList<Node> routeArray;
	protected double maxAcc;
	protected double currStep;
	protected double toNextNode;
	protected int prevNodeIndex = 0;
	protected int line = 1;
	protected double mu;
	protected List<Double> maxspeeds; //maxspeed for each node in route

	public void updatePos() {
	    int destIndex = routeArray.size() - 1;
		double requestedAcc = driver.pushPedal()*maxAcc;
		double ratio, dx, dy;
		Physics physics = sessionObjects.getPhysics();
		Distance distance = sessionObjects.getDistance();
		Node prev, next;

		//Attention! Shit code

		physics.getStepSpeed(this, requestedAcc);

		while (currStep >= toNextNode) {
			currStep -= toNextNode;
			prevNodeIndex++;
			if(prevNodeIndex == destIndex)
			    return;
            pos.setLat(routeArray.get(prevNodeIndex).getLat());
            pos.setLon(routeArray.get(prevNodeIndex).getLon());
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

    public List<Double> getMaxspeeds() {
        return maxspeeds;
    }

    public ArrayList<Node> getRouteList() {
		return routeArray;
	}

    public int getPrevNodeIndex() {
        return prevNodeIndex;
    }

    protected void setMaxSpeeds() {
        maxspeeds = new ArrayList<>();
        Distance distance = sessionObjects.getDistance();
        double direction = 0, newDirection, angle, dx, dy, radius, maxspeed;
        Node currNode, nextNode;
        double frictionK = sessionObjects.getPhysics().getFrictionCoef();
        for(int i = 0; i < routeArray.size() - 1; i++) {
            currNode = routeArray.get(i);
            nextNode = routeArray.get(i+1);
            dy = distance.latDegToMeters(nextNode.getLat() - currNode.getLat());
            dx = distance.lonDegToMeters(nextNode.getLon() - currNode.getLon());
            newDirection = Math.acos(dx/Math.sqrt(dx*dx+dy*dy));
            if(dy < 0)
                newDirection *= -1;
            if(i == 0)
                direction = newDirection;
            angle = newDirection - direction;
            if(Math.abs(angle) < 0.001)
                maxspeed = 400;//Integer.MAX_VALUE;
            else {
                radius = ModelConstants.LINE_BREADTH / (1 - Math.cos(angle / 2));
                maxspeed = Math.sqrt(mu*9.8*radius*frictionK);
            }
            maxspeeds.add(maxspeed);
            direction = newDirection;

        }
        if(routeArray.get(routeArray.size()-1).getId().equals("N-13")) {
            maxspeeds.add(ModelConstants.CITY_MAX_SPEED);
        } else maxspeeds.add(0.5);
    }

}
