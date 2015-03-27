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
import java.util.HashMap;
import java.util.Vector;

import com.opsresearch.orobjects.lib.real.matrix.MatrixI;

public class MatrixGraph extends Graph implements GraphI {

	private static final long serialVersionUID = 1L;

	class Vertex extends VertexBase implements VertexI, java.io.Serializable {

		private static final long serialVersionUID = 1L;

		class OutEdgeIterator implements Iterator<EdgeI> {
			private Edge _edge;
			private Vertex _from;
			private Vertex[] _toVerts;
			private boolean _mutable;
			private int _indexBase;
			private int _i;

			public OutEdgeIterator(Vertex vertex, Vertex[] fromVerts, Vertex[] toVerts, boolean mutable) {
				_edge = null;
				if (vertex._fromIndex == -1)
					return;
				_from = vertex;
				_mutable = mutable;
				_toVerts = toVerts;
				_indexBase = vertex._fromIndex * fromVerts.length;
				for (_i = 0; _i < _toVerts.length; _i++) {
					double cost = _from._edgeCosts[_i];
					if (cost == Double.POSITIVE_INFINITY)
						continue;
					double time = _from._edgeTimes == null ? 0.0 : _from._edgeTimes[_i];
					double dist = _from._edgeDists == null ? 0.0 : _from._edgeDists[_i];
					_edge = _from.newEdge();
					_edge._fromVertex = _from;
					_edge._toVertex = _toVerts[_i];
					_edge._cost = cost;
					_edge._time = time;
					_edge._dist = dist;
					_edge._index = _indexBase + _i;
					_i++;
					return;
				}
			}

			public boolean hasNext() {
				return _edge != null;
			}

			public EdgeI next() {
				if (_edge == null)
					return null;
				Edge ret = _edge;
				for (; _i < _toVerts.length; _i++) {
					double cost = _from._edgeCosts[_i];
					if (cost == Double.POSITIVE_INFINITY)
						continue;
					if (!_mutable) {
						_edge = _from.newEdge();
						_edge._fromVertex = _from;
					}
					_edge._toVertex = _toVerts[_i];
					_edge._cost = cost;
					_edge._time = _from._edgeTimes == null ? 0.0 : _from._edgeTimes[_i];
					_edge._dist = _from._edgeDists == null ? 0.0 : _from._edgeDists[_i];
					_edge._index = _indexBase + _i;
					_i++;
					return ret;
				}
				_edge = null;
				return ret;
			}

			@Override
			public void remove() {
			}
		}

		class InEdgeIterator implements Iterator<EdgeI> {
			private Edge _edge;
			private Vertex _to;
			private Vertex[] _fromVerts;
			private boolean _mutable;
			private int _i;

			public InEdgeIterator(Vertex vertex, Vertex[] fromVerts, Vertex[] toVerts, boolean mutable) {
				_edge = null;
				if (vertex._toIndex == -1)
					return;
				_to = vertex;
				_mutable = mutable;
				_fromVerts = fromVerts;
				for (_i = 0; _i < _fromVerts.length; _i++) {
					Vertex from = _fromVerts[_i];
					double cost = from._edgeCosts[_to._toIndex];
					if (cost == Double.POSITIVE_INFINITY)
						continue;
					double time = from._edgeTimes == null ? 0.0 : from._edgeTimes[_to._toIndex];
					double dist = from._edgeDists == null ? 0.0 : from._edgeDists[_to._toIndex];
					_edge = _to.newEdge();
					_edge._toVertex = _to;
					_edge._fromVertex = from;
					_edge._cost = cost;
					_edge._time = time;
					_edge._dist = dist;
					_edge._index = _to._toIndex + _i * _fromVerts.length;
					_i++;
					return;
				}
			}

			public boolean hasNext() {
				return _edge != null;
			}

