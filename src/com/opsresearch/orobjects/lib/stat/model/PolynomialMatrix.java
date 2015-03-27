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

package com.opsresearch.orobjects.lib.stat.model;

import com.opsresearch.orobjects.lib.real.matrix.FunctionMatrix;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;


public class PolynomialMatrix extends FunctionMatrix {
	private static final long serialVersionUID = 1L;
	private MatrixI _matrix;
	private int _n, _d;

	public PolynomialMatrix(MatrixI matrix, int degree) {
		if (degree < 1)
			throw new Error("The degree must be greater than zero.");
		_d = degree;
		_matrix = matrix;
		_sizeOfRows = matrix.sizeOfRows();
		_n = matrix.sizeOfColumns();
		_sizeOfColumns = _n * _d;
	}

	public int getTermIndex(int column, int degree) {
		if (column >= _n)
			throw new Error("The column index (" + column + ") exceeds the size of the underlying matrix (" + _n + ").");
		if (degree > _d)
			throw new Error("The degree (" + degree + ") expeeds the degree of the model (" + _d + ").");
		if (degree < 1)
			throw new Error("The degree (" + degree + ") can't be less than one.");
		if (degree == 1)
			return column;
		return _n * (degree - 1) + column;
	}

	public double functionElementAt(int row, int column) {
		if (column < _n)
			return _matrix.elementAt(row, column);
		int d = 1 + column / _n;
		double v = _matrix.elementAt(row, column % _n);
		if (d == 2)
			return v * v;
		if (d == 3)
			return v * v * v;
		return Math.pow(v, d);
	}

}
