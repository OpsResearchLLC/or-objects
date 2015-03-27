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
import java.util.Vector;

public class IndexedVector<keyType, valueType> implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int _size;
	private Vector<valueType> _values;
	private HashMap<keyType, Integer> _index;

	public IndexedVector() {
		_index = new HashMap<keyType, Integer>();
		_values = new Vector<valueType>();
		_size = 0;
	}

	public IndexedVector(int capacity) {
		_index = new HashMap<keyType, Integer>(capacity);
		_values = new Vector<valueType>(capacity);
		;
		_size = 0;
	}

	public int capacity() {
		return _values.capacity();
	}

	public void removeAllElements() {
		_index.clear();
		_values.removeAllElements();
		_size = 0;
	}

	public void ensureCapacity(int capacity) {
		_values.ensureCapacity(capacity);
	}

	public int size() {
		return _size;
	}

	public int addElement(keyType key, valueType value) throws NullPointerException {
		Integer i = (Integer) _index.get(key);
		if (i != null) {
			_index.put(key, i);
			_values.setElementAt(value, i.intValue());
		} else {
			i = new Integer(_size);
			_index.put(key, i);
			_values.insertElementAt(value, _size);
			_size++;
		}
		return i.intValue();
	}

	public valueType get(keyType key) {
		Integer i = (Integer) _index.get(key);
		if (i == null)
			return null;
		return _values.elementAt(i.intValue());
	}

	public int getElementIndex(Object key) {
		Integer i = (Integer) _index.get(key);
		if (i == null)
			return -1;
		return i.intValue();
	}

	public Object elementAt(int index) throws ArrayIndexOutOfBoundsException {
		if (_values == null)
			return null;
		return _values.elementAt(index);
	}
}
