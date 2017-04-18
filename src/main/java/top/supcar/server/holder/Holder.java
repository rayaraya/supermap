package top.supcar.server.holder;

import top.supcar.server.SelectedRect;
import top.supcar.server.graph.Distance;
import top.supcar.server.model.RoadThing;

import java.util.*;

/**
	* Container for RoadThings
	*/
public class Holder {
				private List<List<List<RoadThing>>> table;
				private Map<RoadThing, int[]> adresses;
				private int tableSizeX; //(number of rows)
				private int tableSizeY;
				private double cellSizeMet, cellSizeLatDeg, cellSizeLonDeg;


				public Holder(double cellSize) {

								int i, j;
								double dX = Distance.latDegToMeters(SelectedRect.getUpperRight().getLat() - SelectedRect
												.getLowerLeft().getLat());
								double dY = Distance.lonDegToMeters(SelectedRect.getUpperRight().getLon() - SelectedRect
												.getLowerLeft().getLon());

								this.cellSizeMet = cellSize;
								cellSizeLatDeg = Distance.metersToLatDeg(cellSize);
								cellSizeLonDeg = Distance.metersToLonDeg(cellSize);


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
								int row, line;
								int[] posInTable = new int[2];
								/* TODO: Add exception */
								if(!adresses.containsKey(thing)) {
												posInTable[0] = row =(int)((lat - SelectedRect.getLowerLeft().getLat())
																/cellSizeLatDeg);
												posInTable[1] = line =(int)((lon - SelectedRect.getLowerLeft().getLon())
																/cellSizeLonDeg);
												adresses.put(thing, posInTable);
												table.get(row).get(line).add(thing);
								}
				}
				public void dump() {
								Iterator itX = table.iterator();
								Iterator itY, itCell;
								int x, y;
								while(itX.hasNext()) {
												itY = ((List)itX.next()).iterator();
												while(itY.hasNext()) {
																itCell = ((List)itY.next()).iterator();
																while(itCell.hasNext()) {
																				System.out.println( itCell.next());
																}
												}
								}
				}
}
