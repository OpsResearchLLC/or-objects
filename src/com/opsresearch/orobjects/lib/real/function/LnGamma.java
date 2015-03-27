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
import com.opsresearch.orobjects.lib.real.Constants;

public class LnGamma extends Function implements LnGammaI, Serializable {

	private static final long serialVersionUID = 1L;

	@Override
	public double evaluate(double x) {
		if (x <= 0.0)
			throw new InvalidArgumentError("The argument to 'lnGama' must be greater than zero.");
		double z55 = x + 5.5;
		return Math
				.log((1.000000000190015 + 76.18009172947146 / (x + 1.0) - 86.50532032941677 / (x + 2.0)
						+ 24.01409824083091 / (x + 3.0) - 1.231739572450155 / (x + 4.0) + 1.208650973866179E-3
						/ (x + 5.0) - 5.395239384953000E-6 / (x + 6.0))
						* Constants.sqrtTwoPi / x)
				- z55 + Math.log(z55) * (x + 0.5);
	}

}
