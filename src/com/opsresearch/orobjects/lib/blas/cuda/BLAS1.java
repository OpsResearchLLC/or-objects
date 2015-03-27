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

package com.opsresearch.orobjects.lib.blas.cuda;

import com.opsresearch.orobjects.lib.Native;
import com.opsresearch.orobjects.lib.blas.BLAS1I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS1 extends com.opsresearch.orobjects.lib.blas.hpc.BLAS1 implements BLAS1I {

	private static boolean _loaded = Native.isInitialized() && Native.isCudaInitialized();


	@Override
	public double dasum(int n, double[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return dasumNative(n, x, begx, incx);
		else
			return super.dasum(n, x, begx, incx);
	}

	native private double dasumNative(int n, double[] x, int begx, int incx) throws BLASException;

	@Override
	public void daxpy(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			daxpyNative(n, alpha, x, begx, incx, y, begy, incy);
		else
			super.daxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	native private void daxpyNative(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	@Override
	public void dcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			dcopyNative(n, x, begx, incx, y, begy, incy);
		else
			super.dcopy(n, x, begx, incx, y, begy, incy);
	}

	native private void dcopyNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy);

	@Override
	public double ddot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			return ddotNative(n, x, begx, incx, y, begy, incy);
		else
			return super.ddot(n, x, begx, incx, y, begy, incy);
	}

	native private double ddotNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy);

	@Override
	public void drot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin) throws BLASException {
		if (_loaded)
			drotNative(n, x, begx, incx, y, begy, incy, cos, sin);
		else
			super.drot(n, x, begx, incx, y, begy, incy, cos, sin);
	}

	private native void drotNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin) throws BLASException;

	@Override
	public void dscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			dscalNative(n, alpha, x, begx, incx);
		else
			super.dscal(n, alpha, x, begx, incx);
	}

	private native void dscalNative(int n, double alpha, double[] x, int begx, int incx) throws BLASException;

	@Override
	public void dswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			dswapNative(n, x, begx, incx, y, begy, incy);
		else
			super.dswap(n, x, begx, incx, y, begy, incy);
	}

	private native void dswapNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	@Override
	public double dnrm2(int n, double[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return dnrm2Native(n, x, begx, incx);
		else
			return super.dnrm2(n, x, begx, incx);
	}

	private native double dnrm2Native(int n, double[] x, int begx, int incx) throws BLASException;

	@Override
	public int idamax(int n, double[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return idamaxNative(n, x, begx, incx);
		else
			return super.idamax(n, x, begx, incx);
	}

	private native int idamaxNative(int n, double[] x, int begx, int incx) throws BLASException;


	@Override
	public float sasum(int n, float[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return sasumNative(n, x, begx, incx);
		else
			return super.sasum(n, x, begx, incx);
	}

	native private float sasumNative(int n, float[] x, int begx, int incx) throws BLASException;

	@Override
	public void saxpy(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			saxpyNative(n, alpha, x, begx, incx, y, begy, incy);
		else
			super.saxpy(n, alpha, x, begx, incx, y, begy, incy);
	}

	native private void saxpyNative(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	@Override
	public void scopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			scopyNative(n, x, begx, incx, y, begy, incy);
		else
			super.scopy(n, x, begx, incx, y, begy, incy);
	}

	native private void scopyNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy);

	@Override
	public float sdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			return sdotNative(n, x, begx, incx, y, begy, incy);
		else
			return super.sdot(n, x, begx, incx, y, begy, incy);
	}

	native private float sdotNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy);

	@Override
	public void srot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin) throws BLASException {
		if (_loaded)
			srotNative(n, x, begx, incx, y, begy, incy, cos, sin);
		else
			super.srot(n, x, begx, incx, y, begy, incy, cos, sin);
	}

	private native void srotNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin) throws BLASException;

	@Override
	public void sscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			sscalNative(n, alpha, x, begx, incx);
		else
			super.sscal(n, alpha, x, begx, incx);
	}

	private native void sscalNative(int n, float alpha, float[] x, int begx, int incx) throws BLASException;

	@Override
	public void sswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			sswapNative(n, x, begx, incx, y, begy, incy);
		else
			super.sswap(n, x, begx, incx, y, begy, incy);
	}

	private native void sswapNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	@Override
	public float snrm2(int n, float[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return snrm2Native(n, x, begx, incx);
		else
			return super.snrm2(n, x, begx, incx);
	}

	private native float snrm2Native(int n, float[] x, int begx, int incx) throws BLASException;

	@Override
	public int isamax(int n, float[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return isamaxNative(n, x, begx, incx);
		else
			return super.isamax(n, x, begx, incx);
	}

	private native int isamaxNative(int n, float[] x, int begx, int incx) throws BLASException;


	@Override
	public double dzasum(int n, double[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return dzasumNative(n, x, begx, incx);
		else
			return super.dzasum(n, x, begx, incx);
	}

	private native double dzasumNative(int n, double[] x, int begx, int incx) throws BLASException;

	@Override
	public void zaxpy(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			zaxpyNative(n, alpha.getReal(), alpha.getImag(), x, begx, incx, y, begy, incy);
		else
			super.zaxpy(n, alpha, x, begx, incx, y, begy, incy);
	}
	private native void zaxpyNative(int n, double real, double imag, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	@Override
	public void zcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			zcopyNative( n,  x,  begx,  incx,  y,  begy,  incy);
		else
			super.zcopy( n,  x,  begx,  incx,  y,  begy,  incy);
	}
	private native void zcopyNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	@Override
	public Complex zdotu(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results) throws BLASException {
		if (_loaded)
			return zdotuNative( n,  x,  begx,  incx,  y,  begy,  incy,  results);
		else
			return super.zdotu( n,  x,  begx,  incx,  y,  begy,  incy,  results);
	}
	private native Complex zdotuNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results) throws BLASException;

	@Override
	public Complex zdotc(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results) throws BLASException {
		if (_loaded)
			return zdotc( n,  x,  begx,  incx,  y,  begy,  incy,  results);
		else
			return super.zdotc( n,  x,  begx,  incx,  y,  begy,  incy,  results);
	}
	private native Complex zdotcNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results) throws BLASException;

	@Override
	public void zscal(int n, ComplexI alpha, double[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			zscalNative( n,  alpha.getReal(), alpha.getImag(),  x,  begx,  incx);
		else
			super.zscal( n,  alpha,  x,  begx,  incx);
	}
	private native void zscalNative(int n, double real, double imag, double[] x, int begx, int incx) throws BLASException;

	@Override
	public void zswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			zswapNative( n,  x,  begx,  incx,  y,  begy,  incy);
		else
			super.zswap( n,  x,  begx,  incx,  y,  begy,  incy);
	}
	private native void zswapNative(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException;

	
	@Override
	public float scasum(int n, float[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			return scasumNative(n, x, begx, incx);
		else
			return super.scasum(n, x, begx, incx);
	}

	private native float scasumNative(int n, float[] x, int begx, int incx) throws BLASException;

	@Override
	public void caxpy(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			caxpyNative(n, (float)alpha.getReal(), (float)alpha.getImag(), x, begx, incx, y, begy, incy);
		else
			super.caxpy(n, alpha, x, begx, incx, y, begy, incy);
	}
	private native void caxpyNative(int n, float real, float imag, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	@Override
	public void ccopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			ccopyNative( n,  x,  begx,  incx,  y,  begy,  incy);
		else
			super.ccopy( n,  x,  begx,  incx,  y,  begy,  incy);
	}
	private native void ccopyNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

	@Override
	public Complex cdotu(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results) throws BLASException {
		if (_loaded){
			float[] rslt = new float[2];
			cdotuNative( n,  x,  begx,  incx,  y,  begy,  incy,  rslt);
			results.set(rslt[0], rslt[1]);
			return results;
		}
		else
			return super.cdotu( n,  x,  begx,  incx,  y,  begy,  incy,  results);
	}
	private native void cdotuNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] rslt) throws BLASException;

	@Override
	public Complex cdotc(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results) throws BLASException {
		if (_loaded){
			float[] rslt = new float[2];
			cdotcNative( n,  x,  begx,  incx,  y,  begy,  incy,  rslt);
			results.set(rslt[0], rslt[1]);
			return results;
		}
		else
			return super.cdotc( n,  x,  begx,  incx,  y,  begy,  incy,  results);
	}
	private native void cdotcNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] rslt) throws BLASException;

	@Override
	public void cscal(int n, ComplexI alpha, float[] x, int begx, int incx) throws BLASException {
		if (_loaded)
			cscalNative( n,  (float)alpha.getReal(), (float)alpha.getImag(),  x,  begx,  incx);
		else
			super.cscal( n,  alpha,  x,  begx,  incx);
	}
	private native void cscalNative(int n, float real, float imag, float[] x, int begx, int incx) throws BLASException;

	@Override
	public void cswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (_loaded)
			cswapNative( n,  x,  begx,  incx,  y,  begy,  incy);
		else
			super.cswap( n,  x,  begx,  incx,  y,  begy,  incy);
	}
	private native void cswapNative(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException;

}
