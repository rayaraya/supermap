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

package top.supcar.server.holder;

import top.supcar.server.model.RoadThing;

import java.util.Iterator;
import java.util.List;

/**
 * Created by 1 on 19.04.2017.
 */
public class HolderIterator implements Iterator{
	List<List<List<RoadThing>>> table;
	private Holder holder;
	private Iterator rowIt, lineIt, cellIt;
	private int currThing = 0;
	private int numOfThings;

	HolderIterator(Holder holder) {
		this.holder = holder;
		rowIt = holder.getTable().iterator();
		lineIt = ((List)rowIt.next()).iterator();
		cellIt = ((List)lineIt.next()).iterator();
		numOfThings = holder.getAdresses().entrySet().size();
	}

	@Override
	public boolean hasNext() {

		//System.out.println("currThing: " + currThing + " numOfThings: " + numOfThings);

		if(numOfThings > currThing)
			return true;
		else
			return false;
	}

	@Override
	public Object next() {
		if(cellIt.hasNext()) {
			currThing++;
			return cellIt.next();
		}
		while(lineIt.hasNext()) {
			cellIt = ((List)lineIt.next()).iterator();
			if(cellIt.hasNext()) {
				currThing++;
				return cellIt.next();
			}
		}
		while(rowIt.hasNext()) {
			lineIt = ((List)rowIt.next()).iterator();
			while(lineIt.hasNext()) {
				cellIt = ((List) lineIt.next()).iterator();
				if (cellIt.hasNext()) {
					currThing++;
					return cellIt.next();
				}
			}

		}
		return null;
	}

}
