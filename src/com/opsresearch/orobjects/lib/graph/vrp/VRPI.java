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

public interface VRPI {
	public void setGraph(GraphI graph);

	public void setVehicleCost(double vehicleCost);

	public void setCostConstraint(double maxCostPerVehicle);

	public void setCapacityConstraint(double maxLoadPerVehicle);

	public void setProperties(PropertiesI properties);

	public void setEdgeKey(Object edgeKey);

	public double getCost() throws SolutionNotFoundException;

	public double[] getCosts() throws SolutionNotFoundException;

	public double[] getLoads() throws SolutionNotFoundException;

	public Vector<ElementI>[] getTours() throws SolutionNotFoundException;
}
