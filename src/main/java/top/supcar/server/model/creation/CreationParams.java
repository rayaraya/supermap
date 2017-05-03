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
