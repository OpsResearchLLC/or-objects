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

public class Trapezoidal extends Approximation implements IntegrationI, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public Trapezoidal() {
		setMaxIterations(16);
		setEpsilon(1.0E-8);
	}
	
	public Trapezoidal(int maxIterations, double epsilon) {
		setMaxIterations(maxIterations);
		setEpsilon(epsilon);
	}


	public double solve(FunctionI function, double bound1, double bound2) throws AccuracyException {

		if (bound1 == bound2)
			return 0.0;

		double low, high;
		if (bound2 < bound1) {
			high = bound1;
			low = bound2;
		} else {
			low = bound1;
			high = bound2;
		}

		double range = high - low;
		double current;
		double previous = current = 0.5 * range * (function.evaluate(low) + function.evaluate(high));
		int nDelta = 1;
		for (int i = 0; i < _maxIterations; i++) {
			double delta = range / nDelta;
			double x = low + (0.5 * delta);
			double sum = 0.0;
			for (int j = 0; j < nDelta; j++) {
				sum += function.evaluate(x);
				x += delta;
			}
			current = 0.5 * (current + (delta * sum / nDelta));
			if (near(current, previous))
				return current;
			previous = current;
			nDelta <<= 1;
		}

		throw new AccuracyException("Can't find the answer to the specified accuracy");
	}
}
