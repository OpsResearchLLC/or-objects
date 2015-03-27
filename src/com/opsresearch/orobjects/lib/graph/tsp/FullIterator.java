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

public class FullIterator extends ConstructBase implements ConstructI {
	public FullIterator() {

	}

	Vert[] verts;
	double bestCost;

	protected void construct() throws TourNotFoundException {
		int cnt = 0;
		for (Vert n = getFree(); n != null; n = n.getNextFree())
			cnt++;
		verts = new Vert[cnt];
		cnt = 0;
		for (Vert n = getFree(); n != null; n = n.getNextFree())
			verts[cnt++] = n;
		bestCost = Double.POSITIVE_INFINITY;
		permute(cnt - 1, getHead(), 0.0);
		setHead(null);
		setTail(null);
		setFree(null);
	}

	private void permute(int p, Vert prev, double cost) {
		Vert pv = verts[p];
		if (p == 0) {
			double cst = cost + forwardCost(prev.getIdx(), pv.getIdx()) + forwardCost(pv.getIdx(), getTail().getIdx());
			if (cst < bestCost) {
				prev.setNext(pv);
				pv.setNext(getTail());
				saveTour();
				bestCost = cst;
			}
			return;
		}

		for (int i = 0; i <= p; i++) {
			Vert tv = verts[i];
			verts[i] = pv;
			prev.setNext(tv);
			permute(p - 1, tv, cost + forwardCost(prev.getIdx(), tv.getIdx()));
			verts[i] = tv;
		}
	}

}
