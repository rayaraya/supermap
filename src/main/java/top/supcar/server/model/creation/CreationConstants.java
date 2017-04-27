package top.supcar.server.model.creation;

import java.util.HashMap;
import java.util.Map;

/**
	* Created by 1 on 26.04.2017.
	*/
public class CreationConstants {
			 static Map<Integer, Double> spawnProbability = new HashMap<Integer, Double>();
			 static {
							 spawnProbability.put(1, 0.1);
			 }
}
