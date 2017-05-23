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

package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.graph.Graph;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 1 on 26.04.2017.
 */
public class CreationParams {
    static final double DEF_TL_PERIOD = 15;
	private static Map<Integer, Double> spawnProbability = new HashMap<Integer, Double>();

    private static final int DEFAULT_LVL = 1;

    static final double DEFAULT_LVL_INIT_SPAWN_PROBABILITY = 0.5;

    static final double SERVICE_SPAWN_PERIOD = 180;
	static final double LIVING_STREET_SPAWN_PERIOD = 180;
    static final double RESIDENTIAL_SPAWN_PERIOD = 180;
    static final double TERTIARY_SPAWN_PERIOD = 10;
    static final double SECONDARY_SPAWN_PERIOD = 1;
    static final double PRIMARY_SPAWN_PRIOD = 0.3;
    static final double OTHER_SPAWN_PERIOD = 10;




	static {
		//spawnProbability.put(DEFAULT_LVL, 0.1);
	}

}
