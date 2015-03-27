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


package com.opsresearch.orobjects.lib.real.approx;

import com.opsresearch.orobjects.lib.real.function.FunctionI;

public class Bisection extends Approximation implements InverseI, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public Bisection() {
		setMaxIterations(40);
		setEpsilon(1.0e-7);
	}

	public Bisection(int maxIterations, double epsilon) {
		setMaxIterations(maxIterations);
		setEpsilon(epsilon);
	}

	public double solve(FunctionI function, double x1, double x2, double y) throws ApproxException, AccuracyException {
		double y1 = function.evaluate(x1);
		double y2 = function.evaluate(x2);
		if (y1 == y)
			return x1;
		if (y2 == y)
			return x2;
		double xm, dx, x;
		if (y1 < y) {
			if (y2 < y)
				throw new ApproxException("The 'y' value is above the bracket.");
			dx = x2 - x1;
			x = x1;
		} else {
			if (y2 > y)
				throw new ApproxException("The 'y' value is above the bracket.");
			dx = x1 - x2;
			x = x2;
		}
		for (int i = 0; i < _maxIterations; i++) {
			double ym = function.evaluate(xm = x + (dx *= 0.5));
			if (ym <= y)
				x = xm;
			if (near(0, dx))
				return x;
		}

		throw new AccuracyException("Can't find y to specified accuracy");
	}
}
