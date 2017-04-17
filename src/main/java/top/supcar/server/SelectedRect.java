package top.supcar.server;

import info.pavie.basicosmparser.model.Node;

/**
	* Created by 1 on 17.04.2017.
	*/
public class SelectedRect {
				private static Node lowerLeft;
				private static Node upperRight;

				public SelectedRect(Node lowerLeft, Node upperRight) {
								this.lowerLeft = lowerLeft;
								this.upperRight = upperRight;
				}

				public static Node getUpperRight() {
								return upperRight;
				}

				public static Node getLowerLeft() {
								return lowerLeft;
				}
}
