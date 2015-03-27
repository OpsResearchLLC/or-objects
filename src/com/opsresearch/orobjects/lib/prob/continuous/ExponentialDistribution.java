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

public class ExponentialDistribution extends ContinuousDistribution implements ExponentialDistributionI {
	private static final long serialVersionUID = 1L;
	private double _mean = 1.0;

	public ExponentialDistribution() {
	}

	public ExponentialDistribution(double mean) {
		setParameter(mean);
	}

	public void setParameter(double mean) {
		if (mean <= 0.0)
			throw new InvalidArgumentError("The mean must be greater than zero.");

		_mean = mean;
		setVariableBounds(0.0, Double.MAX_VALUE);
		setValid(true);
	}

	public double mean() {
		return _mean;
	}

	public double variance() {
		return _mean * _mean;
	}

	public double std() {
		return _mean;
	}

	public double pdf(double x) {
		if (x < 0.0)
			return 0.0;
		return Math.exp(-x / _mean) / _mean;
	}

	public double _cdf(double x) {
		return 1.0 - Math.exp(-x / _mean);
	}

	public double getRandomDouble() {
		return -_mean * Math.log(nextDouble());
	}

	public double _inverseCdf(double probability) {
		return -_mean * Math.log(1.0 - probability);
	}

	public boolean equals(Object o) {
		if (!(o instanceof ExponentialDistribution))
			return false;
		ExponentialDistribution d = (ExponentialDistribution) o;
		return d._mean == _mean;
	}

	public String toString() {
		return "ExponentialDistribution(mean = " + _mean + ")";
	}

}
