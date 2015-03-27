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

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import com.opsresearch.orobjects.lib.util.Array;

public abstract class CompressedMatrix extends Matrix implements SizableMatrixI {
	private static final long serialVersionUID = 1L;

	@XmlElement(name = "sizeOfRows")
	int _sizeOfRows = 0;
	@XmlElement(name = "fillInSpace")
	int _fillInSpace = 0;
	@XmlElement(name = "sizeOfColumns")
	int _sizeOfColumns = 0;
	@XmlElement(name = "sizeOfElements")
	int _sizeOfElements = 0;
	@XmlElementWrapper(name = "values")
	@XmlElement(name = "value")
	double[] _values;

	public CompressedMatrix() {
	}

	public CompressedMatrix(double epsilon) {
		super(epsilon);
	}

	public CompressedMatrix(double[][] array, double epsilon) {
		super(epsilon);
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row == null)
				continue;
			for (int j = 0; j < row.length; j++) {
				double value = row[j];
				if (!equals(0.0, value))
					_sizeOfElements++;
			}
			if (row.length > _sizeOfColumns)
				_sizeOfColumns = row.length;
		}
	}

	public CompressedMatrix(double[][] array) {
		for (int i = 0; i < array.length; i++) {
			double[] row = array[i];
			if (row == null)
				continue;
			_sizeOfElements += row.length;
			if (row.length > _sizeOfColumns)
				_sizeOfColumns = row.length;
		}
	}

	public void setCapacityOfElements(int capacityOfElements) {
		if (_values.length < capacityOfElements)
			Array.resize(capacityOfElements, _values);
	}

	public void setElements(double value) {
		Array.copy(_values.length, _values, 0, 1, value);
	}

	public double[] getValueArray() {
		return _values;
	}

	public int sizeOfRows() {
		return _sizeOfRows;
	}

	public int sizeOfColumns() {
		return _sizeOfColumns;
	}

}
