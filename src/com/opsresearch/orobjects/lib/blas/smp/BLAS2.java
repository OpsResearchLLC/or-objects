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

import com.opsresearch.orobjects.lib.blas.BLAS2I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.smp.Smp;

public class BLAS2 implements BLAS2I {
	private Smp _smp;
	private BLAS2I _blas2 = null;
	private BLASException _exception = null;

	public BLAS2() {
		_smp = new Smp(100);
		_blas2 = new com.opsresearch.orobjects.lib.blas.java.BLAS2();
	}

	public BLAS2(BLAS2I blas2) {
		_smp = new Smp(100);
		_blas2 = blas2;
	}

	public Smp getSmp() {
		return _smp;
	}

	public void setSmp(Smp smp) {
		_smp = smp;
	}

	public void dgemv(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
			int incx, double beta, double[] y, int begy, int incy) throws BLASException {
		int[] tasks = _smp.balanceTasks(m, n);
		if (tasks.length < 2) {
			_blas2.dgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++) {
			jobs[job] = new DgemvJob(t = tasks[job], n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy,
					incy);
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

	private class DgemvJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _alpha, _beta;
		private double[] _A, _x, _y;

		DgemvJob(int m, int n, double alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
				int incx, double beta, double[] y, int begy, int incy) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.dgemv(_m, _n, _alpha, _A, _begA, _incAi, _incAj, _x, _begx, _incx, _beta, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void dger(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		Runnable[] jobs;
		if (incAj < incAi) {
			int[] tasks = _smp.balanceTasks(m, n);
			if (tasks.length < 2) {
				_blas2.dger(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++)
				jobs[job] = new DgerJob(t = tasks[job], n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
		} else {
			int[] tasks = _smp.balanceTasks(n, m);
			if (tasks.length < 2) {
				_blas2.dger(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAj, begx += t * incx, job++)
				jobs[job] = new DgerJob(m, t = tasks[job], alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
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

	private class DgerJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _alpha;
		private double[] _A, _x, _y;

		DgerJob(int m, int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy, double[] A,
				int begA, int incAi, int incAj) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.dger(_m, _n, _alpha, _x, _begx, _incx, _y, _begy, _incy, _A, _begA, _incAi, _incAj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}


	public void sgemv(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
			int incx, float beta, float[] y, int begy, int incy) throws BLASException {
		int[] tasks = _smp.balanceTasks(m, n);
		if (tasks.length < 2) {
			_blas2.sgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++) {
			jobs[job] = new SgemvJob(t = tasks[job], n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy,
					incy);
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

	private class SgemvJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private float _alpha, _beta;
		private float[] _A, _x, _y;

		SgemvJob(int m, int n, float alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx, int incx,
				float beta, float[] y, int begy, int incy) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.sgemv(_m, _n, _alpha, _A, _begA, _incAi, _incAj, _x, _begx, _incx, _beta, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void sger(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		Runnable[] jobs;
		if (incAj < incAi) {
			int[] tasks = _smp.balanceTasks(m, n);
			if (tasks.length < 2) {
				_blas2.sger(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++)
				jobs[job] = new SgerJob(t = tasks[job], n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
		} else {
			int[] tasks = _smp.balanceTasks(n, m);
			if (tasks.length < 2) {
				_blas2.sger(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAj, begx += t * incx, job++)
				jobs[job] = new SgerJob(m, t = tasks[job], alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
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

	private class SgerJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private float _alpha;
		private float[] _A, _x, _y;

		SgerJob(int m, int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A,
				int begA, int incAi, int incAj) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.sger(_m, _n, _alpha, _x, _begx, _incx, _y, _begy, _incy, _A, _begA, _incAi, _incAj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void zgemv(int m, int n, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
			int incx, ComplexI beta, double[] y, int begy, int incy) throws BLASException {
		int[] tasks = _smp.balanceTasks(m, n);
		if (tasks.length < 2) {
			_blas2.zgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++) {
			jobs[job] = new ZgemvJob(t = tasks[job], n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy,
					incy);
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

	private class ZgemvJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha, _beta;
		private double[] _A, _x, _y;

		ZgemvJob(int m, int n, ComplexI alpha, double[] A, int begA, int incAi, int incAj, double[] x, int begx,
				int incx, ComplexI beta, double[] y, int begy, int incy) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.zgemv(_m, _n, _alpha, _A, _begA, _incAi, _incAj, _x, _begx, _incx, _beta, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void zgerc(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		Runnable[] jobs;
		if (incAj < incAi) {
			int[] tasks = _smp.balanceTasks(m, n);
			if (tasks.length < 2) {
				_blas2.zgerc(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++)
				jobs[job] = new ZgercJob(t = tasks[job], n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
		} else {
			int[] tasks = _smp.balanceTasks(n, m);
			if (tasks.length < 2) {
				_blas2.zgerc(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAj, begx += t * incx, job++)
				jobs[job] = new ZgercJob(m, t = tasks[job], alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
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

	private class ZgercJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha;
		private double[] _A, _x, _y;

		ZgercJob(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
				double[] A, int begA, int incAi, int incAj) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.zgerc(_m, _n, _alpha, _x, _begx, _incx, _y, _begy, _incy, _A, _begA, _incAi, _incAj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void zgeru(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
			double[] A, int begA, int incAi, int incAj) throws BLASException {
		Runnable[] jobs;
		if (incAj < incAi) {
			int[] tasks = _smp.balanceTasks(m, n);
			if (tasks.length < 2) {
				_blas2.zgeru(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++)
				jobs[job] = new ZgeruJob(t = tasks[job], n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
		} else {
			int[] tasks = _smp.balanceTasks(n, m);
			if (tasks.length < 2) {
				_blas2.zgeru(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAj, begx += t * incx, job++)
				jobs[job] = new ZgeruJob(m, t = tasks[job], alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
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

	private class ZgeruJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha;
		private double[] _A, _x, _y;

		ZgeruJob(int m, int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy,
				double[] A, int begA, int incAi, int incAj) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.zgeru(_m, _n, _alpha, _x, _begx, _incx, _y, _begy, _incy, _A, _begA, _incAi, _incAj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void cgemv(int m, int n, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
			int incx, ComplexI beta, float[] y, int begy, int incy) throws BLASException {
		int[] tasks = _smp.balanceTasks(m, n);
		if (tasks.length < 2) {
			_blas2.cgemv(m, n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++) {
			jobs[job] = new CgemvJob(t = tasks[job], n, alpha, A, begA, incAi, incAj, x, begx, incx, beta, y, begy,
					incy);
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

	private class CgemvJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha, _beta;
		private float[] _A, _x, _y;

		CgemvJob(int m, int n, ComplexI alpha, float[] A, int begA, int incAi, int incAj, float[] x, int begx,
				int incx, ComplexI beta, float[] y, int begy, int incy) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_beta = beta;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.cgemv(_m, _n, _alpha, _A, _begA, _incAi, _incAj, _x, _begx, _incx, _beta, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void cgerc(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		Runnable[] jobs;
		if (incAj < incAi) {
			int[] tasks = _smp.balanceTasks(m, n);
			if (tasks.length < 2) {
				_blas2.cgerc(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++)
				jobs[job] = new CgercJob(t = tasks[job], n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
		} else {
			int[] tasks = _smp.balanceTasks(n, m);
			if (tasks.length < 2) {
				_blas2.cgerc(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAj, begx += t * incx, job++)
				jobs[job] = new CgercJob(m, t = tasks[job], alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
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

	private class CgercJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha;
		private float[] _A, _x, _y;

		CgercJob(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A,
				int begA, int incAi, int incAj) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.cgerc(_m, _n, _alpha, _x, _begx, _incx, _y, _begy, _incy, _A, _begA, _incAi, _incAj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	public void cgeru(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy,
			float[] A, int begA, int incAi, int incAj) throws BLASException {
		Runnable[] jobs;
		if (incAj < incAi) {
			int[] tasks = _smp.balanceTasks(m, n);
			if (tasks.length < 2) {
				_blas2.cgeru(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAi, begy += t * incy, job++)
				jobs[job] = new CgeruJob(t = tasks[job], n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
		} else {
			int[] tasks = _smp.balanceTasks(n, m);
			if (tasks.length < 2) {
				_blas2.cgeru(m, n, alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
				return;
			}
			jobs = new Runnable[tasks.length];
			for (int job = 0, t = tasks[0]; job < tasks.length; begA += t * incAj, begx += t * incx, job++)
				jobs[job] = new CgeruJob(m, t = tasks[job], alpha, x, begx, incx, y, begy, incy, A, begA, incAi, incAj);
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

	private class CgeruJob implements Runnable {
		private int _m, _n;
		private int _begA, _incAi, _incAj;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha;
		private float[] _A, _x, _y;

		CgeruJob(int m, int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy, float[] A,
				int begA, int incAi, int incAj) {
			_m = m;
			_n = n;
			_alpha = alpha;
			_A = A;
			_begA = begA;
			_incAi = incAi;
			_incAj = incAj;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas2.cgeru(_m, _n, _alpha, _x, _begx, _incx, _y, _begy, _incy, _A, _begA, _incAi, _incAj);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

}
