package top.supcar.server.parse;

import info.pavie.basicosmparser.model.Element;
import info.pavie.basicosmparser.model.Node;
import info.pavie.basicosmparser.model.Way;
import top.supcar.server.SelectedRect;
import top.supcar.server.SessionObjects;
import top.supcar.server.WSServer;
import top.supcar.server.graph.Distance;
import top.supcar.server.graph.Graph;
import top.supcar.server.holder.CarHolder;
import top.supcar.server.holder.Holder;
import top.supcar.server.model.Car;
import top.supcar.server.model.CityCar;
import top.supcar.server.model.CityCarFactory;
import top.supcar.server.update.CarsUpdater;
import top.supcar.server.update.WorldUpdater;

import java.util.List;
import java.util.Map;

public class TMain {
    public static void main(String[] args) throws Exception {

        WSServer server = new WSServer();
        server.run();
    }
}
