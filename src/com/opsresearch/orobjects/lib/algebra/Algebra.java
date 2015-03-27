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

package com.opsresearch.orobjects.lib.algebra;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.blas.matrix.BLAS1;
import com.opsresearch.orobjects.lib.blas.matrix.BLAS2;
import com.opsresearch.orobjects.lib.blas.matrix.BLAS3;
import com.opsresearch.orobjects.lib.real.matrix.ArrayVectorI;
import com.opsresearch.orobjects.lib.real.matrix.ContiguousMatrixI;
import com.opsresearch.orobjects.lib.real.matrix.DenseMatrix;
import com.opsresearch.orobjects.lib.real.matrix.DenseVector;
import com.opsresearch.orobjects.lib.real.matrix.MatrixElementI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.VectorElementI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;


public class Algebra implements AlgebraI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static BLAS1 _blas1 = null;
	private static BLAS2 _blas2 = null;

	public Algebra() {
		_blas1 = new BLAS1();
		_blas2 = new BLAS2();
	}

	public Algebra(BLAS1 blas1, BLAS2 blas2, BLAS3 blas3) {
		_blas1 = blas1;
		_blas2 = blas2;
	}

	public DenseMatrix add(MatrixI a, MatrixI b) throws AlgebraException {
		if (a.sizeOfRows() != b.sizeOfRows())
			throw new AlgebraException("The row sizes don't match.");
		if (a.sizeOfColumns() != b.sizeOfColumns())
			throw new AlgebraException("The column sizes don't match.");
		DenseMatrix m;
		if (a.sizeOfElements() < b.sizeOfElements()) {
			m = new DenseMatrix(b);
			for (Iterator<MatrixElementI> e = a.elements(); e.hasNext();) {
				MatrixElementI el = e.next();
				double d = m.elementAt(el.getRowIndex(), el.getColumnIndex());
				m.setElementAt(el.getRowIndex(), el.getColumnIndex(), d + el.getValue());
			}
		} else {
			m = new DenseMatrix(a);
			for (Iterator<MatrixElementI> e = b.elements(); e.hasNext();) {
				MatrixElementI el = e.next();
				double d = m.elementAt(el.getRowIndex(), el.getColumnIndex());
				m.setElementAt(el.getRowIndex(), el.getColumnIndex(), d + el.getValue());
			}
		}
		return m;
	}

	public DenseMatrix subtract(MatrixI a, MatrixI b) throws AlgebraException {
		if (a.sizeOfRows() != b.sizeOfRows())
			throw new AlgebraException("The row sizes don't match.");
		if (a.sizeOfColumns() != b.sizeOfColumns())
			throw new AlgebraException("The column sizes don't match.");
		DenseMatrix m;
		if (a.sizeOfElements() < b.sizeOfElements()) {
			m = new DenseMatrix(b);
			for (Iterator<MatrixElementI> e = a.elements(); e.hasNext();) {
				MatrixElementI el = e.next();
				double d = m.elementAt(el.getRowIndex(), el.getColumnIndex());
				m.setElementAt(el.getRowIndex(), el.getColumnIndex(), d - el.getValue());
			}
		} else {
			m = new DenseMatrix(a);
			for (Iterator<MatrixElementI> e = b.elements(); e.hasNext();) {
				MatrixElementI el = e.next();
				double d = m.elementAt(el.getRowIndex(), el.getColumnIndex());
				m.setElementAt(el.getRowIndex(), el.getColumnIndex(), d - el.getValue());
			}
		}
		return m;
	}

	public DenseMatrix multiply(MatrixI a, MatrixI b) throws AlgebraException {
		if (a.sizeOfColumns() != b.sizeOfRows())
			throw new AlgebraException(
					"Can't multiply matrices if the columns in the first matrix don't match the rows in the second.");
		DenseMatrix c = new DenseMatrix(a.sizeOfRows(), b.sizeOfColumns());

		for (int i = 0; i < a.sizeOfRows(); i++)
			for (int j = 0; j < b.sizeOfColumns(); j++) {
				double sum = 0;
				for (int k = 0; k < a.sizeOfColumns(); k++) {
					sum += a.elementAt(i, k) * b.elementAt(k, j);
				}
				c.setElementAt(i, j, sum);
			}

		return c;
	}

	public DenseVector multiply(VectorI a, MatrixI b) throws AlgebraException {
		if (b.sizeOfRows() != a.size())
			throw new AlgebraException("The size of the row vector must match the number of rows in the matrix.");
		DenseVector c = new DenseVector(b.sizeOfColumns());
		if (_blas2 != null && a instanceof ArrayVectorI && b instanceof ContiguousMatrixI) {
			try {
				_blas2.dgemv(true, 1, (ContiguousMatrixI) b, (ArrayVectorI) a, 1, c);
			} catch (BLASException e) {
				throw new AlgebraException("BLAS3:" + e.getMessage());
			}
		} else {
			for (int j = 0; j < b.sizeOfColumns(); j++) {
				double sum = 0;
				for (int i = 0; i < b.sizeOfRows(); i++)
					sum += b.elementAt(i, j) * a.elementAt(i);
				c.setElementAt(j, sum);
			}
		}
		return c;
	}

	public DenseVector multiply(MatrixI a, VectorI b) throws AlgebraException {
		if (a.sizeOfColumns() != b.size())
			throw new AlgebraException("The size of the column vector must match the number of columns in the matrix.");
		DenseVector c = new DenseVector(a.sizeOfRows());
		if (_blas2 != null && b instanceof ArrayVectorI && a instanceof ContiguousMatrixI) {
			try {
				_blas2.dgemv(false, 1, (ContiguousMatrixI) a, (ArrayVectorI) b, 1, c);
			} catch (BLASException e) {
				throw new AlgebraException("BLAS3:" + e.getMessage());
			}
		} else {
			for (int i = 0; i < a.sizeOfRows(); i++) {
				double sum = 0;
				for (int j = 0; j < a.sizeOfColumns(); j++)
					sum += a.elementAt(i, j) * b.elementAt(j);
				c.setElementAt(i, sum);
			}
		}
		return c;
	}

	public DenseVector add(VectorI a, VectorI b) throws AlgebraException {
		if (a.size() != b.size())
			throw new AlgebraException("The sizes don't match.");
		DenseVector v;
		if (_blas1 != null && a instanceof ArrayVectorI && b instanceof ArrayVectorI) {
			v = new DenseVector(a);
			try {
				_blas1.daxpy(1, (ArrayVectorI) b, v);
			} catch (BLASException e) {
				throw new AlgebraException("BLAS1:" + e.getMessage());
			}
		} else if (a.sizeOfElements() < b.sizeOfElements()) {
			v = new DenseVector(b);
			for (Iterator<VectorElementI> e = a.elements(); e.hasNext();) {
				VectorElementI el = e.next();
				double d = v.elementAt(el.getIndex());
				v.setElementAt(el.getIndex(), d + el.getValue());
			}
		} else {
			v = new DenseVector(a);
			for (Iterator<VectorElementI> e = b.elements(); e.hasNext();) {
				VectorElementI el = e.next();
				double d = v.elementAt(el.getIndex());
				v.setElementAt(el.getIndex(), d + el.getValue());
			}
		}
		return v;
	}

	public DenseVector subtract(VectorI a, VectorI b) throws AlgebraException {
		if (a.size() != b.size())
			throw new AlgebraException("The sizes don't match.");
		DenseVector v;
		if (_blas1 != null && a instanceof ArrayVectorI && b instanceof ArrayVectorI) {
			v = new DenseVector(a);
			try {
				_blas1.daxpy(-1, (ArrayVectorI) b, v);
			} catch (BLASException e) {
				throw new AlgebraException("BLAS1:" + e.getMessage());
			}
		} else if (a.sizeOfElements() < b.sizeOfElements()) {
			v = new DenseVector(b);
			for (Iterator<VectorElementI> e = a.elements(); e.hasNext();) {
				VectorElementI el = e.next();
				double d = v.elementAt(el.getIndex());
				v.setElementAt(el.getIndex(), d - el.getValue());
			}
		} else {
			v = new DenseVector(a);
			for (Iterator<VectorElementI> e = b.elements(); e.hasNext();) {
				VectorElementI el = e.next();
				double d = v.elementAt(el.getIndex());
				v.setElementAt(el.getIndex(), d - el.getValue());
			}
		}
		return v;
	}

	public DenseMatrix matrixProduct(VectorI columnVector, VectorI rowVector) throws AlgebraException {
		if (columnVector.size() != rowVector.size())
			throw new AlgebraException("Can't multiply vectors of different sizes.");
		int n = rowVector.size();
		DenseMatrix c = new DenseMatrix(n, n);

		if (_blas2 != null && columnVector instanceof ArrayVectorI && rowVector instanceof ArrayVectorI) {
			try {
				_blas2.dger(1.0, (ArrayVectorI) columnVector, (ArrayVectorI) rowVector, c);
			} catch (BLASException e) {
				throw new AlgebraException("BLAS2:" + e.getMessage());
			}
		} else {
			for (Iterator<VectorElementI> col = columnVector.elements(); col.hasNext();) {
				VectorElementI ce = col.next();
				int i = ce.getIndex();
				double value = ce.getValue();
				for (Iterator<VectorElementI> row = rowVector.elements(); row.hasNext();) {
					VectorElementI re = row.next();
					c.setElementAt(i, re.getIndex(), re.getValue() * value);
				}
			}
		}
		return c;
	}

	public double scalerProduct(VectorI a, VectorI b) throws AlgebraException {
		if (a.size() != b.size())
			throw new AlgebraException("Can't multiply vectors of different sizes.");
		double sum = 0;
		if (_blas1 != null && a instanceof ArrayVectorI && b instanceof ArrayVectorI) {
			try {
				sum = _blas1.ddot((ArrayVectorI) a, (ArrayVectorI) b);
			} catch (BLASException e) {
				throw new AlgebraException("BLAS1:" + e.getMessage());
			}
		} else if (a.sizeOfElements() < b.sizeOfElements()) {
			for (Iterator<VectorElementI> e = a.elements(); e.hasNext();) {
				VectorElementI el = e.next();
				sum += b.elementAt(el.getIndex()) * el.getValue();
			}
		} else {
			for (Iterator<VectorElementI> e = b.elements(); e.hasNext();) {
				VectorElementI el = e.next();
				sum += a.elementAt(el.getIndex()) * el.getValue();
			}
		}

		return sum;
	}

	public DenseMatrix transpose(MatrixI a) {
		DenseMatrix matrix = new DenseMatrix(a.sizeOfColumns(), a.sizeOfRows());

		for (Iterator<MatrixElementI> e = a.elements(); e.hasNext();) {
			MatrixElementI el = e.next();
			matrix.setElementAt(el.getColumnIndex(), el.getRowIndex(), el.getValue());
		}
		return matrix;
	}

	public DenseMatrix invert(MatrixI a) throws AlgebraException, SingularException {
		if (a.sizeOfRows() != a.sizeOfColumns())
			throw new AlgebraException("Can't invert a matrix that isn't square.");
		LUDecompositionI lud = new CroutPivot();
		lud.decompose(a);
		DenseMatrix results = new DenseMatrix(a.sizeOfRows(), a.sizeOfColumns());
		lud.computeInverse(results);
		return results;
	}

}
