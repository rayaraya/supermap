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

package top.supcar.server.update;

import top.supcar.server.session.SessionObjects;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.Car;

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


		sessionObjects.getCarSetter().maintain();

	}
}
