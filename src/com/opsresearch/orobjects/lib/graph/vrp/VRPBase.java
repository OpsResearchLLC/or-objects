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

import com.opsresearch.orobjects.lib.graph.EdgeI;
import com.opsresearch.orobjects.lib.graph.ElementI;
import com.opsresearch.orobjects.lib.graph.GraphI;
import com.opsresearch.orobjects.lib.graph.PropertiesAdapter;
import com.opsresearch.orobjects.lib.graph.PropertiesI;
import com.opsresearch.orobjects.lib.graph.VertexI;

public abstract class VRPBase {

	protected GraphI _graph = null;
	protected boolean _closed = false;
	protected boolean _out = false;
	protected Object _edgeKey = null;
	protected Object _depotKey = null;
	protected double _vehicleCost = 0;
	protected double _maxCost = Double.POSITIVE_INFINITY;
	protected double _maxLoad = Double.POSITIVE_INFINITY;
	protected PropertiesI _properties = new PropertiesAdapter();

	public double getLoad(VertexI vert) {
		return _properties.getVertexDemand(vert)[0];
	}

	public double getCost(VertexI from, VertexI to) {
		if (from == to)
			return 0;
		boolean reverse = false;
		EdgeI edge = _graph.getMutableEdge(from, to, _edgeKey);
		if (_properties != null && edge != null
				&& _properties.isEdgeRestricted(edge, reverse))
			edge = null;
		if (edge == null) {
			reverse = true;
			edge = _graph.getMutableEdge(to, from, _edgeKey);
			if (edge != null && edge.isDirected())
				edge = null;
			if (_properties != null && edge != null
					&& _properties.isEdgeRestricted(edge, reverse))
				edge = null;
		}
		if (edge == null)
			return Double.POSITIVE_INFINITY;
		return _properties.getEdgeCost(edge, reverse);
	}

	public void setGraph(GraphI graph) {
		_graph = graph;
	}

	public GraphI getGraph() {
		return _graph;
	}

	public void setVehicleCost(double vehicleCost) {
		_vehicleCost = vehicleCost;
	}

	public void setCostConstraint(double maxCostPerVehicle) {
		_maxCost = maxCostPerVehicle;
	}

	public void setCapacityConstraint(double maxLoadPerVehicle) {
		_maxLoad = maxLoadPerVehicle;
	}

	public void setProperties(PropertiesI properties) {
		_properties = properties;
	}

	public void setEdgeKey(Object edgeKey) {
		_edgeKey = edgeKey;
	}

	@SuppressWarnings("unchecked")
	protected static Vector<ElementI>[] copyTours(Vector<ElementI>[] tours) {
		Vector<ElementI>[] ts = new Vector[tours.length];
		for (int j = 0; j < tours.length; j++) {
			Vector<ElementI> tour = tours[j];
			Vector<ElementI> t = ts[j] = new Vector<ElementI>(tour.size());
			for (int i = 0; i < tour.size(); i++)
				t.addElement(tour.elementAt(i));
		}
		return ts;
	}

	protected double[] getLoads(Vector<ElementI>[] tours)
			throws SolutionNotFoundException {
		return null;
	}

}
