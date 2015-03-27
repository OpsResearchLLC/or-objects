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

package com.opsresearch.orobjects.lib.graph.tw;

import com.opsresearch.orobjects.lib.graph.VertexValue;
import com.opsresearch.orobjects.lib.graph.VertexValueI;

public class TimeWindowValue extends VertexValue implements VertexValueI, TimeWindowValueI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private double[] _time, _start, _end;

	TimeWindowValue() {
	}

	TimeWindowValue(double cost, double time, double[] demand) {
		super(cost, time, demand);
	}

	public double[] getServiceTime() {
		return _time;
	}

	public double[] getTimeWindowStart() {
		return _start;
	}

	public double[] getTimeWindowEnd() {
		return _end;
	}

	public void setServiceTime(double[] serviceTime) {
		_time = serviceTime;
	}

	public void setTimeWindowStart(double[] windowStart) {
		_start = windowStart;
	}

	public void setTimeWindowEnd(double[] windowEnd) {
		_end = windowEnd;
	}

}
