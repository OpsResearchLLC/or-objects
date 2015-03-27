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

package com.opsresearch.orobjects.lib.real.matrix;

import java.util.Iterator;

public interface VectorI extends RealContainerI {
	public boolean isNull(int index);

	public int size();

	public int sizeOfElements();

	public double[] getArray();

	public void setElementAt(int index, double value);

	public void setElements(double value);

	public void setElements(VectorI values);

	public double elementAt(int index);

	public Iterator<VectorElementI> elements();

	public double sum();

	public double sum(int begin);

	public double sum(int begin, int end);

	public double sumOfSquares();

	public double sumOfSquares(int begin);

	public double sumOfSquares(int begin, int end);

	public double sumOfSquaredDifferences(double scaler);

	public double sumOfSquaredDifferences(int begin, double scaler);

	public double sumOfSquaredDifferences(int begin, int end, double scaler);

	public double maximumAbsoluteValue();

	public double maximumAbsoluteValue(int begin);

	public double maximumAbsoluteValue(int begin, int end);

	public double maximumValue();

	public double maximumValue(int begin);

	public double maximumValue(int begin, int end);

	public boolean equals(VectorI vector);

	double minimumAbsoluteValue(int begin, int end);

	double minimumValue(int begin, int end);

}
