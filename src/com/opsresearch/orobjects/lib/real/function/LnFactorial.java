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


package com.opsresearch.orobjects.lib.real.function;

import java.io.Serializable;

import com.opsresearch.orobjects.lib.InvalidArgumentError;

public class LnFactorial implements LnFactorialI, Serializable {

	private static final long serialVersionUID = 1L;

	private static final int _cacheSize = 150;
	private static double[] _cache = new double[_cacheSize];
	private static LnGammaI _lnGamma = new LnGamma();

	public static void setLnGamma(LnGammaI lnGamma) {
		_lnGamma = lnGamma;
	}

	@Override
	public double function(int n) {

		if (n < 0)
			throw new InvalidArgumentError("Can't compute the factorial for a negative number.");
		if (n <= 1)
			return 0.0;

		if (n < _cacheSize) {
			double cached = _cache[n];
			if (cached == 0)
				return _cache[n] = _lnGamma.evaluate(n + 1);
			else
				return cached;
		}
		return _lnGamma.evaluate(n + 1);
	}
}
