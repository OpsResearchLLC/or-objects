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

public class UniformDistribution extends ContinuousDistribution implements UniformDistributionI {
	private static final long serialVersionUID = 1L;
	private double _min = 0.0;
	private double _max = 1.0;
	private double _scale;
	private double _offset;

	public UniformDistribution() {
	}

	public UniformDistribution(double min, double max) {
		setParameters(min, max);
	}

	public void setParameters(double min, double max) {
		if (max <= min)
			throw new InvalidArgumentError("Max must be greater than min.");

		setValid(false);
		_min = min;
		_max = max;
		_scale = _max - _min;
		_offset = _min;
		setVariableBounds(_min, _max);
		setCdfBounds(0.0, 1.0);
		setValid(true);
	}

	public double getRandomScaler() {
		return _offset + _scale * nextDouble();
	}

	public double mean() {
		return (_max + _min) / 2.0;
	}

	public double variance() {
		double d = _max - _min;
		return (d * d) / 12.0;
	}

	public double std() {
		double d = _max - _min;
		return Math.sqrt((d * d) / 12.0);
	}

	public double pdf(double x) {
		if (x <= _min || x >= _max)
			return 0.0;
		return 1.0 / (_max - _min);
	}

	public double _cdf(double x) {
		if (x <= _min)
			return 0.0;
		if (x >= _max)
			return 1.0;
		return (x - _min) / (_max - _min);
	}

	public double _inverseCdf(double probability) {
		return _min + probability * (_max - _min);
	}

	protected double _probability(double x1, double x2) {
		if (x1 < _min)
			x1 = _min;
		if (x2 > _max)
			x2 = _max;
		return (x2 - x1) / (_max - _min);
	}

	public boolean equals(Object o) {
		if (!(o instanceof UniformDistribution))
			return false;
		UniformDistribution d = (UniformDistribution) o;
		return d._min == _min && d._max == _max;
	}

	public String toString() {
		return "UniformDistribution(min = " + _min + ", max = " + _max + ")";
	}

}
