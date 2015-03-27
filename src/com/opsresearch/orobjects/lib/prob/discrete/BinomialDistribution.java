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
import com.opsresearch.orobjects.lib.real.approx.ApproxException;
import com.opsresearch.orobjects.lib.real.approx.Inverse;
import com.opsresearch.orobjects.lib.real.approx.InverseI;
import com.opsresearch.orobjects.lib.real.function.FunctionI;

public class BinomialDistribution extends BinomialDistributionBase implements
		BinomialDistributionI {
	private static final long serialVersionUID = 1L;

	private InverseI _inverse = new Inverse();

	public void setInverse(InverseI inverse) {
		_inverse = inverse;
	}

	public double computeProportion(int n, int x, double cdf) {
		getIncompleteBeta().setParameters(x + 1, n - x);
		try {
			return _inverse.solve(getIncompleteBeta(), 0.0, 1.0, 1.0 - cdf);
		} catch (ApproxException e) {
			throw new ProbError("Nonlinear:" + e.getMessage());
		}
	}

	public double _cdf(int x) {
		if (x < 0)
			throw new InvalidArgumentError(
					"The x argument must be greater than or equal to zero.");
		if (x >= _n)
			return 1.0;
		getIncompleteBeta().setParameters(x + 1, _n - x);
		return 1.0 - getIncompleteBeta().evaluate(_p);
	}

	public int _inverseCdf(double probability) {
		checkProbability(probability);
		double x;
		if (probability == 1.0)
			return _n;
		try {
			x = _inverse.solve((FunctionI) this, 0.0, _n, probability);
		} catch (ApproxException e) {
			throw new ProbError(e);
		}
		int x1 = (int) x;
		int x2 = (int) Math.ceil(x);
		double d1 = Math.abs(probability - cdf(x1));
		double d2 = Math.abs(probability - cdf(x2));
		return d2 < d1 ? x2 : x1;
	}

	public double probability(int x1, int x2) {
		if (x2 < x1)
			throw new InvalidArgumentError("The x2 must be greater than x1.");
		if (x1 < 0)
			throw new InvalidArgumentError(
					"The x argument must be greater than or equal to zero.");
		if (x1 == 0)
			return cdf(x2);
		return cdf(x2) - cdf(x1 - 1);
	}

	@Override
	public double probability(int x) {
		throw new ProbError("Not implemented!");
	}

}
