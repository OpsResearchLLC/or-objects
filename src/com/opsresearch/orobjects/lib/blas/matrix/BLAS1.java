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

import com.opsresearch.orobjects.lib.blas.BLAS1I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.matrix.ArrayVectorI;



public class BLAS1 {
	private int _base = 0;
	private BLAS1I _blas1;

	public BLAS1() {
		_blas1 = new com.opsresearch.orobjects.lib.blas.BLAS1();
	}

	public BLAS1(int base) {
		_base = base;
		_blas1 = new com.opsresearch.orobjects.lib.blas.BLAS1();
	}

	public BLAS1(int base, BLAS1I blas1) {
		_base = base;
		_blas1 = blas1;
	}


	public double dasum(ArrayVectorI x) throws BLASException {
		int incx = x.getIncrement();
		return _blas1.dasum(x.size() - _base, x.getValueArray(), _base * incx, incx);
	}

	public void daxpy(double a, ArrayVectorI x, ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.daxpy(x.size() - _base, a, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy);
	}

	public void dcopy(ArrayVectorI x, ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.dcopy(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy);
	}

	public double ddot(ArrayVectorI x, ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		return _blas1.ddot(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy,
				incy);
	}

	public void drot(ArrayVectorI x, ArrayVectorI y, double cos, double sin) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.drot(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy,
				cos, sin);
	}

	public void dscal(double a, ArrayVectorI x) throws BLASException {
		int incx = x.getIncrement();
		_blas1.dscal(x.size() - _base, a, x.getValueArray(), _base * incx, incx);
	}

	public void dswap(ArrayVectorI x, ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.dswap(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy);
	}

	public double dnrm2(ArrayVectorI x) throws BLASException {
		int incx = x.getIncrement();
		return _blas1.dnrm2(x.size() - _base, x.getValueArray(), _base * incx, incx);
	}

	public int idamax(ArrayVectorI x) throws BLASException {
		int incx = x.getIncrement();
		return _base + _blas1.idamax(x.size() - _base, x.getValueArray(), _base * incx, incx);
	}


	public void zaxpy(ComplexI a, com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.zaxpy(x.size() - _base, a, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy);
	}

	public void zcopy(com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.zcopy(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy);
	}

	public Complex zdotu(com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y, Complex results) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		return _blas1.zdotu(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy,
				incy, results);
	}

	public Complex zdotc(com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y, Complex results) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		return _blas1.zdotc(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy,
				incy, results);
	}

	public void zscal(ComplexI a, com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x) throws BLASException {
		int incx = x.getIncrement();
		_blas1.zscal(x.size() - _base, a, x.getValueArray(), _base * incx, incx);
	}

	public void zswap(com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI x,
			com.opsresearch.orobjects.lib.complex.matrix.ArrayVectorI y) throws BLASException {
		int incx = x.getIncrement();
		int incy = y.getIncrement();
		if (x.size() != y.size())
			throw new BLASException("X and Y must be the same size.");
		_blas1.zswap(x.size() - _base, x.getValueArray(), _base * incx, incx, y.getValueArray(), _base * incy, incy);
	}

}
