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

import javax.xml.bind.annotation.XmlElement;

import com.opsresearch.orobjects.lib.util.Array;

public class RowArrayMatrix extends ArrayMatrix {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "capacityOfColumns")
	int _capacityOfColumns = 0;

	public RowArrayMatrix() {
	}

	public RowArrayMatrix(int sizeOfRows, int sizeOfColumns) {
		super(sizeOfRows, sizeOfColumns);
		_capacityOfColumns = sizeOfColumns;
		_arrays = new double[sizeOfRows][];
	}

	public RowArrayMatrix(int sizeOfRows, int sizeOfColumns,
			int capacityOfRows, int capacityOfColumns) {
		super(sizeOfRows, sizeOfColumns);
		_capacityOfColumns = _sizeOfColumns;
		_arrays = new double[capacityOfRows][];
	}

	public RowArrayMatrix(VectorI vector) {
		super(vector.size(), vector.size());
		_capacityOfColumns = _sizeOfColumns;
		_arrays = new double[_sizeOfRows][];
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			double[] row;
			if ((row = _arrays[elem.getIndex()]) == null)
				row = _arrays[elem.getIndex()] = new double[_capacityOfColumns];
			row[elem.getIndex()] = elem.getValue();
		}
	}

	public RowArrayMatrix(MatrixI matrix) {
		super(matrix.sizeOfRows(), matrix.sizeOfColumns());
		_capacityOfColumns = _sizeOfColumns;
		_arrays = new double[_sizeOfRows][];
		for (int i = 0; i < _sizeOfRows; i++)
			_arrays[i] = new double[_capacityOfColumns];
		for (Iterator<MatrixElementI> e = matrix.elements(); e
				.hasNext();) {
			MatrixElementI elem = e.next();
			int i = elem.getRowIndex();
			double[] row;
			if ((row = _arrays[i]) == null)
				row = _arrays[i] = new double[_capacityOfColumns];
			row[elem.getColumnIndex()] = elem.getValue();
		}
	}

	public RowArrayMatrix(double[][] array) {
		this(array, false);
	}

	public RowArrayMatrix(double[][] array, boolean useArrayInternally) {
		super(array);
		_capacityOfColumns = _sizeOfColumns;
		if (useArrayInternally) {
			_arrays = array;
			for (int i = 0; i < _sizeOfRows; i++)
				if (array[i] == null || array[i].length != _sizeOfColumns)
					throw new MatrixError(
							"All rows must be the non-null and the same length");
		} else {
			_arrays = new double[_sizeOfRows][];
			for (int i = 0; i < _sizeOfRows; i++) {
				double[] arow = array[i];
				if (arow == null)
					continue;
				double[] row = _arrays[i] = new double[_capacityOfColumns];
				Array.copy(arow.length, row, 0, 1, arow, 0, 1);
			}
		}
	}

	public int sizeOfElements() {
		int cnt = 0;
		for (int i = 0; i < _sizeOfRows; i++)
			if (_arrays[i] != null)
				cnt += _sizeOfColumns;
		return cnt;
	}

	public boolean isRowMajor() {
		return true;
	}

	public boolean isColumnMajor() {
		return false;
	}

	public int capacityOfRows() {
		return _arrays.length;
	}

	public int capacityOfColumns() {
		return _capacityOfColumns;
	}

	public void addRow(VectorI vector) {
		if (_sizeOfRows >= capacityOfRows())
			setCapacity(2 * _sizeOfRows, _sizeOfColumns);
		int i = _sizeOfRows;
		setSize(_sizeOfRows + 1, _sizeOfColumns);
		if (vector == null)
			return;
		double[] row = null;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int j = elem.getIndex();
			if (row == null)
				row = _arrays[i] = new double[_capacityOfColumns];
			row[j] = elem.getValue();
		}
	}

	public void addColumn(VectorI vector) {
		if (_sizeOfColumns >= capacityOfColumns())
			setCapacity(_sizeOfRows, 2 * _sizeOfColumns);
		int j = _sizeOfColumns;
		setSize(_sizeOfRows, _sizeOfColumns + 1);
		if (vector == null)
			return;
		double[] row = null;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if ((row = _arrays[i]) == null)
				row = _arrays[i] = new double[_capacityOfColumns];
			row[j] = elem.getValue();
		}
	}

	public void setSize(int sizeOfRows, int sizeOfColumns) {
		if (sizeOfRows >= capacityOfRows()
				|| sizeOfColumns >= capacityOfColumns())
			setCapacity(sizeOfRows, sizeOfColumns);

		if (sizeOfRows < _sizeOfRows) {
			for (int i = sizeOfRows; i < _sizeOfRows; i++)
				_arrays[i] = null;
		}
		_sizeOfRows = sizeOfRows;

		if (sizeOfColumns > _sizeOfColumns) {
			int n = sizeOfColumns - _sizeOfColumns;
			double[] row;
			if (n == 1) {
				for (int i = 0; i < _sizeOfRows; i++)
					if ((row = _arrays[i]) != null)
						row[_sizeOfColumns] = 0.0;
			} else {
				for (int i = 0; i < _sizeOfRows; i++)
					if ((row = _arrays[i]) != null)
						Array.copy(n, row, _sizeOfColumns, 1, 0.0);
			}
		}
		_sizeOfColumns = sizeOfColumns;
	}

	public void setCapacity(int capacityOfRows, int capacityOfColumns) {
		if (capacityOfRows > capacityOfRows())
			_arrays = Array.resize(capacityOfRows, _arrays);
		if (capacityOfColumns > _capacityOfColumns) {
			for (int i = 0; i < _sizeOfRows; i++) {
				double[] row;
				if ((row = _arrays[i]) == null)
					continue;
				_arrays[i] = Array.resize(capacityOfColumns, row);
			}
		}
		_capacityOfColumns = capacityOfColumns;
	}

	public void setElementAt(int row, int column, double value) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		double[] r;
		if ((r = _arrays[row]) == null)
			r = _arrays[row] = new double[_capacityOfColumns];
		r[column] = value;
	}

	public boolean isNull(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return _arrays[row] == null;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		double[] r;
		return ((r = _arrays[row]) == null) ? 0.0 : r[column];
	}

	public double[][] getArray() {
		double[][] array = new double[_sizeOfRows][_sizeOfColumns];
		for (int i = 0; i < _sizeOfRows; i++) {
			double[] row = _arrays[i];
			if (row == null)
				continue;
			Array.copy(_sizeOfRows, array[i], 0, 1, row, 0, 1);
		}
		return array;
	}

	public Iterator<MatrixElementI> rowElements(int row) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		return new RowEnum(row);
	}

	public Iterator<MatrixElementI> columnElements(int column) {
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return new ColumnEnum(column);
	}

	public Iterator<MatrixElementI> elements() {
		return new Enum();
	}

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		int n = columnEnd - columnBegin;
		if (n < 1)
			return 0.0;
		double sum = 0;
		while (rowBegin < rowEnd) {
			double[] row = _arrays[rowBegin];
			if (row == null)
				continue;
			sum += Array.sum(n, row, columnBegin, 1);
			rowBegin++;
		}
		return sum;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		int n = columnEnd - columnBegin;
		if (n < 1)
			return 0.0;
		double sum = 0;
		while (rowBegin < rowEnd) {
			double[] row = _arrays[rowBegin];
			if (row == null)
				continue;
			sum += Array.sumOfSquares(n, row, columnBegin, 1);
			rowBegin++;
		}
		return sum;
	}

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, double scaler) {
		int n = columnEnd - columnBegin;
		if (n < 1)
			return 0.0;
		double sum = 0;
		int cnt = 0;
		while (rowBegin < rowEnd) {
			double[] row = _arrays[rowBegin];
			if (row == null) {
				cnt += n;
				continue;
			}
			sum += Array
					.sumOfSquaredDifferences(n, row, columnBegin, 1, scaler);
			rowBegin++;
		}
		if (scaler != 0.0)
			sum += cnt * scaler * scaler;
		return sum;
	}

	private class Enum implements Iterator<MatrixElementI>, MatrixElementI {
		int _i = 0;
		int _j = 0;
		double[] _row = null;

		Enum() {
			while (_i < _sizeOfRows && (_row = _arrays[_i]) == null)
				_i++;
		}

		public boolean hasNext() {
			return _row != null;
		}

		public MatrixElementI next() {
			if (_row == null)
				return null;
			_mi = _i;
			_mj = _j;
			_mrow = _row;
			_mvalue = _row[_j++];
			if (_j >= _sizeOfColumns) {
				_i++;
				_row = null;
				_j = 0;
				while (_i < _sizeOfRows && (_row = _arrays[_i]) == null)
					_i++;
			}
			return this;
		}

		int _mi;
		int _mj;
		double _mvalue;
		double[] _mrow;

		public int getRowIndex() {
			return _mi;
		}

		public int getColumnIndex() {
			return _mj;
		}

		public double getValue() {
			return _mvalue;
		}

		public void setValue(double value) {
			_mrow[_mj] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	private class RowEnum implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _j = 0;
		double[] _row = null;

		RowEnum(int row) {
			_mi = row;
			_mrow = _row = _arrays[row];
		}

		public boolean hasNext() {
			return _row != null;
		}

		public MatrixElementI next() {
			if (_row == null)
				return null;
			_mj = _j;
			_mvalue = _row[_j++];
			if (_j >= _sizeOfColumns)
				_row = null;
			return this;
		}

		int _mi;
		int _mj;
		double _mvalue;
		double[] _mrow;

		public int getRowIndex() {
			return _mi;
		}

		public int getColumnIndex() {
			return _mj;
		}

		public double getValue() {
			return _mvalue;
		}

		public void setValue(double value) {
			_mrow[_mj] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	private class ColumnEnum implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _i = 0;
		double[] _row = null;

		ColumnEnum(int column) {
			_mj = column;
			while (_i < _sizeOfRows && (_row = _arrays[_i]) == null)
				_i++;
		}

		public boolean hasNext() {
			return _row != null;
		}

		public MatrixElementI next() {
			if (_row == null)
				return null;
			_mi = _i;
			_mrow = _row;
			_mvalue = _row[_mj];
			_i++;
			_row = null;
			while (_i < _sizeOfRows && (_row = _arrays[_i]) == null)
				_i++;
			return this;
		}

		int _mi;
		int _mj;
		double _mvalue;
		double[] _mrow;

		public int getRowIndex() {
			return _mi;
		}

		public int getColumnIndex() {
			return _mj;
		}

		public double getValue() {
			return _mvalue;
		}

		public void setValue(double value) {
			_mrow[_mj] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

}
