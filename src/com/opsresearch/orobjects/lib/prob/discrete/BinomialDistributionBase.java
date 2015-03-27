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

public abstract class BinomialDistributionBase extends DiscreteDistribution {

	private static final long serialVersionUID = 1L;
	protected int _n;
	protected double _p;
	private double _q;
	private double _lnp;
	private double _lnq;
	private double _lnfn;
	private double _gp;
	private double _gq;
	private double _gexp;
	private double _gmean;
	private double _gamma;
	private double _glogp;
	private double _glogq;

	public void setParameters(double p, int n) {
		checkProbability(p);
		if (n < 0)
			throw new InvalidArgumentError("The parameter 'n' must be greater than zero.");

		setValid(false);
		_n = n;
		_p = p;
		_q = 1.0 - p;
		_lnp = Math.log(_p);
		_lnq = Math.log(_q);
		_lnfn = lnFactorial(_n);
		_gp = Math.min(_p, _q);
		_gq = 1.0 - _gp;
		_gmean = _gp * _n;
		_gexp = Math.exp(-_gmean);
		_glogp = Math.log(_gp);
		_glogq = Math.log(1.0 - _gp);
		_gamma = lnGamma(_n + 1);
		setVariableBounds(0, n);
		setCdfBounds(0.0, 1.0);
		setValid(true);
	}

	public int getRandomInteger() {
		int cnt = 0;
		double t;

		if (_n < 25) {
			for (int i = 0; i < _n; i++)
				if (nextDouble() < _gp)
					cnt++;
		} else if (_gmean < 1.0) {
			int j;
			t = 1.0;
			for (j = 0; j <= _n; j++) {
				t *= nextDouble();
				if (t < _gexp)
					break;
			}
			cnt = (j <= _n ? j : _n);
		} else {
			double dn = _n;
			double n1 = dn + 1.0;
			double y, em, sqrt = Math.sqrt(2.0 * _gmean * _gq);
			do {
				do {
					em = sqrt * (y = Math.tan(Constants.pi * nextDouble())) + _gmean;
				} while (em < 0.0 || em >= n1);
				em = Math.floor(em);
				t = 1.2
						* sqrt
						* (1.0 + y * y)
						* Math.exp(_gamma - lnGamma(em + 1.0) - lnGamma(dn - em + 1.0) + em * _glogp + (dn - em)
								* _glogq);
			} while (nextDouble() > t);
			cnt = (int) em;
		}

		return (_gp != _p ? _n - cnt : cnt);
	}

	public double mean() {
		return _n * _p;
	}

	public double variance() {
		return _n * _p * _q;
	}

	public double std() {
		return Math.sqrt(_n * _p * _q);
	}

	public double pdf(int x) {
		if (x < 0)
			throw new InvalidArgumentError("The x argument can't be less than zero.");
		int nx = _n - x;
		return Math.exp(_lnfn - lnFactorial(x) - lnFactorial(nx) + _lnp * x + _lnq * nx);
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
		if (!(o instanceof BinomialDistribution))
			return false;
		BinomialDistribution d = (BinomialDistribution) o;
		return d._p == _p && d._n == _n;
	}

	public String toString() {
		return "BinomialDistribution(p = " + _p + ", n = " + _n + ")";
	}

}
