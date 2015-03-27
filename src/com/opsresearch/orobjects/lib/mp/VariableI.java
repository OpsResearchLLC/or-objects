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

public interface VariableI {
	public static final byte FREE = 20;

	public static final byte REAL = 21;

	public static final byte INTEGER = 22;

	public static final byte BOOLEAN = 23;

	public String getName();

	public int getColumnIndex();

	public VariableI setType(byte type);

	public byte getType();

	public VariableI setObjectiveCoefficient(double scaler);

	public double getObjectiveCoefficient();

	public VariableI setUpperBound(double scaler);

	public double getUpperBound();

	public VariableI setLowerBound(double scaler);

	public double getLowerBound();
}
