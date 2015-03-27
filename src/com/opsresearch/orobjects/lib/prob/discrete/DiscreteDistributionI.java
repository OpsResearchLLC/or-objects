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

package com.opsresearch.orobjects.lib.prob.discrete;

import com.opsresearch.orobjects.lib.prob.DistributionI;

public interface DiscreteDistributionI extends DistributionI {

	public void setVariableBounds(int min, int max);

	public int getRandomInteger();

	public int[] getRandomArray(int size);

	public int[][] getRandomArray(int sizeOfRows, int sizeOfColumns);

	public double pdf(int x);

	public double cdf(int x);

	public int inverseCdf(double probability);

	public double probability(int x);
	
	public double probability(int x1, int x2);

	public double getCdfMin();
	public double getCdfMax();
	public int getVariableMin();
	public int getVariableMax();
}
