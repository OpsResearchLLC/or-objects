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

public class CompressedColumnMatrix extends CompressedMatrix {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "columnSize")
	int[] _columnSize = null;
	@XmlElement(name = "columnBegin")
	int[] _columnBegin = null;
	@XmlElement(name = "rowIndex")
	int[] _rowIndex = null;

	@XmlElement(name = "columnIndex")
	int[] _columnIndex = null;
	@XmlElement(name = "rowEnd")
	int[] _rowEnd = null;
	@XmlElement(name = "valueIndex")
	int[] _valueIndex = null;

	public CompressedColumnMatrix(int sizeOfRows, int capacityOfColumns,
			int fillInSpace) {
		this(sizeOfRows, capacityOfColumns, fillInSpace, capacityOfColumns
				* (1 + fillInSpace));
	}

	public CompressedColumnMatrix(int sizeOfRows, int capacityOfColumns,
			int fillInSpace, int capacityOfElements) {
		_sizeOfRows = sizeOfRows;
		_fillInSpace = fillInSpace;
		_sizeOfColumns = 0;
		resizeColumns(capacityOfColumns);
		resizeElements(capacityOfElements);
	}

	public CompressedColumnMatrix(double[][] array, int fillInSpace) {
		super(array);
		_fillInSpace = fillInSpace;
		_sizeOfRows = array.length;
		resizeColumns(_sizeOfColumns);
		resizeElements(_sizeOfElements + _sizeOfColumns * fillInSpace);
		int k = 0;
		for (int j = 0; j < _sizeOfColumns; j++) {
			_columnBegin[j] = k;
			for (int i = 0; i < array.length; i++) {
				double[] row = array[i];
				if (row == null || row.length <= j)
					continue;
				_columnSize[j]++;
				_values[k] = row[j];
				_rowIndex[k++] = i;
			}
			k += _fillInSpace;
		}
	}

	public CompressedColumnMatrix(double[][] array, int fillInSpace,
			double epsilon) {
		super(array, epsilon);
		_fillInSpace = fillInSpace;
		_sizeOfRows = array.length;
		resizeColumns(_sizeOfColumns);
		resizeElements(_sizeOfElements + _sizeOfColumns * fillInSpace);
		int k = 0;
		for (int j = 0; j < _sizeOfColumns; j++) {
			_columnBegin[j] = k;
			for (int i = 0; i < array.length; i++) {
				double[] row = array[i];
				if (row == null || row.length <= j)
					continue;
				double v = row[j];
				if (equals(v, 0.0))
					continue;
				_columnSize[j]++;
				_values[k] = v;
				_rowIndex[k++] = i;
			}
			k += _fillInSpace;
		}
	}

	public CompressedColumnMatrix(MatrixI matrix, int fillInSpace) {
		_sizeOfRows = matrix.sizeOfRows();
		_sizeOfColumns = matrix.sizeOfColumns();
		_sizeOfElements = matrix.sizeOfElements();
		_fillInSpace = fillInSpace;
		resizeColumns(_sizeOfColumns);
		resizeElements(_sizeOfElements + _sizeOfColumns * fillInSpace);
		int k = 0;
		for (int j = 0; j < _sizeOfColumns; j++) {
			_columnBegin[j] = k;
			for (Iterator<MatrixElementI> e = matrix.columnElements(j); e
					.hasNext();) {
				MatrixElementI elem = e.next();
				int i = elem.getRowIndex();
				double v = elem.getValue();
				_columnSize[j]++;
				_values[k] = v;
				_rowIndex[k++] = i;
			}
			k += _fillInSpace;
		}
	}

	public CompressedColumnMatrix(VectorI vector, int fillInSpace) {
		_sizeOfRows = vector.size();
		_sizeOfColumns = vector.size();
		_sizeOfElements = vector.size();
		_fillInSpace = fillInSpace;
		resizeColumns(_sizeOfColumns);
		resizeElements(_sizeOfElements + _sizeOfColumns * fillInSpace);
		int k = 0;
		for (int j = 0; j < _sizeOfColumns; j++) {
			double v = vector.elementAt(j);
			_columnSize[j]++;
			_values[k] = v;
			_rowIndex[k++] = j;
		}
		k += _fillInSpace;
	}

	private void resizeColumns(int capacityOfColumns) {
		_columnSize = Array.resize(capacityOfColumns, _columnSize);
		_columnBegin = Array.resize(capacityOfColumns, _columnBegin);
	}

	private void resizeElements(int capacityOfElements) {
		_values = Array.resize(capacityOfElements, _values);
		_rowIndex = Array.resize(capacityOfElements, _rowIndex);
	}

	public int sizeOfElements() {
		return _sizeOfElements;
	}

	public int capacityOfColumns() {
		return _columnBegin.length;
	}

	public int capacityOfRows() {
		return Integer.MAX_VALUE;
	}

	public int[] getRowIndexArray() {
		return _rowIndex;
	}

	public int[] getColumnSizeArray() {
		return _columnSize;
	}

	public int[] getColumnBeginArray() {
		return _columnBegin;
	}

	public void deleteRowIndex() {
		_columnIndex = null;
		_rowEnd = null;
		_valueIndex = null;
	}

	public void buildRowIndex() {
		_columnIndex = new int[_sizeOfElements];
		_rowEnd = new int[_sizeOfColumns];
		_valueIndex = new int[_sizeOfElements];

		int[] cnts = new int[_sizeOfRows];
		for (int j = 0; j < _sizeOfColumns; j++) {
			int beg = _columnBegin[j];
			for (int cnt = _columnSize[j]; cnt > 0; cnt--, beg++)
				cnts[_rowIndex[beg]]++;
		}

		int end = 0;
		for (int i = 0; i < _sizeOfRows; i++)
			_rowEnd[i] = (end += cnts[i]);

		Array.copy(_sizeOfRows, cnts, 0, 1, 0);
		for (int j = 0; j < _sizeOfColumns; j++) {
			int beg = _columnBegin[j];
			for (int cnt = _columnSize[j]; cnt > 0; cnt--, beg++) {
				int i = _rowIndex[beg];
				int l = (i == 0 ? 0 : _rowEnd[i - 1]) + cnts[i];
				_columnIndex[l] = j;
				_valueIndex[l] = beg;
				cnts[i]++;
			}
		}
	}

	public int[] getColumnIndexArray() {
		return _columnIndex;
	}

	public int[] getRowEndArray() {
		return _rowEnd;
	}

	public int[] getValueIndexArray() {
		return _valueIndex;
	}

	public boolean isRowMajor() {
		return false;
	}

	public boolean isColumnMajor() {
		return true;
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

	public void addRow(VectorI vector) {
		int i = _sizeOfColumns;
		setSize(_sizeOfRows + 1, _sizeOfColumns);
		if (vector == null)
			return;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(i, elem.getIndex(), elem.getValue());
		}
	}

	public void setSize(int sizeOfRows, int sizeOfColumns) {
		if (sizeOfRows < _sizeOfRows || sizeOfColumns < _sizeOfColumns) {
			_sizeOfRows = 0;
			_sizeOfColumns = 0;
		}
		if (sizeOfColumns > capacityOfColumns())
			setCapacity(sizeOfRows, sizeOfColumns);
		if (sizeOfColumns > _sizeOfColumns) {
			int n = sizeOfColumns - _sizeOfColumns;
			Array.copy(n, _columnSize, _sizeOfColumns, 1, 0);
			int beg = _sizeOfColumns == 0 ? 0
					: _columnBegin[_sizeOfColumns - 1]
							+ _columnSize[_sizeOfColumns - 1] + _fillInSpace;
			for (int j = _sizeOfColumns; j < sizeOfColumns; j++) {
				_columnBegin[j] = beg;
				beg += _fillInSpace;
			}
			_sizeOfColumns = sizeOfColumns;
		}
		_sizeOfRows = Math.max(_sizeOfRows, sizeOfRows);
	}

	public void setCapacity(int capacityOfRows, int capacityOfColumns) {
		if (capacityOfColumns > capacityOfColumns()) {
			resizeColumns(capacityOfColumns);
			resizeElements(_sizeOfElements + capacityOfColumns * _fillInSpace);
		}
	}

	public void setElementAt(int row, int column, double value) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		int k = Array.find(_columnSize[column], _rowIndex,
				_columnBegin[column], 1, row, row);
		if (k != -1)
			_values[k] = value;
		else
			insert(row, column, value);
	}

	private void insert(int row, int column, double value) {
		int k = _columnBegin[column] + _columnSize[column];
		if (column == _sizeOfColumns - 1) {
			if (_sizeOfElements >= _values.length)
				Array.resize(2 * _values.length, _values);
		} else {
			if (k >= _columnBegin[column + 1]) {
				int l = 10 + _sizeOfElements + _sizeOfColumns * _fillInSpace;
				l = Math.max(l, _values.length);
				int[] rowIndex = new int[l];
				double[] values = new double[l];
				int to = 0;
				for (int j = 0; j < _sizeOfColumns; j++) {
					int from = _columnBegin[j];
					_columnBegin[j] = to;
					int n = _columnSize[j];
					Array.copy(n, values, to, 1, _values, from, 1);
					Array.copy(n, rowIndex, to, 1, _rowIndex, from, 1);
					if (j == column)
						to++;
					to += n + _fillInSpace;
				}
				_values = values;
				_rowIndex = rowIndex;
			}
		}
		_values[k] = value;
		_rowIndex[k] = row;
		_columnSize[column]++;
		_sizeOfElements++;
		if (_rowEnd != null)
			deleteRowIndex();
	}

	public boolean isNull(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		int k = Array.find(_columnSize[column], _rowIndex,
				_columnBegin[column], 1, row, row);
		return k == -1;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		int k = Array.find(_columnSize[column], _rowIndex,
				_columnBegin[column], 1, row, row);
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
		if (_rowEnd == null)
			buildRowIndex();
		return new EnumRow(row);
	}

	public Iterator<MatrixElementI> columnElements(int column) {
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return new EnumCol(column);
	}

	public Iterator<MatrixElementI> elements() {
		return new Enum();
	}

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		double sum = 0;
		while (columnBegin < columnEnd) {
			int beg = _columnBegin[columnBegin];
			for (int cnt = _columnSize[columnBegin]; cnt > 0; cnt--, beg++) {
				int i = _rowIndex[beg];
				if (i < rowBegin || i >= rowEnd)
					continue;
				sum += _values[beg];
			}
			columnBegin++;
		}
		return sum;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		double sum = 0;
		while (columnBegin < columnEnd) {
			int beg = _columnBegin[columnBegin];
			for (int cnt = _columnSize[columnBegin]; cnt > 0; cnt--, beg++) {
				int i = _rowIndex[beg];
				if (i < rowBegin || i >= rowEnd)
					continue;
				double d = _values[beg];
				sum += d * d;
			}
			columnBegin++;
		}
		return sum;
	}

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, double scaler) {
		int ne = 0;
		double sum = 0;
		int n = (rowEnd - rowBegin) * (columnEnd - columnBegin);
		while (columnBegin < columnEnd) {
			int beg = _columnBegin[columnBegin];
			for (int cnt = _columnSize[columnBegin]; cnt > 0; cnt--, beg++) {
				int i = _rowIndex[beg];
				if (i < rowBegin || i >= rowEnd)
					continue;
				double d = _values[beg] - scaler;
				sum += d * d;
				ne++;
			}
			columnBegin++;
		}
		if (scaler != 0)
			sum += (n - ne) * scaler * scaler;
		return sum;
	}

	private class Enum implements Iterator<MatrixElementI>, MatrixElementI {
		int _beg = 0;
		int _cnt = 0;
		int _column = 0;

		public Enum() {
			while (_column < _sizeOfColumns && _columnSize[_column] < 1)
				_column++;
			if (_column < _sizeOfColumns) {
				_cnt = _columnSize[_column];
				_beg = _columnBegin[_column];
			}
		}

		public boolean hasNext() {
			return _column < _sizeOfColumns;
		}

		public MatrixElementI next() {
			if (_column >= _sizeOfColumns)
				return null;
			_mk = _beg;
			_mcol = _column;
			_mrow = _rowIndex[_beg];
			_mvalue = _values[_beg++];
			if (--_cnt <= 0) {
				_column++;
				while (_column < _sizeOfColumns && _columnSize[_column] < 1)
					_column++;
				if (_column < _sizeOfColumns) {
					_cnt = _columnSize[_column];
					_beg = _columnBegin[_column];
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

	private class EnumCol implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _beg = 0;
		int _end = 0;

		public EnumCol(int column) {
			_mcol = column;
			_beg = _columnBegin[column];
			_end = _beg + _columnSize[column];
		}

		public boolean hasNext() {
			return _beg < _end;
		}

		public MatrixElementI next() {
			if (_beg >= _end)
				return null;
			_mk = _beg;
			_mrow = _rowIndex[_beg];
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

	private class EnumRow implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _beg = 0;
		int _end = 0;

		public EnumRow(int row) {
			_mrow = row;
			_beg = row == 0 ? 0 : _rowEnd[row - 1];
			_end = _rowEnd[row];
		}

		public boolean hasNext() {
			return _beg < _end;
		}

		public MatrixElementI next() {
			if (_beg >= _end)
				return null;
			_mk = _valueIndex[_beg];
			_mrow = _columnIndex[_beg];
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
