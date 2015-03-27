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

public class Array {

	static public float[] resize(int n, float[] array) {
		if (array == null)
			return new float[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		float[] newArray = new float[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public float[] resize(int n, float[] array, float fill) {
		if (array == null) {
			float[] newArray = new float[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		float[] newArray = new float[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
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
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		float v = x[begX];
		float l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, float[] x, int begX, int incX, float[] y, int begY, int incY, float epsilon) {
		if (epsilon == 0.0) {
			while (--n >= 0) {
				if (x[begX] != y[begY])
					return false;
				begX += incX;
				begY += incY;
			}
		} else {
			float mepsilon = -epsilon;
			while (--n >= 0) {
				float dif = x[begX] - y[begY];
				if (dif < 0) {
					if (dif < mepsilon)
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

	static public int count(int n, float[] x, int begX, int incX, float low, float high) {
		int cnt = 0;
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				float s = x[begX];
				if (s >= low && s <= high)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, float[] x, int begX, int incX, float low, float high) {
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				float s = x[begX];
				if (s >= low && s <= high)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, float[] x, int begX, int incX, float[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, float[] x, int begX, int incX, float scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

	static public double sum(int n, float[] x, int begX, int incX) {
		double sum = 0;
		while (--n >= 0) {
			sum += x[begX];
			begX += incX;
		}
		return sum;
	}

	static public double sumOfSquares(int n, float[] x, int begX, int incX) {
		double sum = 0;
		while (--n >= 0) {
			double t = x[begX];
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public double sumOfSquaredDifferences(int n, float[] x, int begX, int incX, float scaler) {
		double sum = 0;
		while (--n >= 0) {
			double t = x[begX] - scaler;
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public double sumOfSquaredDifferences(int n, float[] x, int begX, int incX, float[] y, int begY, int incY) {
		double sum = 0;
		while (--n >= 0) {
			double t = x[begX] - y[begY];
			sum += t * t;
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public double sumOfProducts(int n, float[] x, int begX, int incX, float[] y, int begY, int incY) {
		double sum = 0;
		while (--n >= 0) {
			sum += x[begX] * y[begY];
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public float maximumAbsoluteValue(int n, float[] x, int begX, int incX) {
		float max = 0;
		if (incX == 1) {
			while (--n >= 0) {
				float abs = Math.abs(x[begX++]);
				if (abs > max)
					max = abs;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				float abs = Math.abs(x[begX--]);
				if (abs > max)
					max = abs;
			}
		} else {
			while (--n >= 0) {
				float abs = Math.abs(x[begX]);
				begX += incX;
				if (abs > max)
					max = abs;
			}
		}
		return max;
	}

	static public float minimumAbsoluteValue(int n, float[] x, int begX, int incX) {
		float min = Float.MAX_VALUE;
		if (incX == 1) {
			while (--n >= 0) {
				float abs = Math.abs(x[begX++]);
				if (abs < min)
					min = abs;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				float abs = Math.abs(x[begX--]);
				if (abs < min)
					min = abs;
			}
		} else {
			while (--n >= 0) {
				float abs = Math.abs(x[begX]);
				begX += incX;
				if (abs < min)
					min = abs;
			}
		}
		return min;
	}

	static public float maximumValue(int n, float[] x, int begX, int incX) {
		float max = 0;
		if (incX == 1) {
			while (--n >= 0) {
				float v = x[begX++];
				if (v > max)
					max = v;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				float v = x[begX--];
				if (v > max)
					max = v;
			}
		} else {
			while (--n >= 0) {
				float v = x[begX];
				begX += incX;
				if (v > max)
					max = v;
			}
		}
		return max;
	}

	static public float minimumValue(int n, float[] x, int begX, int incX) {
		float min = Float.MAX_VALUE;
		if (incX == 1) {
			while (--n >= 0) {
				float v = x[begX++];
				if (v < min)
					min = v;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				float v = x[begX--];
				if (v < min)
					min = v;
			}
		} else {
			while (--n >= 0) {
				float v = x[begX];
				begX += incX;
				if (v < min)
					min = v;
			}
		}
		return min;
	}


	static public double[] resize(int n, double[] array) {
		if (array == null)
			return new double[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		double[] newArray = new double[n];
		if (c < 10)
			for (int i = 0; i < c; i++)
				newArray[i] = array[i];
		else
			Array.copy(c, newArray, 0, 1, array, 0, 1);
		return newArray;
	}

	static public double[] resize(int n, double[] array, double fill) {
		if (array == null) {
			double[] newArray = new double[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		double[] newArray = new double[n];
		if (c < 10)
			for (int i = 0; i < c; i++)
				newArray[i] = array[i];
		else
			Array.copy(c, newArray, 0, 1, array, 0, 1);
		if (n - c < 10)
			for (int i = c; i < n; i++)
				newArray[i] = fill;
		else
			Array.copy(n - c, newArray, c, 1, fill);
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
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		double v = x[begX];
		double l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, double[] x, int begX, int incX, double[] y, int begY, int incY, double epsilon) {
		if (epsilon == 0.0) {
			while (--n >= 0) {
				if (x[begX] != y[begY])
					return false;
				begX += incX;
				begY += incY;
			}
		} else {
			double mepsilon = -epsilon;
			while (--n >= 0) {
				double dif = x[begX] - y[begY];
				if (dif < 0) {
					if (dif < mepsilon)
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

	static public int count(int n, double[] x, int begX, int incX, double low, double high) {
		int cnt = 0;
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				double s = x[begX];
				if (s >= low && s <= high)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, double[] x, int begX, int incX, double low, double high) {
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				double s = x[begX];
				if (s >= low && s <= high)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, double[] x, int begX, int incX, double[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, double[] x, int begX, int incX, double scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

	static public double sum(int n, double[] x, int begX, int incX) {
		double sum = 0;
		while (--n >= 0) {
			sum += x[begX];
			begX += incX;
		}
		return sum;
	}

	static public double sumOfSquares(int n, double[] x, int begX, int incX) {
		double sum = 0;
		while (--n >= 0) {
			double t = x[begX];
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public double sumOfSquaredDifferences(int n, double[] x, int begX, int incX, double scaler) {
		double sum = 0;
		while (--n >= 0) {
			double t = x[begX] - scaler;
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public double sumOfSquaredDifferences(int n, double[] x, int begX, int incX, double[] y, int begY, int incY) {
		double sum = 0;
		while (--n >= 0) {
			double t = x[begX] - y[begY];
			sum += t * t;
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public double sumOfProducts(int n, double[] x, int begX, int incX, double[] y, int begY, int incY) {
		double sum = 0;
		while (--n >= 0) {
			sum += x[begX] * y[begY];
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public double maximumAbsoluteValue(int n, double[] x, int begX, int incX) {
		double max = 0;
		if (incX == 1) {
			while (--n >= 0) {
				double abs = Math.abs(x[begX++]);
				if (abs > max)
					max = abs;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				double abs = Math.abs(x[begX--]);
				if (abs > max)
					max = abs;
			}
		} else {
			while (--n >= 0) {
				double abs = Math.abs(x[begX]);
				begX += incX;
				if (abs > max)
					max = abs;
			}
		}
		return max;
	}

	static public double minimumAbsoluteValue(int n, double[] x, int begX, int incX) {
		double min = Double.MAX_VALUE;
		if (incX == 1) {
			while (--n >= 0) {
				double abs = Math.abs(x[begX++]);
				if (abs < min)
					min = abs;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				double abs = Math.abs(x[begX--]);
				if (abs < min)
					min = abs;
			}
		} else {
			while (--n >= 0) {
				double abs = Math.abs(x[begX]);
				begX += incX;
				if (abs < min)
					min = abs;
			}
		}
		return min;
	}

	static public double maximumValue(int n, double[] x, int begX, int incX) {
		double max = 0;
		if (incX == 1) {
			while (--n >= 0) {
				double v = x[begX++];
				if (v > max)
					max = v;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				double v = x[begX--];
				if (v > max)
					max = v;
			}
		} else {
			while (--n >= 0) {
				double v = x[begX];
				begX += incX;
				if (v > max)
					max = v;
			}
		}
		return max;
	}

	static public double minimumValue(int n, double[] x, int begX, int incX) {
		double min = Double.MAX_VALUE;
		if (incX == 1) {
			while (--n >= 0) {
				double v = x[begX++];
				if (v < min)
					min = v;
			}
		} else if (incX == -1) {
			while (--n >= 0) {
				double v = x[begX--];
				if (v < min)
					min = v;
			}
		} else {
			while (--n >= 0) {
				double v = x[begX];
				begX += incX;
				if (v < min)
					min = v;
			}
		}
		return min;
	}


	public static byte[] union(byte[] a, byte[] b) {
		QuickSort<byte[]> qs = new QuickSort<byte[]>();
		if (a != null)
			qs.sort(a);
		else
			a = new byte[0];

		if (b != null)
			qs.sort(b);
		else
			b = new byte[0];

		int i = 0, j = 0, c = 0;
		byte[] t = new byte[a.length + b.length];
		while (i < a.length && j < b.length) {
			if (a[i] == b[j]) {
				t[c++] = a[i];
				i++;
				j++;
			} else if (a[i] < b[j]) {
				t[c++] = a[i++];
			} else {
				t[c++] = b[j++];
			}
			;
		}
		while (i < a.length)
			t[c++] = a[i++];
		while (j < b.length)
			t[c++] = b[j++];
		byte[] r = new byte[c];
		for (int k = 0; k < c; k++)
			r[k] = t[k];
		return r;
	}

	static public byte[] resize(int n, byte[] array) {
		if (array == null)
			return new byte[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		byte[] newArray = new byte[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public byte[] resize(int n, byte[] array, byte fill) {
		if (array == null) {
			byte[] newArray = new byte[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		byte[] newArray = new byte[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public byte[][] resize(int n, byte[][] array) {
		if (array == null)
			return new byte[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		byte[][] newArray = new byte[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, byte[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		byte v = x[begX];
		byte l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, byte[] x, int begX, int incX, byte[] y, int begY, int incY) {
		while (--n >= 0) {
			if (x[begX] != y[begY])
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, byte[] x, int begX, int incX, byte low, byte high) {
		int cnt = 0;
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				byte s = x[begX];
				if (s >= low && s <= high)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, byte[] x, int begX, int incX, byte low, byte high) {
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				byte s = x[begX];
				if (s >= low && s <= high)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, byte[] x, int begX, int incX, byte[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, byte[] x, int begX, int incX, byte scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

	static public int sum(int n, byte[] x, int begX, int incX) {
		int sum = 0;
		while (--n >= 0) {
			sum += x[begX];
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquares(int n, byte[] x, int begX, int incX) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX];
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquaredDifferences(int n, byte[] x, int begX, int incX, byte scaler) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX] - scaler;
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquaredDifferences(int n, byte[] x, int begX, int incX, byte[] y, int begY, int incY) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX] - y[begY];
			sum += t * t;
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public int sumOfProducts(int n, byte[] x, int begX, int incX, byte[] y, int begY, int incY) {
		int sum = 0;
		while (--n >= 0) {
			sum += x[begX] * y[begY];
			begX += incX;
			begY += incY;
		}
		return sum;
	}


	public static short[] union(short[] a, short[] b) {
		QuickSort<short[]> qs = new QuickSort<short[]>();
		if (a != null)
			qs.sort(a);
		else
			a = new short[0];

		if (b != null)
			qs.sort(b);
		else
			b = new short[0];

		int i = 0, j = 0, c = 0;
		short[] t = new short[a.length + b.length];
		while (i < a.length && j < b.length) {
			if (a[i] == b[j]) {
				t[c++] = a[i];
				i++;
				j++;
			} else if (a[i] < b[j]) {
				t[c++] = a[i++];
			} else {
				t[c++] = b[j++];
			}
			;
		}
		while (i < a.length)
			t[c++] = a[i++];
		while (j < b.length)
			t[c++] = b[j++];
		short[] r = new short[c];
		for (int k = 0; k < c; k++)
			r[k] = t[k];
		return r;
	}

	static public short[] resize(int n, short[] array) {
		if (array == null)
			return new short[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		short[] newArray = new short[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public short[] resize(int n, short[] array, short fill) {
		if (array == null) {
			short[] newArray = new short[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		short[] newArray = new short[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public short[][] resize(int n, short[][] array) {
		if (array == null)
			return new short[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		short[][] newArray = new short[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, short[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		short v = x[begX];
		short l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) {
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, short[] x, int begX, int incX, short[] y, int begY, int incY) {
		while (--n >= 0) {
			if (x[begX] != y[begY])
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, short[] x, int begX, int incX, short low, short high) {
		int cnt = 0;
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				short s = x[begX];
				if (s >= low && s <= high)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, short[] x, int begX, int incX, short low, short high) {
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				short s = x[begX];
				if (s >= low && s <= high)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, short[] x, int begX, int incX, short[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, short[] x, int begX, int incX, short scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

	static public int sum(int n, short[] x, int begX, int incX) {
		int sum = 0;
		while (--n >= 0) {
			sum += x[begX];
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquares(int n, short[] x, int begX, int incX) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX];
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquaredDifferences(int n, short[] x, int begX, int incX, short scaler) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX] - scaler;
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquaredDifferences(int n, short[] x, int begX, int incX, short[] y, int begY, int incY) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX] - y[begY];
			sum += t * t;
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public int sumOfProducts(int n, short[] x, int begX, int incX, short[] y, int begY, int incY) {
		int sum = 0;
		while (--n >= 0) {
			sum += x[begX] * y[begY];
			begX += incX;
			begY += incY;
		}
		return sum;
	}


	public static int position(int[] a, int v) {
		for (int i = 0; i < a.length; i++)
			if (a[i] == v)
				return i;
		return -1;
	}

	public static int[] union(int[] a, int[] b) {
		QuickSort<int[]> qs = new QuickSort<int[]>();
		if (a != null)
			qs.sort(a);
		else
			a = new int[0];

		if (b != null)
			qs.sort(b);
		else
			b = new int[0];

		int i = 0, j = 0, c = 0;
		int[] t = new int[a.length + b.length];
		while (i < a.length && j < b.length) {
			if (a[i] == b[j]) {
				t[c++] = a[i];
				i++;
				j++;
			} else if (a[i] < b[j]) {
				t[c++] = a[i++];
			} else {
				t[c++] = b[j++];
			}
			;
		}
		while (i < a.length)
			t[c++] = a[i++];
		while (j < b.length)
			t[c++] = b[j++];
		int[] r = new int[c];
		for (int k = 0; k < c; k++)
			r[k] = t[k];
		return r;
	}

	static public int[] resize(int n, int[] array) {
		if (array == null)
			return new int[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		int[] newArray = new int[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public int[] resize(int n, int[] array, int fill) {
		if (array == null) {
			int[] newArray = new int[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		int[] newArray = new int[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public int[][] resize(int n, int[][] array) {
		if (array == null)
			return new int[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		int[][] newArray = new int[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, int[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		int v = x[begX];
		int l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, int[] x, int begX, int incX, int[] y, int begY, int incY) {
		while (--n >= 0) {
			if (x[begX] != y[begY])
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, int[] x, int begX, int incX, int low, int high) {
		int cnt = 0;
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				int s = x[begX];
				if (s >= low && s <= high)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, int[] x, int begX, int incX, int low, int high) {
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				int s = x[begX];
				if (s >= low && s <= high)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, int[] x, int begX, int incX, int[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, int[] x, int begX, int incX, int scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

	static public int sum(int n, int[] x, int begX, int incX) {
		int sum = 0;
		while (--n >= 0) {
			sum += x[begX];
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquares(int n, int[] x, int begX, int incX) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX];
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquaredDifferences(int n, int[] x, int begX, int incX, int scaler) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX] - scaler;
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public int sumOfSquaredDifferences(int n, int[] x, int begX, int incX, int[] y, int begY, int incY) {
		int sum = 0;
		while (--n >= 0) {
			int t = x[begX] - y[begY];
			sum += t * t;
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public int sumOfProducts(int n, int[] x, int begX, int incX, int[] y, int begY, int incY) {
		int sum = 0;
		while (--n >= 0) {
			sum += x[begX] * y[begY];
			begX += incX;
			begY += incY;
		}
		return sum;
	}


	public static long[] union(long[] a, long[] b) {
		QuickSort<long[]> qs = new QuickSort<long[]>();
		if (a != null)
			qs.sort(a);
		else
			a = new long[0];

		if (b != null)
			qs.sort(b);
		else
			b = new long[0];

		int i = 0, j = 0, c = 0;
		long[] t = new long[a.length + b.length];
		while (i < a.length && j < b.length) {
			if (a[i] == b[j]) {
				t[c++] = a[i];
				i++;
				j++;
			} else if (a[i] < b[j]) {
				t[c++] = a[i++];
			} else {
				t[c++] = b[j++];
			}
			;
		}
		while (i < a.length)
			t[c++] = a[i++];
		while (j < b.length)
			t[c++] = b[j++];
		long[] r = new long[c];
		for (int k = 0; k < c; k++)
			r[k] = t[k];
		return r;
	}

	static public long[] resize(int n, long[] array) {
		if (array == null)
			return new long[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		long[] newArray = new long[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public long[] resize(int n, long[] array, long fill) {
		if (array == null) {
			long[] newArray = new long[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		long[] newArray = new long[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public long[][] resize(int n, long[][] array) {
		if (array == null)
			return new long[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		long[][] newArray = new long[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, long[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		long v = x[begX];
		long l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, long[] x, int begX, int incX, long[] y, int begY, int incY) {
		while (--n >= 0) {
			if (x[begX] != y[begY])
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, long[] x, int begX, int incX, long low, long high) {
		int cnt = 0;
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				long s = x[begX];
				if (s >= low && s <= high)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, long[] x, int begX, int incX, long low, long high) {
		if (low == high) {
			while (--n >= 0) {
				if (x[begX] == low)
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				long s = x[begX];
				if (s >= low && s <= high)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, long[] x, int begX, int incX, long[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, long[] x, int begX, int incX, long scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

	static public long sum(int n, long[] x, int begX, int incX) {
		long sum = 0;
		while (--n >= 0) {
			sum += x[begX];
			begX += incX;
		}
		return sum;
	}

	static public long sumOfSquares(int n, long[] x, int begX, int incX) {
		long sum = 0;
		while (--n >= 0) {
			long t = x[begX];
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public long sumOfSquaredDifferences(int n, long[] x, int begX, int incX, long scaler) {
		long sum = 0;
		while (--n >= 0) {
			long t = x[begX] - scaler;
			sum += t * t;
			begX += incX;
		}
		return sum;
	}

	static public long sumOfSquaredDifferences(int n, long[] x, int begX, int incX, long[] y, int begY, int incY) {
		long sum = 0;
		while (--n >= 0) {
			long t = x[begX] - y[begY];
			sum += t * t;
			begX += incX;
			begY += incY;
		}
		return sum;
	}

	static public long sumOfProducts(int n, long[] x, int begX, int incX, long[] y, int begY, int incY) {
		long sum = 0;
		while (--n >= 0) {
			sum += x[begX] * y[begY];
			begX += incX;
			begY += incY;
		}
		return sum;
	}


	public static String[] union(String[] a, String[] b) {
		QuickSort<String[]> qs = new QuickSort<String[]>();
		if (a != null)
			qs.sort(a);
		else
			a = new String[0];

		if (b != null)
			qs.sort(b);
		else
			b = new String[0];

		int i = 0, j = 0, c = 0;
		String[] t = new String[a.length + b.length];
		while (i < a.length && j < b.length) {
			int cmp = a[i].compareTo(b[j]);
			if (cmp == 0) {
				t[c++] = a[i];
				i++;
				j++;
			} else if (cmp < 0) {
				t[c++] = a[i++];
			} else {
				t[c++] = b[j++];
			}
			;
		}
		while (i < a.length)
			t[c++] = a[i++];
		while (j < b.length)
			t[c++] = b[j++];
		String[] r = new String[c];
		for (int k = 0; k < c; k++)
			r[k] = t[k];
		return r;
	}

	static public String[] resize(int n, String[] array) {
		if (array == null)
			return new String[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		String[] newArray = new String[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String[] resize(int n, String[] array, String fill) {
		if (array == null) {
			String[] newArray = new String[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		String[] newArray = new String[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public String[][] resize(int n, String[][] array) {
		if (array == null)
			return new String[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		String[][] newArray = new String[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, String[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		String v = x[begX];
		String l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l || v.equals(l)) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, String[] x, int begX, int incX, String[] y, int begY, int incY) {
		while (--n >= 0) {
			String sx = x[begX];
			String sy = y[begY];
			if (sx != sy && !sx.equals(sy))
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, String[] x, int begX, int incX, String low, String high) {
		int cnt = 0;
		if (low == high || low.equals(high)) {
			while (--n >= 0) {
				String s = x[begX];
				if (s == low || s.equals(low))
					cnt++;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				String s = x[begX];
				if (s.compareTo(low) >= 0 && s.compareTo(high) <= 0)
					cnt++;
				begX += incX;
			}
		}
		return cnt;
	}

	static public int find(int n, String[] x, int begX, int incX, String low, String high) {
		if (low == high || low.equals(high)) {
			while (--n >= 0) {
				String s = x[begX];
				if (s == low || s.equals(low))
					return begX;
				begX += incX;
			}
		} else {
			while (--n >= 0) {
				String s = x[begX];
				if (s.compareTo(low) >= 0 && s.compareTo(high) <= 0)
					return begX;
				begX += incX;
			}
		}
		return -1;
	}

	static public void copy(int n, String[] x, int begX, int incX, String[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, String[] x, int begX, int incX, String scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}


	static public Object[] resize(int n, Object[] array) {
		if (array == null)
			return new Object[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		Object[] newArray = new Object[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public Object[] resize(int n, Object[] array, Object fill) {
		if (array == null) {
			Object[] newArray = new Object[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		Object[] newArray = new Object[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public Object[][] resize(int n, Object[][] array) {
		if (array == null)
			return new Object[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		Object[][] newArray = new Object[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toObject(int n, Object[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		Object v = x[begX];
		Object l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l || v.equals(l)) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, Object[] x, int begX, int incX, Object[] y, int begY, int incY) {
		while (--n >= 0) {
			Object sx = x[begX];
			Object sy = y[begY];
			if (sx != sy && !sx.equals(sy))
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, Object[] x, int begX, int incX, Object key) {
		int cnt = 0;
		while (--n >= 0) {
			Object s = x[begX];
			if (s == key || s.equals(key))
				cnt++;
			begX += incX;
		}
		return cnt;
	}

	static public int find(int n, Object[] x, int begX, int incX, Object key) {
		while (--n >= 0) {
			Object s = x[begX];
			if (s == key || s.equals(key))
				return begX;
			begX += incX;
		}
		return -1;
	}

	static public void copy(int n, Object[] x, int begX, int incX, Object[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, Object[] x, int begX, int incX, Object scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}


	static public boolean[] resize(int n, boolean[] array) {
		if (array == null)
			return new boolean[n];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		boolean[] newArray = new boolean[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public boolean[] resize(int n, boolean[] array, boolean fill) {
		if (array == null) {
			boolean[] newArray = new boolean[n];
			for (int i = 0; i < n; i++)
				newArray[i] = fill;
			return newArray;
		}
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		boolean[] newArray = new boolean[n];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		for (int i = c; i < n; i++)
			newArray[i] = fill;
		return newArray;
	}

	static public boolean[][] resize(int n, boolean[][] array) {
		if (array == null)
			return new boolean[n][];
		if (array.length == n)
			return array;
		int c = Math.min(array.length, n);
		boolean[][] newArray = new boolean[n][];
		for (int i = 0; i < c; i++)
			newArray[i] = array[i];
		return newArray;
	}

	static public String toString(int n, boolean[] x, int begX, int incX) {
		String sep = "\n";
		String s = "Array: [" + begX + "," + (begX + incX) + "..." + begX + (begX + (n - 1) * incX) + "] =" + sep;
		if (n < 1)
			return s;
		boolean v = x[begX];
		boolean l = v;
		boolean ptd = false;
		s += "[" + begX + "] " + v + sep;
		begX += incX;
		while (--n > 0) { 
			v = x[begX];
			if (v == l) {
				if (!ptd) {
					ptd = true;
					s += "..." + sep;
				}
			} else {
				s += "[" + begX + "] " + v + sep;
				l = v;
				ptd = false;
			}
			begX += incX;
		}
		return s;
	}

	static public boolean equals(int n, boolean[] x, int begX, int incX, boolean[] y, int begY, int incY) {
		while (--n >= 0) {
			boolean sx = x[begX];
			boolean sy = y[begY];
			if (sx != sy)
				return false;
			begX += incX;
			begY += incY;
		}
		return true;
	}

	static public int count(int n, boolean[] x, int begX, int incX, boolean key) {
		int cnt = 0;
		while (--n >= 0) {
			boolean s = x[begX];
			if (s == key)
				cnt++;
			begX += incX;
		}
		return cnt;
	}

	static public int find(int n, boolean[] x, int begX, int incX, boolean key) {
		while (--n >= 0) {
			boolean s = x[begX];
			if (s == key)
				return begX;
			begX += incX;
		}
		return -1;
	}

	static public void copy(int n, boolean[] x, int begX, int incX, boolean[] y, int begY, int incY) {
		while (--n >= 0) {
			x[begX] = y[begY];
			begX += incX;
			begY += incY;
		}
	}

	static public void copy(int n, boolean[] x, int begX, int incX, boolean scaler) {
		while (--n >= 0) {
			x[begX] = scaler;
			begX += incX;
		}
	}

}
