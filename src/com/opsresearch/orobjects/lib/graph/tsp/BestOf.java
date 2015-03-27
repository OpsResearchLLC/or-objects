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

public class BestOf extends TSPBase implements ConstructI {
	private Vector<ImproveI> _improve = new Vector<ImproveI>();
	private Vector<ConstructI> _construct = new Vector<ConstructI>();
	private Vector<Integer> _improveMax = new Vector<Integer>();
	private Vector<Integer> _improveMin = new Vector<Integer>();
	private Vector<Integer> _constructMin = new Vector<Integer>();
	private Vector<Integer> _constructMax = new Vector<Integer>();
	private Vector<ElementI> _bestOfTour;

	public BestOf() {
	}

	public void addConstruct(ConstructI construct) {
		addConstruct(construct, 0, Integer.MAX_VALUE);
	}

	public void addConstruct(ConstructI construct, int minSize, int maxSize) {
		construct.setEdgeKey(_edgeKey);
		construct.setProperties(_properties);
		if (getGraph() != null)
			construct.setGraph(getGraph());
		_construct.addElement(construct);
		_constructMin.addElement(new Integer(minSize));
		_constructMax.addElement(new Integer(maxSize));
	}

	public void addImprove(ImproveI improve) {
		addImprove(improve, 0, Integer.MAX_VALUE);
	}

	public void addImprove(ImproveI improve, int minSize, int maxSize) {
		improve.setEdgeKey(_edgeKey);
		improve.setProperties(_properties);
		if (getGraph() != null)
			improve.setGraph(getGraph());
		_improve.addElement(improve);
		_improveMin.addElement(new Integer(minSize));
		_improveMax.addElement(new Integer(maxSize));
	}

	@Override
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

	public double constructOpenTour(Object originKey, Object destinationKey) throws TourNotFoundException,
			VertexNotFoundException {
		countVertices();
		setClosed(false);
		_bestOfTour = null;
		setBestCost(Double.POSITIVE_INFINITY);
		for (int i = 0; i < _construct.size(); i++) {
			if (getSize() < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(getSelected());
			double cost = construct.constructOpenTour(originKey, destinationKey);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = construct.getTour();
			}
		}
		if (_bestOfTour == null)
			throw new TourNotFoundException();
		for (int i = 0; i < _improve.size(); i++) {
			if (getSize() < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveOpenTour(_bestOfTour, true, true);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = improve.getTour();
			}
		}
		return getBestCost();
	}

	public double constructOpenTourFrom(Object originKey) throws TourNotFoundException, VertexNotFoundException {
		countVertices();
		setClosed(false);
		_bestOfTour = null;
		setBestCost(Double.POSITIVE_INFINITY);
		for (int i = 0; i < _construct.size(); i++) {
			if (getSize() < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(getSelected());
			double cost = construct.constructOpenTourFrom(originKey);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = construct.getTour();
			}
		}
		if (_bestOfTour == null)
			throw new TourNotFoundException();
		for (int i = 0; i < _improve.size(); i++) {
			if (getSize() < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveOpenTour(_bestOfTour, true, false);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = improve.getTour();
			}
		}
		return getBestCost();
	}

	public double constructOpenTourTo(Object destinationKey) throws TourNotFoundException, VertexNotFoundException {
		countVertices();
		setClosed(false);
		_bestOfTour = null;
		setBestCost(Double.POSITIVE_INFINITY);
		for (int i = 0; i < _construct.size(); i++) {
			if (getSize() < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(getSelected());
			double cost = construct.constructOpenTourTo(destinationKey);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = construct.getTour();
			}
		}
		if (_bestOfTour == null)
			throw new TourNotFoundException();
		for (int i = 0; i < _improve.size(); i++) {
			if (getSize() < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveOpenTour(_bestOfTour, false, true);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = improve.getTour();
			}
		}
		return getBestCost();
	}

	public double constructOpenTour() throws TourNotFoundException {
		countVertices();
		setClosed(false);
		_bestOfTour = null;
		setBestCost(Double.POSITIVE_INFINITY);
		for (int i = 0; i < _construct.size(); i++) {
			if (getSize() < ((Integer) _constructMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _constructMax.elementAt(i)).intValue())
				continue;
			ConstructI construct = (ConstructI) _construct.elementAt(i);
			construct.selectVertex(getSelected());
			double cost = construct.constructOpenTour();
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = construct.getTour();
			}
		}
		if (_bestOfTour == null)
			throw new TourNotFoundException();
		for (int i = 0; i < _improve.size(); i++) {
			if (getSize() < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveOpenTour(_bestOfTour);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = improve.getTour();
			}
		}
		return getBestCost();
	}

	public double constructClosedTour() throws TourNotFoundException {
		countVertices();
		setClosed(true);
		_bestOfTour = null;
		setBestCost(Double.POSITIVE_INFINITY);
		for (int i = 0; i < _construct.size(); i++) {
			if (getSize() < _constructMin.elementAt(i).intValue())
				continue;
			if (getSize() > _constructMax.elementAt(i).intValue())
				continue;
			ConstructI construct = _construct.elementAt(i);
			construct.selectVertex(getSelected());
			double cost = construct.constructClosedTour();
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = construct.getTour();
			}
		}
		if (_bestOfTour == null)
			throw new TourNotFoundException();
		for (int i = 0; i < _improve.size(); i++) {
			if (getSize() < ((Integer) _improveMin.elementAt(i)).intValue())
				continue;
			if (getSize() > ((Integer) _improveMax.elementAt(i)).intValue())
				continue;
			ImproveI improve = (ImproveI) _improve.elementAt(i);
			double cost = improve.improveClosedTour(_bestOfTour);
			if (cost < getBestCost()) {
				setBestCost(cost);
				_bestOfTour = improve.getTour();
			}
		}
		return getBestCost();
	}

	public Vector<ElementI> getTour() {
		Vector<ElementI> tour = new Vector<ElementI>(_bestOfTour.size());
		for (int i = 0; i < _bestOfTour.size(); i++)
			tour.addElement(_bestOfTour.elementAt(i));
		return tour;
	}

	public double getCost() {
		if (_bestOfTour == null)
			throw new TSPError("No solution has been created");
		return getBestCost();
	}
}
