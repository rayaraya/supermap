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

import java.time.Duration;
import java.time.Instant;

/**
 * Created by 1 on 19.04.2017.
 */
public class WorldUpdater {
	private double timeQuant = 0.01;
	private SessionObjects sessionObjects;
	private int X = 1;
	public static final double FIRST_QUANT = 0.01;


	private Instant lastInstant;

	public WorldUpdater(SessionObjects sessionObjects) {
		this.sessionObjects = sessionObjects;
	}

	public void update() {
		Instant instant = Instant.now();
		sessionObjects.setCurrInstant(instant);
		long timeQuantMillis = (long)(1000*FIRST_QUANT)*X;
		if(lastInstant != null)
			timeQuantMillis = Duration.between(lastInstant,instant).toMillis()*X;
		timeQuant = ((double)timeQuantMillis)/1000;
		lastInstant = instant;
		sessionObjects.getCarsUpdater().update();
		//System.out.println("TimeQuant : " + timeQuantMillis);
	}

	public double getTimeQuant() {
		return timeQuant;
	}

	public void setX (int X) {
	    this.X = X;
        System.out.println("X: " + X);
    }


}
