package top.supcar.server;

import com.google.gson.Gson;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import org.eclipse.jetty.websocket.api.Session;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.Car;
import top.supcar.server.model.CityCar;
import top.supcar.server.model.creation.CarSetter;
import top.supcar.server.model.creation.CityCarFactory;
import top.supcar.server.parse.OSMData;
import top.supcar.server.physics.Physics;
import top.supcar.server.update.CarsUpdater;
import top.supcar.server.update.WorldUpdater;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 1 on 25.04.2017.
 */
public class ClientProcessor {
	private SessionObjects sessionObjects;
	private Session session;
	private Gson gson;

	public ClientProcessor(Session session) {
		this.session = session;
		this.gson = new Gson();
	}

	public void prepare() {

		String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";

		Node ll = new Node(0, 59.9179682, 30.258916);
		Node ur = new Node(0, 59.945318820, 30.343717);

		sessionObjects = new SessionObjects();
		SelectedRect selectedRect = new SelectedRect(ll, ur);
		sessionObjects.setSelectedRect(selectedRect);

		Physics physics = new Physics(sessionObjects);
		sessionObjects.setPhysics(physics);

		Distance distance = new Distance(selectedRect);
		sessionObjects.setDistance(distance);

		Map<String, Way> roads;
		OSMData data = new OSMData(url, sessionObjects);
		//data.loadData();
		data.makeMap();
		roads = data.getMap();
		Graph graph = new Graph(roads);
		sessionObjects.setGraph(graph);

		CarHolder carHolder = new CarHolder(sessionObjects, 100);
		sessionObjects.setCarHolder(carHolder);

		CarSetter cSetter = new CarSetter(sessionObjects, 1);
		sessionObjects.setCarSetter(cSetter);

		CarsUpdater carsUpdater = new CarsUpdater(sessionObjects);

		sessionObjects.setCarsUpdater(carsUpdater);
		WorldUpdater worldUpdater = new WorldUpdater(sessionObjects);
		sessionObjects.setWorldUpdater(worldUpdater);

	}

	public void go() {
		WorldUpdater worldUpdater = sessionObjects.getWorldUpdater();
		SelectedRect selectedRect = sessionObjects.getSelectedRect();
		while (true) {
			worldUpdater.update();
			try {
				sendJson();
				//session.getRemote().sendString(lat + " " + lon;
				Thread.sleep(70);
			} catch (Exception e) {System.err.println("caught exception");}
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
			session.getRemote().sendString(point);
		} catch (Exception e) {
			e.printStackTrace();
		}


	}
}

