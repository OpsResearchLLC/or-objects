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

import java.util.Vector;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.LibError;
import com.opsresearch.orobjects.lib.real.matrix.DenseVector;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class QuickSort<T> implements SortI<T>, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private boolean _dec;
	CompareI _cmp;

	public void setCompare(CompareI compare) {
		_cmp = compare;
	}

	public CompareI getCompare() {
		return _cmp;
	}

	public void setDescending() {
		_dec = true;
	}

	public void setAscending() {
		_dec = false;
	}

	public QuickSort() {
		_cmp = null;
		_dec = false;
	}

	public QuickSort(boolean descending) {
		_cmp = null;
		_dec = descending;
	}

	public QuickSort(CompareI compare) {
		_cmp = compare;
		_dec = false;
	}

	public QuickSort(boolean descending, CompareI compare) {
		_cmp = compare;
		_dec = descending;
	}

	public void sort(Vector<T> vector) {
		sort(vector, 0, vector.size() - 1);
	}

	public void sort(Vector<T> vector, int from, int to) {
		if (_cmp == null)
			throw new LibError("Can't compare objects, the compare object has not been set");
		qs(vector, from, to);
	}

	private void qs(Vector<T> vector, int p, int r) {
		if (p < r) {
			int q = prt(vector, p, r);
			qs(vector, p, q);
			qs(vector, q + 1, r);
		}
	}

	private int prt(Vector<T> vector, int p, int r) {
		Object x = vector.elementAt(p);
		int i = p - 1;
		int j = r + 1;
		int jcmp = _dec ? CompareI.LESS : CompareI.GREATER;
		int icmp = _dec ? CompareI.GREATER : CompareI.LESS;
		while (true) {
			T oj, oi;
			while (_cmp.compare((oj = vector.elementAt(--j)), x) == jcmp) {
			}
			while (_cmp.compare((oi = vector.elementAt(++i)), x) == icmp) {
			}
			if (i < j) {
				vector.setElementAt(oj, i);
				vector.setElementAt(oi, j);
			} else
				return j;
		}
	}

	public void sort(Object[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(Object[] array, int from, int to) {
		if (_cmp == null)
			throw new LibError("Can't compare objects, the compare object has not been set");
		qs(array, from, to);
	}

	private void qs(Object[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(Object[] array, int p, int r) {
		Object x = array[p];
		int i = p - 1;
		int j = r + 1;
		int jcmp = _dec ? CompareI.LESS : CompareI.GREATER;
		int icmp = _dec ? CompareI.GREATER : CompareI.LESS;
		while (true) {
			Object oj, oi;
			while (_cmp.compare((oj = array[--j]), x) == jcmp) {
			}
			while (_cmp.compare((oi = array[++i]), x) == icmp) {
			}
			if (i < j) {
				array[i] = oj;
				array[j] = oi;
			} else
				return j;
		}
	}

	public void sort(long[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(long[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(long[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(long[] array, int p, int r) {
		long x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				long oj, oi;
				while ((oj = array[--j]) > x) {
				}
				while ((oi = array[++i]) < x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				long oj, oi;
				while ((oj = array[--j]) < x) {
				}
				while ((oi = array[++i]) > x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

	public void sort(int[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(int[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(int[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(int[] array, int p, int r) {
		int x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				int oj, oi;
				while ((oj = array[--j]) > x) {
				}
				while ((oi = array[++i]) < x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				int oj, oi;
				while ((oj = array[--j]) < x) {
				}
				while ((oi = array[++i]) > x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

	public void sort(double[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(double[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(double[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(double[] array, int p, int r) {
		double x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				double oj, oi;
				while ((oj = array[--j]) > x) {
				}
				while ((oi = array[++i]) < x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				double oj, oi;
				while ((oj = array[--j]) < x) {
				}
				while ((oi = array[++i]) > x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

	public void sort(String[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(String[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(String[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(String[] array, int p, int r) {
		String x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				String oj, oi;
				while ((oj = array[--j]).compareTo(x) > 0) {
				}
				while ((oi = array[++i]).compareTo(x) < 0) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				String oj, oi;
				while ((oj = array[--j]).compareTo(x) < 0) {
				}
				while ((oi = array[++i]).compareTo(x) > 0) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

	public void sort(VectorI vector) {
		if (!(vector instanceof DenseVector))
			throw new Error("Only implemented for 'DenseVector'");
		qs(((DenseVector) vector).getValueArray(), 0, vector.size() - 1);
	}

	public void sort(VectorI vector, int from, int to) {
		if (!(vector instanceof DenseVector))
			throw new Error("Only implemented for 'DenseVector'");
		qs(((DenseVector) vector).getValueArray(), from, to);
	}

	public void sort(short[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(short[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(short[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(short[] array, int p, int r) {
		short x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				short oj, oi;
				while ((oj = array[--j]) > x) {
				}
				while ((oi = array[++i]) < x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				short oj, oi;
				while ((oj = array[--j]) < x) {
				}
				while ((oi = array[++i]) > x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

	public void sort(byte[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(byte[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(byte[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(byte[] array, int p, int r) {
		byte x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				byte oj, oi;
				while ((oj = array[--j]) > x) {
				}
				while ((oi = array[++i]) < x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				byte oj, oi;
				while ((oj = array[--j]) < x) {
				}
				while ((oi = array[++i]) > x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

	public void sort(float[] array) {
		sort(array, 0, array.length - 1);
	}

	public void sort(float[] array, int from, int to) {
		qs(array, from, to);
	}

	private void qs(float[] array, int p, int r) {
		if (p < r) {
			int q = prt(array, p, r);
			qs(array, p, q);
			qs(array, q + 1, r);
		}
	}

	private int prt(float[] array, int p, int r) {
		float x = array[p];
		int i = p - 1;
		int j = r + 1;
		if (!_dec) {
			while (true) {
				float oj, oi;
				while ((oj = array[--j]) > x) {
				}
				while ((oi = array[++i]) < x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		} else {
			while (true) {
				float oj, oi;
				while ((oj = array[--j]) < x) {
				}
				while ((oi = array[++i]) > x) {
				}
				if (i < j) {
					array[i] = oj;
					array[j] = oi;
				} else
					return j;
			}
		}
	}

}
