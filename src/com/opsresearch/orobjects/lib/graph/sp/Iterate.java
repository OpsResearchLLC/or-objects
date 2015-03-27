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

package com.opsresearch.orobjects.lib.graph.sp;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.cont.OrderedSet;
import com.opsresearch.orobjects.lib.graph.AddEdgeI;
import com.opsresearch.orobjects.lib.graph.AddI;
import com.opsresearch.orobjects.lib.graph.AddVertexI;
import com.opsresearch.orobjects.lib.graph.DuplicateEdgeException;
import com.opsresearch.orobjects.lib.graph.DuplicateVertexException;
import com.opsresearch.orobjects.lib.graph.EdgeValue;
import com.opsresearch.orobjects.lib.graph.EdgeValueI;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.InvalidPropertyException;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;
import com.opsresearch.orobjects.lib.real.matrix.SizableMatrixI;

public class Iterate implements AllPairsI {
	private GraphI _graph;
	private OrderedSet<Object> _origins;
	private OrderedSet<Object> _destinations;
	private SingleVertexI _algorithm;

	public Iterate(GraphI graph, SingleVertexI algorithm) {
		_graph = graph;
		_algorithm = algorithm;
		_init();
	}

	private void _init() {
		_origins = new OrderedSet<Object>(_graph.sizeOfVertices());
		_destinations = new OrderedSet<Object>(_graph.sizeOfVertices());
	}

	public void setProperties(PropertiesI properties) {
		_algorithm.setProperties(properties);
	}

	public void setDestination(boolean isDestination) {
		if (!isDestination) {
			_destinations.removeAllElements();
		} else if (_destinations.size() != _graph.sizeOfVertices()) {
			for (Iterator<VertexI> e = _graph.vertices(); e.hasNext(); ) {
				VertexI vertex = (VertexI) e.next();
				_destinations.addElement(vertex.getKey());
			}
		}
	}

	public void setDestination(Object key, boolean isDestination) throws VertexNotFoundException {
		if (!isDestination)
			_destinations.removeElement(key);
		else {
			VertexI vertex = _graph.getVertex(key);
			if (vertex == null)
				throw new VertexNotFoundException();
			_destinations.addElement(vertex.getKey());
		}
	}

	public VertexI[] getDestinationVertices() {
		VertexI[] vertices = new VertexI[_destinations.size()];
		Iterator<Object> e = _destinations.elements();
		Iterator<Integer> i = _destinations.indices();
		while (e.hasNext())
			vertices[((Integer) i.next()).intValue()] = _graph.getVertex(e.next());
		return vertices;
	}

	public void setOrigin(boolean isOrigin) {
		if (!isOrigin) {
			_origins.removeAllElements();
		} else if (_origins.size() != _graph.sizeOfVertices()) {
			for (Iterator<VertexI> e = _graph.vertices(); e.hasNext(); ) {
				VertexI vertex = e.next();
				_origins.addElement(vertex.getKey());
			}
		}
	}

	public void setOrigin(Object key, boolean isOrigin) throws VertexNotFoundException {
		if (!isOrigin)
			_origins.removeElement(key);
		else {
			VertexI vertex = _graph.getVertex(key);
			if (vertex == null)
				throw new VertexNotFoundException();
			_origins.addElement(vertex.getKey());
		}
	}

	public VertexI[] getOriginVertices() {
		VertexI[] vertices = new VertexI[_origins.size()];
		Iterator<Object> e = _origins.elements();
		Iterator<Integer> i = _origins.indices();
		while (e.hasNext())
			vertices[((Integer) i.next()).intValue()] = _graph.getVertex(e.next());
		return vertices;
	}

	public void fillMatrix(SizableMatrixI cost, SizableMatrixI time, SizableMatrixI distance)
			throws InvalidPropertyException, VertexNotFoundException {
		fillMatrix(cost, time, distance, Integer.MAX_VALUE, 0);
	}

