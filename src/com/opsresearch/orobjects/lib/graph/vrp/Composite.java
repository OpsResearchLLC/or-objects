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
import com.opsresearch.orobjects.lib.graph.VertexNotFoundException;

public class Composite extends RandomizableBase implements ConstructI {
	private ImproveI _improve;
	private ConstructI _construct;

	public Composite(ConstructI construct, ImproveI improve) {
		if (construct == null)
			throw new Error("The construction algorithm can not be null");
		_improve = improve;
		_construct = construct;

	}

	public Composite(ConstructI construct, ImproveI improve, GraphI graph) {
		if (construct == null)
			throw new Error("The construction algorithm can not be null");
		if (graph == null)
			throw new Error("The graph can not be null");
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

	public void setVehicleCost(double vehicleCost) {
		if (_improve != null)
			_improve.setVehicleCost(vehicleCost);
		_construct.setVehicleCost(vehicleCost);
	}

	public void setCostConstraint(double maxCostPerVehicle) {
		if (_improve != null)
			_improve.setCostConstraint(maxCostPerVehicle);
		_construct.setCostConstraint(maxCostPerVehicle);
	}

	public void setCapacityConstraint(double maxLoadPerVehicle) {
		if (_improve != null)
			_improve.setCapacityConstraint(maxLoadPerVehicle);
		_construct.setCapacityConstraint(maxLoadPerVehicle);

	}

	public void setEdgeKey(Object edgeKey) {
		if (_improve != null)
			_improve.setEdgeKey(edgeKey);
		_construct.setEdgeKey(edgeKey);
	}

	public double getCost() throws SolutionNotFoundException {
		return _improve != null ? _improve.getCost() : _construct.getCost();
	}

	public double[] getLoads() throws SolutionNotFoundException {
		return _improve != null ? _improve.getLoads() : _construct.getLoads();
	}

	public double[] getCosts() throws SolutionNotFoundException {
		return _improve != null ? _improve.getCosts() : _construct.getCosts();
	}

	public Vector<ElementI>[] getTours() throws SolutionNotFoundException {
		return _improve != null ? _improve.getTours() : _construct.getTours();
	}

	public double constructOutboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		double ret = _construct.constructOutboundTours(depotKey);
		if (_improve != null) {
			Vector<ElementI>[] tours = _construct.getTours();
			ret = _improve.improveOutboundTours(tours);
		}
		return ret;
	}

	public double constructInboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		double ret = _construct.constructInboundTours(depotKey);
		if (_improve != null) {
			Vector<ElementI>[] tours = _construct.getTours();
			ret = _improve.improveInboundTours(tours);
		}
		return ret;
	}

	public double constructClosedTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		double ret = _construct.constructClosedTours(depotKey);
		if (_improve != null) {
			Vector<ElementI>[] tours = _construct.getTours();
			_improve.improveClosedTours(tours);
		}
		return ret;
	}

	public void selectVertex(boolean[] select) {
		_construct.selectVertex(select);
	}

	public void selectVertex(boolean select) {
		_construct.selectVertex(select);
	}

	public void selectVertex(Object key, boolean select) throws VertexNotFoundException {
		_construct.selectVertex(key, select);
	}

}
