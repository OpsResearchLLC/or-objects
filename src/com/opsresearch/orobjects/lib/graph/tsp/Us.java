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

public class Us extends Geni implements ImproveI {
	public Us(int sizeOfNeighborhood) {
		super(sizeOfNeighborhood);
	}

	public double improveOpenTour(Vector<ElementI> tour) throws TourNotFoundException {
		return improveOpenTour(tour, false, false);
	}

	public double improveOpenTour(Vector<ElementI> tour, boolean fixedOrigin, boolean fixedDestination)
			throws TourNotFoundException {
		if (tour.firstElement() == tour.lastElement())
			throw new TSPError("The tour is not open, use 'improveClosedTour' instead.");
		setClosed(false);
		int head = -1;
		int tail = -1;
		_p = _neighborhood;
		initVertices(tour);
		if (fixedOrigin) {
			_p++;
			head = 0;
		}
		if (fixedDestination) {
			_p++;
			tail = getSize() - 1;
		}
		initTourUS(head, tail);
		improve();
		return getCost();
	}

	public double improveClosedTour(Vector<ElementI> tour) throws TourNotFoundException {
		if (tour.firstElement() != tour.lastElement())
			throw new TSPError("The tour is not closed, use 'improveOpenTour' instead.");
		setClosed(true);
		_p = _neighborhood;
		initVertices(tour);
		initTourUS(0, 0);
		improve();
		return getCost();
	}

	protected void initTourUS(int head, int tail) {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		_verts = new Vert[getSize()];
		for (int i = 0; i < getSize(); i++)
			_verts[i] = new Vert(i);
		_virtualHead = head == -1 ? new Vert(-1) : null;
		_virtualTail = tail == -1 ? new Vert(-1) : null;

		_head = head == -1 ? _virtualHead : _verts[head];
		_tail = isClosed() ? _head : tail == -1 ? _virtualTail : _verts[tail];
		int ib = head == -1 ? 0 : 1;
		int ie = (isClosed() || tail == -1) ? getSize() : getSize() - 1;

		_head._inTour = true;
		_tail._inTour = true;
		addNeighbor(_head);
		if (!isClosed())
			addNeighbor(_tail);

		Vert last = _head;
		for (int i = ib; i < ie; i++) {
			Vert v = _verts[i];
			if (v._inTour)
				throw new TSPError("The initial tour has duplicate vertices");
			newForwardEdge(last, v);
			v._inTour = true;
			addNeighbor(v);
			last = v;
		}
		newForwardEdge(last, _tail);

		_head._reverse = new Vert(_head);
		_tail._reverse = new Vert(_tail);
		for (Vert v = _head._next; v != _tail; v = v._next)
			v._reverse = new Vert(v);
	}

	protected void improve() throws TourNotFoundException {
		int n = getSize();
		setBestTour(calcTour());
		setBestCost(calcCost());

		int v = 0;
		while (v < n) {
			Vert vv = _verts[v];
			_insState = INIT;
			_rmvState = INIT;

			if (isClosed() && vv == _head)
				_head = _tail = vv._prev;
			forwardFindBestRemove(vv);
			reverseFindBestRemove(vv);
			switch (_rmvState) {
			case TYPE0:
				removeType0(vv);
				break;
			case FORWARD_TYPE1:
				forwardRemoveType1(vv, _rmvVj, _rmvVk, _rmvVj._next, _rmvVk._next);
				break;
			case FORWARD_TYPE2:
				forwardRemoveType2(vv, _rmvVj, _rmvVk, _rmvVl, _rmvVj._prev, _rmvVk._next, _rmvVl._next);
				break;
			case REVERSE_TYPE1:
				reverseRemoveType1(vv, _rmvVj, _rmvVk, _rmvVj._prev, _rmvVk._prev);
				break;
			case REVERSE_TYPE2:
				reverseRemoveType2(vv, _rmvVj, _rmvVk, _rmvVl, _rmvVj._next, _rmvVk._prev, _rmvVl._prev);
				break;
			default:
				v++;
				continue;
			}
			addToTour(vv);

			_head._reverse.setReverse(_head);
			_tail._reverse.setReverse(_tail);
			for (Vert z = _head._next; z != _tail; z = z._next)
				z._reverse.setReverse(z);

			double c = calcCost();
			if (c < getBestCost()) {
				setBestCost(c);
				setBestTour(calcTour());
			} else {
				v++;
			}
		}
		_head = _tail = null;
	}

	protected void removeType0(Vert vv) {
		newForwardEdge(vv._prev, vv._next);
	}

	protected void replaceType0(Vert vv) {
		newForwardEdge(vv._prev, vv);
		newForwardEdge(vv, vv._next);
	}

	protected void forwardRemoveType1(Vert vv, Vert vj, Vert vk, Vert vj1, Vert vk1) {
		Vert vp1 = vv._next;
		Vert vm1 = vv._prev;

		forwardFlip(vp1, vk);
		forwardFlip(vk1, vj);
		newForwardEdge(vm1, vk);
		newForwardEdge(vp1, vj);
		newForwardEdge(vk1, vj1);
	}

