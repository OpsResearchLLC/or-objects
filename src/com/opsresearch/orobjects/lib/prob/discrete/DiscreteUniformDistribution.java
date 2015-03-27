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
import com.opsresearch.orobjects.lib.prob.ProbError;

public class DiscreteUniformDistribution extends DiscreteDistribution implements DiscreteUniformDistributionI {
	private static final long serialVersionUID = 1L;
	private int _size, _inc, _min, _max;

	public DiscreteUniformDistribution(){
	}

	public DiscreteUniformDistribution(int min, int increment, int size){
		setParameters(min, increment, size);
	}
	
	public void setParameters(int min, int increment, int size) {
		if (increment == 0)
			throw new InvalidArgumentError("The increment can not be zero.");
		if (size < 2)
			throw new InvalidArgumentError("The size must be greater than one.");
		
		setValid(false);
		_min = min;
		_max = min + increment * (size - 1);
		_size = size;
		_inc = increment;
		setVariableBounds(_min, _max);
		setCdfBounds(0.0, 1.0);
		setValid(true);
	}

	public int getRandomInteger() {
		int n = (int) (_size * nextDouble());
		if (n == _size)
			n--;
		return _min + n * _inc;
	}

	public double mean() {
		return _min + (_size - 1) * _inc / 2.0;
	}

	public double variance() {
		double d = _size * _inc;
		return ((d * d) - _inc) / 12.0;
	}

	public double std() {
		double d = _size * _inc;
		return Math.sqrt(((d * d) - _inc) / 12.0);
	}

	public double pdf(int x) {
		if (x < _min || x > _max)
			return 0.0;
		if ((x - _min + 1) % _inc == 0)
			return 1.0 / _size;
		return 0.0;
	}

	public double _cdf(int x) {
		if (x < _min)
			return 0.0;
		if (x >= _max)
			return 1.0;
		return (((x - _min) / _inc + 1) * _inc) / (double) _size;
	}

	public double probability(int x1, int x2) {
		if (x2 < x1)
			throw new InvalidArgumentError("X2 must be greater than or equal to x1.");
		if (x1 < _min)
			x1 = _min;
		if (x2 > _max)
			x2 = _max;
		if (x1 == x2)
			return pdf(x1);
		if (x1 == _min)
			return cdf(x2);
		return pdf(x1) + (((x2 - x1) / _inc) * _inc) / (double) _size;
	}

	public boolean equals(Object o) {
		if (!(o instanceof DiscreteUniformDistribution))
			return false;
		DiscreteUniformDistribution d = (DiscreteUniformDistribution) o;
		return d._min == _min && d._inc == _inc && d._size == _size;
	}

	public String toString() {
		return "DiscreteUniformDistribution(min = " + _min + ", inc = " + _inc + ", size = " + _size + ")";
	}

	@Override
	public int _inverseCdf(double probability) {
		throw new ProbError("Not implemented!");
	}

	@Override
	public double probability(int x) {
		throw new ProbError("Not implemented!");
	}

}
