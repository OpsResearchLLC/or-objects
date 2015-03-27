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

public interface MatrixI extends RealContainerI {
	public boolean isNull(int row, int column);

	public boolean isRowMajor();

	public boolean isColumnMajor();

	public int sizeOfRows();

	public int sizeOfColumns();

	public int sizeOfElements();

	public void setElementAt(int row, int column, double value);

	public void setElements(double value);

	public void setElements(MatrixI values);

	public void setRow(int row, VectorI values);

	public void setColumn(int column, VectorI values);

	public void setDiagonal(VectorI values);

	public double[][] getArray();

	public double elementAt(int row, int column);

	public Iterator<MatrixElementI> rowElements(int row);

	public Iterator<MatrixElementI> columnElements(int column);

	public Iterator<MatrixElementI> elements();

	public double sum();

	public double sum(int begin);

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd);

	public double sumOfSquares();

	public double sumOfSquares(int begin);

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin, int columnEnd);

	public double sumOfSquaredDifferences(double scaler);

	public double sumOfSquaredDifferences(int begin, double scaler);

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd, int columnBegin, int columnEnd, double scaler);

	public boolean equals(MatrixI matrix);

}
