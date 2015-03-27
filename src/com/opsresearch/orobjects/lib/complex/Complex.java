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

package com.opsresearch.orobjects.lib.complex;

public class Complex implements ComplexI {
	public double real = 0.0;
	public double imag = 0.0;

	public Complex() {
	}

	public Complex(double r) {
		real = r;
	}

	public Complex(double r, double i) {
		real = r;
		imag = i;
	}

	public Complex(ComplexI c) {
		real = c.getReal();
		imag = c.getImag();
	}


	static public double modulus(double real, double imag) {
		double ra = real < 0.0 ? -real : real;
		double ia = imag < 0.0 ? -imag : imag;
		double t;
		if (ra >= ia) {
			t = imag / real;
			return ra * Math.sqrt(1.0 + t * t);
		} else {
			t = real / imag;
			return ia * Math.sqrt(1.0 + t * t);
		}
	}

	static public double modulus(ComplexI a) {
		return modulus(a.getReal(), a.getImag());
	}

	public double modulus() {
		return modulus(real, imag);
	}


	static public Complex invert(double real, double imag, Complex results) {
		double f, m;
		if (results == null)
			results = new Complex();
		if ((real < 0.0 ? -real : real) >= (imag < 0.0 ? -imag : imag)) {
			m = real + imag * (f = imag / real);
			results.real = 1.0 / m;
			results.imag = -f / m;
		} else {
			m = real * (f = real / imag) + imag;
			results.real = f / m;
			results.imag = -1.0 / m;
		}
		return results;
	}

	static public Complex invert(ComplexI a) {
		return invert(a, null);
	}

	static public Complex invert(ComplexI a, Complex results) {
		return invert(a.getReal(), a.getImag(), results);
	}

	public Complex invert() {
		return invert(real, imag, this);
	}


	static public Complex sqrt(double real, double imag, Complex results) {
		if (results == null)
			results = new Complex();
		double w, f;
		double c = real < 0.0 ? -real : real;
		double d = imag < 0.0 ? -imag : imag;
		if (real == 0.0 && imag == 0.0) {
			w = 0.0;
		} else if (c >= d) {
			f = imag / real;
			w = Math.sqrt(c) * Math.sqrt((1.0 + Math.sqrt(1.0 + f * f)) / 2.0);
		} else {
			f = real / imag;
			w = Math.sqrt(d) * Math.sqrt(((f < 0.0 ? -f : f) + Math.sqrt(1.0 + f * f)) / 2.0);
		}

		if (w == 0.0) {
			results.real = 0.0;
			results.imag = 0.0;
		} else if (real >= 0.0) {
			results.real = w;
			results.imag = imag / (w + w);
		} else {
			if (imag >= 0.0) {
				results.real = d / (w + w);
				results.imag = w;
			} else {
				results.real = d / (w + w);
				results.imag = -w;
			}
		}

		return results;
	}

	static public Complex sqrt(ComplexI a) {
		return sqrt(a, null);
	}

	static public Complex sqrt(ComplexI a, Complex results) {
		return sqrt(a.getReal(), a.getImag(), results);
	}

	public Complex sqrt() {
		return sqrt(real, imag, this);
	}


	public Complex conjugate() {
		imag = -imag;
		return this;
	}


	static public Complex add(double r1, double i1, double r2, double i2, Complex results) {
		if (results == null)
			results = new Complex();
		results.real = r1 + r2;
		results.imag = i1 + i2;
		return results;
	}

	static public Complex add(ComplexI a, ComplexI b, Complex results) {
		if (results == null)
			results = new Complex();
		results.real = a.getReal() + b.getReal();
		results.imag = a.getImag() + b.getImag();
		return results;
	}

	static public Complex add(ComplexI a, ComplexI b) {
		Complex results = new Complex();
		results.real = a.getReal() + b.getReal();
		results.imag = a.getImag() + b.getImag();
		return results;
	}

	public Complex add(double r, double i) {
		real += r;
		imag += i;
		return this;
	}

	public Complex add(double r) {
		real += r;
		return this;
	}

	public Complex add(ComplexI a) {
		real += a.getReal();
		imag += a.getImag();
		return this;
	}


	static public Complex subtract(double r1, double i1, double r2, double i2, Complex results) {
		if (results == null)
			results = new Complex();
		results.real = r1 - r2;
		results.imag = i1 - i2;
		return results;
	}

	static public Complex subtract(ComplexI a, ComplexI b, Complex results) {
		if (results == null)
			results = new Complex();
		results.real = a.getReal() - b.getReal();
		results.imag = a.getImag() - b.getImag();
		return results;
	}

