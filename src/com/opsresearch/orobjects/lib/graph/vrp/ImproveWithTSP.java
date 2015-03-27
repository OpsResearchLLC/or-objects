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

package com.opsresearch.orobjects.lib.graph.vrp;

import java.util.Vector;

import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.tsp.ConstructToImproveConverter;

public class ImproveWithTSP extends ImproveBase implements ImproveI {
	private double _cost = 0;
	private double[] _costs = null;
	private Vector<ElementI>[] _tours = null;
	private com.opsresearch.orobjects.lib.graph.tsp.ImproveI _improve = null;

	public ImproveWithTSP(com.opsresearch.orobjects.lib.graph.tsp.ConstructI construct) {
		_improve = new ConstructToImproveConverter(construct);
		setEdgeKey(_edgeKey);
	}

	public ImproveWithTSP(com.opsresearch.orobjects.lib.graph.tsp.ImproveI improve) {
		_improve = improve;
		setEdgeKey(_edgeKey);
		setProperties(_properties);
	}

	@SuppressWarnings("unchecked")
	public double improveOutboundTours(Vector<ElementI>[] tours) throws SolutionNotFoundException {
		_cost = 0.0;
		_costs = new double[tours.length];
		_tours = null;
		Vector<ElementI>[] newTours = new Vector[tours.length];
		if (_graph == null)
			throw new SolutionNotFoundException("The graph is not set");
		for (int i = 0; i < tours.length; i++) {
			try {
				_cost += _costs[i] = _vehicleCost + _improve.improveOpenTour(tours[i], true, false);
				newTours[i] = _improve.getTour();
			} catch (com.opsresearch.orobjects.lib.graph.tsp.TourNotFoundException e) {
				throw new SolutionNotFoundException("From TSP: " + e.getMessage());
			}
		}
		_tours = newTours;
		return getCost();
	}

	@SuppressWarnings("unchecked")
	public double improveInboundTours(Vector<ElementI>[] tours) throws SolutionNotFoundException {
		_cost = 0.0;
		_costs = new double[tours.length];
		_tours = null;
		Vector<ElementI>[] newTours = new Vector[tours.length];
		if (_graph == null)
			throw new SolutionNotFoundException("The graph is not set");
		for (int i = 0; i < tours.length; i++) {
			try {
				_cost += _costs[i] = _vehicleCost + _improve.improveOpenTour(tours[i], false, true);
				newTours[i] = _improve.getTour();
			} catch (com.opsresearch.orobjects.lib.graph.tsp.TourNotFoundException e) {
				throw new SolutionNotFoundException("From TSP: " + e.getMessage());
			}
		}
		_tours = newTours;
		return getCost();
	}

	@SuppressWarnings("unchecked")
	public double improveClosedTours(Vector<ElementI>[] tours) throws SolutionNotFoundException {
		_cost = 0.0;
		_costs = new double[tours.length];
		_tours = null;
		Vector<ElementI>[] newTours = new Vector[tours.length];
		if (_graph == null)
			throw new SolutionNotFoundException("The graph is not set");
		for (int i = 0; i < tours.length; i++) {
			try {
				_cost += _costs[i] = _vehicleCost + _improve.improveClosedTour(tours[i]);
				newTours[i] = _improve.getTour();
			} catch (com.opsresearch.orobjects.lib.graph.tsp.TourNotFoundException e) {
				throw new SolutionNotFoundException("From TSP: " + e.getMessage());
			}
		}
		_tours = newTours;
		return getCost();
	}

	public void setGraph(GraphI graph) {
		super.setGraph(graph);
		_improve.setGraph(graph);
	}

	public void setEdgeKey(Object edgeKey) {
		super.setEdgeKey(edgeKey);
		_improve.setEdgeKey(edgeKey);
	}

	public void setProperties(PropertiesI properties) {
		if (_improve != null)
			_improve.setProperties(properties);
	}

	public Vector<ElementI>[] getTours() throws SolutionNotFoundException {
		if (_tours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return VRPBase.copyTours(_tours);
	}

	public double getCost() throws SolutionNotFoundException {
		if (_tours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _cost;
	}

	public double[] getCosts() throws SolutionNotFoundException {
		if (_tours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _costs;
	}

	public double[] getLoads() throws SolutionNotFoundException {
		if (_tours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return getLoads(_tours);
	}

}
