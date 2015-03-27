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

import com.opsresearch.orobjects.lib.real.matrix.VectorI;


public class PopulationProportion extends Stat {

	private int _n, _cnt;
	private double _p, _std, _var;

	public PopulationProportion(int sampleSize, int successCount) {
		_n = sampleSize;
		_cnt = successCount;
		_p = (double) _cnt / (double) _n;
		_var = _p * (1.0 - _p) / (double) _n;
		_std = Math.sqrt(_var);
	}

	public PopulationProportion(boolean[] sample) {
		_n = sample.length;
		_cnt = 0;
		for (int i = 0; i < _n; i++)
			if (sample[i])
				_cnt++;
		_p = (double) _cnt / (double) _n;
		_var = _p * (1.0 - _p) / (double) _n;
		_std = Math.sqrt(_var);
	}

	public PopulationProportion(VectorI sample) {
		_n = sample.size();
		_cnt = 0;
		for (int i = 0; i < _n; i++)
			if (sample.elementAt(i) > 0.0)
				_cnt++;
		_p = (double) _cnt / (double) _n;
		_var = _p * (1.0 - _p) / (double) _n;
		_std = Math.sqrt(_var);
	}

	public PopulationProportion(VectorI sample, double minSuccess) {
		_n = sample.size();
		_cnt = 0;
		for (int i = 0; i < _n; i++)
			if (sample.elementAt(i) >= minSuccess)
				_cnt++;
		_p = (double) _cnt / (double) _n;
		_var = _p * (1.0 - _p) / (double) _n;
		_std = Math.sqrt(_var);
	}

	public PopulationProportion(int[] sample) {
		_n = sample.length;
		_cnt = 0;
		for (int i = 0; i < _n; i++)
			if (sample[i] > 0)
				_cnt++;
		_p = (double) _cnt / (double) _n;
		_var = _p * (1.0 - _p) / (double) _n;
		_std = Math.sqrt(_var);
	}

	public PopulationProportion(int[] sample, int minSuccess) {
		_n = sample.length;
		_cnt = 0;
		for (int i = 0; i < _n; i++)
			if (sample[i] >= minSuccess)
				_cnt++;
		_p = (double) _cnt / (double) _n;
		_var = _p * (1.0 - _p) / (double) _n;
		_std = Math.sqrt(_var);
	}

	public double size() {
		return _n;
	}

	public double getProportion() {
		return _p;
	}

	public double getVariance() {
		return _var;
	}

	public double getSandardDeviation() {
		return _std;
	}

	public double getLowerBound(double confidenceLevel) {
		if (_cnt == 0)
			return 0.0;
		return getBinomialDistribution().computeProportion(_n, _cnt - 1, 1.0 - 0.5 * (1.0 - confidenceLevel));
	}

	public double getUpperBound(double confidenceLevel) {
		if (_cnt == _n)
			return 1.0;
		return getBinomialDistribution().computeProportion(_n, _cnt, 0.5 * (1.0 - confidenceLevel));
	}

}
