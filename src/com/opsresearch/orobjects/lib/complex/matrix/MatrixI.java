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

public interface MatrixI extends ComplexContainerI {
	public boolean isNull(int row, int column);

	public boolean isRowMajor();

	public boolean isColumnMajor();

	public int sizeOfRows();

	public int sizeOfColumns();

	public int sizeOfElements();

	public void setElementAt(int row, int column, ComplexI value);

	public void setElements(ComplexI value);

	public void setElements(MatrixI values);

	public void setRow(int row, VectorI values);

	public void setColumn(int column, VectorI values);

	public void setDiagonal(VectorI values);

	public double[][] getArray();

	public Complex elementAt(int row, int column);

	public Complex elementAt(int row, int column, Complex results);

	public Iterator<MatrixElementI> rowElements(int row);

	public Iterator<MatrixElementI> columnElements(int column);

	public Iterator<MatrixElementI> elements();

	public Complex sum();

	public Complex sum(int begin);

	public Complex sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd);

	public Complex sumOfSquares();

	public Complex sumOfSquares(int begin);

	public Complex sumOfSquares(int rowBegin, int rowEnd, int columnBegin, int columnEnd);

	public Complex sumOfSquaredDifferences(ComplexI scaler);

	public Complex sumOfSquaredDifferences(int begin, ComplexI scaler);

	public Complex sumOfSquaredDifferences(int rowBegin, int rowEnd, int columnBegin, int columnEnd, ComplexI scaler);

	public boolean equals(MatrixI matrix);

}
