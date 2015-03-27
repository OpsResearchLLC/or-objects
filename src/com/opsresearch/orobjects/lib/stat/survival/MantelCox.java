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

package com.opsresearch.orobjects.lib.stat.survival;

public class MantelCox implements LogRankTestI {

	private final Delegates delegates;
	private SurvivorFunctionI[] funct;
	private Double z;

	public MantelCox() {
		this(new Delegates());
	}

	public MantelCox(Delegates delegates) {
		this.delegates = delegates;
	}

	@Override
	public SurvivorFunctionI[] getSurvivorFunctions() {
		return funct;
	}

	@Override
	public void setSurvivorFunctions(SurvivorFunctionI f1, SurvivorFunctionI f2) {
		this.funct = new SurvivorFunctionI[2];
		this.funct[0] = f1;
		this.funct[1] = f2;
		z = null;
	}

	@Override
	public void setSurvivorFunctions(Trial t1, Trial t2) {
		SurvivorFunctionI f1 = delegates.getSurvivorFunction();
		f1.setTrial(t1);
		SurvivorFunctionI f2 = delegates.getSurvivorFunction();
		f2.setTrial(t2);
		setSurvivorFunctions(f1, f2);
	}

	@Override
	public double getZ() {
		if (z == null) {
			SurvivorFunctionI f1 = funct[0];
			SurvivorFunctionI f2 = funct[1];
			int last1 = f1.getSampleSize() - 1;
			int last2 = f2.getSampleSize() - 1;
			double sum_vj = 0.0;
			double sum_o1j_sub_e1j = 0.0;
			int i1 = 0;
			int i2 = 0;
			while (true) {
				double n1j = f1.getAtRisk(i1);
				double n2j = f2.getAtRisk(i2);
				double nj = n1j + n2j;
				double o1j = f1.getDeaths(i1);
				double o2j = f2.getDeaths(i2);
				double oj = o1j + o2j;
				double e1j = oj * (n1j / nj);
				double vj = (oj * (n1j - nj) * (1.0 - n1j / nj) * (nj - oj)) / (nj - 1.0);
				sum_vj += vj;
				sum_o1j_sub_e1j += o1j - e1j;

				if (i1 < last1 && i2 < last2) {
					double t1 = f1.getTime(i1 + 1);
					double t2 = f1.getTime(i2 + 1);
					if (t1 < t2)
						i1++;
					else if (t2 < t1)
						i2++;
					else {
						i1++;
						i2++;
					}
				} else if (i1 < last1)
					i1++;
				else if (i2 < last2)
					i2++;
				else
					break;
			}
			z = Double.valueOf(sum_o1j_sub_e1j / Math.sqrt(sum_vj));
		}
		return z.doubleValue();
	}

}
