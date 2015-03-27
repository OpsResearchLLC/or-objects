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

package com.opsresearch.orobjects.lib.graph;

public class VertexValue implements VertexValueI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private double _cost, _time;
	private double[] _demand;

	public VertexValue() {
	}

	public VertexValue(double cost, double time, double[] demand) {
		_cost = cost;
		_time = time;
		_demand = demand;
	}

	public double getCost() {
		return _cost;
	}

	public double getTime() {
		return _time;
	}

	public double[] getDemand() {
		return _demand;
	}

	public void set(double cost, double time, double[] demand) {
		_cost = cost;
		_time = time;
		_demand = demand;
	}

	public void setCost(double cost) {
		_cost = cost;
	}

	public void setTime(double time) {
		_time = time;
	}

	public void setDemand(double[] demand) {
		_demand = demand;
	}

}