			public EdgeI next() {
				if (_edge == null)
					return null;
				Edge ret = _edge;
				for (; _i < _fromVerts.length; _i++) {
					Vertex from = _fromVerts[_i];
					double cost = from._edgeCosts[_to._toIndex];
					if (cost == Double.POSITIVE_INFINITY)
						continue;
					if (!_mutable) {
						_edge = _to.newEdge();
						_edge._toVertex = _to;
					}
					_edge._fromVertex = from;
					_edge._cost = cost;
					_edge._time = from._edgeCosts == null ? 0.0 : from._edgeCosts[_to._toIndex];
					_edge._dist = from._edgeDists == null ? 0.0 : from._edgeDists[_to._toIndex];
					_edge._index = _to._toIndex + _i * _fromVerts.length;
					_i++;
					return ret;
				}
				_edge = null;
				return ret;
			}

			@Override
			public void remove() {
			}
		}


		private int _fromIndex, _toIndex;
		private double[] _edgeCosts;
		private double[] _edgeTimes;
		private double[] _edgeDists;

		Vertex(int index, Object key, Object value, double[] edgeCosts) {
			super(index, key, value);
			_fromIndex = _toIndex = -1;
			_edgeCosts = edgeCosts;
		};

		Vertex(int index, Object key, Object value, double[] edgeCosts, double[] edgeTimes, double[] edgeDistances) {
			super(index, key, value);
			_fromIndex = _toIndex = -1;
			_edgeCosts = edgeCosts;
			_edgeTimes = edgeTimes;
			_edgeDists = edgeDistances;
		};

		Vertex(int index, VertexI vertex, double[] edgeCosts, double[] edgeTimes, double[] edgeDistances) {
			super(index, vertex.getKey(), vertex.getValue());
			_fromIndex = _toIndex = -1;
			_edgeCosts = edgeCosts;
			_edgeTimes = edgeTimes;
			_edgeDists = edgeDistances;
		};

		public Iterator<EdgeI> inEdges() {
			return new InEdgeIterator(this, _fromVertices, _toVertices, false);
		}

		public Iterator<EdgeI> outEdges() {
			return new OutEdgeIterator(this, _fromVertices, _toVertices, false);
		}

		public Iterator<EdgeI> mutableInEdges() {
			return new InEdgeIterator(this, _fromVertices, _toVertices, true);
		}

		public Iterator<EdgeI> mutableOutEdges() {
			return new OutEdgeIterator(this, _fromVertices, _toVertices, true);
		}

		public GraphI getGraph() {
			return MatrixGraph.this;
		}

		public Edge getEdgeTo(Vertex to, Edge edge) {
			if (_edgeCosts == null || to._toIndex == -1)
				return null;
			int toIndex = to._toIndex;
			double cost = _edgeCosts[toIndex];
			if (cost == Double.POSITIVE_INFINITY)
				return null;
			edge._cost = cost;
			edge._time = _edgeCosts == null ? 0.0 : _edgeCosts[toIndex];
			edge._dist = _edgeDists == null ? 0.0 : _edgeDists[toIndex];
			edge._index = _index * sizeOfVertices() + toIndex;
			edge._fromVertex = this;
			edge._toVertex = to;
			return edge;
		}

		public String toString() {
			return _key + ", " + _value;
		}

		Edge newEdge() {
			return new Edge();
		}

	}

	class Edge implements EdgeI, java.io.Serializable {
		private static final long serialVersionUID = 1L;
		private int _index;
		private double _cost, _time, _dist;
		private Vertex _toVertex;
		private Vertex _fromVertex;

		public int getIndex() {
			return _index;
		}

		public Object getKey() {
			return null;
		}

		public Object getValue() {
			return null;
		}

		public VertexI getToVertex() {
			return _toVertex;
		}

		public VertexI getFromVertex() {
			return _fromVertex;
		}

		public boolean isDirected() {
			return true;
		}

		public GraphI getGraph() {
			return MatrixGraph.this;
		}

