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

package com.opsresearch.orobjects.lib.smp;

public class Smp {
	static private int _defaultMaxThreads = 1;

	static public int getDefaultMaxThreads() {
		return _defaultMaxThreads;
	}

	static public void setDefaultMaxThreads(int defaultMaxThreads) {
		_defaultMaxThreads = defaultMaxThreads;
	}

	protected int _minWork;
	protected int _maxThreads = _defaultMaxThreads;

	public Smp(int minWork) {
		_minWork = minWork;
	}

	public Smp(int maxThreads, int minWork) {
		_minWork = minWork;
		_maxThreads = maxThreads;
	}

	public void setMinWork(int minWork) {
		_minWork = minWork;
	}

	public void setMaxThreads(int maxThreads) {
		_maxThreads = maxThreads;
	}

	public int[] balanceTasks(int numTasks, int workPerTask) {
		int nthrd = Math.min(_maxThreads, numTasks);
		if (_minWork > 1) {
			int twork = numTasks * workPerTask;
			nthrd = Math.min(nthrd, twork / _minWork);
		}
		nthrd = Math.max(1, nthrd);
		int[] asgn = new int[nthrd];
		int per = numTasks / nthrd;
		int mod = numTasks % nthrd;
		for (int i = 0; i < mod; i++)
			asgn[i] = per + 1;
		for (int i = mod; i < nthrd; i++)
			asgn[i] = per;
		return asgn;
	}

	public static void schedule(int maxThreads, Runnable[] jobs, long millis) throws InterruptedException {
		int n = Math.min(jobs.length, maxThreads);
		if (n < 1)
			return;
		Thread[] threads = new Thread[n];
		int[] jobId = new int[n];
		for (int i = 0; i < n; i++) {
			jobId[i] = i;
			threads[i] = new Thread(jobs[i]);
			threads[i].start();
		}

		int next = n;

		for (;;) {
			int w = 0;
			while (w < n && threads[w] == null)
				w++;
			if (w >= n)
				return;
			threads[w].join(millis);
			for (int i = 0; i < n; i++) {
				if (threads[i] != null && !threads[i].isAlive()) {
					threads[i] = null;
					if (next < jobs.length) {
						threads[i] = new Thread(jobs[next++]);
						threads[i].start();
					}
				}
			}
		}
	}
}
