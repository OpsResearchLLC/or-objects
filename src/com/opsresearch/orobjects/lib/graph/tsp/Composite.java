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

import java.util.Vector;

import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.GraphError;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;

public class Composite implements ConstructI {

	private ImproveI _improve;
	private ConstructI _construct;

	public Composite(ConstructI construct, ImproveI improve) {
		_improve = improve;
		_construct = construct;

	}

	public Composite(ConstructI construct, ImproveI improve, GraphI graph) {
		_improve = improve;
		_construct = construct;
		setGraph(graph);
	}

	public ConstructI getConstructAlgorithm() {
		return _construct;
	}

	public ImproveI getImproveAlgorithm() {
		return _improve;
	}

	public void setGraph(GraphI graph) {
		if (_improve != null)
			_improve.setGraph(graph);
		_construct.setGraph(graph);
	}

	public void setProperties(PropertiesI properties) {
		if (_improve != null)
			_improve.setProperties(properties);
		_construct.setProperties(properties);
	}

	public void setEdgeKey(Object edgeKey) {
		if (_improve != null)
			_improve.setEdgeKey(edgeKey);
		_construct.setEdgeKey(edgeKey);
	}

	public double getCost() {
		return _improve != null ? _improve.getCost() : _construct.getCost();
	}

	public Vector<ElementI> getTour() {
		return _improve != null ? _improve.getTour() : _construct.getTour();
	}

	public double constructOpenTour(Object originKey, Object destinationKey)
			throws TourNotFoundException, VertexNotFoundException {
		double ret = _construct.constructOpenTour(originKey, destinationKey);
		if (_improve != null) {
			Vector<ElementI> tour = _construct.getTour();
			ret = _improve.improveOpenTour(tour, true, true);
		}
		return ret;
	}

	public double constructOpenTourFrom(Object originKey)
			throws TourNotFoundException, VertexNotFoundException {
		double ret = _construct.constructOpenTourFrom(originKey);
		if (_improve != null) {
			Vector<ElementI> tour = _construct.getTour();
			ret = _improve.improveOpenTour(tour, true, false);
		}
		return ret;
	}

	public double constructOpenTourTo(Object destinationKey)
			throws TourNotFoundException, VertexNotFoundException {
		double ret = _construct.constructOpenTourTo(destinationKey);
		if (_improve != null) {
			Vector<ElementI> tour = _construct.getTour();
			_improve.improveOpenTour(tour, false, true);
		}
		return ret;
	}

	public double constructOpenTour() throws TourNotFoundException {
		double ret = _construct.constructOpenTour();
		if (_improve != null) {
			Vector<ElementI> tour = _construct.getTour();
			ret = _improve.improveOpenTour(tour);
		}
		return ret;
	}

	public double constructClosedTour() throws TourNotFoundException {
		double ret = _construct.constructClosedTour();
		if (_improve != null) {
			Vector<ElementI> tour = _construct.getTour();
			ret = _improve.improveClosedTour(tour);
		}
		return ret;
	}

	public void selectVertex(boolean[] select) {
		_construct.selectVertex(select);
	}

	public void selectVertex(boolean select) {
		_construct.selectVertex(select);
	}

	public void selectVertex(Object key, boolean select)
			throws VertexNotFoundException {
		_construct.selectVertex(key, select);
	}

	public Vector<ElementI> rotateClosedTour(Vector<ElementI> tour, Object key)
			throws VertexNotFoundException {
		return _construct.rotateClosedTour(tour, key);
	}

}
