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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.opsresearch.orobjects.lib.util.Array;

public class ArrayVector extends Vect implements ArrayVectorI, SizableVectorI {
	private static final long serialVersionUID = 1L;
	private int _size;

	double[] _array;

	@XmlElement(name = "value")
	@XmlElementWrapper(name = "values")
	public double[] getValues() {
		double[] array = new double[_size];
		Array.copy(_size, array, 0, 1, _array, 0, 1);
		return array;
	}

	public void setValues(double[] array) {
		_array = array;
		_size = array.length;
	}

	public ArrayVector() {
		_size = 0;
		_array = new double[10];
	}

	public ArrayVector(int size) {
		_size = size;
		_array = new double[Math.max(10, _size)];
	}

	public ArrayVector(int size, double fill) {
		_size = size;
		_array = new double[Math.max(10, _size)];
		Array.copy(_size, _array, 0, 1, fill);
	}

	public ArrayVector(int size, int capacity) {
		_size = size;
		_array = new double[Math.max(size, capacity)];
	}

	public ArrayVector(double[] array) {
		this(array, false);
	}

	public ArrayVector(double[] array, boolean useArrayInternally) {
		_size = array.length;
		if (useArrayInternally) {
			_array = array;
		} else {
			_array = new double[array.length];
			Array.copy(_size, _array, 0, 1, array, 0, 1);
		}
	}

	public ArrayVector(VectorI vector) {
		_size = vector.size();
		_array = new double[vector.size()];
		for (Iterator<VectorElementI> e = vector.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			_array[elem.getIndex()] = elem.getValue();
		}
	}

	public void setSize(int size) {
		if (size > _array.length)
			Array.resize(size, _array);
		_size = size;
	}

	public void setCapacity(int capacity) {
		if (capacity > _array.length)
			_array = Array.resize(capacity, _array);
	}

	public boolean isNull(int index) {
		return false;
	}

	public double[] getArray() {
		double[] array = new double[_size];
		Array.copy(_size, array, 0, 1, _array, 0, 1);
		return array;
	}

	public ArrayVectorI subvector(int begin) {
		if (begin >= _size)
			throw new MatrixError("Subvector 'begin' is past last element");
		return new Sub(_array, _size - begin, begin, 1);
	}

	public ArrayVectorI subvector(int begin, int end) {
		if (begin > end)
			throw new MatrixError("Subvector 'begin' greater than 'end'");
		if (end > _size)
			throw new MatrixError("Subvector 'end' is past last element");
		return new Sub(_array, end - begin, begin, 1);
	}

	public double[] getValueArray() {
		return _array;
	}

	public int getOffset(int index) {
		return index;
	}

	public int getBegin() {
		return 0;
	}

	public int getIncrement() {
		return 1;
	}

	public int sizeOfElements() {
		return _size;
	}

	public int size() {
		return _size;
	}

	public int capacity() {
		return _array.length;
	}

	public void addElement(double value) {
		if (_size == _array.length)
			_array = Array.resize(2 * _size, _array);
		_array[_size++] = value;
	}

	public void setElements(double value) {
		Array.copy(_array.length, _array, 0, 1, value);
	}

	public void setElementAt(int index, double value) {
		if (index >= _size)
			throw new ArrayIndexOutOfBoundsException();
		_array[index] = value;
	}

	public double elementAt(int index) {
		if (index >= _size)
			throw new ArrayIndexOutOfBoundsException();
		return _array[index];
	}

	public Iterator<VectorElementI> elements() {
		return new Enum(_array, _size, 0, 1);
	}

	public double sum(int begin, int end) {
		return Array.sum(end - begin, _array, begin, 1);
	}

	public double sumOfSquares(int begin, int end) {
		return Array.sumOfSquares(end - begin, _array, begin, 1);
	}

	public double sumOfSquaredDifferences(int begin, int end, double scaler) {
		return Array.sumOfSquaredDifferences(end - begin, _array, begin, 1,
				scaler);
	}

