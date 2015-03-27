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

package com.opsresearch.orobjects.lib.graph;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.CorruptedError;
import com.opsresearch.orobjects.lib.real.DoubleI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixElementI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;

public class SparseGraph extends BaseGraph implements EditI {

	private static final long serialVersionUID = 1L;

	class Vertex extends VertexBase implements VertexI, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private class InEdgeIterator implements Iterator<EdgeI> {
			private Edge _edge;

			InEdgeIterator(Vertex vertex) {
				_edge = vertex.getInEdge();
			}

			public boolean hasNext() {
				return _edge != null;
			}

			public EdgeI next() {
				Edge ret = _edge;
				_edge = _edge.getNextInEdge();
				return ret;
			}

			@Override
			public void remove() {
			}
		}

		private class OutEdgeIterator implements Iterator<EdgeI> {
			private Edge _edge;

			OutEdgeIterator(Vertex vertex) {
				_edge = vertex.getOutEdge();
			}

			public boolean hasNext() {
				return _edge != null;
			}

			public EdgeI next() {
				Edge ret = _edge;
				_edge = _edge.getNextOutEdge();
				return ret;
			}

			@Override
			public void remove() {
			}
		}


		private Edge _inList;
		private Edge _outList;

		Vertex(int index, Object key, Object value) {
			super(index, key, value);
			_inList = null;
			_outList = null;
		};

		public int getIndex() {
			return _index;
		}

		public int getInDegree() {
			return _inDegree;
		}

		public int getOutDegree() {
			return _outDegree;
		}

		public Object getKey() {
			return _key;
		}

		public Object getValue() {
			return _value;
		}

		public GraphI getGraph() {
			return SparseGraph.this;
		}

		public boolean isEdge() {
			return false;
		}

		public boolean isVertex() {
			return true;
		}

		public double doubleValue() {
			if (_value == null)
				return 0.0;
			if (_value instanceof DoubleI)
				return ((DoubleI) _value).doubleValue();
			if (_value instanceof Number)
				return ((Number) _value).doubleValue();
			return 0.0;
		}

		public Iterator<EdgeI> inEdges() {
			return new InEdgeIterator(this);
		}

		public Iterator<EdgeI> outEdges() {
			return new OutEdgeIterator(this);
		}

		public Iterator<EdgeI> mutableInEdges() {
			return new InEdgeIterator(this);
		}

		public Iterator<EdgeI> mutableOutEdges() {
			return new OutEdgeIterator(this);
		}

		public void decIndex() {
			_index--;
		}

		public Edge getInEdge() {
			return _inList;
		}

		public Edge getOutEdge() {
			return _outList;
		}

		public void removeAllEdges() {
			_inList = null;
			_outList = null;
			_inDegree = 0;
			_outDegree = 0;
		}

		public void addInEdge(Edge edge) {
			edge.setNextInEdge(_inList);
			_inList = edge;
			_inDegree++;
		}

		public void addOutEdge(Edge edge) {
			edge.setNextOutEdge(_outList);
			_outList = edge;
			_outDegree++;
		}

		public boolean removeInEdge(Edge edge) {
			if (_inList == edge) {
				_inList = _inList.getNextInEdge();
				_inDegree--;
				return true;
			}
			Edge e = _inList;
			while (e != null) {
				if (e.getNextInEdge() == edge) {
					e.setNextInEdge(e.getNextInEdge().getNextInEdge());
					_inDegree--;
					return true;
				}
				e = e.getNextInEdge();
			}
			return false;
		}

		public boolean removeOutEdge(Edge edge) {
			if (_outList == edge) {
				_outList = _outList.getNextOutEdge();
				_outDegree--;
				return true;
			}
			Edge e = _outList;
			while (e != null) {
				if (e.getNextOutEdge() == edge) {
					e.setNextOutEdge(e.getNextOutEdge().getNextOutEdge());
					_outDegree--;
					return true;
				}
				e = e.getNextOutEdge();
			}
			return false;
		}

