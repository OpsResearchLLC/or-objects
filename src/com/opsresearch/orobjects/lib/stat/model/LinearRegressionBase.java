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

import com.opsresearch.orobjects.lib.real.matrix.DenseVector;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public abstract class LinearRegressionBase extends Model implements
		LinearRegressionI {

	protected int _m, _n;
	protected int[] _selectedVariables;
	protected int[] _requiredVariables;
	protected int[] _enteringVariables;

	protected VectorI _dependent, _coef, _upper, _lower, _stdv, _tstat,
			_tpvalue;
	protected MatrixI _independent, _cov;

	protected double _exitPValue = 0.05;
	protected double _enterPValue = 0.05;
	protected double _lastConfidenceLevel;
	protected double _cfstat;
	protected boolean _zeroIntercept = false;

	public void setSamples(VectorI dependent, MatrixI independent) {
		_dependent = dependent;
		_independent = independent;
		_m = _independent.sizeOfRows();
		_n = _independent.sizeOfColumns();
		_requiredVariables = new int[0];
		_enteringVariables = allVariables();
		setSelectedVariables(allVariables());
		getGeneralLinearModel().setSamples(_dependent, _independent);
	}

	@Override
	public void setGeneralLinearModel(GeneralLinearModelI generalLinearModel) {
		generalLinearModel.setSamples(_dependent, _independent);
		super.setGeneralLinearModel(generalLinearModel);
	}

	public int[] allVariables() {
		int[] a = new int[_n];
		for (int i = 0; i < _n; i++)
			a[i] = i;
		return a;
	}

	public void setEnteringVariables(int[] variables) {
		_enteringVariables = variables;
	}

	public void setRequiredVarables(int[] variables) {
		_requiredVariables = variables;
	}

	public void setEnteringThreshold(double pValue) {
		_enterPValue = pValue;
	}

	public double getEnteringThreshold() {
		return _enterPValue;
	}

	public void setExitingThreshold(double pValue) {
		_exitPValue = pValue;
	}

	public double getExitingThreshold() {
		return _exitPValue;
	}

	public int[] getSelectedVariables() {
		return _selectedVariables;
	}

	public int[] getEnteringVariables() {
		return _enteringVariables;
	}

	public int[] getRequiredVariables() {
		return _requiredVariables;
	}

	public void setSelectedVariables(int[] variables) {
		if (variables != null)
			_selectedVariables = variables;
		else {
			_selectedVariables = new int[_n];
			for (int i = 0; i < _n; i++)
				_selectedVariables[i] = i;
		}
		getGeneralLinearModel().solve(_zeroIntercept, _selectedVariables);
		_coef = getGeneralLinearModel().getCoefficients();
		_stdv = getGeneralLinearModel().getStandardErrors();
		_lower = _upper = null;

		int n = _selectedVariables.length;
		int n1 = _zeroIntercept ? n : n + 1;
		int df = _m - n - 1;

		getStudentsTDistribution().setParamater(df);

		_tstat = new DenseVector(n1);
		for (int i = 0; i < n; i++)
			_tstat.setElementAt(i, _coef.elementAt(i) / _stdv.elementAt(i));
		if (!_zeroIntercept)
			_tstat.setElementAt(n, _coef.elementAt(n) / _stdv.elementAt(n));

		_tpvalue = new DenseVector(n1);
		for (int i = 0; i < n; i++)
			_tpvalue.setElementAt(i, 2.0 * (1.0 - getStudentsTDistribution()
					.cdf(Math.abs(_tstat.elementAt(i)))));
		if (!_zeroIntercept)
			_tpvalue.setElementAt(n, 2.0 * (1.0 - getStudentsTDistribution()
					.cdf(Math.abs(_tstat.elementAt(n)))));

		_cfstat = getGeneralLinearModel().getMSR()
				/ getGeneralLinearModel().getMSE();
	}

	public double getF() {
		return _cfstat;
	}

	public double getFPV() {
		getFDistribution().setParameters(getGeneralLinearModel().getDFR(),
				getGeneralLinearModel().getDFT());
		return 1.0 - getFDistribution().cdf(_cfstat);
	}

	public VectorI getCoefficients() {
		return _coef;
	}

	public VectorI getLowerBounds(double confidenceLevel) {
		if (confidenceLevel < 0.0 || confidenceLevel > 1.0)
			throw new Error("Bad confidence level");
		if (_lower != null && _lastConfidenceLevel == confidenceLevel)
			return _lower;
		_lastConfidenceLevel = confidenceLevel;
		int n = _selectedVariables.length;
		int n1 = _zeroIntercept ? n : n + 1;
		int df = _m - n - 1;
		getStudentsTDistribution().setParamater(df);

		_lower = new DenseVector(n1);
		_upper = new DenseVector(n1);
		double coef, del, t = getStudentsTDistribution().inverseCdf(
				1.0 - 0.5 * (1.0 - confidenceLevel));
		for (int i = 0; i < n; i++) {
			coef = _coef.elementAt(i);
			del = _stdv.elementAt(i) * t;
			_lower.setElementAt(i, coef - del);
			_upper.setElementAt(i, coef + del);
		}
		if (!_zeroIntercept) {
			coef = _coef.elementAt(n);
			del = _stdv.elementAt(n) * t;
			_lower.setElementAt(n, coef - del);
			_upper.setElementAt(n, coef + del);
		}

		return _lower;
	}

	public VectorI getUpperBounds(double confidenceLevel) {
		getLowerBounds(confidenceLevel);
		return _upper;
	}

	public VectorI getT() {
		return _tstat;
	}

	public VectorI getTPV() {
		return _tpvalue;
	}

	public double getTStatistic(double pValue) {
		getStudentsTDistribution().setParamater(
				getGeneralLinearModel().getDFE());
		return getStudentsTDistribution().inverseCdf(pValue);
	}

	public String toString() {
		StringBuffer s = new StringBuffer(4096);
		s.append("---------------------------\n");
		s.append("---- Linear Regression ----\n");
		s.append("---------------------------\n");
		s.append("\n");
		s.append("R-Squared        = " + getGeneralLinearModel().getRSquared()
				+ "\n");
		s.append("\n");
		s.append("REGRESSION\n");
		s.append("  Degrees of freedom (DFR) = ")
				.append(getGeneralLinearModel().getDFR()).append("\n");
		s.append("  Sum of squares     (SSR) = ")
				.append(getGeneralLinearModel().getSSR()).append("\n");
		s.append("  Mean squared       (MSR) = ")
				.append(getGeneralLinearModel().getMSR()).append("\n");
		s.append("\n");

		s.append("ERROR\n");
		s.append("  Degrees of freedom (DFE) = ")
				.append(getGeneralLinearModel().getDFE()).append("\n");
		s.append("  Sum of squares     (SSE) = ")
				.append(getGeneralLinearModel().getSSE()).append("\n");
		s.append("  Mean squared       (MSE) = ")
				.append(getGeneralLinearModel().getMSE()).append("\n");
		s.append("\n");

		s.append("TOTAL\n");
		s.append("  Degrees of freedom (DFT) = ")
				.append(getGeneralLinearModel().getDFT()).append("\n");
		s.append("  Sum of squares     (SST) = ")
				.append(getGeneralLinearModel().getSST()).append("\n");
		s.append("  Mean squared       (MST) = ")
				.append(getGeneralLinearModel().getMST()).append("\n");
		s.append("\n");

		s.append("MODEL NULL HYPOTHESIS\n");
		s.append("  'F' statistic      = " + getF() + "\n");
		s.append("  'F' p-value        = " + getFPV() + "\n");
		s.append("\n");

		s.append("REGRESSION PARAMETERS\n");
		s.append("  Exit p-value       = " + _exitPValue + "\n");
		s.append("  Enter p-value      = " + _enterPValue + "\n");
		s.append("\n");

		int nv = _selectedVariables.length;
		getLowerBounds(0.95);
		if (!_zeroIntercept) {
			s.append("INTERCEPT\n");
			s.append("  Coefficient       = ").append(_coef.elementAt(nv))
					.append("\n");
			s.append("  Lower bound @ 95% = ").append(_lower.elementAt(nv))
					.append("\n");
			s.append("  Upper bound @ 95% = ").append(_upper.elementAt(nv))
					.append("\n");
			s.append("  Standard Error    = ").append(_stdv.elementAt(nv))
					.append("\n");
			s.append("  'T' statistic     = ").append(_tstat.elementAt(nv))
					.append("\n");
			s.append("  'T' p-value       = ").append(_tpvalue.elementAt(nv))
					.append("\n");
			s.append("\n");
		}

		for (int i = 0; i < nv; i++) {
			int j = _selectedVariables[i];
			s.append("COLUMN-").append(j).append("\n");
			s.append("  Coefficient       = ").append(_coef.elementAt(i))
					.append("\n");
			s.append("  Lower bound @ 95% = ").append(_lower.elementAt(i))
					.append("\n");
			s.append("  Upper bound @ 95% = ").append(_upper.elementAt(i))
					.append("\n");
			s.append("  Standard Error    = ").append(_stdv.elementAt(i))
					.append("\n");
			s.append("  'T' statistic     = ").append(_tstat.elementAt(i))
					.append("\n");
			s.append("  'T' p-value       = ").append(_tpvalue.elementAt(i))
					.append("\n");
			s.append("\n");
		}

		return s.toString();
	}

}
