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

import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.graph.EdgeI;
import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.GraphError;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.PropertiesAdapter;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;
import com.opsresearch.orobjects.lib.util.Array;

public abstract class TSPBase {

	private int _size;
	private int _changeCount;
	private VertexI[] _vertices;
	private int[] _bestTour;
	private double _bestCost;
	private GraphI _graph;
	private boolean _closed;
	private boolean _symmetric;
	private boolean _allAreDirected;
	private boolean[] _selected;
	private int _fromSelectedIdx;
	private int _toSelectedIdx;
	protected PropertiesI _properties = new PropertiesAdapter();
	protected Object _edgeKey;

	TSPBase() {
		_graph = null;
		_vertices = null;
		_bestTour = null;
		_edgeKey = null;
		_selected = null;
	}

	protected void checkChangeCount() {
		if (getGraph().getChangeCount() != _changeCount)
			throw new TSPError("The graph has changed since it was set.");
	}

	public void setProperties(PropertiesI properties) {
		_properties = properties;
	}

	public Vector<ElementI> rotateClosedTour(Vector<ElementI> tour, Object key)
			throws VertexNotFoundException {
		if (tour.firstElement() != tour.lastElement())
			throw new TSPError("The tour is not closed");
		int idx = -1;
		for (int i = 0; i < tour.size(); i += 2) {
			if (((VertexI) tour.elementAt(i)).getKey().equals(key)) {
				idx = i;
				break;
			}
		}
		if (idx == -1)
			throw new VertexNotFoundException();
		Vector<ElementI> newTour = new Vector<ElementI>(tour.size());
		for (int i = idx; i < tour.size() - 1; i++)
			newTour.addElement(tour.elementAt(i));
		for (int i = 0; i <= idx; i++)
			newTour.addElement(tour.elementAt(i));
		return newTour;
	}

	public void setEdgeKey(Object edgeKey) {
		_edgeKey = edgeKey;
	}

	public void setGraph(GraphI graph) {
		_graph = graph;
		if (_graph == null)
			return;
		_changeCount = graph.getChangeCount();
		_allAreDirected = (_graph.sizeOfEdges() == _graph.sizeOfDirectedEdges());
		setSymmetric(_graph.isSymmetric() && _properties.isSymmetric());
		int n = _graph.sizeOfVertices();
		_vertices = new VertexI[n];
		_selected = new boolean[n];
		for (int i = 0; i < n; i++)
			_selected[i] = true;
	}

	protected void initVertices(int from, int to) {
		checkChangeCount();
		setFromSelectedIdx(-1);
		setToSelectedIdx(-1);

		int cnt = 0;
		for (Iterator<VertexI> e = getGraph().vertices(); e
				.hasNext();) {
			VertexI v = e.next();
			int idx = v.getIndex();
			if (idx == from) {
				setFromSelectedIdx(cnt);
				_vertices[cnt++] = v;
			} else if (idx == to) {
				setToSelectedIdx(cnt);
				_vertices[cnt++] = v;
			} else if (_selected[idx]) {
				_vertices[cnt++] = v;
			}
		}
		setSize(cnt);
	}

	protected int countVertices() {
		checkChangeCount();
		int cnt = 0;
		for (int i = 0; i < _selected.length; i++)
			if (_selected[i])
				cnt++;
		setSize(cnt);
		return cnt;
	}

	protected void initVertices(Vector<ElementI> tour) {
		checkChangeCount();
		boolean[] used = new boolean[getGraph().sizeOfVertices()];
		int cnt = 0;
		int n = (tour.firstElement() == tour.lastElement()) ? tour.size() - 2
				: tour.size();
		for (int i = 0; i < n; i += 2) {
			VertexI vertex = (VertexI) tour.elementAt(i);
			_vertices[cnt++] = vertex;
			if (used[vertex.getIndex()])
				throw new TSPError("The tour has duplicate vertices");
			used[vertex.getIndex()] = true;
		}
		setSize(cnt);
	}

	public double getCost() {
		if (getBestTour() == null)
			throw new TSPError("No solution has been created");
		return getBestCost();
	}

