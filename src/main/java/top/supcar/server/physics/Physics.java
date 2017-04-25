package top.supcar.server.physics;

import top.supcar.server.SessionObjects;
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

				}
}
