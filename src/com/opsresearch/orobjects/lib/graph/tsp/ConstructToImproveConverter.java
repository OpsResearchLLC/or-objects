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

import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.GraphError;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;

public class ConstructToImproveConverter implements ImproveI {
	private GraphI _graph;
	private ConstructI _construct;

	public ConstructToImproveConverter(ConstructI construct) {
		_construct = construct;
	}

	public ConstructToImproveConverter(ConstructI construct, GraphI graph) {
		_construct = construct;
		setGraph(graph);
	}

	public ConstructI getConstructAlgorithm() {
		return _construct;
	}

	public void setGraph(GraphI graph) {
		_graph = graph;
		_construct.setGraph(graph);
	}

	public void setProperties(PropertiesI properties) {
		_construct.setProperties(properties);
	}

	public void setEdgeKey(Object edgeKey) {
		_construct.setEdgeKey(edgeKey);
	}

	public double getCost() {
		return _construct.getCost();
	}

	public Vector<ElementI> getTour() {
		return _construct.getTour();
	}

	public Vector<ElementI> rotateClosedTour(Vector<ElementI> tour, Object key) throws VertexNotFoundException {
		return _construct.rotateClosedTour(tour, key);
	}

	private void select(Vector<ElementI> tour) {
		Iterator<ElementI> e = tour.iterator();
		VertexI vertex = (VertexI) e.next();
		boolean[] select = new boolean[_graph.sizeOfVertices()];
		select[vertex.getIndex()] = true;
		while (e.hasNext()) {
			e.next();
			vertex = (VertexI) e.next();
			select[vertex.getIndex()] = true;
		}
		_construct.selectVertex(select);
	}

	public double improveOpenTour(Vector<ElementI> tour) throws TourNotFoundException {
		if (_graph == null)
			throw new TourNotFoundException("The graph is not set.");
		if (tour.size() < 1)
			throw new TourNotFoundException("The tour to improve was empty");
		select(tour);
		_construct.constructOpenTour();
		return getCost();
	}

	public double improveOpenTour(Vector<ElementI> tour, boolean fixedOrigin, boolean fixedDestination)
			throws TourNotFoundException {
		if (_graph == null)
			throw new TourNotFoundException("The graph is not set.");
		if (tour.size() < 1)
			throw new TourNotFoundException("The tour to improve was empty");
		select(tour);
		Object from = ((VertexI) tour.firstElement()).getKey();
		Object to = ((VertexI) tour.lastElement()).getKey();
		try {
			if (fixedOrigin && fixedDestination)
				_construct.constructOpenTour(from, to);
			else if (fixedOrigin)
				_construct.constructOpenTourFrom(from);
			else if (fixedDestination)
				_construct.constructOpenTourTo(to);
			else
				_construct.constructOpenTour();
		} catch (VertexNotFoundException e) {
			throw new TourNotFoundException("Can't find a vertex in the tour by key.");
		}
		return getCost();
	}

	public double improveClosedTour(Vector<ElementI> tour) throws TourNotFoundException {
		if (_graph == null)
			throw new TourNotFoundException("The graph is not set.");
		if (tour.size() < 1)
			throw new TourNotFoundException("The tour to improve was empty");
		select(tour);
		_construct.constructClosedTour();
		return getCost();
	}

}
