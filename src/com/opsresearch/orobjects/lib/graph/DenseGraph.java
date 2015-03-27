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
import java.util.Vector;

import com.opsresearch.orobjects.lib.real.DoubleI;

public class DenseGraph extends BaseGraph implements AddI {

	private static final long serialVersionUID = 1L;

	class Vertex extends VertexBase implements VertexI, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		private class InEdgeIterator implements Iterator<EdgeI> {
			private int _toIndex;
			private int _fromIndex;
			private Edge _edge;
			private Vector<VertexI> _vertexVector;

			InEdgeIterator(Vertex vertex) {
				_vertexVector = ((DenseGraph) vertex.getGraph())._vertexVector;
				_toIndex = vertex.getIndex();
				for (_fromIndex = 0; _fromIndex < _vertexVector.size(); _fromIndex++) {
					Vertex from = (Vertex) _vertexVector.elementAt(_fromIndex);
					if ((_edge = from.getEdgeTo(_toIndex)) != null)
						return;
				}
			}

			public boolean hasNext() {
				return _edge != null;
			}

			public Edge next() {
				if (_edge == null)
					return null;
				Edge ret = _edge;
				_edge = _edge.getNextEdge();
				if (_edge != null)
					return ret;
				for (_fromIndex++; _fromIndex < _vertexVector.size(); _fromIndex++) {
					Vertex from = (Vertex) _vertexVector.elementAt(_fromIndex);
					if ((_edge = from.getEdgeTo(_toIndex)) != null)
						return ret;
				}
				return ret;
			}

			@Override
			public void remove() {
			}
		}

		private class OutEdgeIterator implements Iterator<EdgeI> {
			private int _toIndex;
			private Edge _edge;
			private Vector<EdgeI> _edgeVector;

			OutEdgeIterator(Vertex vertex) {
				if ((_edgeVector = vertex._edgeVector) == null) {
					_edge = null;
					return;
				}
				for (_toIndex = 0; _toIndex < _edgeVector.size(); _toIndex++) {
					if ((_edge = (Edge) _edgeVector.elementAt(_toIndex)) != null)
						return;
				}
			}

			public boolean hasNext() {
				return _edge != null;
			}

			public Edge next() {
				if (_edge == null)
					return null;
				Edge ret = _edge;
				_edge = _edge.getNextEdge();
				if (_edge != null)
					return ret;
				for (_toIndex++; _toIndex < _edgeVector.size(); _toIndex++) {
					if ((_edge = (Edge) _edgeVector.elementAt(_toIndex)) != null)
						return ret;
				}
				return ret;
			}

			@Override
			public void remove() {
			}
		}


		private Vector<EdgeI> _edgeVector;

		Vertex(int index, Object key, Object value) {
			super(index, key, value);
			_edgeVector = null;
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

		public GraphI getGraph() {
			return DenseGraph.this;
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

		public void setIndex(int index) {
			_index = index;
		}

		public Edge getEdgeTo(int toIndex) {
			if (_edgeVector == null)
				return null;
			if (_edgeVector.size() <= toIndex)
				return null;
			return (Edge) _edgeVector.elementAt(toIndex);
		}

		public void addOutEdge(Edge edge) {
			Vertex to = (Vertex) edge.getToVertex();
			int index = to._index;
			if (_edgeVector == null)
				_edgeVector = new Vector<EdgeI>(getVertexCapacity());
			if (_edgeVector.size() <= index)
				_edgeVector.setSize(index + 1);
			to._inDegree++;
			_outDegree++;
			edge.setNextEdge((Edge) _edgeVector.elementAt(index));
			_edgeVector.setElementAt(edge, index);
		}

		public String toString() {
			return _key + ", " + _value;
		}

	}

	class Edge extends EdgeBase implements EdgeI, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private Edge _nextEdge;
		private Vertex _toVertex;
		private Vertex _fromVertex;

		Edge(int index, Vertex from, Vertex to, Object key, Object value,
				boolean isDirected) {
			super(index, key, value, isDirected);
			_toVertex = to;
			_fromVertex = from;
			_fromVertex.addOutEdge(this);
		}

		public VertexI getToVertex() {
			return _toVertex;
		}

		public VertexI getFromVertex() {
			return _fromVertex;
		}

		public GraphI getGraph() {
			return DenseGraph.this;
		}

		public Edge getNextEdge() {
			return _nextEdge;
		}

		public void setNextEdge(Edge edge) {
			_nextEdge = edge;
		}

		public String toString() {
			return _fromVertex._key + (_isDirected ? " -" : " <") + "-> "
					+ _toVertex._key + ", " + _key + ", " + _value;
		}

	}


	private int _vertexCapacity;
	private Vector<VertexI> _vertexVector;

	public DenseGraph() {
		super();
		_vertexVector = new Vector<VertexI>();
	}

	public DenseGraph(int vertexCapacity) {
		super(vertexCapacity);
		_vertexCapacity = vertexCapacity;
		_vertexVector = new Vector<VertexI>(vertexCapacity);
	}

	public boolean isSymmetric() {
		if (_symmetric != null)
			return _symmetric.booleanValue();
		return false;
	}

	public void ensureEdgeCapacity(int edgeCapacity) {
	}

	public EdgeI addEdge(Object fromKey, Object toKey)
			throws DuplicateEdgeException, VertexNotFoundException {
		return addEdge(fromKey, toKey, null, false, null);
	}

	public EdgeI addEdge(Object fromKey, Object toKey, Object value)
			throws DuplicateEdgeException, VertexNotFoundException {
		return addEdge(fromKey, toKey, value, false, null);
	}

	public EdgeI addEdge(Object fromKey, Object toKey, Object value,
			boolean isDirected) throws DuplicateEdgeException,
			VertexNotFoundException {
		return addEdge(fromKey, toKey, value, isDirected, null);
	}

	public EdgeI addEdge(EdgeI edge) throws DuplicateEdgeException,
			VertexNotFoundException {
		return addEdge(edge.getFromVertex().getKey(), edge.getToVertex()
				.getKey(), edge.getValue(), edge.isDirected(), edge.getKey());
	}

	public final EdgeI addEdge(Object fromKey, Object toKey, Object value,
			boolean isDirected, Object key) throws DuplicateEdgeException,
			VertexNotFoundException {
		Vertex fv = (Vertex) getVertex(fromKey);
		if (fv == null)
			throw new VertexNotFoundException(
					"Can't find the FROM vertex object");
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
		Vertex vertex = new Vertex(_vertexVector.size(), key, value);
		_vertexVector.addElement(vertex);
		return vertex;
	}

	int getVertexCapacity() {
		return Math.max(_vertexCapacity, _vertexVector.size());
	}

	public EdgeI getMutableEdge(VertexI from, VertexI to, Object edgeKey) {
		return getEdge(from, to, edgeKey);
	}

	public EdgeI getEdge(VertexI from, VertexI to, Object edgeKey) {
		if (to.getGraph() != this)
			throw new GraphError("The to vertx is not cointained in this graph");
		if (from.getGraph() != this)
			throw new GraphError(
					"The from vertx is not cointained in this graph");
		Edge edge = ((Vertex) from).getEdgeTo(((Vertex) to).getIndex());
		while (edge != null) {
			Object k = edge.getKey();
			if (k == edgeKey)
				return edge;
			if (k != null && edgeKey != null && k.equals(edgeKey))
				return edge;
			edge = edge.getNextEdge();
		}
		;
		return null;
	}

	public String toString() {
		return super.toString("DenseGraph");
	}

}

