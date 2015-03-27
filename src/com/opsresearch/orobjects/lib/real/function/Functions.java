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

public class Functions implements Serializable {

	private static final long serialVersionUID = 1L;

	private LnGammaI _lnGamma;
	private LnFactorialI _lnFactorial;
	private BinomialCoefficientI _binomialCoefficient;
	private IncompleteBetaI _incompleteBeta;
	private IncompleteGammaI _incompleteGamma;

	public double lnGamma(double x) {
		return getLnGamma().evaluate(x);
	}

	public double lnFactorial(int x) {
		return getLnFactorial().function(x);
	}

	public double binomialCoefficient(int n, int k) {
		return getBinomialCoefficient().function(n, k);
	}

	public double incompleteGamma(double a, double x) {
		getIncompleteGamma().setParameter(a);
		return getIncompleteGamma().evaluate(x);
	}

	public double incompleteBeta(double a, double b, double x) {
		getIncompleteBeta().setParameters(a, b);
		return getIncompleteBeta().evaluate(x);
	}

	public LnGammaI getLnGamma() {
		if (_lnGamma == null)
			_lnGamma = new LnGamma();
		return _lnGamma;
	}

	public void setLnGamma(LnGammaI lnGamma) {
		_lnGamma = lnGamma;
	}

	public LnFactorialI getLnFactorial() {
		if (_lnFactorial == null)
			_lnFactorial = new LnFactorial();
		return _lnFactorial;
	}

	public void setLnFactorialI(LnFactorialI lnFactorial) {
		_lnFactorial = lnFactorial;
	}

	public BinomialCoefficientI getBinomialCoefficient() {
		if (_binomialCoefficient == null)
			_binomialCoefficient = new BinomialCoefficient();
		return _binomialCoefficient;
	}

	public void setBinomialCoefficient(BinomialCoefficientI binomialCoefficient) {
		_binomialCoefficient = binomialCoefficient;
	}

	public IncompleteBetaI getIncompleteBeta() {
		if (_incompleteBeta == null)
			_incompleteBeta = new IncompleteBeta();
		return _incompleteBeta;
	}

	public void setIncompleteBeta(IncompleteBetaI incompleteBeta) {
		_incompleteBeta = incompleteBeta;
	}

	public IncompleteGammaI getIncompleteGamma() {
		if (_incompleteGamma == null)
			_incompleteGamma = new IncompleteGamma();
		return _incompleteGamma;
	}

	public void setIncompleteGamma(IncompleteGammaI incompleteGamma) {
		_incompleteGamma = incompleteGamma;
	}

}
