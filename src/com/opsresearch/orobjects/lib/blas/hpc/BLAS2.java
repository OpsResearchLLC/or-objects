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

package com.opsresearch.orobjects.lib.blas.hpc;

import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS2 extends com.opsresearch.orobjects.lib.blas.java.BLAS2 {

	private static boolean _loaded = false; 


	@Override
	public void dgemv(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx, int incx, double beta, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			dgemvNative(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
		else
			super.dgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}

	private native void dgemvNative(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx, int incx, double beta, double[] y, int begy, int incy) throws BLASException;

	@Override
	public void dger(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException {
		if (_loaded)
			dgerNative( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
		else
			super.dger( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
	}
	private native void dgerNative(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException;



	@Override
	public void sgemv(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx, float beta, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			sgemvNative(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
		else
			super.sgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}

	private native void sgemvNative(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx, float beta, float[] y, int begy, int incy) throws BLASException;

	@Override
	public void sger(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException {
		if (_loaded)
			sgerNative( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
		else
			super.sger( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
	}
	private native void sgerNative(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException;


	@Override
	public void zgemv(int m, int n, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx, int incx, ComplexI beta, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			zgemvNative(m, n, alpha.getReal(), alpha.getImag(), A, begA, incAi, incAj, x, begx, incx, beta.getReal(), beta.getImag(), y, begy, incy);
		else
			super.zgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}
	private native void zgemvNative(int m, int n, double alphaReal, double alphaImag, double[] A, int begA, int incAi, int incAj, double[] x, int begx, int incx, double betaReal, double betaImag, double[] y, int begy, int incy) throws BLASException;

	@Override
	public void zgeru(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException {
		if (_loaded)
			zgeruNative( m,  n,  alpha.getReal(), alpha.getImag(),  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
		else
			super.zgeru( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
	}
	private native void zgeruNative(int m, int n, double alphaReal, double alphaImag, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException;

	@Override
	public void zgerc(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException {
		if (_loaded)
			zgercNative( m,  n,  alpha.getReal(), alpha.getImag(),  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
		else
			super.zgerc( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
	}
	private native void zgercNative(int m, int n, double alphaReal, double alphaImag, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException;


	@Override
	public void cgemv(int m, int n, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx, ComplexI beta, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			cgemvNative(m, n, (float)alpha.getReal(), (float)alpha.getImag(), A, begA, incAi, incAj, x, begx, incx, (float)beta.getReal(), (float)beta.getImag(), y, begy, incy);
		else
			super.cgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
	}
	private native void cgemvNative(int m, int n, float alphaReal, float alphaImag, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx, float betaReal, float betaImag, float[] y, int begy, int incy) throws BLASException;

	@Override
	public void cgeru(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException {
		if (_loaded)
			cgeruNative( m,  n,  (float)alpha.getReal(), (float)alpha.getImag(),  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
		else
			super.cgeru( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
	}
	private native void cgeruNative(int m, int n, float alphaReal, float alphaImag, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException;

	@Override
	public void cgerc(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException {
		if (_loaded)
			cgercNative( m,  n,  (float)alpha.getReal(), (float)alpha.getImag(),  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
		else
			super.cgerc( m,  n,  alpha,  x,  begx,  incx,  y,  begy,  incy,  A,  begA,  incAi,  incAj);
	}
	private native void cgercNative(int m, int n, float alphaReal, float alphaImag, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException;

}
