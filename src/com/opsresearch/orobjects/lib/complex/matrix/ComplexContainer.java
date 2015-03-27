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

package com.opsresearch.orobjects.lib.complex.matrix;

import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public class ComplexContainer implements ComplexContainerI, java.io.Serializable {
	private static final long serialVersionUID = 1L;
	protected static double _globalEpsilon = 1.0E-6;

	public static double getGlobalEpsilon() {
		return _globalEpsilon;
	}

	public static void setGlobalEpsilon(double epsilon) {
		_globalEpsilon = epsilon;
	}

	protected double _epsilon;

	public ComplexContainer() {
		_epsilon = _globalEpsilon;
	}

	public ComplexContainer(double epsilon) {
		_epsilon = epsilon;
	}

	public double getEpsilon() {
		return _epsilon;
	}

	public void setEpsilon(double epsilon) {
		_epsilon = epsilon;
	}

	public boolean equals(Complex a, Complex b) {
		double d = a.real - b.real;
		if (d < 0.0)
			d = -d;
		if (d > _epsilon)
			return false;
		d = a.imag - b.imag;
		if (d < 0.0)
			d = -d;
		return d <= _epsilon;
	}

	public boolean equals(ComplexI a, ComplexI b) {
		double d = a.getReal() - b.getReal();
		if (d < 0.0)
			d = -d;
		if (d > _epsilon)
			return false;
		d = a.getImag() - b.getImag();
		if (d < 0.0)
			d = -d;
		return d <= _epsilon;
	}

}
