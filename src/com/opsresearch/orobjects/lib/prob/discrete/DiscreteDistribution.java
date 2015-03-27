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
import com.opsresearch.orobjects.lib.prob.Distribution;

public abstract class DiscreteDistribution extends Distribution implements DiscreteDistributionI {

	private static final long serialVersionUID = 1L;

	public double getRandomScaler() {
		return (double) getRandomInteger();
	}

	public int[] getRandomArray(int size) {
		int[] a = new int[size];
		for (int i = 0; i < size; i++)
			a[i] = getRandomInteger();
		return a;
	}

	public int[][] getRandomArray(int sizeOfRows, int sizeOfColumns) {
		int[][] a = new int[sizeOfRows][sizeOfColumns];
		for (int i = 0; i < sizeOfRows; i++) {
			int[] row = a[i];
			for (int j = 0; j < sizeOfColumns; j++)
				row[j] = getRandomInteger();
		}
		return a;
	}

	public double pdf(double x) {
		return pdf((int) x);
	}

	public double cdf(double x) {
		return cdf((int) x);
	}

	public double probability(double x) {
		return probability((int) x);
	}

	public double probability(double x1, double x2) {
		if (x2 < x1)
			throw new InvalidArgumentError("X2 must be greater than or equal to x1.");
		return probability((int) x1, (int) x2);
	}
	
	private int _variableMax = Integer.MAX_VALUE;
	private int _variableMin = Integer.MIN_VALUE;
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
	public int getVariableMin(){
		return _variableMin;
	}
	
	@Override
	public int getVariableMax(){
		return _variableMax;
	}
	
	public void setCdfBounds(double min, double max) {
		_cdfMin = min;
		_cdfMax = max;
	}
	
	public void setVariableBounds(int min, int max) {
		_variableMin = min;
		_variableMax = max;
		_cdfMin = _cdf(min);
		_cdfMax = _cdf(max);
	}
	
	public abstract double _cdf(int x);

	public final double cdf(int x) {
		if (x < _variableMin)
			return _cdfMin;
		if (x > _variableMax)
			return _cdfMax;
		return _cdf(x);
	}

	public abstract int _inverseCdf(double probability);

	public final int inverseCdf(double probability) {
		checkProbability(probability);
		if (probability <= _cdfMin)
			return _variableMin;
		if (probability >= _cdfMax)
			return _variableMax;
		return _inverseCdf(probability);
	}

}
