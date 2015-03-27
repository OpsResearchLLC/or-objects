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

package com.opsresearch.orobjects.lib.graph.color;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.graph.EdgeI;
import com.opsresearch.orobjects.lib.graph.GraphError;
import com.opsresearch.orobjects.lib.graph.GraphException;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.InvalidGraphError;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.util.QuickSort;

public class WelshPowell implements ColoringI {

	static class CmpVert implements CompareI {
		public int compare(Object a, Object b) {
			int ai = ((Vertex) a)._degree;
			int bi = ((Vertex) b)._degree;
			return ai == bi ? CompareI.EQUAL : ai < bi ? CompareI.LESS : CompareI.GREATER;
		}
	}

	static class Vertex {
		private VertexI _vertex;
		private int _color;
		private int _degree;

		Vertex(VertexI vertex, int degree) {
			_color = -1;
			_vertex = vertex;
			_degree = degree;
		}
	}

	static class CmpEdge implements CompareI {
		public int compare(Object a, Object b) {
			int ai = ((Edge) a)._degree;
			int bi = ((Edge) b)._degree;
			return ai == bi ? CompareI.EQUAL : ai < bi ? CompareI.LESS : CompareI.GREATER;
		}
	}

	static class Edge {
		private EdgeI _edge;
		private int _color;
		private int _degree;

		Edge(EdgeI edge, int degree) {
			_color = -1;
			_edge = edge;
			_degree = degree;
		}
	}

	private int _sizeOfEdgeColors;
	private int _sizeOfVertexColors;
	private int[] _edgeColors;
	private int[] _vertexColors;
	private GraphI _graph;
	private boolean _edgesColored;
	private boolean _verticesColored;

	public WelshPowell(GraphI graph) {
		_graph = graph;
		_edgesColored = false;
		_verticesColored = false;

	}

	public int colorVertices() {
		_sizeOfVertexColors = 0;
		int size = _graph.sizeOfVertices();
		if (size == 0)
			return 0;

		Object[] verts = new Object[size];
		Vertex[] index = new Vertex[size];

		for (Iterator<VertexI> vertices = _graph.vertices(); vertices.hasNext(); ) {
			VertexI vert = (VertexI) vertices.next();
			int idx = vert.getIndex();
			verts[idx] = index[idx] = new Vertex(vert, vert.getInDegree() + vert.getOutDegree());
		}

		new QuickSort<Vertex>(true, new CmpVert()).sort(verts);

		boolean more = true;
		for (int j = 0; more && j < size; j++) {
			more = false;
			for (int i = 0; i < size; i++) {
				Vertex vertex = (Vertex) verts[i];
				if (vertex._color != -1)
					continue;
				VertexI vertexI = vertex._vertex;
				look: {
					for (Iterator<EdgeI> edges = vertexI.inEdges(); edges.hasNext(); ) {
						VertexI v = edges.next().getFromVertex();
						if (index[v.getIndex()]._color == _sizeOfVertexColors) {
							more = true;
							break look;
						}
					}
					for (Iterator<EdgeI> edges = vertexI.outEdges(); edges.hasNext(); ) {
						VertexI v = edges.next().getToVertex();
						if (index[v.getIndex()]._color == _sizeOfVertexColors) {
							more = true;
							break look;
						}
					}
					vertex._color = _sizeOfVertexColors;
				}
			}
			_sizeOfVertexColors++;
		}
		_verticesColored = true;
		_vertexColors = new int[size];
		for (int i = 0; i < size; i++)
			_vertexColors[i] = ((Vertex) index[i])._color;
		return _sizeOfVertexColors;
	}

