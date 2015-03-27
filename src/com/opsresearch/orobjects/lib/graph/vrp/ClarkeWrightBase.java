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

package com.opsresearch.orobjects.lib.graph.vrp;

import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;
import com.opsresearch.orobjects.lib.util.QuickSort;

public class ClarkeWrightBase extends RandomizableBase implements ConstructI,
		RandomizableI {

	class CmpSave implements CompareI {
		public int compare(Object a, Object b) {
			if (((Save) a)._savings > ((Save) b)._savings)
				return LESS;
			if (((Save) a)._savings < ((Save) b)._savings)
				return GREATER;
			return EQUAL;
		}
	}

	class Save {
		private Tour _tour1;
		private Tour _tour2;
		private double _savings;
		private boolean _used = false;

		Save(Tour tour1, Tour tour2, double savings) {
			_tour1 = tour1;
			_tour2 = tour2;
			_savings = savings;
		}
	}

	class Node {
		private Node _next = null;
		private double _load = 0;
		private VertexI _vertex = null;

		Node(VertexI vertex) {
			_vertex = vertex;
			_load = getLoad(vertex);
		}
	}

	class Tour {
		private Node _last = null;
		private Node _first = null;
		private Tour _deleted = null;
		private Tour _nextTour = null;
		private int _sizeOfNodes = 0;
		private double _load = 0;
		private double _cost = 0;

		Tour(Node depot, Node node) throws SolutionNotFoundException {
			_load = node._load;
			_last = node;
			_first = node;
			_cost = _vehicleCost;
			_sizeOfNodes = 1;
			if (_closed || _out)
				_cost += getCost(depot, node);
			if (_closed || !_out)
				_cost += getCost(node, depot);
			if (_cost > _maxCost)
				throw new SolutionNotFoundException(
						"A node singularly exceeds the cost constraint:"
								+ _cost + ">" + _maxCost);
			if (_load > _maxLoad)
				throw new SolutionNotFoundException(
						"A node singularly exceeds the load constraint:"
								+ _load + ">" + _maxLoad);
		}
	}

	private int _pos;
	private int _numTours = 0;
	private Node _depot;
	private Tour _tourList;
	private Vector<Save> _savings;

	public ClarkeWrightBase() {
	}

	public ClarkeWrightBase(
			com.opsresearch.orobjects.lib.graph.tsp.ImproveI improveSubalgorithm) {
		super(improveSubalgorithm);
	}

	private double getCost(Node from, Node to) {
		return getCost(from._vertex, to._vertex);
	}

	public double constructOutboundTours(Object depotKey)
			throws SolutionNotFoundException, VertexNotFoundException {
		_closed = false;
		_out = true;
		_depotKey = depotKey;
		construct();
		return getCost();
	}

	public double constructInboundTours(Object depotKey)
			throws SolutionNotFoundException, VertexNotFoundException {
		_closed = false;
		_out = false;
		_depotKey = depotKey;
		construct();
		return getCost();
	}

	public double constructClosedTours(Object depotKey)
			throws SolutionNotFoundException, VertexNotFoundException {
		_closed = true;
		_depotKey = depotKey;
		construct();
		return getCost();
	}

	private void construct() throws SolutionNotFoundException,
			VertexNotFoundException {
		if (_graph == null)
			throw new SolutionNotFoundException("The graph is not set");
		VertexI depotVertex = _graph.getVertex(_depotKey);
		if (depotVertex == null)
			throw new VertexNotFoundException("Can't find the Depot: "
					+ _depotKey);
		_depot = new Node(depotVertex);
		initTours();
		initSavings();
		mergeTours();
		_savings = null;
	}

	private int nextPos(int pos) {
		while (pos < _savings.size() && _savings.elementAt(pos)._used)
			pos++;
		return pos;
	}

	private Save nextSave() {
		int skip = _strength == 0 ? 0
				: (int) (_random.nextDouble() * _strength + 0.5);
		_pos = nextPos(_pos);
		if (_pos >= _savings.size())
			return null;
		int pos = _pos;
		for (int i = 0; i < skip; i++) {
			int p = nextPos(pos + 1);
			if (p >= _savings.size())
				break;
			pos = p;
		}
		Save save = _savings.elementAt(pos);
		save._used = true;
		return save;
	}

	private void improve(Tour tour) throws SolutionNotFoundException {
		double origCost = tour._cost;
		Vector<ElementI> verts = new Vector<ElementI>(tour._sizeOfNodes + 2);
		if (_closed || _out) {
			verts.addElement(_depot._vertex);
			verts.addElement(null);
		}
		Node node = tour._first;
		verts.addElement(node._vertex);
		while ((node = node._next) != null) {
			verts.addElement(null);
			verts.addElement(node._vertex);
		}
		double cost;
		if (_closed || !_out) {
			verts.addElement(null);
			verts.addElement(_depot._vertex);
		}
		try {
			if (_closed)
				cost = _improve.improveClosedTour(verts);
			else if (_out)
				cost = _improve.improveOpenTour(verts, true, false);
			else
				cost = _improve.improveOpenTour(verts, false, true);
		} catch (com.opsresearch.orobjects.lib.graph.tsp.TourNotFoundException e) {
			throw new SolutionNotFoundException("Subalg: " + e.getMessage());
		}

		tour._cost = _vehicleCost + cost;
		double delta = origCost - tour._cost;
		if (delta < -0.000001)
			throw new SolutionNotFoundException(
					"The subalgorithm increased the tour cost by: " + (-delta));
		Iterator<ElementI> e = _improve.getTour().iterator();
		if (_closed || _out) {
			e.next();
			e.next();
		}
		node = tour._first;
		for (node = tour._first; node != tour._last; node = node._next) {
			node._vertex = (VertexI) e.next();
			e.next();
		}
		tour._last._vertex = (VertexI) e.next();
	}

	private void mergeTours() throws SolutionNotFoundException {
		_pos = 0;
		Save save;
		while ((save = nextSave()) != null) {
			boolean tryMerge = true;
			boolean alreadyImproved = false;
			while (tryMerge) {
				tryMerge = false;
				Tour tour1 = save._tour1;
				Tour tour2 = save._tour2;
				while (tour1._deleted != null)
					tour1 = tour1._deleted;
				while (tour2._deleted != null)
					tour2 = tour2._deleted;
				if (tour1 == tour2)
					continue;
				if (tour1._load + tour2._load > _maxLoad)
					continue;
				double cd1 = 0, cd2 = 0, c1d = 0, c2d = 0;
				double c12 = getCost(tour1._last, tour2._first);
				double c21 = getCost(tour2._last, tour1._first);
				if (_closed || _out) {
					cd1 = getCost(_depot, tour1._first);
					cd2 = getCost(_depot, tour2._first);
				}
				if (_closed || !_out) {
					c1d = getCost(tour1._last, _depot);
					c2d = getCost(tour2._last, _depot);
				}
				if ((cd1 + c12 + c2d) > (cd2 + c21 + c1d)) {
					Tour tmp = tour1;
					tour1 = tour2;
					tour2 = tmp;
				}

				double savings = _vehicleCost
						- getCost(tour1._last, tour2._first);
				if (_closed || _out)
					savings += getCost(_depot, tour2._first);
				if (_closed || !_out)
					savings += getCost(tour1._last, _depot);

				double cost = tour1._cost + tour2._cost - savings;
				if (savings < 0 || cost > _maxCost) {
					if (_improve != null && !alreadyImproved) {
						alreadyImproved = tryMerge = true;
						improve(tour1);
						improve(tour2);
					}
				} else {
					tour2._deleted = tour1;
					tour1._last._next = tour2._first;
					tour1._last = tour2._last;
					tour1._load += tour2._load;
					tour1._cost = cost;
					tour1._sizeOfNodes += tour2._sizeOfNodes;
					_numTours--;
				}
			}
		}
	}

	private void initSavings() {
		_savings = new Vector<Save>(_numTours * _numTours);
		for (Tour tour1 = _tourList; tour1 != null; tour1 = tour1._nextTour) {
			for (Tour tour2 = _tourList; tour2 != null; tour2 = tour2._nextTour) {
				if (tour1 == tour2)
					continue;

				double savings = _vehicleCost
						- getCost(tour1._last, tour2._first);
				if (_closed)
					savings += getCost(_depot, tour2._first)
							+ getCost(tour1._last, _depot);
				else if (_out)
					savings += getCost(_depot, tour2._first);
				else
					savings += getCost(tour1._last, _depot);

				_savings.addElement(new Save(tour1, tour2, savings));
			}
		}
		new QuickSort<Save>(new CmpSave()).sort(_savings);
	}

	private void initTours() throws SolutionNotFoundException {
		_numTours = 0;
		_tourList = null;
		for (Iterator<VertexI> verts = _graph.vertices(); verts
				.hasNext();) {
			VertexI vert = verts.next();
			if (vert == _depot._vertex)
				continue;
			if (!isSelected(vert))
				continue;
			Tour tour = new Tour(_depot, new Node(vert));
			tour._nextTour = _tourList;
			_tourList = tour;
			_numTours++;
		}
	}

	public double getCost() throws SolutionNotFoundException {
		if (_numTours == 0)
			throw new SolutionNotFoundException("No solution has been created");
		double cost = 0;
		for (Tour tour = _tourList; tour != null; tour = tour._nextTour) {
			if (tour._deleted == null)
				cost += tour._cost;
		}
		return cost;
	}

	public double[] getCosts() throws SolutionNotFoundException {
		if (_numTours == 0)
			throw new SolutionNotFoundException("No solution has been created");
		double[] costs = new double[_numTours];
		int cnt = 0;
		for (Tour tour = _tourList; tour != null; tour = tour._nextTour) {
			if (tour._deleted == null)
				costs[cnt++] = tour._cost;
		}
		return costs;
	}

	public double[] getLoads() throws SolutionNotFoundException {
		if (_numTours == 0)
			throw new SolutionNotFoundException("No solution has been created");
		double[] loads = new double[_numTours];
		int cnt = 0;
		for (Tour tour = _tourList; tour != null; tour = tour._nextTour) {
			if (tour._deleted == null)
				loads[cnt++] = tour._load;
		}
		return loads;
	}

	@SuppressWarnings("unchecked")
	public Vector<ElementI>[] getTours() throws SolutionNotFoundException {
		if (_numTours == 0)
			throw new SolutionNotFoundException("No solution has been created");
		int cnt = 0;
		Vector<ElementI>[] tours = new Vector[_numTours];
		for (Tour tour = _tourList; tour != null; tour = tour._nextTour) {
			if (tour._deleted != null)
				continue;
			Vector<ElementI> vect = tours[cnt++] = new Vector<ElementI>();
			if (_closed || _out) {
				vect.addElement(_depot._vertex);
				vect.addElement(_graph.getEdge(_depot._vertex,
						tour._first._vertex, _edgeKey));
			}
			Node lastNode = tour._first;
			vect.addElement(lastNode._vertex);
			for (Node node = lastNode._next; node != null; lastNode = node, node = node._next) {
				vect.addElement(_graph.getEdge(lastNode._vertex, node._vertex,
						_edgeKey));
				vect.addElement(node._vertex);
			}
			if (_closed || !_out) {
				vect.addElement(_graph.getEdge(tour._last._vertex,
						_depot._vertex, _edgeKey));
				vect.addElement(_depot._vertex);
			}
		}
		return tours;
	}

}
