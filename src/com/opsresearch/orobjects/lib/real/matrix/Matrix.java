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

public abstract class Matrix extends RealContainer implements MatrixI,
		java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public Matrix() {
	}

	public Matrix(double epsilon) {
		super(epsilon);
	}

	public double sum() {
		return sum(0, sizeOfRows(), 0, sizeOfColumns());
	}

	public double sumOfSquares() {
		return sumOfSquares(0, sizeOfRows(), 0, sizeOfColumns());
	}

	public double sum(int rowBegin, int rowEnd, int columnBegin, int columnEnd) {
		double sum = 0;
		for (int i = rowBegin; i < rowEnd; i++) {
			for (int j = columnBegin; j < columnEnd; j++) {
				sum += elementAt(i, j);
			}
		}
		return sum;
	}

	public double sumOfSquaredDifferences(int rowBegin, int rowEnd,
			int columnBegin, int columnEnd, double scaler) {
		double sum = 0;
		for (int i = rowBegin; i < rowEnd; i++) {
			for (int j = columnBegin; j < columnEnd; j++) {
				double dif = scaler - elementAt(i, j);
				sum += dif * dif;
			}
		}
		return sum;
	}

	public double[][] getArray() {
		int nr = sizeOfRows();
		int nc = sizeOfColumns();
		double[][] array = new double[nr][nc];
		for (int i = 0; i < nr; i++) {
			for (int j = 0; j < nc; j++) {
				array[i][j] = elementAt(i, j);
			}
		}
		return array;
	}

	public double sumOfSquares(int rowBegin, int rowEnd, int columnBegin,
			int columnEnd) {
		double sum = 0;
		for (int i = rowBegin; i < rowEnd; i++) {
			for (int j = columnBegin; j < columnEnd; j++) {
				double v = elementAt(i, j);
				sum += v * v;
			}
		}
		return sum;
	}

	public double sumOfSquaredDifferences(double scaler) {
		return sumOfSquaredDifferences(0, sizeOfRows(), 0, sizeOfColumns(),
				scaler);
	}

	public double sum(int begin) {
		return sum(begin, sizeOfRows(), begin, sizeOfColumns());
	}

	public double sumOfSquares(int begin) {
		return sumOfSquares(begin, sizeOfRows(), begin, sizeOfColumns());
	}

	public double sumOfSquaredDifferences(int begin, double scaler) {
		return sumOfSquaredDifferences(begin, sizeOfRows(), begin,
				sizeOfColumns(), scaler);
	}

	public boolean equals(Object o) {
		if (o instanceof MatrixI)
			return equals((MatrixI) o);
		return false;
	}

	public void setElements(MatrixI values) {
		for (Iterator<MatrixElementI> e = values.elements(); e
				.hasNext();) {
			MatrixElementI elem = e.next();
			setElementAt(elem.getRowIndex(), elem.getColumnIndex(),
					elem.getValue());
		}
	}

	public void setRow(int row, VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(row, elem.getIndex(), elem.getValue());
		}
	}

	public void setColumn(int column, VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), column, elem.getValue());
		}
	}

	public void setDiagonal(VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			setElementAt(i, i, elem.getValue());
		}
	}

	public boolean equals(MatrixI matrix) {
		if (sizeOfRows() != matrix.sizeOfRows())
			return false;
		if (sizeOfColumns() != matrix.sizeOfColumns())
			return false;

		if (sizeOfElements() == sizeOfRows() * sizeOfColumns()) {
			for (Iterator<MatrixElementI> e = elements(); e
					.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(
						matrix.elementAt(elem.getRowIndex(),
								elem.getColumnIndex()), elem.getValue()))
					return false;
			}
		} else if (matrix.sizeOfElements() == matrix.sizeOfRows()
				* matrix.sizeOfColumns()) {
			for (Iterator<MatrixElementI> e = matrix.elements(); e
					.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(
						elementAt(elem.getRowIndex(), elem.getColumnIndex()),
						elem.getValue()))
					return false;
			}
		} else {
			for (Iterator<MatrixElementI> e = elements(); e
					.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(
						matrix.elementAt(elem.getRowIndex(),
								elem.getColumnIndex()), elem.getValue()))
					return false;
			}
			for (Iterator<MatrixElementI> e = matrix.elements(); e
					.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(
						elementAt(elem.getRowIndex(), elem.getColumnIndex()),
						elem.getValue()))
					return false;
			}
		}
		return true;
	}

	public String toString() {
		String str = "Matrix[" + sizeOfRows() + "][" + sizeOfColumns()
				+ "] = \n";
		for (Iterator<MatrixElementI> e = elements(); e.hasNext(); ) {
			MatrixElementI element = e.next();
			str += "[" + element.getRowIndex() + "]["
					+ element.getColumnIndex() + "] " + element.getValue()
					+ "\n";
		}
		return str;
	}

	public Iterator<MatrixElementI> rowElements(int row) {
		if (row < 0 || row >= sizeOfRows())
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ sizeOfRows());
		return new _RowEnum(row);
	}

	public Iterator<MatrixElementI> columnElements(int column) {
		if (column < 0 || column >= sizeOfColumns())
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + sizeOfColumns());
		return new _ColumnEnum(column);
	}

	public Iterator<MatrixElementI> elements() {
		return new _Enum();
	}

	private class _Enum implements Iterator<MatrixElementI>, MatrixElementI {
		int _i, _j;

		public boolean hasNext() {
			return _i < sizeOfRows();
		}

		public MatrixElementI next() {
			if (!hasNext())
				return null;
			_mi = _i;
			_mj = _j;
			_j++;
			if (_j >= sizeOfColumns()) {
				_i++;
				_j = 0;
			}
			return this;
		}

		int _mi, _mj;

		public int getRowIndex() {
			return _mi;
		}

		public int getColumnIndex() {
			return _mj;
		}

		public double getValue() {
			return elementAt(_mi, _mj);
		}

		public void setValue(double value) {
			setElementAt(_mi, _mj, value);
		}

		@Override
		public void remove() {
		}
	}

	private class _RowEnum implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _j = 0;

		public _RowEnum(int row) {
			_mi = row;
		}

		public boolean hasNext() {
			return _j < sizeOfColumns();
		}

		public MatrixElementI next() {
			if (!hasNext())
				return null;
			_mj = _j;
			_j++;
			return this;
		}

		int _mi, _mj;

		public int getRowIndex() {
			return _mi;
		}

		public int getColumnIndex() {
			return _mj;
		}

		public double getValue() {
			return elementAt(_mi, _mj);
		}

		public void setValue(double value) {
			setElementAt(_mi, _mj, value);
		}

		@Override
		public void remove() {
		}
	}

	private class _ColumnEnum implements Iterator<MatrixElementI>,
			MatrixElementI {
		int _i = 0;

		public _ColumnEnum(int column) {
			_mj = column;
		}

		public boolean hasNext() {
			return _i < sizeOfRows();
		}

		public MatrixElementI next() {
			if (!hasNext())
				return null;
			_mi = _i;
			_i++;
			return this;
		}

		int _mi, _mj;

		public int getRowIndex() {
			return _mi;
		}

		public int getColumnIndex() {
			return _mj;
		}

		public double getValue() {
			return elementAt(_mi, _mj);
		}

		public void setValue(double value) {
			setElementAt(_mi, _mj, value);
		}

		@Override
		public void remove() {
		}
	}

}
