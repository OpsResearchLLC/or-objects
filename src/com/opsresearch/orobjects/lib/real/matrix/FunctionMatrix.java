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

public abstract class FunctionMatrix extends Matrix implements MatrixI {
	private static final long serialVersionUID = 1L;
	@XmlElement(name = "sizeOfRows")
	protected int _sizeOfRows = 0;
	@XmlElement(name = "sizeOfColumns")
	protected int _sizeOfColumns = 0;

	public void setElements(double value) {
		throw new Error("Can't set the elements of a 'FunctionMAtrix'.");
	}

	public int sizeOfRows() {
		return _sizeOfRows;
	}

	public int sizeOfColumns() {
		return _sizeOfColumns;
	}

	public boolean isNull(int row, int column) {
		return false;
	}

	public void setElementAt(int row, int column, double value) {
		throw new Error("Can't set an element in a 'FunctionMatrix'");
	}

	public int sizeOfElements() {
		int size = _sizeOfRows * _sizeOfColumns;
		return size;
	}

	public boolean isRowMajor() {
		return false;
	}

	public boolean isColumnMajor() {
		return false;
	}

	public double elementAt(int row, int column) {
		if (row < 0 || row >= _sizeOfRows)
			throw new IndexOutOfBoundsException("Row=" + row + ", sizeOfRows="
					+ _sizeOfRows);
		if (column < 0 || column >= _sizeOfColumns)
			throw new IndexOutOfBoundsException("Column=" + column
					+ ", sizeOfColumns=" + _sizeOfColumns);
		return functionElementAt(row, column);
	}

	public abstract double functionElementAt(int row, int column);

}
