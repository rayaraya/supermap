package top.supcar.server;

import info.pavie.basicosmparser.model.Node;

/**
 * This class is for choosing the area for modeling.
 * The area is a rectangle.
 * @author niquepolice
 */

public class SelectedRect {
//ATTRIBUTES
	/** The lower left corner. **/
	private Node lowerLeft;
	/** The upper right corner. **/
	private Node upperRight;

//METHODS
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

	/**
	 * Is the {@link Node} in the rectangle.
	 * @param node is the {@link Node}.
	 * @return true if it is in rectangle and false if it is not.
	 */
	public boolean inRectangle(Node node) {

		if(node.getLon() <= upperRight.getLon() && node.getLon() >= lowerLeft.getLon() &&
				node.getLat() <= upperRight.getLat() && node.getLat() >= lowerLeft.getLat())
			return true;
		else
			return false;
	}
}
