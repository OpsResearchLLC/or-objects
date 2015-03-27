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

package com.opsresearch.orobjects.lib.blas.matrix;

import com.opsresearch.orobjects.lib.blas.BLAS2I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.blas.java.BLAS1;
import com.opsresearch.orobjects.lib.blas.java.BLAS3;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.matrix.ArrayVectorI;
import com.opsresearch.orobjects.lib.real.matrix.ContiguousMatrixI;

public class BLAS2 {
	private int _base = 0;
	private BLAS2I _blas2;

	public BLAS2() {
		_blas2 = new com.opsresearch.orobjects.lib.blas.BLAS2();
	}

	public BLAS2(int base) {
		_base = base;
		_blas2 = new com.opsresearch.orobjects.lib.blas.BLAS2();
	}

	public BLAS2(int base, BLAS2I blas2) {
		_base = base;
		_blas2 = blas2;
	}

	public void dgemv(boolean transpose, double alpha, ContiguousMatrixI A, ArrayVectorI x, double beta, ArrayVectorI y)
			throws BLASException {
		int m = transpose ? A.sizeOfColumns() : A.sizeOfRows();
		int n = transpose ? A.sizeOfRows() : A.sizeOfColumns();
		int ri = transpose ? A.getColumnIncrement() : A.getRowIncrement();
		int ci = transpose ? A.getRowIncrement() : A.getColumnIncrement();
		int xi = x.getIncrement();
		int yi = y.getIncrement();
		_blas2.dgemv(m - _base, n - _base, alpha, A.getValueArray(), _base * (ri + ci), ri, ci, x.getValueArray(),
				_base * xi, xi, beta, y.getValueArray(), _base * yi, yi);
	}

	public void dger(double alpha, ArrayVectorI x, ArrayVectorI y, ContiguousMatrixI A) throws BLASException {
		int ri = A.getRowIncrement();
		int ci = A.getColumnIncrement();
		int xi = x.getIncrement();
		int yi = y.getIncrement();

		_blas2.dger(A.sizeOfRows() - _base, A.sizeOfColumns() - _base, alpha, x.getValueArray(), _base * xi, xi,
				y.getValueArray(), _base * yi, yi, A.getValueArray(), _base * (ri + ci), ri, ci);
	}

	public void zgemv(boolean transpose, ComplexI alpha, com.opsresearch.orobjects.lib.complex.matrix.ContiguousMatrixI A,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x, ComplexI beta,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y) throws BLASException {
		int m = transpose ? A.sizeOfColumns() : A.sizeOfRows();
		int n = transpose ? A.sizeOfRows() : A.sizeOfColumns();
		int ri = transpose ? A.getColumnIncrement() : A.getRowIncrement();
		int ci = transpose ? A.getRowIncrement() : A.getColumnIncrement();
		int xi = x.getIncrement();
		int yi = y.getIncrement();
		_blas2.zgemv(m - _base, n - _base, alpha, A.getValueArray(), _base * (ri + ci), ri, ci, x.getValueArray(),
				_base * xi, xi, beta, y.getValueArray(), _base * yi, yi);
	}

	public void zgeru(ComplexI alpha, com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y, com.opsresearch.orobjects.lib.complex.matrix.ContiguousMatrixI A)
			throws BLASException {
		int ri = A.getRowIncrement();
		int ci = A.getColumnIncrement();
		int xi = x.getIncrement();
		int yi = y.getIncrement();

		_blas2.zgeru(A.sizeOfRows() - _base, A.sizeOfColumns() - _base, alpha, x.getValueArray(), _base * xi, xi,
				y.getValueArray(), _base * yi, yi, A.getValueArray(), _base * (ri + ci), ri, ci);
	}

	public void zgerc(ComplexI alpha, com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y, com.opsresearch.orobjects.lib.complex.matrix.ContiguousMatrixI A)
			throws BLASException {
		int ri = A.getRowIncrement();
		int ci = A.getColumnIncrement();
		int xi = x.getIncrement();
		int yi = y.getIncrement();

		_blas2.zgerc(A.sizeOfRows() - _base, A.sizeOfColumns() - _base, alpha, x.getValueArray(), _base * xi, xi,
				y.getValueArray(), _base * yi, yi, A.getValueArray(), _base * (ri + ci), ri, ci);
	}

}
