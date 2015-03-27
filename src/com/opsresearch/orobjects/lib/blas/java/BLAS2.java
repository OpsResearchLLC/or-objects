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

package com.opsresearch.orobjects.lib.blas.java;

import com.opsresearch.orobjects.lib.blas.BLAS2I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS2 implements BLAS2I {
	@Override
	public void dgemv(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
			int incx, double beta, double[] y, int begy, int incy) throws BLASException {
		for (int i = 0; i < m; i++, begA += incAi, begy += incy) {
			int k = begA;
			int l = begx;
			double sum = 0;
			y[begy] *= beta;
			for (int j = 0; j < n; j++, k += incAj, l += incx) {
				sum += A[k] * x[l];
			}
			y[begy] += alpha * sum;
		}
	}

	@Override
	public void dger(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		for (int i = 0; i < m; i++, begA += incAi, begx += incx) {
			int k = begA;
			int l = begy;
			double tmp = alpha * x[begx];
			for (int j = 0; j < n; j++, k += incAj, l += incy) {
				A[k] += y[l] * tmp;
			}
		}
	}


	@Override
	public void sgemv(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
			int incx, float beta, float[] y, int begy, int incy) throws BLASException {
		for (int i = 0; i < m; i++, begA += incAi, begy += incy) {
			int k = begA;
			int l = begx;
			float sum = 0;
			y[begy] *= beta;
			for (int j = 0; j < n; j++, k += incAj, l += incx) {
				sum += A[k] * x[l];
			}
			y[begy] += alpha * sum;
		}
	}

	@Override
	public void sger(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		for (int i = 0; i < m; i++, begA += incAi, begy += incy) {
			int k = begA;
			int l = begx;
			float tmp = alpha * y[begy];
			for (int j = 0; j < n; j++, k += incAj, l += incx) {
				A[k] += x[l] * tmp;
			}
		}
	}

	@Override
	public void zgemv(int m, int n, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
			int incx, ComplexI beta, double[] y, int begy, int incy) throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		double ar = alpha.getReal();
		double ai = alpha.getImag();
		double br = beta.getReal();
		double bi = beta.getImag();
		for (int i = 0; i < m; i++, begA += incAi, begy += incy) {
			int k = begA;
			int l = begx;
			double sumReal = 0, sumImag = 0;
			double yr = y[begy];
			double yi = y[begy + 1];
			yr = yr * br - yi * bi;
			yi = yr * bi + yi * br;
			for (int j = 0; j < n; j++, k += incAj, l += incx) {
				double Ar = A[k], Ai = A[k + 1];
				double xr = x[l], xi = x[l + 1];
				sumReal += Ar * xr - Ai * xi;
				sumImag += Ar * xi + Ai * xr;
			}
			y[begy] = yr + ar * sumReal - ai * sumImag;
			y[begy + 1] = yi + ar * sumImag + ai * sumReal;
		}
	}

	@Override
	public void zgeru(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		double ar = alpha.getReal();
		double ai = alpha.getImag();
		for (int i = 0; i < m; i++, begA += incAi, begx += incx) {
			int k = begA;
			int l = begy;
			double xr = x[begx];
			double xi = x[begx + 1];
			double tr = ar * xr - ai * xi;
			double ti = ar * xi + ai * xr;
			for (int j = 0; j < n; j++, k += incAj, l += incy) {
				double yr = y[l];
				double yi = y[l + 1];
				A[k] += yr * tr - yi * ti;
				A[k + 1] += yr * ti + yi * tr;
			}
		}
	}

	@Override
	public void zgerc(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		double ar = alpha.getReal();
		double ai = alpha.getImag();
		for (int i = 0; i < m; i++, begA += incAi, begx += incx) {
			int k = begA;
			int l = begy;
			double xr = x[begx];
			double xi = x[begx + 1];
			double tr = ar * xr - ai * xi;
			double ti = ar * xi + ai * xr;
			for (int j = 0; j < n; j++, k += incAj, l += incy) {
				double yr = y[l];
				double yi = -y[l + 1];
				A[k] += yr * tr - yi * ti;
				A[k + 1] += yr * ti + yi * tr;
			}
		}
	}

	@Override
	public void cgemv(int m, int n, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
			int incx, ComplexI beta, float[] y, int begy, int incy) throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		float ar = (float) alpha.getReal();
		float ai = (float) alpha.getImag();
		float br = (float) beta.getReal();
		float bi = (float) beta.getImag();
		for (int i = 0; i < m; i++, begA += incAi, begy += incy) {
			int k = begA;
			int l = begx;
			float sumReal = 0, sumImag = 0;
			float yr = y[begy];
			float yi = y[begy + 1];
			yr = yr * br - yi * bi;
			yi = yr * bi + yi * br;
			for (int j = 0; j < n; j++, k += incAj, l += incx) {
				float Ar = A[k], Ai = A[k + 1];
				float xr = x[l], xi = x[l + 1];
				sumReal += Ar * xr - Ai * xi;
				sumImag += Ar * xi + Ai * xr;
			}
			y[begy] = yr + ar * sumReal - ai * sumImag;
			y[begy + 1] = yi + ar * sumImag + ai * sumReal;
		}
	}

	@Override
	public void cgeru(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		float ar = (float) alpha.getReal();
		float ai = (float) alpha.getImag();
		for (int i = 0; i < m; i++, begA += incAi, begx += incx) {
			int k = begA;
			int l = begy;
			float xr = x[begx];
			float xi = x[begx + 1];
			float tr = ar * xr - ai * xi;
			float ti = ar * xi + ai * xr;
			for (int j = 0; j < n; j++, k += incAj, l += incy) {
				float yr = y[l];
				float yi = y[l + 1];
				A[k] += yr * tr - yi * ti;
				A[k + 1] += yr * ti + yi * tr;
			}
		}
	}

	@Override
	public void cgerc(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		float ar = (float) alpha.getReal();
		float ai = (float) alpha.getImag();
		for (int i = 0; i < m; i++, begA += incAi, begx += incx) {
			int k = begA;
			int l = begy;
			float xr = x[begx];
			float xi = x[begx + 1];
			float tr = ar * xr - ai * xi;
			float ti = ar * xi + ai * xr;
			for (int j = 0; j < n; j++, k += incAj, l += incy) {
				float yr = y[l];
				float yi = -y[l + 1];
				A[k] += yr * tr - yi * ti;
				A[k + 1] += yr * ti + yi * tr;
			}
		}
	}

}
