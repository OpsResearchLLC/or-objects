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

import com.opsresearch.orobjects.lib.real.DoubleI;

public abstract class EdgeBase implements EdgeI, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected int _index;
	protected Object _key;
	protected Object _value;
	protected boolean _isDirected;

	EdgeBase(int index, Object key, Object value, boolean isDirected) {
		_key = key;
		_value = value;
		_index = index;
		_isDirected = isDirected;
	};

	public int getIndex() {
		return _index;
	}

	public Object getKey() {
		return _key;
	}

	public Object getValue() {
		return _value;
	}

	public boolean isEdge() {
		return true;
	}

	public boolean isVertex() {
		return false;
	}

	public void setIndex(int index) {
		_index = index;
	}

	public boolean isDirected() {
		return _isDirected;
	}

	public double getCost(boolean isReverse) {
		if (_value != null) {
			if (_value instanceof EdgeValueI)
				return ((EdgeValueI) _value).getCost(isReverse);
			else if (_value instanceof DoubleI)
				return ((DoubleI) _value).doubleValue();
			else if (_value instanceof Number)
				return ((Number) _value).doubleValue();
		}
		return 0.0;
	}

	public double getTime(boolean isReverse) {
		if (_value != null && _value instanceof EdgeValueI)
			return ((EdgeValueI) _value).getCost(isReverse);
		return 0.0;
	}

	public double getDistance(boolean isReverse) {
		if (_value != null && _value instanceof EdgeValueI)
			return ((EdgeValueI) _value).getDistance(isReverse);
		return 0.0;
	}

	public GraphI getSubgraph() {
		return null;
	}

}
