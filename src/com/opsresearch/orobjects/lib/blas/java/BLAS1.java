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

import com.opsresearch.orobjects.lib.blas.BLAS1I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.Real;

public class BLAS1 implements BLAS1I {

	@Override
	public double dasum(int n, double[] x, int begx, int incx) throws BLASException {
		int end;
		double t, sum = 0;
		if (n < 1)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		if (incx == 0)
			return n * Math.abs(x[begx]);
		if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				t = x[begx++];
				if (t < 0)
					sum -= t;
				else
					sum += t;
			}
		} else {
			while (n-- > 0) {
				t = x[begx];
				begx += incx;
				if (t < 0)
					sum -= t;
				else
					sum += t;
			}
		}
		return sum;
	}

	@Override
	public void daxpy(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException {
		int end;
		if (n < 1)
			return;
		if (alpha == 0.0)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			if (alpha == 1)
				while (begx < end)
					y[begy++] += x[begx++];
			else if (alpha == -1)
				while (begx < end)
					y[begy++] -= x[begx++];
			else
				while (begx < end)
					y[begy++] += alpha * x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			if (alpha == 1)
				while (begx < end) {
					y[begy] += x[begx++];
					begy += incy;
				}
			else if (alpha == -1)
				while (begx < end) {
					y[begy] -= x[begx++];
					begy += incy;
				}
			else
				while (begx < end) {
					y[begy] += alpha * x[begx++];
					begy += incy;
				}
		} else if (incy == 1) {
			end = begy + n;
			if (alpha == 1)
				while (begy < end) {
					y[begy++] += x[begx];
					begx += incx;
				}
			else if (alpha == -1)
				while (begy < end) {
					y[begy++] -= x[begx];
					begx += incx;
				}
			else
				while (begy < end) {
					y[begy++] += alpha * x[begx];
					begx += incx;
				}
		} else {
			if (alpha == 1)
				while (n-- > 0) {
					y[begy] += x[begx];
					begx += incx;
					begy += incy;
				}
			else if (alpha == -1)
				while (n-- > 0) {
					y[begy] -= x[begx];
					begx += incx;
					begy += incy;
				}
			else
				while (n-- > 0) {
					y[begy] += alpha * x[begx];
					begx += incx;
					begy += incy;
				}
		}
	}

	@Override
	public void dcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		int end;
		if (n < 1)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end)
				y[begy++] = x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				y[begy] = x[begx++];
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				y[begy++] = x[begx];
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				y[begy] = x[begx];
				begx += incx;
				begy += incy;
			}
		}
	}

	@Override
	public double ddot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		int end;
		double sum = 0;
		if (n < 1)
			return sum;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end)
				sum += y[begy++] * x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				sum += y[begy] * x[begx++];
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				sum += y[begy++] * x[begx];
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				sum += y[begy] * x[begx];
				begx += incx;
				begy += incy;
			}
		}
		return sum;
	}

	@Override
	public void drot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin)
			throws BLASException {
		int end;
		double xe, ye;
		if (n <= 0)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end) {
				xe = x[begx];
				ye = y[begy];
				x[begx++] = cos * xe + sin * ye;
				y[begy++] = cos * ye - sin * xe;
			}
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				xe = x[begx];
				ye = y[begy];
				x[begx++] = cos * xe + sin * ye;
				y[begy] = cos * ye - sin * xe;
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				xe = x[begx];
				ye = y[begy];
				x[begx] = cos * xe + sin * ye;
				y[begy++] = cos * ye - sin * xe;
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				xe = x[begx];
				ye = y[begy];
				x[begx] = cos * xe + sin * ye;
				y[begy] = cos * ye - sin * xe;
				begx += incx;
				begy += incy;
			}
		}
	}

	@Override
	public void dscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		int end;
		if (n < 1)
			return;
		if (incx == 1) {
			end = begx + n;
			while (begx < end)
				x[begx++] *= alpha;
		} else {
			while (n-- > 0) {
				x[begx] *= alpha;
				begx += incx;
			}
		}
	}

	@Override
	public void dswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		int end;
		double t;
		if (n < 1)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end) {
				t = y[begy];
				y[begy++] = x[begx];
				x[begx++] = t;
			}
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				t = y[begy];
				y[begy] = x[begx];
				x[begx++] = t;
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				t = y[begy];
				y[begy++] = x[begx];
				x[begx] = t;
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				t = y[begy];
				y[begy] = x[begx];
				x[begx] = t;
				begy += incy;
				begx += incx;
			}
		}
	}

	@Override
	public double dnrm2(int n, double[] x, int begx, int incx) throws BLASException {
		if (n == 1)
			return Math.abs(x[begx]);
		double t, scale = 0, ssq = 1, absx;
		for (int end = begx + n; begx < end; begx += incx) {
			absx = x[begx];
			if (absx == 0)
				continue;
			if (absx < 0)
				absx = -absx;
			if (scale < absx) {
				t = scale / absx;
				ssq = 1 + ssq * t * t;
				scale = absx;
			} else {
				t = absx / scale;
				ssq += t * t;
			}
		}
		return scale * Math.sqrt(ssq);
	}

	@Override
	public int idamax(int n, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		int off = begx;
		int bi = begx;
		double t = x[begx];
		n--;
		begx += incx;
		double bx = t < 0 ? -t : t;
		while (n-- > 0) {
			t = x[begx];
			if (t < 0)
				t = -t;
			if (t > bx) {
				bi = begx;
				bx = t;
			}
			begx += incx;
		}
		return (bi - off) / incx;
	}


	@Override
	public float sasum(int n, float[] x, int begx, int incx) throws BLASException {
		int end;
		float t, sum = 0;
		if (n < 1)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		if (incx == 0)
			return n * Math.abs(x[begx]);
		if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				t = x[begx++];
				if (t < 0)
					sum -= t;
				else
					sum += t;
			}
		} else {
			while (n-- > 0) {
				t = x[begx];
				begx += incx;
				if (t < 0)
					sum -= t;
				else
					sum += t;
			}
		}
		return sum;
	}

	@Override
	public void saxpy(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException {
		int end;
		if (n < 1)
			return;
		if (alpha == 0.0)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			if (alpha == 1)
				while (begx < end)
					y[begy++] += x[begx++];
			else if (alpha == -1)
				while (begx < end)
					y[begy++] -= x[begx++];
			else
				while (begx < end)
					y[begy++] += alpha * x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			if (alpha == 1)
				while (begx < end) {
					y[begy] += x[begx++];
					begy += incy;
				}
			else if (alpha == -1)
				while (begx < end) {
					y[begy] -= x[begx++];
					begy += incy;
				}
			else
				while (begx < end) {
					y[begy] += alpha * x[begx++];
					begy += incy;
				}
		} else if (incy == 1) {
			end = begy + n;
			if (alpha == 1)
				while (begy < end) {
					y[begy++] += x[begx];
					begx += incx;
				}
			else if (alpha == -1)
				while (begy < end) {
					y[begy++] -= x[begx];
					begx += incx;
				}
			else
				while (begy < end) {
					y[begy++] += alpha * x[begx];
					begx += incx;
				}
		} else {
			if (alpha == 1)
				while (n-- > 0) {
					y[begy] += x[begx];
					begx += incx;
					begy += incy;
				}
			else if (alpha == -1)
				while (n-- > 0) {
					y[begy] -= x[begx];
					begx += incx;
					begy += incy;
				}
			else
				while (n-- > 0) {
					y[begy] += alpha * x[begx];
					begx += incx;
					begy += incy;
				}
		}
	}

	@Override
	public void scopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		int end;
		if (n < 1)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end)
				y[begy++] = x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				y[begy] = x[begx++];
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				y[begy++] = x[begx];
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				y[begy] = x[begx];
				begx += incx;
				begy += incy;
			}
		}
	}

	@Override
	public float sdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		int end;
		float sum = 0;
		if (n < 1)
			return sum;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end)
				sum += y[begy++] * x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				sum += y[begy] * x[begx++];
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				sum += y[begy++] * x[begx];
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				sum += y[begy] * x[begx];
				begx += incx;
				begy += incy;
			}
		}
		return sum;
	}

	@Override
	public void srot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin)
			throws BLASException {
		int end;
		float xe, ye;
		if (n <= 0)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end) {
				xe = x[begx];
				ye = y[begy];
				x[begx++] = cos * xe + sin * ye;
				y[begy++] = cos * ye - sin * xe;
			}
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				xe = x[begx];
				ye = y[begy];
				x[begx++] = cos * xe + sin * ye;
				y[begy] = cos * ye - sin * xe;
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				xe = x[begx];
				ye = y[begy];
				x[begx] = cos * xe + sin * ye;
				y[begy++] = cos * ye - sin * xe;
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				xe = x[begx];
				ye = y[begy];
				x[begx] = cos * xe + sin * ye;
				y[begy] = cos * ye - sin * xe;
				begx += incx;
				begy += incy;
			}
		}
	}

	@Override
	public void sscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		int end;
		if (n < 1)
			return;
		if (incx == 1) {
			end = begx + n;
			while (begx < end)
				x[begx++] *= alpha;
		} else {
			while (n-- > 0) {
				x[begx] *= alpha;
				begx += incx;
			}
		}
	}

	@Override
	public void sswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		int end;
		float t;
		if (n < 1)
			return;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end) {
				t = y[begy];
				y[begy++] = x[begx];
				x[begx++] = t;
			}
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				t = y[begy];
				y[begy] = x[begx];
				x[begx++] = t;
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				t = y[begy];
				y[begy++] = x[begx];
				x[begx] = t;
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				t = y[begy];
				y[begy] = x[begx];
				x[begx] = t;
				begy += incy;
				begx += incx;
			}
		}
	}

	@Override
	public float snrm2(int n, float[] x, int begx, int incx) throws BLASException {
		if (n == 1)
			return Math.abs(x[begx]);
		float t, scale = 0, ssq = 1, absx;
		for (int end = begx + n; begx < end; begx += incx) {
			absx = x[begx];
			if (absx == 0)
				continue;
			if (absx < 0)
				absx = -absx;
			if (scale < absx) {
				t = scale / absx;
				ssq = 1 + ssq * t * t;
				scale = absx;
			} else {
				t = absx / scale;
				ssq += t * t;
			}
		}
		return scale * (float) Math.sqrt(ssq);
	}

	@Override
	public int isamax(int n, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		int bi = begx;
		int off = begx;
		float t = x[begx];
		n--;
		begx += incx;
		float bx = t < 0 ? -t : t;
		while (n-- > 0) {
			t = x[begx];
			if (t < 0)
				t = -t;
			if (t > bx) {
				bi = begx;
				bx = t;
			}
			begx += incx;
		}
		return (bi - off) / incx;
	}


	@Override
	public double dzasum(int n, double[] x, int begx, int incx) throws BLASException {
		begx *= 2;
		incx *= 2;
		double t, sumReal = 0;
		if (n < 1) {
			return 0;
		}
		if (n == 1) {
			return Math.abs(x[begx]);
		}
		if (incx == 0) {
			return n * x[begx];
		}
		while (n-- > 0) {
			t = x[begx];
			if (t < 0)
				sumReal -= t;
			else
				sumReal += t;
			begx += incx;
		}
		return sumReal;
	}

	@Override
	public void zaxpy(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy)
			throws BLASException {
		if (n < 1)
			return;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		double ar = alpha.getReal();
		double ai = alpha.getImag();
		if (ar == 0.0 && ai == 0.0)
			return;
		while (n-- > 0) {
			double xr = x[begx];
			double xi = x[begx + 1];
			y[begy] += xr * ar - xi * ai;
			y[begy + 1] += xr * ai + xi * ar;
			begx += incx;
			begy += incy;
		}
	}

	@Override
	public void zcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		while (n-- > 0) {
			y[begy] = x[begx];
			y[begy + 1] = x[begx + 1];
			begx += incx;
			begy += incy;
		}
	}

	@Override
	public Complex zdotu(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException {
		if (results == null)
			results = new Complex();
		double sumReal = 0, sumImag = 0;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		if (n < 1) {
			results.real = 0;
			results.imag = 0;
			return results;
		}
		while (n-- > 0) {
			double xr = x[begx];
			double xi = x[begx + 1];
			double yr = y[begy];
			double yi = y[begy + 1];
			sumReal += xr * yr - xi * yi;
			sumImag += xr * yi + xi * yr;
			begx += incx;
			begy += incy;
		}

		results.real = sumReal;
		results.imag = sumImag;
		return results;
	}

	@Override
	public Complex zdotc(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results)
			throws BLASException {
		if (results == null)
			results = new Complex();
		double sumReal = 0, sumImag = 0;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		if (n < 1) {
			results.real = 0;
			results.imag = 0;
			return results;
		}
		while (n-- > 0) {
			double xr = x[begx];
			double xi = x[begx + 1];
			double yr = y[begy];
			double yi = y[begy + 1];
			sumReal += xr * yr + xi * yi;
			sumImag += xr * yi - xi * yr;
			begx += incx;
			begy += incy;
		}

		results.real = sumReal;
		results.imag = sumImag;
		return results;
	}

	@Override
	public void zscal(int n, ComplexI alpha, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;
		double xr, xi;
		double ar = alpha.getReal();
		double ai = alpha.getImag();
		begx *= 2;
		incx *= 2;
		while (n-- > 0) {
			xr = x[begx];
			xi = x[begx + 1];
			x[begx] = ar * xr - ai * xi;
			x[begx + 1] = ar * xi + ai * xr;
			begx += incx;
		}
	}

	@Override
	public void zswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		double t;
		if (n < 1)
			return;
		while (n-- > 0) {
			t = y[begy];
			y[begy] = x[begx];
			x[begx] = t;
			t = y[begy + 1];
			y[begy + 1] = x[begx + 1];
			x[begx + 1] = t;
			begy += incy;
			begx += incx;
		}
	}


	@Override
	public float scasum(int n, float[] x, int begx, int incx) throws BLASException {
		begx *= 2;
		incx *= 2;
		float t, sumReal = 0;
		if (n < 1) {
			return 0;
		}
		if (n == 1) {
			return (float) Math.abs(x[begx]);
		}
		if (incx == 0) {
			return n * x[begx];
		}
		while (n-- > 0) {
			t = x[begx];
			if (t < 0)
				sumReal -= t;
			else
				sumReal += t;
			begx += incx;
		}
		return sumReal;
	}

	@Override
	public void caxpy(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy)
			throws BLASException {
		if (n < 1)
			return;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		float ar = (float) alpha.getReal();
		float ai = (float) alpha.getImag();
		if (ar == 0.0 && ai == 0.0)
			return;
		while (n-- > 0) {
			float xr = x[begx];
			float xi = x[begx + 1];
			y[begy] += xr * ar - xi * ai;
			y[begy + 1] += xr * ai + xi * ar;
			begx += incx;
			begy += incy;
		}
	}

	@Override
	public void ccopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		while (n-- > 0) {
			y[begy] = x[begx];
			y[begy + 1] = x[begx + 1];
			begx += incx;
			begy += incy;
		}
	}

	@Override
	public Complex cdotu(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException {
		if (results == null)
			results = new Complex();
		float sumReal = 0, sumImag = 0;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		if (n < 1) {
			results.real = 0;
			results.imag = 0;
			return results;
		}
		while (n-- > 0) {
			float xr = x[begx];
			float xi = x[begx + 1];
			float yr = y[begy];
			float yi = y[begy + 1];
			sumReal += xr * yr - xi * yi;
			sumImag += xr * yi + xi * yr;
			begx += incx;
			begy += incy;
		}

		results.real = sumReal;
		results.imag = sumImag;
		return results;
	}

	@Override
	public Complex cdotc(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results)
			throws BLASException {
		if (results == null)
			results = new Complex();
		float sumReal = 0, sumImag = 0;
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		if (n < 1) {
			results.real = 0;
			results.imag = 0;
			return results;
		}
		while (n-- > 0) {
			float xr = x[begx];
			float xi = x[begx + 1];
			float yr = y[begy];
			float yi = y[begy + 1];
			sumReal += xr * yr + xi * yi;
			sumImag += xr * yi - xi * yr;
			begx += incx;
			begy += incy;
		}

		results.real = sumReal;
		results.imag = sumImag;
		return results;
	}

	@Override
	public void cscal(int n, ComplexI alpha, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;
		float xr, xi;
		float ar = (float) alpha.getReal();
		float ai = (float) alpha.getImag();
		begx *= 2;
		incx *= 2;
		while (n-- > 0) {
			xr = x[begx];
			xi = x[begx + 1];
			x[begx] = ar * xr - ai * xi;
			x[begx + 1] = ar * xi + ai * xr;
			begx += incx;
		}
	}

	@Override
	public void cswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		begx *= 2;
		incx *= 2;
		begy *= 2;
		incy *= 2;
		float t;
		if (n < 1)
			return;
		while (n-- > 0) {
			t = y[begy];
			y[begy] = x[begx];
			x[begx] = t;
			t = y[begy + 1];
			y[begy + 1] = x[begx + 1];
			x[begx + 1] = t;
			begy += incy;
			begx += incx;
		}
	}

	@Override
	public double dsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		int end;
		double sum = 0;
		if (n < 1)
			return sum;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end)
				sum += y[begy++] * x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				sum += y[begy] * x[begx++];
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				sum += y[begy++] * x[begx];
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				sum += y[begy] * x[begx];
				begx += incx;
				begy += incy;
			}
		}
		return sum;
	}

	@Override
	public float sdsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		int end;
		double sum = 0;
		if (n < 1)
			return 0;
		if (incx == 1 && incy == 1) {
			end = begx + n;
			while (begx < end)
				sum += y[begy++] * x[begx++];
		} else if (incx == 1) {
			end = begx + n;
			while (begx < end) {
				sum += y[begy] * x[begx++];
				begy += incy;
			}
		} else if (incy == 1) {
			end = begy + n;
			while (begy < end) {
				sum += y[begy++] * x[begx];
				begx += incx;
			}
		} else {
			while (n-- > 0) {
				sum += y[begy] * x[begx];
				begx += incx;
				begy += incy;
			}
		}
		return (float) sum;
	}

	@Override
	public int izamax(int n, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		begx *= 2;
		incx *= 2;
		int off = begx;
		int bi = begx;
		double tr = x[begx];
		double ti = x[begx + 1];
		n--;
		begx += incx;
		double bx = (tr < 0 ? -tr : tr) + (ti < 0 ? -ti : ti);
		while (n-- > 0) {
			tr = x[begx];
			ti = x[begx + 1];
			double tx = (tr < 0 ? -tr : tr) + (ti < 0 ? -ti : ti);
			if (tx > bx) {
				bi = begx;
				bx = tx;
			}
			begx += incx;
		}
		return (bi - off) / incx;
	}

	@Override
	public int icamax(int n, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		begx *= 2;
		incx *= 2;
		int off = begx;
		int bi = begx;
		float tr = x[begx];
		float ti = x[begx + 1];
		n--;
		begx += incx;
		float bx = (tr < 0 ? -tr : tr) + (ti < 0 ? -ti : ti);
		while (n-- > 0) {
			tr = x[begx];
			ti = x[begx + 1];
			float tx = (tr < 0 ? -tr : tr) + (ti < 0 ? -ti : ti);
			if (tx > bx) {
				bi = begx;
				bx = tx;
			}
			begx += incx;
		}
		return (bi - off) / incx;
	}

	@Override
	public double dznrm2(int n, double[] x, int begx, int incx) throws BLASException {
		begx *= 2;
		incx *= 2;
		if (n == 1)
			return Math.sqrt(x[begx] * x[begx] + x[begx + 1] * x[begx + 1]);
		double t, scale = 0, ssq = 1, absx;
		for (int end = begx + n * incx; begx < end; begx += incx) {
			absx = x[begx];
			if (absx != 0) {
				if (absx < 0)
					absx = -absx;
				if (scale < absx) {
					t = scale / absx;
					ssq = 1 + ssq * t * t;
					scale = absx;
				} else {
					t = absx / scale;
					ssq += t * t;
				}
			}
			absx = x[begx + 1];
			if (absx != 0) {
				if (absx < 0)
					absx = -absx;
				if (scale < absx) {
					t = scale / absx;
					ssq = 1 + ssq * t * t;
					scale = absx;
				} else {
					t = absx / scale;
					ssq += t * t;
				}
			}
		}
		return scale * Math.sqrt(ssq);
	}

	@Override
	public float scnrm2(int n, float[] x, int begx, int incx) throws BLASException {
		begx *= 2;
		incx *= 2;
		if (n == 1)
			return (float) Math.sqrt(x[begx] * x[begx] + x[begx + 1] * x[begx + 1]);
		float t, scale = 0, ssq = 1, absx;
		for (int end = begx + n * incx; begx < end; begx += incx) {
			absx = x[begx];
			if (absx != 0) {
				if (absx < 0)
					absx = -absx;
				if (scale < absx) {
					t = scale / absx;
					ssq = 1 + ssq * t * t;
					scale = absx;
				} else {
					t = absx / scale;
					ssq += t * t;
				}
			}
			absx = x[begx + 1];
			if (absx != 0) {
				if (absx < 0)
					absx = -absx;
				if (scale < absx) {
					t = scale / absx;
					ssq = 1 + ssq * t * t;
					scale = absx;
				} else {
					t = absx / scale;
					ssq += t * t;
				}
			}
		}
		return (float) (scale * Math.sqrt(ssq));
	}

	@Override
	public void drotg(Real a, Real b, Real cos, Real sin) throws BLASException {
		double z, r, roe = b.value;
		if (Math.abs(a.value) > Math.abs(b.value))
			roe = a.value;
		double scale = Math.abs(a.value) + Math.abs(b.value);
		if (scale == 0.0) {
			cos.value = 1.0;
			sin.value = 0.0;
			r = 0.0;
		} else {
			double as = a.value / scale;
			double bs = b.value / scale;
			r = scale * Math.sqrt(as * as + bs * bs);
			if (roe < 0)
				r = -r;
			cos.value = a.value / r;
			sin.value = b.value / r;
		}

		z = sin.value;
		if (Math.abs(cos.value) > 0.0 && Math.abs(cos.value) <= sin.value)
			z = 1.0 / cos.value;
		a.value = r;
		b.value = z;
		return;
	}

	@Override
	public void srotg(Real a, Real b, Real cos, Real sin) throws BLASException {
		double z, r, roe = b.value;
		if (Math.abs(a.value) > Math.abs(b.value))
			roe = a.value;
		double scale = Math.abs(a.value) + Math.abs(b.value);
		if (scale == 0.0) {
			cos.value = 1.0;
			sin.value = 0.0;
			r = 0.0;
		} else {
			double as = a.value / scale;
			double bs = b.value / scale;
			r = scale * Math.sqrt(as * as + bs * bs);
			if (roe < 0)
				r = -r;
			cos.value = a.value / r;
			sin.value = b.value / r;
		}

		z = sin.value;
		if (Math.abs(cos.value) > 0.0 && Math.abs(cos.value) <= sin.value)
			z = 1.0 / cos.value;
		a.value = r;
		b.value = z;
		return;
	}

	@Override
	public void zdscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;
		begx *= 2;
		incx *= 2;
		while (n-- > 0) {
			x[begx] *= alpha;
			x[begx + 1] *= alpha;
			begx += incx;
		}
	}

	@Override
	public void csscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;
		begx *= 2;
		incx *= 2;
		while (n-- > 0) {
			x[begx] *= alpha;
			x[begx + 1] *= alpha;
			begx += incx;
		}
	}

}
