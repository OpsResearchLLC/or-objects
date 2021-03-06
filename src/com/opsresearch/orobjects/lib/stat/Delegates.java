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

package com.opsresearch.orobjects.lib.stat;

import com.opsresearch.orobjects.lib.algebra.Algebras;
import com.opsresearch.orobjects.lib.algebra.LUDecompositionI;
import com.opsresearch.orobjects.lib.algebra.SVDecompositionI;
import com.opsresearch.orobjects.lib.prob.Distributions;
import com.opsresearch.orobjects.lib.prob.continuous.ChiSquareDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.EmpiricalDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.ExponentialDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.FDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.NormalDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.StudentsTDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.UniformDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.BinomialDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.DiscreteEmpiricalDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.DiscreteUniformDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.PoissonDistributionI;

public class Delegates {

	private Distributions _distributions = new Distributions();
	public DiscreteEmpiricalDistributionI getDiscreteEmpiricalDistribution() {
		return _distributions.getDiscreteEmpiricalDistribution();
	}

	public void setDiscreteEmpiricalDistribution(DiscreteEmpiricalDistributionI discreteEmpiricalDistribution) {
		_distributions.setDiscreteEmpiricalDistribution(discreteEmpiricalDistribution);
	}

	public EmpiricalDistributionI getEmpiricalDistribution() {
		return _distributions.getEmpiricalDistribution();
	}

	public void setEmpiricalDistribution(EmpiricalDistributionI empiricalDistribution) {
		_distributions.setEmpiricalDistribution(empiricalDistribution);
	}

	private Algebras _algebras = new Algebras();

	public BinomialDistributionI getBinomialDistribution() {
		return _distributions.getBinomialDistribution();
	}

	public void setBinomialDistribution(BinomialDistributionI binomialDistribution) {
		_distributions.setBinomialDistribution(binomialDistribution);
	}

	public ChiSquareDistributionI getChiSquareDistribution() {
		return _distributions.getChiSquareDistribution();
	}

	public void setChiSquareDistribution(ChiSquareDistributionI chiSquareDistribution) {
		_distributions.setChiSquareDistribution(chiSquareDistribution);
	}

	public DiscreteUniformDistributionI getDiscreteUniformDistribution() {
		return _distributions.getDiscreteUniformDistribution();
	}

	public void setDiscreteUniformDistribution(DiscreteUniformDistributionI discreteUniformDistribution) {
		_distributions.setDiscreteUniformDistribution(discreteUniformDistribution);
	}

	public ExponentialDistributionI getExponentialDistribution() {
		return _distributions.getExponentialDistribution();
	}

	public void setExponentialDistribution(ExponentialDistributionI exponentialDistribution) {
		_distributions.setExponentialDistribution(exponentialDistribution);
	}

	public FDistributionI getFDistribution() {
		return _distributions.getFDistribution();
	}

	public void setFDistribution(FDistributionI fDistribution) {
		_distributions.setFDistribution(fDistribution);
	}

	public NormalDistributionI getNormalDistribution() {
		return _distributions.getNormalDistribution();
	}

	public void setNormalDistribution(NormalDistributionI normalDistribution) {
		_distributions.setNormalDistribution(normalDistribution);
	}

	public PoissonDistributionI getPoissonDistribution() {
		return _distributions.getPoissonDistribution();
	}

	public void setPoissonDistribution(PoissonDistributionI poissonDistribution) {
		_distributions.setPoissonDistribution(poissonDistribution);
	}

	public StudentsTDistributionI getStudentsTDistribution() {
		return _distributions.getStudentsTDistribution();
	}

	public void setStudentsTDistribution(StudentsTDistributionI studentsTDistribution) {
		_distributions.setStudentsTDistribution(studentsTDistribution);
	}

	public UniformDistributionI getUniformDistribution() {
		return _distributions.getUniformDistribution();
	}

	public void setUniformDistribution(UniformDistributionI uniformDistribution) {
		_distributions.setUniformDistribution(uniformDistribution);
	}

	public SVDecompositionI getSVDecomposition() {
		return _algebras.getSVDecomposition();
	}

	public void setSVDecomposition(SVDecompositionI sVDecomposition) {
		_algebras.setSVDecomposition(sVDecomposition);
	}

	public LUDecompositionI getLUDecomposition() {
		return _algebras.getLUDecomposition();
	}

	public void setLUDecomposition(LUDecompositionI lUDecomposition) {
		_algebras.setLUDecomposition(lUDecomposition);
	}

}
