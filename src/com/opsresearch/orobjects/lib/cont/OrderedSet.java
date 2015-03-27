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

import java.util.HashMap;
import java.util.Iterator;

public class OrderedSet<valueType> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int _size;
	private HashMap<valueType, Integer> _index;

	public OrderedSet() {
		_index = new HashMap<valueType, Integer>();
		_size = 0;
	}

	public OrderedSet(int capacity) {
		_index = new HashMap<valueType, Integer>(capacity);
		_size = 0;
	}

	public void removeAllElements() {
		_index.clear();
		_size = 0;
	}

	public int size() {
		return _size;
	}

	public Iterator<valueType> elements() {
		return _index.keySet().iterator();
	}

	public Iterator<Integer> indices() {
		return _index.values().iterator();
	}

	public void removeElement(valueType element) throws NullPointerException {
		Integer i = _index.get(element);
		if (i != null) {
			int idx = i.intValue();
			_index.remove(element);
			for (Iterator<valueType> e = _index.keySet().iterator(); e
					.hasNext(); ) {
				valueType key = e.next();
				Integer index = _index.get(key);
				if (index.intValue() > idx)
					_index.put(key, new Integer(index.intValue() - 1));
			}
		}
	}

	public int addElement(valueType element) throws NullPointerException {
		Integer i = (Integer) _index.get(element);
		if (i != null) {
			_index.put(element, i);
		} else {
			i = new Integer(_size);
			_index.put(element, i);
			_size++;
		}
		return i.intValue();
	}

	public int getIndex(Object element) {
		Integer i = (Integer) _index.get(element);
		if (i == null)
			return -1;
		return i.intValue();
	}
}
