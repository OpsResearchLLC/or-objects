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

public class BestOf extends ConstructBase implements ConstructI {
	private double _bestCost = Double.POSITIVE_INFINITY;
	private ImproveI _bestImprove = null;
	private ConstructI _bestConstruct = null;
	private Vector<ImproveI> _improve = new Vector<ImproveI>();
	private Vector<ConstructI> _construct = new Vector<ConstructI>();
	private Vector<Integer> _improveMax = new Vector<Integer>();
	private Vector<Integer> _improveMin = new Vector<Integer>();
	private Vector<Integer> _constructMin = new Vector<Integer>();
	private Vector<Integer> _constructMax = new Vector<Integer>();

	public BestOf() {

	}

	public BestOf(GraphI graph) {
		setGraph(graph);
	}

	public void addConstruct(ConstructI construct) {
		addConstruct(construct, 0, Integer.MAX_VALUE);
	}

	public void addConstruct(ConstructI construct, int minSize, int maxSize) {
		construct.setEdgeKey(_edgeKey);
		construct.setVehicleCost(_vehicleCost);
		construct.setCostConstraint(_maxCost);
		construct.setCapacityConstraint(_maxLoad);
		construct.setGraph(_graph);
		construct.setProperties(_properties);
		_construct.addElement(construct);
		_constructMin.addElement(new Integer(minSize));
		_constructMax.addElement(new Integer(maxSize));
	}

	public void addImprove(ImproveI improve) {
		addImprove(improve, 0, Integer.MAX_VALUE);
	}

	public void addImprove(ImproveI improve, int minSize, int maxSize) {
		improve.setEdgeKey(_edgeKey);
		improve.setVehicleCost(_vehicleCost);
		improve.setCostConstraint(_maxCost);
		improve.setCapacityConstraint(_maxLoad);
		improve.setGraph(_graph);
		improve.setProperties(_properties);
		_improve.addElement(improve);
		_improveMin.addElement(new Integer(minSize));
		_improveMax.addElement(new Integer(maxSize));
	}

	public void setGraph(GraphI graph) {
		super.setGraph(graph);
		for (int i = 0; i < _construct.size(); i++)
			((ConstructI) _construct.elementAt(i)).setGraph(graph);
		for (int i = 0; i < _improve.size(); i++)
			((ImproveI) _improve.elementAt(i)).setGraph(graph);
	}

	public void setProperties(PropertiesI properties) {
		super.setProperties(properties);
		for (int i = 0; i < _construct.size(); i++)
			((ConstructI) _construct.elementAt(i)).setProperties(properties);
		for (int i = 0; i < _improve.size(); i++)
			((ImproveI) _improve.elementAt(i)).setProperties(properties);
	}

	public void setEdgeKey(Object edgeKey) {
		super.setEdgeKey(edgeKey);
		for (int i = 0; i < _construct.size(); i++)
			((ConstructI) _construct.elementAt(i)).setEdgeKey(edgeKey);
		for (int i = 0; i < _improve.size(); i++)
			((ImproveI) _improve.elementAt(i)).setEdgeKey(edgeKey);
	}

	public void setVehicleCost(double vehicleCost) {
		super.setVehicleCost(vehicleCost);
		for (int i = 0; i < _construct.size(); i++)
			((ConstructI) _construct.elementAt(i)).setVehicleCost(vehicleCost);
		for (int i = 0; i < _improve.size(); i++)
			((ImproveI) _improve.elementAt(i)).setVehicleCost(vehicleCost);
	}

	public void setCostConstraint(double maxCostPerVehicle) {
		super.setCostConstraint(maxCostPerVehicle);
		for (int i = 0; i < _construct.size(); i++)
			((ConstructI) _construct.elementAt(i)).setCostConstraint(maxCostPerVehicle);
		for (int i = 0; i < _improve.size(); i++)
			((ImproveI) _improve.elementAt(i)).setCostConstraint(maxCostPerVehicle);
	}

