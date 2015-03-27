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

import com.opsresearch.orobjects.lib.blas.BLAS1I;
import com.opsresearch.orobjects.lib.blas.BLASException;
import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;
import com.opsresearch.orobjects.lib.real.Real;
import com.opsresearch.orobjects.lib.smp.Smp;

public class BLAS1 implements BLAS1I {
	private Smp _smp;
	private BLAS1I _blas1 = null;
	private BLASException _exception = null;

	public BLAS1() {
		_smp = new Smp(100);
		_blas1 = new com.opsresearch.orobjects.lib.blas.java.BLAS1();
	}

	public BLAS1(BLAS1I blas1) {
		_smp = new Smp(100);
		_blas1 = blas1;
	}

	public Smp getSmp() {
		return _smp;
	}

	public void setSmp(Smp smp) {
		_smp = smp;
	}


	@Override
	public double dasum(int n, double[] x, int begx, int incx) throws BLASException {
		double sum = 0;
		if (n < 1)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		if (incx == 0)
			return n * x[begx];
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.dasum(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new DasumJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((DasumJob) jobs[job])._sum;
		return sum;
	}

	private class DasumJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private double _sum;
		private double[] _x;

		DasumJob(int n, double[] x, int begx, int incx) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_sum = _blas1.dasum(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public int idamax(int n, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		int off = begx;
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.idamax(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new IdamaxJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;

		int idx;
		int bidx = idx = ((IdamaxJob) jobs[0])._begx + incx * ((IdamaxJob) jobs[0])._idx;
		for (int job = 1; job < tasks.length; job++) {
			idx = ((IdamaxJob) jobs[job])._begx + incx * ((IdamaxJob) jobs[job])._idx;
			if (Math.abs(x[idx]) > Math.abs(x[bidx]))
				bidx = idx;
		}

		return (bidx - off) / incx;
	}

	private class IdamaxJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _idx;
		private double[] _x;

		IdamaxJob(int n, double[] x, int begx, int incx) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_idx = _blas1.idamax(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public int izamax(int n, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		int off = begx;
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.izamax(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new IzamaxJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;

		int bidx = 2 * ((IzamaxJob) jobs[0])._begx + incx * ((IzamaxJob) jobs[0])._idx;
		for (int job = 1; job < tasks.length; job++) {
			int idx = 2 * ((IzamaxJob) jobs[job])._begx + incx * ((IzamaxJob) jobs[job])._idx;
			if (Math.abs(x[idx]) + Math.abs(x[idx + 1]) > Math.abs(x[bidx]) + Math.abs(x[bidx + 1]))
				bidx = idx;
		}

		return (bidx / 2 - off) / incx;
	}

	private class IzamaxJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _idx;
		private double[] _x;

		IzamaxJob(int n, double[] x, int begx, int incx) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_idx = _blas1.izamax(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public int isamax(int n, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		int off = begx;
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.isamax(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new IsamaxJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;

		int bidx = ((IsamaxJob) jobs[0])._begx + incx * ((IsamaxJob) jobs[0])._idx;
		for (int job = 1; job < tasks.length; job++) {
			int idx = ((IsamaxJob) jobs[job])._begx + incx * ((IsamaxJob) jobs[job])._idx;
			if (Math.abs(x[idx]) > Math.abs(x[bidx]))
				bidx = idx;
		}

		return (bidx - off) / incx;
	}

	private class IsamaxJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _idx;
		private float[] _x;

		IsamaxJob(int n, float[] x, int begx, int incx) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_idx = _blas1.isamax(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public int icamax(int n, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return -1;
		if (n == 1)
			return 0;
		int off = begx;
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.icamax(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new IcamaxJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;

		int bidx = 2 * ((IcamaxJob) jobs[0])._begx + incx * ((IcamaxJob) jobs[0])._idx;
		for (int job = 1; job < tasks.length; job++) {
			int idx = 2 * ((IcamaxJob) jobs[job])._begx + incx * ((IcamaxJob) jobs[job])._idx;
			if (Math.abs(x[idx]) + Math.abs(x[idx + 1]) > Math.abs(x[bidx]) + Math.abs(x[bidx + 1]))
				bidx = idx;
		}

		return (bidx / 2 - off) / incx;
	}

	private class IcamaxJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _idx;
		private float[] _x;

		IcamaxJob(int n, float[] x, int begx, int incx) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_idx = _blas1.icamax(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public double dzasum(int n, double[] x, int begx, int incx) throws BLASException {
		double sum = 0;
		if (n < 1)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		if (incx == 0)
			return n * x[begx];
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.dzasum(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new DzasumJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((DzasumJob) jobs[job])._sum;
		return sum;
	}

	private class DzasumJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private double _sum;
		private double[] _x;

		DzasumJob(int n, double[] x, int begx, int incx) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_sum = _blas1.dzasum(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void daxpy(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;
		if (alpha == 0.0)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.daxpy(n, alpha, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new DaxpyJob(t = tasks[job], alpha, x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class DaxpyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _alpha;
		private double[] _x, _y;

		DaxpyJob(int n, double alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.daxpy(_n, _alpha, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void dcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.dcopy(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new DcopyJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class DcopyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double[] _x, _y;

		DcopyJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.dcopy(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void drotg(Real a, Real b, Real cos, Real sin) throws BLASException {
		_blas1.drotg(a, b, cos, sin);
	}

	@Override
	public void srotg(Real a, Real b, Real cos, Real sin) throws BLASException {
		_blas1.srotg(a, b, cos, sin);
	}

	@Override
	public double ddot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		double sum = 0;
		if (n < 1)
			return sum;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.ddot(n, x, begx, incx, y, begy, incy);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new DdotJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((DdotJob) jobs[job])._sum;
		return sum;
	}

	private class DdotJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _sum;
		private double[] _x, _y;

		DdotJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.ddot(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void drot(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.drot(n, x, begx, incx, y, begy, incy, cos, sin);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new DrotJob(t = tasks[job], x, begx, incx, y, begy, incy, cos, sin);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class DrotJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _cos, _sin;
		private double[] _x, _y;

		DrotJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, double cos, double sin) {
			_n = n;
			_sin = sin;
			_cos = cos;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.drot(_n, _x, _begx, _incx, _y, _begy, _incy, _cos, _sin);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void dscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.dscal(n, alpha, x, begx, incx);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new DscalJob(t = tasks[job], alpha, x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class DscalJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private double _alpha;
		private double[] _x;

		DscalJob(int n, double alpha, double[] x, int begx, int incx) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_blas1.dscal(_n, _alpha, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void zdscal(int n, double alpha, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.zdscal(n, alpha, x, begx, incx);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new ZdscalJob(t = tasks[job], alpha, x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ZdscalJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private double _alpha;
		private double[] _x;

		ZdscalJob(int n, double alpha, double[] x, int begx, int incx) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_blas1.zdscal(_n, _alpha, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void dswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.dswap(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new DswapJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class DswapJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double[] _x, _y;

		DswapJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.dswap(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public double dnrm2(int n, double[] x, int begx, int incx) throws BLASException {
		if (n <= 0 || incx <= 0)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		double sum = 0;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.dnrm2(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new Dnrm2Job(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++) {
			double nrm = ((Dnrm2Job) jobs[job])._nrm;
			sum += nrm * nrm;
		}
		return Math.sqrt(sum);
	}

	private class Dnrm2Job implements Runnable {
		private int _n;
		private int _begx, _incx;
		private double _nrm;
		private double[] _x;

		Dnrm2Job(int n, double[] x, int begx, int incx) {
			_n = n;
			_nrm = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_nrm = _blas1.dnrm2(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public double dznrm2(int n, double[] x, int begx, int incx) throws BLASException {
		if (n <= 0 || incx <= 0)
			return 0;
		if (n == 1)
			return _blas1.dznrm2(n, x, begx, incx);
		double sum = 0;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.dznrm2(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new Dznrm2Job(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++) {
			double nrm = ((Dznrm2Job) jobs[job])._nrm;
			sum += nrm * nrm;
		}
		return Math.sqrt(sum);
	}

	private class Dznrm2Job implements Runnable {
		private int _n;
		private int _begx, _incx;
		private double _nrm;
		private double[] _x;

		Dznrm2Job(int n, double[] x, int begx, int incx) {
			_n = n;
			_nrm = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_nrm = _blas1.dznrm2(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}


	@Override
	public float sasum(int n, float[] x, int begx, int incx) throws BLASException {
		float sum = 0;
		if (n < 1)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		if (incx == 0)
			return n * x[begx];
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.sasum(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new SasumJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((SasumJob) jobs[job])._sum;
		return sum;
	}

	private class SasumJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private float _sum;
		private float[] _x;

		SasumJob(int n, float[] x, int begx, int incx) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_sum = _blas1.sasum(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public float scasum(int n, float[] x, int begx, int incx) throws BLASException {
		float sum = 0;
		if (n < 1)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		if (incx == 0)
			return n * x[begx];
		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.scasum(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new ScasumJob(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((ScasumJob) jobs[job])._sum;
		return sum;
	}

	private class ScasumJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private float _sum;
		private float[] _x;

		ScasumJob(int n, float[] x, int begx, int incx) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_sum = _blas1.scasum(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void saxpy(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;
		if (alpha == 0.0)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.saxpy(n, alpha, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new SaxpyJob(t = tasks[job], alpha, x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class SaxpyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float _alpha;
		private float[] _x, _y;

		SaxpyJob(int n, float alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.saxpy(_n, _alpha, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void scopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.scopy(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new ScopyJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ScopyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float[] _x, _y;

		ScopyJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.scopy(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public float sdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		float sum = 0;
		if (n < 1)
			return sum;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.sdot(n, x, begx, incx, y, begy, incy);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new SdotJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((SdotJob) jobs[job])._sum;
		return sum;
	}

	private class SdotJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float _sum;
		private float[] _x, _y;

		SdotJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.sdot(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public double dsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		double sum = 0;
		if (n < 1)
			return sum;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.dsdot(n, x, begx, incx, y, begy, incy);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new DsdotJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((DsdotJob) jobs[job])._sum;
		return sum;
	}

	private class DsdotJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _sum;
		private float[] _x, _y;

		DsdotJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.dsdot(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public float sdsdot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		double sum = 0;
		if (n < 1)
			return 0;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.sdsdot(n, x, begx, incx, y, begy, incy);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new SdsdotJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			sum += ((SdsdotJob) jobs[job])._sum;
		return (float) sum;
	}

	private class SdsdotJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double _sum;
		private float[] _x, _y;

		SdsdotJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_sum = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.sdsdot(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void srot(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.srot(n, x, begx, incx, y, begy, incy, cos, sin);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new SrotJob(t = tasks[job], x, begx, incx, y, begy, incy, cos, sin);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class SrotJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float _cos, _sin;
		private float[] _x, _y;

		SrotJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, float cos, float sin) {
			_n = n;
			_sin = sin;
			_cos = cos;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.srot(_n, _x, _begx, _incx, _y, _begy, _incy, _cos, _sin);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void sscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.sscal(n, alpha, x, begx, incx);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new SscalJob(t = tasks[job], alpha, x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class SscalJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private float _alpha;
		private float[] _x;

		SscalJob(int n, float alpha, float[] x, int begx, int incx) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_blas1.sscal(_n, _alpha, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void csscal(int n, float alpha, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.csscal(n, alpha, x, begx, incx);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new CsscalJob(t = tasks[job], alpha, x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class CsscalJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private float _alpha;
		private float[] _x;

		CsscalJob(int n, float alpha, float[] x, int begx, int incx) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_blas1.csscal(_n, _alpha, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void sswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.sswap(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new SswapJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class SswapJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float[] _x, _y;

		SswapJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.sswap(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public float snrm2(int n, float[] x, int begx, int incx) throws BLASException {
		if (n <= 0 || incx <= 0)
			return 0;
		if (n == 1)
			return Math.abs(x[begx]);
		float sum = 0;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.snrm2(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new Snrm2Job(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++) {
			float nrm = ((Snrm2Job) jobs[job])._nrm;
			sum += nrm * nrm;
		}
		return (float) Math.sqrt(sum);
	}

	private class Snrm2Job implements Runnable {
		private int _n;
		private int _begx, _incx;
		private float _nrm;
		private float[] _x;

		Snrm2Job(int n, float[] x, int begx, int incx) {
			_n = n;
			_nrm = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_nrm = _blas1.snrm2(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public float scnrm2(int n, float[] x, int begx, int incx) throws BLASException {
		if (n <= 0 || incx <= 0)
			return 0;
		if (n == 1)
			return _blas1.scnrm2(n, x, begx, incx);
		float sum = 0;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2)
			return _blas1.scnrm2(n, x, begx, incx);
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new Scnrm2Job(t = tasks[job], x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++) {
			float nrm = ((Scnrm2Job) jobs[job])._nrm;
			sum += nrm * nrm;
		}
		return (float) Math.sqrt(sum);
	}

	private class Scnrm2Job implements Runnable {
		private int _n;
		private int _begx, _incx;
		private float _nrm;
		private float[] _x;

		Scnrm2Job(int n, float[] x, int begx, int incx) {
			_n = n;
			_nrm = 0;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_nrm = _blas1.scnrm2(_n, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}


	@Override
	public void zaxpy(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;
		if (alpha.equals(new Complex()))
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.zaxpy(n, alpha, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new ZaxpyJob(t = tasks[job], alpha, x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ZaxpyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha;
		private double[] _x, _y;

		ZaxpyJob(int n, ComplexI alpha, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.zaxpy(_n, _alpha, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void zcopy(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.zcopy(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new ZcopyJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ZcopyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double[] _x, _y;

		ZcopyJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.zcopy(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public Complex zdotu(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results) throws BLASException {
		if (results == null)
			results = new Complex();
		else {
			results.real = 0;
			results.imag = 0;
		}
		if (n < 1)
			return results;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.zdotu(n, x, begx, incx, y, begy, incy, results);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new ZdotuJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			results.add(((ZdotuJob) jobs[job])._sum);
		return results;
	}

	private class ZdotuJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private Complex _sum;
		private double[] _x, _y;

		ZdotuJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.zdotu(_n, _x, _begx, _incx, _y, _begy, _incy, new Complex());
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public Complex zdotc(int n, double[] x, int begx, int incx, double[] y, int begy, int incy, Complex results) throws BLASException {
		if (results == null)
			results = new Complex();
		else {
			results.real = 0;
			results.imag = 0;
		}
		if (n < 1)
			return results;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.zdotc(n, x, begx, incx, y, begy, incy, results);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new ZdotcJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			results.add(((ZdotcJob) jobs[job])._sum);
		return results;
	}

	private class ZdotcJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private Complex _sum;
		private double[] _x, _y;

		ZdotcJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.zdotc(_n, _x, _begx, _incx, _y, _begy, _incy, new Complex());
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void zscal(int n, ComplexI alpha, double[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.zscal(n, alpha, x, begx, incx);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new ZscalJob(t = tasks[job], alpha, x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ZscalJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private ComplexI _alpha;
		private double[] _x;

		ZscalJob(int n, ComplexI alpha, double[] x, int begx, int incx) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_blas1.zscal(_n, _alpha, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void zswap(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.zswap(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new ZswapJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class ZswapJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private double[] _x, _y;

		ZswapJob(int n, double[] x, int begx, int incx, double[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.zswap(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}


	@Override
	public void caxpy(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;
		if (alpha.equals(new Complex()))
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.caxpy(n, alpha, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new CaxpyJob(t = tasks[job], alpha, x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class CaxpyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private ComplexI _alpha;
		private float[] _x, _y;

		CaxpyJob(int n, ComplexI alpha, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.caxpy(_n, _alpha, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void ccopy(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.ccopy(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new CcopyJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class CcopyJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float[] _x, _y;

		CcopyJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.ccopy(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public Complex cdotu(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results) throws BLASException {
		if (results == null)
			results = new Complex();
		else {
			results.real = 0;
			results.imag = 0;
		}
		if (n < 1)
			return results;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.cdotu(n, x, begx, incx, y, begy, incy, results);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new CdotuJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			results.add(((CdotuJob) jobs[job])._sum);
		return results;
	}

	private class CdotuJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private Complex _sum;
		private float[] _x, _y;

		CdotuJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.cdotu(_n, _x, _begx, _incx, _y, _begy, _incy, new Complex());
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public Complex cdotc(int n, float[] x, int begx, int incx, float[] y, int begy, int incy, Complex results) throws BLASException {
		if (results == null)
			results = new Complex();
		else {
			results.real = 0;
			results.imag = 0;
		}
		if (n < 1)
			return results;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			return _blas1.cdotc(n, x, begx, incx, y, begy, incy, results);
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new CdotcJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
		for (int job = 0; job < tasks.length; job++)
			results.add(((CdotcJob) jobs[job])._sum);
		return results;
	}

	private class CdotcJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private Complex _sum;
		private float[] _x, _y;

		CdotcJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_sum = _blas1.cdotc(_n, _x, _begx, _incx, _y, _begy, _incy, new Complex());
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void cscal(int n, ComplexI alpha, float[] x, int begx, int incx) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.cscal(n, alpha, x, begx, incx);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, job++)
			jobs[job] = new CscalJob(t = tasks[job], alpha, x, begx, incx);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class CscalJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private ComplexI _alpha;
		private float[] _x;

		CscalJob(int n, ComplexI alpha, float[] x, int begx, int incx) {
			_n = n;
			_alpha = alpha;
			_x = x;
			_begx = begx;
			_incx = incx;
		}

		public void run() {
			try {
				_blas1.cscal(_n, _alpha, _x, _begx, _incx);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

	@Override
	public void cswap(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) throws BLASException {
		if (n < 1)
			return;

		int[] tasks = _smp.balanceTasks(n, 1);
		if (tasks.length < 2) {
			_blas1.cswap(n, x, begx, incx, y, begy, incy);
			return;
		}
		Runnable[] jobs = new Runnable[tasks.length];
		for (int job = 0, t = tasks[0]; job < tasks.length; begx += t * incx, begy += t * incy, job++)
			jobs[job] = new CswapJob(t = tasks[job], x, begx, incx, y, begy, incy);
		_exception = null;
		try {
			Smp.schedule(jobs.length, jobs, 1000);
		} catch (InterruptedException e) {
			throw new BLASException("Interrupted: " + e.getMessage());
		}
		if (_exception != null)
			throw _exception;
	}

	private class CswapJob implements Runnable {
		private int _n;
		private int _begx, _incx;
		private int _begy, _incy;
		private float[] _x, _y;

		CswapJob(int n, float[] x, int begx, int incx, float[] y, int begy, int incy) {
			_n = n;
			_x = x;
			_begx = begx;
			_incx = incx;
			_y = y;
			_begy = begy;
			_incy = incy;
		}

		public void run() {
			try {
				_blas1.cswap(_n, _x, _begx, _incx, _y, _begy, _incy);
			} catch (BLASException e) {
				_exception = e;
			}
		}
	}

}
