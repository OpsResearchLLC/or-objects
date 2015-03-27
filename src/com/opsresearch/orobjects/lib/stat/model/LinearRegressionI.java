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

public interface LinearRegressionI {
	public int solve();

	public int[] allVariables();

	public void setEnteringVariables(int[] variables);

	public void setRequiredVarables(int[] variables);

	public void setEnteringThreshold(double pValue);

	public double getEnteringThreshold();

	public void setExitingThreshold(double pValue);

	public double getExitingThreshold();

	public void setSelectedVariables(int[] variables);

	public double getF();

	public double getFPV();

	public VectorI getCoefficients();

	public VectorI getLowerBounds(double confidenceLevel);

	public VectorI getUpperBounds(double confidenceLevel);

	public VectorI getT();

	public VectorI getTPV();

	public double getTStatistic(double pValue);

	public int[] getSelectedVariables();

	public int[] getEnteringVariables();

	public int[] getRequiredVariables();

}
