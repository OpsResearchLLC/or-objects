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

import com.opsresearch.orobjects.lib.complex.ComplexI;

public interface BLAS2I {

	public void dgemv(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx, int incx, double beta, double[] y, int begy, int incy) throws BLASException;

	public void dger(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException;


	public void sgemv(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx, float beta, float[] y, int begy, int incy) throws BLASException;

	public void sger(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int bega, int inci, int incj) throws BLASException;


	public void zgemv(int m, int n, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx, int incx, ComplexI beta, double[] y, int begy, int incy) throws BLASException;

	public void zgeru(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException;

	public void zgerc(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A, int begA, int incAi, int incAj) throws BLASException;


	public void cgemv(int m, int n, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx, ComplexI beta, float[] y, int begy, int incy) throws BLASException;

	public void cgeru(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException;

	public void cgerc(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A, int begA, int incAi, int incAj) throws BLASException;

}
