package top.supcar.server.holder;

import top.supcar.server.SelectedRect;
import top.supcar.server.SessionObjects;
import top.supcar.server.model.Car;

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
								double lon = car.getPos().getLon();
								double lat = car.getPos().getLat();
								int row, line;
								int[] posInTable;

								if(!adresses.containsKey(car)) {
												putCar(car, findRow(lon), findLine(lat));
								}
								else {
												double upperRightLon = selectedRect.getUpperRight().getLon();
												double upperRightLat = selectedRect.getUpperRight().getLat();
												double lowerLeftLon = selectedRect.getLowerLeft().getLon();
												double lowerLeftLat = selectedRect.getLowerLeft().getLat();
												if(lon >= upperRightLon || lat >= upperRightLat || lon <= lowerLeftLon ||
																				lat <= lowerLeftLat) {
																removeCar(car);
												}
												else {
																row = findRow(lon);
																line = findLine(lat);
																posInTable = adresses.get(car);
																if(row != posInTable[0] ||  line != posInTable[1]) {
																				removeCar(car);
																				putCar(car, row, line);
																				dump();																}

												}

								}


				}

				private void removeCar(Car car) {
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
												System.out.println("!!! error occurred while removing car: car didn't " +
																				"found " +
																				"in cell (" + row + " , " + line + ")" +
																				" !!! ");
								} else {
												cell.remove(index);
												adresses.remove(car);
								}


				}



				private void putCar(Car car, int row, int line) {

								int[] posInTable = new int[2];
								posInTable[0] = row;
								posInTable[1] = line;
								System.out.println("put car in row: " + row + " line: " + line);

								adresses.put(car, posInTable);
								table.get(row).get(line).add(car);

								System.out.println("car placed at: " + row + " " + line);
				}

				public List<Car> getCars(){
								Iterator it = iterator();
								List<Car> list = new LinkedList<>();
								while(it.hasNext()) {
												list.add((Car)it.next());
								}

								return list;

				}











}
