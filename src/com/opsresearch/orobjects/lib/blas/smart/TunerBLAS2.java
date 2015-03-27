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

package com.opsresearch.orobjects.lib.blas.smart;

import com.opsresearch.orobjects.lib.blas.BLAS2I;

public class TunerBLAS2 implements TunerI<BLAS2I> {

	@Override
	public BLAS2I[][] tune(Tuned<BLAS2I> tuned) {

		final long M = 1000L * 1000L;
		final long G = M * 1000L;
		final long T = G * 1000L;

		if (tuned.getNumTuningPoints() != 3)
			throw new SmartError("This tuner only works with 3 tuning points");

		int numPoints = tuned.getNumTuningPoints();
		int numMethods = tuned.getNumTuningMethods();

		BLAS2I[][] assignments = new BLAS2I[numMethods][numPoints + 1];

		for (int i = 0; i < numMethods; i++) {
			tuned.setTuningPoint(i, 0, M);
			assignments[i][0] = tuned.getImplementation("java");

			tuned.setTuningPoint(i, 1, G);
			assignments[i][1] = tuned.getImplementation("java");

			tuned.setTuningPoint(i, 2, T);
			assignments[i][2] = tuned.getImplementation("smp");

			assignments[i][3] = tuned.getImplementation("smp");
		}
		return assignments;
	}
}
