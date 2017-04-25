package top.supcar.server;

import top.supcar.server.graph.Distance;
import top.supcar.server.holder.Holder;
import top.supcar.server.physics.Physics;

/**
	* Created by 1 on 24.04.2017.
	*/
public class SessionObjs {
				private Holder holder;
				private SelectedRect selectedRect;
				private Physics physics;
				private Distance distance;
				private double timeQuant = 0.01;

				public void setDistance(Distance distance) {
								this.distance = distance;
				}

				public void setHolder(Holder holder) {
								this.holder = holder;
				}

				public void setPhysics(Physics physics) {
								this.physics = physics;
				}

				public void setSelectedRect(SelectedRect selectedRect) {
								this.selectedRect = selectedRect;
				}

				public void setTimeQuant(double timeQuant) {
								this.timeQuant = timeQuant;
				}

				public Distance getDistance() {
								return distance;
				}

				public Holder getHolder() {
								return holder;
				}

				public Physics getPhysics() {
								return physics;
				}

				public SelectedRect getSelectedRect() {
								return selectedRect;
				}

				public double getTimeQuant() {
								return timeQuant;
				}
}
