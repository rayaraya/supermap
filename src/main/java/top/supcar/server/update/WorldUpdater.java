package top.supcar.server.update;

import top.supcar.server.SessionObjects;

import java.time.Duration;
import java.time.Instant;

/**
	* Created by 1 on 19.04.2017.
	*/
public class WorldUpdater {
				private long timeQuantMillis = 10;
				private double timeQuant = 0.01;
				private SessionObjects sessionObjects;

				private Instant lastInstant;

				public WorldUpdater(SessionObjects sessionObjects) {
								this.sessionObjects = sessionObjects;
				}


				public void update() {
								Instant instant = Instant.now();
								if(lastInstant != null)
												timeQuantMillis = Duration.between(lastInstant,instant).toMillis();
								timeQuant = ((double)timeQuantMillis)/1000;
								lastInstant = instant;
								sessionObjects.getCarsUpdater().update();
				}

				public double getTimeQuant() {
								return timeQuant;
				}


}