	public Vector<ElementI> getTour() {
		checkChangeCount();
		if (getBestTour() == null)
			throw new TSPError("No solution has been created");
		if (getBestTour().length != getSize())
			throw new TSPError("Internal Error");
		Vector<ElementI> path = new Vector<ElementI>(
				(getBestTour().length + 1) * 2);
		VertexI last = _vertices[getBestTour()[0]];
		path.addElement(last);
		for (int i = 1; i < getBestTour().length; i++) {
			VertexI vert = _vertices[getBestTour()[i]];
			path.addElement(getGraph().getEdge(last, vert, _edgeKey));
			path.addElement(vert);
			last = vert;
		}
		if (isClosed()) {
			path.addElement(getGraph().getEdge(last,
					_vertices[getBestTour()[0]], _edgeKey));
			path.addElement(_vertices[getBestTour()[0]]);
		}
		return path;
	}

	public final double forwardCost(int r, int c) {
		if (r == c || r == -1 || c == -1)
			return 0.0;
		boolean reverse = false;
		EdgeI edge = getGraph().getMutableEdge(_vertices[r], _vertices[c],
				_edgeKey);
		if (edge != null && _properties.isEdgeRestricted(edge, reverse))
			edge = null;
		if (edge == null && !_allAreDirected) {
			reverse = true;
			edge = getGraph().getMutableEdge(_vertices[c], _vertices[r],
					_edgeKey);
			if (edge != null && edge.isDirected())
				edge = null;
			if (edge != null && _properties.isEdgeRestricted(edge, reverse))
				edge = null;
		}
		if (edge == null)
			return Double.POSITIVE_INFINITY;
		return _properties.getEdgeCost(edge, reverse);
	}

	public final double reverseCost(int c, int r) {
		return forwardCost(r, c);
	}

	public void setBestTour(int[] _bestTour) {
		this._bestTour = _bestTour;
	}

	public int[] getBestTour() {
		return _bestTour;
	}

	public GraphI getGraph() {
		return _graph;
	}

	public void setClosed(boolean _closed) {
		this._closed = _closed;
	}

	public boolean isClosed() {
		return _closed;
	}

	public void setFromSelectedIdx(int _fromSelectedIdx) {
		this._fromSelectedIdx = _fromSelectedIdx;
	}

	public int getFromSelectedIdx() {
		return _fromSelectedIdx;
	}

	public void setToSelectedIdx(int _toSelectedIdx) {
		this._toSelectedIdx = _toSelectedIdx;
	}

	public int getToSelectedIdx() {
		return _toSelectedIdx;
	}

	public void setSize(int _size) {
		this._size = _size;
	}

	public int getSize() {
		return _size;
	}

	public void setBestCost(double _bestCost) {
		this._bestCost = _bestCost;
	}

	public double getBestCost() {
		return _bestCost;
	}

	public void setSymmetric(boolean _symmetric) {
		this._symmetric = _symmetric;
	}

	public boolean isSymmetric() {
		return _symmetric;
	}

	public void setSelected(boolean[] selected) {
		if (selected.length != _selected.length)
			throw new TSPError("The length of 'selected' is invalid.");
		Array.copy(selected.length, _selected, 0, 1, selected, 0, 1);
		this._selected = selected;
	}

	public boolean[] getSelected() {
		boolean[] copy = new boolean[_selected.length];
		Array.copy(_selected.length, copy, 0, 1, _selected, 0, 1);
		return copy;
	}

	public void selectVertex(boolean[] select) {
		if (getGraph() == null)
			throw new TSPError("The graph is not set.");
		checkChangeCount();
		if (select.length != getGraph().sizeOfVertices())
			throw new TSPError(
					"The size of the select array  must equal the number of vertices in the graph.");
		for (int i = 0; i < _selected.length; i++)
			_selected[i] = select[i];
	}

	public void selectVertex(boolean select) {
		if (getGraph() == null)
			throw new TSPError("The graph is not set.");
		checkChangeCount();
		for (int i = 0; i < _selected.length; i++)
			_selected[i] = select;
	}

	public void selectVertex(Object key, boolean select)
			throws VertexNotFoundException {
		if (getGraph() == null)
			throw new TSPError("The graph is not set.");
		checkChangeCount();
		VertexI vertex = getGraph().getVertex(key);
		if (vertex == null)
			throw new VertexNotFoundException();
		_selected[vertex.getIndex()] = select;
	}

}
