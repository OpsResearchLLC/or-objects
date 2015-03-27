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

public class BinomialCoefficient implements BinomialCoefficientI, Serializable {

	private static final long serialVersionUID = 1L;

	private LnFactorialI _lnFactorial = new LnFactorial();
	private double _cache;
	private int _cacheN, _cacheK;
	private boolean _isCached = false;

	public void setLnFactorial(LnFactorialI lnFactorial) {
		_lnFactorial = lnFactorial;
	}

	@Override
	public double function(int n, int k) {
		if (n < k)
			throw new FunctionError("'k' can't be greater than 'n'");
		if (k < 0)
			throw new FunctionError("'k' can't be less than '0'");

		if (!_isCached || _cacheN != n || _cacheK != k) {
			_isCached = true;
			_cacheN = n;
			_cacheK = k;
			_cache = Math.floor(0.5 + Math.exp(_lnFactorial.function(n) - _lnFactorial.function(k)
					- _lnFactorial.function(n - k)));
		}
		return _cache;
	}

}
