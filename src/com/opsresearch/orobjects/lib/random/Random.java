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

package com.opsresearch.orobjects.lib.random;



public class Random implements RandomI {

	private RandomI _random;

	public Random(){
	}
	
	public Random(long seed){
		_random = new LinearCongruential(seed);
	}
	
	public void setRandom(RandomI random) {
		_random = random;
	}

	public RandomI getRandom() {
		if (_random == null)
			_random = new LinearCongruential();
		return _random;
	}

	public void setSeed(long seed) {
		getRandom().setSeed(seed);
	}

	public int sizeOfBits() {
		return getRandom().sizeOfBits();
	}

	public long next() {
		return getRandom().next();
	}

	public double nextDouble() {
		return getRandom().nextDouble();
	}

}