		public boolean isEdge() {
			return true;
		}

		public boolean isVertex() {
			return false;
		}

		public double getCost(boolean reverse) {
			return _cost;
		}

		public double getTime(boolean reverse) {
			return _time;
		}

		public double getDistance(boolean reverse) {
			return _dist;
		}

		public GraphI getSubgraph() {
			return null;
		}

		public String toString() {
			return _fromVertex._key + "--> " + _toVertex._key + ", null, " + _cost;
		}

	}


	private int _sizeOfEdges;
	private Edge _theEdge;
	private boolean _isSymmetric;
	private HashMap<Object, VertexI> _vertexHashMap;
	private Vertex[] _toVertices;
	private Vertex[] _fromVertices;

	public MatrixGraph(MatrixI cost, MatrixI time, MatrixI distance) {
		if (cost.sizeOfRows() != cost.sizeOfColumns())
			throw new GraphError("The matrix must be square.");
		Vector<Object> keys = new Vector<Object>(cost.sizeOfRows());
		for (int i = 0; i < cost.sizeOfRows(); i++)
			keys.addElement(new Integer(i));
		try {
			construct(cost, time, distance, keys, null);
		} catch (DuplicateVertexException e) {
			throw new GraphError("Internal Error");
		}
	}

	public MatrixGraph(MatrixI cost, MatrixI time, MatrixI distance, Vector<Object> vertexKeys)
			throws DuplicateVertexException {
		if (cost != null && cost.sizeOfRows() != cost.sizeOfColumns())
			throw new GraphError("The matrix is not square.");
		if (time != null && time.sizeOfRows() != time.sizeOfColumns())
			throw new GraphError("The matrix is not square.");
		if (distance != null && distance.sizeOfRows() != distance.sizeOfColumns())
			throw new GraphError("The matrix is not square.");
		if (cost != null && cost.sizeOfRows() != vertexKeys.size())
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (time != null && time.sizeOfRows() != vertexKeys.size())
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (distance != null && distance.sizeOfRows() != vertexKeys.size())
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		construct(cost, time, distance, vertexKeys, null);
	}

	public MatrixGraph(MatrixI cost, MatrixI time, MatrixI distance, Vector<Object> vertexKeys,
			Vector<VertexI> vertexValues) throws DuplicateVertexException {
		if (cost != null && cost.sizeOfRows() != cost.sizeOfColumns())
			throw new GraphError("The matrix is not square.");
		if (time != null && time.sizeOfRows() != time.sizeOfColumns())
			throw new GraphError("The matrix is not square.");
		if (distance != null && distance.sizeOfRows() != distance.sizeOfColumns())
			throw new GraphError("The matrix is not square.");
		if (cost != null && cost.sizeOfRows() != vertexKeys.size())
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (time != null && time.sizeOfRows() != vertexKeys.size())
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (distance != null && distance.sizeOfRows() != vertexKeys.size())
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		construct(cost, time, distance, vertexKeys, vertexValues);
	}

	public MatrixGraph(MatrixI cost, MatrixI time, MatrixI distance, VertexI[] fromVertices, VertexI[] toVertices)
			throws ParallelEdgeException {
		if (cost != null && cost.sizeOfRows() != fromVertices.length)
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (cost != null && cost.sizeOfColumns() != toVertices.length)
			throw new GraphError("The size of 'toVertices' must equal the columns in 'matrix'");
		if (time != null && time.sizeOfRows() != fromVertices.length)
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (time != null && time.sizeOfColumns() != toVertices.length)
			throw new GraphError("The size of 'toVertices' must equal the columns in 'matrix'");
		if (distance != null && distance.sizeOfRows() != fromVertices.length)
			throw new GraphError("The size of 'fromVertices' must equal the rows in 'matrix'");
		if (distance != null && distance.sizeOfColumns() != toVertices.length)
			throw new GraphError("The size of 'toVertices' must equal the columns in 'matrix'");
		construct(cost, time, distance, fromVertices, toVertices);
	}

