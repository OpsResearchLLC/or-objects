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

package com.opsresearch.orobjects.lib.blas;

import com.opsresearch.orobjects.lib.complex.ComplexI;

public interface BLAS3I {
	public void dgemm(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB, int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj) throws BLASException;

	public void sgemm(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB, int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) throws BLASException;

	public void zgemm(int m, int n, int k, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB, int incBi, int incBj, ComplexI beta, double[] C, int begC, int incCi, int incCj) throws BLASException;

	public void cgemm(int m, int n, int k, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB, int incBi, int incBj, ComplexI beta, float[] C, int begC, int incCi, int incCj) throws BLASException;

}
