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

package com.opsresearch.orobjects.lib;

import java.math.BigInteger;

public class BigRational {

	private final BigInteger num;
	private final BigInteger den;

	public BigRational(long num) {
		this(BigInteger.valueOf(num));
	}

	public BigRational(long num, long den) {
		this(BigInteger.valueOf(num), BigInteger.valueOf(den));
	}

	public BigRational(BigInteger num) {
		this.num = num;
		this.den = BigInteger.ONE;
	}

	public BigRational(BigInteger num, BigInteger den) {
		if (den.equals(BigInteger.ZERO))
			throw new LibError("Can't have a zero den");

		if (den.compareTo(BigInteger.ONE) < 0) {
			num = num.negate();
			den = den.negate();
		}
		BigInteger gcd = num.gcd(den);
		if (!gcd.equals(BigInteger.ONE)) {
			num = num.divide(gcd);
			den = den.divide(gcd);
		}
		this.num = num;
		this.den = den;
	}

	public String toString() {
		return "" + num + "/" + den;
	}

	public boolean equals(Object other) {
		return this == other || other instanceof BigRational && ((BigRational) other).num().equals(num) && ((BigRational) other).den().equals(den);
	}

	public int compareTo(BigRational o) {
		BigInteger ad = num.multiply(((BigRational) o).den());
		BigInteger bc = den.multiply(((BigRational) o).num());
		return ad.compareTo(bc);
	}

	public int hashCode() {
		return num.hashCode() + den.hashCode();
	}

	public BigRational add(BigRational addend) {
		BigInteger ad = num.multiply(((BigRational) addend).den());
		BigInteger bc = den.multiply(((BigRational) addend).num());
		BigInteger bd = den.multiply(((BigRational) addend).den());
		BigRational rational = new BigRational(ad.add(bc), bd);
		return rational;
	}

	public BigRational sub(BigRational subtrahend) {
		BigInteger ad = num.multiply(((BigRational) subtrahend).den());
		BigInteger bc = den.multiply(((BigRational) subtrahend).num());
		BigInteger bd = den.multiply(((BigRational) subtrahend).den());
		BigRational BigRational = new BigRational(ad.subtract(bc), bd);
		return BigRational;
	}

	public BigRational mul(BigRational factor) {
		BigInteger ac = num.multiply(((BigRational) factor).num());
		BigInteger bd = den.multiply(((BigRational) factor).den());
		BigRational BigRational = new BigRational(ac, bd);
		return BigRational;
	}

	public BigRational div(BigRational dividend) {
		BigInteger ad = num.multiply(((BigRational) dividend).den());
		BigInteger bc = den.multiply(((BigRational) dividend).num());
		BigRational BigRational = new BigRational(ad, bc);
		return BigRational;
	}

	public BigRational abs() {
		if (num.compareTo(BigInteger.ZERO) < 0)
			new BigRational(num.negate(), den);
		return this;
	}

	public BigRational neg() {
		return new BigRational(num.negate(), den);
	}

	public BigInteger num() {
		return num;
	}

	public BigInteger den() {
		return den;
	}

}
