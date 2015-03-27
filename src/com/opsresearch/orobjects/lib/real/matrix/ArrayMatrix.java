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

import com.opsresearch.orobjects.lib.util.Array;

public abstract class ArrayMatrix extends Matrix implements SizableMatrixI {
	private static final long serialVersionUID = 1L;

	protected int _sizeOfRows = 0;
	protected int _sizeOfColumns = 0;
	protected double[][] _arrays = null;

	public ArrayMatrix() {
	}

	public ArrayMatrix(int sizeOfRows, int sizeOfColumns) {
		_sizeOfRows = sizeOfRows;
		_sizeOfColumns = sizeOfColumns;
	}

	public ArrayMatrix(double[][] array) {
		int n = 0;
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row != null && row.length > n)
				n = row.length;
		}
		_sizeOfRows = array.length;
		_sizeOfColumns = n;
	}

	public void setElements(double value) {
		for (int i = 0; i < _arrays.length; i++) {
			double[] row = _arrays[i];
			if (row == null)
				continue;
			Array.copy(row.length, row, 0, 1, value);
		}
	}

	public int sizeOfRows() {
		return _sizeOfRows;
	}

	public int sizeOfColumns() {
		return _sizeOfColumns;
	}

	public boolean isNull(int row, int column) {
		return false;
	}

	public double[][] getValueArray() {
		return _arrays;
	}

	public double[][] getArray() {
		double[][] array = new double[sizeOfRows()][sizeOfColumns()];
		for (Iterator<MatrixElementI> e = elements(); e.hasNext();) {
			MatrixElementI elem = (MatrixElementI) e.next();
			array[elem.getRowIndex()][elem.getColumnIndex()] = elem.getValue();
		}
		return array;
	}
}
