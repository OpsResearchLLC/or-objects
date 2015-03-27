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

import com.opsresearch.orobjects.lib.prob.Distributions;


public class Models extends Distributions {

	GeneralLinearModelI _generalLinearModel;
	LinearCorrelationI _linearCorrelation;
	LinearRegressionI _linearRegression;

	public GeneralLinearModelI getGeneralLinearModel() {
		if (_generalLinearModel == null)
			_generalLinearModel = new GeneralLinearModel();
		return _generalLinearModel;
	}

	public void setGeneralLinearModel(GeneralLinearModelI generalLinearModel) {
		this._generalLinearModel = generalLinearModel;
	}

	public LinearCorrelationI getLinearCorrelation() {
		if (_linearCorrelation == null)
			_linearCorrelation = new LinearCorrelation();
		return _linearCorrelation;
	}

	public void setLinearCorrelation(LinearCorrelationI linearCorrelation) {
		this._linearCorrelation = linearCorrelation;
	}

	public LinearRegressionI getLinearRegression() {
		if (_linearRegression == null)
			_linearRegression = new LinearRegression();
		return _linearRegression;
	}

	public void setLinearRegression(LinearRegressionI linearRegression) {
		this._linearRegression = linearRegression;
	}

}
