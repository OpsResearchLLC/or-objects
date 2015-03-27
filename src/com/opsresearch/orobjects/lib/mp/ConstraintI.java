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

public interface ConstraintI {
	public static final byte FREE = 10;

	public static final byte LESS = 11;

	public static final byte GREATER = 12;

	public static final byte EQUAL = 13;

	public static final byte RANGE = 14;

	public String getName();

	public int getRowIndex();

	public ConstraintI setType(byte type);

	public byte getType();

	public ConstraintI setRightHandSide(double scaler);

	public double getRightHandSide();

	public ConstraintI setUpperRange(double scaler);

	public double getUpperRange();

	public ConstraintI setLowerRange(double scaler);

	public double getLowerRange();
}
