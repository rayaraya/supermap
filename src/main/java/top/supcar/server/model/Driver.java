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
		double accelerate, tempacc;
		if(car.speed < ModelConstants.CITY_MAX_SPEED) {
            accelerate = 0.6;
            if(car.speed < 5) // ускорение на старте
                accelerate = 0.8;
        }
		else
			accelerate = 0;

		if(car.toNextNode < 100) {
            //System.out.println("maxspeed : " +car.maxspeeds.get(car.prevNodeIndex + 1));
            tempacc = (Math.pow(car.maxspeeds.get(car.prevNodeIndex + 1), 2) - Math.pow(car.speed, 2))/
                    (2*car.toNextNode);
		    if(tempacc < accelerate)
		        accelerate = tempacc;
        }

        if(accelerate > 1) accelerate = 1;
		else if(accelerate < -1) accelerate = -1;



		return accelerate;
	}

	public double turnHelm() {
		return 0;
	}

	public void setCar(Car car) {
		this.car = car;
	}
}
