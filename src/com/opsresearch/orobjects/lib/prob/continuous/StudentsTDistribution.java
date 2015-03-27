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
import com.opsresearch.orobjects.lib.real.Constants;
import com.opsresearch.orobjects.lib.real.approx.ApproxException;
import com.opsresearch.orobjects.lib.real.approx.Inverse;
import com.opsresearch.orobjects.lib.real.approx.InverseI;
import com.opsresearch.orobjects.lib.real.function.FunctionI;


public class StudentsTDistribution extends ContinuousDistribution implements
		StudentsTDistributionI, FunctionI {
	private static final long serialVersionUID = 1L;

	private InverseI _inverse = new Inverse();

	private double _degreesOfFreedom, _cache1, _k;
	private final double _variableMax = 10000.0;
	private final double _variableMin = -_variableMax;

	public StudentsTDistribution() {
	}

	public StudentsTDistribution(double degreesOfFreedom) {
		setParamater(degreesOfFreedom);
	}

	public void setParamater(double degreesOfFreedom) {
		if (degreesOfFreedom < 1.0)
			throw new InvalidArgumentError(
					"The degreesOfFreedom can't be less than '1.0'.");
		if (isValid() && degreesOfFreedom == _degreesOfFreedom)
			return;

		setValid(false);
		_degreesOfFreedom = degreesOfFreedom;
		_cache1 = -0.5 * (_degreesOfFreedom + 1);
		getIncompleteBeta().setParameters(0.5 * _degreesOfFreedom, 0.5);
		_k = getIncompleteBeta().evaluate(-_cache1) - 0.5
				* Math.log(_degreesOfFreedom * Constants.pi)
				- getIncompleteBeta().evaluate(0.5 * _degreesOfFreedom);
		setVariableBounds(_variableMin, _variableMax);
		setCdfBounds(cdf(_variableMin), cdf(_variableMax));
		setValid(true);
	}

	public void setInverse(InverseI inverse) {
		_inverse = inverse;
	}

	public double _cdf(double x) {
		if (x == 0.0)
			return 0.5;

		double a = getIncompleteBeta().evaluate(
				_degreesOfFreedom / (_degreesOfFreedom + x * x));
		if (x >= 0.0)
			return 1.0 - 0.5 * a;
		return 0.5 * a;
	}

	public double _inverseCdf(double probability) {
		try {
			return _inverse
					.solve(this, _variableMin, _variableMax, probability);
		} catch (ApproxException e) {
			throw new ProbError("Nonlinear:" + e.getMessage());
		}
	}

	public double pdf(double x) {
		return Math.exp(_k + Math.log(1.0 + x * (x / _degreesOfFreedom))
				* _cache1);
	}

	public double mean() {
		return 0.0;
	}

	public double variance() {
		if (_degreesOfFreedom <= 2.0)
			throw new ProbError(
					"Can't compute the variance for degreesOfFreedom <= 2.0");
		return _degreesOfFreedom / (_degreesOfFreedom - 2.0);
	}

	public double std() {
		if (_degreesOfFreedom <= 2.0)
			throw new ProbError(
					"Can't compute the std for degreesOfFreedom <= 2.0");
		return Math.sqrt(variance());
	}

	public boolean equals(Object o) {
		if (!(o instanceof StudentsTDistribution))
			return false;
		StudentsTDistribution d = (StudentsTDistribution) o;
		return d._degreesOfFreedom == _degreesOfFreedom;
	}

	public String toString() {
		return "StudentsDistribution(degreesOfFreedom = " + _degreesOfFreedom
				+ ")";
	}

	@Override
	public double evaluate(double x) {
		return pdf(x);
	}

}
