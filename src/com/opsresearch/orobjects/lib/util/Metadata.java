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
import java.util.HashMap;

import com.opsresearch.orobjects.lib.LibError;

public class Metadata implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private HashMap<String, String> _index = new HashMap<String, String>();

	public Metadata() {
	}

	public Metadata(String[] keys, String[] values) {
		put(keys, values);
	}

	public boolean isDefined(String key) {
		return _index.get(key) != null;
	}

	public Iterator<String> keys() {
		return _index.keySet().iterator();
	}

	public Iterator<String> values() {
		return _index.values().iterator();
	}

	public void remove(String key) {
		_index.remove(key);
	}

	public void remove(String[] keys) {
		for (int i = 0; i < keys.length; i++)
			if (keys[i] != null)
				_index.remove(keys[i]);
	}

	public void put(Metadata metadata) {
		for (Iterator<String> e = metadata.keys(); e.hasNext();) {
			String key = (String) e.next();
			String value = (String) _index.get(key);
			_index.put(key, value);
		}
	}


	public void put(String key, String value) {
		if (value == null)
			_index.remove(key);
		else
			_index.put(key, value);
	}

	public String get(String key, String defaultValue) {
		String vs = (String) _index.get(key);
		if (vs == null)
			return defaultValue;
		return vs;
	}

	public void put(String[] keys, String[] values) {
		int n = Math.min(keys.length, values.length);
		for (int i = 0; i < n; i++) {
			if (keys[i] == null)
				continue;
			else if (values[i] == null)
				_index.remove(keys[i]);
			else
				_index.put(keys[i], values[i]);
		}
	}

	public String[] get(String[] keys, String[] defaultValues) {
		String[] ret = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			String value = (String) _index.get(keys[i]);
			if (value != null)
				ret[i] = value;
			else if (value == null && defaultValues != null && i < defaultValues.length)
				ret[i] = defaultValues[i];
		}
		return ret;
	}


	public int get(String name, int defaultValue) {
		String value = (String) _index.get(name);
		if (value == null)
			return defaultValue;
		return new Integer(value).intValue();
	}

	public int get(String name, int defaultValue, int min) {
		int v = defaultValue;
		String value = (String) _index.get(name);
		if (value != null)
			v = new Integer(value).intValue();
		if (v < min)
			throw new LibError("Metadatum '" + name + "' = " + v + " is less than " + min);
		return v;
	}

	public int get(String name, int defaultValue, int min, int max) {
		int v = defaultValue;
		String value = (String) _index.get(name);
		if (value != null)
			v = new Integer(value).intValue();
		if (v < min)
			throw new LibError("Metadatum '" + name + "' = " + v + " is less than " + min);
		if (v > max)
			throw new LibError("Metadatum '" + name + "' = " + v + " is greater than " + max);
		return v;
	}


	public double get(String name, double defaultValue) {
		String value = (String) _index.get(name);
		if (value == null)
			return defaultValue;
		return new Double(value).doubleValue();
	}

	public double get(String name, double defaultValue, double min, double max) {
		double v = defaultValue;
		String value = (String) _index.get(name);
		if (value != null)
			v = new Double(value).doubleValue();
		if (v < min)
			throw new LibError("Metadatum '" + name + "' = " + v + " is less than " + min);
		if (v > max)
			throw new LibError("Metadatum '" + name + "' = " + v + " is greater than " + max);
		return v;
	}

	public double get(String name, double defaultValue, double min) {
		double v = defaultValue;
		String value = (String) _index.get(name);
		if (value != null)
			v = new Double(value).doubleValue();
		if (v < min)
			throw new LibError("Metadatum '" + name + "' = " + v + " is less than " + min);
		return v;
	}


	public boolean get(String name, boolean defaultValue) {
		String value = (String) _index.get(name);
		if (value == null)
			return defaultValue;
		return new Boolean(value).booleanValue();
	}

	public String toString() {
		String nl = "\n";
		String s = "--- Metadata ---" + nl;
		for (Iterator<String> e = _index.keySet().iterator(); e.hasNext();) {
			String key = (String) e.next();
			String value = (String) _index.get(key);
			s += "'" + key + "' :: '" + value + "'" + nl;
		}
		return s;
	}

}
