package top.supcar.server.update;

import top.supcar.server.SessionObjects;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.holder.Holder;
import top.supcar.server.model.Car;
import top.supcar.server.model.RoadThing;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 1 on 19.04.2017.
 */
public class CarsUpdater {

	private SessionObjects sessionObjects;

	public CarsUpdater(SessionObjects sessionObjects) {
		this.sessionObjects = sessionObjects;
	}


	public void update() {

		CarHolder carHolder = sessionObjects.getCarHolder();
		List<Car> cars = carHolder.getCars();

		//		System.out.println( carHolder.getCars());

		for (Car car: cars) {
			car.updatePos();
			carHolder.updatePosition(car);
			//	System.out.println( carHolder.getCars());
		}


		//sessionObjects.getCarSetter().maintain();

	}
}
