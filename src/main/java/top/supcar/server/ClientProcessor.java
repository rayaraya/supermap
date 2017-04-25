package top.supcar.server;

import com.google.gson.Gson;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import org.eclipse.jetty.websocket.api.Session;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.CityCar;
import top.supcar.server.model.CityCarFactory;
import top.supcar.server.parse.OSMData;
import top.supcar.server.physics.Physics;
import top.supcar.server.update.CarsUpdater;
import top.supcar.server.update.WorldUpdater;

import java.io.IOException;
import java.util.Map;

/**
	* Created by 1 on 25.04.2017.
	*/
public class ClientProcessor {
				SessionObjects sessionObjects;
				private Session session;
				Gson gson;
				public ClientProcessor(Session session) {
								this.session = session;
								this.gson = new Gson();
				}

				public void prepare() {
								String url = "http://www.overpass-api.de/api/xapi?way[bbox=30.258916543827283,59.917968282222404,30.34371726404213,59.94531882096226]";

								Node ll = new Node(0, 59.94480136, 30.252207);
								Node ur = new Node(0, 59.94965, 30.2636247);

								sessionObjects = new SessionObjects();
								SelectedRect selectedRect = new SelectedRect(ll, ur);
								sessionObjects.setSelectedRect(selectedRect);

								Physics physics = new Physics(sessionObjects);
								sessionObjects.setPhysics(physics);

								Distance distance = new Distance(selectedRect);
								sessionObjects.setDistance(distance);

								Map<String, Way> roads;
								OSMData data = new OSMData(url);
								data.loadData();
								data.makeMap();
								roads = data.getMap();
								Graph graph = new Graph();
								sessionObjects.setGraph(graph);

								graph.setInterMap(roads);
								graph.setMap();

								CarHolder carHolder = new CarHolder(sessionObjects, 100);
								sessionObjects.setCarHolder(carHolder);

								CityCarFactory ccFactory = new CityCarFactory(sessionObjects);
								sessionObjects.setCityCarFactory(ccFactory);

								CarsUpdater carsUpdater = new CarsUpdater(sessionObjects);

								sessionObjects.setCarsUpdater(carsUpdater);
								WorldUpdater worldUpdater = new WorldUpdater(sessionObjects);
								sessionObjects.setWorldUpdater(worldUpdater);
				}

				public void go() {
								WorldUpdater worldUpdater = sessionObjects.getWorldUpdater();
								CityCarFactory ccFactory = new CityCarFactory(sessionObjects);
								SelectedRect selectedRect =  sessionObjects.getSelectedRect();
								CityCar corolla = ccFactory.createCar(selectedRect.getLowerLeft(), selectedRect
																.getUpperRight());
								while(true) {
												worldUpdater.update();
												double lat = corolla.getPos().getLat();
												double lon = corolla.getPos().getLon();
												double[] cts =  {lat, lon};
												try {
																sendJson(cts);
																System.out.println(cts);
																//session.getRemote().sendString(lat + " " + lon;
																Thread.sleep(10);
												}catch (Exception e) {System.out.println("caught exception");}
								}

				}

				private void sendJson(double[] coor){
								double[][] cord = {coor};
								String point = gson.toJson(cord);
					try {
						session.getRemote().sendString(point);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

}
