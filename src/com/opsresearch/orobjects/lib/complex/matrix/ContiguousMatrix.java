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
import com.opsresearch.orobjects.lib.real.matrix.MatrixError;
import com.opsresearch.orobjects.lib.util.Array;
import com.opsresearch.orobjects.lib.util.ComplexArray;

public abstract class ContiguousMatrix extends Matrix implements ContiguousMatrixI, SizableMatrixI {
	private static final long serialVersionUID = 1L;
	int _sizeOfRows = 0;
	int _sizeOfColumns = 0;
	protected int _capacityOfRows = 0;
	protected int _capacityOfColumns = 0;
	double[] _values = null;

	public ContiguousMatrix(int sizeOfRows, int sizeOfColumns, int capacityOfRows, int capacityOfColumns) {
		_sizeOfRows = sizeOfRows;
		_sizeOfColumns = sizeOfColumns;
		_capacityOfRows = Math.max(sizeOfRows, capacityOfRows);
		_capacityOfColumns = Math.max(sizeOfColumns, capacityOfColumns);
		_values = ComplexArray.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(double[][] array) {
		int n = 0;
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row != null && row.length > n)
				n = row.length;
		}
		_capacityOfRows = _sizeOfRows = array.length;
		_capacityOfColumns = _sizeOfColumns = n / 2;
		_values = ComplexArray.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(double[][] real, double[][] imag) {
		int n = 0;
		if (real == null && imag == null)
			throw new MatrixError("Both arrays can't be null.");
		if (real != null && imag != null && real.length != imag.length)
			throw new MatrixError("The real and imag arrays must be the same number of rows: " + real.length + ", "
					+ imag.length);

		if (real != null)
			for (int i = 0; i < real.length; i++) {
				double[] row = real[i];
				if (row != null && row.length > n)
					n = row.length;
			}

		if (imag != null)
			for (int i = 0; i < imag.length; i++) {
				double[] row = imag[i];
				if (row != null && row.length > n)
					n = row.length;
			}

		_capacityOfRows = _sizeOfRows = real != null ? real.length : imag.length;
		_capacityOfColumns = _sizeOfColumns = n;
		_values = ComplexArray.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(MatrixI matrix) {
		_capacityOfRows = _sizeOfRows = matrix.sizeOfRows();
		_capacityOfColumns = _sizeOfColumns = matrix.sizeOfColumns();
		_values = ComplexArray.resize(_capacityOfRows * _capacityOfColumns, _values);
	}

	public ContiguousMatrix(VectorI vector) {
		_capacityOfRows = _sizeOfRows = vector.size();
		_capacityOfColumns = _sizeOfColumns = vector.size();
		_values = ComplexArray.resize(_capacityOfRows * _capacityOfColumns, _values);
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), elem.getIndex(), elem.getValue());
		}
	}

	public void setElements(ComplexI value) {
		ComplexArray.copy(_values.length / 2, _values, 0, 1, value);
	}

	public ArrayVectorI rowVector(int row) {
		return new ArrayVector.Sub(_values, sizeOfColumns(), getOffset(row, 0), getColumnIncrement());
	}

	public ArrayVectorI columnVector(int column) {
		return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, column), getRowIncrement());
	}

	public ArrayVectorI diagonalVector() {
		return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, 0), getRowIncrement() + getColumnIncrement());
	}

	public double[] getValueArray() {
		return _values;
	}

	public boolean isNull(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows=" + _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column + ", sizeOfColumns=" + _sizeOfColumns);
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

	public Complex sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		int beg = getOffset(rowBegin, columnBegin);
		Complex sum = new Complex();
		int m = rowEnd - rowBegin;
		int n = columnEnd - columnBegin;
		if (m < 1 || n < 1)
			return sum;
		int inci = getRowIncrement();
		int incj = getColumnIncrement();
		Complex results = new Complex();
		if (incj < inci) {
			for (int i = 0; i < m; i++, beg += inci) {
				sum.add(ComplexArray.sum(n, _values, beg, incj, results));
			}
		} else {
			for (int j = 0; j < n; j++, beg += incj) {
				sum.add(ComplexArray.sum(m, _values, beg, inci, results));
			}
		}
		return sum;
	}

	public Complex sumOfSquares(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		int beg = getOffset(rowBegin, columnBegin);
		Complex sum = new Complex();
		int m = rowEnd - rowBegin;
		int n = columnEnd - columnBegin;
		if (m < 1 || n < 1)
			return sum;
		int inci = getRowIncrement();
		int incj = getColumnIncrement();
		Complex results = new Complex();
		if (incj < inci) {
			for (int i = 0; i < m; i++, beg += inci) {
				sum.add(ComplexArray.sumOfSquares(n, _values, beg, incj, results));
			}
		} else {
			for (int j = 0; j < n; j++, beg += incj) {
				sum.add(ComplexArray.sumOfSquares(m, _values, beg, inci, results));
			}
		}
		return sum;
	}

	public Complex sumOfSquaredDifferences(int rowBegin, int rowEnd, int columnBegin, int columnEnd, ComplexI scaler) {
		int beg = getOffset(rowBegin, columnBegin);
		Complex sum = new Complex();
		int m = rowEnd - rowBegin;
		int n = columnEnd - columnBegin;
		if (m < 1 || n < 1)
			return sum;
		int inci = getRowIncrement();
		int incj = getColumnIncrement();
		Complex results = new Complex();
		if (incj < inci) {
			for (int i = 0; i < m; i++, beg += inci) {
				sum.add(ComplexArray.sumOfSquares(n, _values, beg, incj, results));
			}
		} else {
			for (int j = 0; j < n; j++, beg += incj) {
				sum.add(ComplexArray.sumOfSquaredDifferences(m, _values, beg, inci, scaler, results));
			}
		}
		return sum;
	}

	static class Enum implements ComplexI, Iterator<MatrixElementI>, MatrixElementI {
		private int _siz1, _siz2, _beg1, _beg2, _inc1, _inc2;
		private int _cnt1 = 0;
		private int _cnt2 = 0;
		private boolean _rowMajor;
		private double[] _values;

		public Enum(double[] values, int m, int n, int beg, int inci, int incj) {
			_beg1 = _beg2 = beg;
			_values = values;
			if (inci < incj) {
				_rowMajor = true;
				_siz1 = m;
				_siz2 = n;
				_inc1 = inci;
				_inc2 = incj;
			} else {
				_rowMajor = false;
				_siz1 = n;
				_siz2 = m;
				_inc1 = incj;
				_inc2 = inci;
			}
		}

		public boolean hasNext() {
			return _cnt2 < _siz2;
		}

		public MatrixElementI next() {
			if (_cnt2 >= _siz2)
				return null;
			if (_rowMajor) {
				_mrow = _cnt1++;
				_mcol = _cnt2;
			} else {
				_mrow = _cnt2;
				_mcol = _cnt1++;
			}
			_mcoef = 2 * _beg1;
			_mreal = _values[_mcoef];
			_mimag = _values[_mcoef + 1];
			if (++_cnt1 >= _siz1) {
				_cnt2++;
				_cnt1 = 0;
				_beg1 = (_beg2 += _inc2);
			} else {
				_cnt1++;
				_beg1 += _inc1;
			}
			return this;
		}

		private int _mrow;
		private int _mcol;
		private int _mcoef;
		private double _mreal, _mimag;

		public int getRowIndex() {
			return _mrow;
		}

		public int getColumnIndex() {
			return _mcol;
		}

		public ComplexI getValue() {
			return this;
		}

		public void setValue(ComplexI value) {
			_values[_mcoef] = _mreal = value.getReal();
			_values[_mcoef + 1] = _mimag = value.getImag();
		}

		public double getReal() {
			return _mreal;
		}

		public double getImag() {
			return _mimag;
		}

		@Override
		public void remove() {
		}
	}

	static public class Sub extends Matrix implements ContiguousMatrixI

	{
		private static final long serialVersionUID = 1L;
		private int _m, _n, _beg, _inci, _incj;
		private double[] _values;

		public Sub(double[] values, int m, int n, int beg, int inci, int incj) {
			_values = values;
			_m = m;
			_n = n;
			_beg = beg;
			_inci = inci;
			_incj = incj;
		}

		public ContiguousMatrixI submatrix(int beginRow, int beginColumn) {
			return new Sub(_values, sizeOfRows() - beginRow, sizeOfColumns() - beginColumn, getOffset(beginRow,
					beginColumn), getRowIncrement(), getColumnIncrement());
		}

		public ContiguousMatrixI submatrix(int beginRow, int endRow, int beginColumn, int endColumn) {
			return new Sub(_values, endRow - beginRow, endColumn - beginColumn, getOffset(beginRow, beginColumn),
					getRowIncrement(), getColumnIncrement());
		}

		public Iterator<MatrixElementI> rowElements(int row) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows=" + _m);
			return submatrix(row, row + 1, 0, _n).elements();
		}

		public Iterator<MatrixElementI> columnElements(int column) {
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column + ", sizeOfColumns=" + _n);
			return submatrix(0, sizeOfRows(), column, column + 1).elements();
		}

		public Iterator<MatrixElementI> elements() {
			return new Enum(_values, sizeOfRows(), sizeOfColumns(), _beg, getRowIncrement(), getColumnIncrement());
		}

		public void setElements(ComplexI value) {
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
				ComplexArray.copy(siz1, _values, beg2, inc1, value);
				beg2 += inc2;
			}
		}

		public int getOffset(int row, int column) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column + ", sizeOfColumns=" + _n);
			return row * _inci + column * _incj;
		}

		public int getRowIncrement() {
			return _inci;
		}

		public int getColumnIncrement() {
			return _incj;
		}

		public boolean isRowMajor() {
			return _inci < _incj;
		}

		public boolean isColumnMajor() {
			return _incj < _inci;
		}

		public double[] getValueArray() {
			return _values;
		}

		public ArrayVectorI rowVector(int row) {
			return new ArrayVector.Sub(_values, sizeOfColumns(), getOffset(row, 0), getColumnIncrement());
		}

		public ArrayVectorI columnVector(int column) {
			return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, column), getRowIncrement());
		}

		public ArrayVectorI diagonalVector() {
			return new ArrayVector.Sub(_values, sizeOfRows(), getOffset(0, 0), getRowIncrement() + getColumnIncrement());
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

		public void setElementAt(int row, int column, ComplexI value) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column + ", sizeOfColumns=" + _n);
			int i = 2 * getOffset(row, column);
			_values[i] = value.getReal();
			_values[i + 1] = value.getImag();
		}

		public Complex elementAt(int row, int column) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column + ", sizeOfColumns=" + _n);
			int i = 2 * getOffset(row, column);
			return new Complex(_values[i], _values[i + 1]);
		}

		public Complex elementAt(int row, int column, Complex results) {
			if (row < 0 || row >= _m)
				throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows=" + _m);
			if (column < 0 || column >= _n)
				throw new IndexOutOfBoundsException("Column=" + column + ", sizeOfColumns=" + _n);
			int i = 2 * getOffset(row, column);
			if (results == null)
				return new Complex(_values[i], _values[i + 1]);
			results.real = _values[i];
			results.imag = _values[i + 1];
			return results;
		}

		public double[][] getArray() {
			double[][] array = new double[_m][_n];
			for (int i = 0; i < array.length; i++) {
				double[] row = array[i];
				if (row == null)
					continue;
				Array.copy(row.length, row, 0, 1, _values, getOffset(i, 0), _inci);
			}
			return array;
		}

		public Complex sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
			int beg = getOffset(rowBegin, columnBegin);
			Complex sum = new Complex();
			int m = rowEnd - rowBegin;
			int n = columnEnd - columnBegin;
			if (m < 1 || n < 1)
				return sum;
			Complex results = new Complex();
			if (_incj < _inci) {
				for (int i = 0; i < m; i++, beg += _inci) {
					sum.add(ComplexArray.sum(n, _values, beg, _incj, results));
				}
			} else {
				for (int j = 0; j < n; j++, beg += _incj) {
					sum.add(ComplexArray.sum(m, _values, beg, _inci, results));
				}
			}
			return sum;
		}

		public Complex sumOfSquares(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
			int beg = getOffset(rowBegin, columnBegin);
			Complex sum = new Complex();
			int m = rowEnd - rowBegin;
			int n = columnEnd - columnBegin;
			if (m < 1 || n < 1)
				return sum;
			Complex results = new Complex();
			if (_incj < _inci) {
				for (int i = 0; i < m; i++, beg += _inci) {
					sum.add(ComplexArray.sumOfSquares(n, _values, beg, _incj, results));
				}
			} else {
				for (int j = 0; j < n; j++, beg += _incj) {
					sum.add(ComplexArray.sumOfSquares(m, _values, beg, _inci, results));
				}
			}
			return sum;
		}

		public Complex sumOfSquaredDifferences(int rowBegin, int rowEnd, int columnBegin, int columnEnd, ComplexI scaler) {
			int beg = getOffset(rowBegin, columnBegin);
			Complex sum = new Complex();
			int m = rowEnd - rowBegin;
			int n = columnEnd - columnBegin;
			if (m < 1 || n < 1)
				return sum;
			Complex results = new Complex();
			if (_incj < _inci) {
				for (int i = 0; i < m; i++, beg += _inci) {
					sum.add(ComplexArray.sumOfSquares(n, _values, beg, _incj, results));
				}
			} else {
				for (int j = 0; j < n; j++, beg += _incj) {
					sum.add(ComplexArray.sumOfSquaredDifferences(m, _values, beg, _inci, scaler, results));
				}
			}
			return sum;
		}

	}

}
