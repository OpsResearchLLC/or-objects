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

package com.opsresearch.orobjects.lib.stat.survival;

import com.opsresearch.orobjects.lib.real.matrix.VectorI;

public class Sample implements Comparable<Sample> {

	private final double time;
	private final int censored;
	private final int deaths;
	private final VectorI factors;

	public Sample(double time, boolean censored) {
		this(time, censored, null);
	}

	public Sample(double time, boolean censored, VectorI factors) {
		this.time = time;
		this.factors = factors;
		if (censored) {
			this.deaths = 0;
			this.censored = 1;
		} else {
			this.deaths = 1;
			this.censored = 0;
		}
	}

	public Sample(double time, int censored, int deaths) {
		this(time, censored, deaths, null);
	}

	public Sample(double time, int censored, int deaths, VectorI factors) {
		this.time = time;
		this.factors = factors;
		this.censored = censored;
		this.deaths = deaths;
	}

	public double getTime() {
		return time;
	}

	public VectorI getFactors() {
		return factors;
	}

	public int getCensored() {
		return censored;
	}

	public int getDeaths() {
		return deaths;
	}

	@Override
	public int compareTo(Sample o) {
		return time < o.time ? -1 : time > o.time ? 1 : 0;
	}

}
