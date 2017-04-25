package top.supcar.server.model;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
	* Created by 1 on 18.04.2017.
	*/
public class CityCar extends Car {

				public CityCar(SessionObjects sessionObjects, List<Node> route, Driver driver,
				               double speed,
				               double orientation) {
								this.routeList = route;
								this.speed = speed;
								this.orientation = orientation;
								this.destIndex = route.size() - 1;
								this.sessionObjects = sessionObjects;
								routeArray = new ArrayList<Node>();

								Iterator it = route.iterator();

								while(it.hasNext()) {
												routeArray.add((Node)it.next());
								}

							//	System.out.println("Citycar, routearr: " + routeArray);

								this.maxAcc = ModelConstants.CITY_CAR_DEF_MAX_ACC;
								this.driver = driver;
								driver.setCar(this);
								type = "C";
								pos = routeArray.get(0);


				}


}
