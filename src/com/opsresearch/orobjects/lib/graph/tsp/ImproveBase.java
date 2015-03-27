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

import java.util.Vector;

import com.opsresearch.orobjects.lib.graph.ElementI;

public abstract class ImproveBase extends TSPBase {
	public class Vert {
		private int _idx;
		private Vert _next;
		private double _cost;
		private double _forwardSum;
		private double _reverseSum;

		Vert(int idx) {
			_idx = idx;
			setNext(null);
		}

		public Vert setNext(Vert _next) {
			this._next = _next;
			return _next;
		}

		public Vert getNext() {
			return _next;
		}

		public void setForwardSum(double _forwardSum) {
			this._forwardSum = _forwardSum;
		}

		public double getForwardSum() {
			return _forwardSum;
		}

		public void setCost(double _cost) {
			this._cost = _cost;
		}

		public double getCost() {
			return _cost;
		}

		public int getIdx() {
			return _idx;
		}

		public void setReverseSum(double _reverseSum) {
			this._reverseSum = _reverseSum;
		}

		public double getReverseSum() {
			return _reverseSum;
		}
	}

	private Vert _head;
	private Vert _tail;

	public ImproveBase() {

	}

	protected abstract void improve() throws TourNotFoundException;

	public double improveOpenTour(Vector<ElementI> tour) throws TourNotFoundException {
		return improveOpenTour(tour, false, false);
	}

	public double improveOpenTour(Vector<ElementI> tour, boolean fixedOrigin, boolean fixedDestination)
			throws TourNotFoundException {
		if (tour.firstElement() == tour.lastElement())
			throw new TSPError("The tour is not open, use 'improveClosedTour' instead.");
		setClosed(false);
		initVertices(tour);
		int head = fixedOrigin ? 0 : -1;
		int tail = fixedDestination ? getSize() - 1 : -1;
		initTour(head, tail);
		improve();
		return getCost();
	}

	public double improveClosedTour(Vector<ElementI> tour) throws TourNotFoundException {
		if (tour.firstElement() != tour.lastElement())
			throw new TSPError("The tour is not closed, use 'improveOpenTour' instead.");
		setClosed(true);
		initVertices(tour);
		initTour(0, 0);
		improve();
		return getCost();
	}

	private void initTour(int head, int tail) {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		setHead(new Vert(head));
		setTail(new Vert(tail));
		Vert v = getHead();
		for (int i = 0; i < getSize(); i++) {
			if (i != head && i != tail)
				v = (v.setNext(new Vert(i)));
		}
		v.setNext(getTail());
		setVertCosts();
	}

	protected void saveTour() {
		int n = getSize();
		setBestCost(0.0);
		setBestTour(new int[n]);
		Vert v = (getHead().getIdx() == -1) ? getHead().getNext() : getHead();
		for (int i = 0; i < n; i++, v = v.getNext()) {
			getBestTour()[i] = v.getIdx();
			if (v.getNext() != null)
				setBestCost(getBestCost() + forwardCost(v.getIdx(), v.getNext().getIdx()));
		}
	}

	protected final void flip(Vert a, Vert b) {
		Vert n, p = null;
		for (Vert v = a; v != b; v = n) {
			n = v.getNext();
			v.setNext(p);
			p = v;
		}
		b.setNext(p);
	}

	protected final void setVertCosts() {
		double fc = 0.0;
		double rc = 0.0;
		for (Vert v = getHead(); v != getTail(); v = v.getNext()) {
			v.setCost(forwardCost(v.getIdx(), v.getNext().getIdx()));
			v.setReverseSum(rc);
			v.setForwardSum(fc);
			rc += reverseCost(v.getIdx(), v.getNext().getIdx());
			fc += v.getCost();
		}
		getTail().setReverseSum(rc);
		getTail().setForwardSum(fc);
	}

	public void setHead(Vert _head) {
		this._head = _head;
	}

	public Vert getHead() {
		return _head;
	}

	public void setTail(Vert _tail) {
		this._tail = _tail;
	}

	public Vert getTail() {
		return _tail;
	}

}
