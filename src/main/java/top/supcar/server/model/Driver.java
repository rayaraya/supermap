package top.supcar.server.model;

/**
	* Created by 1 on 18.04.2017.
	*/
public class Driver {

				private Car car;

				public Car getCar() {
								return car;
				}

				public double pushPedal() {
								double accelerate;
								if(car.speed < 14)
												accelerate = 0.6;
								else
												accelerate = 0;

								return accelerate;
				}

				public double turnHelm() {
								return 0;
				}

				public void setCar(Car car) {
								this.car = car;
				}
}
