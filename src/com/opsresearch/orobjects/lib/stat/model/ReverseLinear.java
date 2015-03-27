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
import com.opsresearch.orobjects.lib.util.Array;

public class ReverseLinear extends LinearRegressionBase implements
		LinearRegressionI {

	private int _histCnt = 0;
	private int[][] _histVars;
	private VectorI[] _histPvals;

	public int solve() {
		_histCnt = 0;
		_histVars = new int[_selectedVariables.length][];
		_histPvals = new VectorI[_selectedVariables.length];

		boolean[] req = new boolean[_n];
		for (int i = 0; i < _requiredVariables.length; i++)
			req[_requiredVariables[i]] = true;
		setSelectedVariables(Array
				.union(_enteringVariables, _requiredVariables));
		VectorI pv = getTPV();
		hist(pv);

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
				return n;
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

	private void hist(VectorI pv) {
		if (_histCnt < _histVars.length) {
			_histVars[_histCnt] = _selectedVariables;
			_histPvals[_histCnt] = pv;
			_histCnt++;
		}
	}

	public String toString() {
		StringBuffer s = new StringBuffer(super.toString());
		s.append("\n");
		s.append("---------------------------\n");
		s.append("---- Reverse Algorithm ----\n");
		s.append("---------------------------\n");
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