	static public Complex subtract(ComplexI a, ComplexI b) {
		Complex results = new Complex();
		results.real = a.getReal() - b.getReal();
		results.imag = a.getImag() - b.getImag();
		return results;
	}

	public Complex subtract(double r, double i) {
		real -= r;
		imag -= i;
		return this;
	}

	public Complex subtract(double r) {
		real -= r;
		return this;
	}

	public Complex subtract(ComplexI a) {
		real -= a.getReal();
		imag -= a.getImag();
		return this;
	}


	static public Complex multiply(double r1, double i1, double r2, double i2, Complex results) {
		if (results == null)
			results = new Complex();
		if (i1 == 0.0) {
			results.real = r1 * r2;
			results.imag = r1 * i2;
		} else if (i2 == 0.0) {
			results.real = r1 * r2;
			results.imag = r2 * i1;
		} else {
			results.real = r1 * r2 - i1 * i2;
			results.imag = r1 * i2 + r2 * i1;
		}
		return results;
	}

	static public Complex multiply(ComplexI a, ComplexI b, Complex results) {
		return multiply(a.getReal(), a.getImag(), b.getReal(), b.getImag(), results);
	}

	static public Complex multiply(ComplexI a, ComplexI b) {
		return multiply(a.getReal(), a.getImag(), b.getReal(), b.getImag(), null);
	}

	public Complex multiply(double r, double i) {
		return multiply(real, imag, r, i, this);
	}

	public Complex multiply(double r) {
		return multiply(real, imag, r, 0, this);
	}

	public Complex multiply(ComplexI a) {
		return multiply(real, imag, a.getReal(), a.getImag(), this);
	}


	static public Complex divide(double r1, double i1, double r2, double i2, Complex results) {
		double f, m;
		if (results == null)
			results = new Complex();
		if ((r2 < 0.0 ? -r2 : r2) >= (i2 < 0.0 ? -i2 : i2)) {
			m = r2 + i2 * (f = i2 / r2);
			results.real = (r1 + i1 * f) / m;
			results.imag = (i1 - r1 * f) / m;
		} else {
			m = r2 * (f = r2 / i2) + i2;
			results.real = (r1 * f + i1) / m;
			results.imag = (i1 * f - r1) / m;
		}
		return results;
	}

	static public Complex divide(ComplexI a, ComplexI b, Complex results) {
		return divide(a.getReal(), a.getImag(), b.getReal(), b.getImag(), results);
	}

	static public Complex divide(ComplexI a, ComplexI b) {
		return divide(a.getReal(), a.getImag(), b.getReal(), b.getImag(), null);
	}

	public Complex divide(double r, double i) {
		return divide(real, imag, r, i, this);
	}

	public Complex divide(double r) {
		return divide(real, imag, r, 0, this);
	}

	public Complex divide(ComplexI a) {
		return divide(real, imag, a.getReal(), a.getImag(), this);
	}

	public Complex set(double r, double i) {
		real = r;
		imag = i;
		return this;
	}

	public Complex set(ComplexI a) {
		real = a.getReal();
		imag = a.getImag();
		return this;
	}

	public double getReal() {
		return real;
	}

	public double getImag() {
		return imag;
	}

	static public boolean equals(double r1, double i1, double r2, double i2, double epsilon) {
		double d, m;
		if (r1 == r2 && i1 == i2)
			return true;
		if (epsilon == 0.0)
			return false;

		d = r1 - r2;
		if (d < 0.0)
			d = -d;
		if (r1 < 0.0)
			r1 = -r1;
		if (r2 < 0.0)
			r2 = -r2;
		m = r1 > r2 ? r1 : r2;
		if (d > epsilon * m)
			return false;

		d = i1 - i2;
		if (d < 0.0)
			d = -d;
		if (i1 < 0.0)
			i1 = -i1;
		if (i2 < 0.0)
			i2 = -i2;
		m = i1 > i2 ? i1 : i2;
		if (d > epsilon * m)
			return false;

		return true;
	}

	static public boolean equals(ComplexI a, ComplexI b, double epsilon) {
		return equals(a.getReal(), a.getImag(), b.getReal(), b.getImag(), epsilon);
	}

	public boolean equals(double r, double i) {
		return real == r && imag == i;
	}

	public boolean equals(ComplexI a) {
		return real == a.getReal() && imag == a.getImag();
	}

	public boolean equals(double r, double i, double epsilon) {
		return equals(real, imag, r, i, epsilon);
	}

	public boolean equals(ComplexI a, double epsilon) {
		return equals(real, imag, a.getReal(), a.getImag(), epsilon);
	}

	public boolean equals(Object o) {
		if (o instanceof ComplexI)
			return equals((ComplexI) o);
		return false;
	}

	public String toString() {
		return "(" + real + ", " + imag + ")";
	}

}
