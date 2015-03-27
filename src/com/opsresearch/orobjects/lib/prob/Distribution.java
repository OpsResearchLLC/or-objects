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

import java.util.Iterator;

import com.opsresearch.orobjects.lib.real.matrix.DenseMatrix;
import com.opsresearch.orobjects.lib.real.matrix.DenseVector;
import com.opsresearch.orobjects.lib.real.matrix.MatrixElementI;
import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.VectorElementI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public abstract class Distribution extends Delegates implements DistributionI, java.io.Serializable {

	private static final long serialVersionUID = 1L;

	public void checkProbability(double probability) {
		if (probability < 0.0)
			throw new ProbError("The 'probability' can't be less than '0.0'.");
		if (probability > 1.0)
			throw new ProbError("The 'probability' can't be greater than '1.0'.");
	}

	public Distribution() {
	}

	public Distribution(long seed) {
	}

	public VectorI getRandomVector(int size) {
		return setElements(new DenseVector(size));
	}

	public MatrixI getRandomMatrix(int sizeOfRows, int sizeOfColumns) {
		return setElements(new DenseMatrix(sizeOfRows, sizeOfColumns));
	}

	public VectorI setElements(VectorI vector) {
		for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();)
			(e.next()).setValue(getRandomScaler());
		return vector;
	}

	public MatrixI setElements(MatrixI matrix) {
		for (Iterator<MatrixElementI> e = matrix.elements(); e.hasNext();)
			e.next().setValue(getRandomScaler());
		return matrix;
	}

}
