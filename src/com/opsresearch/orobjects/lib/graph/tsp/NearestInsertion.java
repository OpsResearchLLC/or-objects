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

public class NearestInsertion extends ConstructBase implements ConstructI {
	public NearestInsertion() {

	}

	protected void construct() throws TourNotFoundException {
		if (getHead().getIdx() == -1 && getTail().getIdx() == -1) {
			if (getFree() == null)
				throw new TourNotFoundException();
			Vert v = getFree();
			setFree(v.getNextFree());
			getHead().setNext(v);
			v.setNext(getTail());
		}

		Vert k, kPrev = null, kMin = null, kPrevMin = null;
		Vert j, jMin = null;
		while (getFree() != null) {
			kMin = null;
			kPrev = null;
			double min = Double.POSITIVE_INFINITY;
			for (k = getFree(); k != null; kPrev = k, k = k.getNextFree()) {
				for (j = getHead(); j != getTail(); j = j.getNext()) {
					double d;
					if (j.getIdx() != -1 && (d = forwardCost(j.getIdx(), k.getIdx())) < min) {
						kMin = k;
						kPrevMin = kPrev;
						jMin = j;
						min = d;
					}
					if (j.getNext().getIdx() != -1 && (d = forwardCost(k.getIdx(), j.getNext().getIdx())) < min) {
						kMin = k;
						kPrevMin = kPrev;
						jMin = j;
						min = d;
					}
				}
			}

			if (kMin == null)
				throw new TourNotFoundException();

			jMin = null;
			min = Double.POSITIVE_INFINITY;

			for (j = getHead(); j != getTail(); j = j.getNext()) {

				double d = forwardCost(j.getIdx(), kMin.getIdx()) + forwardCost(kMin.getIdx(), j.getNext().getIdx())
						- forwardCost(j.getIdx(), j.getNext().getIdx());

				if (d < min) {
					jMin = j;
					min = d;
				}
			}

			if (kPrevMin == null)
				setFree(kMin.getNextFree());
			else
				kPrevMin.setNextFree(kMin.getNextFree());

			kMin.setNext(jMin.getNext());
			jMin.setNext(kMin);
		}
		saveTour();
		setHead(null);
		setTail(null);
		setFree(null);
	}

}
