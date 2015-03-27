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


package com.opsresearch.orobjects.lib.stat.model;

import com.opsresearch.orobjects.lib.algebra.AlgebraException;
import com.opsresearch.orobjects.lib.real.matrix.DenseMatrix;
import com.opsresearch.orobjects.lib.real.matrix.DenseVector;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.RowArrayMatrix;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class LeastSquaresSVD extends Model implements GeneralLinearModelI {

	private int _nSamples, _DFR, _nVariables;
	private int[] _select;
	private MatrixI _independent;
	private VectorI _dependent;
	private double _SSE, _SSR, _SST, _mean;
	private VectorI _coefficients, _stdv;
	private boolean _solved;
	private boolean _zeroIntercept;

	public void setSamples(VectorI dependent, MatrixI independent) {

		if (dependent.size() != independent.sizeOfRows())
			throw new Error(
					"The dependent vector size must match the row size of the independent matrix.");
		_dependent = dependent;
		_independent = independent;
		_solved = _zeroIntercept = false;

		_nSamples = dependent.size();
		_mean = dependent.sum() / _nSamples;
		_SST = dependent.sumOfSquaredDifferences(_mean);
	}

	public double getRSquared() {
		return _SSR / _SST;
	}

	public double getSST() {
		return _SST;
	}

	public double getSSE() {
		return _SSE;
	}

	public double getSSR() {
		return _SSR;
	}

	public int getDFR() {
		return _DFR;
	}

	public int getDFE() {
		return getDFT() - getDFR();
	}

	public int getDFT() {
		return _nSamples - 1;
	}

	public double getMST() {
		return getSST() / getDFT();
	}

	public double getMSE() {
		return getSSE() / getDFE();
	}

	public double getMSR() {
		return getSSR() / getDFR();
	}

	public VectorI solve() {
		return solve(false, null);
	}

	public VectorI solve(boolean forceZeroIntercept) {
		return solve(forceZeroIntercept, null);
	}

	public VectorI solve(boolean forceZeroIntercept, int[] columnSelection) {
		if (columnSelection == null) {
			_select = new int[_independent.sizeOfColumns()];
			for (int i = 0; i < _select.length; i++)
				_select[i] = i;
		} else {
			_select = columnSelection;
			for (int i = 0; i < _select.length; i++) {
				int nc = _independent.sizeOfColumns();
				if (_select[i] >= nc)
					throw new Error("columnSelection[" + i + "] = "
							+ _select[i]
							+ " too large for the independent columns (" + nc
							+ ").");
			}
		}
		_zeroIntercept = forceZeroIntercept;
		_solve();
		return getCoefficients();
	}

	private void _solve() {
		_solved = false;
		_stdv = null;
		_DFR = _select.length;

		_nVariables = _zeroIntercept ? _DFR : _DFR + 1;
		MatrixI matrix = new RowArrayMatrix(_nSamples, _nVariables);

		for (int i = 0; i < _nSamples; i++) {
			for (int j = 0; j < _DFR; j++) {
				matrix.setElementAt(i, j, _independent.elementAt(i, _select[j]));
			}
			if (!_zeroIntercept)
				matrix.setElementAt(i, _DFR, 1.0);
		}

		_coefficients = new DenseVector(_nVariables);

		try {
			matrix.setEpsilon(getEpsilon());
			getSVDecomposition().decompose(matrix);

			VectorI w = getSVDecomposition().getW();
			double max = w.maximumValue();
			double tst = max * getEpsilon();
			for (int i = 0; i < _nVariables; i++)
				if (w.elementAt(i) < tst)
					w.setElementAt(i, 0.0);
			getSVDecomposition().setW(w);
			getSVDecomposition().solveEquations(_dependent, _coefficients);
		} catch (AlgebraException e) {
			throw new Error("Got Algebra Exception" + e.getMessage());
		}

		_SSE = _SSR = 0.0;
		for (int i = 0; i < _nSamples; i++) {
			double computedValue = 0.0;
			for (int j = 0; j < _nVariables; j++) {
				computedValue += _coefficients.elementAt(j)
						* _independent.elementAt(i, _select[j]);
			}
			double diff = _dependent.elementAt(i) - computedValue;
			_SSE += diff * diff;
			diff = _mean - computedValue;
			_SSR += diff * diff;
		}
		_solved = true;
	}

	public VectorI getDependent() {
		return _dependent;
	}

	public MatrixI getIndependent() {
		return _independent;
	}

	public VectorI getCoefficients() {
		if (!_solved)
			throw new Error("The GLM is not solved.");
		return new DenseVector(_coefficients);
	}

	public VectorI getStandardErrors() {
		if (!_solved)
			throw new Error("The GLM is not solved.");
		if (_stdv != null)
			return _stdv;
		_stdv = new DenseVector(_nVariables);
		VectorI w = getSVDecomposition().getW();
		double[] invsq = new double[_nVariables];
		for (int i = 0; i < _nVariables; i++) {
			double v = w.elementAt(i);
			if (v != 0)
				invsq[i] = 1.0 / (v * v);
		}
		MatrixI v = getSVDecomposition().getV();
		for (int i = 0; i < _nVariables; i++) {
			double sum = 0.0;
			for (int k = 0; k < _nVariables; k++) {
				double d = v.elementAt(i, k);
				sum += d * d * invsq[k];
			}
			_stdv.setElementAt(i, Math.sqrt(getMSE() * sum));
		}
		return _stdv;
	}

	public MatrixI getInverseXX() {
		if (!_solved)
			throw new Error("The GLM is not solved.");

		MatrixI invXX = new DenseMatrix(_nVariables, _nVariables);
		double[] invsq = new double[_nVariables];
		VectorI w = getSVDecomposition().getW();
		for (int i = 0; i < _nVariables; i++) {
			double v = w.elementAt(i);
			if (v != 0)
				invsq[i] = 1.0 / (v * v);
		}

		MatrixI v = getSVDecomposition().getV();
		for (int i = 0; i < _nVariables; i++) {
			for (int j = 0; j <= i; j++) {
				double sum = 0.0;
				for (int k = 0; k < _nVariables; k++)
					sum += v.elementAt(i, k) + v.elementAt(j, k) * invsq[k];
				invXX.setElementAt(j, i, sum);
				invXX.setElementAt(i, j, sum);
			}
		}
		return invXX;
	}

	public String toString() {
		StringBuffer s = new StringBuffer(4096);
		s.append("------------------------------\n");
		s.append("---- General Linear Model ----\n");
		s.append("------------------------------\n");
		if (!_solved) {
			s.append("NO SOLUTION\n");
			return s.toString();
		}
		s.append("\n");
		s.append("R-Squared        = " + getRSquared() + "\n");
		s.append("\n");
		s.append("REGRESSION\n");
		s.append("  Degrees of freedom (DFR) = ").append(getDFR()).append("\n");
		s.append("  Sum of squares     (SSR) = ").append(getSSR()).append("\n");
		s.append("  Mean squared       (MSR) = ").append(getMSR()).append("\n");
		s.append("\n");

		s.append("ERROR\n");
		s.append("  Degrees of freedom (DFE) = ").append(getDFE()).append("\n");
		s.append("  Sum of squares     (SSE) = ").append(getSSE()).append("\n");
		s.append("  Mean squared       (MSE) = ").append(getMSE()).append("\n");
		s.append("\n");

		s.append("TOTAL\n");
		s.append("  Degrees of freedom (DFT) = ").append(getDFT()).append("\n");
		s.append("  Sum of squares     (SST) = ").append(getSST()).append("\n");
		s.append("  Mean squared       (MST) = ").append(getMST()).append("\n");
		s.append("\n");

		int nv = _DFR;
		if (!_zeroIntercept) {
			s.append("INTERCEPT\n");
			s.append("  Coefficient    = ").append(_coefficients.elementAt(nv))
					.append("\n");
			s.append("  Standard Error = ").append(_stdv.elementAt(nv))
					.append("\n");
			s.append("\n");
		}

		getStandardErrors();
		for (int i = 0; i < nv; i++) {
			int j = _select[i];
			s.append("COLUMN-").append(j).append("\n");
			s.append("  Coefficient    = ").append(_coefficients.elementAt(i))
					.append("\n");
			s.append("  Standard Error = ").append(_stdv.elementAt(i))
					.append("\n");
			s.append("\n");
		}

		return s.toString();
	}

}
