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

import com.opsresearch.orobjects.lib.prob.continuous.ChiSquareDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.ChiSquareDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.EmpiricalDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.EmpiricalDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.ExponentialDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.ExponentialDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.FDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.FDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.NormalDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.NormalDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.StudentsTDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.StudentsTDistributionI;
import com.opsresearch.orobjects.lib.prob.continuous.UniformDistribution;
import com.opsresearch.orobjects.lib.prob.continuous.UniformDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.BinomialDistribution;
import com.opsresearch.orobjects.lib.prob.discrete.BinomialDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.DiscreteEmpiricalDistribution;
import com.opsresearch.orobjects.lib.prob.discrete.DiscreteEmpiricalDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.DiscreteUniformDistribution;
import com.opsresearch.orobjects.lib.prob.discrete.DiscreteUniformDistributionI;
import com.opsresearch.orobjects.lib.prob.discrete.PoissonDistribution;
import com.opsresearch.orobjects.lib.prob.discrete.PoissonDistributionI;

public class Distributions {

	private ChiSquareDistributionI chiSquareDistribution;
	private EmpiricalDistributionI empiricalDistribution;
	private ExponentialDistributionI exponentialDistribution;
	private FDistributionI fDistribution;
	private NormalDistributionI normalDistribution;
	private StudentsTDistributionI studentsTDistribution;
	private UniformDistributionI uniformDistribution;

	private DiscreteUniformDistributionI discreteUniformDistribution;
	private DiscreteEmpiricalDistributionI discreteEmpiricalDistribution;
	private PoissonDistributionI poissonDistribution;
	private BinomialDistributionI binomialDistribution;	

	public BinomialDistributionI getBinomialDistribution() {
		if (binomialDistribution == null)
			binomialDistribution = new BinomialDistribution();
		return binomialDistribution;
	}

	public void setBinomialDistribution(BinomialDistributionI binomialDistribution) {
		this.binomialDistribution = binomialDistribution;
	}

	public ChiSquareDistributionI getChiSquareDistribution() {
		if (chiSquareDistribution == null)
			chiSquareDistribution = new ChiSquareDistribution();
		return chiSquareDistribution;
	}

	public void setChiSquareDistribution(ChiSquareDistributionI chiSquareDistribution) {
		this.chiSquareDistribution = chiSquareDistribution;
	}

	public DiscreteUniformDistributionI getDiscreteUniformDistribution() {
		if (discreteUniformDistribution == null)
			discreteUniformDistribution = new DiscreteUniformDistribution();
		return discreteUniformDistribution;
	}

	public void setDiscreteUniformDistribution(DiscreteUniformDistributionI discreteUniformDistribution) {
		this.discreteUniformDistribution = discreteUniformDistribution;
	}

	public EmpiricalDistributionI getEmpiricalDistribution() {
		if (empiricalDistribution == null)
			empiricalDistribution = new EmpiricalDistribution();
		return empiricalDistribution;
	}

	public void setEmpiricalDistribution(EmpiricalDistributionI empiricalDistribution) {
		this.empiricalDistribution = empiricalDistribution;
	}
	
	public DiscreteEmpiricalDistributionI getDiscreteEmpiricalDistribution() {
		if (discreteEmpiricalDistribution == null)
			discreteEmpiricalDistribution = new DiscreteEmpiricalDistribution();
		return discreteEmpiricalDistribution;
	}
	
	public void setDiscreteEmpiricalDistribution(DiscreteEmpiricalDistributionI discreteEmpiricalDistribution) {
		this.discreteEmpiricalDistribution = discreteEmpiricalDistribution;
	}

	public ExponentialDistributionI getExponentialDistribution() {
		if (exponentialDistribution == null)
			exponentialDistribution = new ExponentialDistribution();
		return exponentialDistribution;
	}

	public void setExponentialDistribution(ExponentialDistributionI exponentialDistribution) {
		this.exponentialDistribution = exponentialDistribution;
	}

	public FDistributionI getFDistribution() {
		if (fDistribution == null)
			fDistribution = new FDistribution();
		return fDistribution;
	}

	public void setFDistribution(FDistributionI fDistribution) {
		this.fDistribution = fDistribution;
	}

	public NormalDistributionI getNormalDistribution() {
		if (normalDistribution == null)
			normalDistribution = new NormalDistribution();
		return normalDistribution;
	}

	public void setNormalDistribution(NormalDistributionI normalDistribution) {
		this.normalDistribution = normalDistribution;
	}

	public PoissonDistributionI getPoissonDistribution() {
		if (poissonDistribution == null)
			poissonDistribution = new PoissonDistribution();
		return poissonDistribution;
	}

	public void setPoissonDistribution(PoissonDistributionI poissonDistribution) {
		this.poissonDistribution = poissonDistribution;
	}

	public StudentsTDistributionI getStudentsTDistribution() {
		if (studentsTDistribution == null)
			studentsTDistribution = new StudentsTDistribution();
		return studentsTDistribution;
	}

	public void setStudentsTDistribution(StudentsTDistributionI studentsTDistribution) {
		this.studentsTDistribution = studentsTDistribution;
	}

	public UniformDistributionI getUniformDistribution() {
		if (uniformDistribution == null)
			uniformDistribution = new UniformDistribution();
		return uniformDistribution;
	}

	public void setUniformDistribution(UniformDistributionI uniformDistribution) {
		this.uniformDistribution = uniformDistribution;
	}

}
