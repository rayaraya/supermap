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
												(int) (dY / cellSize) + 1;

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

				/**
					*
					* @param lon
					* @return row(X coordinate in the table) if lon is correct, -1 otherwise
					*/

				protected int findRow(double lon) {
								int row = (int)((lon - sessionObjects.getSelectedRect().getLowerLeft().getLon())
																/cellSizeLonDeg);
								return (row >= 0 && row < tableSizeX) ? row : -1;
				}
				/**
					*
					* @param lat
					* @return line(Y coordinate in the table) if lat is correct, -1 otherwise
					*/
				protected int findLine(double lat) {
								int line =  (int)((lat - sessionObjects.getSelectedRect().getLowerLeft().getLat())
																/cellSizeLatDeg);
								return (line >= 0 && line < tableSizeY) ? line : -1;
				}

				public List<RoadThing> getNearby(Node node) {
								boolean closeToLeft = false, closeToRight = false, closeToUp = false,
																closeToLow = false;
								double lon = node.getLon();
								double lat = node.getLat();
								int row = findRow(lon);
								int line = findLine(lat);
								if(row < 0 || line < 0) {
												System.err.println("Can't find nearby things: node isn't in rect. Node " +
																				node.getId() +" : (" +
																				node.getLon() + ", " + node.getLat() + ")");
												return null;
								}
								List<RoadThing> list = new LinkedList<>();
								for (RoadThing thing: table.get(row).get(line)) {
												list.add(thing);
								}
								double toLeftBorder = lon - cellSizeLonDeg*row;
								double toRightBorder = cellSizeLatDeg - toLeftBorder;
								double toUpperBorder = lat - cellSizeLatDeg*line;
								double toLowerBorder = cellSizeLatDeg - lat;
								if(toLeftBorder < cellSizeLonDeg/2) {
												if(row > 0) {
																closeToLeft = true;
																for (RoadThing thing: table.get(row - 1).get(line)) {
																				list.add(thing);
																}
												}
								}
								if(toRightBorder < cellSizeLonDeg/2) {
												if(row < tableSizeX - 1) {
																closeToRight = true;
																for (RoadThing thing: table.get(row + 1).get(line)) {
																				list.add(thing);
																}
												}
								}
								if(toLowerBorder < cellSizeLatDeg/2) {
												if(line > 0) {
																closeToLow = true;
																for (RoadThing thing: table.get(row).get(line - 1)) {
																				list.add(thing);
																}
												}
								}
								if(toUpperBorder < cellSizeLatDeg/2) {
												if(line < tableSizeY) {
																closeToUp = true;
																for (RoadThing thing: table.get(row - 1).get(line)) {
																				list.add(thing);
																}
												}
								}
								if(closeToLeft && closeToUp) {
												for (RoadThing thing: table.get(row - 1).get(line + 1)) {
																list.add(thing);
												}
								}
								if(closeToUp && closeToRight) {
												for (RoadThing thing: table.get(row + 1).get(line + 1)) {
																list.add(thing);
												}
								}
								if(closeToRight && closeToLow) {
												for (RoadThing thing: table.get(row + 1).get(line - 1)) {
																list.add(thing);
												}
								}
								if(closeToLeft && closeToLow) {
												for (RoadThing thing: table.get(row - 1).get(line - 1)) {
																list.add(thing);
												}
								}
								return list;
				}

}
