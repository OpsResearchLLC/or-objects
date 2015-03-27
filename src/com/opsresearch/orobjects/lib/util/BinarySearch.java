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

public class BinarySearch<T> implements SearchI<T>, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int _ins;
	private boolean _dec;
	private CompareI _cmp;

	public BinarySearch() {
		_ins = 0;
		_dec = false;
		_cmp = null;
	}

	public BinarySearch(boolean descending) {
		_ins = 0;
		_dec = descending;
		_cmp = null;
	}

	public BinarySearch(CompareI compare) {
		_ins = 0;
		_dec = false;
		_cmp = compare;
	}

	public BinarySearch(boolean descending, CompareI compare) {
		_ins = 0;
		_dec = descending;
		_cmp = compare;
	}

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

	public int insertionIndex() {
		return _ins;
	}

	public int find(T[] array, T key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(T[] array, T key, int from, int to) {
		if (_cmp == null)
			throw new LibError("The compare object must be set to find Objects");
		_ins = to + 1;
		if (_cmp.compare(array[to], key) == CompareI.EQUAL)
			return to;
		if (_cmp.compare(array[from], key) == CompareI.EQUAL)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (_cmp.compare(key, array[from]) == CompareI.LESS) {
				_ins = from;
				return -1;
			}
			if (_cmp.compare(key, array[to]) == CompareI.GREATER) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (_cmp.compare(array[m], key) == CompareI.EQUAL)
					return m;
				else if (_cmp.compare(array[m], key) == CompareI.LESS)
					f = m;
				else
					t = m;
			}
		} else {
			if (_cmp.compare(key, array[from]) == CompareI.GREATER) {
				_ins = from;
				return -1;
			}
			if (_cmp.compare(key, array[to]) == CompareI.LESS) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (_cmp.compare(array[m], key) == CompareI.EQUAL)
					return m;
				else if (_cmp.compare(array[m], key) == CompareI.GREATER)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(Vector<T> vector, T key) {
		return find(vector, key, 0, vector.size() - 1);
	}

	public int find(Vector<T> vector, T key, int from, int to) {
		if (_cmp == null)
			throw new LibError("The compare object must be set to find Objects");
		_ins = to + 1;
		if (_cmp.compare(vector.elementAt(to), key) == CompareI.EQUAL)
			return to;
		if (_cmp.compare(vector.elementAt(from), key) == CompareI.EQUAL)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (_cmp.compare(key, vector.elementAt(from)) == CompareI.LESS) {
				_ins = from;
				return -1;
			}
			if (_cmp.compare(key, vector.elementAt(to)) == CompareI.GREATER) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (_cmp.compare(vector.elementAt(m), key) == CompareI.EQUAL)
					return m;
				else if (_cmp.compare(vector.elementAt(m), key) == CompareI.LESS)
					f = m;
				else
					t = m;
			}
		} else {
			if (_cmp.compare(key, vector.elementAt(from)) == CompareI.GREATER) {
				_ins = from;
				return -1;
			}
			if (_cmp.compare(key, vector.elementAt(to)) == CompareI.LESS) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (_cmp.compare(vector.elementAt(m), key) == CompareI.EQUAL)
					return m;
				else if (_cmp.compare(vector.elementAt(m), key) == CompareI.GREATER)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(int[] array, int key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(int[] array, int key, int from, int to) {
		_ins = to + 1;
		if (array[to] == key)
			return to;
		if (array[from] == key)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key < array[from]) {
				_ins = from;
				return -1;
			}
			if (key > array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] < key)
					f = m;
				else
					t = m;
			}
		} else {
			if (key > array[from]) {
				_ins = from;
				return -1;
			}
			if (key < array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] > key)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(short[] array, short key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(short[] array, short key, int from, int to) {
		_ins = to + 1;
		if (array[to] == key)
			return to;
		if (array[from] == key)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key < array[from]) {
				_ins = from;
				return -1;
			}
			if (key > array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] < key)
					f = m;
				else
					t = m;
			}
		} else {
			if (key > array[from]) {
				_ins = from;
				return -1;
			}
			if (key < array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] > key)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(byte[] array, byte key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(byte[] array, byte key, int from, int to) {
		_ins = to + 1;
		if (array[to] == key)
			return to;
		if (array[from] == key)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key < array[from]) {
				_ins = from;
				return -1;
			}
			if (key > array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] < key)
					f = m;
				else
					t = m;
			}
		} else {
			if (key > array[from]) {
				_ins = from;
				return -1;
			}
			if (key < array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] > key)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(double[] array, double key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(double[] array, double key, int from, int to) {
		_ins = to + 1;
		if (array[to] == key)
			return to;
		if (array[from] == key)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key < array[from]) {
				_ins = from;
				return -1;
			}
			if (key > array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] < key)
					f = m;
				else
					t = m;
			}
		} else {
			if (key > array[from]) {
				_ins = from;
				return -1;
			}
			if (key < array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] > key)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(float[] array, float key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(float[] array, float key, int from, int to) {
		_ins = to + 1;
		if (array[to] == key)
			return to;
		if (array[from] == key)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key < array[from]) {
				_ins = from;
				return -1;
			}
			if (key > array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] < key)
					f = m;
				else
					t = m;
			}
		} else {
			if (key > array[from]) {
				_ins = from;
				return -1;
			}
			if (key < array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] > key)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(long[] array, long key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(long[] array, long key, int from, int to) {
		_ins = to + 1;
		if (array[to] == key)
			return to;
		if (array[from] == key)
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key < array[from]) {
				_ins = from;
				return -1;
			}
			if (key > array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] < key)
					f = m;
				else
					t = m;
			}
		} else {
			if (key > array[from]) {
				_ins = from;
				return -1;
			}
			if (key < array[to]) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m] == key)
					return m;
				else if (array[m] > key)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(String[] array, String key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(String[] array, String key, int from, int to) {
		_ins = to + 1;
		if (array[to].equals(key))
			return to;
		if (array[from].equals(key))
			return from;

		int t = to;
		int f = from;
		if (!_dec) {
			if (key.compareTo(array[from]) < 0) {
				_ins = from;
				return -1;
			}
			if (key.compareTo(array[to]) > 0) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m].equals(key))
					return m;
				else if (array[m].compareTo(key) < 0)
					f = m;
				else
					t = m;
			}
		} else {
			if (key.compareTo(array[from]) > 0) {
				_ins = from;
				return -1;
			}
			if (key.compareTo(array[to]) < 0) {
				return -1;
			}
			while (t > f + 1) {
				int m = (f + t) / 2;
				if (array[m].equals(key))
					return m;
				else if (array[m].compareTo(key) > 0)
					f = m;
				else
					t = m;
			}
		}
		_ins = t;
		return -1;
	}

	public int find(VectorI vector, double key) {
		if (!(vector instanceof DenseVector))
			throw new Error("Only implemented for 'DenseVector'.");
		return find(((DenseVector) vector).getValueArray(), key, 0, vector.size() - 1);
	}

	public int find(VectorI vector, double key, int from, int to) {
		if (!(vector instanceof DenseVector))
			throw new Error("Only implemented for 'DenseVector'.");
		return find(((DenseVector) vector).getValueArray(), key, from, to);
	}
}
