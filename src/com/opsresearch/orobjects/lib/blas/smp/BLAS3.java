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

package com.opsresearch.orobjects.lib.blas.smp;

import com.opsresearch.orobjects.lib.blas.BLAS3I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.smp.Smp;

public class BLAS3 implements BLAS3I {
	private Smp _smp;
	private BLAS3I _blas3 = null;
	private BLASException _exception = null;

	public BLAS3() {
		_smp = new Smp(100);
		_blas3 = new com.opsresearch.orobjects.lib.blas.java.BLAS3();
	}

	public BLAS3(BLAS3I blas3) {
		_smp = new Smp(100);
		_blas3 = blas3;
	}

	public Smp getSmp() {
		return _smp;
	}

	public void setSmp(Smp smp) {
		_smp = smp;
	}

	public void dgemm(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B,
			int begB, int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj)
			throws BLASException {
		Runnable[] jobs;
		if (incCj > incCi) {
			int[] tasks = _smp.balanceTasks(n, m * k);
			if (tasks.length < 2) {
				_blas3.dgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begB += t * incBj, begC += t * incCj, job++)
				jobs[job] = new DgemmJob(m, t = tasks[job], k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		} else {
			int[] tasks = _smp.balanceTasks(m, n * k);
			if (tasks.length < 2) {
				_blas3.dgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begC += t * incCi, job++)
				jobs[job] = new DgemmJob(t = tasks[job], n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		}

		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class DgemmJob implements Runnable {
		private int _m, _n, _k;
		private int _begA, _incAi, _incAj;
		private int _begB, _incBi, _incBj;
		private int _begC, _incCi, _incCj;
		private double _alpha, _beta;
		private double[] _A, _B, _C;

		DgemmJob(int m, int n, int k, double alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB,
				int incBi, int incBj, double beta, double[] C, int begC, int incCi, int incCj) {
			_m = m;
			_n = n;
			_k = k;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_B = B;
			_begB = begB;
			_incBi = incBi;
			_incBj = incBj;
			_C = C;
			_begC = begC;
			_incCi = incCi;
			_incCj = incCj;
		}

		public void run() {
			try {
				_blas3.dgemm(_m, _n, _k, _alpha, _A, _begA, _incAi, _incAj, _B, _begB, _incBi, _incBj, _beta, _C,
						_begC, _incCi, _incCj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void sgemm(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB,
			int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) throws BLASException {
		Runnable[] jobs;
		if (incCj > incCi) {
			int[] tasks = _smp.balanceTasks(n, m * k);
			if (tasks.length < 2) {
				_blas3.sgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begB += t * incBj, begC += t * incCj, job++)
				jobs[job] = new SgemmJob(m, t = tasks[job], k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		} else {
			int[] tasks = _smp.balanceTasks(m, n * k);
			if (tasks.length < 2) {
				_blas3.sgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begC += t * incCi, job++)
				jobs[job] = new SgemmJob(t = tasks[job], n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		}

		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class SgemmJob implements Runnable {
		private int _m, _n, _k;
		private int _begA, _incAi, _incAj;
		private int _begB, _incBi, _incBj;
		private int _begC, _incCi, _incCj;
		private float _alpha, _beta;
		private float[] _A, _B, _C;

		SgemmJob(int m, int n, int k, float alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB,
				int incBi, int incBj, float beta, float[] C, int begC, int incCi, int incCj) {
			_m = m;
			_n = n;
			_k = k;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_B = B;
			_begB = begB;
			_incBi = incBi;
			_incBj = incBj;
			_C = C;
			_begC = begC;
			_incCi = incCi;
			_incCj = incCj;
		}

		public void run() {
			try {
				_blas3.sgemm(_m, _n, _k, _alpha, _A, _begA, _incAi, _incAj, _B, _begB, _incBi, _incBj, _beta, _C,
						_begC, _incCi, _incCj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void zgemm(int m, int n, int k, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] B,
			int begB, int incBi, int incBj, ComplexI beta, double[] C, int begC, int incCi, int incCj)
			throws BLASException {
		Runnable[] jobs;
		if (incCj > incCi) {
			int[] tasks = _smp.balanceTasks(n, m * k);
			if (tasks.length < 2) {
				_blas3.zgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begB += t * incBj, begC += t * incCj, job++)
				jobs[job] = new ZgemmJob(m, t = tasks[job], k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		} else {
			int[] tasks = _smp.balanceTasks(m, n * k);
			if (tasks.length < 2) {
				_blas3.zgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begC += t * incCi, job++)
				jobs[job] = new ZgemmJob(t = tasks[job], n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		}

		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ZgemmJob implements Runnable {
		private int _m, _n, _k;
		private int _begA, _incAi, _incAj;
		private int _begB, _incBi, _incBj;
		private int _begC, _incCi, _incCj;
		private ComplexI _alpha, _beta;
		private double[] _A, _B, _C;

		ZgemmJob(int m, int n, int k, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] B, int begB,
				int incBi, int incBj, ComplexI beta, double[] C, int begC, int incCi, int incCj) {
			_m = m;
			_n = n;
			_k = k;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_B = B;
			_begB = begB;
			_incBi = incBi;
			_incBj = incBj;
			_C = C;
			_begC = begC;
			_incCi = incCi;
			_incCj = incCj;
		}

		public void run() {
			try {
				_blas3.zgemm(_m, _n, _k, _alpha, _A, _begA, _incAi, _incAj, _B, _begB, _incBi, _incBj, _beta, _C,
						_begC, _incCi, _incCj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void cgemm(int m, int n, int k, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] B,
			int begB, int incBi, int incBj, ComplexI beta, float[] C, int begC, int incCi, int incCj)
			throws BLASException {
		Runnable[] jobs;
		if (incCj > incCi) {
			int[] tasks = _smp.balanceTasks(n, m * k);
			if (tasks.length < 2) {
				_blas3.cgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begB += t * incBj, begC += t * incCj, job++)
				jobs[job] = new CgemmJob(m, t = tasks[job], k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		} else {
			int[] tasks = _smp.balanceTasks(m, n * k);
			if (tasks.length < 2) {
				_blas3.cgemm(m, n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj, beta, C, begC, incCi, incCj);
				return;
			}

			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begC += t * incCi, job++)
				jobs[job] = new CgemmJob(t = tasks[job], n, k, alpha, A, begA, incAi, incAj, B, begB, incBi, incBj,
						beta, C, begC, incCi, incCj);
		}

		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class CgemmJob implements Runnable {
		private int _m, _n, _k;
		private int _begA, _incAi, _incAj;
		private int _begB, _incBi, _incBj;
		private int _begC, _incCi, _incCj;
		private ComplexI _alpha, _beta;
		private float[] _A, _B, _C;

		CgemmJob(int m, int n, int k, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] B, int begB,
				int incBi, int incBj, ComplexI beta, float[] C, int begC, int incCi, int incCj) {
			_m = m;
			_n = n;
			_k = k;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_B = B;
			_begB = begB;
			_incBi = incBi;
			_incBj = incBj;
			_C = C;
			_begC = begC;
			_incCi = incCi;
			_incCj = incCj;
		}

		public void run() {
			try {
				_blas3.cgemm(_m, _n, _k, _alpha, _A, _begA, _incAi, _incAj, _B, _begB, _incBi, _incBj, _beta, _C,
						_begC, _incCi, _incCj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

}
