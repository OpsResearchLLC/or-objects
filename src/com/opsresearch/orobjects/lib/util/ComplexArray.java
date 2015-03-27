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

package com.opsresearch.orobjects.lib.util;

import com.opsresearch.orobjects.lib.LibError;
import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class ComplexArray {

	static public double[] resize(int n, double[] array) {
		n *= 2;
		if (array == null)
			return new double[n];
		if (array.length == n)
			return array;
		if (array.length % 2 != 0)
			throw new LibError("A complex array must have an even number of elements.");
		int c = Math.min(array.length, n);
		double[] newArray = new double[n];
		if (c < 10)
			for (int i = 0; i < c; i++)
				newArray[i] = array[i];
		else
			ComplexArray.copy(c, newArray, 0, 1, array, 0, 1);
		return newArray;
	}

	static public double[] resize(int n, double[] array, ComplexI fill) {
		n *= 2;
		if (array == null) {
			double[] newArray = new double[n];
			for (int i = 0; i < n;) {
				newArray[i++] = fill.getReal();
				newArray[i++] = fill.getImag();
			}
			return newArray;
		}

		if (array.length == n)
			return array;
		if (array.length % 2 != 0)
			throw new LibError("A complex array must have an even number of elements.");
		int c = Math.min(array.length, n);
		double[] newArray = new double[n];
		if (c < 10)
			for (int i = 0; i < c; i++)
				newArray[i] = array[i];
		else
			Array.copy(c, newArray, 0, 1, array, 0, 1);
		if (n - c < 20) {
			for (int i = c; i < n; i += 2) {
				newArray[i] = fill.getReal();
				newArray[i + 1] = fill.getImag();
			}
		} else
			ComplexArray.copy((n - c) / 2, newArray, c / 2, 1, fill);

		return newArray;
	}

	static public double[][] resize(int n, double[][] array) {
		if (array == null)
			return new double[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		double[][] newArray = new double[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, double[] x, int begX, int incX) {
		begX *= 2;
		incX *= 2;
		String sep = "\n";
		String s = "Array: [" + begX / 2 + "," + ((begX + incX) / 2) + "..." + begX + ((begX + (n - 1) * incX) / 2)
				+ "] =" + sep;
		if (n < 1)
			return s;
		double real = x[begX];
		double imag = x[begX + 1];
		double lr = real;
		double li = imag;
		boolean ptd = false;
		s += "[" + begX / 2 + "] (" + real + ", " + imag + ")" + sep;
		begX += incX;
		while (--n > 0) { 
			real = x[begX];
			imag = x[begX + 1];
			if (real == lr && imag == li) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX / 2 + "] (" + real + ", " + imag + ")" + sep;
				lr = real;
				li = imag;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, double[] x, int begX, int incX, double[] y, int begY, int incY, double epsilon) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		if (epsilon == 0.0) {
			while (--n >= 0) {
				if (x[begX] != y[begY] || x[begX + 1] != y[begY + 1])
					return false;
				begX += incX;
				begY += incY;
			}
		} else {
			while (--n >= 0) {
				double dif = x[begX] - y[begY];
				if (dif < 0) {
					if (dif < -epsilon)
						return false;
				} else {
					if (dif > epsilon)
						return false;
				}
				dif = x[begX + 1] - y[begY + 1];
				if (dif < 0) {
					if (dif < -epsilon)
						return false;
				} else {
					if (dif > epsilon)
						return false;
				}
				begX += incX;
				begY += incY;
			}
		}
		return true;
	}

	static public int count(int n, double[] x, int begX, int incX, ComplexI low, ComplexI high) {
		int cnt = 0;
		begX *= 2;
		incX *= 2;
		double lowR = low.getReal();
		double lowI = low.getImag();
		double highR = high.getReal();
		double highI = high.getImag();
		if (lowR == highR && lowI == highI) {
			while (--n >= 0) {
				if (x[begX] == lowR && x[begX + 1] == lowI)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				double s = x[begX];
				if (s < lowR || s > highR)
					continue;
				s = x[begX + 1];
				if (s >= lowI && s <= highI)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, double[] x, int begX, int incX, ComplexI low, ComplexI high) {
		begX *= 2;
		incX *= 2;
		double lowR = low.getReal();
		double lowI = low.getImag();
		double highR = high.getReal();
		double highI = high.getImag();
		if (lowR == highR && lowI == highI) {
			while (--n >= 0) {
				if (x[begX] == lowR && x[begX + 1] == lowI)
					return begX / 2;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				double s = x[begX];
				if (s < lowR || s > highR)
					continue;
				s = x[begX + 1];
				if (s >= lowI && s <= highI)
					return begX / 2;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, double[] x, int begX, int incX, double[] y, int begY, int incY) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		while (--n >= 0) {
			x[begX] = y[begY];
			x[begX + 1] = y[begY + 1];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, double[] x, int begX, int incX, ComplexI scaler) {
		begX *= 2;
		incX *= 2;
		double real = scaler.getReal();
		double imag = scaler.getImag();
		while (--n >= 0) {
			x[begX] = real;
			x[begX + 1] = imag;
			begX += incX;
		}
	}

	static public Complex sum(int n, double[] x, int begX, int incX, Complex results) {
		begX *= 2;
		incX *= 2;
		if (results == null)
			results = new Complex();
		double real = 0;
		double imag = 0;
		while (--n >= 0) {
			real += x[begX];
			imag += x[begX + 1];
			begX += incX;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfSquares(int n, double[] x, int begX, int incX, Complex results) {
		begX *= 2;
		incX *= 2;
		if (results == null)
			results = new Complex();
		double real = 0;
		double imag = 0;
		double xr, xi;
		while (--n >= 0) {
			xr = x[begX];
			xi = x[begX + 1];
			if (xi == 0)
				real += xr * xr;
			else if (xr == 0)
				real -= xi * xi;
			else {
				real += xr * xr - xi * xi;
				imag += 2 * xr * xi;
			}
			begX += incX;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfSquaredDifferences(int n, double[] x, int begX, int incX, ComplexI scaler,
			Complex results) {
		begX *= 2;
		incX *= 2;
		if (results == null)
			results = new Complex();
		double sr = scaler.getReal();
		double si = scaler.getImag();
		double real = 0;
		double imag = 0;
		double xr, xi;
		while (--n >= 0) {
			xr = x[begX] - sr;
			xi = x[begX + 1] - si;
			if (xi == 0)
				real += xr * xr;
			else if (xr == 0)
				real -= xi * xi;
			else {
				real += xr * xr - xi * xi;
				imag += 2 * xr * xi;
			}
			begX += incX;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfSquaredDifferences(int n, double[] x, int begX, int incX, double[] y, int begY,
			int incY, Complex results) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		if (results == null)
			results = new Complex();
		double real = 0;
		double imag = 0;
		double xr, xi;
		while (--n >= 0) {
			xr = x[begX] - y[begY];
			xi = x[begX + 1] - y[begY + 1];
			if (xi == 0)
				real += xr * xr;
			else if (xr == 0)
				real -= xi * xi;
			else {
				real += xr * xr - xi * xi;
				imag += 2 * xr * xi;
			}
			begX += incX;
			begY += incY;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfProducts(int n, double[] x, int begX, int incX, double[] y, int begY, int incY,
			Complex results) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		if (results == null)
			results = new Complex();
		double real = 0;
		double imag = 0;
		double xr, xi;
		double yr, yi;
		while (--n >= 0) {
			xr = x[begX];
			xi = x[begX + 1];
			yr = y[begY];
			yi = y[begY + 1];
			real += xr * yr - xi * yi;
			imag += xr * yi + xi * yr;
			begX += incX;
			begY += incY;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}


	static public float[] resize(int n, float[] array) {
		n *= 2;
		if (array == null)
			return new float[n];
		if (array.length == n)
			return array;
		if (array.length % 2 != 0)
			throw new LibError("A complex array must have an even number of elements.");
		int c = Math.min(array.length, n);
		float[] newArray = new float[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public float[] resize(int n, float[] array, ComplexI fill) {
		n *= 2;
		if (array == null) {
			float[] newArray = new float[n];
			for (int i = 0; i < n;) {
				newArray[i++] = (float) fill.getReal();
				newArray[i++] = (float) fill.getImag();
			}
			return newArray;
		}

		if (array.length == n)
			return array;
		if (array.length % 2 != 0)
			throw new LibError("A complex array must have an even number of elements.");
		int c = Math.min(array.length, n);
		float[] newArray = new float[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n;) {
			newArray[i++] = (float) fill.getReal();
			newArray[i++] = (float) fill.getImag();
		}
		return newArray;
	}

	static public float[][] resize(int n, float[][] array) {
		if (array == null)
			return new float[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		float[][] newArray = new float[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, float[] x, int begX, int incX) {
		begX *= 2;
		incX *= 2;
		String sep = "\n";
		String s = "Array: [" + begX / 2 + "," + ((begX + incX) / 2) + "..." + begX + ((begX + (n - 1) * incX) / 2)
				+ "] =" + sep;
		if (n < 1)
			return s;
		float real = x[begX];
		float imag = x[begX + 1];
		float lr = real;
		float li = imag;
		boolean ptd = false;
		s += "[" + begX / 2 + "] (" + real + ", " + imag + ")" + sep;
		begX += incX;
		while (--n > 0) { 
			real = x[begX];
			imag = x[begX + 1];
			if (real == lr && imag == li) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX / 2 + "] (" + real + ", " + imag + ")" + sep;
				lr = real;
				li = imag;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, float[] x, int begX, int incX, float[] y, int begY, int incY, float epsilon) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		if (epsilon == 0.0) {
			while (--n >= 0) {
				if (x[begX] != y[begY] || x[begX + 1] != y[begY + 1])
					return false;
				begX += incX;
				begY += incY;
			}
		} else {
			while (--n >= 0) {
				float dif = x[begX] - y[begY];
				if (dif < 0) {
					if (dif < -epsilon)
						return false;
				} else {
					if (dif > epsilon)
						return false;
				}
				dif = x[begX + 1] - y[begY + 1];
				if (dif < 0) {
					if (dif < -epsilon)
						return false;
				} else {
					if (dif > epsilon)
						return false;
				}
				begX += incX;
				begY += incY;
			}
		}
		return true;
	}

	static public int count(int n, float[] x, int begX, int incX, ComplexI low, ComplexI high) {
		int cnt = 0;
		begX *= 2;
		incX *= 2;
		float lowR = (float) low.getReal();
		float lowI = (float) low.getImag();
		float highR = (float) high.getReal();
		float highI = (float) high.getImag();
		if (lowR == highR && lowI == highI) {
			while (--n >= 0) {
				if (x[begX] == lowR && x[begX + 1] == lowI)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				float s = x[begX];
				if (s < lowR || s > highR)
					continue;
				s = x[begX + 1];
				if (s >= lowI && s <= highI)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, float[] x, int begX, int incX, ComplexI low, ComplexI high) {
		begX *= 2;
		incX *= 2;
		float lowR = (float) low.getReal();
		float lowI = (float) low.getImag();
		float highR = (float) high.getReal();
		float highI = (float) high.getImag();
		if (lowR == highR && lowI == highI) {
			while (--n >= 0) {
				if (x[begX] == lowR && x[begX + 1] == lowI)
					return begX / 2;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				float s = x[begX];
				if (s < lowR || s > highR)
					continue;
				s = x[begX + 1];
				if (s >= lowI && s <= highI)
					return begX / 2;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, float[] x, int begX, int incX, float[] y, int begY, int incY) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		while (--n >= 0) {
			x[begX] = y[begY];
			x[begX + 1] = y[begY + 1];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, float[] x, int begX, int incX, ComplexI scaler) {
		begX *= 2;
		incX *= 2;
		float real = (float) scaler.getReal();
		float imag = (float) scaler.getImag();
		while (--n >= 0) {
			x[begX] = real;
			x[begX + 1] = imag;
			begX += incX;
		}
	}

	static public Complex sum(int n, float[] x, int begX, int incX, Complex results) {
		begX *= 2;
		incX *= 2;
		if (results == null)
			results = new Complex();
		float real = 0;
		float imag = 0;
		while (--n >= 0) {
			real += x[begX];
			imag += x[begX + 1];
			begX += incX;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfSquares(int n, float[] x, int begX, int incX, Complex results) {
		begX *= 2;
		incX *= 2;
		if (results == null)
			results = new Complex();
		float real = 0;
		float imag = 0;
		float xr, xi;
		while (--n >= 0) {
			xr = x[begX];
			xi = x[begX + 1];
			if (xi == 0)
				real += xr * xr;
			else if (xr == 0)
				real -= xi * xi;
			else {
				real += xr * xr - xi * xi;
				imag += 2 * xr * xi;
			}
			begX += incX;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfSquaredDifferences(int n, float[] x, int begX, int incX, ComplexI scaler, Complex results) {
		begX *= 2;
		incX *= 2;
		if (results == null)
			results = new Complex();
		float sr = (float) scaler.getReal();
		float si = (float) scaler.getImag();
		float real = 0;
		float imag = 0;
		float xr, xi;
		while (--n >= 0) {
			xr = x[begX] - sr;
			xi = x[begX + 1] - si;
			if (xi == 0)
				real += xr * xr;
			else if (xr == 0)
				real -= xi * xi;
			else {
				real += xr * xr - xi * xi;
				imag += 2 * xr * xi;
			}
			begX += incX;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfSquaredDifferences(int n, float[] x, int begX, int incX, float[] y, int begY, int incY,
			Complex results) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		if (results == null)
			results = new Complex();
		float real = 0;
		float imag = 0;
		float xr, xi;
		while (--n >= 0) {
			xr = x[begX] - y[begY];
			xi = x[begX + 1] - y[begY + 1];
			if (xi == 0)
				real += xr * xr;
			else if (xr == 0)
				real -= xi * xi;
			else {
				real += xr * xr - xi * xi;
				imag += 2 * xr * xi;
			}
			begX += incX;
			begY += incY;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

	static public Complex sumOfProducts(int n, float[] x, int begX, int incX, float[] y, int begY, int incY,
			Complex results) {
		begX *= 2;
		incX *= 2;
		begY *= 2;
		incY *= 2;
		if (results == null)
			results = new Complex();
		float real = 0;
		float imag = 0;
		float xr, xi;
		float yr, yi;
		while (--n >= 0) {
			xr = x[begX];
			xi = x[begX + 1];
			yr = y[begY];
			yi = y[begY + 1];
			real += xr * yr - xi * yi;
			imag += xr * yi + xi * yr;
			begX += incX;
			begY += incY;
		}
		results.real = real;
		results.imag = imag;
		return results;
	}

}
