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


package com.opsresearch.orobjects.lib.stat.model;

import com.opsresearch.orobjects.lib.real.matrix.ArrayVector;
import com.opsresearch.orobjects.lib.real.matrix.ColumnMajorMatrix;
import com.opsresearch.orobjects.lib.real.matrix.ContiguousMatrix;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;

public class LinearCorrelation extends Model implements LinearCorrelationI {

	private int _numVariables, _numSamples;
	private MatrixI _samples;
	private double _confidence, _zConfidence;
	private double _lowerConfidence, _upperConfidence;
	private ArrayVector _std, _mean, _sumOfSquares;
	private ContiguousMatrix _lower, _upper, _sumOfProducts, _covariance,
			_correlation;

	public void setSamples(MatrixI samples) {
		_numVariables = samples.sizeOfColumns();
		_numSamples = samples.sizeOfRows();
		_samples = samples;
		_confidence = -1.0;
		_std = _mean = _sumOfSquares = null;
		_lower = _upper = _sumOfProducts = _covariance = _correlation = null;
	}

	public ArrayVector getMean() {
		if (_mean != null)
			return _mean;
		_mean = new ArrayVector(_numVariables);
		for (int i = 0; i < _numVariables; i++) {
			double sum = 0;
			for (int k = 0; k < _numSamples; k++) {
				sum += _samples.elementAt(k, i);
			}
			_mean.setElementAt(i, sum / _numSamples);
		}
		return _mean;
	}

	public ArrayVector getStd() {
		if (_std != null)
			return _std;
		getMean();
		getSumOfSquares();
		_std = new ArrayVector(_numVariables);
		double n1 = 1.0 / (_numSamples - 1.0);
		for (int i = 0; i < _numVariables; i++) {
			double mean = _mean.elementAt(i);
			_std.setElementAt(
					i,
					Math.sqrt(n1
							* (_sumOfSquares.elementAt(i) - _numSamples * mean
									* mean)));
		}
		return _std;
	}

	public ArrayVector getSumOfSquares() {
		if (_sumOfSquares != null)
			return _sumOfSquares;
		_sumOfSquares = new ArrayVector(_numVariables);
		for (int i = 0; i < _numVariables; i++) {
			double sum = 0;
			for (int k = 0; k < _numSamples; k++) {
				double d = _samples.elementAt(k, i);
				sum += d * d;
			}
			_sumOfSquares.setElementAt(i, sum);
		}
		return _sumOfSquares;
	}

	private double getZ(double confidenceLevel) {
		if (_numSamples < 4)
			throw new Error(
					"Can't get confidence interval for fewer than 4 samples.");
		if (_confidence == confidenceLevel)
			return _zConfidence;
		_confidence = confidenceLevel;
		return _zConfidence = getNormalDistribution().inverseCdf(
				1.0 - 0.5 * (1.0 - confidenceLevel))
				/ Math.sqrt(_numSamples - 3);
	}

	public ContiguousMatrix getSumOfProducts() {
		if (_sumOfProducts != null)
			return _sumOfProducts;
		getSumOfSquares();
		_sumOfProducts = new ColumnMajorMatrix(_numVariables, _numVariables);
		for (int i = 0; i < _numVariables; i++) {
			_sumOfProducts.setElementAt(i, i, _sumOfSquares.elementAt(i));
			for (int j = 0; j < i; j++) {
				double sum = 0;
				for (int k = 0; k < _numSamples; k++) {
					sum += _samples.elementAt(k, i) * _samples.elementAt(k, j);
				}
				_sumOfProducts.setElementAt(i, j, sum);
				_sumOfProducts.setElementAt(j, i, sum);
			}
		}
		return _sumOfProducts;
	}

	public ContiguousMatrix getCovariance() {
		if (_covariance != null)
			return _covariance;
		getMean();
		getStd();
		getSumOfProducts();
		_covariance = new ColumnMajorMatrix(_numVariables, _numVariables);
		double n1 = 1.0 / (_numSamples - 1.0);
		for (int i = 0; i < _numVariables; i++) {
			double std = _std.elementAt(i);
			_covariance.setElementAt(i, i, std * std);
			for (int j = 0; j < i; j++) {
				double cov = n1
						* (_sumOfProducts.elementAt(i, j) - _numSamples
								* _mean.elementAt(i) * _mean.elementAt(j));
				_covariance.setElementAt(i, j, cov);
				_covariance.setElementAt(j, i, cov);
			}
		}
		return _covariance;
	}

	public ContiguousMatrix getCorrelation() {
		if (_correlation != null)
			return _correlation;
		getStd();
		getCovariance();
		_correlation = new ColumnMajorMatrix(_numVariables, _numVariables);
		for (int i = 0; i < _numVariables; i++) {
			_correlation.setElementAt(i, i, 1.0);
			for (int j = 0; j < i; j++) {
				double cor = (_covariance.elementAt(i, j) / _std.elementAt(i))
						/ _std.elementAt(j);
				_correlation.setElementAt(i, j, cor);
				_correlation.setElementAt(j, i, cor);
			}
		}
		return _correlation;
	}

	public ContiguousMatrix getLowerBound(double confidenceLevel) {
		if (_lower != null && _lowerConfidence == confidenceLevel)
			return _lower;
		getCorrelation();
		_lowerConfidence = confidenceLevel;
		if (_lower == null)
			_lower = new ColumnMajorMatrix(_numVariables, _numVariables);
		double z = getZ(confidenceLevel);
		for (int i = 0; i < _numVariables; i++) {
			_lower.setElementAt(i, i, _correlation.elementAt(i, i));
			for (int j = 0; j < i; j++) {
				double r = _correlation.elementAt(i, j);
				double w = 0.5 * Math.log((1.0 + r) / (1.0 - r)) - z;
				double e2w = Math.exp(2.0 * w);
				double d = (e2w - 1.0) / (e2w + 1.0);
				_lower.setElementAt(i, j, d);
				_lower.setElementAt(j, i, d);
			}
		}
		return _lower;
	}

	public ContiguousMatrix getUpperBound(double confidenceLevel) {
		if (_upper != null && _upperConfidence == confidenceLevel)
			return _upper;
		getCorrelation();
		_upperConfidence = confidenceLevel;
		if (_upper == null)
			_upper = new ColumnMajorMatrix(_numVariables, _numVariables);
		double z = getZ(confidenceLevel);
		for (int i = 0; i < _numVariables; i++) {
			_upper.setElementAt(i, i, _correlation.elementAt(i, i));
			for (int j = 0; j < i; j++) {
				double r = _correlation.elementAt(i, j);
				double w = 0.5 * Math.log((1.0 + r) / (1.0 - r)) + z;
				double e2w = Math.exp(2.0 * w);
				double d = (e2w - 1.0) / (e2w + 1.0);
				_upper.setElementAt(i, j, d);
				_upper.setElementAt(j, i, d);
			}
		}
		return _upper;
	}

}
