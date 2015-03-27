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
import com.opsresearch.orobjects.lib.real.Constants;
import com.opsresearch.orobjects.lib.util.QuickSort;

public class GillettMillerBase extends RandomizableBase implements ConstructI,
		RandomizableI {

	class CmpDir implements CompareI {
		public int compare(Object a, Object b) {
			if (((Node) a)._dir < ((Node) b)._dir)
				return LESS;
			if (((Node) a)._dir > ((Node) b)._dir)
				return GREATER;
			return EQUAL;
		}
	}

	class Node {
		private Node _next = null;
		private double _load = 0;
		private boolean _used = false;
		private VertexI _vertex = null;
		private double _dir;

		Node(VertexI vertex, double direction) {
			_dir = direction;
			_vertex = vertex;
			_load = getLoad(vertex);
		}
	}

	class Tour {
		private Node _last = null;
		private Node _first = null;
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
	private Node _depot, _insertion;
	private Tour _tourList;
	private Vector<Node> _nodes;

	public GillettMillerBase() {
	}

	public GillettMillerBase(
			com.opsresearch.orobjects.lib.graph.tsp.ImproveI improveSubalgorithm) {
		super(improveSubalgorithm);
	}

	private double getLoad(Node node) {
		return getLoad(node._vertex);
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
		_depot = new Node(depotVertex, 0);
		initNodes();
		buildTours();
		_nodes = null;
	}

	private double findInsertion(Tour tour, Node node) {
		double cost, bestCost;
		cost = getCost(node, tour._first);
		if (_closed || _out)
			cost += getCost(_depot, node) - getCost(_depot, tour._first);
		_insertion = _depot;
		bestCost = cost;

		cost = getCost(tour._last, node);
		if (_closed || !_out)
			cost += getCost(node, _depot) - getCost(tour._last, _depot);
		if (cost < bestCost) {
			_insertion = tour._last;
			bestCost = cost;
		}

		for (Node n = tour._first; n != tour._last; n = n._next) {
			cost = getCost(n, node) + getCost(node, n._next)
					- getCost(n, n._next);
			if (cost < bestCost) {
				_insertion = n;
				bestCost = cost;
			}
		}

		return bestCost;
	}

	private void insert(Tour tour, Node node) {
		if (_insertion == _depot) {
			node._next = tour._first;
			tour._first = node;
		} else if (_insertion == tour._last) {
			tour._last._next = node;
			tour._last = node;
		} else {
			node._next = _insertion._next;
			_insertion._next = node;
		}
	}

	private int nextPos(int pos) {
		while (pos < _nodes.size() && _nodes.elementAt(pos)._used)
			pos++;
		return pos;
	}

	private Node nextNode() {
		int skip = _strength < 2 ? 0
				: (int) (_random.nextDouble() * _strength - 0.5);
		_pos = nextPos(_pos);
		if (_pos >= _nodes.size())
			return null;
		int pos = _pos;
		for (int i = 0; i < skip; i++) {
			int p = nextPos(pos + 1);
			if (p >= _nodes.size())
				break;
			pos = p;
		}
		Node node = _nodes.elementAt(pos);
		node._used = true;
		return node;
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

	private void buildTours() throws SolutionNotFoundException {
		_pos = 0;
		_numTours = 0;
		_tourList = null;
		Node node = nextNode();
		if (node == null)
			throw new SolutionNotFoundException("There are no nodes.");
		Tour tour = _tourList = new Tour(_depot, node);
		_numTours++;
		while ((node = nextNode()) != null) {
			boolean tryAdd = true;
			boolean alreadyImproved = false;
			while (tryAdd) {
				tryAdd = false;
				double cost = tour._cost + findInsertion(tour, node);
				double load = tour._load + getLoad(node);
				if (cost > _maxCost && _improve != null && !alreadyImproved) {
					alreadyImproved = tryAdd = true;
					improve(tour);
					continue;
				}
				if (load > _maxLoad || cost > _maxCost) {
					tour = new Tour(_depot, node);
					tour._nextTour = _tourList;
					_tourList = tour;
					_numTours++;
				} else {
					insert(tour, node);
					tour._cost = cost;
					tour._load = load;
					tour._sizeOfNodes++;
				}
			}
		}
	}

	private void initNodes() throws SolutionNotFoundException {
		_nodes = new Vector<Node>();
		if (!(_depot._vertex.getValue() instanceof com.opsresearch.orobjects.lib.geom.PointI))
			throw new SolutionNotFoundException(
					"Found a vertex value that does not implement 'geom.PointI'");
		com.opsresearch.orobjects.lib.geom.PointI depotPt = (com.opsresearch.orobjects.lib.geom.PointI) _depot._vertex
				.getValue();
		for (Iterator<VertexI> verts = _graph.vertices(); verts
				.hasNext();) {
			VertexI vert = verts.next();
			if (vert == _depot._vertex)
				continue;
			if (!isSelected(vert))
				continue;
			if (!(vert.getValue() instanceof com.opsresearch.orobjects.lib.geom.PointI))
				throw new SolutionNotFoundException(
						"Found a vertex value that does not implement 'geom.PointI'");
			com.opsresearch.orobjects.lib.geom.PointI pt = (com.opsresearch.orobjects.lib.geom.PointI) vert
					.getValue();
			double dir = depotPt.getDirectionTo(pt);
			Node node = new Node(vert, dir);
			_nodes.addElement(node);
		}
		if (_strength > 0) {
			double pi2 = Constants.pi * 2.0;
			double d = _random.nextDouble() * pi2 - Constants.pi;
			for (Iterator<Node> e = _nodes.iterator(); e.hasNext();) {
				Node node = e.next();
				if (node._dir < d)
					node._dir += pi2;
			}
		}
		if (_strength > 0 && _random.nextDouble() > 0.5)
			new QuickSort<Node>(true, new CmpDir()).sort(_nodes);
		else
			new QuickSort<Node>(false, new CmpDir()).sort(_nodes);
	}

	public double getCost() throws SolutionNotFoundException {
		if (_numTours == 0)
			throw new SolutionNotFoundException("No solution has been created");
		double cost = 0;
		for (Tour tour = _tourList; tour != null; tour = tour._nextTour) {
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
