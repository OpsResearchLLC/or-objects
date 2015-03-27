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

package com.opsresearch.orobjects.lib.blas.java;

import com.opsresearch.orobjects.lib.blas.BLAS3I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class BLAS3 implements BLAS3I {
	@Override
	public void dgemm(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B,
			int begB, int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj)
			throws BLASException {
		for (int i = 0, ai = begA, ci = begC; i < m; i++, ai += incAi, ci += incCi) {
			for (int j = 0, bj = begB, cj = ci; j < n; j++, bj += incBj, cj += incCj) {
				C[cj] *= beta;
				double sum = 0.0;
				for (int l = 0, aj = ai, bi = bj; l < k; l++, aj += incAj, bi += incBi) {
					sum += A[aj] * B[bi];
				}
				C[cj] += alpha * sum;
			}
		}
	}

	@Override
	public void sgemm(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB,
			int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) throws BLASException {
		for (int i = 0, ai = begA, ci = begC; i < m; i++, ai += incAi, ci += incCi) {
			for (int j = 0, bj = begB, cj = ci; j < n; j++, bj += incBj, cj += incCj) {
				C[cj] *= beta;
				double sum = 0.0;
				for (int l = 0, aj = ai, bi = bj; l < k; l++, aj += incAj, bi += incBi) {
					sum += A[aj] * B[bi];
				}
				C[cj] += alpha * sum;
			}
		}
	}

	@Override
	public void zgemm(int m, int n, int k, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] B,
			int begB, int incBi, int incBj, ComplexI beta, double[] C, int begC, int incCi, int incCj)
			throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begB *= 2;
		incBi *= 2;
		incBj *= 2;
		begC *= 2;
		incCi *= 2;
		incCj *= 2;
		double alr = alpha.getReal(), ali = alpha.getImag();
		double ber = beta.getReal(), bei = beta.getImag();
		for (int i = 0, ai = begA, ci = begC; i < m; i++, ai += incAi, ci += incCi) {
			for (int j = 0, bj = begB, cj = ci; j < n; j++, bj += incBj, cj += incCj) {
				double sumr = 0, sumi = 0;
				for (int l = 0, aj = ai, bi = bj; l < k; l++, aj += incAj, bi += incBi) {
					double Areal = A[aj];
					double Aimag = A[aj + 1];
					double Breal = B[bi];
					double Bimag = B[bi + 1];
					sumr += Areal * Breal - Aimag * Bimag;
					sumi += Areal * Bimag + Aimag * Breal;
				}
				double Creal = C[cj];
				double Cimag = C[cj + 1];
				double tr = Creal * ber - Cimag * bei;
				double ti = Creal * bei + Cimag * ber;
				C[cj] = tr + alr * sumr - ali * sumi;
				C[cj + 1] = ti + alr * sumi + ali * sumr;
			}
		}
	}

	@Override
	public void cgemm(int m, int n, int k, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] B,
			int begB, int incBi, int incBj, ComplexI beta, float[] C, int begC, int incCi, int incCj)
			throws BLASException {
		begA *= 2;
		incAi *= 2;
		incAj *= 2;
		begB *= 2;
		incBi *= 2;
		incBj *= 2;
		begC *= 2;
		incCi *= 2;
		incCj *= 2;
		float alr = (float) alpha.getReal(), ali = (float) alpha.getImag();
		float ber = (float) beta.getReal(), bei = (float) beta.getImag();
		for (int i = 0, ai = begA, ci = begC; i < m; i++, ai += incAi, ci += incCi) {
			for (int j = 0, bj = begB, cj = ci; j < n; j++, bj += incBj, cj += incCj) {
				float sumr = 0, sumi = 0;
				for (int l = 0, aj = ai, bi = bj; l < k; l++, aj += incAj, bi += incBi) {
					float Areal = A[aj];
					float Aimag = A[aj + 1];
					float Breal = B[bi];
					float Bimag = B[bi + 1];
					sumr += Areal * Breal - Aimag * Bimag;
					sumi += Areal * Bimag + Aimag * Breal;
				}
				float Creal = C[cj];
				float Cimag = C[cj + 1];
				float tr = Creal * ber - Cimag * bei;
				float ti = Creal * bei + Cimag * ber;
				C[cj] = tr + alr * sumr - ali * sumi;
				C[cj + 1] = ti + alr * sumi + ali * sumr;
			}
		}
	}

}
