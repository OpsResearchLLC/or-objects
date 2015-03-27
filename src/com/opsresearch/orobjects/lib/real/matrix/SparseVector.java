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

package com.opsresearch.orobjects.lib.real.matrix;

import java.util.Iterator;
import java.util.HashMap;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

public class SparseVector extends Vect implements SizableVectorI {
	private static final long serialVersionUID = 1L;

	private int _size = 0;
	private Element _tmpElement = new Element(0, 0);
	private HashMap<VectorElementI, VectorElementI> _index = null;

	@SuppressWarnings("unused")
	private Element[] getElements() {
		Element[] a = new Element[_index.size()];
		return _index.values().toArray(a);
	}

	@SuppressWarnings("unused")
	private void setElements(Element[] elements) {
		_index = new HashMap<VectorElementI, VectorElementI>(elements.length);
		for (Element element : elements)
			setElementAt(element._index, element._value);
	}

	public SparseVector() {
		_index = new HashMap<VectorElementI, VectorElementI>();
	}

	public SparseVector(int size) {
		_size = size;
		_index = new HashMap<VectorElementI, VectorElementI>();
	}

	public SparseVector(int size, int capacityOfElements) {
		_size = size;
		_index = new HashMap<VectorElementI, VectorElementI>(capacityOfElements);
	}

	public SparseVector(double[] array) {
		_size = array.length;
		_index = new HashMap<VectorElementI, VectorElementI>(array.length);
		for (int i = 0; i < array.length; i++) {
			Element e = new Element(i, array[i]);
			_index.put(e, e);
		}
	}

	public SparseVector(double[] array, double epsilon) {
		super(epsilon);
		_size = array.length;
		_index = new HashMap<VectorElementI, VectorElementI>(array.length);
		for (int i = 0; i < array.length; i++) {
			double v = array[i];
			if (equals(v, 0.0))
				continue;
			Element e = new Element(i, v);
			_index.put(e, e);
		}
	}

