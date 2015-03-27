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

package com.opsresearch.orobjects.lib.stat;

import com.opsresearch.orobjects.lib.prob.ProbError;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;
import com.opsresearch.orobjects.lib.util.QuickSort;


public class PopulationMedian extends Stat {

	private int _x, _s1;
	private double _median, _lastConf = Double.NEGATIVE_INFINITY;
	private double[] _sample;

	public PopulationMedian(VectorI sample) {
		_sample = sample.getArray();
		getBinomialDistribution().setParameters(0.5, _sample.length);
		new QuickSort<double[]>().sort(_sample);
		if ((_sample.length & 1) == 1) {
			_s1 = _sample.length / 2;
			_median = _sample[_s1];
		} else {
			_s1 = (_sample.length / 2) - 1;
			_median = 0.5 * (_sample[_s1] + _sample[_s1 + 1]);
		}
	}

	public double size() {
		return _sample.length;
	}

	public double getMedian() {
		return _median;
	}

	private void getInv(double confidenceLevel) {
		int x = getBinomialDistribution().inverseCdf(0.5 * (1.0 - confidenceLevel));
		if (x > _s1)
			throw new ProbError("The confidence level is too low.");
		_lastConf = confidenceLevel;
		_x = x;
	}

	public double getLowerBound(double confidenceLevel) {
		if (_lastConf != confidenceLevel)
			getInv(confidenceLevel);
		return _sample[_x];
	}

	public double getUpperBound(double confidenceLevel) {
		if (_lastConf != confidenceLevel)
			getInv(confidenceLevel);
		return _sample[_sample.length - _x - 1];
	}

}
