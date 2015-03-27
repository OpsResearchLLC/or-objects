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

public abstract class Matrix extends ComplexContainer implements MatrixI, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public Matrix() {
	}

	public Matrix(double epsilon) {
		super(epsilon);
	}

	public Complex sum() {
		return sum(0, sizeOfRows(), 0, sizeOfColumns());
	}

	public Complex sumOfSquares() {
		return sumOfSquares(0, sizeOfRows(), 0, sizeOfColumns());
	}

	public Complex sumOfSquaredDifferences(ComplexI scaler) {
		return sumOfSquaredDifferences(0, sizeOfRows(), 0, sizeOfColumns(), scaler);
	}

	public Complex sum(int begin) {
		return sum(begin, sizeOfRows(), begin, sizeOfColumns());
	}

	public Complex sumOfSquares(int begin) {
		return sumOfSquares(begin, sizeOfRows(), begin, sizeOfColumns());
	}

	public Complex sumOfSquaredDifferences(int begin, ComplexI scaler) {
		return sumOfSquaredDifferences(begin, sizeOfRows(), begin, sizeOfColumns(), scaler);
	}

	public void setElements(MatrixI values) {
		for (Iterator<MatrixElementI> e = values.elements(); e.hasNext();) {
			MatrixElementI elem = e.next();
			setElementAt(elem.getRowIndex(), elem.getColumnIndex(), elem.getValue());
		}
	}

	public void setRow(int row, VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(row, elem.getIndex(), elem.getValue());
		}
	}

	public void setColumn(int column, VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), column, elem.getValue());
		}
	}

	public void setDiagonal(VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			setElementAt(i, i, elem.getValue());
		}
	}

	public boolean equals(Object o) {
		if (o instanceof MatrixI)
			return equals((MatrixI) o);
		return false;
	}

	public boolean equals(MatrixI matrix) {
		if (sizeOfRows() != matrix.sizeOfRows())
			return false;
		if (sizeOfColumns() != matrix.sizeOfColumns())
			return false;

		Complex c = new Complex();

		if (sizeOfElements() == sizeOfRows() * sizeOfColumns()) {
			for (Iterator<MatrixElementI> e = elements(); e.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(matrix.elementAt(elem.getRowIndex(), elem.getColumnIndex(), c), elem.getValue()))
					return false;
			}
		} else if (matrix.sizeOfElements() == matrix.sizeOfRows() * matrix.sizeOfColumns()) {
			for (Iterator<MatrixElementI> e = matrix.elements(); e.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(elementAt(elem.getRowIndex(), elem.getColumnIndex(), c), elem.getValue()))
					return false;
			}
		} else {
			for (Iterator<MatrixElementI> e = elements(); e.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(matrix.elementAt(elem.getRowIndex(), elem.getColumnIndex(), c), elem.getValue()))
					return false;
			}
			for (Iterator<MatrixElementI> e = matrix.elements(); e.hasNext();) {
				MatrixElementI elem = e.next();
				if (!equals(elementAt(elem.getRowIndex(), elem.getColumnIndex(), c), elem.getValue()))
					return false;
			}
		}
		return true;
	}

	public String toString() {
		String str = "Matrix[" + sizeOfRows() + "][" + sizeOfColumns() + "] = \n";
		for (Iterator<MatrixElementI> e = elements(); e.hasNext(); ) {
			MatrixElementI element = e.next();
			ComplexI value = element.getValue();
			str += "[" + element.getRowIndex() + "][" + element.getColumnIndex() + "] (" + value.getReal() + ", "
					+ value.getImag() + ")\n";
		}
		return str;
	}

}
