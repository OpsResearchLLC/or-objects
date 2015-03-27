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

public class EdgeValue implements EdgeValueI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private double _costF, _costR;
	private double _timeF, _timeR;
	private double _distF, _distR;

	public EdgeValue() {
	}

	public EdgeValue(EdgeValueI value) {
		_costF = value.getCost(false);
		_timeF = value.getTime(false);
		_distF = value.getDistance(false);
		_costR = value.getCost(true);
		_timeR = value.getTime(true);
		_distR = value.getDistance(true);
	}

	public EdgeValue(double cost, double time, double distance) {
		_costF = cost;
		_timeF = time;
		_distF = distance;
		_costR = cost;
		_timeR = time;
		_distR = distance;
	}

	public double getCost(boolean reverse) {
		return reverse ? _costR : _costF;
	}

	public double getTime(boolean reverse) {
		return reverse ? _timeR : _timeF;
	}

	public double getDistance(boolean reverse) {
		return reverse ? _distR : _distF;
	}

	public void set(double cost, double time, double distance) {
		_costF = cost;
		_timeF = time;
		_distF = distance;
		_costR = cost;
		_timeR = time;
		_distR = distance;
	}

	public void setCost(boolean reverse, double cost) {
		if (reverse)
			_costR = cost;
		else
			_costF = cost;
	}

	public void setTime(boolean reverse, double time) {
		if (reverse)
			_timeR = time;
		else
			_timeF = time;
	}

	public void setDistance(boolean reverse, double distance) {
		if (reverse)
			_distR = distance;
		else
			_distF = distance;
	}

}
