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
import java.util.HashMap;
import java.util.Vector;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

public class SparseMatrix extends Matrix implements SizableMatrixI {

	private static final long serialVersionUID = 1L;
	Element _tmpElement = new Element(0, 0, 0.0);

	HashMap<MatrixElementI, MatrixElementI> _index;
	Vector<MatrixElementI> _rows;
	Vector<MatrixElementI> _columns;

	@XmlElement(name = "sizeOfRows")
	@SuppressWarnings("unused")
	private int getSizeOfRows() {
		return _rows.size();
	}

	@SuppressWarnings("unused")
	private void setSizeOfRows(int sizeOfRows) {
		_rows = new java.util.Vector<MatrixElementI>(sizeOfRows);
		_rows.setSize(sizeOfRows);
	}

	@XmlElement(name = "sizeOfColumns")
	@SuppressWarnings("unused")
	private int getSizeOfColumns() {
		return _columns.size();
	}

	@SuppressWarnings("unused")
	private void setSizeOfColumns(int sizeOfColumns) {
		_columns = new java.util.Vector<MatrixElementI>(sizeOfColumns);
		_columns.setSize(sizeOfColumns);
	}

	@SuppressWarnings("unused")
	@XmlElementWrapper(name = "elements")
	@XmlElement(name = "element")
	private Element[] getElements() {
		Element[] a = new Element[_index.size()];
		return _index.values().toArray(a);
	}

	@SuppressWarnings("unused")
	private void setElements(Element[] elements) {
		_index = new HashMap<MatrixElementI, MatrixElementI>(elements.length);
		for (Element element : elements)
			setElementAt(element._row, element._column, element._value);
	}

	public SparseMatrix() {
	}

