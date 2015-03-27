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

public abstract class VertexBase implements VertexI, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	protected int _index;
	protected int _inDegree = 0;
	protected int _outDegree = 0;
	protected Object _key;
	protected Object _value;

	VertexBase(int index, Object key, Object value) {
		_key = key;
		_value = value;
		_index = index;
	};

	public int getIndex() {
		return _index;
	}

	public int getInDegree() {
		return _inDegree;
	}

	public int getOutDegree() {
		return _outDegree;
	}

	public Object getKey() {
		return _key;
	}

	public Object getValue() {
		return _value;
	}

	public boolean isEdge() {
		return false;
	}

	public boolean isVertex() {
		return true;
	}

	public void setIndex(int index) {
		_index = index;
	}

	public double[] getDemand() {
		double[] a;
		if (_value != null) {
			if (_value instanceof VertexValueI)
				return ((VertexValueI) _value).getDemand();
			else if (_value instanceof DoubleI) {
				a = new double[1];
				a[0] = ((DoubleI) _value).doubleValue();
				return a;
			} else if (_value instanceof Number) {
				a = new double[1];
				a[0] = ((Number) _value).doubleValue();
				return a;
			}
		}
		return null;
	}

	public double getCost() {
		if (_value != null) {
			if (_value instanceof VertexValueI)
				return ((VertexValueI) _value).getCost();
			else if (_value instanceof DoubleI)
				return ((DoubleI) _value).doubleValue();
			else if (_value instanceof Number)
				return ((Number) _value).doubleValue();
		}
		return 0.0;
	}

	public double getTime() {
		if (_value != null && _value instanceof VertexValueI)
			return ((VertexValueI) _value).getTime();
		return 0.0;
	}

}
