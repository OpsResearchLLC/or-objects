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

public class ThreeOpt extends ImproveBase implements ImproveI {
	public ThreeOpt() {

	}

	protected void improve() throws TourNotFoundException {
		final byte NONE = -1;
		final byte A_B_A1_C_B1_C1 = 0;
		final byte A_B1_C_B_A1_C1 = 1;
		final byte A_B1_C_A1_B_C1 = 2;
		final byte A_C_B1_A1_B_C1 = 3;

		boolean gotOne = true;
		Vert a, b, c, a1, b1, c1, aMax, bMax, cMax;
		while (gotOne) {
			gotOne = false;
			aMax = null;
			bMax = null;
			cMax = null;
			byte type = NONE;
			double maxSave = 0.0;
			for (a = getHead(); a != getTail(); a = a.getNext()) {
				a1 = a.getNext();
				for (b = a.getNext(); b != getTail(); b = b.getNext()) {
					b1 = b.getNext();
					for (c = b.getNext(); c != getTail(); c = c.getNext()) {
						c1 = c.getNext();
						double cng, cur = a.getCost() + b.getCost() + c.getCost();
						if (!isSymmetric()) {
							cur += (b.getForwardSum() - a1.getForwardSum());
							cur += (c.getForwardSum() - b1.getForwardSum());
						}

						cng = forwardCost(a.getIdx(), b.getIdx()) + forwardCost(a1.getIdx(), c.getIdx())
								+ forwardCost(b1.getIdx(), c1.getIdx());
						if (!isSymmetric()) {
							cng += (b.getReverseSum() - a1.getReverseSum());
							cng += (c.getReverseSum() - b1.getReverseSum());
						}
						if (cng < cur && (aMax == null || (cur - cng) > maxSave)) {
							type = A_B_A1_C_B1_C1;
							aMax = a;
							bMax = b;
							cMax = c;
							maxSave = cur - cng;
						}

						cng = forwardCost(a.getIdx(), b1.getIdx()) + forwardCost(c.getIdx(), b.getIdx())
								+ forwardCost(a1.getIdx(), c1.getIdx());
						if (!isSymmetric()) {
							cng += (b.getReverseSum() - a1.getReverseSum());
							cng += (c.getForwardSum() - b1.getForwardSum());
						}
						if (cng < cur && (aMax == null || (cur - cng) > maxSave)) {
							type = A_B1_C_B_A1_C1;
							aMax = a;
							bMax = b;
							cMax = c;
							maxSave = cur - cng;
						}

						cng = forwardCost(a.getIdx(), b1.getIdx()) + forwardCost(c.getIdx(), a1.getIdx())
								+ forwardCost(b.getIdx(), c1.getIdx());
						if (!isSymmetric()) {
							cng += (b.getForwardSum() - a1.getForwardSum());
							cng += (c.getForwardSum() - b1.getForwardSum());
						}
						if (cng < cur && (aMax == null || (cur - cng) > maxSave)) {
							type = A_B1_C_A1_B_C1;
							aMax = a;
							bMax = b;
							cMax = c;
							maxSave = cur - cng;
						}

						cng = forwardCost(a.getIdx(), c.getIdx()) + forwardCost(b1.getIdx(), a1.getIdx())
								+ forwardCost(b.getIdx(), c1.getIdx());
						if (!isSymmetric()) {
							cng += (b.getForwardSum() - a1.getForwardSum());
							cng += (c.getReverseSum() - b1.getReverseSum());
						}
						if (cng < cur && (aMax == null || (cur - cng) > maxSave)) {
							type = A_C_B1_A1_B_C1;
							aMax = a;
							bMax = b;
							cMax = c;
							maxSave = cur - cng;
						}
					}
				}
			}

			if (aMax != null) {
				a = aMax;
				b = bMax;
				c = cMax;
				a1 = a.getNext();
				b1 = b.getNext();
				c1 = c.getNext();
				gotOne = true;
				switch (type) {
				case A_B_A1_C_B1_C1:
					flip(a1, b);
					flip(b1, c);
					a.setNext(b);
					a1.setNext(c);
					b1.setNext(c1);
					break;
				case A_B1_C_B_A1_C1:
					flip(a1, b);
					a.setNext(b1);
					c.setNext(b);
					a1.setNext(c1);
					break;
				case A_B1_C_A1_B_C1:
					a.setNext(b1);
					c.setNext(a1);
					b.setNext(c1);
					break;
				case A_C_B1_A1_B_C1:
					flip(b1, c);
					a.setNext(c);
					b1.setNext(a1);
					b.setNext(c1);
					break;
				default:
					throw new TSPError("Internal Error");
				}
				setVertCosts();
			}
		}
		saveTour();
		setHead(null);
		setTail(null);
	}

}