	public MatrixGraph(GraphI graph, Object edgeKey) {
		construct(graph, edgeKey, null);
	}

	public MatrixGraph(GraphI graph, Object edgeKey, EdgePropertiesI properties) {
		construct(graph, edgeKey, properties);
	}

	private void construct(GraphI graph, Object edgeKey, EdgePropertiesI properties) {
		_isSymmetric = graph.isSymmetric();
		if (properties == null)
			properties = new PropertiesAdapter();

		int n = graph.sizeOfVertices();
		double[][] timeArray = new double[n][n];
		double[][] distArray = new double[n][n];
		double[][] costArray = new double[n][n];
		for (int i = 0; i < n; i++) {
			double[] crow = costArray[i];
			double[] trow = timeArray[i];
			double[] drow = distArray[i];
			for (int j = 0; j < n; j++) {
				crow[j] = Double.POSITIVE_INFINITY;
				trow[j] = Double.POSITIVE_INFINITY;
				drow[j] = Double.POSITIVE_INFINITY;
			}
		}
		_theEdge = new Edge();
		_vertexHashMap = new HashMap<Object, VertexI>();

		for (Iterator<EdgeI> e = graph.mutableEdges(); e.hasNext();) {
			EdgeI edgeI = (EdgeI) e.next();
			Object key = edgeI.getKey();
			if (edgeKey != key && (edgeKey == null || key == null || !edgeKey.equals(key)))
				continue;
			int j = edgeI.getToVertex().getIndex();
			int i = edgeI.getFromVertex().getIndex();
			if (!properties.isEdgeRestricted(edgeI, false)) {
				costArray[i][j] = properties.getEdgeCost(edgeI, false);
				timeArray[i][j] = properties.getEdgeTime(edgeI, false);
				distArray[i][j] = properties.getEdgeDistance(edgeI, false);
			}
			if (!edgeI.isDirected() && !properties.isEdgeRestricted(edgeI, true)) {
				costArray[j][i] = properties.getEdgeCost(edgeI, true);
				timeArray[j][i] = properties.getEdgeTime(edgeI, true);
				distArray[j][i] = properties.getEdgeDistance(edgeI, true);
			}
		}

		_toVertices = new Vertex[n];
		_fromVertices = new Vertex[n];
		for (Iterator<VertexI> e = graph.vertices(); e.hasNext();) {
			VertexI vertexI = e.next();
			int idx = vertexI.getIndex();
			double[] costRow = costArray[idx];
			double[] timeRow = timeArray[idx];
			double[] distRow = distArray[idx];
			Vertex vertex = new Vertex(idx, vertexI, costRow, timeRow, distRow);
			_vertexHashMap.put(vertex._key, vertex);
			vertex._fromIndex = vertex._toIndex = idx;
			_fromVertices[idx] = _toVertices[idx] = vertex;
		}

		_sizeOfEdges = 0;
		for (int i = 0; i < n; i++) {
			Vertex from = _fromVertices[i];
			for (int j = 0; j < n; j++) {
				if (from._edgeCosts[j] != Double.POSITIVE_INFINITY) {
					_sizeOfEdges++;
					from._outDegree++;
					_toVertices[j]._inDegree++;
				}
			}
		}

	}

	private void construct(MatrixI cost, MatrixI time, MatrixI distance, Vector<Object> vertexKeys,
			Vector<VertexI> vertexValues) throws DuplicateVertexException {
		int n = vertexKeys.size();
		Vertex[] vertices = new Vertex[n];
		for (int i = 0; i < n; i++)
			vertices[i] = new Vertex(i, vertexKeys.elementAt(i), vertexValues == null ? null
					: vertexValues.elementAt(i), null);
		try {
			construct(cost, time, distance, vertices, vertices);
		} catch (ParallelEdgeException e) {
			throw new DuplicateVertexException();
		}

	}

