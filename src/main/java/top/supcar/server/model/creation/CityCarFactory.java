package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.model.CityCar;
import top.supcar.server.model.Driver;
import top.supcar.server.model.ModelConstants;
import top.supcar.server.model.creation.CarFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 19.04.2017.
 */
public class CityCarFactory implements CarFactory {

	SessionObjects sessionObjects;

	public CityCarFactory(SessionObjects sessionObjects) {
		this.sessionObjects = sessionObjects;
	}

	@Override
	public CityCar createCar(Node start, Node destination) {

		Graph graph = sessionObjects.getGraph();
		Distance distance = sessionObjects.getDistance();
		CarHolder carHolder = sessionObjects.getCarHolder();
		Driver driver = new Driver();
		double angle = 0;
		double maxAcc;
		List<Node> route = graph.getWay(start, destination);
		if(route == null) {
           // System.out.println("no way");
            return null;
        }

		//test : show routes
       /* List<double[]> routeCoords = new ArrayList<>();
        for(Node nd: route) {
            double[] coords = {nd.getLon(), nd.getLat()};
            routeCoords.add(coords);
            sessionObjects.getClientProcessor().sendTestCoord(routeCoords);
            try {
                Thread.sleep(200);
            } catch (Exception e) {
                System.out.println("EXEPTION!");
            }

        }
		sessionObjects.getClientProcessor().sendTestCoord(routeCoords);
        try {
            Thread.sleep(2000);
        } catch (Exception e) {
            System.out.println("EXEPTION!");
        }*/


		if(route.get(1) != null && route.get(0) != null) {
			double dx = distance.latDegToMeters(route.get(1).getLat() - route.get(0)
					.getLat());
			double dy = distance.lonDegToMeters(route.get(1).getLon() - route.get(0)
					.getLon());

			angle = Math.acos(dx/Math.sqrt(dx*dx+dy*dy));

			if(dy < 0)
				angle *= -1;
		}

		maxAcc = ModelConstants.CITY_CAR_DEF_MAX_ACC;
		maxAcc += (Math.random() - 0.5)*maxAcc;
		double mu = ModelConstants.CITY_CAR_DEF_MU*0.7;
        mu += Math.random()*0.6*mu;
		CityCar car = new CityCar(sessionObjects, route, driver, maxAcc, mu);

		/*List<Node> dbg = new ArrayList<>();
		Node sink = route.get(route.size() - 1);
		dbg.add(route.get(route.size() - 1));
        if(!sink.getId().equals("N-13"))
            sessionObjects.getClientProcessor().drawNodes(dbg, 2);*/

		carHolder.updatePosition(car);

		return car;

	}
}
