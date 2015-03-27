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


public class IncompleteBeta extends Function implements ApproxSetI, IncompleteBetaI, Serializable {
	private static final long serialVersionUID = 1L;

	private FunctionI _lnGamma = new LnGamma();
	private Approximation _approximation = new Approximation();

	public void setValueRange(double a, double b) {
		_approximation.setValueRange(a, b);
	}

	private double _a, _b, _k, _t, _s;
	private double _o, _q, _l;
	private double _p, _r, _m;
	private boolean _isValid = false;

	public IncompleteBeta() {
		_approximation.setEpsilon(1.0e-7);
		_approximation.setMaxIterations(100);
	}

	public IncompleteBeta(double a, double b) {
		_approximation.setEpsilon(1.0e-7);
		_approximation.setMaxIterations(100);
		setParameters(a, b);
	}

	public void setLnGamma(FunctionI lnGamma) {
		_lnGamma = lnGamma;
	}

	public void setParameters(double a, double b) {
		if (_isValid && _a == a && _b == b)
			return;
		_isValid = false;
		_a = a;
		_b = b;
		_t = (a + 1.0) / (a + b + 2.0);
		_k = _lnGamma.evaluate(a + b) - _lnGamma.evaluate(a) - _lnGamma.evaluate(b);
		_s = a + b;
		_o = a - 1.0;
		_q = a + 1.0;
		_l = _s / _q;
		_p = b - 1.0;
		_r = b + 1.0;
		_m = _s / _r;
		_isValid = true;
	}

	public double evaluate(double x) throws FunctionError {
		if (x < 0.0)
			throw new FunctionError("'x' can't be less than '0.0'.");
		if (x > 1.0)
			throw new FunctionError("'x' can't be greater than '1.0'.");
		double bt = (x == 0.0 || x == 1.0) ? 0.0 : Math.exp(_k + Math.log(x) * _a + Math.log(1.0 - x) * _b);
		if (x < _t)
			return bt * f1(x) / _a;
		else
			return 1.0 - bt * f2(1.0 - x) / _b;
	}

	private double f1(double x) {
		double c = 1.0;
		double d = _approximation.notZero(1.0 - x * _l);
		d = 1.0 / d;
		double h = d;
		for (int i = 1; i <= _approximation.getMaxIterations(); i++) {
			double m = i;
			double m2 = 2 * m;
			double aa = m * (_b - m) * x / ((_o + m2) * (_a + m2));
			d = _approximation.notZero(1.0 + aa * d);
			c = _approximation.notZero(1.0 + aa / c);
			d = 1.0 / d;
			h *= d * c;
			aa = -(_a + m) * (_s + m) * x / ((_a + m2) * (_q + m2));
			d = _approximation.notZero(1.0 + aa * d);
			c = _approximation.notZero(1.0 + aa / c);
			d = 1.0 / d;
			double del = d * c;
			h *= del;
			if (Math.abs(del - 1.0) < _approximation.getEpsilon())
				return h;
		}
		throw new FunctionError("Can't solve BetaCF to _epsilonilon.");
	}

	private double f2(double x) {
		double c = 1.0;
		double d = _approximation.notZero(1.0 - x * _m);
		d = 1.0 / d;
		double h = d;
		for (int i = 1; i <= _approximation.getMaxIterations(); i++) {
			double m = i;
			double m2 = 2 * m;
			double aa = m * (_a - m) * x / ((_p + m2) * (_b + m2));
			d = _approximation.notZero(1.0 + aa * d);
			c = _approximation.notZero(1.0 + aa / c);
			d = 1.0 / d;
			h *= d * c;
			aa = -(_b + m) * (_s + m) * x / ((_b + m2) * (_r + m2));
			d = _approximation.notZero(1.0 + aa * d);
			c = _approximation.notZero(1.0 + aa / c);
			d = 1.0 / d;
			double del = d * c;
			h *= del;
			if (Math.abs(del - 1.0) < _approximation.getEpsilon())
				return h;
		}
		throw new FunctionError("Can't solve BetaCF to _epsilon.");
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
