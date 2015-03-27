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
import com.opsresearch.orobjects.lib.prob.Distribution;

public abstract class ContinuousDistribution extends Distribution implements ContinuousDistributionI {

	private static final long serialVersionUID = 1L;

	public ContinuousDistribution() {
	}

	public ContinuousDistribution(long seed) {
		super(seed);
	}

	public double getRandomScaler() {
		return getRandomDouble();
	}

	public double getRandomDouble() {
		return inverseCdf(nextDouble());
	}

	public double[] getRandomArray(int size) {
		double[] a = new double[size];
		for (int i = 0; i < size; i++)
			a[i] = getRandomDouble();
		return a;
	}

	public double[][] getRandomArray(int sizeOfRows, int sizeOfColumns) {
		double[][] a = new double[sizeOfRows][sizeOfColumns];
		for (int i = 0; i < sizeOfRows; i++) {
			double[] row = a[i];
			for (int j = 0; j < sizeOfColumns; j++)
				row[j] = getRandomDouble();
		}
		return a;
	}

	public final double probability(double x1, double x2) {
		if (x2 < x1) {
			double tmp = x1;
			x1 = x2;
			x2 = tmp;
		}
		if (x1 == x2)
			return 0.0;
		if (x2 <= _variableMin)
			return 0.0;
		if (x1 >= _variableMax)
			return 0.0;
		return _probability(x1, x2);
	}

	protected double _probability(double x1, double x2) {
		return cdf(x2) - cdf(x1);
	}

	private double _variableMax = Double.MAX_VALUE;
	private double _variableMin = Double.MIN_VALUE;
	private double _cdfMax = Double.MAX_VALUE;
	private double _cdfMin = Double.MIN_VALUE;

	@Override
	public double getCdfMin(){
		return _cdfMin;
	}
	
	@Override
	public double getCdfMax(){
		return _cdfMax;
	}
	
	@Override
	public double getVariableMin(){
		return _variableMin;
	}
	
	@Override
	public double getVariableMax(){
		return _variableMax;
	}
	
	public void setCdfBounds(double min, double max) {
		_cdfMin = min;
		_cdfMax = max;
	}

	public void setVariableBounds(double min, double max) {
		_variableMin = min;
		_variableMax = max;
		_cdfMin = _cdf(min);
		_cdfMax = _cdf(max);
	}

	protected abstract double _cdf(double x);

	public final double cdf(double x) {
		if (x < _variableMin)
			return _cdfMin;
		if (x > _variableMax)
			return _cdfMax;
		return _cdf(x);
	}

	protected abstract double _inverseCdf(double probability);

	public final double inverseCdf(double probability) {
		checkProbability(probability);
		if (probability <= _cdfMin)
			return _variableMin;
		if (probability >= _cdfMax)
			return _variableMax;
		return _inverseCdf(probability);
	}

}
