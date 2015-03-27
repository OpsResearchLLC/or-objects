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

import com.opsresearch.orobjects.lib.blas.BLAS3I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.blas.java.BLAS1;
import com.opsresearch.orobjects.lib.blas.java.BLAS2;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.matrix.ArrayVectorI;
import com.opsresearch.orobjects.lib.real.matrix.ContiguousMatrixI;

public class BLAS3 {
	private int _base = 0;
	private BLAS3I _blas3;

	public BLAS3() {
		_blas3 = new com.opsresearch.orobjects.lib.blas.BLAS3();
	}

	public BLAS3(int base) {
		_base = base;
		_blas3 = new com.opsresearch.orobjects.lib.blas.BLAS3();
	}

	public BLAS3(int base, BLAS3I blas3) {
		_base = base;
		_blas3 = blas3;
	}

	public void dgemm(boolean transposeA, boolean transposeB, double alpha, ContiguousMatrixI A, ContiguousMatrixI B,
			double beta, ContiguousMatrixI C) throws BLASException {
		int m = transposeA ? A.sizeOfColumns() : A.sizeOfRows();
		int ka = transposeA ? A.sizeOfRows() : A.sizeOfColumns();
		int n = transposeB ? B.sizeOfRows() : B.sizeOfColumns();
		int kb = transposeB ? B.sizeOfColumns() : B.sizeOfRows();
		if (ka != kb)
			throw new BLASException("The columns of 'A' and the rows of 'B' must match: " + ka + ", " + kb);
		if (C.sizeOfRows() != m)
			throw new BLASException("The rows of 'A' and 'C' must match: " + C.sizeOfRows() + ", " + m);
		if (C.sizeOfColumns() != n)
			throw new BLASException("The columns of 'A' and 'C' must match: " + C.sizeOfColumns() + ", " + n);

		int Ari = transposeA ? A.getColumnIncrement() : A.getRowIncrement();
		int Aci = transposeA ? A.getRowIncrement() : A.getColumnIncrement();
		int Bri = transposeB ? B.getColumnIncrement() : B.getRowIncrement();
		int Bci = transposeB ? B.getRowIncrement() : B.getColumnIncrement();
		int Cri = C.getRowIncrement();
		int Cci = C.getColumnIncrement();

		_blas3.dgemm(m - _base, n - _base, ka - _base, alpha, A.getValueArray(), _base * (Ari + Aci), Ari, Aci,
				B.getValueArray(), _base * (Bri + Bci), Bri, Bci, beta, C.getValueArray(), _base * (Cri + Cci), Cri,
				Cci);
	}

	public void zgemm(boolean transposeA, boolean transposeB, ComplexI alpha,
			com.opsresearch.orobjects.lib.complex.matrix.ContiguousMatrixI A,
			com.opsresearch.orobjects.lib.complex.matrix.ContiguousMatrixI B, ComplexI beta,
			com.opsresearch.orobjects.lib.complex.matrix.ContiguousMatrixI C) throws BLASException {
		int m = transposeA ? A.sizeOfColumns() : A.sizeOfRows();
		int ka = transposeA ? A.sizeOfRows() : A.sizeOfColumns();
		int n = transposeB ? B.sizeOfRows() : B.sizeOfColumns();
		int kb = transposeB ? B.sizeOfColumns() : B.sizeOfRows();
		if (ka != kb)
			throw new BLASException("The columns of 'A' and the rows of 'B' must match: " + ka + ", " + kb);
		if (C.sizeOfRows() != m)
			throw new BLASException("The rows of 'A' and 'C' must match: " + C.sizeOfRows() + ", " + m);
		if (C.sizeOfColumns() != n)
			throw new BLASException("The columns of 'A' and 'C' must match: " + C.sizeOfColumns() + ", " + n);

		int Ari = transposeA ? A.getColumnIncrement() : A.getRowIncrement();
		int Aci = transposeA ? A.getRowIncrement() : A.getColumnIncrement();
		int Bri = transposeB ? B.getColumnIncrement() : B.getRowIncrement();
		int Bci = transposeB ? B.getRowIncrement() : B.getColumnIncrement();
		int Cri = C.getRowIncrement();
		int Cci = C.getColumnIncrement();

		_blas3.zgemm(m - _base, n - _base, ka - _base, alpha, A.getValueArray(), _base * (Ari + Aci), Ari, Aci,
				B.getValueArray(), _base * (Bri + Bci), Bri, Bci, beta, C.getValueArray(), _base * (Cri + Cci), Cri,
				Cci);
	}

}
