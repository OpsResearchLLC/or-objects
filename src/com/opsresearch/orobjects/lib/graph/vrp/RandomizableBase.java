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

package com.opsresearch.orobjects.lib.graph.vrp;

import com.opsresearch.orobjects.lib.random.RandomI;

public class RandomizableBase extends ConstructBase {
	protected int _strength;
	protected RandomI _random;

	public RandomizableBase() {
		_strength = 0;
		_random = null;
	}

	public RandomizableBase(com.opsresearch.orobjects.lib.graph.tsp.ImproveI improveSubalgorithm) {
		super(improveSubalgorithm);
		_strength = 0;
		_random = null;
	}

	public RandomizableBase(RandomI random) {
		_strength = 0;
		_random = random;
	}

	public void setRandom(RandomI random) {
		_random = random;
	}

	public RandomI getRandom() {
		return _random;
	}

	public void setStrength(int strength) {
		_strength = strength;
	}

}