	public SparseVector(VectorI vector) {
		_size = vector.size();
		_index = new HashMap<VectorElementI, VectorElementI>(_size);
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			Element ne = new Element(elem.getIndex(), elem.getValue());
			_index.put(ne, ne);
		}
	}

	public SparseVector(VectorI vector, double epsilon) {
		_size = vector.size();
		_index = new HashMap<VectorElementI, VectorElementI>(_size);
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			double v = elem.getValue();
			if (equals(v, 0.0))
				continue;
			Element ne = new Element(elem.getIndex(), v);
			_index.put(ne, ne);
		}
	}

	@Override
	public void setSize(int size) {
		if (size < _size) {
			java.util.Vector<VectorElementI> vect = new java.util.Vector<VectorElementI>();
			for (Iterator<VectorElementI> e = _index.values().iterator(); e
					.hasNext();) {
				Element elem = (Element) e.next();
				if (elem._index >= size)
					vect.addElement(elem);
			}
			for (Iterator<VectorElementI> e = vect.iterator(); e.hasNext();) {
				_index.remove(e.next());
			}
		}
		_size = size;
	}

	public void setCapacity(int capacity) {
	}

	@Override
	public void addElement(double value) {
		setElementAt(_size++, value);
	}

	@Override
	public int sizeOfElements() {
		return _index.size();
	}

	@Override
	public int size() {
		return _size;
	}

	@Override
	public int capacity() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void setElements(double value) {
		for (Iterator<VectorElementI> e = elements(); e.hasNext();) {
			e.next().setValue(value);
		}
	}

	@Override
	public void setElementAt(int index, double value) {
		if (index < 0 || index >= _size)
			throw new ArrayIndexOutOfBoundsException("index = " + index
					+ ", size = " + _size);
		_tmpElement._index = index;
		Element e = (Element) _index.get(_tmpElement);
		if (e == null) {
			e = new Element(index, value);
			_index.put(e, e);
		} else {
			e._value = value;
		}
	}

	@Override
	public double[] getArray() {
		double[] array = new double[_size];
		for (Iterator<VectorElementI> e = elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			array[elem.getIndex()] = elem.getValue();
		}
		return array;
	}

	@Override
	public boolean isNull(int index) {
		if (index < 0 || index >= _size)
			throw new ArrayIndexOutOfBoundsException("index = " + index
					+ ", size = " + _size);
		_tmpElement._index = index;
		Element e = (Element) _index.get(_tmpElement);
		return e == null;
	}

	@Override
	public double elementAt(int index) {
		if (index < 0 || index >= _size)
			throw new ArrayIndexOutOfBoundsException("index = " + index
					+ ", size = " + _size);
		_tmpElement._index = index;
		Element e = (Element) _index.get(_tmpElement);
		return (e == null) ? 0 : e._value;
	}

	public Iterator<VectorElementI> elements() {
		return _index.values().iterator();
	}

	@Override
	public double sum() {
		double sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			sum += elem.getValue();
		}
		return sum;
	}

	@Override
	public double sum(int begin) {
		double sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			if (elem.getIndex() < begin)
				continue;
			sum += elem.getValue();
		}
		return sum;
	}

	@Override
	public double sum(int begin, int end) {
		double sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			sum += elem.getValue();
		}
		return sum;
	}

	@Override
	public double sumOfSquares() {
		double d, sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			d = elem.getValue();
			sum += d * d;
		}
		return sum;
	}

	@Override
	public double sumOfSquares(int begin) {
		double d, sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			if (elem.getIndex() < begin)
				continue;
			d = elem.getValue();
			sum += d * d;
		}
		return sum;
	}

	@Override
	public double sumOfSquares(int begin, int end) {
		double d, sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			d = elem.getValue();
			sum += d * d;
		}
		return sum;
	}

	@Override
	public double sumOfSquaredDifferences(double scaler) {
		int cnt = 0;
		double d, sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			d = elem.getValue() - scaler;
			sum += d * d;
			cnt++;
		}
		return sum + (_size - cnt) * scaler * scaler;
	}

	@Override
	public double sumOfSquaredDifferences(int begin, double scaler) {
		int cnt = 0;
		double d, sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			if (elem.getIndex() < begin)
				continue;
			d = elem.getValue() - scaler;
			sum += d * d;
			cnt++;
		}
		return sum + (_size - begin - cnt) * scaler * scaler;
	}

	@Override
	public double sumOfSquaredDifferences(int begin, int end, double scaler) {
		int cnt = 0;
		double d, sum = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			d = elem.getValue() - scaler;
			sum += d * d;
			cnt++;
		}
		return sum + (end - begin - cnt) * scaler * scaler;
	}

	@Override
	public double maximumAbsoluteValue(int begin, int end) {
		double max = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			double abs = Math.abs(elem.getValue());
			if (abs > max)
				max = abs;
		}
		return max;
	}

	@Override
	public double minimumAbsoluteValue(int begin, int end) {
		double min = Double.MAX_VALUE;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			double abs = Math.abs(elem.getValue());
			if (abs < min)
				min = abs;
		}
		return min;
	}

	@Override
	public double maximumValue(int begin, int end) {
		double max = 0;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			double v = elem.getValue();
			if (v > max)
				max = v;
		}
		return max;
	}

	@Override
	public double minimumValue(int begin, int end) {
		double min = Double.MAX_VALUE;
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI elem = e.next();
			int i = elem.getIndex();
			if (i < begin || i >= end)
				continue;
			double v = elem.getValue();
			if (v < min)
				min = v;
		}
		return min;
	}

	@XmlRootElement(name = "sparse-vector-element")
	@XmlAccessorType(XmlAccessType.FIELD)
	static class Element implements VectorElementI, java.io.Serializable {
		private static final long serialVersionUID = 1L;

		@XmlElement(name = "index")
		int _index;

		@XmlElement(name = "value")
		double _value;

		Element() {
		}

		Element(int index, double value) {
			_index = index;
			_value = value;
		}

		public int getIndex() {
			return _index;
		}

		public double getValue() {
			return _value;
		}

		public void setValue(double value) {
			_value = value;
		}

		public int hashCode() {
			return _index;
		}

		public boolean equals(Object o) {
			return _index == ((Element) o)._index;
		}
	}

}
