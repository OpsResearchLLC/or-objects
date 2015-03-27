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
import com.opsresearch.orobjects.lib.random.RandomI;

public class Randomizer implements ConstructI {
	private Vector<Integer> _counts = new Vector<Integer>();
	private Vector<Integer> _strengths = new Vector<Integer>();
	private RandomizableI _randomizable;
	private double _bestCost;
	private double[] _bestCosts = null;
	private double[] _bestLoads = null;
	private Vector<ElementI>[] _bestOfTours = null;

	public Randomizer(RandomizableI randomizable) throws VRPException {
		_randomizable = randomizable;
		if (!(randomizable instanceof ConstructI))
			throw new VRPException("The argument 'randomizable' must implement 'ConstructI'.");
	}

	public void addIterations(int count, int strength) {
		if (count <= 0)
			throw new VRPError("The count must greater than zero");
		_counts.addElement(new Integer(count));
		_strengths.addElement(new Integer(strength));
	}

	public void setRandom(RandomI random) {
		_randomizable.setRandom(random);
	}

	public RandomI getRandom() {
		return _randomizable.getRandom();
	}

	public void selectVertex(boolean[] select) {
		if (!(_randomizable instanceof ConstructI))
			throw new VRPError("The randomized algorithm doesn't implement 'ConstructI'.");
		((ConstructI) _randomizable).selectVertex(select);
	}

	public void selectVertex(boolean select) {
		if (!(_randomizable instanceof ConstructI))
			throw new VRPError("The randomized algorithm doesn't implement 'ConstructI'.");
		((ConstructI) _randomizable).selectVertex(select);
	}

	public void selectVertex(Object key, boolean select) throws VertexNotFoundException {
		if (!(_randomizable instanceof ConstructI))
			throw new VRPError("The randomized algorithm doesn't implement 'ConstructI'.");
		((ConstructI) _randomizable).selectVertex(key, select);
	}

	public double constructOutboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		if (!(_randomizable instanceof ConstructI))
			throw new VRPError("The randomized algorithm doesn't implement 'ConstructI'.");
		ConstructI construct = (ConstructI) _randomizable;
		_bestCost = Double.POSITIVE_INFINITY;
		_bestCosts = null;
		_bestLoads = null;
		_bestOfTours = null;
		if (_strengths.size() <= 0)
			throw new SolutionNotFoundException("The randomizer has no iterations");
		for (int i = 0; i < _strengths.size(); i++) {
			int count = ((Integer) _counts.elementAt(i)).intValue();
			int strength = ((Integer) _strengths.elementAt(i)).intValue();
			_randomizable.setStrength(strength);
			for (int it = 0; it < count; it++) {
				double cost = construct.constructOutboundTours(depotKey);
				if (cost < _bestCost) {
					_bestCost = cost;
					_bestCosts = construct.getCosts();
					_bestLoads = construct.getLoads();
					_bestOfTours = construct.getTours();
				}
			}
		}
		return _bestCost;
	}

	public double constructInboundTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		if (!(_randomizable instanceof ConstructI))
			throw new VRPError("The randomized algorithm doesn't implement 'ConstructI'.");
		ConstructI construct = (ConstructI) _randomizable;
		_bestCost = Double.POSITIVE_INFINITY;
		_bestCosts = null;
		_bestLoads = null;
		_bestOfTours = null;
		if (_strengths.size() <= 0)
			throw new SolutionNotFoundException("The randomizer has no iterations");
		for (int i = 0; i < _strengths.size(); i++) {
			int count = ((Integer) _counts.elementAt(i)).intValue();
			int strength = ((Integer) _strengths.elementAt(i)).intValue();
			_randomizable.setStrength(strength);
			for (int it = 0; it < count; it++) {
				double cost = construct.constructInboundTours(depotKey);
				if (cost < _bestCost) {
					_bestCost = cost;
					_bestCosts = construct.getCosts();
					_bestLoads = construct.getLoads();
					_bestOfTours = construct.getTours();
				}
			}
		}
		return _bestCost;
	}

	public double constructClosedTours(Object depotKey) throws SolutionNotFoundException, VertexNotFoundException {
		if (!(_randomizable instanceof ConstructI))
			throw new VRPError("The randomized algorithm doesn't implement 'ConstructI'.");
		ConstructI construct = (ConstructI) _randomizable;
		_bestCost = Double.POSITIVE_INFINITY;
		_bestCosts = null;
		_bestLoads = null;
		_bestOfTours = null;
		if (_strengths.size() <= 0)
			throw new SolutionNotFoundException("The randomizer has no iterations");
		for (int i = 0; i < _strengths.size(); i++) {
			int count = ((Integer) _counts.elementAt(i)).intValue();
			int strength = ((Integer) _strengths.elementAt(i)).intValue();
			_randomizable.setStrength(strength);
			for (int it = 0; it < count; it++) {
				double cost = construct.constructClosedTours(depotKey);
				if (cost < _bestCost) {
					_bestCost = cost;
					_bestCosts = construct.getCosts();
					_bestLoads = construct.getLoads();
					_bestOfTours = construct.getTours();
				}
			}
		}

		return _bestCost;
	}

	public void setGraph(GraphI graph) {
		((VRPI) _randomizable).setGraph(graph);
	}

	public void setVehicleCost(double vehicleCost) {
		((VRPI) _randomizable).setVehicleCost(vehicleCost);
	}

	public void setCostConstraint(double maxCostPerVehicle) {
		((VRPI) _randomizable).setCostConstraint(maxCostPerVehicle);
	}

	public void setCapacityConstraint(double maxLoadPerVehicle) {
		((VRPI) _randomizable).setCapacityConstraint(maxLoadPerVehicle);
	}

	public void setProperties(PropertiesI properties) {
		((VRPI) _randomizable).setProperties(properties);
	}

	public void setEdgeKey(Object edgeKey) {
		((VRPI) _randomizable).setEdgeKey(edgeKey);
	}

	public Vector<ElementI>[] getTours() throws SolutionNotFoundException {
		if (_bestOfTours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return VRPBase.copyTours(_bestOfTours);
	}

	public double getCost() throws SolutionNotFoundException {
		if (_bestOfTours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _bestCost;
	}

	public double[] getCosts() throws SolutionNotFoundException {
		if (_bestOfTours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _bestCosts;
	}

	public double[] getLoads() throws SolutionNotFoundException {
		if (_bestOfTours == null)
			throw new SolutionNotFoundException("No solution has been created");
		return _bestLoads;
	}

}
