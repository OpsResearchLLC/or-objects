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


package com.opsresearch.orobjects.lib.stat.model;

import com.opsresearch.orobjects.lib.real.matrix.VectorI;
import com.opsresearch.orobjects.lib.util.QuickSort;

public class StepwiseLinear extends LinearRegressionBase implements
		LinearRegressionI {

	private int _histCnt = 0;
	private int[][] _histVars;
	private VectorI[] _histPvals;

	public int solve() {
		if (_enterPValue > _exitPValue)
			throw new Error(
					"The entering p-value must be <= the exiting to insure convergence.");

		_histCnt = 0;
		_histVars = new int[1024][];
		_histPvals = new VectorI[1024];

		boolean[] req = new boolean[_n];
		boolean[] sel = new boolean[_n];
		for (int i = 0; i < _requiredVariables.length; i++) {
			int col = _requiredVariables[i];
			req[col] = sel[col] = true;
		}
		setSelectedVariables(_requiredVariables);
		VectorI pv = getTPV();
		hist(pv);

		while (true) {
			int mini = -1;
			double minp = -1.0;
			int n = _selectedVariables.length;
			int[] old = _selectedVariables;
			int[] inc = new int[n + 1];
			for (int i = 0; i < n; i++)
				inc[i] = _selectedVariables[i];
			for (int i = 0; i < _enteringVariables.length; i++) {
				int col = _enteringVariables[i];
				if (sel[col])
					continue;
				inc[n] = col;
				setSelectedVariables(inc);
				pv = getTPV();
				double p = pv.elementAt(n);
				if (p <= _enterPValue) {
					if (mini == -1 || p < minp) {
						mini = i;
						minp = p;
					}
				}
			}

			if (mini == -1) {
				new QuickSort<int[]>().sort(old);
				setSelectedVariables(old);
				return n;
			}
			int col = _enteringVariables[mini];
			inc[n] = col;
			sel[col] = true;
			setSelectedVariables(inc);
			pv = getTPV();
			hist(pv);

			reverse(req, sel, pv);
		}

	}

	private void hist(VectorI pv) {
		if (_histCnt < _histVars.length) {
			_histVars[_histCnt] = _selectedVariables;
			_histPvals[_histCnt] = pv;
			_histCnt++;
		}
	}

	private void reverse(boolean[] req, boolean[] sel, VectorI pv) {
		while (true) {
			int maxi = -1;
			double maxp = -1.0;
			int n = _selectedVariables.length;
			for (int i = 0; i < n; i++) {
				int col = _selectedVariables[i];
				double p = pv.elementAt(i);
				if (p > _exitPValue && !req[col]) {
					if (maxi == -1 || p > maxp) {
						maxi = i;
						maxp = p;
					}
				}
			}
			if (maxi == -1)
				return;
			int col = _selectedVariables[maxi];
			sel[col] = false;
			int[] ns = new int[n - 1];
			for (int i = 0; i < maxi; i++)
				ns[i] = _selectedVariables[i];
			for (int i = maxi + 1; i < n; i++)
				ns[i - 1] = _selectedVariables[i];
			setSelectedVariables(ns);
			pv = getTPV();
			hist(pv);
		}
	}

	public String toString() {
		StringBuffer s = new StringBuffer(super.toString());
		s.append("\n");
		s.append("----------------------------\n");
		s.append("---- Stepwise Algorithm ----\n");
		s.append("----------------------------\n");
		s.append("\n");
		for (int i = 0; i < _histCnt; i++) {
			s.append("ITERATION-" + i + "\n");
			int[] row = _histVars[i];
			VectorI pv = _histPvals[i];
			for (int j = 0; j < row.length; j++) {
				s.append("  (" + row[j] + ") = " + pv.elementAt(j) + "\n");
			}
			s.append("\n");
		}
		return s.toString();
	}
}