	private void construct(MatrixI cost, MatrixI time, MatrixI distance, VertexI[] fromVertices, VertexI[] toVertices)
			throws ParallelEdgeException {
		_isSymmetric = false;

		int cnt = 0;
		double[][] costArray = cost.getArray();
		double[][] timeArray = time == null ? null : time.getArray();
		double[][] distArray = distance == null ? null : distance.getArray();
		int nFrom = fromVertices.length;
		int nTo = toVertices.length;
		_theEdge = new Edge();
		_vertexHashMap = new HashMap<Object, VertexI>();

		_fromVertices = new Vertex[nFrom];
		for (int i = 0; i < nFrom; i++) {
			VertexI vertexI = fromVertices[i];
			Vertex vertex = (Vertex) _vertexHashMap.get(vertexI.getKey());
			if (vertex != null)
				throw new ParallelEdgeException("Parallel Edge From - " + vertexI.getKey().toString());
			double[] costRow = costArray[i];
			double[] timeRow = timeArray == null ? null : timeArray[i];
			double[] distRow = distArray == null ? null : distArray[i];
			vertex = new Vertex(cnt++, vertexI, costRow, timeRow, distRow);
			_vertexHashMap.put(vertex._key, vertex);
			vertex._fromIndex = i;
			_fromVertices[i] = vertex;
		}

		_toVertices = new Vertex[nTo];
		for (int j = 0; j < nTo; j++) {
			VertexI vertexI = toVertices[j];
			Vertex vertex = (Vertex) _vertexHashMap.get(vertexI.getKey());
			if (vertex != null) {
				if (vertex._toIndex != -1)
					throw new ParallelEdgeException("Parallel Edge To - " + vertexI.getKey().toString());
			} else {
				vertex = new Vertex(cnt++, vertexI, null, null, null);
				_vertexHashMap.put(vertex._key, vertex);
			}
			vertex._toIndex = j;
			_toVertices[j] = vertex;
		}

		_sizeOfEdges = 0;
		for (int i = 0; i < nFrom; i++) {
			Vertex from = _fromVertices[i];
			for (int j = 0; j < nTo; j++) {
				if (from._edgeCosts[j] != Double.POSITIVE_INFINITY) {
					_sizeOfEdges++;
					from._outDegree++;
					_toVertices[j]._inDegree++;
				}
			}
		}

	}

	public int getChangeCount() {
		return 0;
	}

	public int sizeOfVertices() {
		return _vertexHashMap.size();
	}

	public int sizeOfEdges() {
		return _sizeOfEdges;
	}

	public int sizeOfDirectedEdges() {
		return sizeOfEdges();
	}

	public boolean isSymmetric() {
		if (_symmetric != null)
			return _symmetric.booleanValue();
		return _isSymmetric;
	}

	public EdgeI getMutableEdge(VertexI from, VertexI to, Object edgeKey) {
		if (edgeKey != null)
			return null;
		if (to.getGraph() != this)
			throw new GraphError("The to vertex is not contained in this graph");
		if (from.getGraph() != this)
			throw new GraphError("The from vertex is not contained in this graph");
		return ((Vertex) from).getEdgeTo((Vertex) to, _theEdge);
	}

	public Iterator<VertexI> vertices() {
		return _vertexHashMap.values().iterator();
	}

	public VertexI getVertex(Object key) {
		return (VertexI) _vertexHashMap.get(key);
	}

	public EdgeI getEdge(VertexI from, VertexI to, Object edgeKey) {
		if (edgeKey != null)
			return null;
		if (to.getGraph() != this)
			throw new GraphError("The to vertex is not contained in this graph");
		if (from.getGraph() != this)
			throw new GraphError("The from vertex is not contained in this graph");
		return ((Vertex) from).getEdgeTo((Vertex) to, new Edge());
	}

}

