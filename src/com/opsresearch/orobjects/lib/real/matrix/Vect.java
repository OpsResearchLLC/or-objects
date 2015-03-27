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

package com.opsresearch.orobjects.lib.real.matrix;

import java.util.Iterator;

public abstract class Vect extends RealContainer implements VectorI,
		java.io.Serializable {
	private static final long serialVersionUID = 1L;

	public Vect() {
	}

	public Vect(double epsilon) {
		super(epsilon);
	}

	public void setElements(VectorI values) {
		for (Iterator<VectorElementI> e = values.elements(); e
				.hasNext();) {
			VectorElementI elem = e.next();
			int idx = elem.getIndex();
			if (idx < size())
				setElementAt(idx, elem.getValue());
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

		if (sizeOfElements() == size()) {
			for (Iterator<VectorElementI> e = elements(); e
					.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(vector.elementAt(elem.getIndex()), elem.getValue()))
					return false;
			}
		} else if (vector.sizeOfElements() == vector.size()) {
			for (Iterator<VectorElementI> e = vector.elements(); e
					.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(elementAt(elem.getIndex()), elem.getValue()))
					return false;
			}
		} else {
			for (Iterator<VectorElementI> e = elements(); e
					.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(vector.elementAt(elem.getIndex()), elem.getValue()))
					return false;
			}
			for (Iterator<VectorElementI> e = vector.elements(); e
					.hasNext();) {
				VectorElementI elem = e.next();
				if (!equals(elementAt(elem.getIndex()), elem.getValue()))
					return false;
			}
		}

		return true;
	}

	public double sum() {
		return sum(0, size());
	}

	public double sumOfSquares() {
		return sumOfSquares(0, size());
	}

	public double sumOfSquaredDifferences(double scaler) {
		return sumOfSquaredDifferences(0, size(), scaler);
	}

	public double sum(int begin) {
		return sum(begin, size());
	}

	public double sumOfSquares(int begin) {
		return sumOfSquares(begin, size());
	}

	public double sumOfSquaredDifferences(int begin, double scaler) {
		return sumOfSquaredDifferences(begin, size(), scaler);
	}

	public double maximumAbsoluteValue() {
		return maximumAbsoluteValue(0, size());
	}

	public double maximumAbsoluteValue(int begin) {
		return maximumAbsoluteValue(begin, size());
	}

	public double maximumValue() {
		return maximumAbsoluteValue(0, size());
	}

	public double maximumValue(int begin) {
		return maximumAbsoluteValue(begin, size());
	}

	public String toString() {
		String str = "Vector[" + size() + "] = \n";
		for (Iterator<VectorElementI> e = elements(); e.hasNext(); ) {
			VectorElementI element = e.next();
			str += "[" + element.getIndex() + "] " + element.getValue() + "\n";
		}
		return str;
	}

}