	protected void forwardReplaceType1(Vert vv, Vert vj, Vert vk, Vert vj1, Vert vk1) {
		Vert vp1 = vv._next;
		Vert vm1 = vv._prev;

		reverseFlip(vp1, vk);
		reverseFlip(vk1, vj);
		newForwardEdge(vm1, vv);
		newForwardEdge(vv, vp1);
		newForwardEdge(vj, vj1);
		newForwardEdge(vk, vk1);
	}

	protected void forwardRemoveType2(Vert vv, Vert vj, Vert vk, Vert vl, Vert vj1, Vert vk1, Vert vl1) {
		Vert vp1 = vv._next;
		Vert vm1 = vv._prev;

		forwardFlip(vp1, vj1);
		forwardFlip(vl1, vk);
		newForwardEdge(vm1, vk);
		newForwardEdge(vl1, vj1);
		newForwardEdge(vp1, vj);
		newForwardEdge(vl, vk1);
	}

	protected void forwardReplaceType2(Vert vv, Vert vj, Vert vk, Vert vl, Vert vj1, Vert vk1, Vert vl1) {
		Vert vp1 = vv._next;
		Vert vm1 = vv._prev;

		reverseFlip(vp1, vj1);
		reverseFlip(vl1, vk);
		newForwardEdge(vm1, vv);
		newForwardEdge(vv, vp1);
		newForwardEdge(vj1, vj);
		newForwardEdge(vk, vk1);
		newForwardEdge(vl, vl1);
	}

	protected void reverseRemoveType1(Vert vv, Vert vj, Vert vk, Vert vj1, Vert vk1) {
		Vert vp1 = vv._prev;
		Vert vm1 = vv._next;

		reverseFlip(vp1, vk);
		reverseFlip(vk1, vj);
		newReverseEdge(vm1, vk);
		newReverseEdge(vp1, vj);
		newReverseEdge(vk1, vj1);
	}

	protected void reverseReplaceType1(Vert vv, Vert vj, Vert vk, Vert vj1, Vert vk1) {
		Vert vp1 = vv._prev;
		Vert vm1 = vv._next;

		forwardFlip(vp1, vk);
		forwardFlip(vk1, vj);
		newReverseEdge(vm1, vv);
		newReverseEdge(vv, vp1);
		newReverseEdge(vj, vj1);
		newReverseEdge(vk, vk1);
	}

	protected void reverseRemoveType2(Vert vv, Vert vj, Vert vk, Vert vl, Vert vj1, Vert vk1, Vert vl1) {
		Vert vp1 = vv._prev;
		Vert vm1 = vv._next;

		reverseFlip(vp1, vj1);
		reverseFlip(vl1, vk);
		newReverseEdge(vm1, vk);
		newReverseEdge(vl1, vj1);
		newReverseEdge(vp1, vj);
		newReverseEdge(vl, vk1);
	}

	protected void reverseReplaceType2(Vert vv, Vert vj, Vert vk, Vert vl, Vert vj1, Vert vk1, Vert vl1) {
		Vert vp1 = vv._prev;
		Vert vm1 = vv._next;

		forwardFlip(vp1, vj1);
		forwardFlip(vl1, vk);
		newReverseEdge(vm1, vv);
		newReverseEdge(vv, vp1);
		newReverseEdge(vj1, vj);
		newReverseEdge(vk, vk1);
		newReverseEdge(vl, vl1);
	}

	protected void saveForRemoveType0(Vert vv) {
		_newMin = false;
		vv._inTour = false;
		removeType0(vv);
		forwardFindBestInsert(vv);
		reverseFindBestInsert(vv);
		replaceType0(vv);
		vv._inTour = true;

		if (_newMin) {
			_rmvState = TYPE0;
		}
	}

	protected void forwardSaveForRemoveType1(Vert vv, Vert vj, Vert vk) {
		Vert vj1 = vj._next;
		Vert vk1 = vk._next;
		_newMin = false;
		vv._inTour = false;
		forwardRemoveType1(vv, vj, vk, vj1, vk1);
		forwardFindBestInsert(vv);
		reverseFindBestInsert(vv);
		forwardReplaceType1(vv, vj, vk, vj1, vk1);
		vv._inTour = true;

		if (_newMin) {
			_rmvState = FORWARD_TYPE1;
			_rmvVj = vj;
			_rmvVk = vk;
		}
	}

	protected void forwardSaveForRemoveType2(Vert vv, Vert vj, Vert vk, Vert vl) {
		Vert vj1 = vj._prev;
		Vert vk1 = vk._next;
		Vert vl1 = vl._next;
		_newMin = false;
		vv._inTour = false;
		forwardRemoveType2(vv, vj, vk, vl, vj1, vk1, vl1);
		forwardFindBestInsert(vv);
		reverseFindBestInsert(vv);
		forwardReplaceType2(vv, vj, vk, vl, vj1, vk1, vl1);
		vv._inTour = true;

		if (_newMin) {
			_rmvState = FORWARD_TYPE2;
			_rmvVj = vj;
			_rmvVk = vk;
			_rmvVl = vl;
		}
	}

