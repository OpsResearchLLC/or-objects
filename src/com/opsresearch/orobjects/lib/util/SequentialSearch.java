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

import java.util.Iterator;
import java.util.Vector;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.real.matrix.VectorElementI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class SequentialSearch<T> implements SearchI<T>, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int _ins;
	private CompareI _cmp;

	public SequentialSearch() {
		_ins = 0;
		_cmp = null;
	}

	public SequentialSearch(CompareI compare) {
		_ins = 0;
		_cmp = compare;
	}

	public void setCompare(CompareI compare) {
		_cmp = compare;
	}

	public CompareI getCompare() {
		return _cmp;
	}

	public int insertionIndex() {
		return _ins;
	}

	public int find(Object[] array, Object key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(Object[] array, Object key, int from, int to) {
		if (_cmp == null) {
			for (int i = from; i <= to; i++)
				if (array[i].equals(key))
					return i;
		} else {
			for (int i = from; i <= to; i++)
				if (_cmp.compare(array[i], key) == CompareI.EQUAL)
					return i;
		}
		_ins = to + 1;
		return -1;
	}

	public int find(Vector<T> vector, T key) {
		return find(vector, key, 0, vector.size() - 1);
	}

	public int find(Vector<T> vector, T key, int from, int to) {
		if (_cmp == null) {
			for (int i = from; i <= to; i++)
				if (vector.elementAt(i).equals(key))
					return i;
		} else {
			for (int i = from; i <= to; i++)
				if (_cmp.compare(vector.elementAt(i), key) == CompareI.EQUAL)
					return i;
		}
		_ins = to + 1;
		return -1;
	}

	public int find(int[] array, int key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(int[] array, int key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i] == key)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(short[] array, short key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(short[] array, short key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i] == key)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(byte[] array, byte key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(byte[] array, byte key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i] == key)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(double[] array, double key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(double[] array, double key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i] == key)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(float[] array, float key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(float[] array, float key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i] == key)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(long[] array, long key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(long[] array, long key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i] == key)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(String[] array, String key) {
		return find(array, key, 0, array.length - 1);
	}

	public int find(String[] array, String key, int from, int to) {
		for (int i = from; i <= to; i++)
			if (array[i].compareTo(key) == 0)
				return i;
		_ins = to + 1;
		return -1;
	}

	public int find(VectorI vector, double key) {
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
			VectorElementI elem = (VectorElementI) e.next();
			if (elem.getValue() == key)
				return elem.getIndex();
		}
		return -1;
	}

	public int find(VectorI vector, double key, int from, int to) {
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
			VectorElementI elem = (VectorElementI) e.next();
			int i = elem.getIndex();
			if (i < from || i > to)
				continue;
			if (elem.getValue() == key)
				return i;
		}
		return -1;
	}
}
