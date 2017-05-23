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

package top.supcar.server.session;

import com.google.gson.Gson;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import org.eclipse.jetty.websocket.api.Session;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.Car;
import top.supcar.server.model.creation.CarSetter;
import top.supcar.server.parse.OSMData;
import top.supcar.server.physics.Physics;
import top.supcar.server.update.CarsUpdater;
import top.supcar.server.update.WorldUpdater;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 25.04.2017.
 */
public class ClientProcessor {
	private SessionObjects sessionObjects;
	private Session session;
	private Gson gson;
	private int runFlag;
	private int stopFlag;
	private OSMData data;
	private Kmp kmp;
	private int X = 1;

	public ClientProcessor(Session session) {
		this.session = session;
		this.gson = new Gson();
		runFlag = 0;
		stopFlag = 0;
	}

	public class Kmp extends Thread {
		public void run() {
			go();
		}
	}
	private void prepare(Node ll, Node ur ) {
        String url = "http://www.overpass-api.de/api/xapi?way[bbox="+ll.getLon() +"," +ll.getLat()+","+ur.getLon()+","+ur.getLat()+"]";

		sessionObjects = new SessionObjects();
		sessionObjects.setClientProcessor(this);

		SelectedRect selectedRect = new SelectedRect(ll, ur);
		sessionObjects.setSelectedRect(selectedRect);

		Physics physics = new Physics(sessionObjects);
		sessionObjects.setPhysics(physics);

		Distance distance = new Distance(selectedRect);
		sessionObjects.setDistance(distance);

		Map<String, Way> roads;
		data = new OSMData(url, sessionObjects);
		data.loadData();
		System.out.println("map downloaded from XAPI ");
		data.makeMap();
		roads = data.getMap();
		Graph graph = new Graph(roads, sessionObjects);
		sessionObjects.setGraph(graph);

		CarHolder carHolder = new CarHolder(sessionObjects, 100);
		sessionObjects.setCarHolder(carHolder);

		CarSetter cSetter = new CarSetter(sessionObjects, 1);
		sessionObjects.setCarSetter(cSetter);

		CarsUpdater carsUpdater = new CarsUpdater(sessionObjects);

		sessionObjects.setCarsUpdater(carsUpdater);
		WorldUpdater worldUpdater = new WorldUpdater(sessionObjects);
		sessionObjects.setWorldUpdater(worldUpdater);
		runFlag = 1;
	}

	private void go() {
		WorldUpdater worldUpdater = sessionObjects.getWorldUpdater();
		while (true) {
			if(stopFlag == 1){
				break;
			}
			if(runFlag == 1) {
				worldUpdater.update();
				//System.out.println(Instant.now());
				try {
					sendJson();
					Thread.sleep(20/X);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			try {
				Thread.sleep(2);
			} catch (Exception e){
				e.printStackTrace();
			}
		}

	}

	public void stop(){
		runFlag = 0;
		data.clear();
	}

	public void pause(){
		runFlag = 0;
	}

	public void play() {
		runFlag = 1;
	}

	public void handleMsg (String message){

        Map result = (Map) gson.fromJson(message, Object.class);
        if(result.keySet().toArray()[0].equals("SelectedRect")) {
			String[] coordinates = result.get(result.keySet().toArray()[0]).toString().split(",");
			Node ll = new Node(0, Double.parseDouble(coordinates[1]), Double.parseDouble(coordinates[0]));
			Node ur = new Node(0, Double.parseDouble(coordinates[3]), Double.parseDouble(coordinates[2]));
			this.prepare(ll, ur);
			kmp = new Kmp();
			kmp.start();
			//this.go();
		}

        if(result.keySet().toArray()[0].equals("button_msg")){
            String msg = (String)result.get(result.keySet().toArray()[0]);
            if(msg.equals("close")){
                stop();
			}
			if(msg.equals("pause")){
				pause();
			}
			if(msg.equals("play")){
				play();
			}
        }
		if(result.keySet().toArray()[0].equals("speed_change")){
			Map values = (Map)result.get(result.keySet().toArray()[0]);
			Double oldVal = (Double)values.get(values.keySet().toArray()[0]);
			Double newVal = (Double)values.get(values.keySet().toArray()[1]);
			if(!oldVal.equals(newVal)){
				setX(newVal.intValue());
            }

		}
		if(result.keySet().toArray()[0].equals("capacity_change")){
			Map values = (Map)result.get(result.keySet().toArray()[0]);
			Double oldVal = (Double)values.get(values.keySet().toArray()[0]);
			Double newVal = (Double)values.get(values.keySet().toArray()[1]);
			if(!oldVal.equals(newVal)){
				setCapacity(newVal.intValue());
            }
		}


    }
	private void sendJson() {
		ArrayList<double[]> carsCoordinates = new ArrayList<>();
		ArrayList<Car> cars = sessionObjects.getCarHolder().getCars();

		//System.out.println("num of cars: " + cars.size());

		for (Car car : cars) {
			double[] coordinates = {car.getPos().getLon(), car.getPos().getLat()};
			carsCoordinates.add(coordinates);
		}
		try {
			String point = gson.toJson(carsCoordinates);
			if(session.isOpen()) {
				session.getRemote().sendString(point);
			}
		} catch (Exception e) {
			stop();
			System.out.println("closing connection on client side");
			e.printStackTrace();
		}

	}
	public void sendTestCoord(List<double[]> list) {
        try {
            String point = gson.toJson(list);
			if(session.isOpen()) {
				session.getRemote().sendString(point);
			}
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param list list
     * @param pause in seconds
     */
    public void drawNodes(List<Node> list, double pause) {
        List<double[]> coords = new ArrayList<>();
        for(Node node : list) {
            double[] coord = {node.getLon(), node.getLat()};
            coords.add(coord);
            sessionObjects.getClientProcessor().sendTestCoord(coords);
            try {
                Thread.sleep((long)pause*1000);
            } catch (Exception e) {
                System.out.println("EXEPTION!");
            }

        }

    }

    private void setX(int X) {
        sessionObjects.getWorldUpdater().setX(X);
        this.X = X;
    }
    private void setCapacity(int capacity) {
        sessionObjects.getCarSetter().setCapacity(capacity);
    }

}

