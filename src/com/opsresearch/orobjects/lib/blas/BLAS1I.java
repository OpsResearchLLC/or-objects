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

public interface BLAS1I {

	public double dasum(int n, double[] x, int begx, int incx) throws BLASException;

	public void daxpy(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException;

	public void dcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	public double ddot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	public void drot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin)
			throws BLASException;

	public void dscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException;

	public void dswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	public double dnrm2(int n, double[] x, int begx, int incx) throws BLASException;

	public int idamax(int n, double[] x, int begx, int incx) throws BLASException;


	public float sasum(int n, float[] x, int begx, int incx) throws BLASException;

	public void saxpy(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException;

	public void scopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	public float sdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	public void srot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin)
			throws BLASException;

	public void sscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException;

	public void sswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	public float snrm2(int n, float[] x, int begx, int incx) throws BLASException;

	public int isamax(int n, float[] x, int begx, int incx) throws BLASException;


	public double dzasum(int n, double[] x, int begx, int incx) throws BLASException;

	public void zaxpy(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException;

	public void zcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	public Complex zdotu(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException;

	public Complex zdotc(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException;

	public void zscal(int n, ComplexI alpha, double[] x, int begx, int incx) throws BLASException;

	public void zswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;


	public float scasum(int n, float[] x, int begx, int incx) throws BLASException;

	public void caxpy(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException;

	public void ccopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	public Complex cdotu(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException;

	public Complex cdotc(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException;

	public void cscal(int n, ComplexI alpha, float[] x, int begx, int incx) throws BLASException;

	public void cswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;


	public double dsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	public float sdsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	public int izamax(int n, double[] x, int begx, int incx) throws BLASException;

	public int icamax(int n, float[] x, int begx, int incx) throws BLASException;

	public double dznrm2(int n, double[] x, int begx, int incx) throws BLASException;

	public float scnrm2(int n, float[] x, int begx, int incx) throws BLASException;

	public void drotg(Real da, Real db, Real cos, Real sin) throws BLASException;

	public void srotg(Real da, Real db, Real cos, Real sin) throws BLASException;

	public void zdscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException;

	public void csscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException;

}
