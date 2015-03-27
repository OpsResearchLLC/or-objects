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


public class Geni extends TSPBase implements ConstructI {
	protected class Vert {
		protected int _idx, _nIn, _nOut, _insSeq, _rmvSeq;
		protected Vert _prev, _next, _reverse;
		protected boolean _inTour;
		protected double _prevCost, _nextCost;
		protected double _prevSumCost, _nextSumCost;
		protected double[] _neighborCostIn;
		protected double[] _neighborCostOut;
		protected Vert[] _neighborsIn;
		protected Vert[] _neighborsOut;

		Vert(int idx) {
			_idx = idx;
			_prev = _next = null;
			_nIn = _nOut = 0;
			_inTour = false;
			_neighborsIn = new Vert[_neighborhood + 2];
			_neighborsOut = new Vert[_neighborhood + 2];
			_neighborCostIn = new double[_neighborhood + 2];
			_neighborCostOut = new double[_neighborhood + 2];
		}

		Vert(Vert v) {
			_idx = v._idx;
			_prev = _next = null;
			_nIn = v._nIn;
			_nOut = v._nOut;
			_inTour = v._inTour;
			_neighborsIn = v._neighborsIn;
			_neighborsOut = v._neighborsOut;
			_neighborCostIn = v._neighborCostIn;
			_neighborCostOut = v._neighborCostOut;
		}

		void setReverse(Vert v) {
			_prev = v._next;
			_next = v._prev;
			_prevCost = v._nextCost;
			_nextCost = v._prevCost;
			_prevSumCost = v._nextSumCost;
			_nextSumCost = v._prevSumCost;
		}

		public void addNeighbor(Vert vert) {
			if (vert._idx == _idx)
				return;
			addNeighborIn(vert);
			addNeighborOut(vert);
		}

		public void addNeighborIn(Vert vert) {
			double c = forwardCost(vert._idx, _idx);
			if (c == Double.POSITIVE_INFINITY)
				return;
			if (_nIn < _p) {
				_neighborsIn[_nIn] = vert;
				_neighborCostIn[_nIn++] = c;
			} else {
				int idx = 0;
				double max = _neighborCostIn[0];
				for (int i = 1; i < _nIn; i++) {
					double d;
					if ((d = _neighborCostIn[i]) > max) {
						idx = i;
						max = d;
					}
				}
				if (max > c) {
					_neighborsIn[idx] = vert;
					_neighborCostIn[idx] = c;
				}
			}
		}

		public void addNeighborOut(Vert vert) {
			double c = forwardCost(_idx, vert._idx);
			if (c == Double.POSITIVE_INFINITY)
				return;
			if (_nOut < _p) {
				_neighborsOut[_nOut] = vert;
				_neighborCostOut[_nOut++] = c;
			} else {
				int idx = 0;
				double max = _neighborCostOut[0];
				for (int i = 1; i < _nOut; i++) {
					double d;
					if ((d = _neighborCostOut[i]) > max) {
						idx = i;
						max = d;
					}
				}
				if (max > c) {
					_neighborsOut[idx] = vert;
					_neighborCostOut[idx] = c;
				}
			}
		}

		public String toString() {
			return "" + _idx + ", " + _prevCost + ", " + _nextCost;
		}

	}

	static final byte INIT = 0;
	static final byte TYPE0 = 1;
	static final byte FORWARD_TYPE1 = 2;
	static final byte FORWARD_TYPE2 = 3;
	static final byte REVERSE_TYPE1 = 4;
	static final byte REVERSE_TYPE2 = 5;

	protected int _p;
	protected int _neighborhood;
	protected Vert _insVi, _insVj, _insVk, _insVl;
	protected Vert _rmvVj, _rmvVk, _rmvVl;
	protected Vert _head;
	protected Vert _tail;
	protected Vert _virtualHead;
	protected Vert _virtualTail;
	protected double _insMin;
	protected byte _insState, _rmvState;
	protected boolean _lower, _newMin;
	protected Vert[] _verts;

	public Geni(int sizeOfNeighborhood) {
		_neighborhood = sizeOfNeighborhood;
		_head = _tail = null;
		setBestTour(null);
	}


