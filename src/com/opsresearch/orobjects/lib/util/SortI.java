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

public interface SortI<T> {
	public void setCompare(CompareI compare);

	public CompareI getCompare();

	public void setDescending();

	public void setAscending();

	public void sort(Vector<T> vector);

	public void sort(Vector<T> vector, int from, int to);

	public void sort(T[] array);

	public void sort(T[] array, int from, int to);

	public void sort(VectorI vector);

	public void sort(VectorI vector, int from, int to);

}
