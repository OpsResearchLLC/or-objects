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

package com.opsresearch.orobjects.lib.complex.matrix;

import java.util.Iterator;

import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixError;
import com.opsresearch.orobjects.lib.util.Array;
import com.opsresearch.orobjects.lib.util.ComplexArray;

public class ArrayVector extends Vect implements ArrayVectorI, SizableVectorI {
	private static final long serialVersionUID = 1L;
	private int _size;
	double[] _values;

	public ArrayVector() {
		_size = 0;
		_values = new double[20];
	}

	public ArrayVector(int size) {
		_size = size;
		_values = new double[Math.max(20, 2 * _size)];
	}

	public ArrayVector(int size, ComplexI fill) {
		_size = size;
		_values = new double[Math.max(20, 2 * _size)];
		ComplexArray.copy(_size, _values, 0, 1, fill);
	}

	public ArrayVector(int size, int capacity) {
		_size = size;
		_values = new double[Math.max(2 * size, 2 * capacity)];
	}

	public ArrayVector(double[] array) {
		this(array, false);
	}

	public ArrayVector(double[] array, boolean useArrayInternally) {
		_size = array.length / 2;
		if (useArrayInternally) {
			_values = array;
		} else {
			_values = new double[array.length];
			ComplexArray.copy(_size, _values, 0, 1, array, 0, 1);
		}
	}

	public ArrayVector(double[] real, double[] imag) {
		if (real == null && imag == null)
			throw new MatrixError("Both arrays can't be null.");
		if (real != null && imag != null && real.length != imag.length)
			throw new MatrixError("The real and imag arrays must be the same length: " + real.length + ", "
					+ imag.length);
		_size = real != null ? real.length : imag.length;
		_values = new double[2 * _size];
		if (real != null)
			Array.copy(_size, _values, 0, 2, real, 0, 1);
		if (imag != null)
			Array.copy(_size, _values, 1, 2, imag, 0, 1);
	}

	public ArrayVector(VectorI vector) {
		_size = vector.size();
		_values = new double[2 * vector.size()];
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			int i = 2 * elem.getIndex();
			ComplexI value = elem.getValue();
			_values[i] = value.getReal();
			_values[i + 1] = value.getImag();
		}
	}

	public void setSize(int size) {
		if (size > _values.length)
			ComplexArray.resize(size, _values);
		_size = size;
	}

	public void setCapacity(int capacity) {
		if (capacity > _values.length)
			_values = ComplexArray.resize(capacity, _values);
	}

	public boolean isNull(int index) {
		return false;
	}

	public double[] getArray() {
		double[] array = new double[2 * _size];
		ComplexArray.copy(_size, array, 0, 1, _values, 0, 1);
		return array;
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

	public double[] getValueArray() {
		return _values;
	}

	public ArrayVectorI subvector(int begin) {
		if (begin >= _size)
			throw new MatrixError("Subvector 'begin' is past last element");
		return new Sub(_values, _size - begin, begin, 1);
	}

	public ArrayVectorI subvector(int begin, int end) {
		if (begin > end)
			throw new MatrixError("Subvector 'begin' greater than 'end'");
		if (end > _size)
			throw new MatrixError("Subvector 'end' is past last element");
		return new Sub(_values, end - begin, begin, 1);
	}

	public int sizeOfElements() {
		return _size;
	}

	public int size() {
		return _size;
	}

	public int capacity() {
		return _values.length / 2;
	}

	public void addElement(ComplexI value) {
		if (_size == _values.length / 2)
			_values = ComplexArray.resize(2 * _size, _values);
		int i = 2 * _size++;
		_values[i] = value.getReal();
		_values[i + 1] = value.getImag();
	}

	public void setElements(ComplexI value) {
		ComplexArray.copy(_values.length / 2, _values, 0, 1, value);
	}

	public void setElementAt(int index, ComplexI value) {
		if (index >= _size)
			throw new ArrayIndexOutOfBoundsException();
		index *= 2;
		_values[index] = value.getReal();
		_values[index + 1] = value.getImag();
	}

	public Complex elementAt(int index, Complex results) {
		if (index >= _size)
			throw new ArrayIndexOutOfBoundsException();
		index *= 2;
		if (results == null)
			return new Complex(_values[index], _values[index + 1]);
		results.real = _values[index];
		results.imag = _values[index + 1];
		return results;
	}

	public Complex elementAt(int index) {
		if (index >= _size)
			throw new ArrayIndexOutOfBoundsException();
		index *= 2;
		return new Complex(_values[index], _values[index + 1]);
	}

	public Iterator<VectorElementI> elements() {
		return new Enum(_values, _size, 0, 1);
	}

	public Complex sum(int begin, int end) {
		return ComplexArray.sum(end - begin, _values, begin, 1, new Complex());
	}

	public Complex sumOfSquares(int begin, int end) {
		return ComplexArray.sumOfSquares(end - begin, _values, begin, 1, new Complex());
	}

	public Complex sumOfSquaredDifferences(int begin, int end, ComplexI scaler) {
		return ComplexArray.sumOfSquaredDifferences(end - begin, _values, begin, 1, scaler, new Complex());
	}

	static class Enum implements ComplexI, Iterator<VectorElementI>, VectorElementI {
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
			_moff = 2 * _beg;
			_mreal = _values[_moff];
			_mimag = _values[_moff + 1];
			_beg += _inc;
			return this;
		}

		private int _mindex, _moff;
		private double _mreal;
		private double _mimag;

		public double getReal() {
			return _mreal;
		}

		public double getImag() {
			return _mimag;
		}

		public int getIndex() {
			return _mindex;
		}

		public ComplexI getValue() {
			return this;
		}

		public void setValue(ComplexI value) {
			_values[_moff] = _mreal = value.getReal();
			_values[_moff + 1] = _mimag = value.getImag();
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

		public void setElements(ComplexI value) {
			ComplexArray.copy(_siz, _values, _beg, _inc, value);
		}

		public void setElementAt(int index, ComplexI value) {
			if (index >= _siz)
				throw new ArrayIndexOutOfBoundsException();
			int i = 2 * (_beg + index * _inc);
			_values[i] = value.getReal();
			_values[i + 1] = value.getImag();
			;
		}

		public Complex elementAt(int index) {
			if (index >= _siz)
				throw new ArrayIndexOutOfBoundsException();
			int i = 2 * (_beg + _inc * index);
			return new Complex(_values[i], _values[i + 1]);
		}

		public Complex elementAt(int index, Complex results) {
			if (index >= _siz)
				throw new ArrayIndexOutOfBoundsException();
			int i = 2 * (_beg + _inc * index);
			results.real = _values[i];
			results.imag = _values[i + 1];
			return results;
		}

		public Iterator<VectorElementI> elements() {
			return new Enum(_values, _siz, _beg, _inc);
		}

		public Complex sum(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return ComplexArray.sum(_siz, _values, _beg, _inc, new Complex());
		}

		public Complex sumOfSquares(int begin, int end) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return ComplexArray.sumOfSquares(_siz, _values, _beg, _inc, new Complex());
		}

		public Complex sumOfSquaredDifferences(int begin, int end, ComplexI scaler) {
			if (begin > end)
				throw new MatrixError("Subvector 'begin' greater than 'end'");
			if (end > _siz)
				throw new MatrixError("Subvector 'end' is past last element");
			return ComplexArray.sumOfSquaredDifferences(_siz, _values, _beg, _inc, scaler, new Complex());
		}

	}

}
