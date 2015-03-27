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

import com.opsresearch.orobjects.lib.blas.BLAS2I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS2 extends Tuned<BLAS2I> implements BLAS2I {

	public BLAS2() {
		super(new com.opsresearch.orobjects.lib.blas.java.BLAS2(), new TunerBLAS2());
		setNumTuningPoints(3);
		setNumTuningMethods(NMETHODS);
		addImplementation("java", new com.opsresearch.orobjects.lib.blas.java.BLAS2());
		addImplementation("smp", new com.opsresearch.orobjects.lib.blas.smp.BLAS2());
		addImplementation("hpc", new com.opsresearch.orobjects.lib.blas.hpc.BLAS2());
		addImplementation("cuda", new com.opsresearch.orobjects.lib.blas.cuda.BLAS2());
		addImplementation("opencl", new com.opsresearch.orobjects.lib.blas.opencl.BLAS2());
		tune();
	}

	@Override
	public void dgemv(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
			int incx, double beta, double[] y, int begy, int incy) throws BLASException {
		select(0, m, n).dgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}

	@Override
	public void dger(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		select(1, m, n).dger(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
	}

	@Override
	public void sgemv(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
			int incx, float beta, float[] y, int begy, int incy) throws BLASException {
		select(2, m, n).sgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}

	@Override
	public void sger(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int bega, int inci, int incj) throws BLASException {
		select(3, m, n).sger(m, n, alpha, x, begx, incx, y, begy, incy, A, bega, inci, incj);
	}

	@Override
	public void zgemv(int m, int n, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
			int incx, ComplexI beta, double[] y, int begy, int incy) throws BLASException {
		select(4, m, n).zgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}

	@Override
	public void zgeru(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		select(5, m, n).zgeru(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
	}

	@Override
	public void zgerc(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		select(6, m, n).zgerc(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
	}

	@Override
	public void cgemv(int m, int n, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
			int incx, ComplexI beta, float[] y, int begy, int incy) throws BLASException {
		select(7, m, n).cgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}

	@Override
	public void cgeru(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		select(8, m, n).cgeru(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
	}

	@Override
	public void cgerc(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		select(9, m, n).cgerc(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
	}

	private final static int NMETHODS = 10;

}
