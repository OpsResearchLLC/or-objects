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

import java.util.Arrays;

public class Trial {

	private int numberAtRisk;
	private Sample[] samples;

	public Trial() {
		this(new Delegates());
	}

	public Trial(Delegates delegates) {
	}

	
	public Sample[] getSamples() {
		return samples;
	}

	
	public void setSamples(int numberAtRisk, Sample[] samples) {
		this.samples = samples.clone();
		this.numberAtRisk = numberAtRisk;
		Arrays.sort(this.samples);
	}

	
	public int getNumberAtRisk() {
		return numberAtRisk;
	}

}
