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

package com.opsresearch.orobjects.lib.complex.matrix;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public interface VectorI extends ComplexContainerI {
	public boolean isNull(int index);

	public int size();

	public int sizeOfElements();

	public double[] getArray();

	public void setElementAt(int index, ComplexI value);

	public void setElements(ComplexI value);

	public void setElements(VectorI values);

	public Complex elementAt(int index);

	public Complex elementAt(int index, Complex results);

	public Iterator<VectorElementI> elements();

	public Complex sum();

	public Complex sum(int begin);

	public Complex sum(int begin, int end);

	public Complex sumOfSquares();

	public Complex sumOfSquares(int begin);

	public Complex sumOfSquares(int begin, int end);

	public Complex sumOfSquaredDifferences(ComplexI scaler);

	public Complex sumOfSquaredDifferences(int begin, ComplexI scaler);

	public Complex sumOfSquaredDifferences(int begin, int end, ComplexI scaler);

	public boolean equals(VectorI vector);

}
