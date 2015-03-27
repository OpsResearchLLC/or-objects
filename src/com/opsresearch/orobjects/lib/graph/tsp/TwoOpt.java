/* ***** BEGIN LICENSE BLOCK *****
 * 
 * Copyright (C) 2012 OpsResearch LLC (a Delaware company)
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License, version 3,
 * as published by the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * ***** END LICENSE BLOCK ***** */

package com.opsresearch.orobjects.lib.graph.tsp;

public class TwoOpt extends ImproveBase implements ImproveI {
	public TwoOpt() {
	}

	protected void improve() throws TourNotFoundException {
		boolean gotOne = true;
		while (gotOne) {
			gotOne = false;
			if (getHead().getNext() == getTail())
				continue;
			Vert aMax = null, bMax = null;
			double maxSave = 0.0;
			for (Vert a = getHead(); a != getTail(); a = a.getNext()) {
				for (Vert b = a.getNext(); b != getTail(); b = b.getNext()) {
					double cur = a.getCost() + b.getCost();
					double cng = forwardCost(a.getIdx(), b.getIdx())
							+ forwardCost(a.getNext().getIdx(), b.getNext().getIdx());
					if (!isSymmetric()) {
						cur += (b.getForwardSum() - a.getNext().getForwardSum());
						cng += (b.getReverseSum() - a.getNext().getReverseSum());
					}
					if (cng < cur && (aMax == null || (cur - cng) > maxSave)) {
						aMax = a;
						bMax = b;
						maxSave = cur - cng;
					}
				}
			}
			if (aMax != null) {
				Vert a1 = aMax.getNext();
				Vert b1 = bMax.getNext();
				flip(a1, bMax);
				aMax.setNext(bMax);
				a1.setNext(b1);
				gotOne = true;
				setVertCosts();
			}
		}
		saveTour();
		setHead(null);
		setTail(null);
	}

}
