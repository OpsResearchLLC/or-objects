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


package com.opsresearch.orobjects.lib.algebra;

import com.opsresearch.orobjects.lib.real.matrix.ArrayVector;
import com.opsresearch.orobjects.lib.real.matrix.DenseMatrix;
import com.opsresearch.orobjects.lib.real.matrix.DenseVector;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;
import com.opsresearch.orobjects.lib.util.Array;

public class CroutPivot implements LUDecompositionI, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private int _exchangeCnt;
	private int[] _permutation;
	private double[][] _array;
	private double _epsilon;
	private boolean _fixSingular = false;

	public CroutPivot() {
		_array = null;
		_permutation = null;
	}

	public CroutPivot(MatrixI matrix) throws AlgebraException, SingularException {
		_array = null;
		_permutation = null;
		decompose(matrix);
	}

	public void setFixSingular(boolean value) {
		_fixSingular = value;
	}

	public void decompose(MatrixI m) throws AlgebraException, SingularException {

		int n = m.sizeOfRows();
		_array = m.getArray();
		_permutation = new int[n];
		_exchangeCnt = 0;
		_epsilon = m.getEpsilon();

		double[] scale = new double[n];
		for (int i = 0; i < n; i++) {
			double max = Array.maximumAbsoluteValue(n, _array[i], 0, 1);
			if (max <= _epsilon)
				throw new SingularException();
			scale[i] = 1.0 / max;
		}

		int pivot = 0;
		for (int diag = 0; diag < n; diag++) {
			double[] rowDiag = _array[diag];
			for (int i = 0; i < diag; i++) {
				double[] rowI = _array[i];
				double accumulate = rowI[diag];
				for (int j = 0; j < i; j++)
					accumulate -= rowI[j] * _array[j][diag];
				rowI[diag] = accumulate;
			}

			double max = 0.0;
			for (int i = diag; i < n; i++) {
				double[] rowI = _array[i];
				double accumulate = rowI[diag];
				for (int j = 0; j < diag; j++)
					accumulate -= rowI[j] * _array[j][diag];
				rowI[diag] = accumulate;
				double pivotValue = scale[i] * Math.abs(accumulate);
				if (pivotValue >= max) {
					max = pivotValue;
					pivot = i;
				}
			}

			if (diag != pivot) {
				rowDiag = _array[pivot];
				_array[pivot] = _array[diag];
				_array[diag] = rowDiag;
				_exchangeCnt++;
				scale[pivot] = scale[diag];
			}
			_permutation[diag] = pivot;

			if (Math.abs(rowDiag[diag]) <= _epsilon) {
				if (_fixSingular)
					rowDiag[diag] = _epsilon;
				else
					throw new SingularException();
			}

			if (diag != (n - 1)) {
				double pivotValue = rowDiag[diag];
				for (int i = diag + 1; i < n; i++)
					_array[i][diag] /= pivotValue;
			}
		}
	}

	public DenseMatrix getL() {
		DenseMatrix results = new DenseMatrix(_array.length, _array.length);
		getL(results);
		return results;
	}

	public MatrixI getL(MatrixI results) {
		int n = _array.length;
		for (int i = 0; i < n; i++) {
			results.setElementAt(i, i, 1.0);
			double[] rowI = _array[i];
			for (int j = 0; j < i; j++) {
				results.setElementAt(i, j, rowI[j]);
			}
		}
		return results;
	}

	public DenseMatrix getU() {
		DenseMatrix results = new DenseMatrix(_array.length, _array.length);
		getU(results);
		return results;
	}

	public MatrixI getU(MatrixI results) {
		int n = _array.length;
		for (int i = 0; i < n; i++) {
			double[] rowI = _array[i];
			for (int j = i; j < n; j++) {
				results.setElementAt(i, j, rowI[j]);
			}
		}
		return results;
	}

	public int[] getRowPermutations() {
		return _permutation;
	}

	public DenseVector solveEquations(VectorI rightHandSides) throws AlgebraException {
		DenseVector results = new DenseVector(rightHandSides.size());
		solveEquations(rightHandSides, results);
		return results;
	}

	public VectorI solveEquations(VectorI rightHandSides, VectorI results) throws AlgebraException {
		if (_array == null)
			throw new AlgebraException("No matrix has been decomposed.");

		double[] array = rightHandSides.getArray();

		int n = _array.length;
		int index = 0;
		boolean isIndexValid = false;
		for (int i = 0; i < n; i++) {
			int ip = _permutation[i];
			double accumulate = array[ip];
			array[ip] = array[i];

			if (isIndexValid) {
				double[] rowI = _array[i];
				for (int j = index; j <= i - 1; j++)
					accumulate -= rowI[j] * array[j];
			} else if (accumulate != 0.0) {
				isIndexValid = true;
				index = i;
			}

			array[i] = accumulate;
		}
		for (int i = (n - 1); i >= 0; i--) {
			double accumulate = array[i];
			double[] rowI = _array[i];
			for (int j = i + 1; j < n; j++)
				accumulate -= rowI[j] * array[j];
			array[i] = accumulate / rowI[i];
		}

		results.setElements(new ArrayVector(array, true));
		return results;
	}

	public DenseMatrix computeInverse() throws AlgebraException {
		DenseMatrix results = new DenseMatrix(_array.length, _array.length);
		computeInverse(results);
		return results;
	}

	public MatrixI computeInverse(MatrixI results) throws AlgebraException {
		if (_array == null)
			throw new AlgebraError("No matrix has been decomposed.");

		int n = _array.length;
		DenseVector col = new DenseVector(n);
		DenseVector rsl = new DenseVector(n);
		for (int j = 0; j < n; j++) {
			col.setElementAt(j, 1.0);
			results.setColumn(j, solveEquations(col, rsl));
			col.setElementAt(j, 0.0);
		}

		return results;
	}

	public double computeDeterminate() throws AlgebraException {
		if (_array == null)
			throw new AlgebraError("No matrix has been decomposed.");
		double determinate = _exchangeCnt % 2 == 0 ? 1.0 : -1.0;
		for (int j = 0; j < _array.length; j++)
			determinate *= _array[j][j];
		return determinate;
	}

}
