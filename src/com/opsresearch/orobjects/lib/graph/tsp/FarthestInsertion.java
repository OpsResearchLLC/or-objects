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

public class FarthestInsertion extends ConstructBase implements ConstructI {
	public FarthestInsertion() {

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

		Vert k, kPrev = null, kMax = null, kPrevMax = null;
		Vert j, jMin = null;
		while (getFree() != null) {
			kMax = null;
			kPrev = null;
			double max = Double.NEGATIVE_INFINITY;
			for (k = getFree(); k != null; kPrev = k, k = k.getNextFree()) {
				double min = Double.POSITIVE_INFINITY;
				for (j = getHead(); j != getTail(); j = j.getNext()) {
					double d;
					if (j.getIdx() != -1 && (d = forwardCost(j.getIdx(), k.getIdx())) < min) {
						jMin = j;
						min = d;
					}
					if (j.getNext().getIdx() != -1 && (d = forwardCost(k.getIdx(), j.getNext().getIdx())) < min) {
						jMin = j;
						min = d;
					}
				}
				if (min > max) {
					kMax = k;
					kPrevMax = kPrev;
					max = min;
				}
			}

			if (kMax == null)
				throw new TourNotFoundException();

			jMin = null;
			double min = Double.POSITIVE_INFINITY;

			for (j = getHead(); j != getTail(); j = j.getNext()) {

				double d = forwardCost(j.getIdx(), kMax.getIdx()) + forwardCost(kMax.getIdx(), j.getNext().getIdx())
						- forwardCost(j.getIdx(), j.getNext().getIdx());

				if (d < min) {
					jMin = j;
					min = d;
				}
			}

			if (kPrevMax == null)
				setFree(kMax.getNextFree());
			else
				kPrevMax.setNextFree(kMax.getNextFree());

			kMax.setNext(jMin.getNext());
			jMin.setNext(kMax);
		}
		saveTour();
		setHead(null);
		setTail(null);
		setFree(null);
	}

}
