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

public class DenseMatrix extends RowMajorMatrix {
	private static final long serialVersionUID = 1L;

	public DenseMatrix(int sizeOfRows, int sizeOfColumns) {
		super(sizeOfRows, sizeOfColumns);
	}

	public DenseMatrix(int sizeOfRows, int sizeOfColumns, int capacityOfRows, int capacityOfColumns) {
		super(sizeOfRows, sizeOfColumns, capacityOfRows, capacityOfColumns);
	}

	public DenseMatrix(double[][] array) {
		super(array);
	}

	public DenseMatrix(MatrixI matrix) {
		super(matrix);
	}

	public DenseMatrix(VectorI vector) {
		super(vector);
	}

}