	public void fillMatrix(SizableMatrixI cost, SizableMatrixI time, SizableMatrixI distance, int maxPathsOut,
			int maxPathsIn) throws InvalidPropertyException, VertexNotFoundException {
		if (cost != null)
			cost.setSize(_origins.size(), _destinations.size());
		if (time != null)
			time.setSize(_origins.size(), _destinations.size());
		if (distance != null)
			distance.setSize(_origins.size(), _destinations.size());

		if (cost != null)
			cost.setElements(Double.POSITIVE_INFINITY);
		if (time != null)
			time.setElements(Double.POSITIVE_INFINITY);
		if (distance != null)
			distance.setElements(Double.POSITIVE_INFINITY);

		for (Iterator<Object> o = _origins.elements(); o.hasNext(); ) {
			Object originKey = o.next();
			int row = _origins.getIndex(originKey);
			int col = _destinations.getIndex(originKey);
			if (row >= 0 && col >= 0) {
				if (cost != null)
					cost.setElementAt(row, col, 0);
				if (time != null)
					time.setElementAt(row, col, 0);
				if (distance != null)
					distance.setElementAt(row, col, 0);
			}
		}

		if (maxPathsOut > 0) {
			_algorithm.setMaxPaths(maxPathsOut);
			_algorithm.setCandidate(false);
			for (Iterator<Object> o = _destinations.elements(); o.hasNext(); ) {
				_algorithm.setCandidate(o.next(), true);
			}
			for (Iterator<Object> d = _origins.elements(); d.hasNext(); ) {
				Object originKey = d.next();
				int row = _origins.getIndex(originKey);
				_algorithm.generatePathsFrom(originKey);
				for (Iterator<VertexI> c = _algorithm.candidates(); c.hasNext(); ) {
					VertexI destination = (VertexI) c.next();
					int col = _destinations.getIndex(destination.getKey());
					EdgeValueI edgeValue = _algorithm.getEdgeValue(destination);
					if (cost != null)
						cost.setElementAt(row, col, edgeValue.getCost(false));
					if (time != null)
						time.setElementAt(row, col, edgeValue.getTime(false));
					if (distance != null)
						distance.setElementAt(row, col, edgeValue.getDistance(false));
				}
			}
		}

		if (maxPathsIn > 0) {
			_algorithm.setMaxPaths(maxPathsIn);
			_algorithm.setCandidate(false);
			for (Iterator<Object> d = _origins.elements(); d.hasNext(); ) {
				_algorithm.setCandidate(d.next(), true);
			}
			for (Iterator<Object> o = _destinations.elements(); o.hasNext(); ) {
				Object destinationKey = o.next();
				int col = _destinations.getIndex(destinationKey);
				_algorithm.generatePathsTo(destinationKey);
				for (Iterator<VertexI> c = _algorithm.candidates(); c.hasNext(); ) {
					VertexI origin = (VertexI) c.next();
					EdgeValueI edgeValue = _algorithm.getEdgeValue(origin);
					int row = _origins.getIndex(origin.getKey());
					cost.setElementAt(row, col, edgeValue.getCost(false));
					if (time != null)
						time.setElementAt(row, col, edgeValue.getTime(false));
					if (distance != null)
						distance.setElementAt(row, col, edgeValue.getDistance(false));

				}
			}
		}
	}

	public AddI fillGraph(AddI graph) throws InvalidPropertyException, VertexNotFoundException, DuplicateEdgeException,
			DuplicateVertexException {
		return fillGraph(graph, Integer.MAX_VALUE, 0);
	}

	public AddI fillGraph(AddI graph, int maxPathsOut, int maxPathsIn) throws InvalidPropertyException,
			VertexNotFoundException, DuplicateEdgeException, DuplicateVertexException {
		for (Iterator<Object> o = _origins.elements(); o.hasNext(); ) {
			Object key = o.next();
			if (graph.getVertex(key) == null)
				((AddVertexI) graph).addVertex(_graph.getVertex(key));
		}

		for (Iterator<Object> d = _destinations.elements(); d.hasNext(); ) {
			Object key = d.next();
			if (graph.getVertex(key) == null)
				((AddVertexI) graph).addVertex(_graph.getVertex(key));
		}

		if (maxPathsOut > 0) {
			_algorithm.setMaxPaths(maxPathsOut);
			_algorithm.setCandidate(false);
			for (Iterator<Object> o = _destinations.elements(); o.hasNext(); ) {
				_algorithm.setCandidate(o.next(), true);
			}
			for (Iterator<Object> o = _origins.elements(); o.hasNext(); ) {
				Object originKey = o.next();
				_algorithm.generatePathsFrom(originKey);
				for (Iterator<VertexI> c = _algorithm.candidates(); c.hasNext(); ) {
					VertexI destination = c.next();
					((AddEdgeI) graph).addEdge(originKey, destination.getKey(),
							new EdgeValue(_algorithm.getEdgeValue(destination)));
				}
			}
		}

		if (maxPathsIn > 0) {
			_algorithm.setMaxPaths(maxPathsIn);
			_algorithm.setCandidate(false);
			for (Iterator<Object> d = _origins.elements(); d.hasNext(); ) {
				_algorithm.setCandidate(d.next(), true);
			}
			for (Iterator<Object> d = _destinations.elements(); d.hasNext(); ) {
				Object destinationKey = d.next();
				_algorithm.generatePathsTo(destinationKey);
				for (Iterator<VertexI> c = _algorithm.candidates(); c.hasNext(); ) {
					VertexI origin = c.next();
					((AddEdgeI) graph).addEdge(origin.getKey(), destinationKey,
							new EdgeValue(_algorithm.getEdgeValue(origin)));
				}
			}
		}

		return graph;
	}

}
