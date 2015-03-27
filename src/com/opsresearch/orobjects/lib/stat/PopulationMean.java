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

import com.opsresearch.orobjects.lib.prob.continuous.StudentsTDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.StudentsTDistributionI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;


public class PopulationMean extends Stat {
	static final int _smallSampleSize = 100;

	public int sampleSize(double std, double confidenceLevel, double confidenceInterval) {
		if (std < 0.0)
			throw new StatError("The 'std' must be positive");
		if (confidenceLevel < 0.0)
			throw new StatError("The 'confidenceLevel' must be >= 0.0");
		if (confidenceLevel > 1.0)
			throw new StatError("The 'confidenceLevel' must be <= 1.0");
		double a2 = 1.0 - 0.5 * (1.0 - confidenceLevel);
		double t = getNormalDistribution().inverseCdf(a2) * std / confidenceInterval;
		return (int) Math.ceil(t * t);
	}

	private StudentsTDistributionI _studentsTDistribution = new StudentsTDistribution();

	public void setStudentsTDistribution(StudentsTDistributionI studentsTDistribution) {
		_studentsTDistribution = studentsTDistribution;
	}

	private int _n;
	private double _std, _mean, _ci, _lastCl = Double.NEGATIVE_INFINITY;

	public PopulationMean(int sampleSize, double sampleMean, double populationStd) {
		_n = sampleSize;
		if (_n < 2)
			throw new StatError("The sample size can't be less than '2'.");
		_mean = sampleMean;
		_std = populationStd;
	}

	public PopulationMean(VectorI sample) {
		_n = sample.size();
		if (_n < 2)
			throw new StatError("The sample size can't be less than '2'.");
		_mean = sample.sum() / _n;
		_std = Math.sqrt(sample.sumOfSquaredDifferences(_mean) / (_n - 1.0));
	}

	public double size() {
		return _n;
	}

	public double getMean() {
		return _mean;
	}

	public double getStd() {
		return _std;
	}

	private void getCi(double cl) {
		if (_n <= _smallSampleSize) {
			_studentsTDistribution.setParamater(_n - 1);
			_ci = _studentsTDistribution.inverseCdf(1.0 - 0.5 * (1.0 - cl)) * _std / Math.sqrt(_n);
		} else {
			_ci = getNormalDistribution().inverseCdf(1.0 - 0.5 * (1.0 - cl)) * _std / Math.sqrt(_n);
		}
		_lastCl = cl;
	}

	public double getLowerBound(double confidenceLevel) {
		if (_lastCl != confidenceLevel)
			getCi(confidenceLevel);
		return _mean - _ci;
	}

	public double getUpperBound(double confidenceLevel) {
		if (_lastCl != confidenceLevel)
			getCi(confidenceLevel);
		return _mean + _ci;
	}

}
