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

public class FDistribution extends ContinuousDistribution implements FDistributionI {
	private static final long serialVersionUID = 1L;

	private double _m, _n, _mn2, _m22, _k, _max;
	private InverseI _inverse = new Inverse();

	public FDistribution() {
	}

	public FDistribution(double degreesOfFreedom1, double degreesOfFreedom2) {
		setParameters(degreesOfFreedom1, degreesOfFreedom2);
	}

	public void setParameters(double degreesOfFreedom1, double degreesOfFreedom2) {
		if (degreesOfFreedom1 < 1.0)
			throw new InvalidArgumentError("The degreesOfFreedom1 can't be less than '1.0'.");
		if (degreesOfFreedom2 < 1.0)
			throw new InvalidArgumentError("The degreesOfFreedom2 can't be less than '1.0'.");
		if (isValid() && _m == degreesOfFreedom1 && _n == degreesOfFreedom2)
			return;

		setValid(false);
		_m = degreesOfFreedom1;
		_n = degreesOfFreedom2;
		getIncompleteBeta().setParameters(0.5 * _n, 0.5 * _m);
		_mn2 = 0.5 * (_m + _n);
		_m22 = 0.5 * (_m - 2.0);
		_k = lnGamma(_mn2) - lnGamma(0.5 * _m) - lnGamma(0.5 * _n);
		_k += Math.log(_m) * 0.5 * _m;
		_k += Math.log(_n) * 0.5 * _n;

		_max = 2000.0;
		setVariableBounds(0.0, _max);
		setCdfBounds(0.0, cdf(_max));
		setValid(true);
	}

	public void setInverse(InverseI inverse) {
		_inverse = inverse;
	}

	public double _cdf(double x) {
		return 1.0 - getIncompleteBeta().evaluate(_n / (_n + _m * x));
	}

	public class CDF implements FunctionI {
		public double evaluate(double x) {
			return cdf(x);
		}

		public boolean isValid() {
			return isValid();
		}
	}

	public double _inverseCdf(double probability) {
		try {
			return _inverse.solve(new CDF(), 0.0, _max, probability);
		} catch (ApproxException e) {
			throw new ProbError("Nonlinear:" + e.getMessage());
		}
	}

	public double pdf(double x) {
		if (x == 0.0)
			return 0.0;
		if (x < 0.0)
			throw new InvalidArgumentError("'x' can't be negative.");
		return Math.exp(_k + Math.log(x) * _m22 - Math.log(_n + _m * x) * _mn2);
	}

	public double mean() {
		return _n / (_n - 2.0);
	}

	public double variance() {
		if (_n <= 4.0)
			throw new ProbError("Can't compute the variance for degreesOfFreedom2 <= 4.0");
		double n2 = _n - 2.0;
		return 2.0 * _n * _n * (_m + n2) / (_m * n2 * n2 * (_n - 4));
	}

	public double std() {
		if (_n <= 4.0)
			throw new ProbError("Can't compute the std for degreesOfFreedom <= 4.0");
		return Math.sqrt(variance());
	}

	public boolean equals(Object o) {
		if (!(o instanceof FDistribution))
			return false;
		FDistribution d = (FDistribution) o;
		return d._n == _n && d._m == _m;
	}

	public String toString() {
		return "FDistribution(degreesOfFreedom = " + _n + ")";
	}

}
