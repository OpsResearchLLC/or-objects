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
import com.opsresearch.orobjects.lib.real.approx.Integration;
import com.opsresearch.orobjects.lib.real.approx.IntegrationI;
import com.opsresearch.orobjects.lib.real.approx.Inverse;
import com.opsresearch.orobjects.lib.real.approx.InverseI;
import com.opsresearch.orobjects.lib.real.function.FunctionI;


public class NormalDistribution extends ContinuousDistribution implements
		NormalDistributionI, FunctionI {
	private static final long serialVersionUID = 1L;

	private double _std = 1.0, _var, _var2;
	private double _mean = 0.0;
	private double _left, _extra;
	private double _variableMin, _variableMax;
	private boolean _haveExtra;
	private InverseI _inverse = new Inverse();

	private IntegrationI _integration = new Integration();

	public NormalDistribution() {
	}

	public NormalDistribution(double mean, double std) {
		setParameters(mean, std);
	}

	public void setParameters(double mean, double std) {
		if (std <= 0.0)
			throw new InvalidArgumentError("The std must be greater than zero.");
		if (isValid() && _mean == mean && _std == std)
			return;

		setValid(false);
		_std = std;
		_var = std * std;
		_var2 = -1.0 / (_var * 2.0);
		_mean = mean;
		_left = 1.0 / (Math.sqrt(2.0 * Constants.pi) * _var);
		_haveExtra = false;
		double far = 4.0 * std;
		_variableMin = mean - far;
		_variableMax = mean + far;
		setVariableBounds(_variableMin, _variableMax);
		setCdfBounds(_cdf(_variableMin), _cdf(_variableMax));
		setValid(true);
	}

	public void setIntegration(IntegrationI integration) {
		_integration = integration;
	}

	public double pdf(double x) {
		double d = x - _mean;
		return _left * Math.exp(d * d * _var2);
	}

	public double mean() {
		return _mean;
	}

	public double variance() {
		return _var;
	}

	public double std() {
		return _std;
	}

	public double getRandomDouble() {
		if (_haveExtra) {
			_haveExtra = false;
			return _extra;
		}

		double x, y, radiusSquared;
		do {
			x = nextDouble() * 2.0 - 1.0;
			y = nextDouble() * 2.0 - 1.0;
			radiusSquared = x * x + y * y;
		} while (radiusSquared >= 1.0 || radiusSquared == 0.0);

		double t = _std
				* Math.sqrt(-2.0 * Math.log(radiusSquared) / radiusSquared);
		_extra = y * t + _mean;
		_haveExtra = true;
		return x * t + _mean;
	}

	public boolean equals(Object o) {
		if (!(o instanceof NormalDistribution))
			return false;
		NormalDistribution d = (NormalDistribution) o;
		return d._std == _std && d._mean == _mean;
	}

	public String toString() {
		return "NormalDistribution(mean = " + _mean + ", std = " + _std + ")";
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
		if (probability == 0.5)
			return _mean;

		try {
			return _inverse.solve(new CDF(), _variableMin, _variableMax,
					probability);
		} catch (ApproxException e) {
			throw new ProbError(e);
		}
	}

	public double _cdf(double x) {
		if (x == _mean)
			return 0.5;
		try {
			if (x < _mean) {
				return _integration.solve(this, _variableMin, x);
			} else {
				return 0.5 + _integration.solve(this, _mean, x);
			}
		} catch (ApproxException e) {
			throw new ProbError(e);
		}
	}

	@Override
	protected double _probability(double x1, double x2) {

		try {
			return _integration.solve(this, x1, x2);
		} catch (ApproxException e) {
			throw new ProbError("NonLinear:" + e.getMessage());
		}
	}

	@Override
	public double evaluate(double x) {
		return pdf(x);
	}

}
