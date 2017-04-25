package top.supcar.server.holder;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SelectedRect;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;
import top.supcar.server.model.RoadThing;

import java.util.*;

/**
	* Container for RoadThings
	*/
public abstract class Holder {
				List<List<List<RoadThing>>> table;
				Map<RoadThing, int[]> adresses;
				protected int tableSizeX; //(number of rows)
				protected int tableSizeY;
				protected double cellSizeLatDeg, cellSizeLonDeg;
				protected SessionObjects sessionObjects;


				public Holder(SessionObjects sessionObjects, double cellSize) {

								this.sessionObjects = sessionObjects;
								Distance distance = sessionObjects.getDistance();
								SelectedRect selectedRect = sessionObjects.getSelectedRect();
								int i, j;
								double dY = distance.latDegToMeters(selectedRect.getUpperRight().getLat() -
												selectedRect
												.getLowerLeft().getLat());
								double dX = distance.lonDegToMeters(selectedRect.getUpperRight().getLon() -
												selectedRect
												.getLowerLeft().getLon());

								//this.cellSizeMet = cellSize;
								cellSizeLatDeg = distance.metersToLatDeg(cellSize);
								cellSizeLonDeg = distance.metersToLonDeg(cellSize);


								tableSizeX = ((int) (dX / cellSize) == dX / cellSize) ? (int) (dX / cellSize) :
												(int) (dX / cellSize) + 1;
								tableSizeY = ((int) (dY / cellSize) == dY / cellSize) ? (int) (dY / cellSize) :
												(int) (dX / cellSize) + 1;

								table = new ArrayList<List<List<RoadThing>>>(tableSizeX);

								for (i = 0; i < tableSizeX; i++) {
												table.add(i, new ArrayList<List<RoadThing>>(tableSizeY));
												for (j = 0; j < tableSizeY; j++) {
																(table.get(i)).add(j, new LinkedList<RoadThing>());
												}
								}

								System.out.println("holder sizex: " + tableSizeX + " Y: " + tableSizeY);

								adresses = new HashMap<>();
				}


				public void dump() {
								Iterator itX = table.iterator();
								Iterator itY, itCell;
								int x = 0, y = 0;
								while(itX.hasNext()) {
												itY = ((List)itX.next()).iterator();
												y = 0;
												while(itY.hasNext()) {
																itCell = ((List)itY.next()).iterator();
																while(itCell.hasNext()) {
																				System.out.println("(" + x + ";" + y + ")" + itCell.next());
																}
																y++;
												}
												x++;
								}
				}

				List<List<List<RoadThing>>> getTable() {
								return table;
				}

				public Map<RoadThing, int[]> getAdresses() {
								return adresses;
				}
				public Iterator iterator() {
								return new HolderIterator(this);
				}

				protected int findRow(double lon) {
								return (int)((lon - sessionObjects.getSelectedRect().getLowerLeft().getLon())
																/cellSizeLonDeg);
				}
				protected int findLine(double lat) {
								return (int)((lat - sessionObjects.getSelectedRect().getLowerLeft().getLat())
																/cellSizeLatDeg);
				}

				public List<RoadThing> getNearbyThings(Node node) {

								double lon = node.getLon();
								double lat = node.getLat();
								int row = findRow(lon);
								int line = findLine(lat);
								List<RoadThing> list = new LinkedList<>();

								//table.get(row).get(line).add(car);
								return null;

				}

}
