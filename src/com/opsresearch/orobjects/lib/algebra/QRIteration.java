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
import com.opsresearch.orobjects.lib.real.matrix.RowArrayMatrix;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;
import com.opsresearch.orobjects.lib.util.Array;

public class QRIteration implements SVDecompositionI, java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private double _epsilon;
	private double[] _arrayW;
	private double[] _arrayWf;
	private double[][] _arrayU;
	private double[][] _arrayV;
	private int _nRows, _nCols;
	private int _maxIterations = 30;

	private int _i, _j, _j1;
	private double _c, _f, _h, _s, _x, _y, _z;
	private int _l;
	private int _i1;
	private double[] _ar;
	private double _g;
	private double _x1;
	private double _x2;

	public QRIteration() {
	}

	public QRIteration(MatrixI matrix) throws AlgebraException {
		decompose(matrix);
	}

	public MatrixI getU() {
		if (_arrayU == null)
			throw new AlgebraError("U does not exist.");
		return new RowArrayMatrix(_arrayU);
	}

	public MatrixI getV() {
		if (_arrayV == null)
			throw new AlgebraError("V does not exist.");
		return new RowArrayMatrix(_arrayV);
	}

	public VectorI getW() {
		if (_arrayW == null)
			throw new AlgebraError("W does not exist.");
		return new ArrayVector(_arrayW);
	}

	public void setW(VectorI w) {
		if (w.size() != _nRows)
			throw new AlgebraError("W is the wrong size.");
		_arrayW = w.getArray();
	}

	public VectorI solveEquations(VectorI rightHandSides) throws AlgebraException {
		DenseVector results = new DenseVector(_arrayW.length);
		solveEquations(rightHandSides, results);
		return results;
	}

	public VectorI solveEquations(VectorI rightHandSides, VectorI results) throws AlgebraException {
		if (_arrayU == null)
			throw new AlgebraError("No matrix has been decomposed");

		double[] rhs = rightHandSides.getArray();

		if (_arrayWf == null)
			makeWf();
		int j1, j, i;
		double s;

		double[] tmp = new double[_nCols];
		for (j = 0; j < _nCols; j++) {
			s = 0.0;
			if (_arrayWf[j] != 0.0) {
				for (i = 0; i < _nRows; i++)
					s += _arrayU[i][j] * rhs[i];
				s *= _arrayWf[j];
			}
			tmp[j] = s;
		}
		for (j = 0; j < _nCols; j++) {
			s = 0.0;
			double[] rowVJ = _arrayV[j];
			for (j1 = 0; j1 < _nCols; j1++)
				s += rowVJ[j1] * tmp[j1];
			results.setElementAt(j, s);
		}

		return results;
	}

	public double computeConditionNumber() {
		if (_arrayU == null)
			throw new AlgebraError("No matrix has been decomposed");
		int n = _arrayW.length;
		double min = Math.abs(_arrayW[0]);
		double max = min;
		for (int i = 1; i < n; i++) {
			double w = Math.abs(_arrayW[i]);
			if (w < min)
				min = w;
			if (w > max)
				max = w;
		}
		return min == 0.0 ? Double.NaN : max / min;
	}

	public boolean isIllConditioned() {
		if (_arrayU == null)
			throw new AlgebraError("No matrix has been decomposed");
		return (1.0 / computeConditionNumber()) < 1.0e-12;
	}

	public boolean isSingular() {
		if (_arrayU == null)
			throw new AlgebraError("No matrix has been decomposed");
		int n = _arrayW.length;
		for (int i = 1; i < n; i++)
			if (_arrayW[i] == 0.0)
				return true;

		return false;
	}

	public DenseMatrix computeInverse() throws AlgebraException {
		DenseMatrix results = new DenseMatrix(_arrayW.length, _arrayW.length);
		computeInverse(results);
		return results;
	}

	public MatrixI computeInverse(MatrixI results) throws AlgebraException {
		if (_arrayU == null)
			throw new AlgebraError("No matrix has been decomposed.");
		if (_arrayU.length != _arrayW.length)
			throw new AlgebraException("The decomposed matrix was not square.");
		if (_arrayWf == null)
			makeWf();
		int n = _arrayW.length;
		for (int i = 0; i < n; i++) {
			double[] rowVI = _arrayV[i];
			for (int j = 0; j < n; j++) {
				double s = 0.0;
				double[] rowUJ = _arrayU[j];
				for (int k = 0; k < n; k++)
					s += rowVI[k] * _arrayWf[k] * rowUJ[k];
				results.setElementAt(i, j, s);
			}
		}
		return results;
	}

	public void decompose(MatrixI matrix) throws AlgebraException {
		_nRows = matrix.sizeOfRows();
		_nCols = matrix.sizeOfColumns();
		_arrayW = new double[_nCols];
		_arrayU = matrix.getArray();
		_arrayV = new double[_nCols][_nCols];
		_epsilon = matrix.getEpsilon();
		_arrayWf = null;
		_l = 0;
		_i1 = 0;

		_ar = new double[_nCols];
		_g = 0.0;
		_x1 = 0.0;
		_x2 = 0.0;

		reduce();
		accumulateRight();
		accumulateLeft();

		for (int diag = (_nCols - 1); diag >= 0; diag--) {
			boolean converged = false;
			for (int iteration = 0; iteration < _maxIterations; iteration++) {

				if (needsSplitting(diag)) {
					cancel(diag);
				}
				_z = _arrayW[diag];
				if (_l == diag) {
					if (_z < 0.0) {
						_arrayW[diag] = -_z;
						for (_j = 0; _j < _nCols; _j++)
							_arrayV[_j][diag] = -_arrayV[_j][diag];
					}
					converged = true;
					break;
				}

				shift(diag);
			}
			if (!converged) {
				throw new AlgebraError("Exceeded " + _maxIterations + " iterations.");
			}
		}
	}

	private double hypotenuse(double x, double y) {
		if (x == 0 || y == 0.0)
			return 0.0;
		x = Math.abs(x);
		y = Math.abs(y);
		if (x > y) {
			double div = y / x;
			return x * Math.sqrt(1.0 + div * div);
		}
		double div = x / y;
		return y * Math.sqrt(1.0 + div * div);
	}

	private void reduce() {
		for (_i = 0; _i < _nCols; _i++) {
			_l = _i + 1;
			_ar[_i] = _x1 * _g;
			_g = _s = _x1 = 0.0;
			if (_i < _nRows) {
				double[] rowAI = _arrayU[_i];
				for (int k = _i; k < _nRows; k++)
					_x1 += Math.abs(_arrayU[k][_i]);
				if (_x1 != 0.0) {
					for (int k = _i; k < _nRows; k++) {
						_arrayU[k][_i] /= _x1;
						_s += _arrayU[k][_i] * _arrayU[k][_i];
					}
					_f = rowAI[_i];
					_g = (_f >= 0 ? -Math.sqrt(_s) : Math.sqrt(_s));
					_h = _f * _g - _s;
					rowAI[_i] = _f - _g;
					for (_j = _l; _j < _nCols; _j++) {
						_s = 0.0;
						for (int k = _i; k < _nRows; k++)
							_s += _arrayU[k][_i] * _arrayU[k][_j];
						_f = _s / _h;
						for (int k = _i; k < _nRows; k++)
							_arrayU[k][_j] += _f * _arrayU[k][_i];
					}
					for (int k = _i; k < _nRows; k++)
						_arrayU[k][_i] *= _x1;
				}
			}
			_arrayW[_i] = _x1 * _g;
			_g = _s = _x1 = 0.0;
			if (_i < _nRows && _i != (_nCols - 1)) {
				double[] rowAI = _arrayU[_i];
				for (int k = _l; k < _nCols; k++)
					_x1 += Math.abs(rowAI[k]);
				if (_x1 != 0.0) {
					for (int k = _l; k < _nCols; k++) {
						double d = rowAI[k] /= _x1;
						_s += d * d;
					}
					_f = rowAI[_l];
					_g = (_f >= 0 ? -Math.sqrt(_s) : Math.sqrt(_s));
					_h = _f * _g - _s;
					rowAI[_l] = _f - _g;
					for (int k = _l; k < _nCols; k++)
						_ar[k] = rowAI[k] / _h;
					for (_j = _l; _j < _nRows; _j++) {
						_s = 0.0;
						for (int k = _l; k < _nCols; k++)
							_s += _arrayU[_j][k] * rowAI[k];
						for (int k = _l; k < _nCols; k++)
							_arrayU[_j][k] += _s * _ar[k];
					}
					for (int k = _l; k < _nCols; k++)
						rowAI[k] *= _x1;
				}
			}
			_x2 = Math.max(_x2, (Math.abs(_arrayW[_i]) + Math.abs(_ar[_i])));
		}
	}

	private void accumulateRight() {
		for (_i = (_nCols - 1); _i >= 0; _i--) {
			double[] rowVI = _arrayV[_i];
			if (_i < _nCols) {
				if (_g != 0.0) {
					double[] rowAI = _arrayU[_i];
					for (_j = _l; _j < _nCols; _j++)
						_arrayV[_j][_i] = (rowAI[_j] / rowAI[_l]) / _g;
					for (_j = _l; _j < _nCols; _j++) {
						_s = 0.0;
						for (int k = _l; k < _nCols; k++)
							_s += rowAI[k] * _arrayV[k][_j];
						for (int k = _l; k < _nCols; k++)
							_arrayV[k][_j] += _s * _arrayV[k][_i];
					}
				}
				for (_j = _l; _j < _nCols; _j++)
					rowVI[_j] = _arrayV[_j][_i] = 0.0;
			}
			rowVI[_i] = 1.0;
			_g = _ar[_i];
			_l = _i;
		}
	}

	private void accumulateLeft() {
		for (_i = Math.min(_nRows, _nCols) - 1; _i >= 0; _i--) {
			double[] rowAI = _arrayU[_i];
			_l = _i + 1;
			_g = _arrayW[_i];
			for (_j = _l; _j < _nCols; _j++)
				rowAI[_j] = 0.0;
			if (_g != 0.0) {
				_g = 1.0 / _g;
				for (_j = _l; _j < _nCols; _j++) {
					_s = 0.0;
					for (int k = _l; k < _nRows; k++)
						_s += _arrayU[k][_i] * _arrayU[k][_j];
					_f = (_s / rowAI[_i]) * _g;
					for (int k = _i; k < _nRows; k++)
						_arrayU[k][_j] += _f * _arrayU[k][_i];
				}
				for (_j = _i; _j < _nRows; _j++)
					_arrayU[_j][_i] *= _g;
			} else
				for (_j = _i; _j < _nRows; _j++)
					_arrayU[_j][_i] = 0.0;
			++rowAI[_i];
		}
	}

	private void shift(int diag) {
		_x = _arrayW[_l];
		_i1 = diag - 1;
		_y = _arrayW[_i1];
		_g = _ar[_i1];
		_h = _ar[diag];
		_f = ((_y - _z) * (_y + _z) + (_g - _h) * (_g + _h)) / (2.0 * _h * _y);
		_g = hypotenuse(_f, 1.0);
		_f = ((_x - _z) * (_x + _z) + _h * ((_y / (_f + (_f >= 0 ? _g : -_g))) - _h)) / _x;
		_c = _s = 1.0;
		for (_j = _l; _j <= _i1; _j++) {
			_i = _j + 1;
			_g = _ar[_i];
			_y = _arrayW[_i];
			_h = _s * _g;
			_g = _c * _g;
			_z = hypotenuse(_f, _h);
			_ar[_j] = _z;
			_c = _f / _z;
			_s = _h / _z;
			_f = _x * _c + _g * _s;
			_g = _g * _c - _x * _s;
			_h = _y * _s;
			_y *= _c;
			for (_j1 = 0; _j1 < _nCols; _j1++) {
				double[] rowVJ1 = _arrayV[_j1];
				_x = rowVJ1[_j];
				_z = rowVJ1[_i];
				rowVJ1[_j] = _x * _c + _z * _s;
				rowVJ1[_i] = _z * _c - _x * _s;
			}
			_z = hypotenuse(_f, _h);
			_arrayW[_j] = _z;
			if (_z != 0.0) {
				_z = 1.0 / _z;
				_c = _f * _z;
				_s = _h * _z;
			}
			_f = _c * _g + _s * _y;
			_x = _c * _y - _s * _g;
			for (_j1 = 0; _j1 < _nRows; _j1++) {
				double[] rowAJ1 = _arrayU[_j1];
				_y = rowAJ1[_j];
				_z = rowAJ1[_i];
				rowAJ1[_j] = _y * _c + _z * _s;
				rowAJ1[_i] = _z * _c - _y * _s;
			}
		}
		_ar[_l] = 0.0;
		_ar[diag] = _f;
		_arrayW[diag] = _x;
	}

	private boolean needsSplitting(int diag) {
		boolean split = true;
		for (_l = diag; _l >= 0; _l--) {
			_i1 = _l - 1;
			if ((double) (Math.abs(_ar[_l]) + _x2) == _x2) {
				split = false;
				break;
			}
			if ((double) (Math.abs(_arrayW[_i1]) + _x2) == _x2)
				break;
		}
		return split;
	}

	private void cancel(int diag) {
		_c = 0.0;
		_s = 1.0;
		for (_i = _l; _i < diag; _i++) {
			_f = _s * _ar[_i];
			_ar[_i] = _c * _ar[_i];
			if ((double) (Math.abs(_f) + _x2) == _x2)
				break;
			_g = _arrayW[_i];
			_h = hypotenuse(_f, _g);
			_arrayW[_i] = _h;
			_h = 1.0 / _h;
			_c = _g * _h;
			_s = -_f * _h;
			for (_j = 0; _j < _nRows; _j++) {
				double[] rowAJ = _arrayU[_j];
				_y = rowAJ[_i1];
				_z = rowAJ[_i];
				rowAJ[_i1] = _y * _c + _z * _s;
				rowAJ[_i] = _z * _c - _y * _s;
			}
		}
	}

	private void makeWf() {
		int n = _arrayW.length;
		_arrayWf = new double[n];
		double max = Array.maximumValue(n, _arrayW, 0, 1);
		double min = max * _epsilon;
		for (int i = 0; i < n; i++)
			_arrayWf[i] = (_arrayW[i] < min ? 0.0 : 1.0 / _arrayW[i]);
	}

}
