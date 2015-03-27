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
import javax.xml.bind.annotation.XmlElementWrapper;

import com.opsresearch.orobjects.lib.util.Array;

public abstract class ContiguousMatrix extends Matrix implements
		ContiguousMatrixI, SizableMatrixI {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "sizeOfRows")
	int _sizeOfRows = 0;
	@XmlElement(name = "sizeOfColumns")
	int _sizeOfColumns = 0;
	@XmlElement(name = "capacityOfRows")
	int _capacityOfRows = 0;
	@XmlElement(name = "capacityOfColumns")
	int _capacityOfColumns = 0;

	@XmlElementWrapper(name = "array")
	@XmlElement(name = "item")
	double[] _values = null;

	public ContiguousMatrix(int sizeOfRows, int sizeOfColumns,
			int capacityOfRows, int capacityOfColumns) {
		_sizeOfRows = sizeOfRows;
		_sizeOfColumns = sizeOfColumns;
		_capacityOfRows = Math.max(sizeOfRows, capacityOfRows);
		_capacityOfColumns = Math.max(sizeOfColumns, capacityOfColumns);
		_values = Array.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(double[][] array) {
		int n = 0;
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row != null && row.length > n)
				n = row.length;
		}
		_capacityOfRows = _sizeOfRows = array.length;
		_capacityOfColumns = _sizeOfColumns = n;
		_values = Array.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(MatrixI matrix) {
		_capacityOfRows = _sizeOfRows = matrix.sizeOfRows();
		_capacityOfColumns = _sizeOfColumns = matrix.sizeOfColumns();
		_values = Array.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(VectorI vector) {
		_capacityOfRows = _sizeOfRows = vector.size();
		_capacityOfColumns = _sizeOfColumns = vector.size();
		_values = Array.resize(_capacityOfRows * _capacityOfColumns, _values);
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), elem.getIndex(), elem.getValue());
		}
	}

	public ContiguousMatrixI submatrix(int beginRow, int beginColumn) {
		return new Sub(_values, sizeOfRows() - beginRow, sizeOfColumns()
				- beginColumn, getOffset(beginRow, beginColumn),
				getRowIncrement(), getColumnIncrement());
	}

	public ContiguousMatrixI submatrix(int beginRow, int endRow,
			int beginColumn, int endColumn) {
		return new Sub(_values, endRow - beginRow, endColumn - beginColumn,
				getOffset(beginRow, beginColumn), getRowIncrement(),
				getColumnIncrement());
	}

	public Iterator<MatrixElementI> rowElements(int row) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		return submatrix(row, row + 1, 0, sizeOfColumns()).elements();
	}

	public Iterator<MatrixElementI> columnElements(int column) {
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return submatrix(0, sizeOfRows(), column, column + 1).elements();
	}

	public Iterator<MatrixElementI> elements() {
		return new Enum(_values, sizeOfRows(), sizeOfColumns(), 0,
				getRowIncrement(), getColumnIncrement());
	}

	public void setElements(double value) {
		Array.copy(_values.length, _values, 0, 1, value);
	}

	public double[] getValueArray() {
		return _values;
	}

	public ArrayVectorI rowVector(int row) {
		return new ArrayVector.Sub(_values, sizeOfColumns(), getOffset(row, 0),
				getColumnIncrement());
	}

	public ArrayVectorI columnVector(int column) {
		return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, column),
				getRowIncrement());
	}

	public ArrayVectorI diagonalVector() {
		return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, 0),
				getRowIncrement() + getColumnIncrement());
	}

	public boolean isNull(int row, int column) {
		return false;
	}

	public int sizeOfRows() {
		return _sizeOfRows;
	}

	public int sizeOfColumns() {
		return _sizeOfColumns;
	}

	public int sizeOfElements() {
		return _sizeOfRows * _sizeOfColumns;
	}

	public int capacityOfRows() {
		return _capacityOfRows;
	}

	public int capacityOfColumns() {
		return _capacityOfColumns;
	}

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		int beg = getOffset(rowBegin, columnBegin);
		double sum = 0;
		int m = rowEnd - rowBegin;
		int n = columnEnd - columnBegin;
		if (m < 1 || n < 1)
			return sum;
		int inci = getRowIncrement();
		int incj = getColumnIncrement();
		if (incj < inci) {
			for (int i = 0; i < m; i++, beg += inci) {
				sum += Array.sum(n, _values, beg, incj);
			}
		} else {
			for (int j = 0; j < n; j++, beg += incj) {
				sum += Array.sum(m, _values, beg, inci);
			}
		}
		return sum;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		int beg = getOffset(rowBegin, columnBegin);
		double sum = 0;
		int m = rowEnd - rowBegin;
		int n = columnEnd - columnBegin;
		if (m < 1 || n < 1)
			return sum;
		int inci = getRowIncrement();
		int incj = getColumnIncrement();
		if (incj < inci) {
			for (int i = 0; i < m; i++, beg += inci) {
				sum += Array.sumOfSquares(n, _values, beg, incj);
			}
		} else {
			for (int j = 0; j < n; j++, beg += incj) {
				sum += Array.sumOfSquares(m, _values, beg, inci);
			}
		}
		return sum;
	}

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, double scaler) {
		int beg = getOffset(rowBegin, columnBegin);
		double sum = 0;
		int m = rowEnd - rowBegin;
		int n = columnEnd - columnBegin;
		if (m < 1 || n < 1)
			return sum;
		int inci = getRowIncrement();
		int incj = getColumnIncrement();
		if (incj < inci) {
			for (int i = 0; i < m; i++, beg += inci) {
				sum += Array.sumOfSquaredDifferences(n, _values, beg, incj,
						scaler);
			}
		} else {
			for (int j = 0; j < n; j++, beg += incj) {
				sum += Array.sumOfSquaredDifferences(m, _values, beg, inci,
						scaler);
			}
		}
		return sum;
	}

	static class Enum implements Iterator<MatrixElementI>, MatrixElementI {
		int _k, _m, _n, _inci, _incj, _beg;
		int _i = 0;
		int _j = 0;
		boolean _rowMajor = false;
		double[] _values;

		public Enum(double[] values, int m, int n, int beg, int inci, int incj) {
			_k = _beg = beg;
			_values = values;
			if (incj < inci) {
				_rowMajor = true;
				_m = m;
				_n = n;
				_inci = inci;
				_incj = incj;
			} else {
				_m = n;
				_n = m;
				_inci = incj;
				_incj = inci;
			}
		}

		public boolean hasNext() {
			return _i < _m;
		}

		public MatrixElementI next() {
			if (_i >= _m)
				return null;
			if (_rowMajor) {
				_mrow = _i;
				_mcol = _j;
			} else {
				_mrow = _j;
				_mcol = _i;
			}
			_mvalue = _values[_mcoef = _k];
			if (++_j == _n) {
				_j = 0;
				_i++;
				_k = _beg + _i * _inci;
			} else
				_k += _incj;
			return this;
		}

		int _mrow;
		int _mcol;
		int _mcoef;
		double _mvalue;

		public int getRowIndex() {
			return _mrow;
		}

		public int getColumnIndex() {
			return _mcol;
		}

		public double getValue() {
			return _mvalue;
		}

		public void setValue(double value) {
			_values[_mcoef] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	static public class Sub extends Matrix implements ContiguousMatrixI

	{
		private static final long serialVersionUID = 1L;
		int _m, _n, _beg, _inci, _incj;
		double[] _values;

		public Sub(double[] values, int m, int n, int beg, int inci, int incj) {
			_values = values;
			_m = m;
			_n = n;
			_beg = beg;
			_inci = inci;
			_incj = incj;
		}

		public ContiguousMatrixI submatrix(int beginRow, int beginColumn) {
			return new Sub(_values, sizeOfRows() - beginRow, sizeOfColumns()
					- beginColumn, getOffset(beginRow, beginColumn),
					getRowIncrement(), getColumnIncrement());
		}

		public ContiguousMatrixI submatrix(int beginRow, int endRow,
				int beginColumn, int endColumn) {
			return new Sub(_values, endRow - beginRow, endColumn - beginColumn,
					getOffset(beginRow, beginColumn), getRowIncrement(),
					getColumnIncrement());
		}

		public Iterator<MatrixElementI> rowElements(int row) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row
						+ ", sizeOfRows=" + _m);
			return submatrix(row, row + 1, 0, _n).elements();
		}

		public Iterator<MatrixElementI> columnElements(int column) {
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column
						+ ", sizeOfColumns=" + _n);
			return submatrix(0, sizeOfRows(), column, column + 1).elements();
		}

		public Iterator<MatrixElementI> elements() {
			return new Enum(_values, sizeOfRows(), sizeOfColumns(), _beg,
					getRowIncrement(), getColumnIncrement());
		}

		public void setElements(double value) {
			int siz1, beg2 = _beg, inc1, inc2, end2;
			if (_inci < _incj) {
				siz1 = _m;
				inc1 = _inci;
				inc2 = _incj;
				end2 = _beg + _n * _incj;
			} else {
				siz1 = _n;
				inc1 = _incj;
				inc2 = _inci;
				end2 = _beg + _m * _inci;
			}
			while (beg2 < end2) {
				Array.copy(siz1, _values, beg2, inc1, value);
				beg2 += inc2;
			}
		}

		public int getOffset(int row, int column) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row
						+ ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column
						+ ", sizeOfColumns=" + _n);
			return _beg + row * _inci + column * _incj;
		}

		public int getRowIncrement() {
			return _inci;
		}

		public int getColumnIncrement() {
			return _incj;
		}

		public boolean isRowMajor() {
			return _incj < _inci;
		}

		public boolean isColumnMajor() {
			return _inci < _incj;
		}

		public double[] getValueArray() {
			return _values;
		}

		public ArrayVectorI rowVector(int row) {
			return new ArrayVector.Sub(_values, sizeOfColumns(), getOffset(row,
					0), getColumnIncrement());
		}

		public ArrayVectorI columnVector(int column) {
			return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0,
					column), getRowIncrement());
		}

		public ArrayVectorI diagonalVector() {
			return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, 0),
					getRowIncrement() + getColumnIncrement());
		}

		public boolean isNull(int row, int column) {
			return false;
		}

		public int sizeOfRows() {
			return _m;
		}

		public int sizeOfColumns() {
			return _n;
		}

		public int sizeOfElements() {
			return _m * _n;
		}

		public void setElementAt(int row, int column, double value) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row
						+ ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column
						+ ", sizeOfColumns=" + _n);
			_values[getOffset(row, column)] = value;
		}

		public double elementAt(int row, int column) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row
						+ ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column
						+ ", sizeOfColumns=" + _n);
			return _values[getOffset(row, column)];
		}

		public double[][] getArray() {
			double[][] array = new double[_m][_n];
			for (int i = 0; i < array.length; i++) {
				double[] row = array[i];
				if (row == null)
					continue;
				Array.copy(row.length, row, 0, 1, _values, getOffset(i, 0),
						_inci);
			}
			return array;
		}

		public double sum(int rowBegin, int rowEnd, int columnBegin,
				int columnEnd) {
			int beg = getOffset(rowBegin, columnBegin);
			double sum = 0;
			int m = rowEnd - rowBegin;
			int n = columnEnd - columnBegin;
			if (m < 1 || n < 1)
				return sum;
			if (_incj < _inci) {
				for (int i = 0; i < m; i++, beg += _inci) {
					sum += Array.sum(n, _values, beg, _incj);
				}
			} else {
				for (int j = 0; j < n; j++, beg += _incj) {
					sum += Array.sum(m, _values, beg, _inci);
				}
			}
			return sum;
		}

		public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
				int columnEnd) {
			int beg = getOffset(rowBegin, columnBegin);
			double sum = 0;
			int m = rowEnd - rowBegin;
			int n = columnEnd - columnBegin;
			if (m < 1 || n < 1)
				return sum;
			if (_incj < _inci) {
				for (int i = 0; i < m; i++, beg += _inci) {
					sum += Array.sum(n, _values, beg, _incj);
				}
			} else {
				for (int j = 0; j < n; j++, beg += _incj) {
					sum += Array.sumOfSquares(m, _values, beg, _inci);
				}
			}
			return sum;
		}

		public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
				int columnBegin, int columnEnd, double scaler) {
			int beg = getOffset(rowBegin, columnBegin);
			double sum = 0;
			int m = rowEnd - rowBegin;
			int n = columnEnd - columnBegin;
			if (m < 1 || n < 1)
				return sum;
			if (_incj < _inci) {
				for (int i = 0; i < m; i++, beg += _inci) {
					sum += Array.sum(n, _values, beg, _incj);
				}
			} else {
				for (int j = 0; j < n; j++, beg += _incj) {
					sum += Array.sumOfSquaredDifferences(m, _values, beg,
							_inci, scaler);
				}
			}
			return sum;
		}

	}

}
