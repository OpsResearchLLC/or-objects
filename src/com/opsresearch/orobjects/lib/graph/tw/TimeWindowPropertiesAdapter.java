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

import com.opsresearch.orobjects.lib.graph.PropertiesAdapter;
import com.opsresearch.orobjects.lib.graph.VertexI;

public class TimeWindowPropertiesAdapter extends PropertiesAdapter implements TimeWindowPropertiesI {
	public double[] getServiceTime(VertexI vertex) {
		Object v = vertex.getValue();
		if (v != null && v instanceof TimeWindowValueI)
			return ((TimeWindowValueI) v).getServiceTime();
		return null;
	}

	public double[] getTimeWindowStart(VertexI vertex) {
		Object v = vertex.getValue();
		if (v != null && v instanceof TimeWindowValueI)
			return ((TimeWindowValueI) v).getTimeWindowStart();
		return null;
	}

	public double[] getTimeWindowEnd(VertexI vertex) {
		Object v = vertex.getValue();
		if (v != null && v instanceof TimeWindowValueI)
			return ((TimeWindowValueI) v).getTimeWindowEnd();
		return null;
	}

}
