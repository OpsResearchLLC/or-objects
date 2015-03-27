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

package com.opsresearch.orobjects.lib.blas;

import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.Real;

public class BLAS1 implements BLAS1I{
	
	private BLAS1I _implementation	= new com.opsresearch.orobjects.lib.blas.java.BLAS1();

	@Override
	public double dasum(int n, double[] x, int begx, int incx) throws BLASException {
		return _implementation.dasum(n, x, begx, incx);
	}

	@Override
	public void daxpy(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException {
		_implementation.daxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void dcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		_implementation.dcopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public double ddot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		return _implementation.ddot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public void drot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin)
			throws BLASException {
		_implementation.drot(n, x, begx, incx, y, begy, incy, cos, sin);
	}

	@Override
	public void dscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		_implementation.dscal(n, alpha, x, begx, incx);
	}

	@Override
	public void dswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		_implementation.dswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public double dnrm2(int n, double[] x, int begx, int incx) throws BLASException {
		return _implementation.dnrm2(n, x, begx, incx);
	}

	@Override
	public int idamax(int n, double[] x, int begx, int incx) throws BLASException {
		return _implementation.idamax(n, x, begx, incx);
	}

	@Override
	public float sasum(int n, float[] x, int begx, int incx) throws BLASException {
		return _implementation.sasum(n, x, begx, incx);
	}

	@Override
	public void saxpy(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException {
		_implementation.saxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void scopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		_implementation.scopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float sdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		return _implementation.sdot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public void srot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin)
			throws BLASException {
		_implementation.srot(n, x, begx, incx, y, begy, incy, cos, sin);
	}

	@Override
	public void sscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		_implementation.sscal(n, alpha, x, begx, incx);
	}

	@Override
	public void sswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		_implementation.sswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float snrm2(int n, float[] x, int begx, int incx) throws BLASException {
		return _implementation.snrm2(n, x, begx, incx);
	}

	@Override
	public int isamax(int n, float[] x, int begx, int incx) throws BLASException {
		return _implementation.isamax(n, x, begx, incx);
	}

	@Override
	public double dzasum(int n, double[] x, int begx, int incx) throws BLASException {
		return _implementation.dzasum(n, x, begx, incx);
	}

	@Override
	public void zaxpy(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException {
		_implementation.zaxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void zcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		_implementation.zcopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public Complex zdotu(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException {
		return _implementation.zdotu(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public Complex zdotc(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException {
		return _implementation.zdotc(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public void zscal(int n, ComplexI alpha, double[] x, int begx, int incx) throws BLASException {
		_implementation.zscal(n, alpha, x, begx, incx);
	}

	@Override
	public void zswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		_implementation.zswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float scasum(int n, float[] x, int begx, int incx) throws BLASException {
		return _implementation.scasum(n, x, begx, incx);
	}

	@Override
	public void caxpy(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException {
		_implementation.caxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void ccopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		_implementation.ccopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public Complex cdotu(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException {
		return _implementation.cdotu(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public Complex cdotc(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException {
		return _implementation.cdotc(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public void cscal(int n, ComplexI alpha, float[] x, int begx, int incx) throws BLASException {
		_implementation.cscal(n, alpha, x, begx, incx);
	}

	@Override
	public void cswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		_implementation.cswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public double dsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		return _implementation.dsdot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float sdsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		return _implementation.sdsdot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public int izamax(int n, double[] x, int begx, int incx) throws BLASException {
		return _implementation.izamax(n, x, begx, incx);
	}

	@Override
	public int icamax(int n, float[] x, int begx, int incx) throws BLASException {
		return _implementation.icamax(n, x, begx, incx);
	}

	@Override
	public double dznrm2(int n, double[] x, int begx, int incx) throws BLASException {
		return _implementation.dznrm2(n, x, begx, incx);
	}

	@Override
	public float scnrm2(int n, float[] x, int begx, int incx) throws BLASException {
		return _implementation.scnrm2(n, x, begx, incx);
	}

	@Override
	public void drotg(Real da, Real db, Real cos, Real sin) throws BLASException {
		_implementation.drotg(da, db, cos, sin);
	}

	@Override
	public void srotg(Real da, Real db, Real cos, Real sin) throws BLASException {
		_implementation.srotg(da, db, cos, sin);
	}

	@Override
	public void zdscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		_implementation.zdscal(n, alpha, x, begx, incx);
	}

	@Override
	public void csscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		_implementation.csscal(n, alpha, x, begx, incx);
	}

	public BLAS1I getImplementation() {
		return _implementation;
	}

	public void setImplementation(BLAS1I implementation) {
		this._implementation = implementation;
	}

	
}
