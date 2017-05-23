/*
 * Copyright 2017 SUPMUP
 *
 * This file is part of Supermap.
 *
 * Supermap is free software: you can redistribute it and/or modify it under the terms of
 * the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * Supermap is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE.  See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Supermap. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package top.supcar.server.session;

import info.pavie.basicosmparser.model.Node;

/**
 * This class is for choosing the area for modeling <br>
 * The area is a rectangle <br>
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
	 * Is the {@link Node} in the rectangle <br>
	 * @param node is the {@link Node} <br>
	 * @return true if it is in rectangle and false if it is not
	 */
	public boolean inRectangle(Node node) {

		if(node.getLon() <= upperRight.getLon() && node.getLon() >= lowerLeft.getLon() &&
				node.getLat() <= upperRight.getLat() && node.getLat() >= lowerLeft.getLat())
			return true;
		else
			return false;
	}
}
