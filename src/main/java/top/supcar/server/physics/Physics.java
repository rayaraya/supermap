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

package top.supcar.server.physics;

import top.supcar.server.session.SessionObjects;
import top.supcar.server.model.Car;
import top.supcar.server.update.WorldUpdater;

/**
 * Created by 1 on 18.04.2017.
 */
public class Physics {

	private SessionObjects sessionObjects;
	private double frictionCoef = 1;

	public Physics(SessionObjects sessionObjects) {
		this.sessionObjects = sessionObjects;
	}

	public void getStepSpeed(Car car, double requestedAcc) {

		WorldUpdater wUpdater = sessionObjects.getWorldUpdater();

		// System.out.println("phys, quant: " + wUpdater.getTimeQuant());

		car.setCurrStep(car.getSpeed()*wUpdater.getTimeQuant());

		car.setSpeed(car.getSpeed() + requestedAcc*frictionCoef*wUpdater.getTimeQuant());
        /*double speed = car.getMaxspeeds().get(car.getPrevNodeIndex() + 1);
        if(speed > 100)
            speed = 100;
        car.setSpeed(speed);*/
	}

	public double getFrictionCoef() {
		return frictionCoef;
	}
}
