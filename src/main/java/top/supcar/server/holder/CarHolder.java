package top.supcar.server.holder;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SelectedRect;
import top.supcar.server.SessionObjects;
import top.supcar.server.model.Car;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by 1 on 24.04.2017.
 */
public class CarHolder extends Holder {

	public CarHolder(SessionObjects sessionObjects, double cellSize) {
		super(sessionObjects, cellSize);
	}

	public void updatePosition(Car car) {
		SelectedRect selectedRect = sessionObjects.getSelectedRect();
		//System.out.println("srect urlat: " + selectedRect.getUpperRight().getLat());
		double lon = car.getPos().getLon();
		double lat = car.getPos().getLat();
		int row = findRow(lon), line = findLine(lat);
		int[] posInTable;

		if(adresses.containsKey(car)) {
			posInTable = adresses.get(car);
			//		System.out.println("row: " + row + " line: " + line + " pos0 " +posInTable[0] + " pos1 " + posInTable[1]);
			if(row != posInTable[0] ||  line != posInTable[1]) {
				removeCar(car);
				//	System.out.println("remove");
				if(row > -1 && line > -1)
					putCar(car, row, line);
				else {
                    if(car.getPrevNodeIndex() != car.getRouteList().size() - 1) {
                        System.out.println("car deleted and we dont know why, sending car and its route's last node");
                        List<Node> dbg = new ArrayList<>();
                        dbg.add(car.getPos());
                        dbg.add(car.getRouteList().get(car.getRouteList().size() - 1));
                        sessionObjects.getClientProcessor().drawNodes(dbg, 0.4           );

                    }
                }
				//	dump();
			}
		} else if(row > -1 && line > -1) {
			putCar(car, row, line);
		}
		ArrayList<Node> routelist = car.getRouteList();
		if((routelist.size() - 1) == car.getPrevNodeIndex())
		    removeCar(car);
    }

	public void removeCar(Car car) {
								/*SelectedRect selectedRect = sessionObjects.getSelectedRect();
								double lon = car.getPos().getLon();
								double lat = car.getPos().getLat();
								double upperRightLon = selectedRect.getUpperRight().getLon();
								double upperRightLat = selectedRect.getUpperRight().getLat();
								double lowerLeftLon = selectedRect.getLowerLeft().getLon();
								double lowerLeftLat = selectedRect.getLowerLeft().getLat();
								if (lon > upperRightLon)
												lon = upperRightLon - cellSizeLonDeg;
								if (lon < lowerLeftLon)
												lon = lowerLeftLon + cellSizeLonDeg;
								if (lat > upperRightLat)
												lat = upperRightLat - cellSizeLatDeg;
								if (lat < lowerLeftLat)
												lat = lowerLeftLat + cellSizeLatDeg;

								int row = findRow(lon);
								int line = findLine(lat);

								List cell = table.get(row).get(line);

								int index = cell.indexOf(car);

								if (index < 0) {
												System.out.println("!!! error occurred while removing car: car didn't " +
																				"found " +
																				"in cell (" + row + " , " + line + ")" +
																				" !!! ");
								} else {
												cell.remove(index);
												adresses.remove(car);
								}*/

		int[] adr = adresses.get(car);
		int row = adr[0];
		int line = adr[1];
		List cell = table.get(row).get(line);

		int index = cell.indexOf(car);

		if (index < 0) {
			System.err.println("error occurred while removing car: car didn't " +
					"found " +
					"in cell (" + row + " , " + line + ")");
		} else {
			cell.remove(index);
			adresses.remove(car);
		}




	}



	private void putCar(Car car, int row, int line) {

		int[] posInTable = new int[2];
		posInTable[0] = row;
		posInTable[1] = line;
		//System.out.println("put car in row: " + row + " line: " + line);

		adresses.put(car, posInTable);
		table.get(row).get(line).add(car);

								/*System.out.println(car +" ( " + car.getPos().getLon() + ", " + car.getPos()
																.getLat() + ") "  +
																" " +
																"placed at: " +	row
																+ " " +
																line);*/
	}

	public ArrayList<Car> getCars(){
		Iterator it = iterator();
		ArrayList<Car> list = new ArrayList<>();
		while(it.hasNext()) {
			list.add((Car)it.next());
		}

		return list;

	}


}
