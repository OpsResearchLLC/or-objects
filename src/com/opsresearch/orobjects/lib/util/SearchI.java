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
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public interface SearchI<T> {
	public void setCompare(CompareI compare);

	public CompareI getCompare();

	public int insertionIndex();

	public int find(Vector<T> vector, T key);

	public int find(Vector<T> vector, T key, int from, int to);

	public int find(VectorI vector, double key);

	public int find(VectorI vector, double key, int from, int to);

	public int find(T[] array, T key);

	public int find(T[] array, T key, int from, int to);

	public int find(int[] array, int key);

	public int find(int[] array, int key, int from, int to);

	public int find(byte[] array, byte key);

	public int find(byte[] array, byte key, int from, int to);

	public int find(short[] array, short key);

	public int find(short[] array, short key, int from, int to);

	public int find(long[] array, long key);

	public int find(long[] array, long key, int from, int to);

	public int find(double[] array, double key);

	public int find(double[] array, double key, int from, int to);

	public int find(float[] array, float key);

	public int find(float[] array, float key, int from, int to);

	public int find(String[] array, String key);

	public int find(String[] array, String key, int from, int to);
}
