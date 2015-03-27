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

package com.opsresearch.orobjects.lib.real.approx;

import java.io.Serializable;

import com.opsresearch.orobjects.lib.real.function.FunctionI;

public class Approximations implements Serializable {

	private static final long serialVersionUID = 1L;

	private IntegrationI _integration;
	private InverseI _inverse;

	public double invert(FunctionI function, double x1, double x2, double y) throws ApproxException, AccuracyException {
		return getInverse().solve(function, x1, x2, y);
	}

	public double integrate(FunctionI function, double bound1, double bound2) throws AccuracyException {
		return getIntegration().solve(function, bound1, bound2);
	}

	public IntegrationI getIntegration() {
		if (_integration == null)
			_integration = new Integration();
		return _integration;
	}

	public void setIntegration(IntegrationI integration) {
		_integration = integration;
	}

	public InverseI getInverse() {
		if (_inverse == null)
			_inverse = new Inverse();
		return _inverse;
	}

	public void setInverse(InverseI inverse) {
		_inverse = inverse;
	}

}
