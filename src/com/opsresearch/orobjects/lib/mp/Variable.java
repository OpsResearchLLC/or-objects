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

package com.opsresearch.orobjects.lib.mp;


public class Variable implements VariableI, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	static public String typeToString(byte type) {
		switch (type) {
		case FREE:
			return "FREE";
		case REAL:
			return "REAL";
		case INTEGER:
			return "INTEGER";
		case BOOLEAN:
			return "BOOLEAN";
		}
		return "UNKNOWN-" + type;
	}

	protected int _columnIndex = 0;
	private byte _type = REAL;
	protected String _name = null;
	private double _lowerBound = 0.0;
	private double _upperBound = Double.POSITIVE_INFINITY;
	private double _objectiveCoefficient = 0.0;

	Variable(int columnIndex, String name) {
		_name = name;
		_columnIndex = columnIndex;
	}

	public String getName() {
		return _name;
	}

	public int getColumnIndex() {
		return _columnIndex;
	}

	public VariableI setType(byte type) {
		if (type != FREE && type != REAL && type != INTEGER && type != BOOLEAN)
			throw new MpError("Invalid variable type: " + type);
		_type = type;
		return this;
	}

	public byte getType() {
		return _type;
	}

	public VariableI setObjectiveCoefficient(double scaler) {
		_objectiveCoefficient = scaler;
		return this;
	}

	public double getObjectiveCoefficient() {
		return _objectiveCoefficient;
	}

	public VariableI setUpperBound(double scaler) {
		_upperBound = scaler;
		return this;
	}

	public double getUpperBound() {
		return _upperBound;
	}

	public VariableI setLowerBound(double scaler) {
		_lowerBound = scaler;
		return this;
	}

	public double getLowerBound() {
		return _lowerBound;
	}

	public String toString() {
		String s = "";
		s += "[" + _columnIndex + "]";
		if (_name != null)
			s += ", " + _name;
		s += ", " + typeToString(_type);
		s += ", Obj=" + _objectiveCoefficient;
		s += ", LoBnd=" + _lowerBound;
		s += ", UpBnd=" + _upperBound;
		return s;
	}

	public boolean equals(Object o) {
		if (!(o instanceof VariableI))
			return false;
		VariableI variable = (VariableI) o;
		if (_type != variable.getType())
			return false;
		if (_lowerBound != variable.getLowerBound())
			return false;
		if (_upperBound != variable.getUpperBound())
			return false;
		if (_objectiveCoefficient != variable.getObjectiveCoefficient())
			return false;
		return true;
	}

}
