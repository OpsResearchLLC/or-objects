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

package com.opsresearch.orobjects.lib.prob.discrete;

import com.opsresearch.orobjects.lib.InvalidArgumentError;
import com.opsresearch.orobjects.lib.real.Constants;

public abstract class PoissonDistributionBase extends DiscreteDistribution {
	private static final long serialVersionUID = 1L;
	private double _exp;
	private double _log;
	private double _gam;
	protected double _mean;
	private double _sqrt;

	public void setParameter(double mean) {
		if (mean <= 0.0)
			throw new InvalidArgumentError("The mean must be greater than zero.");
		if (isValid() && _mean == mean)
			return;

		setValid(false);
		_mean = mean;
		_exp = Math.exp(-_mean);
		_sqrt = Math.sqrt(2.0 * _mean);
		_log = Math.log(_mean);
		_gam = _mean * _log - lnGamma(_mean + 1.0);
		setValid(true);
	}

	public int getRandomInteger() {
		if (_mean < 12.0) {
			int cnt = -1;
			double t = 1.0;
			do {
				cnt++;
				t *= nextDouble();
			} while (t > _exp);
			return cnt;
		}

		double p, t, y;
		do {
			do {
				y = Math.tan(Constants.pi * nextDouble());
				p = _sqrt * y * _mean;

			} while (p < 0.0);
			p = Math.floor(p);
			t = 0.9 * (1.0 + y * y) * Math.exp(p * _log - lnGamma(p + 1.0) - _gam);

		} while (nextDouble() > t);

		return (int) p;
	}

	public double mean() {
		return _mean;
	}

	public double variance() {
		return _mean;
	}

	public double std() {
		return Math.sqrt(_mean);
	}

	public double pdf(int x) {
		if (x < 0)
			throw new InvalidArgumentError("The x argument must be greater than or equal to zero.");
		return Math.exp(_log * x - _mean - lnFactorial(x));
	}

	public double _cdf(int x) {
		if (x < 0)
			throw new InvalidArgumentError("The x argument must be greater than or equal to zero.");
		double sum = 0.0;
		for (int i = 0; i <= x; i++)
			sum += pdf(i);
		return sum;
	}

	public double probability(int x1, int x2) {
		if (x2 < x1)
			throw new InvalidArgumentError("The x2 must be greater than x1.");
		if (x1 < 0)
			throw new InvalidArgumentError("The x argument must be greater than or equal to zero.");
		double sum = 0.0;
		for (int i = x1; i <= x2; i++)
			sum += pdf(i);
		return sum;
	}

	public boolean equals(Object o) {
		if (!(o instanceof PoissonDistribution))
			return false;
		PoissonDistribution d = (PoissonDistribution) o;
		return d._mean == _mean;
	}

	public String toString() {
		return "PoissonDistribution(mean = " + _mean + ")";
	}

}
