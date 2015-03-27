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

package com.opsresearch.orobjects.lib.blas.smart;

import com.opsresearch.orobjects.lib.blas.BLAS1I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.Real;

public class BLAS1 extends Tuned<BLAS1I> implements BLAS1I {

	public BLAS1() {
		super(new com.opsresearch.orobjects.lib.blas.java.BLAS1(), new TunerBLAS1());
		setNumTuningPoints(3);
		setNumTuningMethods(NMETHODS);
		addImplementation("java", new com.opsresearch.orobjects.lib.blas.java.BLAS1());
		addImplementation("smp", new com.opsresearch.orobjects.lib.blas.smp.BLAS1());
		addImplementation("hpc", new com.opsresearch.orobjects.lib.blas.hpc.BLAS1());
		addImplementation("cuda", new com.opsresearch.orobjects.lib.blas.cuda.BLAS1());
		addImplementation("opencl", new com.opsresearch.orobjects.lib.blas.opencl.BLAS1());
		tune();
	}

	@Override
	public double dasum(int n, double[] x, int begx, int incx) throws BLASException {
		return select(0,n).dasum(n, x, begx, incx);
	}

	@Override
	public void daxpy(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException {
		select(1,n).daxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void dcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		select(2,n).dcopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public double ddot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		return select(3,n).ddot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public void drot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin)
			throws BLASException {
		select(4,n).drot(n, x, begx, incx, y, begy, incy, cos, sin);
	}

	@Override
	public void dscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		select(5,n).dscal(n, alpha, x, begx, incx);
	}

	@Override
	public void dswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		select(6,n).dswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public double dnrm2(int n, double[] x, int begx, int incx) throws BLASException {
		return select(7,n).dnrm2(n, x, begx, incx);
	}

	@Override
	public int idamax(int n, double[] x, int begx, int incx) throws BLASException {
		return select(8,n).idamax(n, x, begx, incx);
	}

	@Override
	public float sasum(int n, float[] x, int begx, int incx) throws BLASException {
		return select(9,n).sasum(n, x, begx, incx);
	}

	@Override
	public void saxpy(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException {
		select(10,n).saxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void scopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		select(11,n).scopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float sdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		return select(12,n).sdot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public void srot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin)
			throws BLASException {
		select(13,n).srot(n, x, begx, incx, y, begy, incy, cos, sin);
	}

	@Override
	public void sscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		select(14,n).sscal(n, alpha, x, begx, incx);
	}

	@Override
	public void sswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		select(15,n).sswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float snrm2(int n, float[] x, int begx, int incx) throws BLASException {
		return select(16,n).snrm2(n, x, begx, incx);
	}

	@Override
	public int isamax(int n, float[] x, int begx, int incx) throws BLASException {
		return select(17,n).isamax(n, x, begx, incx);
	}

	@Override
	public double dzasum(int n, double[] x, int begx, int incx) throws BLASException {
		return select(18,n).dzasum(n, x, begx, incx);
	}

	@Override
	public void zaxpy(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException {
		select(19,n).zaxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void zcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		select(20,n).zcopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public Complex zdotu(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException {
		return select(21,n).zdotu(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public Complex zdotc(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException {
		return select(22,n).zdotc(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public void zscal(int n, ComplexI alpha, double[] x, int begx, int incx) throws BLASException {
		select(23,n).zscal(n, alpha, x, begx, incx);
	}

	@Override
	public void zswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		select(24,n).zswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float scasum(int n, float[] x, int begx, int incx) throws BLASException {
		return select(25,n).scasum(n, x, begx, incx);
	}

	@Override
	public void caxpy(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException {
		select(26,n).caxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	@Override
	public void ccopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		select(27,n).ccopy(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public Complex cdotu(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException {
		return select(28,n).cdotu(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public Complex cdotc(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException {
		return select(29,n).cdotc(n, x, begx, incx, y, begy, incy, results);
	}

	@Override
	public void cscal(int n, ComplexI alpha, float[] x, int begx, int incx) throws BLASException {
		select(30,n).cscal(n, alpha, x, begx, incx);
	}

	@Override
	public void cswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		select(31,n).cswap(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public double dsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		return select(32,n).dsdot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public float sdsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		return select(33,n).sdsdot(n, x, begx, incx, y, begy, incy);
	}

	@Override
	public int izamax(int n, double[] x, int begx, int incx) throws BLASException {
		return select(34,n).izamax(n, x, begx, incx);
	}

	@Override
	public int icamax(int n, float[] x, int begx, int incx) throws BLASException {
		return select(35,n).icamax(n, x, begx, incx);
	}

	@Override
	public double dznrm2(int n, double[] x, int begx, int incx) throws BLASException {
		return select(36,n).dznrm2(n, x, begx, incx);
	}

	@Override
	public float scnrm2(int n, float[] x, int begx, int incx) throws BLASException {
		return select(37,n).scnrm2(n, x, begx, incx);
	}

	@Override
	public void drotg(Real da, Real db, Real cos, Real sin) throws BLASException {
		select(38,1L).drotg(da, db, cos, sin);
	}

	@Override
	public void srotg(Real da, Real db, Real cos, Real sin) throws BLASException {
		select(39,1L).srotg(da, db, cos, sin);
	}

	@Override
	public void zdscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		select(40,n).zdscal(n, alpha, x, begx, incx);
	}

	@Override
	public void csscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		select(41,n).csscal(n, alpha, x, begx, incx);
	}
	
	private final static int NMETHODS = 42;
}
