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

public class RowMajorMatrix extends ContiguousMatrix {
	private static final long serialVersionUID = 1L;

	public RowMajorMatrix(int sizeOfRows, int sizeOfColumns) {
		super(sizeOfRows, sizeOfColumns, sizeOfRows, sizeOfColumns);
	}

	public RowMajorMatrix(int sizeOfRows, int sizeOfColumns,
			int capacityOfRows, int capacityOfColumns) {
		super(sizeOfRows, sizeOfColumns, capacityOfRows, capacityOfColumns);
	}

	public RowMajorMatrix(double[][] array) {
		super(array);
		for (int i = 0, k = 0; i < array.length; i++, k += _capacityOfColumns) {
			double[] row = array[i];
			if (row == null)
				continue;
			Array.copy(row.length, _values, k, 1, row, 0, 1);
		}
	}

	public RowMajorMatrix(MatrixI matrix) {
		super(matrix);
		for (Iterator<MatrixElementI> e = matrix.elements(); e
				.hasNext();) {
			MatrixElementI elem = e.next();
			setElementAt(elem.getRowIndex(), elem.getColumnIndex(),
					elem.getValue());
		}
	}

	public RowMajorMatrix(VectorI vector) {
		super(vector);
	}

	public int getOffset(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return row * _capacityOfColumns + column;
	}

	public int getRowIncrement() {
		return _capacityOfColumns;
	}

	public int getColumnIncrement() {
		return 1;
	}

	public boolean isRowMajor() {
		return true;
	}

	public boolean isColumnMajor() {
		return false;
	}

	public void setSize(int sizeOfRows, int sizeOfColumns) {
		if (sizeOfRows > _capacityOfRows || sizeOfColumns > _capacityOfColumns)
			setCapacity(sizeOfRows, sizeOfColumns);
		for (int i = _sizeOfRows, k = _sizeOfRows * _capacityOfColumns; i < sizeOfRows; i++, k += _capacityOfColumns) {
			Array.copy(sizeOfColumns, _values, k, 1, 0.0);
		}
		if (sizeOfColumns > _sizeOfColumns) {
			int n = sizeOfColumns - _sizeOfColumns;
			for (int i = 0, k = _sizeOfColumns; i < _sizeOfRows; i++, k += _capacityOfColumns)
				Array.copy(n, _values, k, 1, 0.0);
		}
		_sizeOfRows = sizeOfRows;
		_sizeOfColumns = sizeOfColumns;
	}

	public void setCapacity(int capacityOfRows, int capacityOfColumns) {
		if (capacityOfRows <= _capacityOfRows
				&& capacityOfColumns <= _capacityOfColumns)
			return;
		double[] values = new double[capacityOfRows * capacityOfColumns];
		for (int i = 0, k = 0, l = 0; i < _sizeOfRows; i++, k += _capacityOfColumns, l += capacityOfColumns)
			Array.copy(_sizeOfColumns, values, l, 1, _values, k, 1);
		_values = values;
		_capacityOfRows = capacityOfRows;
		_capacityOfColumns = capacityOfColumns;
	}

	public void addRow(VectorI vector) {
		int i = _sizeOfRows;
		setSize(_sizeOfRows + 1, _sizeOfColumns);
		if (vector == null)
			return;
		if (vector instanceof DenseVector) {
			DenseVector vect = (DenseVector) vector;
			int n = Math.min(vector.size(), _sizeOfColumns);
			Array.copy(n, _values, i * _capacityOfColumns, 1, vect._array, 0, 1);
		} else {
			for (Iterator<VectorElementI> e = vector.elements(); e
					.hasNext();) {
				VectorElementI elem = e.next();
				int j = elem.getIndex();
				if (j < _sizeOfColumns)
					setElementAt(i, j, elem.getValue());
			}
		}
	}

	public void addColumn(VectorI vector) {
		int j = _sizeOfColumns;
		setSize(_sizeOfRows, _sizeOfColumns + 1);
		if (vector == null)
			return;
		if (vector instanceof DenseVector) {
			DenseVector vect = (DenseVector) vector;
			int n = Math.min(vector.size(), _sizeOfRows);
			Array.copy(n, _values, _sizeOfColumns - 1, _capacityOfRows,
					vect._array, 0, 1);
		} else {
			for (Iterator<VectorElementI> e = vector.elements(); e
					.hasNext();) {
				VectorElementI elem = e.next();
				int i = elem.getIndex();
				if (i < _sizeOfRows)
					setElementAt(i, j, elem.getValue());
			}
		}
	}

	public void setElementAt(int row, int column, double value) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		_values[column + _capacityOfColumns * row] = value;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return _values[column + _capacityOfColumns * row];
	}

	public double[][] getArray() {
		double[][] array = new double[_sizeOfRows][_sizeOfColumns];
		for (int i = 0, k = 0; i < array.length; i++, k += _capacityOfColumns) {
			double[] row = array[i];
			Array.copy(row.length, row, 0, 1, _values, k, 1);
		}
		return array;
	}

}
