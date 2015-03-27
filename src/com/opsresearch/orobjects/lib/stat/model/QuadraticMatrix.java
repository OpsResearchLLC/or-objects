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


public class QuadraticMatrix extends FunctionMatrix {
	private static final long serialVersionUID = 1L;
	private int _n;
	private MatrixI _matrix;

	public QuadraticMatrix(MatrixI matrix) {
		_n = matrix.sizeOfColumns();
		_matrix = matrix;
		_sizeOfRows = matrix.sizeOfRows();
		_sizeOfColumns = _n + _n * _n;
	}

	public int getProductIndex(int column1, int column2) {
		if (column1 >= _n)
			throw new Error("The column-1 index (" + column1 + ") exceeds the size of the underlying matrix (" + _n
					+ ").");
		if (column2 >= _n)
			throw new Error("The column-2 index (" + column2 + ") exceeds the size of the underlying matrix (" + _n
					+ ").");
		return _n + column1 * _n + column2;
	}

	public double functionElementAt(int row, int column) {
		if (column < _n)
			return _matrix.elementAt(row, column);
		int c = column - _n;
		int i = c / _n;
		int j = c % _n;
		return _matrix.elementAt(row, i) * _matrix.elementAt(row, j);
	}
}
