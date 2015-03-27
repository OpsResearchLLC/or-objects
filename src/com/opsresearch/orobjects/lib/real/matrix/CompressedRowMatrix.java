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

public class CompressedRowMatrix extends CompressedMatrix {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "rowSize")
	int[] _rowSize = null;
	@XmlElement(name = "rowBegin")
	int[] _rowBegin = null;
	@XmlElement(name = "columnIndex")
	int[] _columnIndex = null;

	@XmlElement(name = "rowIndex")
	int[] _rowIndex = null;
	@XmlElement(name = "columnEnd")
	int[] _columnEnd = null;
	@XmlElement(name = "valueIndex")
	int[] _valueIndex = null;

	public CompressedRowMatrix(int capacityOfRows, int sizeOfColumns,
			int fillInSpace) {
		this(capacityOfRows, sizeOfColumns, fillInSpace, capacityOfRows
				* (1 + fillInSpace));
	}

	public CompressedRowMatrix(int capacityOfRows, int sizeOfColumns,
			int fillInSpace, int capacityOfElements) {
		_sizeOfRows = 0;
		_fillInSpace = fillInSpace;
		_sizeOfColumns = sizeOfColumns;
		resizeRows(capacityOfRows);
		resizeElements(capacityOfElements);
	}

	public CompressedRowMatrix(double[][] array, int fillInSpace) {
		super(array);
		_fillInSpace = fillInSpace;
		_sizeOfRows = array.length;
		resizeRows(_sizeOfRows);
		resizeElements(_sizeOfElements + _sizeOfRows * fillInSpace);
		int k = 0;
		for (int i = 0; i < _sizeOfRows; i++) {
			double[] row = array[i];
			_rowBegin[i] = k;
			for (int j = 0; j < row.length; j++) {
				_rowSize[i]++;
				_values[k] = row[j];
				_columnIndex[k++] = j;
			}
			k += _fillInSpace;
		}
	}

	public CompressedRowMatrix(double[][] array, int fillInSpace, double epsilon) {
		super(array, epsilon);
		_fillInSpace = fillInSpace;
		_sizeOfRows = array.length;
		resizeRows(_sizeOfRows);
		resizeElements(_sizeOfElements + _sizeOfRows * fillInSpace);
		int k = 0;
		for (int i = 0; i < _sizeOfRows; i++) {
			double[] row = array[i];
			_rowBegin[i] = k;
			for (int j = 0; j < row.length; j++) {
				double v = row[j];
				if (equals(v, 0.0))
					continue;
				_rowSize[i]++;
				_values[k] = v;
				_columnIndex[k++] = j;
			}
			k += _fillInSpace;
		}
	}

	public CompressedRowMatrix(MatrixI matrix, int fillInSpace) {
		_sizeOfRows = matrix.sizeOfRows();
		_sizeOfColumns = matrix.sizeOfColumns();
		_sizeOfElements = matrix.sizeOfElements();
		_fillInSpace = fillInSpace;
		resizeRows(_sizeOfRows);
		resizeElements(_sizeOfElements + _sizeOfRows * fillInSpace);
		int k = 0;
		for (int i = 0; i < _sizeOfRows; i++) {
			_rowBegin[i] = k;
			for (Iterator<MatrixElementI> e = matrix.rowElements(i); e
					.hasNext();) {
				MatrixElementI elem = e.next();
				int j = elem.getColumnIndex();
				double v = elem.getValue();
				_rowSize[i]++;
				_values[k] = v;
				_columnIndex[k++] = j;
			}
			k += _fillInSpace;
		}
	}

	public CompressedRowMatrix(VectorI vector, int fillInSpace) {
		_sizeOfRows = vector.size();
		_sizeOfColumns = vector.size();
		_sizeOfElements = vector.size();
		_fillInSpace = fillInSpace;
		resizeRows(_sizeOfRows);
		resizeElements(_sizeOfElements + _sizeOfRows * fillInSpace);
		int k = 0;
		for (int i = 0; i < _sizeOfRows; i++) {
			double v = vector.elementAt(i);
			_rowSize[i]++;
			_values[k] = v;
			_columnIndex[k++] = i;
		}
		k += _fillInSpace;
	}

	private void resizeRows(int capacityOfRows) {
		_rowSize = Array.resize(capacityOfRows, _rowSize);
		_rowBegin = Array.resize(capacityOfRows, _rowBegin);
	}

	private void resizeElements(int capacityOfElements) {
		_values = Array.resize(capacityOfElements, _values);
		_columnIndex = Array.resize(capacityOfElements, _columnIndex);
	}

	public int sizeOfElements() {
		return _sizeOfElements;
	}

	public int capacityOfRows() {
		return _rowBegin.length;
	}

	public int capacityOfColumns() {
		return Integer.MAX_VALUE;
	}

	public int[] getColumnIndexArray() {
		return _columnIndex;
	}

	public int[] getRowSizeArray() {
		return _rowSize;
	}

	public int[] getRowBeginArray() {
		return _rowBegin;
	}

	public void deleteColumnIndex() {
		_rowIndex = null;
		_columnEnd = null;
		_valueIndex = null;
	}

	public void buildColumnIndex() {
		_rowIndex = new int[_sizeOfElements];
		_columnEnd = new int[_sizeOfColumns];
		_valueIndex = new int[_sizeOfElements];

		int[] cnts = new int[_sizeOfColumns];
		for (int i = 0; i < _sizeOfRows; i++) {
			int beg = _rowBegin[i];
			for (int cnt = _rowSize[i]; cnt > 0; cnt--, beg++)
				cnts[_columnIndex[beg]]++;
		}

		int end = 0;
		for (int j = 0; j < _sizeOfColumns; j++)
			_columnEnd[j] = (end += cnts[j]);

		Array.copy(_sizeOfColumns, cnts, 0, 1, 0);
		for (int i = 0; i < _sizeOfRows; i++) {
			int beg = _rowBegin[i];
			for (int cnt = _rowSize[i]; cnt > 0; cnt--, beg++) {
				int j = _columnIndex[beg];
				int l = (j == 0 ? 0 : _columnEnd[j - 1]) + cnts[j];
				_rowIndex[l] = i;
				_valueIndex[l] = beg;
				cnts[j]++;
			}
		}
	}

	public int[] getRowIndexArray() {
		return _rowIndex;
	}

	public int[] getColumnEndArray() {
		return _columnEnd;
	}

	public int[] getValueIndexArray() {
		return _valueIndex;
	}

	public boolean isRowMajor() {
		return true;
	}

	public boolean isColumnMajor() {
		return false;
	}

	public void addRow(VectorI vector) {
		int i = _sizeOfRows;
		setSize(_sizeOfRows + 1, _sizeOfColumns);
		if (vector == null)
			return;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(i, elem.getIndex(), elem.getValue());
		}
	}

	public void addColumn(VectorI vector) {
		int j = _sizeOfColumns;
		setSize(_sizeOfRows, _sizeOfColumns + 1);
		if (vector == null)
			return;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), j, elem.getValue());
		}
	}

	public void setSize(int sizeOfRows, int sizeOfColumns) {
		if (sizeOfRows < _sizeOfRows || sizeOfColumns < _sizeOfColumns) {
			_sizeOfRows = 0;
			_sizeOfColumns = 0;
		}
		if (sizeOfRows > capacityOfRows())
			setCapacity(sizeOfRows, sizeOfColumns);
		if (sizeOfRows > _sizeOfRows) {
			int n = sizeOfRows - _sizeOfRows;
			Array.copy(n, _rowSize, _sizeOfRows, 1, 0);
			int beg = _sizeOfRows == 0 ? 0 : _rowBegin[_sizeOfRows - 1]
					+ _rowSize[_sizeOfRows - 1] + _fillInSpace;
			for (int i = _sizeOfRows; i < sizeOfRows; i++) {
				_rowBegin[i] = beg;
				beg += _fillInSpace;
			}
			_sizeOfRows = sizeOfRows;
		}
		_sizeOfColumns = Math.max(_sizeOfColumns, sizeOfColumns);
	}

	public void setCapacity(int capacityOfRows, int capacityOfColumns) {
		if (capacityOfRows > capacityOfRows()) {
			resizeRows(capacityOfRows);
			resizeElements(_sizeOfElements + capacityOfRows * _fillInSpace);
		}
	}

	public void setElementAt(int row, int column, double value) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		int k = Array.find(_rowSize[row], _columnIndex, _rowBegin[row], 1,
				column, column);
		if (k != -1)
			_values[k] = value;
		else
			insert(row, column, value);
	}

	private void insert(int row, int column, double value) {
		int k = _rowBegin[row] + _rowSize[row];
		if (row == _sizeOfRows - 1) {
			if (_sizeOfElements >= _values.length)
				Array.resize(2 * _values.length, _values);
		} else {
			if (k >= _rowBegin[row + 1]) {
				int l = 10 + _sizeOfElements + _sizeOfRows * _fillInSpace;
				l = Math.max(l, _values.length);
				int[] columnIndex = new int[l];
				double[] values = new double[l];
				int to = 0;
				for (int i = 0; i < _sizeOfRows; i++) {
					int from = _rowBegin[i];
					_rowBegin[i] = to;
					int n = _rowSize[i];
					Array.copy(n, values, to, 1, _values, from, 1);
					Array.copy(n, columnIndex, to, 1, _columnIndex, from, 1);
					if (i == row)
						to++;
					to += n + _fillInSpace;
				}
				_values = values;
				_columnIndex = columnIndex;
			}
		}
		_values[k] = value;
		_columnIndex[k] = column;
		_rowSize[row]++;
		_sizeOfElements++;
		if (_columnEnd != null)
			deleteColumnIndex();
	}

	public boolean isNull(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		int k = Array.find(_rowSize[row], _columnIndex, _rowBegin[row], 1,
				column, column);
		return k == -1;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		int k = Array.find(_rowSize[row], _columnIndex, _rowBegin[row], 1,
				column, column);
		return (k == -1) ? 0 : _values[k];
	}

	public double[][] getArray() {
		double[][] array = new double[sizeOfRows()][sizeOfColumns()];
		for (Iterator<MatrixElementI> e = elements(); e.hasNext();) {
			MatrixElementI elem = e.next();
			array[elem.getRowIndex()][elem.getColumnIndex()] = elem.getValue();
		}
		return array;
	}

	public Iterator<MatrixElementI> rowElements(int row) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		return new EnumRow(row);
	}

	public Iterator<MatrixElementI> columnElements(int column) {
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		if (_columnEnd == null)
			buildColumnIndex();
		return new EnumCol(column);
	}

	public Iterator<MatrixElementI> elements() {
		return new Enum();
	}

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		double sum = 0;
		while (rowBegin < rowEnd) {
			int beg = _rowBegin[rowBegin];
			for (int cnt = _rowSize[rowBegin]; cnt > 0; cnt--, beg++) {
				int j = _columnIndex[beg];
				if (j < columnBegin || j >= columnEnd)
					continue;
				sum += _values[beg];
			}
			rowBegin++;
		}
		return sum;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		double sum = 0;
		while (rowBegin < rowEnd) {
			int beg = _rowBegin[rowBegin];
			for (int cnt = _rowSize[rowBegin]; cnt > 0; cnt--, beg++) {
				int j = _columnIndex[beg];
				if (j < columnBegin || j >= columnEnd)
					continue;
				double d = _values[beg];
				sum += d * d;
			}
			rowBegin++;
		}
		return sum;
	}

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, double scaler) {
		int ne = 0;
		double sum = 0;
		int n = (rowEnd - rowBegin) * (columnEnd - columnBegin);
		while (rowBegin < rowEnd) {
			int beg = _rowBegin[rowBegin];
			for (int cnt = _rowSize[rowBegin]; cnt > 0; cnt--, beg++) {
				int j = _columnIndex[beg];
				if (j < columnBegin || j >= columnEnd)
					continue;
				double d = _values[beg] - scaler;
				sum += d * d;
				ne++;
			}
			rowBegin++;
		}
		if (scaler != 0)
			sum += (n - ne) * scaler * scaler;
		return sum;
	}

	private class Enum implements Iterator<MatrixElementI>, MatrixElementI {
		int _row = 0;
		int _beg = 0;
		int _cnt = 0;

		public Enum() {
			while (_row < _sizeOfRows && _rowSize[_row] < 1)
				_row++;
			if (_row < _sizeOfRows) {
				_cnt = _rowSize[_row];
				_beg = _rowBegin[_row];
			}
		}

		public boolean hasNext() {
			return _row < _sizeOfRows;
		}

		public MatrixElementI next() {
			if (_row >= _sizeOfRows)
				return null;
			_mk = _beg;
			_mrow = _row;
			_mcol = _columnIndex[_beg];
			_mvalue = _values[_beg++];
			if (--_cnt <= 0) {
				_row++;
				while (_row < _sizeOfRows && _rowSize[_row] < 1)
					_row++;
				if (_row < _sizeOfRows) {
					_cnt = _rowSize[_row];
					_beg = _rowBegin[_row];
				}
			}
			return this;
		}

		int _mrow;
		int _mcol;
		int _mk;
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
			_values[_mk] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	private class EnumRow implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _beg = 0;
		int _end = 0;

		public EnumRow(int row) {
			_mrow = row;
			_beg = _rowBegin[row];
			_end = _beg + _rowSize[row];
		}

		public boolean hasNext() {
			return _beg < _end;
		}

		public MatrixElementI next() {
			if (_beg >= _end)
				return null;
			_mk = _beg;
			_mcol = _columnIndex[_beg];
			_mvalue = _values[_beg];
			_beg++;
			return this;
		}

		int _mrow;
		int _mcol;
		int _mk;
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
			_values[_mk] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	private class EnumCol implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _beg = 0;
		int _end = 0;

		public EnumCol(int column) {
			_mcol = column;
			_beg = column == 0 ? 0 : _columnEnd[column - 1];
			_end = _columnEnd[column];
		}

		public boolean hasNext() {
			return _beg < _end;
		}

		public MatrixElementI next() {
			if (_beg >= _end)
				return null;
			_mk = _valueIndex[_beg];
			_mrow = _rowIndex[_beg];
			_mvalue = _values[_mk];
			_beg++;
			return this;
		}

		int _mrow;
		int _mcol;
		int _mk;
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
			_values[_mk] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

}
