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

package com.opsresearch.orobjects.lib.prob;

import com.opsresearch.orobjects.lib.random.Random;
import com.opsresearch.orobjects.lib.random.RandomI;
import com.opsresearch.orobjects.lib.real.approx.AccuracyException;
import com.opsresearch.orobjects.lib.real.approx.ApproxException;
import com.opsresearch.orobjects.lib.real.approx.Approximation;
import com.opsresearch.orobjects.lib.real.approx.Approximations;
import com.opsresearch.orobjects.lib.real.approx.IntegrationI;
import com.opsresearch.orobjects.lib.real.approx.InverseI;
import com.opsresearch.orobjects.lib.real.function.BinomialCoefficientI;
import com.opsresearch.orobjects.lib.real.function.Function;
import com.opsresearch.orobjects.lib.real.function.FunctionI;
import com.opsresearch.orobjects.lib.real.function.Functions;
import com.opsresearch.orobjects.lib.real.function.IncompleteBetaI;
import com.opsresearch.orobjects.lib.real.function.IncompleteGammaI;
import com.opsresearch.orobjects.lib.real.function.LnFactorialI;
import com.opsresearch.orobjects.lib.real.function.LnGammaI;

public class Delegates {

	private Functions _functions = new Functions();
	private Random _randoms = new Random();
	private Approximations _approximations = new Approximations();
	public void setSeed(long seed) {
		_randoms.setSeed(seed);
	}

	public int sizeOfBits() {
		return _randoms.sizeOfBits();
	}

	public long next() {
		return _randoms.next();
	}

	public double nextDouble() {
		return _randoms.nextDouble();
	}

	private Function _function = new Function();
	private Approximation _approximation = new Approximation();

	public double getEpsilon() {
		return _approximation.getEpsilon();
	}

	public double getZero() {
		return _approximation.getZero();
	}

	public int getMaxIterations() {
		return _approximation.getMaxIterations();
	}

	public double getValueMax() {
		return _approximation.getValueMax();
	}

	public int getMinValue() {
		return _approximation.getMinValue();
	}

	public void setMaxIterations(int maxIterations) {
		_approximation.setMaxIterations(maxIterations);
	}

	public void setEpsilon(double epsilon) {
		_approximation.setEpsilon(epsilon);
	}

	public void setValueRange(double a, double b) {
		_approximation.setValueRange(a, b);
	}

	public void setZero(double zero) {
		_approximation.setZero(zero);
	}

	public boolean inRange(double value) {
		return _approximation.inRange(value);
	}

	public double zero(double d) {
		return _approximation.zero(d);
	}

	public double notZero(double d) {
		return _approximation.notZero(d);
	}

	public boolean near(double a, double b) {
		return _approximation.near(a, b);
	}

	public boolean isValid() {
		return _function.isValid();
	}

	public boolean setValid(boolean valid) {
		return _function.setValid(valid);
	}

	public double evaluate(double x) {
		return _function.evaluate(x);
	}

	public double lnGamma(double x) {
		return _functions.lnGamma(x);
	}

	public double lnFactorial(int x) {
		return _functions.lnFactorial(x);
	}

	public double binomialCoefficient(int n, int k) {
		return _functions.binomialCoefficient(n, k);
	}

	public double incompleteGamma(double a, double x) {
		return _functions.incompleteGamma(a, x);
	}

	public double incompleteBeta(double a, double b, double x) {
		return _functions.incompleteBeta(a, b, x);
	}

	public LnGammaI getLnGamma() {
		return _functions.getLnGamma();
	}

	public void setLnGamma(LnGammaI lnGamma) {
		_functions.setLnGamma(lnGamma);
	}

	public LnFactorialI getLnFactorial() {
		return _functions.getLnFactorial();
	}

	public void setLnFactorialI(LnFactorialI lnFactorial) {
		_functions.setLnFactorialI(lnFactorial);
	}

	public BinomialCoefficientI getBinomialCoefficient() {
		return _functions.getBinomialCoefficient();
	}

	public void setBinomialCoefficient(BinomialCoefficientI binomialCoefficient) {
		_functions.setBinomialCoefficient(binomialCoefficient);
	}

	public IncompleteBetaI getIncompleteBeta() {
		return _functions.getIncompleteBeta();
	}

	public void setIncompleteBeta(IncompleteBetaI incompleteBeta) {
		_functions.setIncompleteBeta(incompleteBeta);
	}

	public IncompleteGammaI getIncompleteGamma() {
		return _functions.getIncompleteGamma();
	}

	public void setIncompleteGamma(IncompleteGammaI incompleteGamma) {
		_functions.setIncompleteGamma(incompleteGamma);
	}

	public RandomI getRandom() {
		return _randoms.getRandom();
	}

	public void setRandom(RandomI random) {
		_randoms.setRandom(random);
	}

	public IntegrationI getIntegration() {
		return _approximations.getIntegration();
	}

	public void setIntegration(IntegrationI integration) {
		_approximations.setIntegration(integration);
	}

	public double integrate(FunctionI function, double bound1, double bound2) throws AccuracyException {
		return _approximations.integrate(function, bound1, bound2);
	}

	public InverseI getInverse() {
		return _approximations.getInverse();
	}

	public void setInverse(InverseI inverse) {
		_approximations.setInverse(inverse);
	}

	public double invert(FunctionI function, double x1, double x2, double y) throws ApproxException, AccuracyException {
		return _approximations.invert(function, x1, x2, y);
	}

}