	public double constructOpenTour(Object originKey, Object destinationKey) throws TourNotFoundException,
			VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		if (originKey.equals(destinationKey))
			throw new TSPError("The origin and destination keys are equal, use 'constructClosedTour' instead.");
		VertexI orig = getGraph().getVertex(originKey);
		if (orig == null)
			throw new VertexNotFoundException("The origin vertex does not exist");
		VertexI dest = getGraph().getVertex(destinationKey);
		if (dest == null)
			throw new VertexNotFoundException("The destination vertex does not exist");
		setClosed((orig == dest));
		_p = _neighborhood;
		initVertices(orig.getIndex(), dest.getIndex());
		initTourGENI(getFromSelectedIdx(), getToSelectedIdx());
		construct();
		return getCost();
	}

	public double constructOpenTourFrom(Object originKey) throws TourNotFoundException, VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		VertexI orig = getGraph().getVertex(originKey);
		if (orig == null)
			throw new VertexNotFoundException("The origin vertex does not exist");
		setClosed(false);
		_p = _neighborhood + 1;
		initVertices(orig.getIndex(), -1);
		initTourGENI(getFromSelectedIdx(), -1);
		construct();
		return getCost();
	}

	public double constructOpenTourTo(Object destinationKey) throws TourNotFoundException, VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		VertexI dest = getGraph().getVertex(destinationKey);
		if (dest == null)
			throw new VertexNotFoundException("The destination vertex does not exist");
		setClosed(false);
		_p = _neighborhood + 1;
		initVertices(-1, dest.getIndex());
		initTourGENI(-1, getToSelectedIdx());
		construct();
		return getCost();
	}

	public double constructOpenTour() throws TourNotFoundException {
		setClosed(false);
		_p = _neighborhood + 2;
		initVertices(-1, -1);
		initTourGENI(-1, -1);
		construct();
		return getCost();
	}

	public double constructClosedTour() throws TourNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		_p = _neighborhood;
		setClosed(true);
		initVertices(-1, -1);
		initTourGENI(0, 0);
		construct();
		return getCost();
	}

	private void initTourGENI(int head, int tail) {
		if (getGraph() == null)
			throw new TSPError("The graph has not been set.");
		int n = getSize();
		_verts = new Vert[n];
		for (int i = 0; i < n; i++)
			_verts[i] = new Vert(i);
		_virtualHead = head == -1 ? new Vert(-1) : null;
		_virtualTail = tail == -1 ? new Vert(-1) : null;

		int minV1 = 0;
		int minV2 = 0;
		double min = Double.POSITIVE_INFINITY;
		out: {
			for (int v1 = 0; v1 < n; v1++) {
				if (v1 == head || v1 == tail)
					continue;
				for (int v2 = 0; v2 < n; v2++) {
					if (v1 == v2 || v2 == head || v2 == tail)
						continue;
					double c1 = forwardCost(head, v1);
					if (c1 == Double.POSITIVE_INFINITY)
						continue;
					double c2 = forwardCost(v1, v2);
					if (c2 == Double.POSITIVE_INFINITY)
						continue;
					double c3 = forwardCost(v2, tail);
					if (c3 == Double.POSITIVE_INFINITY)
						continue;
					double cost = c1 + c2 + c3;
					if (cost < min) {
						min = cost;
						minV1 = v1;
						minV2 = v2;
						if (n > 4)
							break out;
					}
				}
			}
		}

		if (min == Double.POSITIVE_INFINITY)
			throw new TSPError("Can't find an initial solution");

		_head = head == -1 ? _virtualHead : _verts[head];
		_tail = isClosed() ? _head : tail == -1 ? _virtualTail : _verts[tail];
		Vert vert1 = _verts[minV1];
		Vert vert2 = _verts[minV2];

		_head._inTour = true;
		_tail._inTour = true;
		vert1._inTour = true;
		vert2._inTour = true;

		addNeighbor(_head);
		if (!isClosed())
			addNeighbor(_tail);
		addNeighbor(vert1);
		addNeighbor(vert2);

		newForwardEdge(_head, vert1);
		newForwardEdge(vert1, vert2);
		newForwardEdge(vert2, _tail);
	}

	private void construct() throws TourNotFoundException {
		int n = getSize();
		for (int v = 0; v < n; v++) {
			Vert vv = _verts[v];
			if (vv._inTour)
				continue;
			_insState = INIT;
			forwardFindBestInsert(vv);
			reverseFindBestInsert(vv);
			addToTour(vv);
			addNeighbor(vv);
		}
		setBestTour(calcTour());
		setBestCost(calcCost());
		_head = _tail = null;
	}

	protected double calcCost() {
		if (_head == null || _tail == null)
			throw new TSPError("No solution has been found");
		double cost = _head._nextCost;
		for (Vert v = _head._next; v != _tail; v = v._next)
			cost += v._nextCost;
		return cost;
	}

	protected int[] calcTour() {
		if (_head == null || _tail == null)
			throw new TSPError("No solution has been found");
		int cnt = 0;
		int[] tour = new int[getSize()];
		if (_head._idx != -1)
			tour[cnt++] = _head._idx;
		for (Vert v = _head._next; v != _tail; v = v._next) {
			if (v._idx != -1)
				tour[cnt++] = v._idx;
		}
		if (!isClosed() && _tail._idx != -1)
			tour[cnt++] = _tail._idx;
		return tour;
	}

	protected boolean checkList() {
		int c = 0;
		boolean[] flg = new boolean[getSize()];
		if (_head._idx != -1)
			flg[_head._idx] = true;
		if (_tail._idx != _head._idx && _tail._idx != -1)
			flg[_tail._idx] = true;
		for (Vert v = _head._next; v != _tail; v = v._next) {
			if (flg[v._idx])
				return false;
			flg[v._idx] = true;
			c++;
			if (c > getSize())
				return false;
			if (forwardCost(v._prev._idx, v._idx) != v._prev._nextCost)
				return false;
			if (reverseCost(v._prev._idx, v._idx) != v._prevCost)
				return false;
		}
		c = 0;
		for (Vert v = _tail._prev; v != _head; v = v._prev) {
			c++;
			if (c > getSize())
				return false;
		}
		return true;
	}

	protected void forwardFlip(Vert beg, Vert end) {
		if (beg == end)
			return;
		Vert v = beg;
		Vert n = v._next;
		Vert nn = n._next;
		double d = v._nextCost;
		while (v != end) {
			v._prev = n;
			v._prevCost = d;
			n._next = v;
			d = n._nextCost;
			n._nextCost = n._prevCost;
			v = n;
			n = nn;
			nn = n._next;
		}
	}

	protected void reverseFlip(Vert end, Vert beg) {
		if (beg == end)
			return;
		Vert v = beg;
		Vert n = v._next;
		Vert nn = n._next;
		double d = v._nextCost;
		while (v != end) {
			v._prev = n;
			v._prevCost = d;
			n._next = v;
			d = n._nextCost;
			n._nextCost = n._prevCost;
			v = n;
			n = nn;
			nn = n._next;
		}
	}

	protected void costOfType0(int v, Vert vi) {
		Vert vi1 = vi._next;
		double cost = forwardCost(vi._idx, v) + forwardCost(v, vi1._idx) - vi._nextCost;
		if (_insState == INIT || cost < _insMin) {
			_insMin = cost;
			_insState = TYPE0;
			_insVi = vi;
			_newMin = true;
		}
	}

	protected void forwardCostOfInsertType1(int v, Vert vi, Vert vj, Vert vk) {
		Vert vi1 = vi._next;
		Vert vj1 = vj._next;
		Vert vk1 = vk._next;

		double cost = 0;
		if (!isSymmetric()) {
			cost -= (vj._nextSumCost - vi1._nextSumCost);
			cost -= (vk._nextSumCost - vj1._nextSumCost);
			cost += vj._prevSumCost - vi1._prevSumCost;
			cost += vk._prevSumCost - vj1._prevSumCost;
		}
		cost -= (vi._nextCost + vj._nextCost + vk._nextCost);
		cost += forwardCost(vi._idx, v);
		cost += forwardCost(v, vj._idx);
		cost += forwardCost(vi1._idx, vk._idx);
		cost += forwardCost(vj1._idx, vk1._idx);

		if (_insState == INIT || cost < _insMin) {
			_insMin = cost;
			_insState = FORWARD_TYPE1;
			_insVi = vi;
			_insVj = vj;
			_insVk = vk;
			_newMin = true;
		}
	}

	protected void forwardCostOfInsertType2(int v, Vert vi, Vert vj, Vert vk, Vert vl) {
		Vert vi1 = vi._next;
		Vert vj1 = vj._next;
		Vert vk1 = vk._prev;
		Vert vl1 = vl._prev;

		double cost = 0.0;
		if (!isSymmetric()) {
			cost -= (vl1._nextSumCost - vi1._nextSumCost);
			cost -= (vj._nextSumCost - vl._nextSumCost);
			cost += vl1._prevSumCost - vi1._prevSumCost;
			cost += vj._prevSumCost - vl._prevSumCost;
		}
		cost -= (vi._nextCost + vj._nextCost + vk1._nextCost + vl1._nextCost);
		cost += forwardCost(vi._idx, v);
		cost += forwardCost(v, vj._idx);
		cost += forwardCost(vl._idx, vj1._idx);
		cost += forwardCost(vk1._idx, vl1._idx);
		cost += forwardCost(vj1._idx, vk._idx);

		if (_insState == INIT || cost < _insMin) {
			_insMin = cost;
			_insState = FORWARD_TYPE2;
			_insVi = vi;
			_insVj = vj;
			_insVk = vk;
			_insVl = vl;
			_newMin = true;
		}
	}

	protected void reverseCostOfInsertType1(int v, Vert vi, Vert vj, Vert vk) {
		Vert vi1 = vi._prev;
		Vert vj1 = vj._prev;
		Vert vk1 = vk._prev;

		double cost = 0;
		if (!isSymmetric()) {
			cost -= (vj._nextSumCost - vi1._nextSumCost);
			cost -= (vk._nextSumCost - vj1._nextSumCost);
			cost += (vj._prevSumCost - vi1._prevSumCost);
			cost += (vk._prevSumCost - vj1._prevSumCost);
		}
		cost -= (vi._prev._nextCost + vj._prev._nextCost + vk._prev._nextCost);
		cost += reverseCost(vi._idx, v);
		cost += reverseCost(v, vj._idx);
		cost += reverseCost(vi1._idx, vk._idx);
		cost += reverseCost(vj1._idx, vk1._idx);

		if (_insState == INIT || cost < _insMin) {
			_insMin = cost;
			_insState = REVERSE_TYPE1;
			_insVi = vi;
			_insVj = vj;
			_insVk = vk;
			_newMin = true;
		}
	}

	protected void reverseCostOfInsertType2(int v, Vert vi, Vert vj, Vert vk, Vert vl) {
		Vert vi1 = vi._prev;
		Vert vj1 = vj._prev;
		Vert vk1 = vk._next;
		Vert vl1 = vl._next;

		double cost = 0.0;
		if (!isSymmetric()) {
			cost -= (vl1._nextSumCost - vi1._nextSumCost);
			cost -= (vj._nextSumCost - vl._nextSumCost);
			cost += (vl1._prevSumCost - vi1._prevSumCost);
			cost += (vj._prevSumCost - vl._prevSumCost);
		}
		cost -= (vi1._nextCost + vj1._nextCost + vk._nextCost + vl._nextCost);
		cost += reverseCost(vi._idx, v);
		cost += reverseCost(v, vj._idx);
		cost += reverseCost(vl._idx, vj1._idx);
		cost += reverseCost(vk1._idx, vl1._idx);
		cost += reverseCost(vj1._idx, vk._idx);

		if (_insState == INIT || cost < _insMin) {
			_insMin = cost;
			_insState = REVERSE_TYPE2;
			_insVi = vi;
			_insVj = vj;
			_insVk = vk;
			_insVl = vl;
			_newMin = true;
		}
	}

	protected void forwardFindBestInsert(Vert vv) {
		if (!isClosed())
			forwardSequenceInsert(_head);
		for (int i = 0; i < vv._nIn; i++) {
			Vert vi = vv._neighborsIn[i];
			if (!vi._inTour)
				continue;
			if (isClosed())
				forwardSequenceInsert(vi);
			Vert vi1 = vi._next;
			if (vi1 == null)
				continue;
			costOfType0(vv._idx, vi);
			for (int j = 0; j < vv._nOut; j++) {
				Vert vj = vv._neighborsOut[j];
				if (!vj._inTour)
					continue;
				if (vj._insSeq <= vi._insSeq)
					continue;
				Vert vj1 = vj._next;
				if (vj1 == null)
					continue;
				for (int k = 0; k < vi1._nOut; k++) {
					Vert vk = vi1._neighborsOut[k];
					if (!vk._inTour)
						continue;
					if (vk._insSeq <= vj._insSeq)
						continue;
					if (vk._next != null)
						forwardCostOfInsertType1(vv._idx, vi, vj, vk);
					if (vk._insSeq <= vj1._insSeq)
						continue;
					for (int l = 0; l < vj1._nIn; l++) {
						Vert vl = vj1._neighborsIn[l];
						if (!vl._inTour)
							continue;
						if (vl._insSeq <= vi1._insSeq || vl._insSeq > vj._insSeq)
							continue;
						forwardCostOfInsertType2(vv._idx, vi, vj, vk, vl);
					}
				}

			}
		}
	}

	protected void reverseFindBestInsert(Vert vv) {
		if (!isClosed())
			reverseSequenceInsert(_tail);
		for (int i = 0; i < vv._nOut; i++) {
			Vert vi = vv._neighborsOut[i];
			if (!vi._inTour)
				continue;
			if (isClosed())
				reverseSequenceInsert(vi);
			Vert vi1 = vi._prev;
			if (vi1 == null)
				continue;
			for (int j = 0; j < vv._nIn; j++) {
				Vert vj = vv._neighborsIn[j];
				if (!vj._inTour)
					continue;
				if (vj._insSeq <= vi._insSeq)
					continue;
				Vert vj1 = vj._prev;
				if (vj1 == null)
					continue;
				for (int k = 0; k < vi1._nIn; k++) {
					Vert vk = vi1._neighborsIn[k];
					if (!vk._inTour)
						continue;
					if (vk._insSeq <= vj._insSeq)
						continue;
					if (vk._prev != null)
						reverseCostOfInsertType1(vv._idx, vi, vj, vk);
					if (vk._insSeq <= vj1._insSeq)
						continue;
					for (int l = 0; l < vj1._nOut; l++) {
						Vert vl = vj1._neighborsOut[l];
						if (!vl._inTour)
							continue;
						if (vl._insSeq <= vi1._insSeq || vl._insSeq > vj._insSeq)
							continue;
						reverseCostOfInsertType2(vv._idx, vi, vj, vk, vl);
					}
				}

			}
		}
	}

	protected void newForwardEdge(Vert from, Vert to) {
		from._next = to;
		from._nextCost = forwardCost(from._idx, to._idx);
		to._prev = from;
		to._prevCost = forwardCost(to._idx, from._idx);
	}

	protected void newReverseEdge(Vert to, Vert from) {
		from._next = to;
		from._nextCost = forwardCost(from._idx, to._idx);
		to._prev = from;
		to._prevCost = forwardCost(to._idx, from._idx);
	}

	protected void applyType0(Vert v) {
		Vert vi1 = _insVi._next;
		newForwardEdge(_insVi, v);
		newForwardEdge(v, vi1);
	}

	protected void applyForwardType1(Vert v) {
		Vert vi1 = _insVi._next;
		Vert vj1 = _insVj._next;
		Vert vk1 = _insVk._next;

		forwardFlip(vi1, _insVj);
		forwardFlip(vj1, _insVk);
		newForwardEdge(_insVi, v);
		newForwardEdge(v, _insVj);
		newForwardEdge(vi1, _insVk);
		newForwardEdge(vj1, vk1);
	}

	protected void applyForwardType2(Vert v) {
		Vert vi1 = _insVi._next;
		Vert vj1 = _insVj._next;
		Vert vk1 = _insVk._prev;
		Vert vl1 = _insVl._prev;

		forwardFlip(vi1, vl1);
		forwardFlip(_insVl, _insVj);
		newForwardEdge(_insVi, v);
		newForwardEdge(v, _insVj);
		newForwardEdge(_insVl, vj1);
		newForwardEdge(vk1, vl1);
		newForwardEdge(vi1, _insVk);
	}

	protected void applyReverseType1(Vert v) {
		Vert vi1 = _insVi._prev;
		Vert vj1 = _insVj._prev;
		Vert vk1 = _insVk._prev;

		reverseFlip(vi1, _insVj);
		reverseFlip(vj1, _insVk);
		newReverseEdge(_insVi, v);
		newReverseEdge(v, _insVj);
		newReverseEdge(vi1, _insVk);
		newReverseEdge(vj1, vk1);
	}

	protected void applyReverseType2(Vert v) {
		Vert vi1 = _insVi._prev;
		Vert vj1 = _insVj._prev;
		Vert vk1 = _insVk._next;
		Vert vl1 = _insVl._next;

		reverseFlip(vi1, vl1);
		reverseFlip(_insVl, _insVj);
		newReverseEdge(_insVi, v);
		newReverseEdge(v, _insVj);
		newReverseEdge(_insVl, vj1);
		newReverseEdge(vk1, vl1);
		newReverseEdge(vi1, _insVk);
	}

	protected void addNeighbor(Vert vv) {
		if (_virtualHead != null)
			_virtualHead.addNeighbor(vv);
		if (_virtualTail != null)
			_virtualTail.addNeighbor(vv);
		for (int i = 0; i < _verts.length; i++)
			_verts[i].addNeighbor(vv);
	}

	protected void addToTour(Vert vv) throws TourNotFoundException {
		switch (_insState) {
		case TYPE0:
			applyType0(vv);
			break;
		case FORWARD_TYPE1:
			applyForwardType1(vv);
			break;
		case FORWARD_TYPE2:
			applyForwardType2(vv);
			break;
		case REVERSE_TYPE1:
			applyReverseType1(vv);
			break;
		case REVERSE_TYPE2:
			applyReverseType2(vv);
			break;
		default:
			throw new TourNotFoundException();
		}
		addNeighbor(vv);
		vv._inTour = true;
	}

	protected void forwardSequenceInsert(Vert head) {
		if (head == null)
			return;
		int c = 0;
		double nextSumCost = 0.0;
		double prevSumCost = 0.0;
		head._insSeq = c++;
		head._nextSumCost = nextSumCost;
		head._prevSumCost = prevSumCost;
		nextSumCost += head._nextCost;
		for (Vert z = head._next; z != null && z != head; z = z._next) {
			z._insSeq = c++;
			prevSumCost += z._prevCost;
			z._prevSumCost = prevSumCost;
			z._nextSumCost = nextSumCost;
			nextSumCost += z._nextCost;
		}
	}

	protected void reverseSequenceInsert(Vert tail) {
		if (tail == null)
			return;
		int c = 0;
		double nextSumCost = 0.0;
		double prevSumCost = 0.0;
		tail._insSeq = c++;
		tail._nextSumCost = nextSumCost;
		tail._prevSumCost = prevSumCost;
		prevSumCost += tail._prevCost;
		for (Vert z = tail._prev; z != null && z != tail; z = z._prev) {
			z._insSeq = c++;
			nextSumCost += z._nextCost;
			z._nextSumCost = nextSumCost;
			z._prevSumCost = prevSumCost;
			prevSumCost += z._prevCost;
		}
	}

	public String toString() {
		String str = _head.toString() + "\n";
		for (Vert z = _head._next; z != null && z != _head; z = z._next)
			str += z.toString() + "\n";
		return str;
	}

}
