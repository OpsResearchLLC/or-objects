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

import java.util.Iterator;

import com.opsresearch.orobjects.lib.complex.Complex;
import com.opsresearch.orobjects.lib.complex.ComplexI;

public abstract class Vect extends ComplexContainer implements VectorI, java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public Vect() {
	}

	public Vect(double epsilon) {
		super(epsilon);
	}

	public void setElements(VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e.hasNext();) {
			VectorElementI elem = e.next();
			setElementAt(elem.getIndex(), elem.getValue());
		}
	}

	public boolean equals(Object o) {
		if (o instanceof VectorI)
			return equals((VectorI) o);
		return false;
	}

	public boolean equals(VectorI vector) {
		if (size() != vector.size())
			return false;
		Complex c = new Complex();

		if (sizeOfElements() == size()) {
			for (Iterator<VectorElementI> e = elements(); e.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(vector.elementAt(elem.getIndex(), c), elem.getValue()))
					return false;
			}
		} else if (vector.sizeOfElements() == vector.size()) {
			for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(elementAt(elem.getIndex(), c), elem.getValue()))
					return false;
			}
		} else {
			for (Iterator<VectorElementI> e = elements(); e.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(vector.elementAt(elem.getIndex(), c), elem.getValue()))
					return false;
			}
			for (Iterator<VectorElementI> e = vector.elements(); e.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(elementAt(elem.getIndex(), c), elem.getValue()))
					return false;
			}
		}

		return true;
	}

	public Complex sum() {
		return sum(0, size());
	}

	public Complex sumOfSquares() {
		return sumOfSquares(0, size());
	}

	public Complex sumOfSquaredDifferences(ComplexI scaler) {
		return sumOfSquaredDifferences(0, size(), scaler);
	}

	public Complex sum(int begin) {
		return sum(begin, size());
	}

	public Complex sumOfSquares(int begin) {
		return sumOfSquares(begin, size());
	}

	public Complex sumOfSquaredDifferences(int begin, ComplexI scaler) {
		return sumOfSquaredDifferences(begin, size(), scaler);
	}

	public String toString() {
		String str = "Vector[" + size() + "] = \n";
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI element = e.next();
			ComplexI value = element.getValue();
			str += "[" + element.getIndex() + "] (" + value.getReal() + ", " + value.getImag() + ")\n";
		}
		return str;
	}

}
