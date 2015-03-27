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

import java.io.Serializable;

import com.opsresearch.orobjects.lib.real.function.FunctionI;

public class Approximation implements ApproximationI, Serializable {

	private static final long serialVersionUID = 1L;

	protected double _epsilon = 1.0E-7;
	protected double _valueMax = Double.MAX_VALUE;
	protected double _valueMin = Double.MIN_VALUE;
	protected int _maxIterations = 25;
	private double _zero = 1.0e-30;
	private double _negZero = -_zero;

	public double getEpsilon() {
		return _epsilon;
	}
	
	public double getZero() {
		return _epsilon;
	}

	public int getMaxIterations() {
		return _maxIterations;
	}

	public double getValueMax() {
		return _valueMax;
	}

	public int getMinValue() {
		return _maxIterations;
	}

	public void setMaxIterations(int maxIterations) {
		_maxIterations = maxIterations;
	}

	public void setEpsilon(double epsilon) {
		_epsilon = epsilon;
	}

	public void setValueRange(double a, double b) {
		_valueMin = Math.min(a, b);
		_valueMax = Math.max(a, b);
	}

	public void setZero(double zero) {
		_negZero = -(_zero = Math.abs(zero));
	}


	public boolean inRange(double value) {
		return value <= _valueMax && value >= _valueMin;
	}

	public double zero(double d) {
		if (d < 0.0) {
			return d > _negZero ? d : 0.0;
		} else {
			return d < _zero ? d : 0.0;
		}
	}

	public double notZero(double d) {
		if (d < 0.0) {
			return d < _negZero ? d : _negZero;
		} else {
			return d > _zero ? d : _zero;
		}
	}

	public boolean near(double a, double b) {
		return a < b ? (b - a) <= _epsilon : (a - b) <= _epsilon;
	}

	private static Approximations _approximations = new Approximations();

	public static Approximations getApproximations() {
		return _approximations;
	}

	public static void setApproximations(Approximations approximations) {
		_approximations = approximations;
	}

	public static double integrate(FunctionI function, double bound1, double bound2) throws AccuracyException {
		return _approximations.integrate(function, bound1, bound2);
	}

	public static double invert(FunctionI function, double x1, double x2, double y) throws ApproxException,
			AccuracyException {
		return _approximations.invert(function, x1, x2, y);
	}

}
