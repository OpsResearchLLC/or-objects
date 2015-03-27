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

public class Simpsons extends Approximation implements IntegrationI, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public Simpsons() {
		setMaxIterations(64);
		setEpsilon(1.0E-12);
	}
	
	public Simpsons(int maxIterations, double epsilon) {
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
		double currentSimpsons, current;

		double previous = current = 0.5 * range * (function.evaluate(low) + function.evaluate(high));
		double previousSimpsons = currentSimpsons = (4.0 * current - previous) / 3.0;
		int nDelta = 1;
		for (int i = 0; i < _maxIterations; i++) {
			double delta = range / nDelta;
			double x = low + (0.5 * delta);
			double sum = 0.0;
			for (int j = 0; j < nDelta; j++) {
				sum += function.evaluate(x);
				x += delta;
			}
			current = 0.5 * (current + (range * sum / nDelta));
			currentSimpsons = (4.0 * current - previous) / 3.0;
			if (near(currentSimpsons, previousSimpsons))
				return currentSimpsons;
			previousSimpsons = currentSimpsons;
			previous = current;
			nDelta <<= 1;
		}

		throw new AccuracyException("Can't find the answer to the specified accuracy");
	}
}
