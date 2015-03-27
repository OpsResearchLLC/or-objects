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

public class ColumnArrayMatrix extends ArrayMatrix {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "capacityOfRows")
	int _capacityOfRows = 0;

	public ColumnArrayMatrix(int sizeOfRows, int sizeOfColumns) {
		super(sizeOfRows, sizeOfColumns);
		_capacityOfRows = sizeOfRows;
		_arrays = new double[sizeOfColumns][];
	}

	public ColumnArrayMatrix(int sizeOfRows, int sizeOfColumns,
			int capacityOfRows, int capacityOfColumns) {
		super(sizeOfRows, sizeOfColumns);
		_capacityOfRows = _sizeOfRows;
		_arrays = new double[capacityOfColumns][];
	}

	public ColumnArrayMatrix(VectorI vector) {
		super(vector.size(), vector.size());
		_capacityOfRows = _sizeOfRows;
		_arrays = new double[_sizeOfColumns][];
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			double[] column;
			if ((column = _arrays[elem.getIndex()]) == null)
				column = _arrays[elem.getIndex()] = new double[_capacityOfRows];
			column[elem.getIndex()] = elem.getValue();
		}
	}

	public ColumnArrayMatrix(MatrixI matrix) {
		super(matrix.sizeOfRows(), matrix.sizeOfColumns());
		_capacityOfRows = _sizeOfRows;
		_arrays = new double[_sizeOfColumns][];
		for (int j = 0; j < _sizeOfColumns; j++)
			_arrays[j] = new double[_capacityOfRows];
		for (Iterator<MatrixElementI> e = matrix.elements(); e
				.hasNext();) {
			MatrixElementI elem = e.next();
			int j = elem.getColumnIndex();
			double[] column;
			if ((column = _arrays[j]) == null)
				column = _arrays[j] = new double[_capacityOfRows];
			column[elem.getRowIndex()] = elem.getValue();
		}
	}

	public ColumnArrayMatrix(double[][] array) {
		super(array);
		_capacityOfRows = _sizeOfRows;
		_arrays = new double[_sizeOfColumns][_capacityOfRows];
		for (int j = 0; j < _sizeOfColumns; j++) {
			double[] column = _arrays[j];
			for (int i = 0; i < _sizeOfRows; i++) {
				double[] arow = array[i];
				if (arow == null || j >= arow.length)
					continue;
				column[i] = arow[j];
			}
		}
	}

	public int sizeOfElements() {
		int cnt = 0;
		for (int j = 0; j < _sizeOfColumns; j++)
			if (_arrays[j] != null)
				cnt += _sizeOfRows;
		return cnt;
	}

	public boolean isRowMajor() {
		return false;
	}

	public boolean isColumnMajor() {
		return true;
	}

	public int capacityOfRows() {
		return _capacityOfRows;
	}

	public int capacityOfColumns() {
		return _arrays.length;
	}

	public void addColumn(VectorI vector) {
		if (_sizeOfColumns >= capacityOfColumns())
			setCapacity(_sizeOfRows, 2 * _sizeOfColumns);
		int j = _sizeOfColumns;
		setSize(_sizeOfRows, _sizeOfColumns + 1);
		if (vector == null)
			return;
		double[] column = null;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (column == null)
				column = _arrays[j] = new double[_capacityOfRows];
			column[i] = elem.getValue();
		}
	}

	public void addRow(VectorI vector) {
		if (_sizeOfRows >= capacityOfRows())
			setCapacity(2 * _sizeOfRows, _sizeOfColumns);
		int i = _sizeOfRows;
		setSize(_sizeOfRows + 1, _sizeOfColumns);
		if (vector == null)
			return;
		double[] column = null;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			if ((column = _arrays[i]) == null)
				column = _arrays[i] = new double[_capacityOfRows];
			column[i] = elem.getValue();
		}
	}

	public void setSize(int sizeOfRows, int sizeOfColumns) {
		if (sizeOfRows >= capacityOfRows()
				|| sizeOfColumns >= capacityOfColumns())
			setCapacity(sizeOfRows, sizeOfColumns);

		if (sizeOfColumns < _sizeOfColumns) {
			for (int j = sizeOfColumns; j < _sizeOfColumns; j++)
				_arrays[j] = null;
		}
		_sizeOfColumns = sizeOfColumns;

		if (sizeOfRows > _sizeOfRows) {
			int m = sizeOfRows - _sizeOfRows;
			double[] column;
			if (m == 1) {
				for (int j = 0; j < _sizeOfColumns; j++)
					if ((column = _arrays[j]) != null)
						column[_sizeOfRows] = 0.0;
			} else {
				for (int j = 0; j < _sizeOfColumns; j++)
					if ((column = _arrays[j]) != null)
						Array.copy(m, column, _sizeOfRows, 1, 0.0);
			}
		}
		_sizeOfRows = sizeOfRows;
	}

	public void setCapacity(int capacityOfRows, int capacityOfColumns) {
		if (capacityOfColumns > capacityOfColumns())
			_arrays = Array.resize(capacityOfColumns, _arrays);
		if (capacityOfRows > _capacityOfRows) {
			for (int j = 0; j < _sizeOfColumns; j++) {
				double[] column;
				if ((column = _arrays[j]) == null)
					continue;
				_arrays[j] = Array.resize(capacityOfRows, column);
			}
		}
		_capacityOfRows = capacityOfRows;
	}

	public void setElementAt(int row, int column, double value) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		double[] c;
		if ((c = _arrays[column]) == null)
			c = _arrays[column] = new double[_capacityOfRows];
		c[row] = value;
	}

	public boolean isNull(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return _arrays[column] == null;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		double[] c;
		return ((c = _arrays[column]) == null) ? 0.0 : c[row];
	}

	public double[][] getArray() {
		double[][] array = new double[_sizeOfRows][_sizeOfColumns];
		for (int j = 0; j < _sizeOfColumns; j++) {
			double[] column = _arrays[j];
			if (column == null)
				continue;
			for (int i = 0; i < _sizeOfRows; i++)
				array[i][j] = column[i];
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
		int m = rowEnd - rowBegin;
		if (m < 1)
			return 0.0;
		double sum = 0;
		while (columnBegin < columnEnd) {
			double[] column = _arrays[columnBegin];
			if (column == null)
				continue;
			sum += Array.sum(m, column, rowBegin, 1);
			columnBegin++;
		}
		return sum;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		int m = rowEnd - rowBegin;
		if (m < 1)
			return 0.0;
		double sum = 0;
		while (columnBegin < columnEnd) {
			double[] column = _arrays[columnBegin];
			if (column == null)
				continue;
			sum += Array.sumOfSquares(m, column, rowBegin, 1);
			columnBegin++;
		}
		return sum;
	}

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, double scaler) {
		int m = rowEnd - rowBegin;
		if (m < 1)
			return 0.0;
		double sum = 0;
		int cnt = 0;
		while (columnBegin < columnEnd) {
			double[] column = _arrays[columnBegin];
			if (column == null) {
				cnt += m;
				continue;
			}
			sum += Array
					.sumOfSquaredDifferences(m, column, rowBegin, 1, scaler);
			columnBegin++;
		}
		if (scaler != 0.0)
			sum += cnt * scaler * scaler;
		return sum;

	}

	private class Enum implements Iterator<MatrixElementI>, MatrixElementI {
		int _i = 0;
		int _j = 0;
		double[] _column = null;

		Enum() {
			while (_j < _sizeOfColumns && (_column = _arrays[_i]) == null)
				_j++;
		}

		public boolean hasNext() {
			return _column != null;
		}

		public MatrixElementI next() {
			if (_column == null)
				return null;
			_mi = _i;
			_mj = _j;
			_mcolumn = _column;
			_mvalue = _column[_i++];
			if (_i >= _sizeOfRows) {
				_j++;
				_column = null;
				_i = 0;
				while (_j < _sizeOfColumns && (_column = _arrays[_j]) == null)
					_j++;
			}
			return this;
		}

		int _mi;
		int _mj;
		double _mvalue;
		double[] _mcolumn;

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
			_mcolumn[_mi] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	private class ColumnEnum implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _i = 0;
		double[] _column = null;

		ColumnEnum(int column) {
			_mj = column;
			_mcolumn = _column = _arrays[column];
		}

		public boolean hasNext() {
			return _column != null;
		}

		public MatrixElementI next() {
			if (_column == null)
				return null;
			_mi = _i;
			_mvalue = _column[_i++];
			if (_i >= _sizeOfRows)
				_column = null;
			return this;
		}

		int _mi;
		int _mj;
		double _mvalue;
		double[] _mcolumn;

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
			_mcolumn[_mi] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	private class RowEnum implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _j = 0;
		double[] _column = null;

		RowEnum(int row) {
			_mi = row;
			while (_j < _sizeOfColumns && (_column = _arrays[_j]) == null)
				_j++;
		}

		public boolean hasNext() {
			return _column != null;
		}

		public MatrixElementI next() {
			if (_column == null)
				return null;
			_mj = _j;
			_mcolumn = _column;
			_mvalue = _column[_mi];
			_j++;
			_column = null;
			while (_j < _sizeOfColumns && (_column = _arrays[_j]) == null)
				_j++;
			return this;
		}

		int _mi;
		int _mj;
		double _mvalue;
		double[] _mcolumn;

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
			_mcolumn[_mi] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

}
