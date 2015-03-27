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

import com.opsresearch.orobjects.lib.geom.CoordinateSystemI;
import com.opsresearch.orobjects.lib.geom.rect2.Point;
import com.opsresearch.orobjects.lib.geom.rect2.PointI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class PointGraph extends BaseGraph implements GraphI, AddVertexI, RemoveVertexI {

	private static final long serialVersionUID = 1L;

	class Vertex extends VertexBase implements VertexI, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		class InEdgeIterator implements Iterator<EdgeI> {
			private Edge _theEdge;
			private Vertex _toVertex;
			private Iterator<VertexI> vertices;
			private boolean _mutable, _useProxy;

			InEdgeIterator(Vertex vertex, boolean mutable, boolean useProxy) {
				_useProxy = useProxy;
				_mutable = mutable;
				_toVertex = vertex;
				vertices = vertex.getGraph().vertices();
				if (mutable)
					_theEdge = vertex.newEdge(null, vertex, 0.0, 0.0, 0.0);
			}

			public boolean hasNext() {
				return vertices.hasNext();
			}

			public EdgeI next() {
				Vertex fromVertex = (Vertex) vertices.next();
				if (fromVertex == null)
					return null;
				double dist = _useProxy ? ((PointI) fromVertex._value).getDistanceProxyTo((PointI) _toVertex._value)
						: ((PointI) fromVertex._value).getDistanceTo((PointI) _toVertex._value);
				double cost = dist * _cost;
				double time = dist / _speed;
				if (_mutable) {
					_theEdge._fromVertex = fromVertex;
					((EdgeValue) _theEdge._value).set(cost, time, dist);
					return _theEdge;
				}
				return _toVertex.newEdge(fromVertex, _toVertex, cost, time, dist);
			}

			@Override
			public void remove() {
			}
		}

		class OutEdgeIterator implements Iterator<EdgeI> {
			private Edge _theEdge;
			private Vertex _fromVertex;
			private Iterator<VertexI> vertices;
			private boolean _mutable, _useProxy;

			OutEdgeIterator(Vertex vertex, boolean mutable, boolean useProxy) {
				_useProxy = useProxy;
				_mutable = mutable;
				_fromVertex = vertex;
				vertices = vertex.getGraph().vertices();
				if (mutable)
					_theEdge = vertex.newEdge(vertex, null, 0.0, 0.0, 0.0);
			}

			public boolean hasNext() {
				return vertices.hasNext();
			}

			public EdgeI next() {
				Vertex toVertex = (Vertex) vertices.next();
				if (toVertex == null)
					return null;
				double dist = _useProxy ? ((PointI) _fromVertex._value).getDistanceProxyTo((PointI) toVertex._value)
						: ((PointI) _fromVertex._value).getDistanceTo((PointI) toVertex._value);
				double cost = dist * _cost;
				double time = dist / _speed;
				if (_mutable) {
					_theEdge._toVertex = toVertex;
					((EdgeValue) _theEdge._value).set(cost, time, dist);
					return _theEdge;
				}
				return _fromVertex.newEdge(_fromVertex, toVertex, cost, time, dist);
			}

			@Override
			public void remove() {
			}
		}


		Vertex(int index, Object key, PointI value) {
			super(index, key, value);
		};

		Edge newEdge(Vertex from, Vertex to, double cost, double time, double distance) {
			return new Edge(from, to, cost, time, distance);
		}

		public int getInDegree() {
			return sizeOfVertices();
		}

		public int getOutDegree() {
			return sizeOfVertices();
		}

		public GraphI getGraph() {
			return PointGraph.this;
		}

		public Iterator<EdgeI> inEdges() {
			return new InEdgeIterator(this, false, _useProxy);
		}

		public Iterator<EdgeI> outEdges() {
			return new OutEdgeIterator(this, false, _useProxy);
		}

		public Iterator<EdgeI> mutableInEdges() {
			return new InEdgeIterator(this, true, _useProxy);
		}

		public Iterator<EdgeI> mutableOutEdges() {
			return new OutEdgeIterator(this, true, _useProxy);
		}

		public void decIndex() {
			_index--;
		}

	}

	class Edge extends EdgeBase implements EdgeI, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private Vertex _toVertex;
		private Vertex _fromVertex;

		Edge(Vertex from, Vertex to, double cost, double time, double distance) {
			super(0, null, new EdgeValue(cost, time, distance), false);
			_toVertex = to;
			_fromVertex = from;
		}

		public int getIndex() {
			return _fromVertex._index * sizeOfVertices() + _toVertex._index;
		}

		public VertexI getToVertex() {
			return _toVertex;
		}

		public VertexI getFromVertex() {
			return _fromVertex;
		}

		public GraphI getGraph() {
			return PointGraph.this;
		}

	}


	private boolean _useProxy;
	private Edge _theEdge;
	private CoordinateSystemI _coordinateSystem;
	private double _cost = 1.0;
	private double _speed = 1.0;

	public PointGraph() {
		super();
		_coordinateSystem = null;
		_useProxy = false;
		_theEdge = new Edge(null, null, 0.0, 0.0, 0.0);
	}

	public PointGraph(int vertexCapacity) {
		super(vertexCapacity);
		_coordinateSystem = null;
		_useProxy = false;
		_theEdge = new Edge(null, null, 0.0, 0.0, 0.0);
	}

	public PointGraph(VectorI x, VectorI y) {
		super(x.size());
		if (x.size() != y.size())
			throw new GraphError("The arguments must be the same size.");
		_useProxy = false;
		_theEdge = new Edge(null, null, 0.0, 0.0, 0.0);
		int n = x.size();
		try {
			for (int i = 0; i < n; i++)
				addVertex(new Integer(i), new Point(x.elementAt(i), y.elementAt(i)));
		} catch (DuplicateVertexException e) {
		}
	}

	public PointGraph(VectorI x, VectorI y, VectorI z) {
		super(x.size());
		if (x.size() != y.size())
			throw new GraphError("The arguments must be the same size.");
		if (x.size() != z.size())
			throw new GraphError("The arguments must be the same size.");
		_useProxy = false;
		_theEdge = new Edge(null, null, 0.0, 0.0, 0.0);
		int n = x.size();
		try {
			for (int i = 0; i < n; i++)
				addVertex(new Integer(i), new com.opsresearch.orobjects.lib.geom.rect3.Point(x.elementAt(i), y.elementAt(i),
						z.elementAt(i)));
		} catch (DuplicateVertexException e) {
		}
	}

	public void setSpeed(double speed) {
		_speed = speed;
	}

	public void setCost(double cost) {
		_cost = cost;
	}

	public CoordinateSystemI cordinateSystem() {
		return _coordinateSystem;
	}

	public void useDistanceProxy(boolean useProxy) {
		_useProxy = useProxy;
	}

	public VertexI addVertex(Object key, Object value) throws DuplicateVertexException {
		if (value == null)
			throw new GraphError("The value of a PointGraph vertex can not be null.");
		if (!(value instanceof PointI))
			throw new GraphError("The value of a PointGraph vertex must implement the PointI interface.");

		if (_coordinateSystem == null)
			_coordinateSystem = ((PointI) value).coordinateSystem();
		else if (!_coordinateSystem.equals(((PointI) value).coordinateSystem()))
			throw new GraphError("All the points must be from the same coordinate system.");

		VertexI ret = super.addVertex(key, value);
		_sizeOfDirectedEdges = _sizeOfEdges = sizeOfVertices() * sizeOfVertices();
		_changeCount++;
		return ret;
	}

	protected VertexI newVertex(Object key, Object value) {
		return new Vertex(sizeOfVertices(), key, (PointI) value);
	}

	public boolean isSymmetric() {
		if (_symmetric != null)
			return _symmetric.booleanValue();
		return _coordinateSystem.isSymmetric();
	}

	public GraphI newGraph(int vertexCapacity, int edgeCapacity) {
		if (vertexCapacity == 0)
			return new PointGraph();
		else
			return new PointGraph(vertexCapacity);
	}

	public EdgeI getEdge(VertexI from, VertexI to, Object edgeKey) {
		if (edgeKey != null)
			return null;
		if (to.getGraph() != this)
			throw new GraphError("The to vertex is not contained in this graph");
		if (from.getGraph() != this)
			throw new GraphError("The from vertex is not contained in this graph");
		Vertex f = (Vertex) from;
		Vertex t = (Vertex) to;
		double dist = _useProxy ? ((PointI) f._value).getDistanceProxyTo((PointI) t._value) : ((PointI) f._value)
				.getDistanceTo((PointI) t._value);
		double cost = dist * _cost;
		double time = dist / _speed;
		return new Edge(f, t, cost, time, dist);
	}

	public EdgeI getMutableEdge(VertexI from, VertexI to, Object edgeKey) {
		if (edgeKey != null)
			return null;
		if (to.getGraph() != this)
			throw new GraphError("The to vertex is not contained in this graph");
		if (from.getGraph() != this)
			throw new GraphError("The from vertex is not contained in this graph");
		Vertex f = (Vertex) from;
		Vertex t = (Vertex) to;
		double dist = _useProxy ? ((PointI) f._value).getDistanceProxyTo((PointI) t._value) : ((PointI) f._value)
				.getDistanceTo((PointI) t._value);
		double cost = dist * _cost;
		double time = dist / _speed;
		_theEdge._fromVertex = f;
		_theEdge._toVertex = t;
		((EdgeValue) _theEdge._value).set(cost, time, dist);
		return _theEdge;
	}


	private void _deleteVertex(VertexI vertex) {
		int index = vertex.getIndex();
		_vertexHashMap.remove(vertex.getKey());
		for (Iterator<VertexI> e = vertices(); e.hasNext(); ) {
			Vertex vert = (Vertex) e.next();
			if (vert.getIndex() > index)
				vert.decIndex();
		}
		_sizeOfDirectedEdges = _sizeOfEdges = sizeOfVertices() * sizeOfVertices();
	}

	public VertexI removeVertex(VertexI vertex, boolean removeZeroVertices) throws VertexNotFoundException {
		return removeVertex(vertex.getKey(), removeZeroVertices);
	}

	public VertexI removeVertex(Object key, boolean removeZeroVertices) throws VertexNotFoundException {
		Vertex vertex = (Vertex) _vertexHashMap.get(key);
		if (vertex == null)
			throw new VertexNotFoundException();
		_deleteVertex(vertex);
		_changeCount++;
		return vertex;
	}

	public String toString() {
		return super.toString("PointGraph");
	}

}