	public int colorEdges() {
		_sizeOfEdgeColors = 0;
		int size = _graph.sizeOfEdges();
		if (size == 0)
			return 0;

		Edge[] edges = new Edge[size];
		Edge[] index = new Edge[size];

		for (Iterator<EdgeI> edges1 = _graph.edges(); edges1.hasNext(); ) {
			EdgeI edge = edges1.next();
			int idx = edge.getIndex();
			VertexI to = edge.getToVertex();
			VertexI from = edge.getFromVertex();
			int degree = to.getInDegree() + to.getOutDegree() + from.getInDegree() + from.getOutDegree() - 2;
			edges[idx] = index[idx] = new Edge(edge, degree);
		}

		new QuickSort<EdgeI>(true, new CmpEdge()).sort(edges);

		boolean more = true;
		for (int j = 0; more && j < size; j++) {
			more = false;
			for (int i = 0; i < size; i++) {
				Edge edge = (Edge) edges[i];
				if (edge._color != -1)
					continue;
				EdgeI edgeI = edge._edge;
				look: {
					VertexI v = edgeI.getToVertex();
					for (Iterator<EdgeI> edges1 = v.inEdges(); edges1.hasNext(); ) {
						EdgeI e = edges1.next();
						if (index[e.getIndex()]._color == _sizeOfEdgeColors) {
							more = true;
							break look;
						}
					}
					for (Iterator<EdgeI> edges1 = v.outEdges(); edges1.hasNext(); ) {
						EdgeI e = edges1.next();
						if (index[e.getIndex()]._color == _sizeOfEdgeColors) {
							more = true;
							break look;
						}
					}

					v = edgeI.getFromVertex();
					for (Iterator<EdgeI> edges1 = v.inEdges(); edges1.hasNext(); ) {
						EdgeI e = edges1.next();
						if (index[e.getIndex()]._color == _sizeOfEdgeColors) {
							more = true;
							break look;
						}
					}
					for (Iterator<EdgeI> edges1 = v.outEdges(); edges1.hasNext(); ) {
						EdgeI e = edges1.next();
						if (index[e.getIndex()]._color == _sizeOfEdgeColors) {
							more = true;
							break look;
						}
					}
					edge._color = _sizeOfEdgeColors;
				}
			}
			_sizeOfEdgeColors++;
		}
		_edgesColored = true;
		_edgeColors = new int[size];
		for (int i = 0; i < size; i++)
			_edgeColors[i] = ((Edge) index[i])._color;
		return _sizeOfEdgeColors;
	}

	public int sizeOfVertexColors() throws GraphException {
		if (!_verticesColored)
			throw new GraphException("The vertices have not been colored");
		if (_vertexColors.length != _graph.sizeOfVertices())
			throw new InvalidGraphError("The number of vertices in the graph has changed since they were colored");
		return _sizeOfVertexColors;
	}

	public int sizeOfEdgeColors() throws GraphException {
		if (!_edgesColored)
			throw new GraphException("The edges have not been colored");
		if (_edgeColors.length != _graph.sizeOfEdges())
			throw new InvalidGraphError("The number of edges in the graph has changed since they were colored");
		return _sizeOfEdgeColors;
	}

	public int getVertexColor(VertexI vertex) throws GraphException {
		if (!_verticesColored)
			throw new GraphException("The vertices have not been colored");
		if (vertex.getGraph() != _graph)
			throw new GraphError("The vertex is not owned by the graph");
		if (_vertexColors.length != _graph.sizeOfVertices())
			throw new InvalidGraphError("The number of vertices in the graph has changed since they were colored");
		int idx = vertex.getIndex();
		if (idx >= _vertexColors.length)
			throw new InvalidGraphError("The number of vertices in the graph has changed since the edge was obtained.");
		return _vertexColors[idx];
	}

	public int getEdgeColor(EdgeI edge) throws GraphException {
		if (!_edgesColored)
			throw new GraphException("The edges have not been colored");
		if (edge.getGraph() != _graph)
			throw new GraphError("The edge is not owned by the graph");
		if (_edgeColors.length != _graph.sizeOfEdges())
			throw new InvalidGraphError("The number of edges in the graph has changed since they were colored.");
		int idx = edge.getIndex();
		if (idx >= _edgeColors.length)
			throw new InvalidGraphError("The number of edges in the graph has changed since the edge was obtained.");
		return _edgeColors[idx];
	}

	public int[] getVertexColors() throws GraphException {
		if (!_verticesColored)
			throw new GraphException("The vertices have not been colored");
		if (_vertexColors.length != _graph.sizeOfVertices())
			throw new InvalidGraphError("The number of vertices in the graph has changed since they were colored");
		int[] colors = new int[_vertexColors.length];
		for (int i = 0; i < _vertexColors.length; i++)
			colors[i] = _vertexColors[i];
		return colors;
	}

	public int[] getEdgeColors() throws GraphException {
		if (!_edgesColored)
			throw new GraphException("The edges have not been colored");
		if (_edgeColors.length != _graph.sizeOfEdges())
			throw new InvalidGraphError("The number of edges in the graph has changed since they were colored");
		int[] colors = new int[_edgeColors.length];
		for (int i = 0; i < _edgeColors.length; i++)
			colors[i] = _edgeColors[i];
		return colors;
	}

}
