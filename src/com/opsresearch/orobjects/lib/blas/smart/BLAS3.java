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

import com.opsresearch.orobjects.lib.blas.BLAS3I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS3 extends Tuned<BLAS3I> implements BLAS3I {

	public BLAS3() {
		super(new com.opsresearch.orobjects.lib.blas.java.BLAS3(), new TunerBLAS3());
		setNumTuningPoints(3);
		setNumTuningMethods(NMETHODS);
		addImplementation("java", new com.opsresearch.orobjects.lib.blas.java.BLAS3());
		addImplementation("smp", new com.opsresearch.orobjects.lib.blas.smp.BLAS3());
		addImplementation("hpc", new com.opsresearch.orobjects.lib.blas.hpc.BLAS3());
		addImplementation("cuda", new com.opsresearch.orobjects.lib.blas.cuda.BLAS3());
		addImplementation("opencl", new com.opsresearch.orobjects.lib.blas.opencl.BLAS3());
		tune();
	}

	@Override
	public void dgemm(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B,
			int begB, int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj)
			throws BLASException {
		select(0, m, n, k).dgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi,
				incCj);
	}

	@Override
	public void sgemm(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB,
			int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) throws BLASException {
		select(1, m, n, k).sgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi,
				incCj);
	}

	@Override
	public void zgemm(int m, int n, int k, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] B,
			int begB, int incBi, int incBj, ComplexI beta, double[] C, int begC, int incCi, int incCj)
			throws BLASException {
		select(2, m, n, k).zgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi,
				incCj);
	}

	@Override
	public void cgemm(int m, int n, int k, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] B,
			int begB, int incBi, int incBj, ComplexI beta, float[] C, int begC, int incCi, int incCj)
			throws BLASException {
		select(3, m, n, k).cgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi,
				incCj);
	}

	private final static int NMETHODS = 10;

}
