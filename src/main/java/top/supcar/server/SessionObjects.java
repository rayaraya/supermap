package top.supcar.server;

import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.holder.Holder;
import top.supcar.server.model.CityCarFactory;
import top.supcar.server.physics.Physics;
import top.supcar.server.update.CarsUpdater;
import top.supcar.server.update.WorldUpdater;

/**
	* Created by 1 on 24.04.2017.
	*/
public class SessionObjects {
				private CarHolder carHolder;
				private SelectedRect selectedRect;
				private Physics physics;
				private Distance distance;
				private CarsUpdater carsUpdater;
				private WorldUpdater worldUpdater;
				private Graph graph;
				private CityCarFactory cityCarFactory;


				public void setCityCarFactory(CityCarFactory cityCarFactory) {
								this.cityCarFactory = cityCarFactory;
				}

				public void setCarsUpdater(CarsUpdater carsUpdater) {
								this.carsUpdater = carsUpdater;
				}

				public void setGraph(Graph graph) {
								this.graph = graph;
				}

				public void setWorldUpdater(WorldUpdater worldUpdater) {
								this.worldUpdater = worldUpdater;
				}

				public void setDistance(Distance distance) {
								this.distance = distance;
				}

				public void setCarHolder(CarHolder carHolder) {
								this.carHolder = carHolder;
				}

				public void setPhysics(Physics physics) {
								this.physics = physics;
				}

				public void setSelectedRect(SelectedRect selectedRect) {
								this.selectedRect = selectedRect;
				}


				public WorldUpdater getWorldUpdater() {
								return worldUpdater;
				}

				public CarsUpdater getCarsUpdater() {
								return carsUpdater;
				}

				public Distance getDistance() {
								return distance;
				}

				public CarHolder getCarHolder() {
								return carHolder;
				}

				public Physics getPhysics() {
								return physics;
				}

				public SelectedRect getSelectedRect() {
								return selectedRect;
				}

				public Graph getGraph() {
								return graph;
				}

				public CityCarFactory getCityCarFactory() {
								return cityCarFactory;
				}
}