	public SparseMatrix(int sizeOfRows, int sizeOfColumns) {
		_rows = new java.util.Vector<MatrixElementI>(sizeOfRows);
		_rows.setSize(sizeOfRows);
		_columns = new java.util.Vector<MatrixElementI>(sizeOfColumns);
		_columns.setSize(sizeOfColumns);
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				sizeOfRows + sizeOfColumns));
	}

	public SparseMatrix(VectorI vector) {
		_rows = new java.util.Vector<MatrixElementI>(vector.size());
		_rows.setSize(vector.size());
		_columns = new java.util.Vector<MatrixElementI>(vector.size());
		_columns.setSize(vector.size());
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				vector.sizeOfElements()));
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), elem.getIndex(), elem.getValue());
		}
	}

	public SparseMatrix(VectorI vector, double epsilon) {
		super(epsilon);
		_rows = new java.util.Vector<MatrixElementI>(vector.size());
		_rows.setSize(vector.size());
		_columns = new java.util.Vector<MatrixElementI>(vector.size());
		_columns.setSize(vector.size());
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				vector.sizeOfElements()));
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			double v = elem.getValue();
			if (equals(v, 0.0))
				continue;
			setElementAt(elem.getIndex(), elem.getIndex(), v);
		}
	}

	public SparseMatrix(MatrixI matrix) {
		_rows = new java.util.Vector<MatrixElementI>(matrix.sizeOfRows());
		_rows.setSize(matrix.sizeOfRows());
		_columns = new java.util.Vector<MatrixElementI>(matrix.sizeOfColumns());
		_columns.setSize(matrix.sizeOfColumns());
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				matrix.sizeOfElements()));
		for (Iterator<MatrixElementI> e = matrix.elements(); e
				.hasNext();) {
			MatrixElementI elem = e.next();
			setElementAt(elem.getRowIndex(), elem.getColumnIndex(),
					elem.getValue());
		}
	}

	public SparseMatrix(MatrixI matrix, double epsilon) {
		super(epsilon);
		_rows = new java.util.Vector<MatrixElementI>(matrix.sizeOfRows());
		_rows.setSize(matrix.sizeOfRows());
		_columns = new java.util.Vector<MatrixElementI>(matrix.sizeOfColumns());
		_columns.setSize(matrix.sizeOfColumns());
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				matrix.sizeOfElements()));
		for (Iterator<MatrixElementI> e = matrix.elements(); e
				.hasNext();) {
			MatrixElementI elem = e.next();
			double v = elem.getValue();
			if (equals(v, 0.0))
				continue;
			setElementAt(elem.getRowIndex(), elem.getColumnIndex(), v);
		}
	}

	public SparseMatrix(double[][] array) {
		int ncol = 0;
		int nrow = 0;
		if (array != null) {
			nrow = array.length;
			for (int i = 0; i < array.length; i++) {
				double[] row = array[i];
				if (row != null && row.length > ncol)
					ncol = row.length;
			}
		}

		_rows = new java.util.Vector<MatrixElementI>(nrow);
		_rows.setSize(nrow);
		_columns = new java.util.Vector<MatrixElementI>(ncol);
		_columns.setSize(ncol);
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				nrow * ncol));

		if (array == null)
			return;
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row == null)
				continue;
			for (int j = 0; j < row.length; j++) {
				Element e = new Element(i, j, row[j]);
				_index.put(e, e);
				e._nextRow = (Element) _rows.elementAt(e._row);
				_rows.setElementAt(e, e._row);
				e._nextColumn = (Element) _columns.elementAt(e._column);
				_columns.setElementAt(e, e._column);
			}
		}
	}

	public SparseMatrix(double[][] array, double epsilon) {
		super(epsilon);
		int ncol = 0;
		int nrow = 0;
		if (array != null) {
			nrow = array.length;
			for (int i = 0; i < array.length; i++) {
				double[] row = array[i];
				if (row != null && row.length > ncol)
					ncol = row.length;
			}
		}

		_rows = new java.util.Vector<MatrixElementI>(nrow);
		_rows.setSize(nrow);
		_columns = new java.util.Vector<MatrixElementI>(ncol);
		_columns.setSize(ncol);
		_index = new HashMap<MatrixElementI, MatrixElementI>(Math.max(10,
				nrow * ncol));

		if (array == null)
			return;
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row == null)
				continue;
			for (int j = 0; j < row.length; j++) {
				double v = row[j];
				if (equals(v, 0.0))
					continue;
				Element e = new Element(i, j, v);
				_index.put(e, e);
				e._nextRow = (Element) _rows.elementAt(e._row);
				_rows.setElementAt(e, e._row);
				e._nextColumn = (Element) _columns.elementAt(e._column);
				_columns.setElementAt(e, e._column);
			}
		}
	}

	public void setElements(double value) {
		for (Iterator<MatrixElementI> e = elements(); e.hasNext();) {
			e.next().setValue(value);
		}
	}

	public boolean isRowMajor() {
		return false;
	}

	public boolean isColumnMajor() {
		return false;
	}

	public int sizeOfRows() {
		return _rows.size();
	}

	public int sizeOfColumns() {
		return _columns.size();
	}

	public int capacityOfRows() {
		return _rows.capacity();
	}

	public int capacityOfColumns() {
		return _columns.capacity();
	}

	public void addRow(VectorI vector) {
		int i = sizeOfRows();
		setSize(sizeOfRows() + 1, sizeOfColumns());
		if (vector == null)
			return;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int j = elem.getIndex();
			if (j <= sizeOfColumns())
				continue;
			setElementAt(i, j, elem.getValue());
		}
	}

	public void addColumn(VectorI vector) {
		int j = sizeOfColumns();
		setSize(sizeOfRows(), sizeOfColumns() + 1);
		if (vector == null)
			return;
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i <= sizeOfRows())
				continue;
			setElementAt(i, j, elem.getValue());
		}
	}

	public void setSize(int sizeOfRows, int sizeOfColumns) {
		_rows.setSize(sizeOfRows);
		_columns.setSize(sizeOfColumns);
	}

	public void setCapacity(int capacityOfRows, int capacityOfColumns) {
		_rows.ensureCapacity(capacityOfRows);
		_columns.ensureCapacity(capacityOfColumns);
	}

	public int sizeOfElements() {
		return _index.size();
	}

	public void setElementAt(int row, int column, double value) {
		if (row < 0 || row >= _rows.size())
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _rows.size());
		if (column < 0 || column >= _columns.size())
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _columns.size());
		_tmpElement._row = row;
		_tmpElement._column = column;
		Element e = (Element) _index.get(_tmpElement);
		if (e == null) {
			e = new Element(row, column, value);
			_index.put(e, e);
			e._nextRow = (Element) _rows.elementAt(e._row);
			_rows.setElementAt(e, e._row);
			e._nextColumn = (Element) _columns.elementAt(e._column);
			_columns.setElementAt(e, e._column);
		} else {
			e._value = value;
		}
	}

	public boolean isNull(int row, int column) {
		if (row < 0 || row >= _rows.size())
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _rows.size());
		if (column < 0 || column >= _columns.size())
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _columns.size());
		_tmpElement._row = row;
		_tmpElement._column = column;
		Element e = (Element) _index.get(_tmpElement);
		return e == null;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _rows.size())
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _rows.size());
		if (column < 0 || column >= _columns.size())
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _columns.size());
		_tmpElement._row = row;
		_tmpElement._column = column;
		Element e = (Element) _index.get(_tmpElement);
		return (e == null) ? 0 : e._value;
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
		if (row < 0 || row >= _rows.size())
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _rows.size());
		return new RowEnum(_rows.elementAt(row));
	}

	public Iterator<MatrixElementI> columnElements(int column) {
		if (column < 0 || column >= _columns.size())
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _columns.size());
		return new ColumnEnum(_columns.elementAt(column));
	}

	public Iterator<MatrixElementI> elements() {
		return _index.values().iterator();
	}

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		double sum = 0;
		while (rowBegin < rowEnd) {
			for (Element e = (Element) _rows.elementAt(rowBegin); e != null; e = e._nextRow) {
				if (e.getColumnIndex() < columnBegin
						|| e.getColumnIndex() >= columnEnd)
					continue;
				sum += e.getValue();
			}
			rowBegin++;
		}
		return sum;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		double sum = 0;
		while (rowBegin < rowEnd) {
			for (Element e = (Element) _rows.elementAt(rowBegin); e != null; e = e._nextRow) {
				if (e.getColumnIndex() < columnBegin
						|| e.getColumnIndex() >= columnEnd)
					continue;
				double d = e.getValue();
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
			for (Element e = (Element) _rows.elementAt(rowBegin); e != null; e = e._nextRow) {
				if (e.getColumnIndex() < columnBegin
						|| e.getColumnIndex() >= columnEnd)
					continue;
				double d = e.getValue() - scaler;
				sum += d * d;
				ne++;
			}
			rowBegin++;
		}
		if (scaler != 0)
			sum += (n - ne) * scaler * scaler;
		return sum;
	}

	@XmlRootElement(name = "sparse-matrix-element")
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Element implements MatrixElementI,
			java.io.Serializable {
		private static final long serialVersionUID = 1L;

		@XmlElement(name = "row")
		int _row;
		@XmlElement(name = "column")
		int _column;
		@XmlElement(name = "value")
		double _value;

		transient Element _nextRow;
		transient Element _nextColumn;

		@SuppressWarnings("unused")
		Element() {
		}

		Element(int row, int col, double value) {
			_row = row;
			_column = col;
			_value = value;
		}

		public int getRowIndex() {
			return _row;
		}

		public int getColumnIndex() {
			return _column;
		}

		public double getValue() {
			return _value;
		}

		public void setValue(double value) {
			_value = value;
		}

		public int hashCode() {
			return _row * _column;
		}

		public boolean equals(Object o) {
			Element element = (Element) o;
			return _row == element._row && _column == element._column;
		}

	}

	private static class RowEnum implements Iterator<MatrixElementI> {
		Element _element;

		RowEnum(MatrixElementI element) {
			_element = (Element) element;
		}

		public boolean hasNext() {
			return _element != null;
		}

		public MatrixElementI next() {
			MatrixElementI o = _element;
			if (_element != null)
				_element = _element._nextRow;
			return o;
		}

		@Override
		public void remove() {
		}
	}

	private static class ColumnEnum implements Iterator<MatrixElementI> {
		Element _element;

		ColumnEnum(MatrixElementI element) {
			_element = (Element) element;
		}

		public boolean hasNext() {
			return _element != null;
		}

		public MatrixElementI next() {
			MatrixElementI o = _element;
			if (_element != null)
				_element = _element._nextColumn;
			return o;
		}

		@Override
		public void remove() {
		}
	}

}
