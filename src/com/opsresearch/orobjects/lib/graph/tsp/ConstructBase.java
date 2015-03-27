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

import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;

public abstract class ConstructBase extends TSPBase {

	protected class Vert {
		private int _idx;
		private Vert _next;
		private Vert _nextFree;

		Vert(int idx) {
			_idx = idx;
			setNext(null);
			setNextFree(null);
		}

		public int getIdx() {
			return _idx;
		}

		public void setNextFree(Vert _nextFree) {
			this._nextFree = _nextFree;
		}

		public Vert getNextFree() {
			return _nextFree;
		}

		public void setNext(Vert _next) {
			this._next = _next;
		}

		public Vert getNext() {
			return _next;
		}
	}

	private Vert _head;
	private Vert _tail;
	private Vert _free;

	public ConstructBase() {
	}

	protected abstract void construct() throws TourNotFoundException;

	public double constructOpenTour(Object originKey, Object destinationKey)
			throws TourNotFoundException, VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		if (originKey.equals(destinationKey))
			throw new TSPError(
					"The origin and destination keys are equal, use 'constructClosedTour' instead.");
		VertexI orig = getGraph().getVertex(originKey);
		if (orig == null)
			throw new VertexNotFoundException(
					"The origin vertex does not exist");
		VertexI dest = getGraph().getVertex(destinationKey);
		if (dest == null)
			throw new VertexNotFoundException(
					"The destination vertex does not exist");
		setClosed((orig == dest));
		initVertices(orig.getIndex(), dest.getIndex());
		initTour(getFromSelectedIdx(), getToSelectedIdx());
		construct();
		return getCost();
	}

	public double constructOpenTourFrom(Object originKey)
			throws TourNotFoundException, VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		VertexI orig = getGraph().getVertex(originKey);
		if (orig == null)
			throw new VertexNotFoundException(
					"The origin vertex does not exist");
		setClosed(false);
		initVertices(orig.getIndex(), -1);
		initTour(getFromSelectedIdx(), -1);
		construct();
		return getCost();
	}

	public double constructOpenTourTo(Object destinationKey)
			throws TourNotFoundException, VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		VertexI dest = getGraph().getVertex(destinationKey);
		if (dest == null)
			throw new VertexNotFoundException(
					"The destination vertex does not exist");
		setClosed(false);
		initVertices(-1, dest.getIndex());
		initTour(-1, getToSelectedIdx());
		construct();
		return getCost();
	}

	public double constructOpenTour() throws TourNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		setClosed(false);
		initVertices(-1, -1);
		initTour(-1, -1);
		construct();
		return getCost();
	}

	public double constructClosedTour() throws TourNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		setClosed(true);
		initVertices(-1, -1);
		initTour(0, 0);
		construct();
		return getCost();
	}

	private void initTour(int head, int tail) throws TourNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		if (getSize() < 2)
			throw new TSPError(
					"The TSP must have at least two vertices selected.");
		int n = getSize();
		setClosed((head != -1 && head == tail));
		setHead(new Vert(head));
		setTail(new Vert(tail));
		getHead().setNext(getTail());
		setFree(null);
		for (int i = 0; i < n; i++) {
			if (i != head && i != tail) {
				Vert v = new Vert(i);
				v.setNextFree(getFree());
				setFree(v);
			}
		}
	}

	protected void saveTour() {
		int n = getSize();
		setBestCost(0.0);
		setBestTour(new int[n]);
		Vert v = (getHead().getIdx() == -1) ? getHead().getNext() : getHead();
		for (int i = 0; i < n; i++, v = v.getNext()) {
			getBestTour()[i] = v.getIdx();
			if (v.getNext() != null)
				setBestCost(getBestCost()
						+ forwardCost(v.getIdx(), v.getNext().getIdx()));
		}
	}

	public void selectVertex(boolean[] select) {
		if (getGraph() == null)
			throw new TSPError("The graph is not set.");
		checkChangeCount();
		if (select.length != getGraph().sizeOfVertices())
			throw new TSPError(
					"The size of the select array  must equal the number of vertices in the graph.");
		for (int i = 0; i < getSelected().length; i++)
			getSelected()[i] = select[i];
	}

	public void selectVertex(boolean select) {
		if (getGraph() == null)
			throw new TSPError("The graph is not set.");
		checkChangeCount();
		for (int i = 0; i < getSelected().length; i++)
			getSelected()[i] = select;
	}

	public void selectVertex(Object key, boolean select)
			throws VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph is not set.");
		checkChangeCount();
		VertexI vertex = getGraph().getVertex(key);
		if (vertex == null)
			throw new VertexNotFoundException();
		getSelected()[vertex.getIndex()] = select;
	}

	public void setHead(Vert _head) {
		this._head = _head;
	}

	public Vert getHead() {
		return _head;
	}

	public void setFree(Vert _free) {
		this._free = _free;
	}

	public Vert getFree() {
		return _free;
	}

	public void setTail(Vert _tail) {
		this._tail = _tail;
	}

	public Vert getTail() {
		return _tail;
	}

}
