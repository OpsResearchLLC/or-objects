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

public class Operations {
	public static GraphI union(GraphI graph1, GraphI graph2) {
		SparseGraph rslt = new SparseGraph();
		try {
			for (Iterator<VertexI> e = graph1.vertices(); e.hasNext();) {
				rslt.addVertex(e.next());
			}
			for (Iterator<EdgeI> e = graph1.edges(); e.hasNext();) {
				rslt.addEdge((EdgeI) e.next());
			}
			for (Iterator<VertexI> e = graph2.vertices(); e.hasNext();) {
				VertexI vertex = (VertexI) e.next();
				if (rslt.getVertex(vertex.getKey()) == null)
					rslt.addVertex(vertex);
			}
			for (Iterator<EdgeI> e = graph2.edges(); e.hasNext();) {
				EdgeI edge = (EdgeI) e.next();
				if (rslt.getEdge(edge) == null)
					rslt.addEdge(edge);
			}
		} catch (DuplicateEdgeException e) {
		} catch (DuplicateVertexException e) {
		} catch (VertexNotFoundException e) {
		}
		return rslt;
	}

	public static GraphI intersection(GraphI graph1, GraphI graph2) {
		SparseGraph rslt = new SparseGraph();
		try {
			for (Iterator<VertexI> e = graph1.vertices(); e.hasNext();) {
				VertexI vertex = (VertexI) e.next();
				if (graph2.getVertex(vertex.getKey()) != null)
					rslt.addVertex(vertex);
			}
			for (Iterator<EdgeI> e = graph1.edges(); e.hasNext();) {
				EdgeI edge = (EdgeI) e.next();
				if (graph2.getEdge(edge) != null)
					rslt.addEdge(edge);
			}
		} catch (DuplicateEdgeException e) {
		} catch (DuplicateVertexException e) {
		} catch (VertexNotFoundException e) {
		}
		return rslt;
	}

	public static GraphI ringSum(GraphI graph1, GraphI graph2) {
		SparseGraph rslt = new SparseGraph();
		try {
			for (Iterator<EdgeI> e = graph1.edges(); e.hasNext();) {
				EdgeI edge = (EdgeI) e.next();
				if (graph2.getEdge(edge) == null) {
					if (rslt.getVertex(edge.getFromVertex().getKey()) == null)
						rslt.addVertex(edge.getFromVertex());
					if (rslt.getVertex(edge.getToVertex().getKey()) == null)
						rslt.addVertex(edge.getToVertex());
					rslt.addEdge(edge);
				}
			}
			for (Iterator<EdgeI> e = graph2.edges(); e.hasNext();) {
				EdgeI edge = (EdgeI) e.next();
				if (graph1.getEdge(edge) == null) {
					if (rslt.getVertex(edge.getFromVertex().getKey()) == null)
						rslt.addVertex(edge.getFromVertex());
					if (rslt.getVertex(edge.getToVertex().getKey()) == null)
						rslt.addVertex(edge.getToVertex());
					rslt.addEdge(edge);
				}
			}
		} catch (DuplicateEdgeException e) {
		} catch (DuplicateVertexException e) {
		} catch (VertexNotFoundException e) {
		}
		return rslt;
	}

	class EdgeIterator implements Iterator<EdgeI> {
		private EdgeI _edge;
		private Iterator<EdgeI> _edges;
		private Iterator<VertexI> _vertices;

		EdgeIterator(GraphI graph) {
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
				for (_edges = ((VertexI) _vertices.next()).outEdges(); _edges.hasNext(); ) {
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

}
