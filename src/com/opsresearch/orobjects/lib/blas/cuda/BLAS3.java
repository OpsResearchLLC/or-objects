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

package com.opsresearch.orobjects.lib.blas.cuda;

import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS3 extends com.opsresearch.orobjects.lib.blas.java.BLAS3 {

	private static boolean _loaded = false; 


	public void dgemm(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB, int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj) throws BLASException {
		if (_loaded)
			dgemmNative(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
		else
			super.dgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
	}

	private native void dgemmNative(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB, int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj) throws BLASException;


	public void sgemm(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB, int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) throws BLASException {
		if (_loaded)
			sgemmNative(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
		else
			super.sgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
	}

	private native void sgemmNative(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB, int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) throws BLASException;


	public void zgemm(int m, int n, int k, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB, int incBi, int incBj, ComplexI beta, double[] C, int begC, int incCi, int incCj) throws BLASException {
		if (_loaded)
			zgemmNative(m, n, k, alpha.getReal(), alpha.getImag(), A, begA, incAi, incAj, B, begB, incBi, incBj, beta.getReal(), beta.getImag(), C, begC, incCi, incCj);
		else
			super.zgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
	}

	private native void zgemmNative(int m, int n, int k, double alphaReal, double alphaImag, double[] A, int begA, int incAi, int incAj, double[] B, int begB, int incBi, int incBj, double betaReal, double betaImag, double[] C, int begC, int incCi, int incCj) throws BLASException;


	public void cgemm(int m, int n, int k, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB, int incBi, int incBj, ComplexI beta, float[] C, int begC, int incCi, int incCj) throws BLASException {
		if (_loaded)
			cgemmNative(m, n, k, (float) alpha.getReal(), (float) alpha.getImag(), A, begA, incAi, incAj, B, begB, incBi, incBj, (float) beta.getReal(), (float) beta.getImag(), C, begC, incCi, incCj);
		else
			super.cgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
	}

	private native void cgemmNative(int m, int n, int k, float alphaReal, float alphaImag, float[] A, int begA, int incAi, int incAj, float[] B, int begB, int incBi, int incBj, float betaReal, float betaImag, float[] C, int begC, int incCi, int incCj) throws BLASException;

}
