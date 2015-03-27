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

import com.opsresearch.orobjects.lib.real.matrix.MatrixI;
import com.opsresearch.orobjects.lib.real.matrix.VectorI;


public interface GeneralLinearModelI {
	public VectorI solve();

	public VectorI solve(boolean forceZeroIntercept);

	public VectorI solve(boolean forceZeroIntercept, int[] columnSelection);

	public VectorI getDependent();

	public MatrixI getIndependent();

	public void setSamples(VectorI dependent, MatrixI independent);

	public VectorI getCoefficients();

	public VectorI getStandardErrors();

	public MatrixI getInverseXX();

	public double getRSquared();

	public double getSST();

	public double getSSE();

	public double getSSR();

	public int getDFR();

	public int getDFE();

	public int getDFT();

	public double getMST();

	public double getMSE();

	public double getMSR();

}
