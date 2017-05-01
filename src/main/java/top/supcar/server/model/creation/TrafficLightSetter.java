package top.supcar.server.model.creation;

import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.SessionObjects;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.TLKunteynir;
import top.supcar.server.model.TrafficLight;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by 1 on 01.05.2017.
 */
public class TrafficLightSetter {
	public SessionObjects sessionObjects;

	public TrafficLightSetter(SessionObjects sessionObjects) {
		this.sessionObjects = sessionObjects;
	}

		/*		public void setTLs() {
								Graph graph = sessionObjects.getGraph();
								Map<String, Way> map = graph.getInterMap();
								Iterator<Map.Entry<String, Way>> it = map.entrySet().iterator();
								List<Node> road;
								TLKunteynir tlKunteynir =  sessionObjects.getTlKunteynir();
								double defPeriod =  CreationConstants.DEF_TL_PERIOD;
								double period;
								while(it.hasNext()) {
												road = it.next().getValue().getNodes();
												for (Node nd: road) {
																if(nd.getTags().get("highway") == "traffic_signals") {
																				if(tlKunteynir.getTL(nd) == null) {
																								period = defPeriod + (int)((Math.random() - 0.5)*defPeriod/2);
																								TrafficLight tl = new TrafficLight(nd, period);
																								tlKunteynir.addTL();
																				}
																}
												}



								}

				}
				*/
}