	public double maximumAbsoluteValue(int begin, int end) {
		return Array.maximumAbsoluteValue(end - begin, _array, begin, 1);
	}

	public double minimumAbsoluteValue(int begin, int end) {
		return Array.minimumAbsoluteValue(end - begin, _array, begin, 1);
	}

	@Override
	public double maximumValue(int begin, int end) {
		return Array.maximumValue(end - begin, _array, begin, 1);
	}

	@Override
	public double minimumValue(int begin, int end) {
		return Array.minimumValue(end - begin, _array, begin, 1);
	}

	static class Enum implements Iterator<VectorElementI>, VectorElementI {
		private int _siz;
		private int _beg;
		private int _inc;
		private int _idx;
		private double[] _values;

		Enum(double[] values, int siz, int beg, int inc) {
			_idx = 0;
			_siz = siz;
			_beg = beg;
			_inc = inc;
			_values = values;
		}

		public boolean hasNext() {
			return _idx < _siz;
		}

		public VectorElementI next() {
			if (_idx >= _siz)
				return null;
			_mindex = _idx++;
			_mvalue = _values[_moff = _beg];
			_beg += _inc;
			return this;
		}

		private int _mindex, _moff;
		private double _mvalue;

		public int getIndex() {
			return _mindex;
		}

		public double getValue() {
			return _mvalue;
		}

		public void setValue(double value) {
			_values[_moff] = _mvalue = value;
		}

		@Override
		public void remove() {
		}
	}

	static class Sub extends Vect implements ArrayVectorI {
		private static final long serialVersionUID = 1L;
		private int _siz, _beg, _inc;
		private double[] _values;

		public Sub(double[] values, int siz, int beg, int inc) {
			_siz = siz;
			_beg = beg;
			_inc = inc;
			_values = values;
		}

		public ArrayVectorI subvector(int begin) {
			if (begin >= _siz)
				throw new MatrixError("Subvector 'begin' is past last element");
			return new Sub(_values, _siz - begin, _beg + _inc * begin, _inc);
		}

		public ArrayVectorI subvector(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return new Sub(_values, end - begin, _beg + _inc * begin, _inc);
		}

		public double[] getValueArray() {
			return _values;
		}

		public int getOffset(int index) {
			return _beg + _inc * index;
		};

		public int getBegin() {
			return _beg;
		}

		public int getIncrement() {
			return _inc;
		}

		public boolean isNull(int index) {
			return false;
		}

		public double[] getArray() {
			double[] array = new double[_siz];
			Array.copy(_siz, array, 0, 1, _values, _beg, _inc);
			return array;
		}

		public int sizeOfElements() {
			return _siz;
		}

		public int size() {
			return _siz;
		}

		public void setElements(double value) {
			Array.copy(_siz, _values, _beg, _inc, value);
		}

		public void setElementAt(int index, double value) {
			if (index >= _siz)
				throw new ArrayIndexOutOfBoundsException();
			_values[_beg + index * _inc] = value;
		}

		public double elementAt(int index) {
			if (index >= _siz)
				throw new ArrayIndexOutOfBoundsException();
			return _values[_beg + _inc * index];
		}

		public Iterator<VectorElementI> elements() {
			return new Enum(_values, _siz, _beg, _inc);
		}

		public double sum(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.sum(_siz, _values, _beg, _inc);
		}

		public double sumOfSquares(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.sumOfSquares(_siz, _values, _beg, _inc);
		}

		public double sumOfSquaredDifferences(int begin, int end, double scaler) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.sumOfSquaredDifferences(_siz, _values, _beg, _inc,
					scaler);
		}

		public double maximumAbsoluteValue(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.maximumAbsoluteValue(_siz, _values, _beg, _inc);
		}

		@Override
		public double maximumValue(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.maximumValue(_siz, _values, _beg, _inc);
		}

		@Override
		public double minimumAbsoluteValue(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.minimumAbsoluteValue(_siz, _values, _beg, _inc);
		}

		@Override
		public double minimumValue(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return Array.minimumValue(_siz, _values, _beg, _inc);
		}

	}

}
