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

public class PoissonDistribution extends PoissonDistributionBase implements PoissonDistributionI {
	private static final long serialVersionUID = 1L;

	public PoissonDistribution() {
	}

	public PoissonDistribution(double mean) {
		setParameter(mean);
	}

	public double _cdf(int x) {
		if (x < 0)
			throw new InvalidArgumentError("The x argument must be greater than or equal to zero.");
		getIncompleteGamma().setParameter(x + 1);
		return 1.0 - getIncompleteGamma().evaluate(_mean);
	}

	public double probability(int x1, int x2) {
		if (x2 < x1)
			throw new InvalidArgumentError("The x2 must be greater than x1.");
		if (x1 < 0)
			throw new InvalidArgumentError("The x argument must be greater than or equal to zero.");
		if (x1 == 0)
			return cdf(x2);
		return cdf(x2) - cdf(x1 - 1);
	}

	@Override
	public double probability(int x) {
		throw new ProbError("Not implemented!");
	}

	@Override
	public int _inverseCdf(double probability) {
		throw new ProbError("Not implemented!");
	}

}
