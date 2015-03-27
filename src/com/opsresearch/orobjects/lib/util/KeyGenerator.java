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

public class KeyGenerator implements Iterator<String>, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private int _size;
	private int[] _i;
	private String[] _charSet;
	private boolean _rolledOver = false;

	public KeyGenerator(int size) {
		this(size, "ABCDEFGHIJKLMNOPQRSTUVWXYZ");
	}

	public KeyGenerator(int size, String characterSet) {
		if (characterSet.length() < 1)
			throw new Error("The KeyGenerator character set is empty.");
		_size = size;
		_i = new int[_size];
		_charSet = new String[_size];
		for (int i = 0; i < _size; i++)
			_charSet[i] = characterSet;
	}

	public KeyGenerator(String[] characterSet) {
		_size = characterSet.length;
		_i = new int[_size];
		_charSet = new String[_size];
		for (int i = 0; i < _size; i++) {
			if (characterSet[i].length() < 1)
				throw new Error("One of the KeyGenerator character sets is empty.");
			_charSet[i] = characterSet[i];
		}
	}

	public boolean hasNext() {
		return !_rolledOver;
	}

	public String next() {
		return nextKey();
	}

	public String nextKey() {
		String key = "";
		for (int i = _size - 1; i >= 0; i--)
			key += _charSet[i].charAt(_i[i]);
		boolean carry = true;
		for (int i = 0; i < _size; i++) {
			if (carry) {
				_i[i]++;
				carry = false;
			}
			if (_i[i] >= _charSet[i].length()) {
				_i[i] = 0;
				carry = true;
			}
		}
		if (carry)
			_rolledOver = true;
		return key;
	}

	@Override
	public void remove() {
	}
}