	protected void reverseSaveForRemoveType1(Vert vv, Vert vj, Vert vk) {
		Vert vj1 = vj._prev;
		Vert vk1 = vk._prev;
		_newMin = false;
		vv._inTour = false;
		reverseRemoveType1(vv, vj, vk, vj1, vk1);
		forwardFindBestInsert(vv);
		reverseFindBestInsert(vv);
		reverseReplaceType1(vv, vj, vk, vj1, vk1);
		vv._inTour = true;

		if (_newMin) {
			_rmvState = REVERSE_TYPE1;
			_rmvVj = vj;
			_rmvVk = vk;
		}
	}

	protected void reverseSaveForRemoveType2(Vert vv, Vert vj, Vert vk, Vert vl) {
		Vert vj1 = vj._next;
		Vert vk1 = vk._prev;
		Vert vl1 = vl._prev;
		_newMin = false;
		vv._inTour = false;
		reverseRemoveType2(vv, vj, vk, vl, vj1, vk1, vl1);
		forwardFindBestInsert(vv);
		reverseFindBestInsert(vv);
		reverseReplaceType2(vv, vj, vk, vl, vj1, vk1, vl1);
		vv._inTour = true;

		if (_newMin) {
			_rmvState = REVERSE_TYPE2;
			_rmvVj = vj;
			_rmvVk = vk;
			_rmvVl = vl;
		}
	}

	protected void forwardFindBestRemove(Vert vv) {
		Vert vp1 = vv._next;
		Vert vm1 = vv._prev;
		if (vp1 == null || vm1 == null)
			return;
		if (isClosed())
			forwardSequenceRemove(vm1);
		else
			forwardSequenceRemove(_head);

		saveForRemoveType0(vv);

		for (int j = 0; j < vp1._nOut; j++) {
			Vert vj = vp1._neighborsOut[j];
			if (vj._rmvSeq <= vp1._rmvSeq)
				continue;
			if (vj._next == null)
				continue;
			for (int k = 0; k < vm1._nOut; k++) {
				Vert vk = vm1._neighborsOut[k];
				if (vk._rmvSeq <= vv._rmvSeq)
					continue;
				Vert vk1 = vk._next;
				if (vk1 == null)
					continue;
				if (vj._rmvSeq > vk._rmvSeq)
					forwardSaveForRemoveType1(vv, vj, vk);
				if (vk._rmvSeq <= vj._rmvSeq + 1)
					continue;
				for (int l = 0; l < vk1._nIn; l++) {
					Vert vl = vk1._neighborsIn[l];
					if (vl._rmvSeq < vj._rmvSeq)
						continue;
					if (vl._rmvSeq >= vk._rmvSeq)
						continue;
					forwardSaveForRemoveType2(vv, vj, vk, vl);
				}

			}

		}
	}

	protected void reverseFindBestRemove(Vert vv) {
		Vert vp1 = vv._prev;
		Vert vm1 = vv._next;
		if (vp1 == null || vm1 == null)
			return;
		if (isClosed())
			reverseSequenceRemove(vm1);
		else
			reverseSequenceRemove(_tail);

		for (int j = 0; j < vp1._nIn; j++) {
			Vert vj = vp1._neighborsIn[j];
			if (vj._rmvSeq <= vp1._rmvSeq)
				continue;
			if (vj._prev == null)
				continue;
			for (int k = 0; k < vm1._nIn; k++) {
				Vert vk = vm1._neighborsIn[k];
				if (vk._rmvSeq <= vv._rmvSeq)
					continue;
				Vert vk1 = vk._prev;
				if (vk1 == null)
					continue;
				if (vj._rmvSeq > vk._rmvSeq)
					reverseSaveForRemoveType1(vv, vj, vk);
				if (vk._rmvSeq <= vj._rmvSeq + 1)
					continue;
				for (int l = 0; l < vk1._nOut; l++) {
					Vert vl = vk1._neighborsOut[l];
					if (vl._rmvSeq < vj._rmvSeq)
						continue;
					if (vl._rmvSeq >= vk._rmvSeq)
						continue;
					reverseSaveForRemoveType2(vv, vj, vk, vl);
				}

			}

		}
	}

	protected void forwardSequenceRemove(Vert head) {
		if (head == null)
			return;
		int c = 0;
		head._rmvSeq = c++;
		for (Vert z = head._next; z != null && z != head; z = z._next)
			z._rmvSeq = c++;
	}

	protected void reverseSequenceRemove(Vert tail) {
		if (tail == null)
			return;
		int c = 0;
		tail._rmvSeq = c++;
		for (Vert z = tail._prev; z != null && z != tail; z = z._prev)
			z._rmvSeq = c++;
	}

}