		public String toString() {
			return _key + ", " + _value;
		}

	}

	class Edge extends EdgeBase implements EdgeI, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private Vertex _toVertex;
		private Vertex _fromVertex;
		private Edge _nextInEdge;
		private Edge _nextOutEdge;

		Edge(int index, Vertex from, Vertex to, Object key, Object value, boolean isDirected) {
			super(index, key, value, isDirected);
			_toVertex = to;
			_fromVertex = from;
			_toVertex.addInEdge(this);
			_fromVertex.addOutEdge(this);
		}

		public VertexI getToVertex() {
			return _toVertex;
		}

		public VertexI getFromVertex() {
			return _fromVertex;
		}

		public GraphI getGraph() {
			return SparseGraph.this;
		}

		public void decIndex() {
			_index--;
		}

		public Edge getNextInEdge() {
			return _nextInEdge;
		}

		public void setNextInEdge(Edge edge) {
			_nextInEdge = edge;
		}

		public Edge getNextOutEdge() {
			return _nextOutEdge;
		}

		public void setNextOutEdge(Edge edge) {
			_nextOutEdge = edge;
		}

		public String toString() {
			return _fromVertex._key + (_isDirected ? " -" : " <") + "-> " + _toVertex._key + ", " + _key + ", "
					+ _value;
		}

	}


	public SparseGraph() {
		super();
	}

	public SparseGraph(int vertexCapacity) {
		super(vertexCapacity);
	}

	public SparseGraph(MatrixI matrix) {
		super(Math.max(matrix.sizeOfRows(), matrix.sizeOfColumns()));
		int n = Math.max(matrix.sizeOfRows(), matrix.sizeOfColumns());

		try {

			Integer[] ints = new Integer[n];
			for (int i = 0; i < n; i++)
				addVertex(ints[i] = new Integer(i));

			for (Iterator<MatrixElementI> e = matrix.elements(); e.hasNext();) {
				MatrixElementI elem = e.next();
				double d = elem.getValue();
				if (d != Double.POSITIVE_INFINITY)
					addEdge(ints[elem.getRowIndex()], ints[elem.getColumnIndex()], new Double(d), true);

			}

		} catch (DuplicateEdgeException e) {
		} catch (VertexNotFoundException e) {
		} catch (DuplicateVertexException e) {
		}
	}

	public boolean isSymmetric() {
		if (_symmetric != null)
			return _symmetric.booleanValue();
		return false;
	}

	public void ensureEdgeCapacity(int edgeCapacity) {
	}

	public EdgeI addEdge(Object fromKey, Object toKey) throws DuplicateEdgeException, VertexNotFoundException {
		return addEdge(fromKey, toKey, null, false, null);
	}

	public EdgeI addEdge(Object fromKey, Object toKey, Object value) throws DuplicateEdgeException,
			VertexNotFoundException {
		return addEdge(fromKey, toKey, value, false, null);
	}

	public EdgeI addEdge(Object fromKey, Object toKey, Object value, boolean isDirected) throws DuplicateEdgeException,
			VertexNotFoundException {
		return addEdge(fromKey, toKey, value, isDirected, null);
	}

	public EdgeI addEdge(EdgeI edge) throws DuplicateEdgeException, VertexNotFoundException {
		return addEdge(edge.getFromVertex().getKey(), edge.getToVertex().getKey(), edge.getValue(), edge.isDirected(),
				edge.getKey());
	}

	public final EdgeI addEdge(Object fromKey, Object toKey, Object value, boolean isDirected, Object key)
			throws DuplicateEdgeException, VertexNotFoundException {
		Vertex fv = (Vertex) getVertex(fromKey);
		if (fv == null)
			throw new VertexNotFoundException("Can't find the FROM vertex object");
		Vertex tv = (Vertex) getVertex(toKey);
		if (tv == null)
			throw new VertexNotFoundException("Can't find the TO vertex object");
		if (getEdge(fv, tv, key) != null)
			throw new DuplicateEdgeException();
		if (isDirected)
			_sizeOfDirectedEdges++;
		_changeCount++;
		return new Edge(_sizeOfEdges++, fv, tv, key, value, isDirected);
	}

	protected VertexI newVertex(Object key, Object value) {
		return new Vertex(sizeOfVertices(), key, value);
	}

	public EdgeI getMutableEdge(VertexI from, VertexI to, Object edgeKey) {
		return getEdge(from, to, edgeKey);
	}

	public EdgeI getEdge(VertexI from, VertexI to, Object edgeKey) {
		if (to.getGraph() != this)
			throw new GraphError("The to vertx is not cointained in this graph");
		if (from.getGraph() != this)
			throw new GraphError("The from vertx is not cointained in this graph");

		Vertex t = (Vertex) to;
		Vertex f = (Vertex) from;

		if (f.getOutDegree() < t.getInDegree()) {
			for (Edge edge = f.getOutEdge(); edge != null; edge = edge.getNextOutEdge()) {
				if (edge.getToVertex() != t)
					continue;
				if (edge.getKey() == edgeKey)
					return edge;
				if (edge.getKey() == null || edgeKey == null)
					continue;
				if (edge.getKey().equals(edgeKey))
					return edge;
			}
		} else {
			for (Edge edge = t.getInEdge(); edge != null; edge = edge.getNextInEdge()) {
				if (edge.getFromVertex() != f)
					continue;
				if (edge.getKey() == edgeKey)
					return edge;
				if (edge.getKey() == null || edgeKey == null)
					continue;
				if (edge.getKey().equals(edgeKey))
					return edge;
			}
		}
		return null;
	}


	private void _deleteVertex(VertexI vertex) {
		int index = vertex.getIndex();
		_vertexHashMap.remove(vertex.getKey());
		for (Iterator<VertexI> e = vertices(); e.hasNext(); ) {
			Vertex vert = (Vertex) e.next();
			if (vert.getIndex() > index)
				vert.decIndex();
		}

	}

	public VertexI removeVertex(VertexI vertex, boolean removeZeroVertices) throws VertexNotFoundException {
		return removeVertex(vertex.getKey(), removeZeroVertices);
	}

	public VertexI removeVertex(Object key, boolean removeZeroVertices) throws VertexNotFoundException {
		VertexI vertex = (VertexI) _vertexHashMap.get(key);
		if (vertex == null)
			throw new VertexNotFoundException();
		_removeVertex(vertex, removeZeroVertices);
		_changeCount++;
		return vertex;
	}

	protected void _removeVertex(VertexI vertex, boolean removeZeroVertex) {
		Vertex vert = (Vertex) vertex;
		while (vert.getInEdge() != null)
			_removeEdge(vert.getInEdge(), removeZeroVertex);
		while (vert.getOutEdge() != null)
			_removeEdge(vert.getOutEdge(), removeZeroVertex);
		if (!removeZeroVertex)
			_deleteVertex(vertex);
	}

	private void _deleteEdge(EdgeI edge) {
		int index = edge.getIndex();
		for (Iterator<EdgeI> e = edges(); e.hasNext(); ) {
			Edge edg = (Edge) e.next();
			if (edg.getIndex() > index)
				edg.decIndex();
		}
	}

	public EdgeI removeEdge(EdgeI edge, boolean removeZeroVertices) throws VertexNotFoundException,
			EdgeNotFoundException {
		return removeEdge(edge.getFromVertex().getKey(), edge.getFromVertex().getKey(), edge.getKey(),
				removeZeroVertices);
	}

	public EdgeI removeEdge(Object fromKey, Object toKey, Object key, boolean removeZeroVertices)
			throws VertexNotFoundException, EdgeNotFoundException {
		VertexI from = (VertexI) _vertexHashMap.get(fromKey);
		if (from == null)
			throw new VertexNotFoundException("Can't find FROM vertex");
		VertexI to = (VertexI) _vertexHashMap.get(toKey);
		if (to == null)
			throw new VertexNotFoundException("Can't find TO vertex");
		EdgeI edge = getEdge(from, to, key);
		if (edge == null)
			throw new EdgeNotFoundException();
		_removeEdge((Edge) edge, removeZeroVertices);
		_changeCount++;
		return edge;
	}

	private void _removeEdge(Edge edge, boolean removeZeroVertices) {
		Vertex to = (Vertex) edge.getToVertex();
		Vertex from = (Vertex) edge.getFromVertex();
		if (!to.removeInEdge(edge))
			throw new CorruptedError("Edge was not in the to vertex.");
		if (!from.removeOutEdge(edge))
			throw new CorruptedError("Edge was not in the from vertex.");
		if (removeZeroVertices) {
			if (to.getInDegree() == 0 && to.getOutDegree() == 0)
				_deleteVertex(to);
			if (from.getInDegree() == 0 && from.getOutDegree() == 0)
				_deleteVertex(from);
		}
		_sizeOfEdges--;
		if (edge._isDirected)
			_sizeOfDirectedEdges--;
		_deleteEdge(edge);
	}

	public void removeAllEdges() {
		_sizeOfEdges = 0;
		_sizeOfDirectedEdges = 0;
		for (Iterator<VertexI> e = vertices(); e.hasNext(); )
			((Vertex) e.next()).removeAllEdges();
		_changeCount++;
	}

	public String toString() {
		return super.toString("SparseGraph");
	}

}