	public void setCapacityConstraint(double maxLoadPerVehicle) {
		super.setCapacityConstraint(maxLoadPerVehicle);
		for (int i = 0; i < _construct.size(); i++)
			((ConstructI) _construct.elementAt(i)).setCapacityConstraint(maxLoadPerVehicle);
		for (int i = 0; i < _improve.size(); i++)
			((ImproveI) _improve.elementAt(i)).setCapacityConstraint(maxLoadPerVehicle);
	}

	public double constructOutboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		_bestCost = Double.POSITIVE_INFINITY;
		_bestImprove = null;
		_bestConstruct = null;
		int size = sizeOfSelected();
		for (int i = 0; i < _construct.size(); i++) {
			if (size < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (size > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(_selected);
			double cost = construct.constructOutboundTours(depotKey);
			if (cost < _bestCost) {
				_bestCost = cost;
				_bestConstruct = construct;
			}
		}
		if (_bestConstruct == null)
			throw new SolutionNotFoundException();
		Vector<ElementI>[] bestConstructTours = _bestConstruct.getTours();
		for (int i = 0; i < _improve.size(); i++) {
			if (size < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (size > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveOutboundTours(bestConstructTours);
			if (cost < _bestCost) {
				_bestCost = cost;
				_bestImprove = improve;
			}
		}
		return _bestCost;
	}

	public double constructInboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		_closed = false;
		_bestCost = Double.POSITIVE_INFINITY;
		_bestImprove = null;
		_bestConstruct = null;
		int size = sizeOfSelected();
		for (int i = 0; i < _construct.size(); i++) {
			if (size < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (size > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(_selected);
			double cost = construct.constructInboundTours(depotKey);
			if (cost < _bestCost) {
				_bestCost = cost;
				_bestConstruct = construct;
			}
		}
		if (_bestConstruct == null)
			throw new SolutionNotFoundException();
		Vector<ElementI>[] bestConstructTours = _bestConstruct.getTours();
		for (int i = 0; i < _improve.size(); i++) {
			if (size < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (size > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveInboundTours(bestConstructTours);
			if (cost < _bestCost) {
				_bestCost = cost;
				_bestImprove = improve;
			}
		}
		return _bestCost;
	}

	public double constructClosedTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		_closed = true;
		_bestCost = Double.POSITIVE_INFINITY;
		_bestImprove = null;
		_bestConstruct = null;
		int size = sizeOfSelected();
		for (int i = 0; i < _construct.size(); i++) {
			if (size < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (size > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(_selected);
			double cost = construct.constructClosedTours(depotKey);
			if (cost < _bestCost) {
				_bestCost = cost;
				_bestConstruct = construct;
			}
		}
		if (_bestConstruct == null)
			throw new SolutionNotFoundException();
		Vector<ElementI>[] bestConstructTours = _bestConstruct.getTours();
		for (int i = 0; i < _improve.size(); i++) {
			if (size < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (size > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveClosedTours(bestConstructTours);
			if (cost < _bestCost) {
				_bestCost = cost;
				_bestImprove = improve;
			}
		}
		return _bestCost;
	}

	public Vector<ElementI>[] getTours() throws SolutionNotFoundException {
		if (_bestImprove != null)
			_bestImprove.getTours();
		if (_bestConstruct == null)
			throw new VRPError("No solution has been created");
		return _bestConstruct.getTours();
	}

	public double getCost() throws SolutionNotFoundException {
		if (_bestImprove != null)
			_bestImprove.getCost();
		if (_bestConstruct == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _bestConstruct.getCost();
	}

	public double[] getLoads() throws SolutionNotFoundException {
		if (_bestImprove != null)
			_bestImprove.getLoads();
		if (_bestConstruct == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _bestConstruct.getLoads();
	}

	public double[] getCosts() throws SolutionNotFoundException {
		if (_bestImprove != null)
			_bestImprove.getCosts();
		if (_bestConstruct == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _bestConstruct.getCosts();
	}

}
