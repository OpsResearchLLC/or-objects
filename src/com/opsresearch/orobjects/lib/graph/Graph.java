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

public abstract class Graph implements GraphI, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	private class EdgeIterator implements Iterator<EdgeI> {
		private EdgeI _edge;
		private boolean _mutable;
		private Iterator<EdgeI> _edges;
		private Iterator<VertexI> _vertices;

		EdgeIterator(GraphI graph, boolean mutable) {
			_edges = null;
			_vertices = graph.vertices();
			findNext();
		}

		void findNext() {
			_edge = null;
			if (_edges != null && _edges.hasNext()) {
				_edge = (EdgeI) _edges.next();
				return;
			}
			while (_vertices.hasNext()) {
				VertexI vertex = (VertexI) _vertices.next();
				_edges = _mutable ? vertex.mutableOutEdges() : vertex
						.outEdges();
				if (_edges.hasNext()) {
					_edge = (EdgeI) _edges.next();
					return;
				}
			}
		}

		public boolean hasNext() {
			return _edge != null;
		}

		public EdgeI next() {
			if (_edge == null)
				return null;
			EdgeI ret = _edge;
			_edge = null;
			findNext();
			return ret;
		}

		@Override
		public void remove() {
		}
	}


	protected Boolean _symmetric;

	Graph() {
		_symmetric = null;
	}

	public void setSymmetric(Boolean symmetric) {
		_symmetric = symmetric;
	}

	public EdgeI getEdge(EdgeI edge) {
		return getEdge(edge.getFromVertex().getKey(), edge.getToVertex()
				.getKey(), edge.getKey());
	}

	public EdgeI getEdge(Object fromKey, Object toKey, Object key) {
		VertexI from = getVertex(fromKey);
		if (from == null)
			return null;
		VertexI to = getVertex(toKey);
		if (to == null)
			return null;
		return getEdge(from, to, key);
	}

	public Iterator<EdgeI> edges() {
		return new EdgeIterator(this, false);
	}

	public Iterator<EdgeI> mutableEdges() {
		return new EdgeIterator(this, true);
	}

	public String toString(String cls) {
		String ret = "";
		ret += "------------------------------------\n";
		ret += cls + ": " + sizeOfVertices() + " vertices, " + sizeOfEdges()
				+ " edges\n";
		ret += "------------------------------------\n";
		for (Iterator<VertexI> e = vertices(); e.hasNext(); )
			ret += e.next() + "\n";
		for (Iterator<EdgeI> e = edges(); e.hasNext(); )
			ret += e.next() + "\n";
		ret += "\n";
		return ret;
	}


	public boolean isSubsetOf(GraphI graph) {
		if (sizeOfEdges() > graph.sizeOfEdges())
			return false;
		if (sizeOfVertices() > graph.sizeOfVertices())
			return false;

		for (Iterator<VertexI> e = vertices(); e.hasNext();) {
			VertexI vertex = (VertexI) e.next();
			if (graph.getVertex(vertex.getKey()) == null)
				return false;
		}
		for (Iterator<EdgeI> e = edges(); e.hasNext();) {
			EdgeI edge = (EdgeI) e.next();
			if (graph.getEdge(edge) == null)
				return false;
		}
		return true;
	}

	public boolean equals(Object o) {
		if (!(o instanceof GraphI))
			return false;
		GraphI graph = (GraphI) o;
		if (sizeOfEdges() != graph.sizeOfEdges())
			return false;
		if (sizeOfVertices() != graph.sizeOfVertices())
			return false;
		return isSubsetOf(graph);
	}

}
