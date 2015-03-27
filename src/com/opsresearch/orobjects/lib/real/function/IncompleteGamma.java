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

import com.opsresearch.orobjects.lib.real.approx.ApproxSetI;
import com.opsresearch.orobjects.lib.real.approx.Approximation;

public class IncompleteGamma extends Function implements IncompleteGammaI, ApproxSetI, Serializable {
	private static final long serialVersionUID = 1L;

	private Approximation _approximation = new Approximation();
	public void setValueRange(double a, double b) {
		_approximation.setValueRange(a, b);
	}

	private double _a, _1a, _a1, _ainv, _alng;
	private FunctionI _lnGamma = new LnGamma();
	private boolean _isCached = false;

	public IncompleteGamma() {
		setEpsilon(1.0e-7);
		setMaxIterations(100);
	}

	public IncompleteGamma(double a) {
		setEpsilon(1.0e-7);
		setMaxIterations(100);
		setParameter(a);
	}

	public void setLnGamma(FunctionI lnGamma) {
		_lnGamma = lnGamma;
	}

	public void setParameter(double a) {
		if (a <= 0.0)
			throw new FunctionError("'a' must be greater than '0.0'");
		if (_isCached && _a == a)
			return;
		_isCached = true;
		_a = a;
		_a1 = a + 1.0;
		_1a = 1.0 - a;
		_ainv = 1.0 / a;
		_alng = _lnGamma.evaluate(a);
	}

	public double evaluate(double x) throws FunctionError {
		if (x < 0.0)
			throw new FunctionError("'x' can't be less than '0'.");
		if (x < _a1)
			return f1(x);
		else
			return 1.0 - f2(x);
	}

	private double f1(double x) {
		if (x == 0.0)
			return 0.0;
		double xln = Math.log(x);
		double ap = _a;
		double sum = _ainv;
		double del = _ainv;
		for (int i = 1; i <= _approximation.getMaxIterations(); i++) {
			++ap;
			del *= x / ap;
			sum += del;
			if (Math.abs(del) < Math.abs(sum) * _approximation.getEpsilon()) {
				return sum * Math.exp(-x + _a * xln - _alng);
			}
		}
		throw new FunctionError("Can't find igamma to within epsilon");
	}

	private double f2(double x) {
		if (x == 0.0)
			return 1.0;
		double xln = Math.log(x);
		double b = x + _1a;
		double c = Double.MAX_VALUE;
		double d = 1.0 / b;
		double h = d;
		for (int i = 1; i <= _approximation.getMaxIterations(); i++) {
			double an = -i * (i - _a);
			b += 2.0;
			d = _approximation.notZero(1.0 / (an * d + b));
			c = _approximation.notZero(b + an / c);
			double del = d * c;
			h *= del;
			if (Math.abs(del - 1.0) < _approximation.getEpsilon()) {
				return h * Math.exp(-x + _a * xln - _alng);
			}
		}
		throw new FunctionError("Can't find igamma to within epsilon");
	}

	@Override
	public void setMaxIterations(int maxIterations) {
		_approximation.setMaxIterations(maxIterations);
	}

	@Override
	public void setEpsilon(double epsilon) {
		_approximation.setEpsilon(epsilon);

	}

	@Override
	public void setZero(double zero) {
		_approximation.setZero(zero);
	}

}
