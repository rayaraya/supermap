package top.supcar.server;

import info.pavie.basicosmparser.model.Node;


/**
	* Created by 1 on 17.04.2017.
	*/
public class SelectedRect {
				private Node lowerLeft;
				private Node upperRight;

				public SelectedRect(Node lowerLeft, Node upperRight) {
								this.lowerLeft = lowerLeft;
							 this.upperRight = upperRight;
				}

				public Node getUpperRight() {
								return upperRight;
				}

				public Node getLowerLeft() {
								return lowerLeft;
				}
}
