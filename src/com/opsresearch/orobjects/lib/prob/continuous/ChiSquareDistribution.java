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


package com.opsresearch.orobjects.lib.prob.continuous;

import com.opsresearch.orobjects.lib.InvalidArgumentError;
import com.opsresearch.orobjects.lib.prob.ProbError;
import com.opsresearch.orobjects.lib.real.approx.ApproxException;
import com.opsresearch.orobjects.lib.real.approx.Inverse;
import com.opsresearch.orobjects.lib.real.approx.InverseI;
import com.opsresearch.orobjects.lib.real.function.FunctionI;
import com.opsresearch.orobjects.lib.real.function.IncompleteGamma;
import com.opsresearch.orobjects.lib.real.function.IncompleteGammaI;


public class ChiSquareDistribution extends ContinuousDistribution implements ChiSquareDistributionI, FunctionI {
	private static final long serialVersionUID = 1L;

	private double _n, _k, _n2, _n22, _max;
	private IncompleteGammaI _incompleteGamma = new IncompleteGamma();
	private static InverseI _inverse = new Inverse();

	public ChiSquareDistribution() {
	}

	public ChiSquareDistribution(double degreesOfFreedom) {
		setParameters(degreesOfFreedom);
	}

	public void setParameters(double degreesOfFreedom) {
		if (degreesOfFreedom < 1.0)
			throw new InvalidArgumentError("The degreesOfFreedom can't be less than '1.0'.");
		if (isValid() && _n == degreesOfFreedom)
			return;

		setValid(false);
		_n = degreesOfFreedom;
		_n2 = 0.5 * _n;
		_n22 = 0.5 * (_n - 2.0);
		_incompleteGamma.setParameter(_n2);
		_k = Math.log(1.0) - Math.log(2.0) * _n2 - _incompleteGamma.evaluate(_n2);
		_max = 10000.0;
		setVariableBounds(0.0, _max);
		setCdfBounds(0.0, _cdf(_max));
		setValid(true);
	}

	public void set_incompleteGamma(IncompleteGammaI _incompleteGamma) {
		this._incompleteGamma = _incompleteGamma;
	}

	public static void set_inverse(InverseI _inverse) {
		ChiSquareDistribution._inverse = _inverse;
	}

	public double _cdf(double x) {
		return _incompleteGamma.evaluate(0.5 * x);
	}

	public double _inverseCdf(double probability) {
		try {
			return _inverse.solve(this, 0.0, _max, probability);
		} catch (ApproxException e) {
			throw new ProbError(e);
		}
	}

	public double pdf(double x) {
		if (x < 0.0)
			throw new InvalidArgumentError("'x' can't be negative.");
		if (x == 0.0)
			return 0.0;

		return Math.exp(_k + Math.log(x) * _n22 + -0.5 * x);
	}

	public double mean() {
		return _n;
	}

	public double variance() {
		return 2.0 * _n;
	}

	public double std() {
		if (_n <= 4.0)
			throw new ProbError("Can't compute the std for degreesOfFreedom <= 4.0");
		return Math.sqrt(variance());
	}

	public boolean equals(Object o) {
		if (!(o instanceof ChiSquareDistribution))
			return false;
		ChiSquareDistribution d = (ChiSquareDistribution) o;
		return d._n == _n;
	}

	public String toString() {
		return "ChiSquareDistribution(degreesOfFreedom = " + _n + ")";
	}

	@Override
	public double evaluate(double x) {
		return cdf(x);
	}

}
