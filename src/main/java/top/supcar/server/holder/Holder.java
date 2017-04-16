package top.supcar.server.holder;

import top.supcar.server.model.RoadThing;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
	* Container for RoadThings
	*/
public class Holder {
				private List<List<List<RoadThing>>> table;
				private Map<RoadThing, int[]> adresses;
				private int tableSizeX;
				private int tableSizeY;
				private double cellSize;
				private double lowerLeftLat, lowerLeftLon, upperRightLat, upperRightLon;


				public Holder(double lowerLeftLat, double lowerLeftLon, double upperRightLat,
				              double upperRightLon, double cellSize) {

								int i, j;
								double dX = upperRightLat - lowerLeftLat;
								double dY = upperRightLon - lowerLeftLon;

								this.cellSize = cellSize;
								this.lowerLeftLat = lowerLeftLat;
								this.lowerLeftLon = lowerLeftLon;
								this.upperRightLat = upperRightLat;
								this.upperRightLon = upperRightLon;

								tableSizeX = ((int)(dX/cellSize) == dX/cellSize) ? (int)(dX/cellSize) :
												(int)(dX/cellSize) + 1;
								tableSizeY = ((int)(dY/cellSize) == dY/cellSize) ? (int)(dY/cellSize) :
												(int)(dX/cellSize) + 1;

								table = new ArrayList<List<List<RoadThing>>>(tableSizeX);

								for(i = 0; i < tableSizeX; i++) {
												table.add(i, new ArrayList<List<RoadThing>>(tableSizeY));
												for(j = 0; j < tableSizeY; j++) {
																(table.get(i)).add(j, new LinkedList<RoadThing>());
												}
								}
				}

				public void updatePosition(RoadThing thing) {
								double lon = thing.getLon();
								double lat = thing.getLat();
								/* TODO: Add exception */
								if(!adresses.containsKey(thing)) {
												//
								}
				}
}
