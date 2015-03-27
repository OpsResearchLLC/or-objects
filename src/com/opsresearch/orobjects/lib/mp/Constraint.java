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

public class Constraint implements ConstraintI, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	static public String typeToString(byte type) {
		switch (type) {
		case FREE:
			return "FREE";
		case LESS:
			return "LESS";
		case EQUAL:
			return "EQUAL";
		case RANGE:
			return "RANGE";
		case GREATER:
			return "GREATER";
		}
		return "UNKNOWN-" + type;
	}

	int _rowIndex = 0;
	private byte _type = LESS;
	String _name = null;
	private double _lowerRange = 0.0;
	private double _upperRange = 0.0;
	private double _rightHandSide = 0.0;

	Constraint(int rowIndex, String name) {
		_name = name;
		_rowIndex = rowIndex;
	}

	public String getName() {
		return _name;
	}

	public int getRowIndex() {
		return _rowIndex;
	}

	public ConstraintI setType(byte type) {
		if (type != FREE && type != LESS && type != GREATER && type != EQUAL && type != RANGE)
			throw new MpError("Invalid constraint type: " + type);
		_type = type;
		return this;
	}

	public byte getType() {
		return _type;
	}

	public ConstraintI setRightHandSide(double scaler) {
		_rightHandSide = scaler;
		return this;
	}

	public double getRightHandSide() {
		return _rightHandSide;
	}

	public ConstraintI setUpperRange(double scaler) {
		_upperRange = scaler;
		return this;
	}

	public double getUpperRange() {
		return _upperRange;
	}

	public ConstraintI setLowerRange(double scaler) {
		_lowerRange = scaler;
		return this;
	}

	public double getLowerRange() {
		return _lowerRange;
	}

	public String toString() {
		String s = "";
		s += "[" + _rowIndex + "]";
		if (_name != null)
			s += ", " + _name;
		s += ", " + typeToString(_type);
		s += ", Rhs=" + _rightHandSide;
		s += ", LoRng=" + _lowerRange;
		s += ", UpRng=" + _upperRange;
		return s;
	}

	public boolean equals(Object o) {
		if (!(o instanceof ConstraintI))
			return false;
		ConstraintI constraint = (ConstraintI) o;
		if (_type != constraint.getType())
			return false;
		if (_type == RANGE) {
			if (_lowerRange != constraint.getLowerRange())
				return false;
			if (_upperRange != constraint.getUpperRange())
				return false;
		} else if (_rightHandSide != constraint.getRightHandSide())
			return false;
		return true;
	}

}
