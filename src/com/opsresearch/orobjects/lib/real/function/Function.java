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

import com.opsresearch.orobjects.lib.real.approx.ApproxError;

public class Function implements FunctionI {

	boolean _valid = false;

	public boolean setValid(boolean valid) {
		return _valid = valid;
	}

	public boolean isValid() {
		return _valid;
	}

	protected void checkValid() {
		if (!_valid)
			throw new ApproxError(
					"The distributionis invalid, possibly the parameters have not been set.");
	}

	private static Functions _functions = new Functions();

	public static Functions getFunctions() {
		return _functions;
	}

	public static void setFunctions(Functions functions) {
		_functions = functions;
	}

	public static double lnGamma(double x) {
		return _functions.lnGamma(x);
	}

	public static double lnFactorial(int x) {
		return _functions.lnFactorial(x);
	}

	public static double binomialCoefficient(int n, int k) {
		return _functions.binomialCoefficient(n, k);
	}

	public static double incompleteGamma(double a, double x) {
		return _functions.incompleteGamma(a, x);
	}

	public static double incompleteBeta(double a, double b, double x) {
		return _functions.incompleteBeta(a, b, x);
	}

	public double evaluate(double x) {
		return 0;
	}
}
