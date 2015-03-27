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

package com.opsresearch.orobjects.lib.cont;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.CompareI;
import com.opsresearch.orobjects.lib.InvalidPriorityException;
import com.opsresearch.orobjects.lib.PairI;

public interface PriorityQueueI<priority, value> {
	public int size();

	public void removeAllElements();

	public void ensureCapacity(int capacity);

	public Iterator<PairI<priority, value>> elements();

	public boolean check();

	public void changePriority(PairI<priority, value> element, priority p) throws InvalidPriorityException;

	public PairI<priority, value> getHead();

	public PairI<priority, value> popHead();

	public PairI<priority, value> insert(priority p, value v);

	public void setCompare(CompareI compare);

}
